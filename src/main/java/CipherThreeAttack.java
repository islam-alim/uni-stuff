
import java.util.Hashtable;

class CipherThreeAttack {

    private final int[] S_BOX_INVERSE = {
            4, 8, 6, 10, 1, 3, 0, 5, 12, 14, 13, 15, 2, 11, 7, 9
    };

    int[] counters = new int[16];


    private void counterCipherThree(Hashtable<Integer, int[][]> table) {
        for (int[][] array : table.values()) { // hashtable == {{m0, c0}, {m1, c1}}
            int c0 = array[0][1];
            int c1 = array[1][1];
            for (int k3 = 0; k3 < 16; k3++) { // k2 = {0..15}
                int z0 = c0 ^ k3;
                int z1 = c1 ^ k3;
                int y0 = S_BOX_INVERSE[z0];
                int y1 = S_BOX_INVERSE[z1];
                if ((y0 ^ y1) == 12) {
                    counters[k3]++;
                }
            }
        }
    }

    private int bestKey(int[] array) {
        int max = array[0];
        int index = 0;
        for (int i = 1; i < array.length; i++)
            if (array[i] > max) {
                max = array[i];
                index = i;
            }
        return index;
    }

    public static void main(String[] args) {

        int[][] plainCipherPairs = {
                {0, 1}, {1, 13}, {2, 8}, {3, 10},
                {4, 4}, {5, 3}, {6, 0}, {7, 2},
                {8, 15}, {9, 6}, {10, 14}, {11, 12},
                {12, 5}, {13, 11}, {14, 7}, {15, 9}
        };

        Hashtable<Integer, int[][]> pairsTable = new Hashtable<>();

        int n = plainCipherPairs.length;
        int index = 0;

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                int a = plainCipherPairs[i][0]; // pair1[0] = m0
                int b = plainCipherPairs[j][0]; // pair2[0] = m1
                if ((a ^ b) == 15) { // only pairs with difference f

                    int[][] combination = new int[2][2];
                    combination[0] = plainCipherPairs[i];
                    combination[1] = plainCipherPairs[j];

                    pairsTable.put(index, combination);
                    index++;
                }
            }
        }

        CipherThreeAttack cipherThreeAttack = new CipherThreeAttack();
        cipherThreeAttack.counterCipherThree(pairsTable);
        System.out.println("Recovered k3: " + cipherThreeAttack.bestKey(cipherThreeAttack.counters));
    }
}







