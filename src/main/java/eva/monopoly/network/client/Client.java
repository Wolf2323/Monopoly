package eva.monopoly.network.client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import eva.monopoly.network.api.ExchangeObject;
import eva.monopoly.network.api.ExchangeType;
import eva.monopoly.network.api.SocketConnector;


public class Client
{
	public final static Logger		LOGGER	= Logger.getLogger(Client.class.getName());

	private static SocketConnector	socketConnector;

	private Client()
	{
		// registerHandle
	}

	public static void disconnectFromProxy()
	{
		try
		{
			socketConnector.closeConnection();
		}
		catch(Exception e)
		{
		}
		socketConnector = null;
	}

	public static void connectToProxy()
	{
		try
		{
			socketConnector = new ServerHandlerRunnable(new Socket(Config.host, Config.port));
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
		if(socketConnector != null)
		{
			socketConnector.sendMessage(ExchangeObject);
		}
	}
}
