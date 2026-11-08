#include <stdio.h>

/**
 * Selection Sort in Arrays
 *
 * Selection Sort works by repeatedly finding the minimum element from the
 * unsorted portion of the array and placing it at the beginning.
 *
 * Time Complexity: O(n^2) — always, regardless of input
 * Space Complexity: O(1) — in-place sorting, no extra memory needed
 */

int main() {

    int A[5] = {40, 10, 50, 20, 30};
    int size = 5;

    /* Outer loop: move the boundary of the unsorted subarray one step right each pass */
    for (int i = 0; i < size - 1; i++) {

        /* Assume the first element of the unsorted portion is the minimum */
        int minIndex = i;

        /* Inner loop: scan the rest of the unsorted portion to find the true minimum */
        for (int j = i + 1; j < size; j++) {

            if (A[j] < A[minIndex]) {
                /* Found a smaller element — update the index of the minimum */
                minIndex = j;
            }
        }

        /* Swap the found minimum element with the first unsorted element
           (only swap if the minimum is not already in the correct position) */
        if (minIndex != i) {
            int temp = A[minIndex];
            A[minIndex] = A[i];
            A[i] = temp;
        }
    }

    /* Display the sorted array */
    printf("Sorted array: ");
    for (int i = 0; i < size; i++) {
        printf("%d ", A[i]);
    }
    printf("\n");

    return 0;
}
