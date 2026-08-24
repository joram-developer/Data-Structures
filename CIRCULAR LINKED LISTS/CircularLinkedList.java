public class CircularLinkedList {

    // ======================================================
    // NODE CLASS
    // ======================================================

    /*
        Same node as a single linked list — the "circular" part
        is not in the class, it's in HOW WE LINK the nodes:
        the last node's next points back to head instead of null.
    */

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
        newNode.next = null;   // caller wires it into the ring immediately
        return newNode;
    }


    // ======================================================
    // INSERT AT THE END
    // ======================================================

    static Node insertAtEnd(Node head, int data) {

        Node newNode = createNode(data);


        /*
            ─────────────────────────────────────────
            CASE 1: The list is empty.  ★ THE SPECIAL CASE

                A ring with one node must still be a ring:
                the node points to ITSELF.

                    ┌──────┐
                    ↓      │
                  [10] ────┘        newNode.next = newNode;

            Compare with an SLL: there, the empty case left
            newNode.next as null. THE SELF-LOOP IS THE ONE THING
            THAT MAKES IT CIRCULAR.
        */

        if (head == null) {
            newNode.next = newNode;   // one-node ring points to itself
            head = newNode;
            return head;
        }


        /*
            ─────────────────────────────────────────
            CASE 2: The list has nodes already.

                ┌→ [10] → [20] → [30] ─┐     insert 40 at end:

                walk until we stand on the LAST node.
                ⚠ Stop condition is `current.next != head`,
                  NOT `!= null` — there is no null in a ring!

            STEP 1: splice the new node in between last and head.

                newNode.next  = head;      // [40].next → [10]
                current.next  = newNode;   // [30].next → [40]

            Result:

                ┌→ [10] → [20] → [30] → [40] ─┘
        */

        Node current = head;

        while (current.next != head) {    // stop when next wraps home
            current = current.next;
        }

        newNode.next = head;              // STEP 1a: new node → first node
        current.next = newNode;           // STEP 1b: old last → new node

        return head;                      // head unchanged
    }


    // ======================================================
    // INSERT AT THE BEGINNING
    // ======================================================

    static Node insertAtBeginning(Node head, int data) {

        Node newNode = createNode(data);

        /*
            CASE 1: Empty list → identical to insertAtEnd's CASE 1.
        */

        if (head == null) {
            newNode.next = newNode;   // self-loop makes it a ring
            head = newNode;
            return head;
        }

        /*
            CASE 2: Non-empty.

            ⚠ THE TRAP THAT DIFFERS FROM AN SLL:

            In an SLL, insert-at-beginning touches ONLY the new node
            and head. In a CIRCULAR list that's not enough — the LAST
            node also points at head, and after the insert it must
            point at the NEW head instead:

                before:  ┌→ [10] → [20] → [30] ─┐     [30].next = [10]
                after:   ┌→ [5] → [10] → [20] → [30] ┐  [30].next = [5]!

            So we must FIND THE LAST NODE even though we're inserting
            at the FRONT. Hidden cost of front-insertion in a ring.

            STEPS (order matters!):

            STEP 1: find the last node.
            STEP 2: newNode.next = head;      // wire new node forward
            STEP 3: last.next = newNode;      // redirect old last
            STEP 4: head = newNode;           // move the entry point

            ⚠ Step 3 must run BEFORE step 4 changes what "head"
              means — otherwise the walk-to-last condition compares
              against the wrong node mid-surgery.
        */

        // STEP 1: find the last node (needed even for front-insert!)
        Node last = head;
        while (last.next != head) {
            last = last.next;
        }

        newNode.next = head;           // STEP 2: new node → old first
        last.next = newNode;           // STEP 3: old last → new first
        head = newNode;                // STEP 4: new node is the entry point

        return head;
    }


    // ======================================================
    // TRAVERSE — do-while, NOT while!
    // ======================================================

    /*
        Why do-while?

        WRONG:  while (current != head) { ... }
                The loop STARTS on head, so the test is true
                immediately and NOTHING gets printed.

        RIGHT:  visit head FIRST (do-block), THEN ask
                "am I back home?" after moving.
    */

    static void traverse(Node head) {

        if (head == null) {             // empty list guard
            System.out.println("(empty)");
            return;
        }

        Node current = head;

        do {
            System.out.print(current.data + " ");
            current = current.next;
        } while (current != head);      // stop AFTER revisiting head

        System.out.println();
    }


    // ======================================================
    // COUNT NODES — same do-while pattern
    // ======================================================

    static int countNodes(Node head) {

        if (head == null) return 0;

        int count = 0;
        Node current = head;

        do {
            count++;
            current = current.next;
        } while (current != head);

        return count;
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        Node head = null;

        /*
            First insert exercises the SELF-LOOP case.
        */

        System.out.println("After inserting 10 into empty list:");
        System.out.print("  ");
        head = insertAtEnd(head, 10);
        traverse(head);                  // 10
        System.out.println("  node count: " + countNodes(head));   // 1

        /*
            Build up: then 20, 30 at the end.
        */

        System.out.println("\nAfter inserting 20, 30 at end:");
        System.out.print("  ");
        head = insertAtEnd(head, 20);
        head = insertAtEnd(head, 30);
        traverse(head);                  // 10 20 30
        System.out.println("  node count: " + countNodes(head));   // 3

        /*
            Front insertion — including the hidden walk-to-last.
        */

        System.out.println("\nAfter inserting 5 at beginning:");
        System.out.print("  ");
        head = insertAtBeginning(head, 5);
        traverse(head);                  // 5 10 20 30
        System.out.println("  node count: " + countNodes(head));   // 4

        /*
            Traverse twice more to show the ring is stable —
            every call does exactly one clean lap.
        */

        System.out.print("\nSecond lap: ");
        traverse(head);                  // 5 10 20 30

        System.out.print("Third lap:  ");
        traverse(head);                  // 5 10 20 30

        /*
            No freeCircular needed — GC reclaims the ring
            when head goes out of scope.
        */
    }
}
