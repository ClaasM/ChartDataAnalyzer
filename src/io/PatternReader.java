package io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import listener.CandlePatternPOJO;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import other.Utils;
import chartdataeditor.Candle;
import chartdataeditor.CandlePattern;
import enums.Markets;

public class PatternReader {

	
	
	public static final String RESULT_FILENAME = "results";

	private FileReader in;

	private CSVParser parser;
	
	public PatternReader(Markets market) throws IOException{
		this.in = new FileReader(Utils.marketBasePath(market) + "results/" + RESULT_FILENAME + ".csv");
		this.parser = CSVFormat.EXCEL.withHeader().parse(in);
	}

	
	public List<CandlePattern> readAllCandlePatterns() throws IOException{
		List<CandlePattern> allCandlePatterns = new ArrayList<CandlePattern>();
		for (CSVRecord record : parser) {

			//TODO das pattern Array einlesen
			boolean[][] pattern = Utils.parseBooleanArray(record.get("pattern"));

			
			long candleDuration = Long.parseLong(record.get("candleLength"));
			
			CandlePatternPOJO newPattern = new CandlePatternPOJO(pattern, candleDuration );
			
			newPattern.averageMovement = Double.parseDouble(record.get("averageMovement"));
			newPattern.timesFoundTotal = Integer.parseInt(record.get("timesFoundTotal"));
			newPattern.timesBullishTotal = Integer.parseInt(record.get("timesBullishTotal"));
			newPattern.timesBearishTotal = Integer.parseInt(record.get("timesBearishTotal"));
			
			allCandlePatterns.add(newPattern);
		}
			return allCandlePatterns;
	}
	
	public void close() throws IOException{
		in.close();
		parser.close();
	}
}
