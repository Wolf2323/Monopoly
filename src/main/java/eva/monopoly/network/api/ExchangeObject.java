package eva.monopoly.network.api;

public class ExchangeObject
{
	private final static String	SPLITTER	= ";";
	final private String		sender;
	final private ExchangeType	type;
	final private String		channel;
	final private String		message;

	public ExchangeObject(final String sender, final ExchangeType type, final String channel, final String message)
	{
		this.sender = sender;
		this.type = type;
		this.channel = channel;
		this.message = message;
	}

	public String getSender()
	{
		return this.sender;
	}

	public ExchangeType getType()
	{
		return type;
	}

	public String getChannel()
	{
		return channel;
	}

	public String getMessage()
	{
		return this.message;
	}

	@Override
	public String toString()
	{
		return sender + SPLITTER + type + SPLITTER + channel + SPLITTER + message;
	}

	public static ExchangeObject fromString(String input)
	{
		String[] args = input.split(SPLITTER);
		ExchangeType type = ExchangeType.valueOf(args[1]);
		return new ExchangeObject(args[0], type, args[2], args[3]);
	}
}
