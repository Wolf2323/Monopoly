package eva.monopoly.network.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import eva.monopoly.network.api.HandlerException;
import eva.monopoly.network.api.SocketConnector;
import eva.monopoly.network.api.messages.NameInfo;


public class Client
{
	public final static Logger	LOGGER	= Logger.getLogger(Client.class.getName());

	private SocketConnector		socketConnector;
	private String				remoteName;

	public Client(String host, int port, String name, Consumer<HandlerException> shutdownHandler) throws UnknownHostException, IOException
	{
		try
		{
			SocketConnector socketConnector = new SocketConnector(new Socket(host, port), shutdownHandler);
			socketConnector.establishConnection();
			socketConnector.sendMessage(new NameInfo(name));
			socketConnector.registerHandle(NameInfo.class, nameInfo ->
			{
				this.socketConnector = socketConnector;
				this.remoteName = nameInfo.getName();
				LOGGER.log(Level.INFO, "Server Name: " + nameInfo.getName());
			});
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
			LOGGER.log(Level.INFO, "Verbindung zum Server getrennt");
		}
		catch(Exception e)
		{
		}
		socketConnector = null;
		remoteName = null;
	}

	public SocketConnector getSocketConnector()
	{
		return socketConnector;
	}

	public String getRemoteName()
	{
		return remoteName;
	}
}
