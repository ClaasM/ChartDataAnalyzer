package other;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tictactec.ta.lib.CandleSetting;
import com.tictactec.ta.lib.CandleSettingType;
import com.tictactec.ta.lib.RangeType;

import enums.Markets;

public class Utils {
	
	private static final String DATAPATH = "data/";
	private static final String FOREXPATH = "fx/";
	private static final String INDICESPATH = "indices/";
	

	public static boolean[][] splitIntoArraysOfLength(boolean[] array, int length){
		boolean[][] splitted = new boolean[(int) Math.ceil(array.length / length)][length];
		
		int counter = 0;
		int len = array.length;
		
		for (int i = 0; i < len - length + 1; i += length)
		    splitted[counter ++] = Arrays.copyOfRange(array, i, i + length);

		if (len % length != 0)
		    splitted[counter] = Arrays.copyOfRange(array, len - len % length, len);
		
		
		return splitted;
	}
	
	public static String toSciNotation(double number) {
	    return formatSciNotation(new DecimalFormat(number<0?"0.000E0":"0.0000E0").format(number));
	}

	public static String toSciNotation(long number) {
	    return formatSciNotation(new DecimalFormat(number<0?"0.000E0":"0.0000E0").format(number));
	}

	private static String formatSciNotation(String strNumber) {
	    if (strNumber.length() > 8) {
	        Matcher matcher = Pattern.compile("(-?\\d+)(\\.\\d{2})(E-?\\d+)").matcher(strNumber);

	        if (matcher.matches()) {
	            int diff = strNumber.length() - 8;
	            strNumber = String.format("%s%s%s", 
	                    matcher.group(1),
	                    // We add one back to include the decimal point
	                    matcher.group(2).substring(0, diff + 1),
	                    matcher.group(3)); 
	        }
	    }
	    return strNumber;
	}

	/**
	 * returns (time / intervalLength).floor  * intervalLength
	 * 
	 * @param time
	 * @param intervalLength
	 * @return the startTime of the Interval the Time belongs to
	 */
	public static long startTime(long time, long intervalLength) {
	    return ((long) (time / intervalLength)) * intervalLength;
	}
	
	
	public static int sampleSize(double sigma, double probability, double precision){
		return (int) ((Math.pow(sigma, 2) * (probability) * (1 - probability)) / Math.pow(precision, 2));
	}
	
	public static String marketBasePath(Markets market){
		return DATAPATH
				+ (market == Markets.DEUIDX ? INDICESPATH : FOREXPATH)
				+ market.name() + "/";
	}

	public static boolean[][] parseBooleanArray(String string) {
		String[] strOuter = string.split("]");
		boolean[][] outer = new boolean[strOuter.length][];
		
		for(int i = 0; i < strOuter.length; i++){
			//Erstes Komma abschneiden, an verbleibenden Kommas splitten
			String[] strInner = strOuter[i].substring(1, strOuter[i].length()).split(",");
			outer[i] = new boolean[strInner.length];
			
			for(int j = 0; j < strInner.length; j++){
				outer[i][j] = Boolean.parseBoolean(strInner[j].replace("[", "").replace(" ", ""));
			}
		}
		return outer;
	}
}
