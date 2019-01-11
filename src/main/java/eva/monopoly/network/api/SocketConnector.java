package eva.monopoly.network.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import eva.monopoly.network.api.messages.NameInfo;


public abstract class SocketConnector
{
	final private static ScheduledExecutorService																		SCHEDULED_EXECUTOR	= new ScheduledThreadPoolExecutor(
			4);
	final private static ExecutorService																				MESSAGE_DISPATCHER	= new ThreadPoolExecutor(2, 8,
			30, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
	final private static long																							PERIOD				= 50;

	private final Socket																								socket;
	private ObjectOutputStream																							out;
	private ObjectInputStream																							in;
	private ScheduledFuture<?>																							future;
	private String																										remoteName			= "Undefined";
	private Consumer<HandlerException>																					shutdownHandler;

	private final ConcurrentHashMap<Class<? extends ExchangeMessage>, ExchangeMessageHandle<? extends ExchangeMessage>>	handler				= new ConcurrentHashMap<>();

	public SocketConnector(final Socket socket, Consumer<HandlerException> shutdownHandler)
	{
		this.socket = socket;
		this.shutdownHandler = shutdownHandler;

		registerHandle(NameInfo.class, (nameInfo) -> remoteName = nameInfo.getName());
	}

	public void establishConnection(String name)
	{
		try
		{
			this.socket.setSoTimeout(15 * 1000);
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
			try
			{
				while(in.available() > 0 && !future.isCancelled())
				{
					try
					{
						final Object obj = in.readObject();
						solveMessage((ExchangeMessage) obj);
					}
					catch(SocketException | SocketTimeoutException e)
					{
						shutdownConnection("Socket wurde unerwartet geschlossen!", e);

					}
					catch(Exception e)
					{
						shutdownConnection("Fehler beim Empfangen der Nachricht", e);
					}
				}
			}
			catch(IOException e)
			{
				shutdownConnection("Fehler beim prüfen von Nachrichten", e);
			}
		};

		future = SCHEDULED_EXECUTOR.scheduleAtFixedRate(runnable, 1, PERIOD, TimeUnit.MILLISECONDS);

		sendMessage(new NameInfo(name));
	}

	public void closeConnection() throws IOException
	{
		future.cancel(false);
		out.close();
		in.close();
		socket.close();
	}

	public Socket getSocket()
	{
		return socket;
	}

	public String getRemoteName()
	{
		return remoteName;
	}

	public boolean sendMessage(final ExchangeMessage exchangeMessage)
	{
		try
		{
			synchronized(out)
			{
				out.writeObject(exchangeMessage);
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
		final ExchangeMessageHandle<? extends ExchangeMessage> wrapper = handler.get(obj.getClass());

		MESSAGE_DISPATCHER.execute(() -> wrapper.handle(obj));
	}

	public <T extends ExchangeMessage> void registerHandle(Class<T> clazz, Consumer<T> consumer)
	{
		final ExchangeMessageHandle<T> wrapper = new ExchangeMessageHandle<T>(clazz, consumer);
		handler.put(clazz, wrapper);
	}
}
