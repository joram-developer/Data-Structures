#include <stdio.h>

int main() {
    int A[5] = {10, 20, 30, 40};
    int capacity = 5;
    int size = 4;

    int key = 25;
    int location = 2;

    // Check if the array is full
    if (size == capacity) {
        printf("Array is full. Cannot insert.\n");
        return 0;
    }
    else{

    // Shift elements to the right
    for (int i = size; i > location; i--) {
        A[i] = A[i - 1];
    }

    // Insert the new element
    A[location] = key;

    // Increase size
    size++;

    // Display array
    printf("Array after insertion: ");

    for (int i = 0; i < size; i++) {
        printf("%d ", A[i]);
    }
}

    return 0;
}