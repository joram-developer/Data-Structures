public class InsertBefore {

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
    // INSERT BEFORE A TARGET
    // ======================================================

    static Node insertBefore(Node head, int target, int data) {

        /*
            ⭐ THE BIG DIFFERENCE FROM THE SLL VERSION:

            In the SLL insertBefore we needed TWO walking references:

                previous → trails behind current

            because once you land on the target, an SLL node has no
            way to tell you what came before it.

            In the DLL the target node CARRIES its own predecessor:
            current.prev IS the "previous" reference. So we walk with
            ONE reference and read the predecessor straight from it.
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
            CASE 1: The target is the FIRST node (previous == null).

                    current
                       ↓
            null ← [10] ⇄ [20] ⇄ [30] → null

            There is nothing before the target, so this is exactly
            insertAtBeginning. We detect it with current.prev == null
            (equivalent to previous == null in the SLL version).
        */

        if (current.prev == null) {

            newNode.next = current;   // [5].next → [10]
            current.prev = newNode;   // [10].prev → [5]
            head = newNode;           // new node is the first now

            return head;
        }


        /*
            CASE 2: The target has a node before it.

                previous  current
                    ↓        ↓
            null ← [10] ⇄ [20] ⇄ [30] → null

            Inserting 15 before 20:

            null ← [10] ⇄ [15] ⇄ [20] ⇄ [30] → null

            ORDER OF OPERATIONS — new node first, then existing nodes:

            STEP 1: newNode points forward to the target.
                newNode.next = current;

            STEP 2: newNode points backward to the old predecessor.
                    Read current.prev NOW — it still holds the
                    original value.
                newNode.prev = current.prev;

            STEP 3: the old predecessor points forward to newNode.
                current.prev.next = newNode;

            STEP 4: the target points backward to newNode.
                current.prev = newNode;

            ⚠ Steps 3 and 4 MUST come after steps 1-2, and step 3 must
            run before step 4: step 3 reads current.prev, and step 4
            overwrites it. Do step 4 first and step 3 corrupts the list.

            Head does NOT change here — we inserted mid-list.
        */

        newNode.next = current;           // STEP 1: forward link from new node
        newNode.prev = current.prev;      // STEP 2: backward link to old predecessor
        current.prev.next = newNode;      // STEP 3: old predecessor looks forward
        current.prev = newNode;           // STEP 4: target looks backward

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
            Insert 15 before 20 (mid-list case).

            Before:   null ← [10] ⇄ [20] ⇄ [30]
            After:    null ← [10] ⇄ [15] ⇄ [20] ⇄ [30]
        */

        head = insertBefore(head, 20, 15);

        System.out.print("After inserting 15 before 20:");
        traverse(head);              // 10 15 20 30

        /*
            Insert 5 before 10 (FIRST-NODE case — head changes).
        */

        head = insertBefore(head, 10, 5);

        System.out.print("After inserting 5 before 10: ");
        traverse(head);              // 5 10 15 20 30

        /*
            Target that doesn't exist.
        */

        head = insertBefore(head, 99, 50);
    }
}
