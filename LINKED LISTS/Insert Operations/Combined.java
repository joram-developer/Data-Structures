public class Combined {

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
    // INSERT AT THE BEGINNING
    // ======================================================

    /*
        Core pointer idea:

            newNode.next = head;   // new node points to existing list
            head = newNode;        // new node becomes the new head

        Order matters: save head into newNode.next FIRST,
        then overwrite head. Otherwise we lose the list.
    */

    static Node insertAtBeginning(Node head, int data) {

        Node newNode = createNode(data);

        newNode.next = head;
        head = newNode;

        return head;
    }


    // ======================================================
    // INSERT AT THE END
    // ======================================================

    /*
        Core pointer idea:

            current.next = newNode;

        We walk until current.next == null (not current == null)
        so that current still points AT the last node when we stop.

        Special case: empty list → new node becomes head directly.
    */

    static Node insertAtEnd(Node head, int data) {

        Node newNode = createNode(data);

        // Special case: empty list
        if (head == null) {
            head = newNode;
            return head;
        }

        // Walk to the last node
        Node current = head;
        while (current.next != null) {
            current = current.next;
        }

        current.next = newNode;

        return head;
    }


    // ======================================================
    // INSERT BEFORE A TARGET
    // ======================================================

    /*
        Core pointer idea:

            newNode.next  = current;     // new node → target
            previous.next = newNode;     // previous → new node

        Requires two pointers (previous + current) because a node
        has no link backwards — once you land on the target you
        can no longer reach the node before it.

        Special case: target is the first node (previous == null)
        → falls back to the same logic as insertAtBeginning.
    */

    static Node insertBefore(Node head, int target, int data) {

        Node current = head;
        Node previous = null;

        // Walk until we find the target
        while (current != null && current.data != target) {
            previous = current;
            current = current.next;
        }

        if (current == null) {
            System.out.println("Target " + target + " was not found.");
            return head;
        }

        Node newNode = createNode(data);

        if (previous == null) {
            // Target is the first node — same as insertAtBeginning
            newNode.next = head;
            head = newNode;
        } else {
            // Target is mid-list or last node
            newNode.next = current;       // STEP 1: wire new node forward
            previous.next = newNode;      // STEP 2: redirect previous to new node
        }

        return head;
    }


    // ======================================================
    // INSERT AFTER A TARGET
    // ======================================================

    /*
        Core pointer idea:

            newNode.next  = current.next;   // new node → current's old successor
            current.next  = newNode;        // current → new node

        Only one pointer needed (current) because once we land
        on the target, current.next already gives us the successor.

        Order matters: save current.next into newNode.next FIRST,
        then overwrite current.next. Otherwise we lose the rest of the list.
    */

    static Node insertAfter(Node head, int target, int data) {

        Node current = head;

        // Walk until we find the target
        while (current != null && current.data != target) {
            current = current.next;
        }

        if (current == null) {
            System.out.println("Target " + target + " was not found.");
            return head;
        }

        Node newNode = createNode(data);

        newNode.next = current.next;    // STEP 1: inherit current's successor
        current.next = newNode;         // STEP 2: current now points to new node

        return head;
    }


    // ======================================================
    // MAIN — all four operations in sequence
    // ======================================================

    public static void main(String[] args) {

        /*
            Build the starting list manually:

            10 → 20 → 30 → null
        */

        Node head = createNode(10);
        Node node2 = createNode(20);
        Node node3 = createNode(30);

        head.next = node2;
        node2.next = node3;

        System.out.print("Original list:                  ");
        traverse(head);
        // 10 20 30


        // ──────────────────────────────────────────────────
        // 1. INSERT AT BEGINNING
        // ──────────────────────────────────────────────────

        /*
            10 → 20 → 30
             ↓
            5 → 10 → 20 → 30
        */

        head = insertAtBeginning(head, 5);

        System.out.print("After insertAtBeginning(5):     ");
        traverse(head);
        // 5 10 20 30


        // ──────────────────────────────────────────────────
        // 2. INSERT AT END
        // ──────────────────────────────────────────────────

        /*
            5 → 10 → 20 → 30
                            ↓
            5 → 10 → 20 → 30 → 40
        */

        head = insertAtEnd(head, 40);

        System.out.print("After insertAtEnd(40):          ");
        traverse(head);
        // 5 10 20 30 40


        // ──────────────────────────────────────────────────
        // 3. INSERT BEFORE
        // ──────────────────────────────────────────────────

        /*
            5 → 10 → 20 → 30 → 40
                      ↓
            5 → 10 → 15 → 20 → 30 → 40
        */

        head = insertBefore(head, 20, 15);

        System.out.print("After insertBefore(20, 15):     ");
        traverse(head);
        // 5 10 15 20 30 40


        // ──────────────────────────────────────────────────
        // 4. INSERT AFTER
        // ──────────────────────────────────────────────────

        /*
            5 → 10 → 15 → 20 → 30 → 40
                            ↓
            5 → 10 → 15 → 20 → 25 → 30 → 40
        */

        head = insertAfter(head, 20, 25);

        System.out.print("After insertAfter(20, 25):      ");
        traverse(head);
        // 5 10 15 20 25 30 40


        /*
            No need to free memory in Java.
            The garbage collector cleans up automatically
            once head goes out of scope.
        */
    }
}
