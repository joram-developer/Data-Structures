# Explanation: 2D Min/Max Search & Fisher-Yates Shuffle

---

## PART 1 — 2D Min and Max Search

### What is the goal?

You have a grid of numbers (a 2D array). You want to find:
- The **smallest** number in the entire grid, and **which cell** it sits in
- The **largest** number in the entire grid, and **which cell** it sits in

---

### The Array Used

```
arr2D[3][4] = {
    {15,  8, 23, 12},   ← Row 0
    {42,  5, 18, 30},   ← Row 1
    { 9, 35, 11,  6}    ← Row 2
}
```

Think of it like a table: 3 rows, 4 columns.

```
        Col 0   Col 1   Col 2   Col 3
Row 0 |  15      8      23      12
Row 1 |  42      5      18      30
Row 2 |   9     35      11       6
```

Answer we expect:
- Minimum → **5** at row 1, col 1
- Maximum → **42** at row 1, col 0

---

### How the Algorithm Works

The idea is simple: **assume the first cell is both the min and the max, then scan every cell and update if you find something smaller or larger.**

```
Step 1: Assume arr[0][0] = 15 is the minimum (and maximum)

Step 2: Visit every cell row by row:

  Row 0:
    arr[0][0] = 15  → not less than 15, skip
    arr[0][1] =  8  → 8 < 15 ✓  → new min = 8  at [0][1]
    arr[0][2] = 23  → 23 > 15 ✓ → new max = 23 at [0][2]
    arr[0][3] = 12  → skip

  Row 1:
    arr[1][0] = 42  → 42 > 23 ✓ → new max = 42 at [1][0]
    arr[1][1] =  5  → 5 < 8  ✓  → new min = 5  at [1][1]
    arr[1][2] = 18  → skip
    arr[1][3] = 30  → skip

  Row 2:
    arr[2][0] =  9  → skip
    arr[2][1] = 35  → skip
    arr[2][2] = 11  → skip
    arr[2][3] =  6  → skip

Final answer:
  Minimum = 5 at [1][1]
  Maximum = 42 at [1][0]
```

---

### The Code (C version)

```c
void findMin(int arr[][4], int rows, int cols, int *minVal, int *minRow, int *minCol) {
    *minVal = arr[0][0];   // assume first cell is the minimum
    *minRow = 0;
    *minCol = 0;

    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < cols; j++) {
            if (arr[i][j] < *minVal) {   // found something smaller?
                *minVal = arr[i][j];      // update the minimum value
                *minRow = i;              // remember which row
                *minCol = j;              // remember which column
            }
        }
    }
}
```

---

### Why Does C Use `*minVal`, `*minRow`, `*minCol`?

This is the trickiest part. Here is the full explanation.

**The Problem:**
A function in C can only `return` one value. But we need to return three things from `findMin`: the value, the row, and the column. How?

**The Solution: Pass addresses (pointers)**

Instead of returning values, you give the function the *memory address* of your variables. The function then writes directly into those addresses.

```c
// In main(), you declare three empty variables:
int minVal, minRow, minCol;

// You pass their ADDRESSES to findMin using &
findMin(arr2D, 3, 4, &minVal, &minRow, &minCol);
//                    ↑         ↑        ↑
//               address of  address  address
//               minVal      of minRow of minCol
```

Inside `findMin`, the parameter `int *minVal` means "a pointer — it holds an address of an integer."

```
*minVal = arr[0][0];
```

The `*` here means "go to that address and write the value there."

**Analogy:**
Imagine you have a notebook at home (address = your home address). Instead of calling you and telling you the answer, I come to your house and write the answer directly in your notebook. That is what `*minVal = ...` does — it goes to the address and writes there.

```
Without *:   minVal is the ADDRESS itself (like a house number)
With *:      *minVal is the VALUE living at that address (the notebook content)
```

**Complete flow:**

```
main():
  minVal lives at memory address 0x1000, currently holds garbage
  
  findMin(..., &minVal, ...)   ← sends address 0x1000 to the function

findMin():
  *minVal = arr[0][0] = 15    ← goes to address 0x1000, writes 15 there
  ...later...
  *minVal = 5                 ← goes to address 0x1000, writes 5 there

back in main():
  minVal is now 5             ← the function modified the original variable!
```

---

### The Code (Java version)

Java does not have pointers, so the problem is solved differently. The function just returns an array containing all three values at once:

