package chartdataeditor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

import other.Utils;

public class CandlePattern extends Pattern {

    public static final int NUMBER_OF_VARIABLES_ANALYZED = 3;

    private List<Candle> patternFollowUps;

    private long candleDuration = 0;

    /**
     * (1. open < nextOpen)
     * 1. close < nextClose
     * 2. high < nextHigh
     * 3. low < nextLow
     *
     * @param pattern
     */
    public CandlePattern(boolean[][] pattern, long candleDuration) {
        this.pattern = pattern;
        this.candleDuration = candleDuration;

        this.isTickPattern = false;

        patternFollowUps = new ArrayList<>();
    }

    public void analyze(List<Candle> candles) {

        if (candles.isEmpty() || candleDuration != candles.get(0).getDuration()) {
            System.err.println("Cannot analyze."
                    + (candles.isEmpty() ? "isEmpty" : " candleDuration: " + candles.get(0).getDuration() + " (Should be " + candleDuration + ")"));
            return;
        }

        for (int i = 0; i < candles.size() - pattern.length - 1; i++) {
            boolean found = true;
            patternLoop:
            for (int j = 0; j < pattern.length; j++) {
                if (matches(candles.get(i + j), candles.get(i + j + 1), pattern[j])) {
                    continue;
                } else {
                    found = false;
                    break patternLoop;
                }
            }
            if (found) {
                patternFollowUps.add(candles.get(i + pattern.length + 1));
            }
        }

    }


    public double probability(boolean bullish) {
        if (timesFoundTotal() > 0) {
            if (bullish) {
                return (double) timesBullishTotal() / (double) timesFoundTotal();
            } else {
                return (double) timesBearishTotal() / (double) timesFoundTotal();
            }
        } else {
            return 0;
        }

    }

    public double averageMovement() {
        return patternFollowUps.stream().mapToDouble(c -> c.getClose() - c.getOpen()).sum() / timesFoundTotal();
    }

    public long timesFoundTotal() {
        return patternFollowUps.size();
    }

    public long timesBullishTotal() {
        return patternFollowUps.stream().filter(c -> c.getOpen() < c.getClose()).count();
    }

    public long timesBearishTotal() {
        return patternFollowUps.stream().filter(c -> c.getOpen() > c.getClose()).count();
    }

    public boolean direction() {
        return probability(true) > probability(false);
    }


    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.00");
        return "Probability (Bullish): " + df.format(probability(true) * 100) + "%"
                + "\t" + " (" + timesBullishTotal() + "/" + timesFoundTotal() + ")" + " "
                + "\t" + ("Probability (Bearish): " + df.format(probability(false) * 100) + "%"
                + "\t" + " (" + timesBearishTotal() + "/" + timesFoundTotal() + ")" + " "
                + "\t" + "Average Movement: " + Utils.toSciNotation(averageMovement()))
                + "\t" + "For Pattern: " + Arrays.deepToString(pattern);
    }

    public boolean suits(List<Candle> candles) {
        if (candles.size() < pattern.length + 1 || candles.get(0).getDuration() != candleDuration) {
            System.err.println("Candles k�nnen so nicht passen");
            return false;
        }

        //Testet ob die letzten Candles dem Pattern entsprechen
        for (int i = 0; i < pattern.length; i++) {
            if (!matches(candles.get(candles.size() - pattern.length + i - 1), candles.get(candles.size() - pattern.length + i), pattern[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean matches(Candle first, Candle next, boolean[] relation) {

        //(Wenn die relation false ist kanns auch gleich sein)

        //Candle entspricht dem Pattern
        return ((first.getClose() < next.getClose()) == relation[0])
                && ((first.getHigh() < next.getHigh()) == relation[1])
                && ((first.getLow() < next.getLow()) == relation[2])

                //Und ist keine Notierungsl�cke (daf�r muss irgendwas anders sein)
                && ((first.getLow() != next.getLow())
                || (first.getHigh() != next.getHigh())
                || (first.getLow() != next.getLow())
                || (first.getOpen() != next.getOpen()));
    }


    public long getCandleDuration() {
        return candleDuration;
    }

	
	
	/*STATIC METHODS*/

    public static List<CandlePattern> generateAllPossiblePatterns(int length, long candleDuration) {

        List<CandlePattern> allPatterns = new ArrayList<CandlePattern>();

        boolean[][] allBooleanCombinations = generateAllPossibleBooleanArrays(length * NUMBER_OF_VARIABLES_ANALYZED);

        for (boolean[] combination : allBooleanCombinations) {
            allPatterns.add(new CandlePattern(Utils.splitIntoArraysOfLength(combination, NUMBER_OF_VARIABLES_ANALYZED), candleDuration));
        }

        return allPatterns;
    }

    private static boolean[][] generateAllPossibleBooleanArrays(int length) {

        boolean[][] arrays = new boolean[(int) Math.pow(2, length)][length];

        for (int i = 0; i < Math.pow(2, length); i++) {

            //In Bin�rstring umwandeln
            String bin = Integer.toBinaryString(i);

            //Mit nullen auff�llen
            while (bin.length() < length)
                bin = "0" + bin;

            char[] charArray = bin.toCharArray();
            boolean[] boolArray = new boolean[length];

            //charArrays in boolArray umwandeln
            for (int j = 0; j < charArray.length; j++) {
                boolArray[j] = charArray[j] == '0' ? true : false;
            }
            arrays[i] = boolArray;
        }

        return arrays;
    }

    public static Comparator<CandlePattern> highestProbabilityDescending() {
        return new Comparator<CandlePattern>() {
            @Override
            public int compare(CandlePattern p1, CandlePattern p2) {
                return Double.compare(Math.max(p1.probability(true), p1.probability(false)), Math.max(p2.probability(true), p2.probability(false)));
            }
        };
    }

    public static Comparator<CandlePattern> totalOccurencesAscending() {
        return new Comparator<CandlePattern>() {
            @Override
            public int compare(CandlePattern p1, CandlePattern p2) {
                return Double.compare(p1.timesFoundTotal(), p2.timesFoundTotal());
            }
        };
    }

    public static Comparator<CandlePattern> absoluteAverageMovementDescending() {
        return new Comparator<CandlePattern>() {
            @Override
            public int compare(CandlePattern p1, CandlePattern p2) {
                return Double.compare(p1.averageMovement(), p2.averageMovement());
            }
        };
    }


    public static Predicate<CandlePattern> passPredicate() {
        return new Predicate<CandlePattern>() {

            @Override
            public boolean test(CandlePattern p) {
                return Utils.sampleSize(1.64, Math.max(p.probability(true), p.probability(false)), .10) <= p.timesFoundTotal()
                        && (Math.min(p.probability(true), p.probability(false)) > 0 || p.timesFoundTotal() > 10)
                        && (Math.max(p.probability(true), p.probability(false)) > .65);
            }
        };
    }
}
