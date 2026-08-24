public class InsertAtEnd {

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
    // INSERT AT THE END
    // ======================================================

    static Node insertAtEnd(Node head, int data) {

        Node newNode = createNode(data);


        /*
            CASE 1: The list is empty.

            The new node becomes the head. Its prev and next
            are already null, so all boundary rules hold.
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

            null ← [10] ⇄ [20] ⇄ [30] ⇄ [40] → null

            STEP 1: walk until current is the LAST node.
                    Same forward walk as the SLL version —
                    we are moving with .next, so nothing changes.

            STEP 2: wire the new node's TWO pointers first.

                newNode.prev = current;     // [40].prev → [30]

                (.next is already null — correct for a last node.)

            STEP 3: wire the old last node forward.

                current.next = newNode;     // [30].next → [40]

            Boundary check afterwards:
                [40].next == null           ✓ (rule 2 preserved)
                [40].prev == [30]           ✓ (rules 3 & 4 preserved)

            In C this was identical except -> instead of .
            and a free() loop at the end.
        */

        Node current = head;

        while (current.next != null) {   // STEP 1: find the last node
            current = current.next;
        }

        newNode.prev = current;          // STEP 2: new node looks back
        current.next = newNode;          // STEP 3: old tail points forward

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

        System.out.print("Original list:             ");
        traverse(head);              // 10 20 30

        /*
            Insert 40 at the end.

            Before:   null ← [10] ⇄ [20] ⇄ [30]
            After:    null ← [10] ⇄ [20] ⇄ [30] ⇄ [40]
        */

        head = insertAtEnd(head, 40);

        System.out.print("After inserting 40 at end: ");
        traverse(head);              // 10 20 30 40

        /*
            Insert into an EMPTY list — exercises CASE 1.
        */

        head = null;
        head = insertAtEnd(head, 99);

        System.out.print("Empty-list insert result:  ");
        traverse(head);              // 99
    }
}
