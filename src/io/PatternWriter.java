package io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import chartdataeditor.CandlePattern;
import other.Utils;
import enums.Markets;

public class PatternWriter {

	private static final String[] HEADER = {"candleLength","pattern","averageMovement","timesFoundTotal","timesBullishTotal","timesBearishTotal"};
	
	private FileWriter out;
	private CSVPrinter printer;
	
	public PatternWriter(Markets market) throws IOException{
		//File erstellen
		String path = Utils.marketBasePath(market) + "results/" + new Date().toString().replace(":","_") + "_results.csv";
		File f = new File(path);
		f.createNewFile();
		
		//Printer initialisieren
		out = new FileWriter(f);
		CSVFormat format = CSVFormat.EXCEL.withHeader(HEADER);
		printer = new CSVPrinter(out, format);
	}

	public void writeCandlePattern(CandlePattern pattern) throws IOException{
		printer.printRecord(pattern.getCandleDuration(),
				Arrays.deepToString(pattern.getPattern()),
				pattern.averageMovement(),
				pattern.timesFoundTotal(),
				pattern.timesBullishTotal(),
				pattern.timesBearishTotal());
	}
	
	public void close() throws IOException{
		out.flush();
		out.close();
		printer.close();
	}
	
	
}
