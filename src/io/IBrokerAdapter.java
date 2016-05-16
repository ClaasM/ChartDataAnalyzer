package io;

import enums.Markets;

public interface IBrokerAdapter {
	//public float requestExchangeRate(String from, String to);
	public double requestBid(Markets market);
	void placeBinary(Markets market, double amount, boolean direction,
			long duration);
}
