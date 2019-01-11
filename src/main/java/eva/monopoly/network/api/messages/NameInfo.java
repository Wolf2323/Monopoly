package eva.monopoly.network.api.messages;

import eva.monopoly.network.api.ExchangeMessage;


public class NameInfo extends ExchangeMessage
{
	private static final long	serialVersionUID	= 3601976405972934497L;
	String						name;

	public String getName()
	{
		return name;
	}

	public NameInfo(String name)
	{
		this.name = name;
	}
}
