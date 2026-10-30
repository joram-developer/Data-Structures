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
    int leftSize = mid - left + 1;//leftsize stores how many elements are in the left half
                                  //we do mid - left + 1 because mid is inclusive in the left half, so we add 1 to include it in the count
    int rightSize = right - mid;//rightsize stores how many elements are in the right half
                                //here we don't add 1 because mid is exclusive in the right half, so we don't include it in the count

    int *leftArray = (int *)malloc(leftSize * sizeof(int));//dynamically allocate memory for the left half of the array
    int *rightArray = (int *)malloc(rightSize * sizeof(int));//dynamically allocate memory for the right half of the array

    for (int i = 0; i < leftSize; i++) {
        leftArray[i] = arr[left + i];
    }

    for (int j = 0; j < rightSize; j++) {
        rightArray[j] = arr[mid + 1 + j];
    }

    int i = 0, j = 0, k = left; // i=0 points to the first element of the leftarray, j=0 points to the first element of the right array
    int crossInversions = 0;    // k=left identifies where we should write the next sorted element in the original array
    //the sorting happens
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

    int mid = left + (right - left) / 2;//we do left + (right - left) because

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
