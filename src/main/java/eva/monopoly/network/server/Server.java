package eva.monopoly.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eva.monopoly.network.api.ExchangeMessage;
import eva.monopoly.network.api.HandlerException;
import eva.monopoly.network.api.SocketConnector;
import eva.monopoly.network.api.messages.NameInfo;


public class Server
{
	public final static Logger					LOG					= LoggerFactory.getLogger(Server.class);

	private HashMap<String, SocketConnector>	socketConnectors	= new HashMap<>();

	private ServerSocket						serverSocket;

	public Server(int port, String name, Consumer<HandlerException> shutdownHandler) throws IOException
	{
		try
		{
			serverSocket = new ServerSocket(port);
			final Runnable runnable = () ->
			{
				try
				{
					SocketConnector client = new SocketConnector(serverSocket.accept(), shutdownHandler);
					LOG.info("Verbunden mit Client: {}", client.getSocket().getInetAddress().getHostAddress());
					client.registerHandle(NameInfo.class, nameInfo ->
					{
						socketConnectors.put(nameInfo.getName(), client);
						LOG.info("Client  Name: {}", nameInfo.getName());
					});
					client.establishConnection();
					client.sendMessage(new NameInfo(name));
				}
				catch(IOException e)
				{
					LOG.error("Fehler beim verbinden mit Client!", e);
				}
			};
			new Thread(runnable).start();
		}
		catch(IOException e)
		{
			LOG.error("Fehler bei der Initialisierung des Servers:", e);
			throw e;
		}
		LOG.info("Server gestartet");
	}

	public void closeConnection()
	{
		try
		{
			for(String connector : socketConnectors.keySet())
			{
				socketConnectors.get(connector).closeConnection();
				LOG.info("Verbindung zu {} getrennt", connector);
				socketConnectors.remove(connector);
			}
			serverSocket.close();
			LOG.info("Verbindung zu allen Clients getrennt");
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
