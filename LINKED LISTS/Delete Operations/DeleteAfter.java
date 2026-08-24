public class DeleteAfter {

    // ======================================================
    // NODE CLASS
    // ======================================================

    static class Node {
        int data;
        Node next;
    }


    // ======================================================
    // CREATE A NEW NODE
    // ======================================================

    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.next = null;
        return newNode;
    }


    // ======================================================
    // TRAVERSE THE LIST
    // ======================================================

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

    /*
        GOAL: Find the node containing 'target', then
        remove the node that comes immediately after it.

        Compare this with insertAfter — the idea is similar.
        We find the target using one pointer, and because
        a node knows what comes after it (via .next), we
        already have everything we need once we land on it.

        No second pointer required.

        In C this was three steps:

            struct Node *toDelete = current->next;  // 1. save
            current->next = toDelete->next;         // 2. skip over
            free(toDelete);                         // 3. release

        In Java, step 3 vanishes — no free(). But steps 1
        and 2 are still needed and still in this exact order:

            Node toDelete = current.next;   // save BEFORE overwriting
            current.next = toDelete.next;   // rewire around it

        If you skip step 1 and write current.next = current.next.next,
        the code actually works in Java too — but doing it the two-step
        way keeps your C and Java thinking identical, which is the
        whole point of writing both.
    */


    /*
        ─────────────────────────────────────────
        CASE 1: The list is empty.
        ─────────────────────────────────────────

            head
             ↓
            null

        There is no node to search and nothing after it.
    */

    static Node deleteAfter(Node head, int target) {

        if (head == null) {
            System.out.println("List is empty. Nothing to delete.");
            return head;
        }


        /*
            ─────────────────────────────────────────
            CASE 2: Walk the list to find target.
            ─────────────────────────────────────────

                head
                 ↓
               [10] → [20] → [30] → [40] → null

            Looking for target = 20: stop when current.data == 20.
            If we fall off the end (current == null), the target
            was not in the list at all.
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
            ─────────────────────────────────────────

                current
                   ↓
                  [40] → null

            There is no node after the target to delete.
        */

        if (current.next == null) {
            System.out.println("No node after " + target + ". Nothing to delete.");
            return head;
        }


        /*
            ─────────────────────────────────────────
            CASE 4: Target found, and there IS a node after it.
            ─────────────────────────────────────────

                current
                   ↓
                  [20] → [30] → [40] → null

            We want to delete [30].

            STEP 1: Save a reference to the node to delete.

                Node toDelete = current.next;

                current    toDelete
                   ↓           ↓
                  [20]  →  [30]  →  [40] → null


            STEP 2: Rewire [20] to point directly to [40],
            bypassing [30].

                current.next = toDelete.next;

                current    toDelete
                   ↓           ↓
                  [20]  →  [40] → null
                            ↑
                    toDelete.next
                    (copied into current.next)

            [30] is now unreachable from the list.


            STEP 3: Release [30]'s memory.

                In C: free(toDelete);   ← mandatory or you leak memory
                In Java: nothing — the garbage collector sees that
                nothing references [30] anymore and reclaims it.

            The key pattern here:

                Node toDelete = current.next;      // save what we're losing
                current.next = toDelete.next;      // reconnect the chain

            This is the deletion equivalent of insertAfter's
            two-line pointer update — one line to save what
            we're about to lose, one to reconnect.
        */

        Node toDelete = current.next;    // STEP 1: save the node to remove
        current.next = toDelete.next;    // STEP 2: skip over it

        return head;                     // (STEP 3 is the garbage collector's job)
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        /*
            Build the list:  10 → 20 → 30 → 40 → null
        */

        Node head = createNode(10);
        head.next = createNode(20);
        head.next.next = createNode(30);
        head.next.next.next = createNode(40);

        System.out.print("Original list:                  ");
        traverse(head);
        // 10 20 30 40

        /*
            Delete the node after 20 (which is 30).

            Before:   10 → 20 → 30 → 40
                            ↓
            After:    10 → 20 → 40
        */

        head = deleteAfter(head, 20);

        System.out.print("After deleting after 20:        ");
        traverse(head);
        // 10 20 40

        /*
            Delete the node after 10 (which is now 20).

            Before:   10 → 20 → 40
                       ↓
            After:    10 → 40
        */

        head = deleteAfter(head, 10);

        System.out.print("After deleting after 10:        ");
        traverse(head);
        // 10 40

        /*
            Try to delete after 40 — it is the last node,
            so there is nothing after it.
            Should print a message and leave the list unchanged.
        */

        head = deleteAfter(head, 40);

        System.out.print("After deleting after 40:        ");
        traverse(head);
        // 10 40 (unchanged)

        /*
            Try to delete after a value that doesn't exist.
            Should print a not-found message.
        */

        head = deleteAfter(head, 99);

        /*
            No need to free memory in Java.
            The garbage collector cleans up automatically
            once the deleted nodes become unreachable.
        */
    }
}
