/**
 * Simple Memory Allocation Practice
 *
 * Java equivalent of simplepractice.c
 *
 * The C version demonstrates dynamic memory allocation for a single int
 * using malloc(), assigning a value, printing it, and freeing it.
 *
 * In Java, primitive values like int are stored directly and do not need
 * to be dynamically allocated with malloc. The garbage collector also
 * means there is no free() call.
 *
 * To faithfully mirror the C concept of "a pointer to a single allocated
 * int", we use an Integer object (a reference to a boxed int). This is the
 * closest Java analogue to "int *p = malloc(sizeof(int)); *p = 25;".
 *
 *   C:    int *p = malloc(sizeof(int));   // p points to heap memory
 *         *p = 25;                        // write through the pointer
 *         printf("Value: %d\n", *p);
 *         free(p);                         // release the memory
 *
 *   Java: Integer p = 25;                 // p is a reference to an Integer
 *         System.out.println("Value: " + p);
 *         // no free() - GC reclaims p when it is no longer reachable
 */
public class SimplePractice {

    public static void main(String[] args) {

        // Declare a reference to an Integer (Java's analogue of int*).
        Integer p;

        // "Allocate" - in Java we just assign. Java's "new"/autoboxing handles memory.
        p = 25;

        // Check if memory allocation was successful.
        // In C: if (p == NULL) { ... }
        // In Java: "new"/autoboxing never returns null, so no check is needed.
        // (On exhaustion the JVM throws OutOfMemoryError instead of returning null.)

        // Assign a value to the allocated memory.
        // C: *p = 25;   Java: p = 25;  (autoboxes int -> Integer)

        System.out.println("Value: " + p);

        // Free the allocated memory.
        // In C: free(p);
        // In Java there is no free(); when p goes out of scope the garbage
        // collector reclaims the object automatically.
    }
}
