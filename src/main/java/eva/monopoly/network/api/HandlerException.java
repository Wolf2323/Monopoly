package eva.monopoly.network.api;

public class HandlerException extends Throwable
{
	private static final long serialVersionUID = 3097575622764570757L;

	public HandlerException()
	{
	}

	public HandlerException(String message)
	{
		super(message);
	}

	public HandlerException(Throwable cause)
	{
		super(cause);
	}

	public HandlerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public HandlerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
