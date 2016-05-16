package chartdataeditor;

public abstract class Pattern {

	protected boolean[][] pattern;
	protected boolean isTickPattern;
//	protected double probability = -1;
	
//	public static final int DOWN = -1;
//	public static final int UP = 1;
	
	public boolean isTickPattern(){
		return isTickPattern;
	}

	public boolean[][] getPattern() {
		return pattern;
	}
	
	
}
