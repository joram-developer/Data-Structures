public class DeleteAfter {

    // ======================================================
    // NODE CLASS + HELPERS
    // ======================================================

    static class Node {
        int data;
        Node prev;
        Node next;
    }

    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.prev = null;
        newNode.next = null;
        return newNode;
    }

    static void traverse(Node head) {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }


    // ======================================================
    // DELETE AFTER A TARGET
    // ======================================================

    static Node deleteAfter(Node head, int target) {

        /*
            GOAL: Find the node containing 'target', then
            remove the node that comes immediately after it.

            Same idea as the SLL version: one reference finds the
            target, and the node after it is reached via .next.
            The DLL difference shows up at the END, where the
            connection must be cut in BOTH directions.
        */


        /*
            ─────────────────────────────────────────
            CASE 1: The list is empty.
            ─────────────────────────────────────────
        */

        if (head == null) {
            System.out.println("List is empty. Nothing to delete.");
            return head;
        }


        /*
            ─────────────────────────────────────────
            CASE 2: Walk the list to find target.

                head
                 ↓
            null ← [10] ⇄ [20] ⇄ [30] ⇄ [40] → null

            Looking for target = 20.
        */

        Node current = head;

        while (current != null && current.data != target) {
            current = current.next;
        }

        // Target was not found
        if (current == null) {
            System.out.println("Target " + target + " was not found.");
            return head;
        }


        /*
            ─────────────────────────────────────────
            CASE 3: Target found, but nothing after it.

                current
                   ↓
               ... [40] → null

            There is no node after the target to delete.
        */

        if (current.next == null) {
            System.out.println("No node after " + target + ". Nothing to delete.");
            return head;
        }


        /*
            ─────────────────────────────────────────
            CASE 4: Target found, and there IS a node after it.

                current
                   ↓
            null ← [20] ⇄ [30] ⇄ [40] → null

            We want to delete [30].

            STEP 1: Save a reference to the node to delete.

                Node toDelete = current.next;

                current    toDelete
                   ↓           ↓
            null ← [20] ⇄  [30]  ⇄ [40] → null


            STEP 2: Reconnect the FORWARD direction — make [20]
            point straight at [40], skipping [30].

                current.next = toDelete.next;


            STEP 3: THE NEW DLL LINE — reconnect the BACKWARD
            direction. The SLL version had nothing like this,
            because SLL nodes have no prev. If [40] exists,
            its prev must stop pointing at the doomed [30]:

                IF toDelete.next != null:
                    toDelete.next.prev = current;   // [40].prev → [20]

            ⚠ THE GUARD: if we deleted the LAST node ([30] with no
            successor), then toDelete.next is null and there is no
            node whose prev needs fixing. Skipping this check throws
            a NullPointerException on exactly that case.


            STEP 4 (C only): free(toDelete). In Java there is no
            free() — once nothing references [30], the GC reclaims
            it. Note the C version must read toDelete->next BEFORE
            free(); Java has no such trap.

            Boundary check afterwards:
                [20].next == [40] ✓ and [40].prev == [20] ✓
                Rules 3 & 4 hold across the splice in both directions.

            Head never changes — we delete strictly AFTER an
            existing node, so head always survives.

            The key pattern:

                Node toDelete = current.next;      // save
                current.next = toDelete.next;      // forward cut
                IF survivor exists: survivor.prev = current;  // backward cut
        */

        Node toDelete = current.next;               // STEP 1: save the doomed node
        current.next = toDelete.next;               // STEP 2: forward cut

        if (toDelete.next != null) {                // STEP 3: guarded backward cut
            toDelete.next.prev = current;           //         survivor looks back
        }

        return head;                                // (STEP 4 is the GC's job)
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        /*
            Build:  null ← [10] ⇄ [20] ⇄ [30] ⇄ [40] → null
        */

        Node n1 = createNode(10);
        Node n2 = createNode(20);
        Node n3 = createNode(30);
        Node n4 = createNode(40);

        n1.next = n2;
        n2.prev = n1;
        n2.next = n3;
        n3.prev = n2;
        n3.next = n4;
        n4.prev = n3;

        Node head = n1;

        System.out.print("Original list:                  ");
        traverse(head);              // 10 20 30 40

        /*
            Delete the node after 20 (which is 30) — middle case,
            both cuts fire.

            Before:   null ← [10] ⇄ [20] ⇄ [30] ⇄ [40]
            After:    null ← [10] ⇄ [20] ⇄ [40]
        */

        head = deleteAfter(head, 20);

        System.out.print("After deleting after 20:        ");
        traverse(head);              // 10 20 40

        /*
            Delete the node after 10 (which is now 20).
        */

        head = deleteAfter(head, 10);

        System.out.print("After deleting after 10:        ");
        traverse(head);              // 10 40

        /*
            Try to delete after 40 — last node, nothing after it.
            Exercises CASE 3 / the guard's negative path.
        */

        head = deleteAfter(head, 40);

        System.out.print("After deleting after 40:        ");
        traverse(head);              // 10 40 (unchanged)

        /*
            Try to delete after a value that doesn't exist.
        */

        head = deleteAfter(head, 99);

        /*
            No need to free memory in Java.
            The garbage collector cleans up automatically
            once the deleted nodes become unreachable.
        */
    }
}
