#include <stdio.h>

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

int findPeakElement(int nums[], int n) {
    int low = 0;
    int high = n - 1;

    while (low < high) {
        int mid = low + (high - low) / 2;

        if (nums[mid] < nums[mid + 1]) {
            low = mid + 1;
        } else {
            high = mid;
        }
    }

    return low;
}

int main() {
    int nums[] = {1, 2, 1, 3, 5, 6, 4};
    int n = sizeof(nums) / sizeof(nums[0]);

    printf("Input Array: ");
    printArray(nums, n);

    int peakIndex = findPeakElement(nums, n);
    printf("Peak element found at index: %d (Value: %d)\n", peakIndex, nums[peakIndex]);

    return 0;
}
