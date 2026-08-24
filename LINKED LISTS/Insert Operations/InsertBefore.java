public class InsertBefore {

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
    // INSERT BEFORE A TARGET
    // ==============================

    static Node insertBefore(Node head, int target, int data) {

        /*
            This is the most complex of the four insert operations
            because we need to know the node BEFORE the target.

            A linked list node only has a .next pointer — it knows
            what comes after it, but NOT what came before it.

            So if we walk forward and land ON the target, it is
            already too late. We have no way to reach the previous node.

            The solution: use TWO pointers moving together.

                previous → trails one step behind current
                current  → the node we are currently examining

            Visual as the two pointers walk the list:

            Step 0 (start):
                previous = null
                current  = [10] → [20] → [30]

            Step 1 (current.data != target, so advance):
                previous = [10]
                current  = [20] → [30]

            Step 2 (current.data == target, stop!):
                previous = [10]
                current  = [20] → [30]

            Now we have exactly what we need:
                previous points to the node BEFORE the target.
                current  points to the target itself.
        */

        Node current = head;
        Node previous = null;    // null means "there is no previous node yet"

        // Walk forward until we find the target or run out of nodes
        while (current != null && current.data != target) {
            previous = current;        // remember where we just were
            current = current.next;    // move forward
        }

        // If current is null, we walked off the end without finding target
        if (current == null) {
            System.out.println("Target " + target + " was not found.");
            return head;
        }

        // We found the target. Create the new node.
        Node newNode = createNode(data);

        /*
            Now there are two sub-cases depending on where the target is.

            ─────────────────────────────────────────────────────────
            SUB-CASE A: target is the FIRST node  (previous == null)
            ─────────────────────────────────────────────────────────

            previous is still null, which means current IS head.
            There is no node before the target, so we use
            the exact same logic as insertAtBeginning:

                newNode.next = head;
                head = newNode;

            Before:   head
                       ↓
                      [10] → [20] → [30]

            After:    head
                       ↓
                      [5] → [10] → [20] → [30]


            ─────────────────────────────────────────────────────────
            SUB-CASE B: target is somewhere AFTER the first node
            ─────────────────────────────────────────────────────────

            previous → [10]
            current  → [20] → [30]

            We want to insert [15] between them.

            STEP 1: Connect the new node to current (the target):

                newNode.next = current;

            State:
                previous → [10]
                newNode  → [15] → [20] → [30]
                current  → [20] → [30]

            STEP 2: Connect previous to the new node:

                previous.next = newNode;

            State:
                [10] → [15] → [20] → [30]

            Why this order?
            If we did STEP 2 first (previous.next = newNode),
            we would lose the reference to [20]. We'd have no
            way to set newNode.next correctly.

            Always wire the NEW node's outgoing link first,
            then redirect the existing node to point to it.
        */

        if (previous == null) {
            // SUB-CASE A: target is the first node
            newNode.next = head;
            head = newNode;
        } else {
            // SUB-CASE B: target is somewhere in the middle or end
            newNode.next = current;      // STEP 1: new node points to target
            previous.next = newNode;     // STEP 2: previous now points to new node
        }

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

        System.out.print("Original list:                    ");
        traverse(head);

        /*
            Insert 15 before 20.
            This tests the normal mid-list case.

            Before:   10 → 20 → 30
                            ↓
            After:    10 → 15 → 20 → 30
        */

        head = insertBefore(head, 20, 15);

        System.out.print("After inserting 15 before 20:     ");
        traverse(head);

        /*
            Insert 5 before 10.
            This tests SUB-CASE A — target is the first node.

            Before:   10 → 15 → 20 → 30
                       ↓
            After:    5 → 10 → 15 → 20 → 30
        */

        head = insertBefore(head, 10, 5);

        System.out.print("After inserting 5 before 10:      ");
        traverse(head);

        /*
            Try to insert before a value that does not exist.
            Should print a not-found message and leave the list unchanged.
        */

        head = insertBefore(head, 99, 88);

        System.out.print("After inserting 88 before 99:     ");
        traverse(head);
    }
}
