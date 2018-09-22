public class TwoDinosaurs {

    private static final int OFFSET = 400000;
    private static final long NOT_CALC = -1;

    private int numFood;
    private int[] raphael;
    private int[] leonardo;
    private int maxDiff;
    private long[][] memoTable; // prepare for BigInteger

    private long dp(int foodIndex, int difference) {
        if (foodIndex >= numFood) {
            if (Math.abs(difference) <= maxDiff) {
                return 1;
            } else {
                return 0;
            }
        } else if (memoTable[foodIndex][difference + OFFSET] != NOT_CALC) {
            return memoTable[foodIndex][difference + OFFSET];
        }

        memoTable[foodIndex][difference + OFFSET] = 0;

        long ans = 0;

        // Take both
        ans += dp(foodIndex + 1,difference + raphael[foodIndex] - leonardo[foodIndex]);

        // Take Raphael
        ans += dp(foodIndex + 1, difference + raphael[foodIndex]);

        // Take Leo
        ans += dp(foodIndex + 1, difference - leonardo[foodIndex]);

        // Take nothing
        ans += dp(foodIndex + 1, difference);

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

        memoTable = new long[numFood][maxIntermediateDiff + OFFSET + 1];
        for (int i = 0; i < numFood; ++i) {
            for (int j = 0; j < maxIntermediateDiff + OFFSET + 1; ++j) {
                memoTable[i][j] = NOT_CALC;
            }
        }
    }

    public long solve() {
        return dp(0, 0);
    }

    public static void main(String[] args) {
        TwoDinosaurs dinosaurs = new TwoDinosaurs(2, new int[]{4, 5}, new int[]{3, 6}, 3);
        System.out.println(dinosaurs.solve());
    }
}
