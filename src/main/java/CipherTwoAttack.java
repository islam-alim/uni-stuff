
class SBox {

    public static int SBoxInverse(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i; // return index when found
            }
        }
        return -1; // return -1 if value not found
    }
}

class CipherTwoAttack {

    private final int[] S_BOX = {
            6, 4, 12, 5, 0, 7, 2, 14, 1, 15, 3, 13, 8, 10, 9, 11
    };

    int[] counters = new int[16];


    public void counter(int[][] array) { // {{m0, c0}, {m1, c1}}

            int c0 = array[0][1];
            int c1 = array[1][1];

            for (int k2 = 0; k2 < 16; k2++) { // k = {1, ... , 15}
                int x0 = c0 ^ k2;
                int x1 = c1 ^ k2;
                int w0 = SBox.SBoxInverse(S_BOX, x0);
                int w1 = SBox.SBoxInverse(S_BOX, x1);
                if ((w0 ^ w1) == 13) {
                    counters[k2]++;
                }
            }
    }

    public int largest(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++)
            if (array[i] > max)
                max = array[i];
        return max;
    }

    public static void main(String[] args) {

        //int[][] plainCipherPairs = {{1, 14}, {14, 9}, {2, 6}, {13, 10}, {3, 7}, {12, 11}};

        // all possible combinations of pairs (without repetitions)
        int[][][] pairs = {
                {{1, 14}, {14, 9}},
                {{1, 14}, {2, 6}},
                {{1, 14}, {13, 10}},
                {{1, 14}, {3, 7}},
                {{1, 14}, {12, 11}},
                {{14, 9}, {2, 6}},
                {{14, 9}, {13, 10}},
                {{14, 9}, {3, 7}},
                {{14, 9}, {12, 11}},
                {{2, 6}, {13, 10}},
                {{2, 6}, {3, 7}},
                {{2, 6}, {12, 11}},
                {{13, 10}, {3, 7}},
                {{13, 10}, {12, 11}},
                {{3, 7}, {12, 11}}
        };



        CipherTwoAttack cipherTwoAttack = new CipherTwoAttack();

        for (int[][] pair : pairs) {
            cipherTwoAttack.counter(pair);
        }
        System.out.println(cipherTwoAttack.largest(cipherTwoAttack.counters));
    }


}




