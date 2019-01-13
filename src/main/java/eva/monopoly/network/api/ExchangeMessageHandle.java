package eva.monopoly.network.api;

import java.util.function.Consumer;


public class ExchangeMessageHandle<T extends ExchangeMessage>
{
	private final Class<T>		clazz;
	private final Consumer<T>	consumer;

	public ExchangeMessageHandle(final Class<T> clazz, final Consumer<T> consumer)
	{
		this.clazz = clazz;
		this.consumer = consumer;
	}

	public void handle(final ExchangeMessage exMsg)
	{
		final T correctExchangeMsg = clazz.cast(exMsg);
		consumer.accept(correctExchangeMsg);
	}
}
