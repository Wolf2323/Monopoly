package eva.monopoly.network.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public abstract class AbstractHandlerRunnable
{
	private final Socket	socket;

	private PrintWriter		out;

	private String			name	= "";

	public AbstractHandlerRunnable(final Socket socket, String name)
	{
		this.socket = socket;

		try
		{
			this.socket.setSoTimeout(15 * 1000);
		}
		catch(SocketException e)
		{
			shutdownConnection("Konnte SoTimeout nicht setzen!");
			return;
		}

		try
		{
			out = new PrintWriter(socket.getOutputStream(), true);
		}
		catch(IOException e)
		{
			shutdownConnection("Outputstream konnte nicht initialisiert werden!");
			return;
		}

		try
		{
			final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			final Runnable recieveRunnable = () ->
			{
				while(true)
				{
					try
					{
						final String msg = in.readLine();
						if(msg == null)
						{
							throw new SocketTimeoutException();
						}
						solveMessage(ExchangeObject.fromString(msg));
					}
					catch(SocketException | SocketTimeoutException e)
					{
						shutdownConnection(null);
						break;
					}
					catch(Exception e)
					{
						shutdownConnection("Konnte Nachricht nicht empfangen!");
						e.printStackTrace();
						break;
					}
				}
			};
			new Thread(recieveRunnable).start();
		}
		catch(IOException e)
		{
			shutdownConnection("Inputstream konnte nicht initialisiert werden!");
			return;
		}

		sendMessage(new ExchangeObject(name, ExchangeType.LOGIN, null, null));
	}

	public Socket getSocket()
	{
		return socket;
	}

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public boolean sendMessage(final ExchangeObject object)
	{
		try
		{
			synchronized(this)
			{
				out.println(object.toString());
			}
			return true;
		}
		catch(Exception e)
		{
			shutdownConnection("Konnte Nachricht nicht senden!");
		}
		return false;
	}

	protected abstract void shutdownConnection(String reason);

	protected abstract void solveMessage(ExchangeObject obj);

}
