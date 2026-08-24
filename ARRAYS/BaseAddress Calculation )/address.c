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

arr2D:  [10 20 30 40] [50 60 70 80] [90 100 110 120]
         ↑ (points to start of 4-int array, not just an int)
      int (*)[4]

That's why 2D needs casting to int* - to flatten the 2D structure into a 1D sequence!*/

    return 0;
}