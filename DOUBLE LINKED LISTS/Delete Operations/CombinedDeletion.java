public class CombinedDeletion {

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

    static void traverseBackward(Node head) {
        if (head == null) { System.out.println("(empty)"); return; }
        Node current = head;
        while (current.next != null) current = current.next;
        while (current != null) { System.out.print(current.data + " "); current = current.prev; }
        System.out.println();
    }


    // ======================================================
    // 1. DELETE AT THE BEGINNING
    // ======================================================

    /*
        Core pointer ideas:

            head = head.next;           // second node becomes head
            IF head != null:            ⚠ guard (single-node list)
                head.prev = null;       // restore boundary rule 1

        The C version additionally saved temp = head and called
        free(temp). Java needs neither — the old node becomes
        unreachable and the GC reclaims it.
    */

    static Node deleteAtBeginning(Node head) {

        if (head == null) {
            System.out.println("List is empty. Nothing to delete.");
            return head;
        }

        head = head.next;              // advance head

        if (head != null) {            // guard against single-node list
            head.prev = null;          // nothing before the new first node
        }

        return head;
    }


    // ======================================================
    // 2. DELETE AT THE END
    // ======================================================

    /*
        Core pointer ideas:

            walk until current is last, then:

                current.prev.next = null;   // cut from the SURVIVING side

        ⭐ No `previous` walker needed — current.prev IS it.
           The doomed node's stale prev vanishes with the node itself.
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

        while (current.next != null) { // walk to the last node
            current = current.next;
        }

        current.prev.next = null;      // cut forward link on survivor

        return head;
    }


    // ======================================================
    // 3. DELETE AFTER A TARGET
    // ======================================================

    /*
        Core pointer ideas — cut the connection in BOTH directions:

            toDelete = current.next;          // save FIRST
            current.next = toDelete.next;     // forward cut
            IF toDelete.next != null:         ⚠ guard (deleted last node)
                toDelete.next.prev = current; // backward cut

        In C a final free(toDelete) follows; in Java, GC handles it.
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

        Node toDelete = current.next;            // STEP 1: save it

        current.next = toDelete.next;            // STEP 2: forward cut

        if (toDelete.next != null) {             // STEP 3: guarded backward cut
            toDelete.next.prev = current;        //         survivor looks back
        }

        return head;                             // (STEP 4 is the GC's job)
    }


    // ======================================================
    // MAIN — all three operations in sequence
    // ======================================================

    public static void main(String[] args) {

        /*
            Build:  null ← [10] ⇄ [20] ⇄ [30] ⇄ [40] ⇄ [50] → null
        */

        Node[] nodes = new Node[5];
        for (int i = 0; i < 5; i++) nodes[i] = createNode((i + 1) * 10);
        for (int i = 0; i < 4; i++) {
            nodes[i].next = nodes[i + 1];
            nodes[i + 1].prev = nodes[i];
        }

        Node head = nodes[0];

        System.out.print("Original list (forward):     ");
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

        /*
            Final proof that all remaining prev links are correct.
            If any backward link were broken by a deletion,
            this walk would print wrong values or stop early.
        */

        System.out.print("Final list (backward):       ");
        traverseBackward(head);      // 30 20

        // ──────────────────────────────────────────────
        // EDGE CASES
        // ──────────────────────────────────────────────

        head = deleteAfter(head, 30);      // nothing after last node
        head = deleteAfter(head, 99);      // target not found

        head = deleteAtEnd(head);          // removes 30
        head = deleteAtEnd(head);          // removes 20 (single-node case)
        head = deleteAtEnd(head);          // "List is empty."
    }
}
