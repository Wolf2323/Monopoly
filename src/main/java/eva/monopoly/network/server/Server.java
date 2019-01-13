package eva.monopoly.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import eva.monopoly.network.api.HandlerException;
import eva.monopoly.network.api.SocketConnector;
import eva.monopoly.network.client.Client;


public class Server
{
	public final static Logger					LOGGER				= Logger.getLogger(Client.class.getName());

	private HashMap<String, SocketConnector>	socketConnectors	= new HashMap<>();

	private ServerSocket						serverSocket;

	public Server(int port, String name, Consumer<HandlerException> shutdownHandler) throws UnknownHostException, IOException
	{
		try
		{
			serverSocket = new ServerSocket(port);
			final Runnable runnable = () ->
			{
				final ClientHandlerRunnable runnable = new ClientHandlerRunnable(ExchangeServer.getServerSocket().accept());
				addClient(runnable);
			};
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

		// registerHandle
	}

	public void closeConnection()
	{
		try
		{
			socketConnector.closeConnection();
			LOGGER.log(Level.INFO, "Verbunden zu Server getrennt");
		}
		catch(Exception e)
		{
		}
		socketConnector = null;
	}

	public SocketConnector getSocketConnector()
	{
		return socketConnector;
	}
}
