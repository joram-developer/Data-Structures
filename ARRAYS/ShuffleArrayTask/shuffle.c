#include <stdio.h>
#include <stdlib.h>
#include <time.h>

/*
 * ============================================================================
 * SHUFFLE IN-PLACE - FISHER-YATES ALGORITHM
 * ============================================================================
 * 
 * PROBLEM:
 * Write a function that shuffles an array in-place. The shuffle must be
 * truly random, meaning every permutation must be equally likely.
 * 
 * CONSTRAINTS:
 * - In-place: Don't create a new array, modify the original
 * - Truly random: Every permutation has equal probability
 * - Time: O(n) - visit each element once
 * - Space: O(1) - only use temporary variable for swapping
 * 
 * EXAMPLE:
 * Input:  [1, 2, 3, 4, 5]
 * Output: [3, 5, 1, 2, 4]  (or any other random permutation)
 * 
 * ============================================================================
 * THE ALGORITHM: FISHER-YATES SHUFFLE
 * ============================================================================
 * 
 * CORE IDEA:
 * Shuffle from BACK to FRONT by repeatedly:
 * 1. Pick a random element from the unshuffled portion
 * 2. Swap it with the current position
 * 3. Move backwards to the next position
 * 
 * WHY THIS WORKS:
 * - Each position gets exactly ONE final random element
 * - No element can be picked twice for the same position
 * - Mathematically proven to give uniform distribution
 * - Every possible permutation has equal probability: 1/n!
 * 
 * VISUAL EXAMPLE WITH [1, 2, 3, 4, 5]:
 * 
 * Start: [1, 2, 3, 4, 5]
 *                    ↑ Start here (last position)
 * 
 * i=4: Random from 0-4 = 2 → Swap arr[4] with arr[2]
 *      [1, 2, 5, 4, 3]
 *           ↑ This is now locked in place
 * 
 * i=3: Random from 0-3 = 0 → Swap arr[3] with arr[0]
 *      [4, 2, 5, 1, 3]
 *     ↑ This is now locked in place
 * 
 * i=2: Random from 0-2 = 1 → Swap arr[2] with arr[1]
 *      [4, 5, 2, 1, 3]
 *        ↑ This is now locked in place
 * 
 * i=1: Random from 0-1 = 0 → Swap arr[1] with arr[0]
 *      [5, 4, 2, 1, 3]
 *     ↑ This is now locked in place
 * 
 * i=0: Loop ends (no more elements to compare)
 * Final: [5, 4, 2, 1, 3] ✓ Completely shuffled!
 * 
 * ============================================================================
 * WHY NOT JUST RANDOM SWAPS?
 * ============================================================================
 * 
 * WRONG APPROACH:
 * for (int i = 0; i < size; i++) {
 *     int j = rand() % size;  // Pick ANY random position
 *     swap(arr[i], arr[j]);
 * }
 * 
 * PROBLEMS:
 * - Same element can be swapped multiple times
 * - Some elements might never move
 * - Distribution is BIASED - not all permutations equally likely
 * - Mathematically incorrect!
 * 
 * CORRECT APPROACH (Fisher-Yates):
 * for (int i = size - 1; i > 0; i--) {
 *     int j = rand() % (i + 1);  // Pick from 0 to i ONLY
 *     swap(arr[i], arr[j]);
 * }
 * 
 * BENEFITS:
 * - Each position gets exactly ONE final element
 * - No element is locked until we move past it
 * - Uniform distribution - all permutations equally likely
 * - Mathematically proven correct!
 * 
 * ============================================================================
 * IN-PLACE EXPLANATION
 * ============================================================================
 * 
 * IN-PLACE MEANS:
 * - Modify the ORIGINAL array directly
 * - Don't create a new array
 * - Space complexity: O(1) (only temporary variables)
 * 
 * OUR IMPLEMENTATION:
 * void shuffleInPlace(int arr[], int size) {
 *     // arr is passed by reference (it's already a pointer)
 *     // Changes to arr[i] modify the original array in main()
 *     
 *     int temp = arr[i];           // O(1) extra space
 *     arr[i] = arr[randomIndex];   // Modify original
 *     arr[randomIndex] = temp;     // Modify original
 * }
 * 
 * MEMORY LAYOUT:
 * Before calling function:
 *     arr = [1, 2, 3, 4, 5]  in main()'s memory
 * 
 * Inside function:
 *     arr still points to main()'s array
 *     Swaps modify the SAME array
 * 
 * After returning:
 *     arr = [3, 5, 1, 2, 4]  in main()'s memory - MODIFIED!
 * 
 * ============================================================================
 * RANDOM NUMBER GENERATION IN C
 * ============================================================================
 * 
 * WHAT WE USE:
 * - srand(time(NULL));  Seeds the random generator with current time
 * - rand() % (i + 1);   Generates random number from 0 to i
 * 
 * WHY srand()?
 * - Without srand(), rand() produces the same sequence every time
 * - time(NULL) gives a different seed each time program runs
 * - Ensures different shuffle each execution
 * 
 * WHY % (i + 1)?
 * - rand() gives large numbers
 * - % (i + 1) narrows it to range 0 to i
 * - Example: rand() % 5 gives 0, 1, 2, 3, or 4
 * 
 * TIMING NOTE:
 * - srand(time(NULL)) is called ONCE in the function
 * - If called multiple times in quick succession, seed might be same
 * - For one shuffle per program run, this is fine
 * 
 * ============================================================================
 * COMPLEXITY ANALYSIS
 * ============================================================================
 * 
 * TIME COMPLEXITY: O(n)
 * - We loop from n-1 down to 1 = n iterations
 * - Each iteration does constant work (random, swap)
 * - Total: n * O(1) = O(n)
 * 
 * SPACE COMPLEXITY: O(1)
 * - Only temporary variable 'temp' for swapping
 * - No extra arrays or data structures
 * - Works directly on input array
 * 
 * ============================================================================
 * KEY POINTS TO REMEMBER
 * ============================================================================
 * 
 * 1. LOOP BACKWARDS (from size-1 to 1, NOT 0 to size-1)
 *    - Forward loop would create bias
 *    - Backward loop ensures each position gets one final element
 * 
 * 2. RANDOM RANGE IS CRUCIAL (0 to i, NOT 0 to size)
 *    - rand() % (i + 1) gives 0 to i
 *    - This ensures uniform distribution
 *    - Wrong range ruins the entire algorithm
 * 
 * 3. SEED ONCE (srand called once per shuffle)
 *    - Calling srand multiple times in quick succession won't help
 *    - time(NULL) might return same value
 * 
 * 4. IN-PLACE MODIFICATION
 *    - Original array is modified
 *    - No separate output array needed
 *    - Function modifies array argument directly
 * 
 * 5. EVERY PERMUTATION EQUALLY LIKELY
 *    - Not just "looks random"
 *    - Mathematically proven: each of n! permutations has probability 1/n!
 *    - This is why Fisher-Yates is the standard algorithm
 * 
 * ============================================================================
 */

