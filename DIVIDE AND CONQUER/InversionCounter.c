#include <stdio.h>
#include <stdlib.h>

/*"An inversion is a pair of elements A[i] and A[j] where i < j but A[i] > A[j]. Instead of checking every pair directly in O(n²), 
our algorithm uses divide-and-conquer based on merge sort. It recursively divides the array into smaller halves, counts inversions within the left half and right half, and then counts inversions between the two halves while merging them. This gives an O(n log n) solution."*/

void printArray(int arr[], int n) {
    printf("[");
    for (int i = 0; i < n; i++) {
        printf("%d", arr[i]);
        if (i < n - 1) {
            printf(", ");
        }
    }
    
    printf("]\n");
}

int mergeAndCount(int arr[], int left, int mid, int right) {
    int leftSize = mid - left + 1;
    int rightSize = right - mid;

    int *leftArray = (int *)malloc(leftSize * sizeof(int));
    int *rightArray = (int *)malloc(rightSize * sizeof(int));

    for (int i = 0; i < leftSize; i++) {
        leftArray[i] = arr[left + i];
    }

    for (int j = 0; j < rightSize; j++) {
        rightArray[j] = arr[mid + 1 + j];
    }

    int i = 0, j = 0, k = left;
    int crossInversions = 0;

    while (i < leftSize && j < rightSize) {
        if (leftArray[i] <= rightArray[j]) {
            arr[k++] = leftArray[i++];
        } else {
            arr[k++] = rightArray[j++];
            crossInversions += (leftSize - i);
        }
    }

    while (i < leftSize) {
        arr[k++] = leftArray[i++];
    }

    while (j < rightSize) {
        arr[k++] = rightArray[j++];
    }

    free(leftArray);
    free(rightArray);
    return crossInversions;
}

int countInversions(int arr[], int left, int right) {
    if (left >= right) {
        return 0;
    }

    int mid = left + (right - left) / 2;

    int leftInversions = countInversions(arr, left, mid);
    int rightInversions = countInversions(arr, mid + 1, right);
    int crossInversions = mergeAndCount(arr, left, mid, right);

    return leftInversions + rightInversions + crossInversions;
}

int main() {
    int arr[] = {2, 4, 1, 3, 5};
    int n = sizeof(arr) / sizeof(arr[0]);

    printf("Input Array: ");
    printArray(arr, n);

    int inversionCount = countInversions(arr, 0, n - 1);
    printf("Number of inversions: %d\n", inversionCount);

    return 0;
}
