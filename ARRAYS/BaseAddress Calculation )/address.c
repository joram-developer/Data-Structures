#include <stdio.h>


int main(void) {
    // C Example
    int arr1D[] = {10, 20, 30, 40, 50};
    int size = 5;
    // Base address of the array
    int *baseAddr = arr1D;  // or &arr1D[0]
    printf("Base Address: %p\n", (void *)baseAddr);

    // Traversal
    for (int i = 0; i < size; i++) {
        printf("Index %d: Value = %d, Address = %p\n", 
               i, arr1D[i], (void *)&arr1D[i]);
    }

    //2D Example
    
int arr2D[3][4] = {
    {1, 2, 3, 4},
    {5, 6, 7, 8},
    {9, 10, 11, 12}
};

// Base address
int *baseAddr = (int *)arr2D;  // Cast to int pointer
printf("Base Address: %p\n", baseAddr);

// Traversal (Row-Major Order - C default)
for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 4; j++) {
        printf("arr2D[%d][%d] = %d at %p\n", 
               i, j, arr2D[i][j], &arr2D[i][j]);
    }
}

/*Visual Explanation

1D Array Layout:

arr1D:  [10][20][30][40][50]
         ↑
      int* (directly points to sequence of ints)

2D Array Layout:

arr2D:  [1  2  3  4] [5  6  7  8] [9  10 11 12]
         ↑ (points to start of a 4-int row, not a single int)
      int (*)[4]

That's why 2D needs casting to int* - to flatten the 2D structure into a 1D sequence!*/

    /* =======================
       3D Array Example
       ======================= */

    /*
     * A 3D array is an array of 2D arrays.
     * Think of it as a stack of grids (like the pages of a book):
     *
     *   Dimension 1 (i) = which layer  (which page)
     *   Dimension 2 (j) = which row    (which line on the page)
     *   Dimension 3 (k) = which column (which character on the line)
     *
     * arr3D[i][j][k] means: layer i, row j, column k.
     *
     * Memory Layout (Row-Major Order):
     * C stores the entire 3D array as ONE contiguous flat block of ints.
     * The formula for the address of arr3D[i][j][k] is:
     *
     *   address = baseAddr + (i * ROWS * COLS + j * COLS + k) * sizeof(int)
     *
     * For our 2-layer, 3-row, 4-column array (ROWS=3, COLS=4):
     *
     *   arr3D[0][0][0]  ->  offset 0   (first element)
     *   arr3D[0][0][1]  ->  offset 4   (next int, 4 bytes on)
     *   arr3D[0][1][0]  ->  offset 16  (= 1 * 4 * 4 bytes, new row)
     *   arr3D[1][0][0]  ->  offset 48  (= 1 * 3 * 4 * 4 bytes, new layer)
     *
     * Visualised:
     *
     *  Layer 0:              Layer 1:
     *  [ 1  2  3  4]         [13 14 15 16]
     *  [ 5  6  7  8]         [17 18 19 20]
     *  [ 9 10 11 12]         [21 22 23 24]
     *
     * Flat in memory:
     * [1][2][3][4][5][6][7][8][9][10][11][12][13][14][15][16]...[24]
     *  ↑
     *  baseAddr (arr3D cast to int*)
     */

    int LAYERS = 2, ROWS = 3, COLS = 4;

    int arr3D[2][3][4] = {
        {                       /* Layer 0 */
            { 1,  2,  3,  4},
            { 5,  6,  7,  8},
            { 9, 10, 11, 12}
        },
        {                       /* Layer 1 */
            {13, 14, 15, 16},
            {17, 18, 19, 20},
            {21, 22, 23, 24}
        }
    };

    /* Base address: cast to int* to flatten the 3D structure.
       In C, arr3D decays to a pointer to its first element,
       which is a 2D array (int (*)[3][4]). Casting to int*
       gives us a pointer to the very first int. */
    int *baseAddr3D = (int *)arr3D;
    printf("\nBase Address (3D): %p\n", (void *)baseAddr3D);

    /* Traversal: three nested loops — layer, row, column.
       The address of arr3D[i][j][k] is calculated using
       the row-major offset formula. */
    for (int i = 0; i < LAYERS; i++) {
        for (int j = 0; j < ROWS; j++) {
            for (int k = 0; k < COLS; k++) {
                int offset = i * ROWS * COLS + j * COLS + k;
                printf("arr3D[%d][%d][%d] = %2d  at %p\n",
                       i, j, k,
                       arr3D[i][j][k],
                       (void *)&arr3D[i][j][k]);
            }
        }
    }

    /*
     * Why cast to (int *) for 3D?
     *
     * arr3D has type  int (*)[3][4]  — pointer to a 2D array.
     * To treat the whole thing as a flat sequence of ints
     * (and do offset arithmetic with sizeof(int) steps),
     * we cast to int*.  Same reason as the 2D case.
     *
     * The pointer arithmetic confirms the formula:
     *   &arr3D[i][j][k]  ==  baseAddr3D + (i*ROWS*COLS + j*COLS + k)
     */

    return 0;
}