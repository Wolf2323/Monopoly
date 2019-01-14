package eva.monopoly.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import eva.monopoly.network.api.ExchangeMessage;
import eva.monopoly.network.api.HandlerException;
import eva.monopoly.network.api.SocketConnector;
import eva.monopoly.network.api.messages.NameInfo;
import eva.monopoly.network.client.Client;


public class Server
{
	public final static Logger					LOGGER				= Logger.getLogger(Client.class.getName());

	private HashMap<String, SocketConnector>	socketConnectors	= new HashMap<>();

	private ServerSocket						serverSocket;

	public Server(int port, String name, Consumer<HandlerException> shutdownHandler, Consumer<HandlerException> clientShutdownHandler) throws UnknownHostException,
			IOException
	{
		try
		{
			serverSocket = new ServerSocket(port);
			final Runnable runnable = () ->
			{
				try
				{
					SocketConnector client = new SocketConnector(serverSocket.accept(), clientShutdownHandler);
					LOGGER.log(Level.INFO, "Verbunden mit Client: " + client.getSocket().getInetAddress().getHostAddress());
					client.establishConnection();
					client.sendMessage(new NameInfo(name));
					client.registerHandle(NameInfo.class, nameInfo ->
					{
						socketConnectors.put(nameInfo.getName(), client);
						LOGGER.log(Level.INFO, "Client  Name: " + nameInfo.getName());
					});
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			};
			new Thread(runnable).start();
		}
		catch(IOException e)
		{
			LOGGER.log(Level.SEVERE, "Fehler bei der Initialisierung des Servers: ", e);
			throw e;
		}
		LOGGER.log(Level.INFO, "Server bestartet");
	}

	public void closeConnection()
	{
		try
		{
			for(String connector : socketConnectors.keySet())
			{
				socketConnectors.get(connector).closeConnection();
				LOGGER.log(Level.INFO, "Verbindung zu " + connector + " getrennt");
				socketConnectors.remove(connector);
			}
			serverSocket.close();
			LOGGER.log(Level.INFO, "Verbindung zu allen Clients getrennt");
		}
		catch(Exception e)
		{
		}
		serverSocket = null;
	}

	public HashMap<String, SocketConnector> getSocketConnectors()
	{
		return socketConnectors;
	}

	public SocketConnector getSocketConnector(String name)
	{
		return socketConnectors.get(name);
	}

	public boolean sendMessageToAll(final ExchangeMessage exchangeMessage)
	{
		boolean returnValue = true;
		for(SocketConnector connector : socketConnectors.values())
		{
			if(!connector.sendMessage(exchangeMessage))
			{
				returnValue = false;
			}
		}
		return returnValue;
	}
}
