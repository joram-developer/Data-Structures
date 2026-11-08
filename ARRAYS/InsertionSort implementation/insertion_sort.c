#include <stdio.h>

/**
 * Insertion Sort in Arrays
 *
 * Insertion Sort builds a sorted array one element at a time.
 * It picks each element from the unsorted portion and inserts it
 * into its correct position in the already-sorted portion — just
 * like sorting a hand of playing cards.
 *
 * Time Complexity:
 *   Best case  — O(n)   : array is already sorted (no shifts needed)
 *   Worst case — O(n^2) : array is reverse sorted (maximum shifts)
 * Space Complexity: O(1) — in-place, no extra array needed
 */

int main() {

    int A[5] = {40, 10, 50, 20, 30};
    int size = 5;

    /* Outer loop: start from the second element (index 1)
       A[0] on its own is trivially already "sorted" */
    for (int i = 1; i < size; i++) {

        /* Pick the current element — this is what we want to place correctly */
        int key = A[i];

        /* j starts just left of the current element and scans backwards
           through the sorted portion */
        int j = i - 1;

        /* Shift every sorted element that is greater than key one position right
           This creates a gap for key to drop into */
        while (j >= 0 && A[j] > key) {
            A[j + 1] = A[j]; /* move element one slot to the right */
            j--;              /* move pointer one step left */
        }

        /* Place key in its correct position
           j + 1 is either where we started (key was already in place)
           or the gap we just created */
        A[j + 1] = key;
    }

    /* Display the sorted array */
    printf("Sorted array: ");
    for (int i = 0; i < size; i++) {
        printf("%d ", A[i]);
    }
    printf("\n");

    return 0;
}
