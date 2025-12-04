import java.util.Arrays;
import java.util.Hashtable;

class CipherTwo {

    public static int[] S_BOX_INVERSE = {
            4, 8, 6, 10, 1, 3, 0, 5, 12, 14, 13, 15, 2, 11, 7, 9
    };

    int[] counters2 = new int[16];

    public void counterС2(Hashtable<Integer, int[][]> table) {
        for (int[][] array : table.values()) { // hashtable == {{m0, c0}, {m1, c1}}
            int c0 = array[0][1];
            int c1 = array[1][1];
            for (int k2 = 0; k2 < 16; k2++) { // k2 = {0..15}
                int x0 = c0 ^ k2;
                int x1 = c1 ^ k2;
                int w0 = S_BOX_INVERSE[x0];
                int w1 = S_BOX_INVERSE[x1];
                if ((w0 ^ w1) == 13) {
                    counters2[k2]++;
                }
            }
        }
    }

    public int bestKey(int[] array) {
        int max = array[0];
        int index = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        }
        return index;
    }
}

class CipherOne extends CipherTwo {

    int[] counters1 = new int[16];

    public Hashtable<Integer, int[][]> createMWTable(int[][] plainCipherPairs, int k2) {

        Hashtable<Integer, int[][]> mwTable = new Hashtable<>();

        int n = plainCipherPairs.length;
        int index = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                int m0 = plainCipherPairs[i][0];
                int m1 = plainCipherPairs[j][0];

                int c0 = plainCipherPairs[i][1];
                int c1 = plainCipherPairs[j][1];

                int x0 = c0 ^ k2;
                int x1 = c1 ^ k2;

                int w0 = CipherTwo.S_BOX_INVERSE[x0];
                int w1 = CipherTwo.S_BOX_INVERSE[x1];

                int[][] pair = new int[2][2];

                pair[0][0] = m0;
                pair[0][1] = w0;

                pair[1][0] = m1;
                pair[1][1] = w1;

                mwTable.put(index, pair);
                index++;
                }
            }
    return mwTable;
    }

    public void counterС1(Hashtable<Integer, int[][]> table) {
        for (int[][] array : table.values()) { // hashtable == {{m0, w0}, {m1, w1}}
            int m0 =  array[0][0];
            int m1 = array[1][0];
            int dm = m0 ^ m1;
            int w0 = array[0][1];
            int w1 = array[1][1];
            for (int k1 = 0; k1 < 16; k1++) { // k1 = {0..15}
                int v0 = w0 ^ k1;
                int v1 = w1 ^ k1;
                int u0 = CipherTwo.S_BOX_INVERSE[v0];
                int u1 = CipherTwo.S_BOX_INVERSE[v1];
                if ((u0 ^ u1) == dm) {
                    counters1[k1]++;
                }
            }
        }
    }
}

class CipherZero extends CipherTwo {

    int[] counters0 = new int[16];

    public Hashtable<Integer, int[][]> createMUTable(int[][] plainCipherPairs, int k2, int k1) {

        Hashtable<Integer, int[][]> muTable = new Hashtable<>();

        int n = plainCipherPairs.length;
        int index = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                int m0 = plainCipherPairs[i][0];
                int m1 = plainCipherPairs[j][0];

                int c0 = plainCipherPairs[i][1];
                int c1 = plainCipherPairs[j][1];

                int x0 = c0 ^ k2;
                int x1 = c1 ^ k2;

                int w0 = CipherTwo.S_BOX_INVERSE[x0];
                int w1 = CipherTwo.S_BOX_INVERSE[x1];

                int v0 = w0 ^ k1;
                int v1 = w1 ^ k1;

                int u0 = CipherTwo.S_BOX_INVERSE[v0];
                int u1 = CipherTwo.S_BOX_INVERSE[v1];

                int[][] pair = new int[2][2];

                pair[0][0] = m0;
                pair[0][1] = u0;

                pair[1][0] = m1;
                pair[1][1] = u1;

                muTable.put(index, pair);
                index++;
            }
        }
        return muTable;
    }

    public void counterС0(Hashtable<Integer, int[][]> table) {

        for (int k0 = 0; k0 < 16; k0++) {

            int hits = 0;

            for (int[][] array : table.values()) {

                int m = array[0][0];
                int u = array[0][1];

                if ((m ^ k0) == u) {
                    hits++;
                }
            }
            counters0[k0] = hits;
        }
    }


}


class CipherTwoAttack {

    public static void main(String[] args) {

        int[][] plainCipherPairs = {
                {1, 14}, {14, 9}, {2, 6}, {13, 10}, {3, 7}, {12, 11}
        };

        Hashtable<Integer, int[][]> pairsTable = new Hashtable<>();

        int n = plainCipherPairs.length;
        int index = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                int a = plainCipherPairs[i][0];
                int b = plainCipherPairs[j][0];
                if ((a ^ b) == 15) { // selecting for the difference f

                    int[][] combination = new int[2][2];
                    combination[0] = plainCipherPairs[i];
                    combination[1] = plainCipherPairs[j];

                    pairsTable.put(index, combination);
                    index++;
                }
            }
        }

        CipherTwo cipherTwo = new CipherTwo();
        cipherTwo.counterС2(pairsTable);
        System.out.println(Arrays.toString(cipherTwo.counters2));
        int k2 = cipherTwo.bestKey(cipherTwo.counters2);
        System.out.println("Recovered k2: " + k2);

        CipherOne cipherOne = new CipherOne();
        Hashtable<Integer, int[][]> table = cipherOne.createMWTable(plainCipherPairs, k2);
        cipherOne.counterС1(table);
        System.out.println(Arrays.toString(cipherOne.counters1));
        int k1 = cipherOne.bestKey(cipherOne.counters1);
        System.out.println("Recovered k1: " + k1);

        CipherZero cipherZero = new CipherZero();
        Hashtable<Integer, int[][]> tableZero = cipherZero.createMUTable(plainCipherPairs, k2, k1);
        cipherZero.counterС0(tableZero);
        System.out.println(Arrays.toString(cipherZero.counters0));
        int k0 = cipherZero.bestKey(cipherZero.counters0);
        System.out.println("Recovered k0: " + k0);
    }
}




