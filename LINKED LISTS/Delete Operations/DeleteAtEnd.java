public class DeleteAtEnd {

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
    // DELETE AT THE END
    // ======================================================

    /*
        Core pointer idea:

            previous.next = null;

        The last node is the ONLY node whose predecessor we must
        change — we cannot walk backwards to it. So we use the
        same two-pointer technique as insertBefore:

            previous → trails one step behind current

        When current reaches the last node, previous is standing
        on the second-to-last node, and cutting the link is one line.
    */

    static Node deleteAtEnd(Node head) {

        /*
            ─────────────────────────────────────────
            CASE 1: The list is empty.
            ─────────────────────────────────────────

                head
                 ↓
                null

            Nothing to delete. Return head unchanged.
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
               [10] → null

            That single node IS the last node.
            After deleting it, the list is empty.

            We cannot use the two-pointer walk below because
            there is no "previous" node — head itself is the
            last node.

            In C this case was:

                free(head);
                head = null;

            In Java there is no free(). We simply set
            head to null and [10] becomes garbage that the
            collector reclaims.

                head = null;
        */

        if (head.next == null) {
            head = null;   // single node removed — list is now empty
            return head;
        }


        /*
            ─────────────────────────────────────────
            CASE 3: The list has TWO or more nodes.
            ─────────────────────────────────────────

                head
                 ↓
               [10] → [20] → [30] → null

            We want to remove [30] and make [20] point to null.


            STEP 1: Walk until current is the LAST node.

                while (current.next != null) {
                    previous = current;
                    current = current.next;
                }

            State when the loop ends:

                previous    current
                   ↓           ↓
                  [20]   →   [30] → null


            STEP 2: Disconnect [30] by making previous point to null.

                previous.next = null;

            State:

                previous
                   ↓
                  [20] → null      [30] → null  (still in memory!)

            STEP 3: Release the last node.

                In C: free(current);
                In Java: nothing to do — once no reference points
                to [30], it is unreachable and the garbage
                collector removes it automatically.
        */

        Node current = head;
        Node previous = null;

        // STEP 1: walk until current is the last node
        while (current.next != null) {
            previous = current;
            current = current.next;
        }

        // current is now the last node, previous is the one before it
        previous.next = null;   // STEP 2: cut the link to the last node

        return head;
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        /*
            Build the list:  10 → 20 → 30 → null
        */

        Node head = createNode(10);
        head.next = createNode(20);
        head.next.next = createNode(30);

        System.out.print("Original list:           ");
        traverse(head);
        // 10 20 30

        /*
            Delete the last node.

            Before:   10 → 20 → 30
                                  ↓
            After:    10 → 20
        */

        head = deleteAtEnd(head);

        System.out.print("After deleting at end:   ");
        traverse(head);
        // 10 20

        /*
            Delete again — now 20 is the last node.

            Before:   10 → 20
                             ↓
            After:    10
        */

        head = deleteAtEnd(head);

        System.out.print("After deleting at end:   ");
        traverse(head);
        // 10

        /*
            Delete the last remaining node.
            This triggers CASE 2 (single node).

            Before:   10
                       ↓
            After:    (empty list)
        */

        head = deleteAtEnd(head);

        System.out.print("After deleting at end:   ");
        traverse(head);
        // (blank line — empty list)

        /*
            Try to delete from an empty list.
            Should print a message and do nothing.
        */

        head = deleteAtEnd(head);
    }
}
