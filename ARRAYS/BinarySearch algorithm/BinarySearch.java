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
public class BinarySearch {
    
    /**
     * Iterative implementation of binary search
     * 
     * @param arr The sorted array to search in
     * @param target The value to search for
     * @return Index of the target if found, -1 otherwise
     */
    public static int binarySearchIterative(int[] arr, int target) {
        int left = 0;           // Left boundary of search range
        int right = arr.length - 1;   // Right boundary of search range
        
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
    public static int binarySearchRecursive(int[] arr, int left, int right, int target) {
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
    public static void printArray(int[] arr) {
        System.out.print("Array: [");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    public static void main(String[] args) {
        // Sorted array for binary search
        int[] arr = {2, 5, 8, 12, 16, 23, 38, 45, 56, 67, 78};
        
        System.out.println("Binary Search Implementation");
        System.out.println("============================");
        printArray(arr);
        System.out.println();
        
        // Test cases
        int[] targets = {23, 2, 78, 1, 100, 12};
        
        System.out.println("Testing Iterative Binary Search:");
        for (int target : targets) {
            int result = binarySearchIterative(arr, target);
            
            if (result != -1) {
                System.out.println("Element " + target + " found at index " + result);
            } else {
                System.out.println("Element " + target + " not found");
            }
        }
        
        System.out.println("\nTesting Recursive Binary Search:");
        for (int target : targets) {
            int result = binarySearchRecursive(arr, 0, arr.length - 1, target);
            
            if (result != -1) {
                System.out.println("Element " + target + " found at index " + result);
            } else {
                System.out.println("Element " + target + " not found");
            }
        }
    }
}