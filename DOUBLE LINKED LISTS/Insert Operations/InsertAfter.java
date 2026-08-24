public class InsertAfter {

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
    // INSERT AFTER A TARGET
    // ======================================================

    static Node insertAfter(Node head, int target, int data) {

        /*
            Search for the target — same forward walk as the SLL.
            Only ONE pointer needed: once we land on the target,
            current.next already tells us what comes after it.
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

        Node newNode = createNode(data);


        /*
                current
                   ↓
            null ← [20] ⇄ [30] → null

            We want:

            null ← [20] ⇄ [25] ⇄ [30] → null

            FOUR reference updates (an SLL only needed two!),
            because the [20]-[30] connection exists in BOTH
            directions and we are splicing into the middle of it.

            ORDER OF OPERATIONS:
            Wire the NEW node first (its fields are empty and safe
            to overwrite), then fix the existing nodes.

            STEP 1: newNode points forward to [30].
                newNode.next = current.next;

            STEP 2: newNode points backward to [20].
                newNode.prev = current;

            STEP 3: IF there is a node after current ([30] here,
            but null if target is the last node!), make it point
            BACK to the new node.

                if (current.next != null)
                    current.next.prev = newNode;

            ⚠ THE CRITICAL GUARD: in the SLL version, inserting after
            the LAST node needed no special handling. In the DLL it DOES —
            we must also write [30].prev, and if current.next is null
            there IS no [30] to update. Skipping this check crashes.

            STEP 4: current points forward to the new node.
                    Do this AFTER step 3 — step 3 reads current.next,
                    so overwriting it too early loses [30].

                current.next = newNode;

            Boundary check afterwards:
                [25].next == [30], [30].prev == [25]   ✓ rules 3 & 4 up
                [25].prev == [20], [20].next == [25]   ✓ rules 3 & 4 down
                If inserted at the very end: [25].next == null ✓ rule 2

            Head does NOT change in any case here.
        */

        newNode.next = current.next;          // STEP 1: forward link from new node
        newNode.prev = current;               // STEP 2: backward link from new node

        if (current.next != null) {           // STEP 3: guard against last-node case!
            current.next.prev = newNode;      //         old successor looks back
        }

        current.next = newNode;               // STEP 4: target points to new node

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

        System.out.print("Original list:              ");
        traverse(head);              // 10 20 30

        /*
            Insert 25 after 20 (middle case — all four steps fire).

            Before:   null ← [10] ⇄ [20] ⇄ [30]
            After:    null ← [10] ⇄ [20] ⇄ [25] ⇄ [30]
        */

        head = insertAfter(head, 20, 25);

        System.out.print("After inserting 25 after 20:");
        traverse(head);              // 10 20 25 30

        /*
            Insert 40 after 30 (LAST-NODE case — exercises the guard).
        */

        head = insertAfter(head, 30, 40);

        System.out.print("After inserting 40 after 30:");
        traverse(head);              // 10 20 25 30 40

        /*
            Target that doesn't exist.
        */

        head = insertAfter(head, 99, 50);
    }
}
