package listener;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import other.Utils;
import chartdataeditor.Candle;
import chartdataeditor.CandlePattern;

public class CandlePatternPOJO extends CandlePattern{

	public double averageMovement;
	
	public long timesFoundTotal;
	public long timesBullishTotal;
	public long timesBearishTotal;

	
	public CandlePatternPOJO(boolean[][] pattern, long candleDuration) {
		super(pattern, candleDuration);
	}

	@Override
	public double probability(boolean bullish) {
		if(bullish){
			return (double)timesBullishTotal / (double)timesFoundTotal;
		} else {
			return (double)timesBearishTotal / (double)timesFoundTotal;
		}
	}

	@Override
	public double averageMovement() {
		return averageMovement;
	}

	@Override
	public long timesFoundTotal() {
		return timesFoundTotal;
	}

	@Override
	public long timesBullishTotal() {
		return timesBullishTotal;
	}

	@Override
	public long timesBearishTotal() {
		return timesBearishTotal;
	}

	@Override
	public void analyze(List<Candle> candles) {
		System.err.println("This is a Fake Pattern");
	}
}
