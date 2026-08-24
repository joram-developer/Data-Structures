#include <stdio.h>

// Find minimum element and its location
void findMin(int arr[][4], int rows, int cols, int *minVal, int *minRow, int *minCol) {
    *minVal = arr[0][0];
    *minRow = 0;
    *minCol = 0;
    
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (arr[i][j] < *minVal) {
                *minVal = arr[i][j];
                *minRow = i;
                *minCol = j;
            }
        }
    }
}

// Find maximum element and its location
void findMax(int arr[][4], int rows, int cols, int *maxVal, int *maxRow, int *maxCol) {
    *maxVal = arr[0][0];
    *maxRow = 0;
    *maxCol = 0;
    
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (arr[i][j] > *maxVal) {
                *maxVal = arr[i][j];
                *maxRow = i;
                *maxCol = j;
            }
        }
    }
}

int main() {
    int arr2D[3][4] = {
        {15, 8, 23, 12},
        {42, 5, 18, 30},
        {9, 35, 11, 6}
    };
    
    int minVal, minRow, minCol;
    int maxVal, maxRow, maxCol;
    
    findMin(arr2D, 3, 4, &minVal, &minRow, &minCol);
    findMax(arr2D, 3, 4, &maxVal, &maxRow, &maxCol);
    
    printf("2D Array:\n");
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 4; j++) {
            printf("%d ", arr2D[i][j]);
        }
        printf("\n");
    }
    
    printf("\nMinimum: %d at location [%d][%d]\n", minVal, minRow, minCol);
    printf("Maximum: %d at location [%d][%d]\n", maxVal, maxRow, maxCol);
    
    return 0;
}

/*
 * ============================================================================
 * ARRAY OPERATIONS - POINTERS & 2D ARRAYS SUMMARY
 * ============================================================================
 * 
 * KEY CONCEPTS:
 * 
 * 1. POINTERS BASICS
 *    - A pointer is a variable that HOLDS an ADDRESS (memory location)
 *    - int *ptr;        // ptr is a pointer that will hold an address
 *    - &variable;       // Gets the ADDRESS of a variable
 *    - *ptr;            // Dereferences: accesses the VALUE at that address
 * 
 * 2. WHY PASS ADDRESSES TO FUNCTIONS?
 *    - Functions can only return ONE value
 *    - To modify multiple variables and return them, pass their ADDRESSES
 *    - The function then modifies the ORIGINAL variables through pointers
 *    
 *    Example:
 *    int minVal;
 *    findMin(arr, 3, 4, &minVal, &minRow, &minCol);
 *                        ↑ Pass ADDRESS, not value
 *    
 * 3. POINTERS vs VALUES
 *    - int minVal = 5;        // minVal HOLDS the VALUE 5
 *    - int *ptr = &minVal;    // ptr HOLDS the ADDRESS of minVal
 *    - *ptr = 10;             // Dereference: put 10 at the address ptr points to
 *    
 * 4. DEREFERENCING WITH *
 *    - *ptr means: "Go to the address stored in ptr, access what's there"
 *    - *ptr = value;          // Put value at that address
 *    - if (x < *ptr)          // Compare x with the value at that address
 *    - WITHOUT * : ptr itself is just an address (a number)
 *    - WITH *    : we access what's stored at that address
 * 
 * 5. WHY COLUMNS MUST BE SPECIFIED IN 2D ARRAYS
 *    - int arr2D[3][4];       // Must specify columns!
 *    - Memory is linear: [row0][row1][row2]
 *    - To find arr[i][j]:
 *      Address = BaseAddress + (i × columns × size) + (j × size)
 *                                    ↑ Compiler needs this!
 *    - Without knowing columns, compiler can't calculate correct addresses
 * 
 * 6. MEMORY LAYOUT - 2D ARRAY EXAMPLE
 *    int arr[3][4] = {{1,2,3,4}, {5,6,7,8}, {9,10,11,12}};
 *    
 *    Visual:
 *    [1][2][3][4][5][6][7][8][9][10][11][12]  ← Linear in memory
 *    Row 0     Row 1     Row 2
 *    
 *    To find arr[1][2]:
 *    Address = Base + (1 × 4 × 4) + (2 × 4) = Base + 24 bytes
 * 
 * 7. PASSING ADDRESSES - THE COMPLETE FLOW
 *    
 *    In main():
 *    │
 *    ├─ int minVal;           // Variable at address 0x1000, contains garbage
 *    │
 *    └─ findMin(..., &minVal, ...)
 *                   ↓ Passes the ADDRESS 0x1000
 *    
 *    Inside findMin():
 *    │
 *    ├─ int *minVal;          // Parameter receives address 0x1000
 *    │
 *    ├─ *minVal = arr[0][0];  // Dereference: go to 0x1000 and store value
 *    │
 *    └─ Return (no explicit return needed - original var is modified)
 *    
 *    Back in main():
 *    │
 *    └─ minVal now contains the result (function modified the original!)
 * 
 * 8. POINTER OPERATIONS IN CODE
 *    
 *    // Setting initial value through pointer
 *    *minVal = arr[0][0];     // "Go to address, put this value"
 *    
 *    // Comparing value through pointer
 *    if (arr[i][j] < *minVal) // "Is arr[i][j] less than the value at address?"
 *    
 *    // Updating through pointer
 *    *minVal = arr[i][j];     // "Go to address, update with new value"
 *    
 * 9. KEY DIFFERENCES: C vs JAVA
 *    
 *    C:
 *    - Manual memory management with pointers
 *    - Must specify array dimensions (especially columns for 2D)
 *    - Can access and manipulate addresses directly
 *    - Faster but more error-prone
 *    
 *    Java:
 *    - No explicit pointers (references instead)
 *    - Can return arrays/objects directly
 *    - Automatic bounds checking and garbage collection
 *    - Safer but slightly slower
 * 
 * 10. COMMON OPERATIONS SUMMARY
 *    
 *    Declare pointer:        int *ptr;
 *    Get address:            &variable
 *    Assign address to ptr:  ptr = &variable;
 *    Dereference (access):   value = *ptr;
 *    Dereference (modify):   *ptr = new_value;
 *    Pass to function:       function(&variable, ...);
 *    Use in function:        *parameter = something;
 * 
 * ============================================================================
 */