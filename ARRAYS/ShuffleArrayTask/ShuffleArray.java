package ARRAYS.ShuffleArrayTask;




import java.util.Random;

/*
 * ============================================================================
 * SHUFFLE IN-PLACE - FISHER-YATES ALGORITHM (JAVA)
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
 * for (int i = 0; i < arr.length; i++) {
 *     int j = random.nextInt(arr.length);  // Pick ANY random position
 *     // Swap arr[i] and arr[j]
 * }
 * 
 * PROBLEMS:
 * - Same element can be swapped multiple times
 * - Some elements might never move
 * - Distribution is BIASED - not all permutations equally likely
 * - Mathematically incorrect!
 * 
 * CORRECT APPROACH (Fisher-Yates):
 * for (int i = arr.length - 1; i > 0; i--) {
 *     int j = random.nextInt(i + 1);  // Pick from 0 to i ONLY
 *     // Swap arr[i] and arr[j]
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
 * public static void shuffleInPlace(int[] arr) {
 *     // arr is a REFERENCE to the array in main()
 *     // Changes to arr[i] modify the original array
 *     
 *     int temp = arr[i];           // O(1) extra space
 *     arr[i] = arr[randomIndex];   // Modify original
 *     arr[randomIndex] = temp;     // Modify original
 * }
 * 
 * MEMORY LAYOUT (Java):
 * Before calling function:
 *     data = reference to [1, 2, 3, 4, 5]  in heap memory
 * 
 * Inside function:
 *     arr = receives the same reference as data
 *     arr and data point to SAME array object
 *     Swaps modify the SAME array
 * 
 * After returning:
 *     data and arr both point to [3, 5, 1, 2, 4]  - MODIFIED!
 * 
 * JAVA vs C DIFFERENCE:
 * - In Java, arrays are OBJECTS (references)
 * - When you pass arr to function, function gets reference to same object
 * - Changes in function automatically visible in main
 * - No need for pointers or & operator like in C!
 * 
 * ============================================================================
 * RANDOM NUMBER GENERATION IN JAVA
 * ============================================================================
 * 
 * WHAT WE USE:
 * - Random random = new Random();
 *   Creates a Random object for generating random numbers
 * 
 * - random.nextInt(i + 1);
 *   Generates random integer from 0 to i (inclusive)
 * 
 * WHY nextInt(i + 1)?
 * - nextInt(n) returns 0 to n-1 (doesn't include n)
 * - nextInt(i + 1) returns 0 to i (includes i) ✓
 * 
 * Example: nextInt(5) gives 0, 1, 2, 3, or 4
 *          nextInt(5) does NOT give 5
 *          So we use nextInt(i + 1) to include i
 * 
 * JAVA ADVANTAGE:
 * - No need to seed like in C
 * - Random object automatically seeds itself with current time
 * - Creates new Random() once per shuffle is fine
 * - Or create once at class level for better practice
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
 * - Note: Random object is created, but we can optimize by
 *   creating it once as class variable
 * 
 * ============================================================================
 * KEY POINTS TO REMEMBER
 * ============================================================================
 * 
 * 1. LOOP BACKWARDS (from length-1 to 1, NOT 0 to length-1)
 *    - Forward loop would create bias
 *    - Backward loop ensures each position gets one final element
 * 
 * 2. RANDOM RANGE IS CRUCIAL (0 to i, NOT 0 to length)
 *    - random.nextInt(i + 1) gives 0 to i
 *    - This ensures uniform distribution
 *    - Wrong range ruins the entire algorithm
 * 
 * 3. ARRAYS ARE REFERENCES IN JAVA
 *    - Passed by reference automatically
 *    - Changes in function modify original array
 *    - No need for pointers or & operator like C
 * 
 * 4. IN-PLACE MODIFICATION
 *    - Original array is modified
 *    - No separate output array needed
 *    - Function modifies array directly
 * 
 * 5. EVERY PERMUTATION EQUALLY LIKELY
 *    - Not just "looks random"
 *    - Mathematically proven: each of n! permutations has probability 1/n!
 *    - This is why Fisher-Yates is the standard algorithm
 * 
 * ============================================================================
 * C vs JAVA COMPARISON FOR SHUFFLE
 * ============================================================================
 * 
 * ASPECT                  C                       JAVA
 * ─────────────────────────────────────────────────────────────────────────
 * Random Generation       rand()                  Random.nextInt()
 * 
 * Seeding                 srand(time(NULL))       Automatic
 * 
 * Random Range            rand() % (i+1)          random.nextInt(i+1)
 * 
 * Array Passing           Pointer explicitly      Reference implicitly
 *                         arr[] is int*           arr[] is reference
 * 
 * Modifications Visible   Yes (pointer)           Yes (reference)
 *                         in calling function     in calling function
 * 
 * Syntax Clarity          More verbose            More concise
 * 
 * Memory Management       Manual                  Automatic (GC)
 * 
 * ============================================================================
 */

public class ShuffleArray {
    
    public static void shuffleInPlace(int[] arr) {
        /*
         * FUNCTION: shuffleInPlace
         * 
         * PURPOSE:
         * Shuffle an array in-place using Fisher-Yates algorithm
         * 
         * PARAMETER:
         *   arr: The integer array to shuffle
         *        (passed as reference in Java)
         * 
         * RETURN:
         *   void - modifies array in-place
         * 
         * TIME: O(n) - single loop through array
         * SPACE: O(1) - only temporary variable for swapping
         */
        
        // Create Random object for generating random numbers
        // This automatically seeds itself with current time
        Random random = new Random();
        
        // Start from the LAST element and work BACKWARDS
        // This is crucial for unbiased shuffle!
        for (int i = arr.length - 1; i > 0; i--) {
            /*
             * STEP 1: Generate random index
             * 
             * random.nextInt(i + 1) generates number from 0 to i (inclusive)
             * 
             * IMPORTANT DETAIL:
             * nextInt(n) returns 0 to n-1 (doesn't include n)
             * So nextInt(i + 1) returns 0 to i (includes i) ✓
             * 
             * Example when i = 4:
             *   random.nextInt(5) gives 0, 1, 2, 3, or 4
             *   (We use nextInt(i+1) which is nextInt(5))
             * 
             * Why i+1 and not i?
             *   We need to include i in the random range
             *   nextInt(i) would give 0 to i-1 (doesn't include i)
             *   nextInt(i+1) gives 0 to i (includes i) ✓
             */
            int randomIndex = random.nextInt(i + 1);
            
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
         * This also means arr[0] stays as last remaining element
         * which is fine - by then all others are shuffled
         */
    }
    
    // Helper method to print array
    public static void printArray(int[] arr) {
        /*
         * METHOD: printArray
         * 
         * PURPOSE: Display array contents in readable format
         * 
         * OUTPUT FORMAT: [ 1 2 3 4 5 ]
         */
        System.out.print("[ ");
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println("]");
    }
    
    public static void main(String[] args) {
        /*
         * EXAMPLE USAGE OF shuffleInPlace
         */
        
        // Create array to shuffle
        int[] data = {1, 2, 3, 4, 5};
        
        // Display original array
        System.out.print("Original array: ");
        printArray(data);
        
        // Shuffle the array (modifies 'data' directly)
        // Note: In Java, arrays are references
        // Changes in shuffleInPlace() modify the original array
        shuffleInPlace(data);
        
        // Display shuffled array
        System.out.print("Shuffled array: ");
        printArray(data);
        
        // Shuffle again to demonstrate randomness
        shuffleInPlace(data);
        System.out.print("Shuffled again: ");
        printArray(data);
    }
}