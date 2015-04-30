package rabbitmq.prototype;

public enum ExchangeType {

	CUSTOMER_EVENT_DIRECT("direct", "events.customer"),
	CUSTOMER_EVENT_TOPIC("topic", "events.customer"),
	CUSTOMER_EVENT_FANOUT("fanout", "events.customer");

	private String exchangeType;

	private String exchangeName;

	ExchangeType(String exchangeType, String exchangeName) {
		this.exchangeType = exchangeType;
		this.exchangeName = exchangeName;
	}

	public String getExchangeType() {
		return exchangeType;
	}

	public String getExchangeName() {
		return exchangeName;
	}
}
