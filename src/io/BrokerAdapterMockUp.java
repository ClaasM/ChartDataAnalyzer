package io;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import enums.Markets;

public class BrokerAdapterMockUp implements IBrokerAdapter{

	private HttpClient httpClient;
	private ResponseHandler<String> responseHandler;
	
	private double lastBid;
	private double capital = 10000;
	
	
	public BrokerAdapterMockUp(){
		//Keine Ahnung wieso
		httpClient = HttpClientBuilder.create().build();
		//Was auch immer hinter dem Get steht bestimmt was man bekommt
		responseHandler = new BasicResponseHandler();
	}
	
	//TODO Hier künstlich ein Konto verwalten

	@Override
	public void placeBinary(Markets market, double amount, boolean direction, long duration){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//Binary simulation
					
					final double startBid = lastBid;
					System.out.println("Placing Binary in " + market.name() + ", " + amount + "€, " + (direction?"Short":"Long") + ", " + duration + "ms");
					
					Thread.sleep(duration);
					
					if(startBid > lastBid && !direction){
						capital += amount * .81;
						System.out.println("Success! " + market.name() + ", " + (amount * .81) + "€ profit, " + (capital) + "€ capital, "  + (direction?"Short":"Long") + ", " + duration + "ms");
					} else if (startBid < lastBid && direction){
						capital+= amount * .81;
						System.out.println("Success! " + market.name() + ", " + (amount * .81) + "€ profit, " + (capital) + "€ capital, "  + (direction?"Short":"Long") + ", " + duration + "ms");
					} else {
						capital-= amount;
						System.out.println("Failed! " + market.name() + ", " + (amount * .81) + "€ profit, " + (capital) + "€ capital, "  + (direction?"Short":"Long") + ", " + duration + "ms");
					}
					
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}).start();
	}

	@Override
	public double requestBid(Markets market) {
		HttpGet httpGet = new HttpGet("http://quote.yahoo.com/d/quotes.csv?s=" + market.name() + "=X&f=l1&e=.csv");
		try {
			lastBid = Double.parseDouble(httpClient.execute(httpGet,responseHandler));
			return lastBid;
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
