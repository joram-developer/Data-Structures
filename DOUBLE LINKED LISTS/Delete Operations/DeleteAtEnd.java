public class DeleteAtEnd {

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
    // DELETE AT THE END
    // ======================================================

    static Node deleteAtEnd(Node head) {

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
            CASE 2: The list has exactly ONE node.
            ─────────────────────────────────────────

                head
                 ↓
            null ← [10] → null

            head IS the last node. In C this was:
                free(head); head = NULL;
            In Java: just set head to null — GC reclaims [10].
        */

        if (head.next == null) {
            head = null;
            return head;
        }


        /*
            ─────────────────────────────────────────
            CASE 3: The list has TWO or more nodes.

                    head
                     ↓
            null ← [10] ⇄ [20] ⇄ [30] → null

            We want to remove [30] and make [20] point to null.


            ⭐ THE BIG DIFFERENCE FROM THE SLL VERSION:

            In the SLL we dragged TWO references through the walk:

                previous → trails one step behind current

            because once current reached the last node, there was no
            way back to [20].

            In the DLL, [30] KNOWS its own predecessor:
            current.prev IS "previous". One reference is enough.


            STEP 1: Walk until current is the last node.

                while (current.next != null)
                    current = current.next;

                        head    current
                         ↓         ↓
            null ← [10] ⇄ [20] ⇄ [30] → null


            STEP 2: Cut the link from the SURVIVING side.

                SLL needed only:
                    previous.next = null;

                DLL needs the same single surviving-side cut:

                    current.prev.next = null;   // [20].next → null

                What about [30].prev? It still points at [20], but
                that's fine — [30] is about to become unreachable,
                so its stale prev disappears with it. Only the
                SURVIVING node's references must be corrected.

            Boundary check: [20].next == null ✓ (rule 2 restored).

            STEP 3 (C only): free(current). Java: nothing — GC.
        */

        Node current = head;

        while (current.next != null) {      // STEP 1: walk to the last node
            current = current.next;
        }

        current.prev.next = null;           // STEP 2: forward cut on survivor

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
            Delete the last node.

            Before:   null ← [10] ⇄ [20] ⇄ [30]
            After:    null ← [10] ⇄ [20]
        */

        head = deleteAtEnd(head);

        System.out.print("After deleting at end:   ");
        traverse(head);              // 10 20

        /*
            Delete again — now 20 is the last node.
        */

        head = deleteAtEnd(head);

        System.out.print("After deleting at end:   ");
        traverse(head);              // 10

        /*
            Delete the final remaining node — exercises CASE 2.
        */

        head = deleteAtEnd(head);

        System.out.print("After deleting at end:   ");
        traverse(head);              // (blank line — empty list)

        /*
            Try to delete from an empty list.
            Should print a message and do nothing.
        */

        head = deleteAtEnd(head);
    }
}
