package chartdataeditor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class Candle implements Comparable<Candle> {

    /**
     * @param args
     */
    private long startTime;    //Wann der von der Candle beschriebene Zeitraum beginnt
    private long duration;    //Wie lange er dauert in ms

    /*TODO Können auch direkt im Getter aus den Ticks berechnet werden*/
    private double high;
    private double low;
    private double open;
    private double close;

    private List<Tick> ticks;


    /**
     * high, low, open, close werden im Konstruktor berechnet (Muss eventuell noch ge�ndert werden)
     *
     * @param startTime StartZeit der Candle
     * @param duration  Dauer der Candle in ms
     * @param ticks
     */
    public Candle(long startTime, long duration, List<Tick> ticks) {
        this.startTime = startTime;
        this.duration = duration;

        if (!ticks.isEmpty()) {
            this.open = ticks.get(0).value();
            this.close = ticks.get(ticks.size() - 1).value();
        }

        high = Collections.max(ticks).value();
        low = Collections.min(ticks).value();

        this.ticks = ticks;
    }

    public Candle(long startTime, long duration) {
        this.startTime = startTime;
        this.duration = duration;
        this.low = Double.MAX_VALUE;
    }

    public Candle(long startTime, long duration, double high, double low,
                  double open, double close) {
        this.startTime = startTime;
        this.duration = duration;
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
    }


//	@Override
//	public String toString() {
//		DecimalFormat doubleformatter = new DecimalFormat("#0.00");
//		DecimalFormat intFormatter = new DecimalFormat("000");
//		return "\nCandle [startTime=" + startTime 
//				+ ", duration=" + duration
//				+ ", high=" + doubleformatter.format(high)
//				+ ", low=" + doubleformatter.format(low)
//				+ ", open=" + doubleformatter.format(open)
//				+ ", close=" + doubleformatter.format(close)
//				+  "]"; //ticks != null ? (", ticks.size=" + intFormatter.format(ticks.size())) : "" +
//	}


    //Ticks werden nach Wert verglichen, wohingegen Candles nach StartZeit verglichen werden
    @Override
    public int compareTo(Candle other) {
        return Long.compare(startTime, other.startTime);
    }


    @Override
    public String toString() {
        return "Candle [startTime=" + startTime + ", duration=" + duration
                + ", high=" + high + ", low=" + low + ", open=" + open
                + ", close=" + close + "]";
    }


    public long getStartTime() {
        return startTime;
    }


    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }


    public long getDuration() {
        return duration;
    }


    public void setDuration(long duration) {
        this.duration = duration;
    }


    public double getHigh() {
        return high;
    }


    public double getLow() {
        return low;
    }


    public double getOpen() {
        return open;
    }


    public double getClose() {
        return close;
    }


    public void setHigh(double high) {
        this.high = high;
    }


    public void setLow(double low) {
        this.low = low;
    }


    public void setOpen(double open) {
        this.open = open;
    }


    public void setClose(double close) {
        this.close = close;
    }
}
