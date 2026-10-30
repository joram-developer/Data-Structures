/**
 * Base Address Calculation and Array Traversal
 *
 * Java equivalent of address.c
 *
 * NOTE ON ADDRESSES:
 *   In C, every array element has a real memory address we can print with
 *   the & operator and %p format. C arrays are just contiguous blocks of
 *   memory, so the base address plus an offset gives any element's address.
 *
 *   In Java, arrays are objects and memory is managed by the JVM. Java does
 *   NOT expose raw memory addresses of individual elements. As a stand-in,
 *   we use System.identityHashCode() which returns a unique integer for each
 *   object. This is NOT a real memory address, but it serves the same
 *   educational purpose of showing that an array is a single object with a
 *   single "base" identifier.
 *
 *   The traversal logic (1D and 2D, row-major order) is identical to the C
 *   version.
 */
public class Address {

    public static void main(String[] args) {

        // =======================
        // 1D Array Example
        // =======================

        int[] arr1D = {10, 20, 30, 40, 50};
        int size = 5;

        // "Base address" of the array object (stand-in for &arr1D[0]).
        // In C this was: int *baseAddr = arr1D;
        int baseAddr = System.identityHashCode(arr1D);
        System.out.println("Base Address: 0x" + Integer.toHexString(baseAddr));

        // Traversal
        for (int i = 0; i < size; i++) {
            // In C: printf("Index %d: Value = %d, Address = %p", i, arr1D[i], &arr1D[i]);
            // In Java we cannot get a per-element address for primitives,
            // so we show the index, value, and the conceptual offset.
            System.out.println("Index " + i + ": Value = " + arr1D[i]
                    + ", Address = 0x" + Integer.toHexString(baseAddr)
                    + " + " + i + " * 4 bytes");
        }

        // =======================
        // 2D Array Example
        // =======================

        int[][] arr2D = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12}
        };

        // "Base address" of the 2D array object.
        // In C: int *baseAddr = (int *)arr2D;
        int baseAddr2D = System.identityHashCode(arr2D);
        System.out.println("\nBase Address: 0x" + Integer.toHexString(baseAddr2D));

        // Traversal (Row-Major Order - C default)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                // In C: printf("arr2D[%d][%d] = %d at %p", i, j, arr2D[i][j], &arr2D[i][j]);
                // Java can't expose per-element addresses, so we show value + position.
                System.out.println("arr2D[" + i + "][" + j + "] = " + arr2D[i][j]
                        + " at row " + i + ", col " + j);
            }
        }

        /*
         * Visual Explanation
         *
         * 1D Array Layout:
         *
         * arr1D:  [10][20][30][40][50]
         *          ^ base address (identity hash code of the array object)
         *
         * In C this is a contiguous block of ints; the base pointer points
         * directly to the first int. In Java it is a single array object and
         * the elements live inside that object.
         *
         * 2D Array Layout (Row-Major Order):
         *
         * arr2D:  [1  2  3  4]    row 0
         *         [5  6  7  8]    row 1
         *         [9 10 11 12]    row 2
         *
         * In C a 2D array is one contiguous block; arr2D points to the start
         * of a row of 4 ints. That is why 2D needs casting to int* - to flatten
         * the 2D structure into a 1D sequence.
         *
         * In Java, int[][] is an array of arrays (each row is a separate
         * object), so there is no single contiguous block to flatten.
         */

        // =======================
        // 3D Array Example
        // =======================

        /*
         * A 3D array adds a third dimension on top of the 2D grid.
         * Think of it as a stack of 2D grids — like the pages of a book.
         *
         *   Dimension 1 (i) = which layer  (which page)
         *   Dimension 2 (j) = which row    (which line on the page)
         *   Dimension 3 (k) = which column (which character on the line)
         *
         * arr3D[i][j][k] means: layer i, row j, column k.
         *
         * Memory Layout (Row-Major Order in C):
         * All elements are stored in one contiguous flat block.
         * The formula for the flat offset of arr3D[i][j][k] is:
         *
         *   offset = (i * ROWS * COLS + j * COLS + k) * 4 bytes
         *
         * where ROWS = rows per layer, COLS = columns per row.
         *
         * For our 2-layer, 3-row, 4-column array (ROWS=3, COLS=4):
         *
         *   arr3D[0][0][0]  →  offset 0
         *   arr3D[0][0][1]  →  offset 4
         *   arr3D[0][1][0]  →  offset 16   (= 1 * 4 * 4 bytes)
         *   arr3D[1][0][0]  →  offset 48   (= 1 * 3 * 4 * 4 bytes)
         *
         * Visualised:
         *
         *  Layer 0:              Layer 1:
         *  [ 1  2  3  4]         [13 14 15 16]
         *  [ 5  6  7  8]         [17 18 19 20]
         *  [ 9 10 11 12]         [21 22 23 24]
         *
         * In Java, int[][][] is an array of arrays of arrays.
         * Each layer and each row is a separate object on the heap,
         * so there is no single contiguous block. The base address
         * below is the identity hash of the outermost array object.
         */

        int[][][] arr3D = {
            {                              // Layer 0
                { 1,  2,  3,  4},
                { 5,  6,  7,  8},
                { 9, 10, 11, 12}
            },
            {                              // Layer 1
                {13, 14, 15, 16},
                {17, 18, 19, 20},
                {21, 22, 23, 24}
            }
        };

        int LAYERS = arr3D.length;           // 2 layers
        int ROWS   = arr3D[0].length;        // 3 rows per layer
        int COLS   = arr3D[0][0].length;     // 4 columns per row

        // "Base address" of the 3D array object.
        // In C: int *baseAddr3D = (int *)arr3D;
        int baseAddr3D = System.identityHashCode(arr3D);
        System.out.println("\nBase Address (3D): 0x" + Integer.toHexString(baseAddr3D));

        // Traversal (Layer → Row → Column)
        // In C: arr3D[i][j][k] lives at baseAddr + (i*ROWS*COLS + j*COLS + k) * 4
        for (int i = 0; i < LAYERS; i++) {
            for (int j = 0; j < ROWS; j++) {
                for (int k = 0; k < COLS; k++) {
                    // Calculate the conceptual flat offset the same way C would
                    int offset = (i * ROWS * COLS + j * COLS + k);
                    System.out.println("arr3D[" + i + "][" + j + "][" + k + "] = "
                            + arr3D[i][j][k]
                            + "  at 0x" + Integer.toHexString(baseAddr3D)
                            + " + " + offset + " * 4 bytes");
                }
            }
        }
    }
}
