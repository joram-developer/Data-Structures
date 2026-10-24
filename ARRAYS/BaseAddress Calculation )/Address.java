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
    }
}
