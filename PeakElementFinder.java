package da_coders;
import java.util.Arrays;

/*
    The Problem: Finding the Peak ElementGiven an array of integers nums, find a peak element, and return its index.
    A peak element is an element that is strictly greater than its neighbors.
    The Rules & Constraints:nums[-1] and nums[n] are considered negative infinity (\(-\infty \)).
    This means if an element is at the very edge of the array, it only needs to be greater than its single internal neighbor to be a peak.
    You may assume nums[i] != nums[i + 1] for all valid i (no two adjacent elements are equal).
    The Catch: Your solution must run in \(O(\log n)\) time complexity.
*/

public class PeakElementFinder {

    public static void main(String[] args) {
        int[] nums = {1, 2, 1, 3, 5, 6, 4};

        System.out.println("Input Array: " + Arrays.toString(nums));
        int peakIndex = findPeakElement(nums);
        System.out.println("Peak found at index: " + peakIndex + " (Value: " + nums[peakIndex] + ")");
    }

    /**
     * Finds a peak element index using the Divide and Conquer strategy.
     * Time Complexity: O(log n)
     * Space Complexity: O(1)
     */
    public static int findPeakElement(int[] nums) {
        int low = 0;
        int high = nums.length - 1;

        // Binary search loop
        while (low < high) {
            int mid = low + (high - low) / 2;

            // Divide: Look at the neighbor to the right
            if (nums[mid] < nums[mid + 1]) {
                // If the right neighbor is greater, a peak MUST exist in the right half.
                // Move our boundary to focus entirely on the right side.
                low = mid + 1;
            } else {
                // If the current element is greater than or equal to its right neighbor,
                // a peak could be the current element itself or lie in the left half.
                high = mid;
            }
        }

        // Conquer/Combine: When low == high, they point exactly to a peak element.
        return low;
    }
}
