public class CombinedDeletion {

    // ======================================================
    // NODE CLASS + HELPERS
    // ======================================================

    static class Node {
        int data;
        Node next;
    }

    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
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
    // 1. DELETE AT THE BEGINNING
    // ======================================================

    /*
        Core pointer idea:

            head = head.next;

        In C this needed three steps (save temp → advance → free).
        In Java the save and the free vanish — once nothing
        references the old node, the GC reclaims it.
    */

    static Node deleteAtBeginning(Node head) {

        if (head == null) {
            System.out.println("List is empty. Nothing to delete.");
            return head;
        }

        head = head.next;   // old first node becomes unreachable garbage

        return head;
    }


    // ======================================================
    // 2. DELETE AT THE END
    // ======================================================

    /*
        Core pointer idea (two-pointer walk — SLL has no prev!):

            while (current.next != null) {   // stop AT the last node
                previous = current;
                current  = current.next;
            }
            previous.next = null;            // cut the link

        Cases: empty list / single-node list / two or more nodes.
        No free() in Java — just cut the surviving side's link.
    */

    static Node deleteAtEnd(Node head) {

        if (head == null) {
            System.out.println("List is empty. Nothing to delete.");
            return head;
        }

        if (head.next == null) {       // single node IS the last node
            head = null;
            return head;
        }

        Node current = head;
        Node previous = null;

        while (current.next != null) { // walk until current is the last
            previous = current;
            current = current.next;
        }

        previous.next = null;          // second-to-last now ends the list

        return head;
    }


    // ======================================================
    // 3. DELETE AFTER A TARGET
    // ======================================================

    /*
        Core pointer ideas:

            toDelete = current.next;      // save the doomed node FIRST
            current.next = toDelete.next; // rewire around it

        Guards: empty list / target not found / target is last node.
        (In C a third step follows: free(toDelete).)
    */

    static Node deleteAfter(Node head, int target) {

        if (head == null) {
            System.out.println("List is empty. Nothing to delete.");
            return head;
        }

        Node current = head;

        while (current != null && current.data != target) {
            current = current.next;
        }

        if (current == null) {
            System.out.println("Target " + target + " was not found.");
            return head;
        }

        if (current.next == null) {
            System.out.println("No node after " + target + ". Nothing to delete.");
            return head;
        }

        Node toDelete = current.next;   // STEP 1: save it
        current.next = toDelete.next;   // STEP 2: skip over it

        return head;                    // (STEP 3 is the GC's job)
    }


    // ======================================================
    // MAIN — all three operations in sequence
    // ======================================================

    public static void main(String[] args) {

        /*
            Build the starting list:

                10 → 20 → 30 → 40 → 50 → null
        */

        Node head = createNode(10);
        head.next = createNode(20);
        head.next.next = createNode(30);
        head.next.next.next = createNode(40);
        head.next.next.next.next = createNode(50);

        System.out.print("Original list:               ");
        traverse(head);              // 10 20 30 40 50

        // ──────────────────────────────────────────────
        // 1. DELETE AFTER 30 → removes 40
        // ──────────────────────────────────────────────

        head = deleteAfter(head, 30);

        System.out.print("After deleteAfter(30):       ");
        traverse(head);              // 10 20 30 50

        // ──────────────────────────────────────────────
        // 2. DELETE AT START → removes 10
        // ──────────────────────────────────────────────

        head = deleteAtBeginning(head);

        System.out.print("After deleteAtBeginning:     ");
        traverse(head);              // 20 30 50

        // ──────────────────────────────────────────────
        // 3. DELETE AT END → removes 50
        // ──────────────────────────────────────────────

        head = deleteAtEnd(head);

        System.out.print("After deleteAtEnd:           ");
        traverse(head);              // 20 30

        // ──────────────────────────────────────────────
        // EDGE CASES — each should print a message, list unchanged
        // ──────────────────────────────────────────────

        head = deleteAfter(head, 30);      // nothing after last node
        head = deleteAfter(head, 99);      // target not found

        /*
            Drain the list completely, then prove empty-list handling.
        */

        head = deleteAtEnd(head);          // removes 30
        head = deleteAtEnd(head);          // removes 20 (single-node case)
        head = deleteAtEnd(head);          // "List is empty."
    }
}
