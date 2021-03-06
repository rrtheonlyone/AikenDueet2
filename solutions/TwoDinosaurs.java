public class TwoDinosaurs {

    private static final int OFFSET = 400000;
    private static final int MODULO = 100_000_123;

    private int numFood;
    private int[] raphael;
    private int[] leonardo;
    private int maxDiff;

    public TwoDinosaurs(int numFood, int[] raphael, int[] leonardo, int maxDiff) {
        this.numFood = numFood;
        this.raphael = raphael;
        this.leonardo = leonardo;
        this.maxDiff = maxDiff;
    }

    public long solve() {
        int maxIntermediateDiff = 0;
        for (int food : raphael) {
            maxIntermediateDiff += food;
        }
        int memoSize = maxIntermediateDiff + OFFSET + 1;

        long[] memoTable = new long[memoSize];
        for (int i = 0; i < memoSize; ++i) {
            memoTable[i] = 0;
        }

        memoTable[OFFSET] = 1; // Initial value

        int[][] cases = new int[4][2];
        cases[0][0] = 1; cases[0][1] = 1; // Eat both
        cases[1][0] = 0; cases[1][1] = 1; // Leonard eats
        cases[2][0] = 1; cases[2][1] = 0; // Raphael eats
        cases[3][0] = 0; cases[3][1] = 0; // No one eats.

        for (int foodIndex = 0; foodIndex < numFood; ++foodIndex) {
            long[] tempMemoTable = new long[memoSize];
            for (int i = 0; i < memoSize; ++i) {
                tempMemoTable[i] = 0;
            }

            for (int diff = 0; diff < memoSize; ++diff) {
                if (memoTable[diff] == 0) {
                    continue;
                }

                for (int i = 0; i < 4; ++i) {
                    int nextDiff = diff + cases[i][0] * raphael[foodIndex] - cases[i][1] * leonardo[foodIndex];
                    if (nextDiff >= 0 && nextDiff < memoSize) {
                        tempMemoTable[nextDiff] = (tempMemoTable[nextDiff] + memoTable[diff]) % MODULO;
                    }
                }
            }

            memoTable = tempMemoTable;
        }

        long output = 0;
        for (int i = Math.max(0, OFFSET - maxDiff); i <= Math.min(memoSize - 1, OFFSET + maxDiff); ++i) {
            output = (output + memoTable[i]) % MODULO;
        }

        return output;
    }

    public static void main(String[] args) {
        int size = 200;
        int[] raph = new int[size];
        int[] leo = new int[size];
        for (int i = 0; i < size; ++i) {
            raph[i] = 2000 - i;
            leo[i] = 1800 + i;
        }

        TwoDinosaurs dinosaurs = new TwoDinosaurs(size, raph, leo, 200000);

//        int[] raph = new int[] {4, 5};
//        int[] leo = new int[] {3, 6};
//
//        TwoDinosaurs dinosaurs = new TwoDinosaurs(2, raph, leo,3);
        System.out.println(dinosaurs.solve());
    }
}
