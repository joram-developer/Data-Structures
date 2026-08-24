public class DeleteAtStart {

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
    // DELETE AT THE BEGINNING
    // ======================================================

    static Node deleteAtBeginning(Node head) {

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
            CASE 2: The list has one or more nodes.

                    head
                     ↓
            null ← [10] ⇄ [20] ⇄ [30] → null

            We want to remove [10] and make [20] the new head.


            In C this needed FOUR steps:

                struct Node *temp = head;   // 1. save address for free()
                head = head->next;          // 2. move head forward
                if (head != NULL)           // 3. clear new head's prev
                    head->prev = NULL;      //    (guarded — see below)
                free(temp);                 // 4. release memory


            In Java, steps 1 and 4 vanish (no free(), no temp).
            Step 2 is one line:

                head = head.next;

            Step 3 STILL EXISTS and still needs its guard:

                if (head != null)
                    head.prev = null;

            ⚠ THE DLL-SPECIFIC STEP: [20].prev still points at the
            deleted [10]. If we leave it that way, rule 1 breaks and
            anyone walking backwards from head lands on garbage.
            The guard matters when the list had exactly ONE node:
            after step 2, head is null, and writing head.prev would
            throw a NullPointerException.

            The deleted node becomes unreachable → GC reclaims it.
        */

        head = head.next;              // STEP 2: move head forward

        if (head != null) {            // STEP 3: clear prev — guarded!
            head.prev = null;
        }

        return head;
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        /*
            Build:  null ← [10] ⇄ [20] ⇄ [30] → null
        */

        Node n1 = createNode(10);
        Node n2 = createNode(20);
        Node n3 = createNode(30);

        n1.next = n2;
        n2.prev = n1;
        n2.next = n3;
        n3.prev = n2;

        Node head = n1;

        System.out.print("Original list:           ");
        traverse(head);              // 10 20 30

        /*
            Delete the first node.

            Before:   null ← [10] ⇄ [20] ⇄ [30]
            After:    null ← [20] ⇄ [30]
        */

        head = deleteAtBeginning(head);

        System.out.print("After deleting at start: ");
        traverse(head);              // 20 30

        /*
            Delete again.
        */

        head = deleteAtBeginning(head);

        System.out.print("After deleting at start: ");
        traverse(head);              // 30

        /*
            Delete the last remaining node.
            Exercises the `if (head != null)` guard — after this
            deletion head is null and there is no prev to clear.
        */

        head = deleteAtBeginning(head);

        System.out.print("After deleting at start: ");
        traverse(head);              // (blank line — empty list)

        /*
            Try to delete from an empty list.
            Should print a message and do nothing.
        */

        head = deleteAtBeginning(head);
    }
}