```java
public static int[] findMin(int[][] arr) {
    int minVal = arr[0][0];
    int minRow = 0, minCol = 0;

    for (int i = 0; i < arr.length; i++) {
        for (int j = 0; j < arr[i].length; j++) {
            if (arr[i][j] < minVal) {
                minVal = arr[i][j];
                minRow = i;
                minCol = j;
            }
        }
    }

    return new int[]{minVal, minRow, minCol};  // return all three in one array
}
```

In `main()`:
```java
int[] min = findMin(arr2D);
// min[0] = the minimum value
// min[1] = the row it was found in
// min[2] = the column it was found in
```

Much simpler — no pointer syntax needed.

---

### C vs Java Side-by-Side

| Aspect | C | Java |
|---|---|---|
| Return multiple values | Pass addresses with `&`, write with `*` | Return an `int[]` array |
| Reading result in caller | `minVal` (normal variable, now updated) | `min[0]`, `min[1]`, `min[2]` |
| Array size in function | Must write `arr[][4]` (columns hardcoded) | `arr.length` and `arr[i].length` (dynamic) |
| Pointer syntax | Yes (`*`, `&`) | No |

---

### Complexity

- **Time: O(rows × cols)** — every cell is visited exactly once
- **Space: O(1)** — no extra array; just a few variables to track the current best

---
---

## PART 2 — Fisher-Yates Shuffle

### What is the goal?

You have an array like `[1, 2, 3, 4, 5]`. You want to rearrange it into a **random order**, where every possible ordering has an equal chance of happening.

---

### Why Not Just Swap Randomly?

First, let's understand why a naive approach fails.

**Wrong approach:**
```c
for (int i = 0; i < size; i++) {
    int j = rand() % size;   // pick ANY random position in the whole array
    swap(arr[i], arr[j]);
}
```

This looks random but it is **biased**. Some permutations appear more often than others. The math: with 5 elements, there are 5! = 120 possible orderings. The naive loop runs 5 iterations each picking from 5 positions → 5^5 = 3125 paths. Since 3125 is not divisible by 120, some orderings get more paths than others. The shuffle is unfair.

**The fix: shrink the random range as you go.**

---

### The Fisher-Yates Algorithm

**Core idea:** Walk from the *end* of the array to the front. At each position `i`, pick a random position from `0` to `i` (not the whole array), and swap. Once you swap an element into position `i`, it is *locked* — you never touch it again.

```
Loop: i goes from (size-1) down to 1

At each step:
  1. Pick a random index j, where 0 ≤ j ≤ i
  2. Swap arr[i] with arr[j]
  3. arr[i] is now final. Move to i-1.
```

**Full walkthrough with `[1, 2, 3, 4, 5]`:**

```
Start: [ 1  2  3  4  5 ]
        ↑ unshuffled ↑

i=4: pick random j from 0..4, say j=2
     Swap arr[4] and arr[2]:
     [ 1  2  5  4  3 ]
                    ↑ LOCKED (this slot is done)

i=3: pick random j from 0..3, say j=0
     Swap arr[3] and arr[0]:
     [ 4  2  5  1  3 ]
                ↑ LOCKED

i=2: pick random j from 0..2, say j=1
     Swap arr[2] and arr[1]:
     [ 4  5  2  1  3 ]
             ↑ LOCKED

i=1: pick random j from 0..1, say j=0
     Swap arr[1] and arr[0]:
     [ 5  4  2  1  3 ]
          ↑ LOCKED

i=0: loop ends (only 1 element left, nothing to swap)

Final result: [ 5  4  2  1  3 ]
```

At every step, the "unshuffled" zone on the left shrinks by one. Each element gets exactly one chance to land in its final position.

---

### The Code (C version)

```c
void shuffleInPlace(int arr[], int size) {
    srand(time(NULL));              // seed the random number generator

    for (int i = size - 1; i > 0; i--) {
        int randomIndex = rand() % (i + 1);    // pick j from 0 to i

        // swap arr[i] and arr[randomIndex]
        int temp = arr[i];
        arr[i] = arr[randomIndex];
        arr[randomIndex] = temp;
    }
}
```

---

### Breaking Down Each Part

**`srand(time(NULL))`**

`rand()` in C produces the same sequence of numbers every time you run the program — unless you give it a different starting seed. `time(NULL)` returns the current time as a number. Since time changes every second, each run gets a different seed and therefore a different shuffle.

