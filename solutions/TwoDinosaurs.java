import java.math.BigInteger;

public class TwoDinosaurs {

    private static final int OFFSET = 400000;
    private static final BigInteger NOT_CALC = BigInteger.valueOf(-1);

    private int numFood;
    private int[] raphael;
    private int[] leonardo;
    private int maxDiff;
    private BigInteger[][] memoTable; // prepare for BigInteger

    private BigInteger dp(int foodIndex, int difference) {
        if (foodIndex >= numFood) {
            if (Math.abs(difference) <= maxDiff) {
                return BigInteger.ONE;
            } else {
                return BigInteger.ZERO;
            }
        } else if (memoTable[foodIndex][difference + OFFSET] != NOT_CALC) {
            return memoTable[foodIndex][difference + OFFSET];
        }

        BigInteger ans = BigInteger.ZERO;

        // Take both
        ans = ans.add(dp(foodIndex + 1,difference + raphael[foodIndex] - leonardo[foodIndex]));
        // Take Raphael
        ans = ans.add(dp(foodIndex + 1, difference + raphael[foodIndex]));
        // Take Leo
        ans = ans.add(dp(foodIndex + 1, difference - leonardo[foodIndex]));
        // Take nothing
        ans = ans.add(dp(foodIndex + 1, difference));

        return memoTable[foodIndex][difference + OFFSET] = ans;
    }


    public TwoDinosaurs(int numFood, int[] raphael, int[] leonardo, int maxDiff) {
        this.numFood = numFood;
        this.raphael = raphael;
        this.leonardo = leonardo;
        this.maxDiff = maxDiff;

        int maxIntermediateDiff = 0;
        for (int food : raphael) {
            maxIntermediateDiff += food;
        }

        memoTable = new BigInteger[numFood][maxIntermediateDiff + OFFSET + 1];
        for (int i = 0; i < numFood; ++i) {
            for (int j = 0; j < maxIntermediateDiff + OFFSET + 1; ++j) {
                memoTable[i][j] = NOT_CALC;
            }
        }
    }

    public BigInteger solve() {
        return dp(0, 0);
    }

    public static void main(String[] args) {
        TwoDinosaurs dinosaurs = new TwoDinosaurs(2, new int[]{4, 5}, new int[]{3, 6}, 3);
        System.out.println(dinosaurs.solve());
    }
}
