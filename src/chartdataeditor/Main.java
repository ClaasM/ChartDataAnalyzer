package chartdataeditor;

import io.CandleDataReader;
import io.PatternWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import other.Utils;
import enums.Markets;

public class Main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static List<Tick> readTicksFromTo(Calendar start, Calendar end){
		//Monat -1!
		List<Tick> ticks = new ArrayList<Tick>();

		for (Calendar date = start; date.before(end); date
				.add(Calendar.DATE, 1)) {
			// Dauert ca. 300ms für einen Tag DEUIDX:
			try {
				ticks.addAll(new TickDataReader(Markets.DEUIDX, date)
						.readTicks(7, 21));
				System.out.println(date.getTime()
						+ " ist ein Handelstag/ Daten geladen");
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.out.println(date.getTime()
						+ " ist kein Handelstag/ die Daten liegen nicht vor");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return ticks;
	}
}
