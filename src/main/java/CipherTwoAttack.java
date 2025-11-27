import java.util.Hashtable;

class CipherTwoAttack {

    private final int[] S_BOX_INVERSE = {
            4, 8, 6, 10, 1, 3, 0, 5, 12, 14, 13, 15, 2, 11, 7, 9
    };

    int[] counters = new int[16];


    private void counter(Hashtable<Integer, int[][]> table) {
        for (int[][] array : table.values()) { // hashtable == {{m0, c0}, {m1, c1}}
            int c0 = array[0][1];
            int c1 = array[1][1];
            for (int k2 = 0; k2 < 16; k2++) { // k2 = {0..15}
                int x0 = c0 ^ k2;
                int x1 = c1 ^ k2;
                int w0 = S_BOX_INVERSE[x0];
                int w1 = S_BOX_INVERSE[x1];
                if ((w0 ^ w1) == 13) {
                    counters[k2]++;
                }
            }
        }
    }


    protected int largest(int[] array) {
        int max = array[0];
        for (int i = 1; i < array.length; i++)
            if (array[i] > max)
                max = array[i];
        return max;
    }

    public static void main(String[] args) {



        //int[][] plainCipherPairs = {{1, 14}, {14, 9}, {2, 6}, {13, 10}, {3, 7}, {12, 11}};

        // all possible combinations of pairs (without repetitions)
//        int[][][] pairs = {
//                {{1, 14}, {14, 9}},
//                {{1, 14}, {2, 6}},
//                {{1, 14}, {13, 10}},
//                {{1, 14}, {3, 7}},
//                {{1, 14}, {12, 11}},
//                {{14, 9}, {2, 6}},
//                {{14, 9}, {13, 10}},
//                {{14, 9}, {3, 7}},
//                {{14, 9}, {12, 11}},
//                {{2, 6}, {13, 10}},
//                {{2, 6}, {3, 7}},
//                {{2, 6}, {12, 11}},
//                {{13, 10}, {3, 7}},
//                {{13, 10}, {12, 11}},
//                {{3, 7}, {12, 11}}
//        };

        int[][] plainCipherPairs = {
                {1, 14}, {14, 9}, {2, 6}, {13, 10}, {3, 7}, {12, 11}
        };

        Hashtable<Integer, int[][]> pairsTable = new Hashtable<>();

        int n = plainCipherPairs.length;
        int index = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                int a = plainCipherPairs[i][0]; // pair1[0]
                int b = plainCipherPairs[j][0]; // pair2[0]
                if ((a ^ b) == 15) {

                    int[][] combination = new int[2][2];
                    combination[0] = plainCipherPairs[i];
                    combination[1] = plainCipherPairs[j];

                    pairsTable.put(index, combination);
                    index++;
                }
            }
        }

        System.out.println("These are all pairs:");
        for (int key : pairsTable.keySet()) {
            int[][] p = pairsTable.get(key);
            System.out.println(
                    "[" + p[0][0] + "," + p[0][1] + "]  ⊕  [" +
                            p[1][0] + "," + p[1][1] + "]"
            );
        }




        CipherTwoAttack cipherTwoAttack = new CipherTwoAttack();
        cipherTwoAttack.counter(pairsTable);
        System.out.println("Answer: " + cipherTwoAttack.largest(cipherTwoAttack.counters));
    }
}




