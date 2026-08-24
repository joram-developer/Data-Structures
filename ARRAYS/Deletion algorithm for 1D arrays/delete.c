#include <stdio.h>

int main() {
    int A[5] = {10, 20, 30, 40, 50};

    int size = 5;
    int location = 2;

    // Check whether the array is empty
    if (size == 0) {
        printf("Array is empty. Cannot delete.\n");
        return 0;
    }
    else if(location < 0 || location >= size) {
        printf("Invalid location. Cannot delete.\n");
        return 0;
    }

    else{

    // Shift elements to the left
    for (int i = location; i < size - 1; i++) {
        A[i] = A[i + 1];
    }

    // Decrease the size
    size--;

    // Display the array
    printf("Array after deletion: ");

    for (int i = 0; i < size; i++) {
        printf("%d ", A[i]);
    }
    }
    return 0;
}