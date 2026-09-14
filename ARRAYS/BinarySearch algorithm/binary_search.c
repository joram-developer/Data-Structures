#include <stdio.h>

/**
 * Binary Search in Arrays
 * 
 * Binary search is an efficient algorithm for finding an item from a sorted list of items.
 * It works by repeatedly dividing in half the portion of the list that could contain the item,
 * until you've narrowed down the possible locations to just one.
 * 
 * Time Complexity: O(log n)
 * Space Complexity: O(1) for iterative, O(log n) for recursive
 */

/**
 * Iterative implementation of binary search
 * 
 * @param arr The sorted array to search in
 * @param size The number of elements in the array
 * @param target The value to search for
 * @return Index of the target if found, -1 otherwise
 */
int binarySearchIterative(int arr[], int size, int target) {
    int left = 0;           // Left boundary of search range
    int right = size - 1;   // Right boundary of search range
    
    while (left <= right) {
        int mid = left + (right - left) / 2;  // Calculate middle index safely
        
        // Check if target is present at mid
        if (arr[mid] == target) {
            return mid;
        }
        
        // If target is greater, ignore left half
        if (arr[mid] < target) {
            left = mid + 1;
        }
        // If target is smaller, ignore right half
        else {
            right = mid - 1;
        }
    }
    
    // Target not found
    return -1;
}

/**
 * Recursive implementation of binary search
 * 
 * @param arr The sorted array to search in
 * @param left Left boundary of search range
 * @param right Right boundary of search range
 * @param target The value to search for
 * @return Index of the target if found, -1 otherwise
 */
int binarySearchRecursive(int arr[], int left, int right, int target) {
    if (left > right) {
        // Base case: target not found
        return -1;
    }
    
    int mid = left + (right - left) / 2;  // Calculate middle index safely
    
    // Check if target is present at mid
    if (arr[mid] == target) {
        return mid;
    }
    
    // If target is smaller than mid element, search in left subarray
    if (arr[mid] > target) {
        return binarySearchRecursive(arr, left, mid - 1, target);
    }
    // Else search in right subarray
    else {
        return binarySearchRecursive(arr, mid + 1, right, target);
    }
}

/**
 * Helper function to print the array
 */
void printArray(int arr[], int size) {
    printf("Array: [");
    for (int i = 0; i < size; i++) {
        printf("%d", arr[i]);
        if (i < size - 1) {
            printf(", ");
        }
    }
    printf("]\n");
}

int main() {
    // Sorted array for binary search
    int arr[] = {2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78};
    int size = sizeof(arr) / sizeof(arr[0]);
    
    printf("Binary Search Implementation\n");
    printf("============================\n");
    printArray(arr, size);
    printf("\n");
    
    // Test cases
    int targets[] = {23, 2, 78, 1, 100, 12};
    int numTargets = sizeof(targets) / sizeof(targets[0]);
    
    printf("Testing Iterative Binary Search:\n");
    for (int i = 0; i < numTargets; i++) {
        int target = targets[i];
        int result = binarySearchIterative(arr, size, target);
        
        if (result != -1) {
            printf("Element %d found at index %d\n", target, result);
        } else {
            printf("Element %d not found\n", target);
        }
    }
    
    printf("\nTesting Recursive Binary Search:\n");
    for (int i = 0; i < numTargets; i++) {
        int target = targets[i];
        int result = binarySearchRecursive(arr, 0, size - 1, target);
        
        if (result != -1) {
            printf("Element %d found at index %d\n", target, result);
        } else {
            printf("Element %d not found\n", target);
        }
    }
    
    return 0;
}