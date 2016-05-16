package test;

import static org.junit.Assert.*;
import io.PatternReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import listener.Observer;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chartdataeditor.CandlePattern;
import enums.Markets;

public class Observing {

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

	@Test
	public void test() {

		
		final Markets[] marketsToObserve = {
				Markets.AUDUSD,
				//Markets.EURUSD,
				//Markets.GBPUSD,//?
				//Markets.USDCAD,
				//Markets.USDCHF,
				//Markets.USDJPY
				};
		
		Observer[] observer = new Observer[marketsToObserve.length];
		
		//Observer initialisieren
		for(int i = 0; i < observer.length; i++){
			try {
				PatternReader pr = new PatternReader(marketsToObserve[i]);
				List<CandlePattern> patternToLookFor = pr.readAllCandlePatterns();

				observer[i] = new Observer(patternToLookFor, marketsToObserve[i]);
				Thread t = new Thread(observer[i]);
				//t.setDaemon(true);
				t.start();

				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
