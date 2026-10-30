public class ArrayTraversal {
    public static void main(String[] args) {

        // =======================
        // 1D Array Traversal
        // =======================

        int[] A = {10, 20, 30, 40, 50};
        int size = A.length;

        System.out.println("1D Array Traversal:");

        // Visit every element from index 0 to the last index
        for (int i = 0; i < size; i++) {
            System.out.print(A[i] + " ");
        }
        System.out.println();

        // =======================
        // 2D Array Traversal
        // =======================

        int[][] B = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };

        System.out.println("\n2D Array Traversal (Row-Major Order):");

        // Outer loop walks through each row
        // Inner loop walks through each column within that row
        for (int i = 0; i < B.length; i++) {
            for (int j = 0; j < B[i].length; j++) {
                System.out.print("B[" + i + "][" + j + "]=" + B[i][j] + "  ");
            }
            System.out.println();
        }

        // =======================
        // 3D Array Traversal
        // =======================

        /*
         * A 3D array is an array of 2D arrays.
         * Think of it as a stack of grids (like the pages of a book):
         *
         *   Dimension 1 (i) = which "layer" / page
         *   Dimension 2 (j) = which row inside that layer
         *   Dimension 3 (k) = which column inside that row
         *
         * C[i][j][k] means: layer i, row j, column k.
         *
         * Memory layout (row-major order):
         * All elements of layer 0 come first, then layer 1, then layer 2.
         * Within each layer, elements are stored row by row.
         *
         * Visualised:
         *
         *  Layer 0:          Layer 1:          Layer 2:
         *  [ 1  2  3]        [13 14 15]        [25 26 27]
         *  [ 4  5  6]        [16 17 18]        [28 29 30]
         *  [ 7  8  9]        [19 20 21]        [31 32 33]
         *  [10 11 12]        [22 23 24]        [34 35 36]
         */

        int[][][] C = {
            {                           // Layer 0
                { 1,  2,  3},
                { 4,  5,  6},
                { 7,  8,  9},
                {10, 11, 12}
            },
            {                           // Layer 1
                {13, 14, 15},
                {16, 17, 18},
                {19, 20, 21},
                {22, 23, 24}
            },
            {                           // Layer 2
                {25, 26, 27},
                {28, 29, 30},
                {31, 32, 33},
                {34, 35, 36}
            }
        };

        int layers  = C.length;         // number of 2D layers  = 3
        int rows    = C[0].length;      // rows per layer        = 4
        int cols    = C[0][0].length;   // columns per row       = 3

        System.out.println("\n3D Array Traversal (Layer by Layer, Row-Major Order):");

        // Outermost loop  → moves through each layer
        for (int i = 0; i < layers; i++) {

            System.out.println("  Layer " + i + ":");

            // Middle loop  → moves through each row inside the current layer
            for (int j = 0; j < rows; j++) {

                System.out.print("    ");

                // Innermost loop  → moves through each column in the current row
                for (int k = 0; k < cols; k++) {
                    System.out.printf("C[%d][%d][%d]=%-4d", i, j, k, C[i][j][k]);
                }
                System.out.println();
            }
        }
    }
}