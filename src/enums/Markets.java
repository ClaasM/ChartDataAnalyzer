package enums;

public enum Markets {
	
	DEUIDX (MarketTypes.INDEX),
	
	EURUSD (MarketTypes.FOREX),
	USDJPY (MarketTypes.FOREX),
	GBPUSD (MarketTypes.FOREX),
	USDCAD (MarketTypes.FOREX),
	USDCHF (MarketTypes.FOREX),
	AUDUSD (MarketTypes.FOREX);
	
	private MarketTypes type;
	
	Markets(MarketTypes type){
		this.type = type;
	}
	
	public MarketTypes getMarketType(){
		return type;
	}
	
	@Override
	public String toString(){
		return this.name();
	}
}

