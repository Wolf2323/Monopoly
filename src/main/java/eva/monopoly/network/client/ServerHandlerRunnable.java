package eva.monopoly.network.client;

import java.net.Socket;
import java.util.logging.Level;

import eva.monopoly.network.api.AbstractHandlerRunnable;
import eva.monopoly.network.api.ExchangeObject;


public class ServerHandlerRunnable extends AbstractHandlerRunnable
{
	public ServerHandlerRunnable(final Socket serverSocket, String clientName)
	{
		super(serverSocket, clientName);
	}

	@Override
	protected void shutdownConnection(final String reason)
	{
		if(reason != null)
		{
			Client.LOGGER.log(Level.SEVERE, reason);
		}
		Client.restartIfNoProblem();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void solveMessage(final ExchangeObject obj)
	{
		switch(obj.getType())
		{
			case LOGIN:
			{
				setName(obj.getSender());
				Client.LOGGER.log(Level.INFO, "Server Name: " + obj.getSender());
				break;
			}
			case LOGOUT:
			{
				break;
			}
			case DATA:
			{
				Client.resolveData(obj);
				break;
			}
			case PING:
			{
				break;
			}

		}
	}
}
