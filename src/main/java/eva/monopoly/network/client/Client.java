package eva.monopoly.network.client;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import eva.monopoly.network.api.ExchangeObject;
import eva.monopoly.network.api.ExchangeType;


public class Client
{
	public final static Logger				LOGGER	= Logger.getLogger(Client.class.getName());

	private static ServerHandlerRunnable	connectedServerSocket;

	private static boolean					restart;

	private Client()
	{
	}

	public static void setRestart()
	{
		restart = true;
	}

	public static void disconnectFromProxy()
	{
		try
		{
			connectedServerSocket.getSocket().close();
		}
		catch(Exception e)
		{
		}
		connectedServerSocket = null;
	}

	public static void connectToProxy()
	{
		try
		{
			connectedServerSocket = new ServerHandlerRunnable(new Socket(Config.host, Config.port));
			Utils.executeAsync(new HeartBeat(), 0, 200);
		}
		catch(UnknownHostException e)
		{
			ExchangeClient.consoleExchangeClient(LogLevel.ERROR, null, "Server " + Config.host + " ungültiger Host");
			restartIfNoProblem();
			return;
		}
		catch(IOException e)
		{
			ExchangeClient.consoleExchangeClient(LogLevel.ERROR, null, "Fehler bei Initialisierung des Servers: " + Config.host);
			restartIfNoProblem();
			return;
		}
		ExchangeClient.consoleExchangeClient(LogLevel.INFO, null, "Server hinzugefügt: ");
	}

	public static void sendMessage(final String receiver, final String channel, final String message)
	{
		sendMessage(new ExchangeObject(Network.getThisServer().getName(), receiver, ExchangeType.DATA, channel, message));
	}

	private static void sendMessage(final ExchangeObject ExchangeObject)
	{
		if(connectedServerSocket != null)
		{
			connectedServerSocket.sendMessage(ExchangeObject);
		}
	}

	public static void restartIfNoProblem()
	{
		if(restart)
		{
			return;
		}
		try
		{
			Network.getThisServer().setServerStatus(ServerStatus.LOCKDOWN);
		}
		catch(LockDownException e)
		{
		}

		disconnectFromProxy();

		final Runnable runnable = () ->
		{

			try
			{
				final Socket socket = new Socket(Config.host, Config.port);
				if(socket.isConnected())
				{
					socket.close();
					Utils.restart();
					return;
				}
				socket.close();
			}
			catch(IOException e)
			{
			}

		};

		switch(Network.getThisServer().getServerMode())
		{
			case BUNGEE:
			{
				UtilsBungee.executeAsync(runnable, 0, 30, TimeUnit.SECONDS);
				break;
			}
			case BUKKIT:
			{
				UtilsBukkit.executeSync(runnable, 0, 600);
				break;
			}
		}
	}

	public static ServerHandlerRunnable getConnectedServerSocket()
	{
		return connectedServerSocket;
	}

	public static void resolveData(ExchangeObject obj)
	{

	}
}
