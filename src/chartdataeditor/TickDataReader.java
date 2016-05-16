package chartdataeditor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import enums.MarketTypes;
import enums.Markets;


public class TickDataReader {

	private static final String DATAPATH = "data/";
	private static final String FOREXPATH = "fx/";
	private static final String INDICESPATH = "indices/";
	
	private FileReader in;
	private Iterable<CSVRecord> records;
	private SimpleDateFormat dateFormat;
	
	
	public TickDataReader(Markets market, Calendar cal) throws IOException{
		in = new FileReader(toPath(market, cal));
		records = CSVFormat.EXCEL.withHeader().parse(in);
		dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
	}

	/**
	 * Contructs a relative FilePath from market and date
	 * @param market
	 * @param cal
	 * @return
	 */
	
	public  String toPath(Markets market, Calendar cal){
		return DATAPATH
				+ (market.getMarketType() == MarketTypes.INDEX ? INDICESPATH : FOREXPATH)
				+ (market.name()) + "/"
				+ (cal.get(Calendar.YEAR) - 2000) + "/" //Unsauber aber was will man machen
				+ (cal.get(Calendar.MONTH) + 1) + "/"
				+ (cal.get(Calendar.DAY_OF_MONTH))
				+ ".csv";
	}
	
	
	/**
	 * This method may take a while depending on the file size.
	 * the hour in 'to' is included! So if th ehour is 23; 23:59 will still be added!
	 * @return List of all Ticks in the specified path.
	 * @throws ParseException 
	 * @throws NumberFormatException
	 * @throws IOException 
	 */
	
	//TODO erst garnicht so viel einlesen sondern gleich nur die richtigen (spart Zeit)
	public List<Tick> readTicks(int fromHour, int toHour) throws NumberFormatException, ParseException{
		List<Tick> ticks = readTicks();
		ticks = filterByHoursOfDay(fromHour, toHour, ticks);
		return ticks;
	}

	public List<Tick> readTicks() throws NumberFormatException, ParseException  {
		List<Tick> ticks = new ArrayList<Tick>();
		for (CSVRecord record : records) {
			ticks.add(new Tick(dateFormat.parse(record.get("Time")).getTime(), 
					Double.parseDouble(record.get("Ask")),
					Double.parseDouble(record.get("Bid")),
					Double.parseDouble(record.get("AskVolume")),	//Volumen sind in Millionen!
					Double.parseDouble(record.get("BidVolume"))));
		}
		return ticks;

	}
	
	private List<Tick> filterByHoursOfDay(int fromHour, int toHour, List<Tick> ticks){
		ticks = ticks
				.stream()
				.filter(t -> {
					Calendar cal = Calendar.getInstance();
					
					cal.setTimeInMillis(t.getTime());
					
					return cal.get(Calendar.HOUR_OF_DAY) >= fromHour
						&& cal.get(Calendar.HOUR_OF_DAY) < toHour;
					
				}).collect(Collectors.toList());
		return ticks;
	}
}
