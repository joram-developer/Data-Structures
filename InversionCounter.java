package da_coders;

import java.util.Arrays;

public class InversionCounter{
    /*
    The Problem: Counting Inversions in an Array
    An inversion is a pair of elements in an array that are out of their natural sorted order.
    Formally, given an array arr, two elements arr[i] and arr[j] form an inversion if:i < j (the element appears earlier in the array)arr[i] > arr[j] (but its value is greater than the later element)
    */

    public static void main(String[] args) {
        int[] arr = {2, 4, 1, 3, 5};

        System.out.println("Input Array: " + Arrays.toString(arr));

        // We clone the array to preserve the original, as merge sort modifies it
        int inversionCount = countInversions(arr.clone(), 0, arr.length - 1);

        System.out.println("Number of inversions: " + inversionCount);
    }

    // Divide and Conquer wrapper
    public static int countInversions(int[] arr, int left, int right) {
        int count = 0;
        if (left < right) {
            int mid = left + (right - left) / 2;

            // 1. Count inversions in the left half
            count += countInversions(arr, left, mid);

            // 2. Count inversions in the right half
            count += countInversions(arr, mid + 1, right);

            // 3. Count "split" inversions during the merge step
            count += mergeAndCount(arr, left, mid, right);
        }
        return count;
    }

    // Combine and Count step
    private static int mergeAndCount(int[] arr, int left, int mid, int right) {
        // Create copies of the left and right subarrays
        int[] leftArray = Arrays.copyOfRange(arr, left, mid + 1);
        int[] rightArray = Arrays.copyOfRange(arr, mid + 1, right + 1);

        int i = 0, j = 0, k = left;
        int swaps = 0;

        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k++] = leftArray[i++];
            } else {
                arr[k++] = rightArray[j++];

                // CRITICAL LOGIC: If leftArray[i] > rightArray[j], then
                // rightArray[j] is smaller than ALL remaining elements in leftArray.
                swaps += (leftArray.length - i);
            }
        }

        // Copy remaining elements
        while (i < leftArray.length) {
            arr[k++] = leftArray[i++];
        }
        while (j < rightArray.length) {
            arr[k++] = rightArray[j++];
        }

        return swaps;
    }
}
