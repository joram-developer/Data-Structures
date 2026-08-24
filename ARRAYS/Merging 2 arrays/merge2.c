#include <stdio.h>

void merge1D(int A1[], int size1, int A2[], int size2, int result[]) {
    // Copy A1 to result
    for (int i = 0; i < size1; i++) {
        result[i] = A1[i];
    }
    
    // Copy A2 to result
    for (int i = 0; i < size2; i++) {
        result[size1 + i] = A2[i];
    }
}

void printArray(int arr[], int size) {
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
    printf("\n");
}

int main() {
    int A1[] = {1, 2, 3, 4, 5};
    int A2[] = {6, 7, 8, 9};
    int size1 = 5, size2 = 4;
    int result[9];  // Combined size
    
    printf("Array 1: ");
    printArray(A1, size1);
    
    printf("Array 2: ");
    printArray(A2, size2);
    
    merge1D(A1, size1, A2, size2, result);
    
    printf("Merged Array: ");
    printArray(result, size1 + size2);
    
    return 0;
}