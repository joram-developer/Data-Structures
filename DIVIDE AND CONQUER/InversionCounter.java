
import java.util.Arrays;

public class InversionCounter {
    /*
    The Problem: Counting Inversions in an Array
    An inversion is a pair of elements in an array that are out of sorted order.
    Formally, arr[i] and arr[j] form an inversion if:
        i < j and arr[i] > arr[j]
    */

    public static void main(String[] args) {
        int[] arr = {2, 4, 1, 3, 5};

        System.out.println("Input Array: " + Arrays.toString(arr));

        // Clone the array so the original stays unchanged
        int inversionCount = countInversions(arr.clone(), 0, arr.length - 1);

        System.out.println("Number of inversions: " + inversionCount);
    }

    // Divide and Conquer: split the problem into smaller pieces
    public static int countInversions(int[] arr, int left, int right) {
        if (left >= right) {
            return 0;
        }

        int mid = left + (right - left) / 2;

        // 1. Count inversions in the left half
        int leftInversions = countInversions(arr, left, mid);

        // 2. Count inversions in the right half
        int rightInversions = countInversions(arr, mid + 1, right);

        // 3. Count inversions that cross the two halves while merging
        int crossInversions = mergeAndCount(arr, left, mid, right);

        return leftInversions + rightInversions + crossInversions;
    }

    // Merge step: combine two sorted halves and count cross inversions
    private static int mergeAndCount(int[] arr, int left, int mid, int right) {
        int[] leftArray = Arrays.copyOfRange(arr, left, mid + 1);
        int[] rightArray = Arrays.copyOfRange(arr, mid + 1, right + 1);

        int i = 0; // index in leftArray
        int j = 0; // index in rightArray
        int k = left; // index in original array
        int crossCount = 0;

        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k++] = leftArray[i++];
            } else {
                // leftArray[i] > rightArray[j]
                // so rightArray[j] forms an inversion with every remaining element
                // in leftArray from i to the end.
                arr[k++] = rightArray[j++];
                crossCount += (leftArray.length - i);
            }
        }

        while (i < leftArray.length) {
            arr[k++] = leftArray[i++];
        }

        while (j < rightArray.length) {
            arr[k++] = rightArray[j++];
        }

        return crossCount;
    }
}