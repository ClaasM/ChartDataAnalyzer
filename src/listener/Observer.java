package listener;

import io.BrokerAdapterMockUp;
import io.IBrokerAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Date;

import other.Utils;
import chartdataeditor.Candle;
import chartdataeditor.CandlePattern;
import enums.Markets;

public class Observer implements Runnable{
	//Observiert den aktuellen Kurs bei Yahoo Finance und reagiert entsprechend
	
	private List<CandlePattern> patternToLookFor;
	private List<Candle>[] observedCandles;
	private List<Long> durationsToObserve;
	
	private Markets market;
	private IBrokerAdapter brokerAdapter;
	
	private boolean running;
	
	public Observer(List<CandlePattern> patternToLookFor, Markets in) {
		this.patternToLookFor = patternToLookFor;
		this.market = in;
		this.durationsToObserve = new ArrayList<Long>();
		
		for(CandlePattern pattern:patternToLookFor){
			if(!durationsToObserve.contains(pattern.getCandleDuration())){
				durationsToObserve.add(pattern.getCandleDuration());
			}
		}
		
		observedCandles = (List<Candle>[]) Array.newInstance(new ArrayList<Candle>().getClass(), durationsToObserve.size());
		for(int i = 0; i < observedCandles.length; i++){
			observedCandles[i] = new ArrayList<Candle>();
		}
		brokerAdapter = new BrokerAdapterMockUp();
	}




	@Override
	public void run() {
		running = true;
		double lastBid = 0;
		
		while(running){
			
			double newBid = brokerAdapter.requestBid(market);
			long time = System.currentTimeMillis();
			
			if(newBid == -1){
				System.exit(-1);
			}
			
			for(int i = 0; i < durationsToObserve.size(); i++){
				Candle currentCandle = null;
				
				if(observedCandles[i].isEmpty()){
					//Add first Candle
					currentCandle = new Candle(Utils.startTime(time, durationsToObserve.get(i)), durationsToObserve.get(i));
					currentCandle.setOpen(newBid);
					observedCandles[i].add(currentCandle);

				} else {
					currentCandle = observedCandles[i].get(observedCandles[i].size() - 1);
					
					if(Utils.startTime(time, durationsToObserve.get(i)) != currentCandle.getStartTime()){
						currentCandle.setClose(lastBid);
						System.out.println(new Date(time) + " " +  market.name() +  "\n" + currentCandle);
						
						//TODO Analyse triggern (Aber nur für die Momentane Duration)
						for(CandlePattern pattern:patternToLookFor){
							if(pattern.getCandleDuration() == durationsToObserve.get(i) && observedCandles[i].size() > pattern.getPattern().length){
								if(pattern.suits(observedCandles[i])){
									System.out.println("Match! " + Arrays.deepToString(pattern.getPattern()));
									brokerAdapter.placeBinary(market, 100, pattern.direction(), pattern.getCandleDuration());
								}
							}
						}
						
						//Add new Candle
						currentCandle = new Candle(Utils.startTime(time, durationsToObserve.get(i)), durationsToObserve.get(i));
						currentCandle.setOpen(newBid);
						observedCandles[i].add(currentCandle);
					
					}
				}
				
				//Muss immer gemacht werden
				if(newBid > currentCandle.getHigh()){
					currentCandle.setHigh(newBid);
				}
				if(newBid < currentCandle.getLow()){
					currentCandle.setLow(newBid);
				}
				
			}
			lastBid = newBid;
		}
	}
	
	
	
	
	
	
	
}
