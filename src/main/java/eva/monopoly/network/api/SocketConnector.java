package eva.monopoly.network.api;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SocketConnector
{
	public final static Logger																							LOG					= LoggerFactory.getLogger(
			SocketConnector.class);

	final private static ExecutorService																				EXECUTOR			= Executors
			.newCachedThreadPool();
	final private static ExecutorService																				MESSAGE_DISPATCHER	= Executors
			.newSingleThreadExecutor();
	final private static int																							TIMEOUT				= 15 * 1000;

	private final Socket																								socket;
	private ObjectOutputStream																							out;
	private ObjectInputStream																							in;
	private Future<?>																									future;
	private Consumer<HandlerException>																					shutdownHandler;

	private final ConcurrentHashMap<Class<? extends ExchangeMessage>, ExchangeMessageHandle<? extends ExchangeMessage>>	handler				= new ConcurrentHashMap<>();

	public SocketConnector(final Socket socket, Consumer<HandlerException> shutdownHandler)
	{
		this.socket = socket;
		this.shutdownHandler = shutdownHandler;

		registerHandle(ExchangeMessage.class, (msg) -> LOG.warn("There was an unhandled message of type {}: {}", msg.getClass().getSimpleName(), msg.toString()));
	}

	public void establishConnection()
	{
		try
		{

			this.socket.setSoTimeout(TIMEOUT);
		}
		catch(SocketException e)
		{
			shutdownConnection("Konnte SoTimeout nicht setzen!", e);
			return;
		}

		try
		{
			out = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			shutdownConnection("Outputstream konnte nicht initialisiert werden!", e);
			return;
		}

		try
		{
			in = new ObjectInputStream(socket.getInputStream());
		}
		catch(IOException e)
		{
			shutdownConnection("Inputstream konnte nicht initialisiert werden!", e);
			return;
		}

		final Runnable runnable = () ->
		{
			LOG.debug("Start waiting for messages");
			while(!future.isCancelled())
			{
				try
				{
					final Object obj = in.readObject();
					LOG.debug("Received message of type: {}", obj.getClass().getSimpleName());
					solveMessage((ExchangeMessage) obj);
				}
				catch(InterruptedIOException e)
				{
					if(!future.isCancelled())
					{
						future.cancel(false);
						shutdownConnection("Unexpected interrupt", e);
					}
					return;
				}
				catch(SocketException e)
				{
					shutdownConnection("Socket wurde unerwartet geschlossen!", e);
					return;
				}
				catch(Exception e)
				{
					shutdownConnection("Fehler beim Empfangen der Nachricht", e);
					return;
				}
			}
		};

		future = EXECUTOR.submit(runnable);
	}

	public void closeConnection() throws IOException
	{
		out.flush();
		future.cancel(true);
		out.close();
		in.close();
		socket.close();
	}

	public Socket getSocket()
	{
		return socket;
	}

	public boolean sendMessage(final ExchangeMessage exchangeMessage)
	{
		try
		{
			LOG.debug("Waiting for sending Message");
			synchronized(out)
			{
				out.writeObject(exchangeMessage);
				LOG.debug("Send Message of type: {}", exchangeMessage.getClass().getSimpleName());
			}
			return true;
		}
		catch(Exception e)
		{
			shutdownConnection("Konnte Nachricht nicht senden!", e);
		}
		return false;
	}

	private void shutdownConnection(String reason, Throwable e)
	{
		shutdownHandler.accept(new HandlerException(reason, e));
	}

	private void solveMessage(ExchangeMessage obj)
	{
		Class<?> clazz = obj.getClass();
		ExchangeMessageHandle<? extends ExchangeMessage> wrapper = handler.get(clazz);
		while(wrapper == null)
		{
			clazz = clazz.getSuperclass();
			if(clazz == null)
			{
				LOG.error("No handler registered for type hierarchy of class: {}. This should never happen!", obj.getClass().getSimpleName());
				return;
			}
			wrapper = handler.get(clazz.getSuperclass());
		}
		final ExchangeMessageHandle<? extends ExchangeMessage> finWrapper = wrapper;
		MESSAGE_DISPATCHER.execute(() -> finWrapper.handle(obj));
	}

	public <T extends ExchangeMessage> void registerHandle(Class<T> clazz, Consumer<T> consumer)
	{
		final ExchangeMessageHandle<T> wrapper = new ExchangeMessageHandle<T>(clazz, consumer);
		handler.put(clazz, wrapper);
	}
}
