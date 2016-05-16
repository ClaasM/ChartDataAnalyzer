package converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import chartdataeditor.ChartDataFactory;
import chartdataeditor.TickDataReader;
import chartdataeditor.Tick;
import enums.Markets;


/**
 * Verschiebt die Zeiten aller Ticks/Datensätze in einem CSV/Einer Range von CSV's und eine angegebene Stundenzahl.
 * Damit werden die Daten auf Sommerzeit eingestellt, was bei Dukascopy leider verpatzt wird.
 * 
 * @author Claas
 *
 */
public class TimeShifter {

	public static void main(String[] args) {
		
		//Vom 13. bis zum 29.4.15
		Calendar start = Calendar.getInstance();
		start.set(2015, 03, 13);
		Calendar end = Calendar.getInstance();
		end.set(2015, 03, 30);


		for (Calendar date = start; date.before(end); date
				.add(Calendar.DATE, 1)) {
			shiftHoursInFile(date, 1);
		}
	}
	
	public static void shiftHoursInFile(Calendar date, int shiftBy){
		try {
			//Bisschen doof aber passt schon
			String fromPath = new TickDataReader(Markets.DEUIDX, date).toPath(Markets.DEUIDX, date);
			String toPath = fromPath; //In diesem Fall wird die ursprüngliche Datei überschrieben
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
			BufferedReader reader = new BufferedReader(new FileReader(fromPath));
			
			List<String> newLines = new ArrayList<String>();
			
			String line = reader.readLine();
			line = reader.readLine();		//Header überspringen
			while(line != null){
				String[] values = line.split(",");
				try {
					Calendar c = Calendar.getInstance();
					c.setTimeInMillis(dateFormat.parse(values[0]).getTime());
					c.add(Calendar.HOUR_OF_DAY, shiftBy);
					values[0] = dateFormat.format(c.getTime());
					
					newLines.add(values[0] + ","
							+ values[1] + ","
							+ values[2] + ","
							+ values[3] + ","
							+ values[4]);
					
					

				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				line = reader.readLine();
			}
			reader.close();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(toPath));
			writer.write("Time,Ask,Bid,AskVolume,BidVolume"); //Header
			
			for(String newLine:newLines){
				writer.newLine();
				writer.write(newLine);
			}
			writer.close();
			
		} catch (IOException e) {
			System.out.println(date.getTime() + " ist kein Handelstag/ die Daten liegen nicht vor");
		}
	}

}
