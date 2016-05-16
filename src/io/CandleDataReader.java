package io;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import other.Utils;
import chartdataeditor.Candle;
import enums.MarketTypes;
import enums.Markets;

public class CandleDataReader {


	private long candleLength; // Candlelength in ms!

	private FileReader in;
	private CSVParser parser;
	
	private SimpleDateFormat dateFormat;

	/**candleLength in MINUTES!
	 * @param market
	 * @param candleLength
	 * @throws IOException
	 */
	public CandleDataReader(Markets market, int candleLength)
			throws IOException {
		this.in = new FileReader(Utils.marketBasePath(market) + candleLength + "M_BID.csv");
		this.parser = CSVFormat.EXCEL.withHeader().parse(in);
		this.dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

		this.candleLength = candleLength * 60 * 1000;
	}

	/** @return List of all Ticks in the specified path.
	 * @throws ParseException
	 * @throws NumberFormatException
	 * @throws IOException
	 */

	// TODO erst garnicht so viel einlesen sondern gleich nur die richtigen
	// (spart Zeit)
	public List<Candle> readCandles(int fromHour, int toHour)
			throws NumberFormatException, ParseException {
		List<Candle> candles = readCandles();
		candles = filterByHoursOfDay(fromHour, toHour, candles);
		return candles;
	}

	public List<Candle> readCandles() throws NumberFormatException, ParseException  {
		List<Candle> candles = new ArrayList<Candle>();
		for (CSVRecord record : parser) {
			candles.add(new Candle(dateFormat.parse(record.get("Time")).getTime(), 
					candleLength, 
					Double.parseDouble(record.get("High")), 
					Double.parseDouble(record.get("Low")), 
					Double.parseDouble(record.get("Open")), 
					Double.parseDouble(record.get("Close"))));
		}
			return candles;
	}

	private List<Candle> filterByHoursOfDay(int fromHour, int toHour,
			List<Candle> candles) {
		candles = candles
				.stream()
				.filter(t -> {
					Calendar cal = Calendar.getInstance();

					cal.setTimeInMillis(t.getStartTime());

					return cal.get(Calendar.HOUR_OF_DAY) >= fromHour
							&& cal.get(Calendar.HOUR_OF_DAY) < toHour;

				}).collect(Collectors.toList());
		return candles;
	}
}
