package test;

import static org.junit.Assert.*;
import io.CandleDataReader;
import io.PatternWriter;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chartdataeditor.Candle;
import chartdataeditor.CandlePattern;
import chartdataeditor.ChartDataFactory;
import enums.Markets;

public class Analyzing {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	//TODO einen Test schreiben der die häufigst vorkommenden in eine Datei schreibt 
	//(Für Testzwecke - ansonsten dauert es vielleicht zu lange bis resultate kommen beim Observer)

	@Test
	public void test() {
		PatternWriter pw = null;
		
		try {

			//Jede Zeiteinheit muss ein vielfaches der vorherigen sein
			//Nicht vergessen auch vielfachezu analysieren die beim ersten durchlauf nicht dabei sein konnten
			//1-3-24-48
			//1-5-15-30-60
			//IG Sprint: 1-2-5-20-60
			
			final long[] CANDLE_LENGTHS = {TimeUnit.MINUTES.toMillis(1),
					TimeUnit.MINUTES.toMillis(5),
					TimeUnit.MINUTES.toMillis(20),
					TimeUnit.MINUTES.toMillis(1)};
			
			final int[] patternLengths = {1,2,3,4}; //,3,4
			
			final Markets market = Markets.AUDUSD;
			
			pw = new PatternWriter(market);
			
			//Candles einlesen
			CandleDataReader cdr = new CandleDataReader(market, 1);
			List<Candle> candles = cdr.readCandles();
			System.out.println("candles eingelesen");
			System.out.println(candles.size());
			
			for(long candleLength: CANDLE_LENGTHS){
				
				//In nächste CandleLength umwandeln
				candles = ChartDataFactory.convertCandleLength(candleLength, candles);
				System.out.println("CandleLength: " + TimeUnit.MILLISECONDS.toMinutes(candleLength));
				System.out.println(candles.size());	
				
				for(int patternLength:patternLengths){
					
					List<CandlePattern> allPatterns = CandlePattern.generateAllPossiblePatterns(patternLength, candleLength);
					
					for(CandlePattern currentPattern:allPatterns){
						currentPattern.analyze(candles);
					}
					
					//Filtern (passPredicate könnte BS sein...mal recherchieren und rumfragen)
					//allPatterns = allPatterns.stream().filter(CandlePattern.passPredicate()).collect(Collectors.toList());
					
					//Sortieren
					allPatterns.sort(CandlePattern.totalOccurencesAscending());
					
//					//Nur das erste sechstel
//					for(int i = 0; i < allPatterns.size() / 6; i++){
//						if(allPatterns.get(i).timesFoundTotal() > 10)
//							pw.writeCandlePattern(allPatterns.get(i));
//					}
					
					
					for(CandlePattern cp: allPatterns){
						if(cp.timesFoundTotal() > 0){
							//Ausgeben
							System.out.println("\t" + cp);
							//In CSV schreiben
							pw.writeCandlePattern(cp);	
						}
					}
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (NumberFormatException e) {
			e.printStackTrace();
			fail();
		} catch (ParseException e) {
			e.printStackTrace();
			fail();
		} finally {
			try {
				pw.close();
			} catch (IOException e) {
				e.printStackTrace();
				fail();
			}
		}
	}
}
