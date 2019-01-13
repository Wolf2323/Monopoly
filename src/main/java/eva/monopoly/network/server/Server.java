package eva.monopoly.network.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import eva.monopoly.network.api.ExchangeMessage;
import eva.monopoly.network.api.HandlerException;
import eva.monopoly.network.api.SocketConnector;


public class Server
{
	public final static Logger	LOGGER	= Logger.getLogger(Server.class.getName());

	private SocketConnector		socketConnector;

	public Server(String host, int port, String name, Consumer<HandlerException> shutdownHandler) throws UnknownHostException, IOException
	{
		try
		{
			socketConnector = new SocketConnector(new Socket(host, port), shutdownHandler);
			socketConnector.establishConnection(name);
		}
		catch(UnknownHostException e)
		{
			LOGGER.log(Level.SEVERE, "Ungültige Server Adresse: " + host, e);
			throw e;
		}
		catch(IOException e)
		{
			LOGGER.log(Level.SEVERE, "Fehler bei der Initialisierung des Servers: " + host, e);
			throw e;
		}
		LOGGER.log(Level.INFO, "Verbunden zu Server: " + host);
	}

	public void closeConnection()
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

	public String getRemoteName()
	{
		return socketConnector.getRemoteName();
	}

	public boolean sendMessage(final ExchangeMessage exchangeMessage)
	{
		return socketConnector.sendMessage(exchangeMessage);
	}

	public <T extends ExchangeMessage> void registerHandle(Class<T> clazz, Consumer<T> consumer)
	{
		socketConnector.registerHandle(clazz, consumer);
	}
}
