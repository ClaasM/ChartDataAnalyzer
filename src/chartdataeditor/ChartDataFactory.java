package chartdataeditor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import other.Utils;
import exceptions.InvalidIntervalException;

public class ChartDataFactory {

	
	/**
	 * Einfache handwerksmethode die aus den Tickdaten die richtigen ausliest und zu Candles zusammenfasst
	 * 
	 * @param candleLength Zeitraum, den die Candles beschreiben (z.b. 5 Minuten / 1 Std / 1 Tag) (In ms!)
	 * @param from
	 * @param to
	 * @return List of Candles
	 * @throws InvalidIntervalException 
	 */
	public static List<Candle> toCandles(int candleLength, Calendar from, Calendar to, List<Tick> ticks) throws InvalidIntervalException{
		
		if(from.after(to)){
			throw new InvalidIntervalException("From darf nicht nach to liegen");
		}
		
		Calendar cal = Calendar.getInstance();
		
		
		return null;
		
	}
	/**
	 * Einfache handwerksmethode die alle Tickdaten zu candles zusammenfasst
	 * 
	 * @param candleLength Zeitraum, den die Candles beschreiben (z.b. 5 Minuten / 1 Std / 1 Tag) (In ms!)
	 * @param from
	 * @param to
	 * @return List of Candles
	 * @throws InvalidIntervalException 
	 */
	public static List<Candle> toCandles(int candleLength, List<Tick> ticks){

		Map<Long, List<Tick>> candlesMap =
		         ticks.stream()
		                 .collect(Collectors.groupingBy(t -> Utils.startTime(t.getTime(),candleLength)));
		
		List<Candle> candles = new ArrayList<Candle>();
		for(Map.Entry<Long, List<Tick>> entry:candlesMap.entrySet()){
			candles.add(new Candle(entry.getKey().longValue(), candleLength, entry.getValue()));
		}
		
		Collections.sort(candles);
		return candles;
	}
	
	/**
	 * Konvertiert candles in candles einer höheren Zeiteinheit
	 * 
	 * @param candleLength
	 * @param candles
	 * @return
	 */
	public static List<Candle> convertCandleLength(long candleLength, List<Candle> candles) {
		if(candleLength < candles.get(0).getDuration()){
			System.err.println("Auf niedrigere Zeiteinheit konvertieren geht nicht!");
			return null;
		}else if(candleLength % candles.get(0).getDuration() != 0){
			System.err.println("Die gewünschte candleLength muss ein vielfaches der candleLength der candles sein");
			return null;
		} else {
			
			List<Candle> newCandles = new ArrayList<Candle>();
			int factor = (int) (candleLength / candles.get(0).getDuration());
			
			
			for(int i = 0; i < candles.size(); i += factor){
				
				long startTime = candles.get(i).getStartTime();
				
				double open = candles.get(i).getOpen();
				double close = candles.get(Math.min(i + factor - 1, candles.size())).getClose();
				double low = Double.POSITIVE_INFINITY;
				double high = Double.NEGATIVE_INFINITY;
				for(int j = i; j < Math.min(i + factor, candles.size()); j++){
					if(candles.get(j).getHigh() > high){
						high = candles.get(j).getHigh();
					}
					if(candles.get(j).getLow() < low){
						low = candles.get(j).getLow();
					}
				}
				newCandles.add(new Candle(startTime,candleLength,high,low,open,close));
			}
			
			return newCandles;
		}
		
	}
	
	/**
	 * Filtert Wochentage aus. z.b. Samstage/Sonntage
	 * @param days
	 * @param filterOut
	 * @return
	 */
	
	public static List<List<Candle>> filterDaysOfWeek(List<List<Candle>> days, int...filterOut){
		return days.stream().filter(l -> {
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(l.get(0).getStartTime());

					if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
							|| c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						return false;
					} else {
						return true;
					}
		}).collect(Collectors.toList());
	}
	
	/**
	 * Fasst Candles zu Tagen zusammen.
	 * 
	 * @param candleLength
	 * @return
	 */
	public static List<List<Candle>> toDays(List<Candle> candles){
		//Ein Tag in ms
		long dayLength = 1000 * 60 * 60 * 24;
		
		
		List<List<Candle>> days = new ArrayList<List<Candle>>();
		int factor = (int) (dayLength / candles.get(0).getDuration());
		
		
		for(int i = 0; i < candles.size(); i += factor){
			List<Candle> day = new ArrayList<Candle>();
			for(int j = i; j < Math.min(i + factor, candles.size()); j++){
				day.add(candles.get(j));
			}
			days.add(day);
		}
		return days;
	}
	
	
}