void shuffleInPlace(int arr[], int size) {
    /*
     * FUNCTION: shuffleInPlace
     * 
     * PURPOSE:
     * Shuffle an array in-place using Fisher-Yates algorithm
     * 
     * PARAMETERS:
     *   arr:  Pointer to the array to shuffle
     *   size: Number of elements in the array
     * 
     * RETURN:
     *   void - modifies array in-place
     * 
     * TIME: O(n) - single loop through array
     * SPACE: O(1) - only temporary variable
     */
    
    // Seed random number generator with current time
    // This ensures different shuffle each program run
    srand(time(NULL));
    
    // Start from the LAST element and work BACKWARDS
    // This is crucial for unbiased shuffle!
    for (int i = size - 1; i > 0; i--) {
        /*
         * STEP 1: Generate random index
         * 
         * rand() % (i + 1) generates number from 0 to i (inclusive)
         * 
         * Example when i = 4:
         *   rand() % 5 gives 0, 1, 2, 3, or 4
         * 
         * Why i+1 and not i?
         *   Because we want to include i in the random range
         *   rand() % i gives 0 to i-1 (doesn't include i)
         *   rand() % (i+1) gives 0 to i (includes i) ✓
         */
        int randomIndex = rand() % (i + 1);
        
        /*
         * STEP 2: Swap current element with random element
         * 
         * This is the KEY operation of Fisher-Yates:
         * - Take element at position i (which will be locked in place)
         * - Replace it with element from random position (0 to i)
         * - Put the original element at the random position
         * 
         * After this swap:
         *   arr[i] now contains element from arr[randomIndex]
         *   arr[randomIndex] now contains element from arr[i]
         * 
         * SWAP IMPLEMENTATION:
         * We can't do: arr[i] = arr[randomIndex];
         *              arr[randomIndex] = arr[i];
         * Because arr[i] is already overwritten!
         * 
         * That's why we need temp variable:
         */
        
        // Save arr[i] before we overwrite it
        int temp = arr[i];
        
        // Put the random element into position i
        arr[i] = arr[randomIndex];
        
        // Put the saved element into the random position
        arr[randomIndex] = temp;
        
        /*
         * VISUAL OF SWAP:
         * Before: arr = [1, 2, 5, 4, 3]
         *                  i=3, randomIndex=0
         * 
         * temp = arr[3] = 4
         * arr[3] = arr[0] = 1
         * arr[0] = temp = 4
         * 
         * After:  arr = [4, 2, 5, 1, 3]
         *              Elements at i and randomIndex are swapped!
         */
    }
    
    /*
     * WHY LOOP ENDS AT i > 0 (not i >= 0)?
     * 
     * When i = 0, there's only one element left
     * No need to shuffle one element with itself
     * So we stop at i = 1 (when i > 0 becomes false)
     * 
     * This also means array[0] stays as last remaining element
     * which is fine - by then all others are shuffled
     */
}

// Helper function to print array
void printArray(int arr[], int size) {
    /*
     * FUNCTION: printArray
     * 
     * PURPOSE: Display array contents in readable format
     * 
     * OUTPUT FORMAT: [ 1 2 3 4 5 ]
     */
    printf("[ ");
    for (int i = 0; i < size; i++) {
        printf("%d ", arr[i]);
    }
    printf("]\n");
}

int main() {
    /*
     * EXAMPLE USAGE OF shuffleInPlace
     */
    
    // Create array to shuffle
    int data[] = {1, 2, 3, 4, 5};
    int size = 5;
    
    // Display original array
    printf("Original array: ");
    printArray(data, size);
    
    // Shuffle the array (modifies 'data' directly)
    shuffleInPlace(data, size);
    
    // Display shuffled array
    printf("Shuffled array: ");
    printArray(data, size);
    
    // Shuffle again to demonstrate randomness
    shuffleInPlace(data, size);
    printf("Shuffled again: ");
    printArray(data, size);
    
    return 0;
}