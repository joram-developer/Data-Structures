public class InsertAtEnd {

    // ==============================
    // NODE CLASS
    // ==============================

    static class Node {
        int data;
        Node next;
    }


    // ==============================
    // CREATE A NEW NODE
    // ==============================

    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.next = null;
        return newNode;
    }


    // ==============================
    // TRAVERSE THE LIST
    // ==============================

    static void traverse(Node head) {
        Node current = head;

        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }

        System.out.println();
    }


    // ==============================
    // INSERT AT THE END
    // ==============================

    static Node insertAtEnd(Node head, int data) {

        Node newNode = createNode(data);

        /*
            There are two cases.

            ─────────────────────────────────────────
            CASE 1: The list is empty
            ─────────────────────────────────────────

            head
             ↓
            null

            There is no existing node, so the new node
            simply becomes the first (and only) node:

                head = newNode;

            Result:

            head
             ↓
            [newNode] → null


            ─────────────────────────────────────────
            CASE 2: The list is NOT empty
            ─────────────────────────────────────────

            head
             ↓
            [10] → [20] → [30] → null

            We need to find the LAST node — the one
            whose .next is null.

            We use a pointer called "current" and walk
            it forward until current.next == null.

            Why current.next == null and NOT current == null?

            If we stopped at current == null, current
            would have walked PAST the last node.
            We would have no way to attach the new node
            because we lost the reference to [30].

            We stop at current.next == null so that
            current is still pointing AT [30], and then:

                current.next = newNode;

            gives us:

            [10] → [20] → [30] → [newNode] → null
        */

        // CASE 1: empty list
        if (head == null) {
            head = newNode;
            return head;
        }

        // CASE 2: walk to the last node
        Node current = head;

        while (current.next != null) {   // stop BEFORE falling off the end
            current = current.next;
        }

        // current is now the last node — attach the new node
        current.next = newNode;

        return head;
    }


    // ==============================
    // MAIN
    // ==============================

    public static void main(String[] args) {

        /*
            Create the initial list:

            10 → 20 → 30 → null
        */

        Node head = createNode(10);
        Node node2 = createNode(20);
        Node node3 = createNode(30);

        head.next = node2;
        node2.next = node3;

        System.out.print("Original list:       ");
        traverse(head);

        /*
            Insert 40 at the end.

            Before:   10 → 20 → 30
                                  ↓
            After:    10 → 20 → 30 → 40
        */

        head = insertAtEnd(head, 40);

        System.out.print("After inserting 40:  ");
        traverse(head);

        /*
            Insert 50 at the end.

            Before:   10 → 20 → 30 → 40
                                        ↓
            After:    10 → 20 → 30 → 40 → 50
        */

        head = insertAtEnd(head, 50);

        System.out.print("After inserting 50:  ");
        traverse(head);

        /*
            No need to free memory in Java.
            The garbage collector handles it automatically.
        */
    }
}
