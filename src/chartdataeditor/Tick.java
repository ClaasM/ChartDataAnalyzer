package chartdataeditor;

public class Tick implements Comparable<Tick>{
	
	private long time;
	private double ask;
	private double bid;
	
	private double askVolume;
	private double bidVolume;
	
	public Tick(long time, double ask, double bid) {
		super();
		this.time = time;
		this.ask = ask;
		this.bid = bid;
	}
	
	

	
	public Tick(long time, double ask, double bid, double askVolume,
			double bidVolume) {
		super();
		this.time = time;
		this.ask = ask;
		this.bid = bid;
		this.askVolume = askVolume;
		this.bidVolume = bidVolume;
	}




	/** Nicht ganz korrekt, muss eventuell nachgearbeitet werden.
	 * @returns Den Durchschnitt zwischen ask und bid.
	 */
	public double value(){
		return (ask + bid) / 2d;
	}


	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}


	public double getAsk() {
		return ask;
	}


	public void setAsk(double ask) {
		this.ask = ask;
	}


	public double getBid() {
		return bid;
	}


	public void setBid(double bid) {
		this.bid = bid;
	}


	@Override
	public String toString() {
		return "Tick [time=" + time + ", ask=" + ask + ", bid=" + bid + "]";
	}



	//Ticks werden nach Wert verglichen, wohingegen Candles nach StartZeit verglichen werden
	//Ticks implementieren compareTo, damit der höchstwertige/niedrigstwertige indentifiziert werden kann
	@Override
	public int compareTo(Tick o) {
		return Double.compare(value(), o.value());
	}
	
	
	
}