> Note: `srand` should ideally be called once in `main()`, not inside the shuffle function. If you call `shuffleInPlace` twice within the same second, both calls get the same seed and produce the same result. This is the known bug in your code.

---

**`rand() % (i + 1)`**

`rand()` gives a large random number. `% (i + 1)` cuts it down to the range `0` to `i`.

```
When i = 4:  rand() % 5  gives  0, 1, 2, 3, or 4   ✓
When i = 3:  rand() % 4  gives  0, 1, 2, or 3       ✓
When i = 2:  rand() % 3  gives  0, 1, or 2           ✓
When i = 1:  rand() % 2  gives  0 or 1               ✓
```

Why `% (i + 1)` and not `% i`?
Because `% i` would give `0` to `i-1`, which *excludes* `i` itself. We want to include `i` so an element can stay in place.

---

**The swap using `temp`**

```c
int temp = arr[i];          // save a copy of arr[i]
arr[i] = arr[randomIndex];  // overwrite arr[i] — the old value is gone!
arr[randomIndex] = temp;    // restore old arr[i] into arr[randomIndex]
```

You cannot do this without `temp`:
```c
// WRONG — do not do this:
arr[i] = arr[randomIndex];  // arr[i] is now overwritten
arr[randomIndex] = arr[i];  // this just copies arr[randomIndex] back to itself
```
The original value of `arr[i]` is lost in the wrong version. `temp` saves it before the overwrite.

---

**Why does the loop stop at `i > 0` and not `i >= 0`?**

When `i = 0`, there is only one element left in the unshuffled zone. Swapping element 0 with a random index from 0 to 0 just picks index 0 itself — a no-op. So stopping at `i = 1` (the condition `i > 0`) saves one useless iteration.

---

### The Code (Java version)

```java
public static void shuffleInPlace(int[] arr) {
    Random random = new Random();   // auto-seeds with current time

    for (int i = arr.length - 1; i > 0; i--) {
        int randomIndex = random.nextInt(i + 1);   // 0 to i inclusive

        int temp = arr[i];
        arr[i] = arr[randomIndex];
        arr[randomIndex] = temp;
    }
}
```

The logic is identical to C. Two differences in Java:

1. **`Random` instead of `rand()`** — `new Random()` seeds itself automatically. No `srand` needed.
2. **`random.nextInt(i + 1)`** — `nextInt(n)` returns `0` to `n-1`. So `nextInt(i + 1)` returns `0` to `i`. Same logic as `rand() % (i + 1)` in C.

Also, in Java, arrays are **references** — when you pass `arr` to the function, the function receives a reference pointing to the same array in memory. So changes inside the function automatically appear in `main()`. In C, an array argument also decays to a pointer, so both languages end up modifying the original array directly.

---

### Why Is This Guaranteed to Be Unbiased?

At step `i`, there are `i + 1` elements left to place. We pick uniformly from `i + 1` options. The math works out such that each of the `n!` total permutations has exactly probability `1/n!`. Every ordering is equally likely. This is the property that makes Fisher-Yates the standard shuffle algorithm used in real software (including programming language standard libraries).

---

### C vs Java Side-by-Side

| Aspect | C | Java |
|---|---|---|
| Random number | `rand() % (i + 1)` | `random.nextInt(i + 1)` |
| Seeding | `srand(time(NULL))` (manual) | Automatic inside `new Random()` |
| Array modification visible in caller | Yes (pointer) | Yes (reference) |
| Swap | Needs `temp` variable | Needs `temp` variable (same) |
| Loop condition | `i > 0` | `i > 0` |

---

### Complexity

- **Time: O(n)** — the loop runs exactly `n - 1` times, each doing constant work
- **Space: O(1)** — only one `temp` variable; the original array is shuffled in place

---

## Quick Reference Summary

| | 2D Min/Max Search | Fisher-Yates Shuffle |
|---|---|---|
| **What it does** | Finds smallest/largest value and its position in a grid | Randomly rearranges an array so all orderings are equally likely |
| **Key technique** | Nested loops scanning every cell; track best-so-far | Walk backwards; shrink random range each step; swap |
| **C-specific detail** | Uses `*` and `&` to return multiple values from a function | Uses `srand` + `rand() % (i+1)` |
| **Java-specific detail** | Returns `int[]` with `{value, row, col}` | Uses `Random.nextInt(i+1)` |
| **Time complexity** | O(rows × cols) | O(n) |
| **Space complexity** | O(1) | O(1) |
