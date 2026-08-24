public class InsertAtStart {

    // ======================================================
    // NODE CLASS
    // ======================================================

    /*
        Same as the SLL node class, plus one extra reference.
    */

    static class Node {
        int data;
        Node prev;   // NEW: arrow to the node before this one
        Node next;
    }


    // ======================================================
    // CREATE A NEW NODE
    // ======================================================

    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.prev = null;   // both pointers start empty
        newNode.next = null;
        return newNode;
    }


    // ======================================================
    // TRAVERSE THE LIST (FORWARD)
    // ======================================================

    static void traverse(Node head) {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;   // same as SLL
        }
        System.out.println();
    }


    // ======================================================
    // TRAVERSE THE LIST (BACKWARD)  ← impossible in an SLL!
    // ======================================================

    /*
        STEP 1: walk forward until current is the LAST node
                (current.next == null).

        STEP 2: follow .prev backwards, printing as we go,
                until we pass the first node (its prev is null).
    */

    static void traverseBackward(Node head) {

        if (head == null) {          // empty list — nothing to print
            System.out.println();
            return;
        }

        Node current = head;

        // STEP 1: walk to the last node
        while (current.next != null) {
            current = current.next;
        }

        // STEP 2: walk back using prev
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.prev;
        }

        System.out.println();
    }


    // ======================================================
    // INSERT AT THE BEGINNING
    // ======================================================

    static Node insertAtBeginning(Node head, int data) {

        Node newNode = createNode(data);


        /*
            CASE 1: The list is empty.

            The new node becomes the head. Its prev and next
            are already null, so the boundary rules hold:

                head.prev == null ✓
                head.next == null ✓
        */

        if (head == null) {
            head = newNode;
            return head;
        }


        /*
            CASE 2: The list has one or more nodes.

                    head
                     ↓
            null ← [10] ⇄ [20] ⇄ [30] → null

            We want:

            null ← [5] ⇄ [10] ⇄ [20] ⇄ [30] → null

            ORDER OF OPERATIONS MATTERS HERE.
            Connect the new node FIRST, then touch existing nodes.

            STEP 1: point the new node at the old head.

                newNode.next = head;

            STEP 2: make the old head's prev point BACK at the new node.

                head.prev = newNode;

            STEP 3: move head.

                head = newNode;

            In C this was identical except -> instead of .
            and free() worries — in Java, no memory management,
            just the three rewiring lines.

            Boundary check afterwards:
                head.prev == null       ✓ (rule 1 preserved)
                [10].prev == newNode    ✓ (rules 3 & 4 preserved)
        */

        newNode.next = head;   // STEP 1: new node points forward to old head
        head.prev = newNode;   // STEP 2: old head points backward to new node
        head = newNode;        // STEP 3: new node IS the head now

        return head;
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        /*
            Build the initial list:  null ← [10] ⇄ [20] ⇄ [30] → null
        */

        Node n1 = createNode(10);
        Node n2 = createNode(20);
        Node n3 = createNode(30);

        n1.next = n2;
        n2.prev = n1;
        n2.next = n3;
        n3.prev = n2;

        Node head = n1;

        System.out.print("Original list (forward):   ");
        traverse(head);              // 10 20 30

        /*
            Insert 5 at the beginning.

            Before:   null ← [10] ⇄ [20] ⇄ [30]
            After:    null ← [5] ⇄ [10] ⇄ [20] ⇄ [30]
        */

        head = insertAtBeginning(head, 5);

        System.out.print("After inserting 5 at start:");
        traverse(head);              // 5 10 20 30

        /*
            Now prove the BACKWARD links really exist:
            walk the same list from tail to head.
        */

        System.out.print("Same list (backward):      ");
        traverseBackward(head);      // 30 20 10 5

        /*
            No need to free memory in Java.
            The garbage collector cleans up automatically.
        */
    }
}
