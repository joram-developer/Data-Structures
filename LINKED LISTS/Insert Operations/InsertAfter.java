public class InsertAfter {

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
    // INSERT AFTER A TARGET
    // ==============================

    static Node insertAfter(Node head, int target, int data) {

        /*
            Compare this to insertBefore.

            insertBefore needed TWO pointers (previous + current)
            because a node has no way to reach what is behind it.

            insertAfter only needs ONE pointer (current).
            Once we find the target node, we already have
            everything we need:
                - current       is the target
                - current.next  is whatever comes after it

            We just need to slide the new node in between them.
        */

        // Walk forward until we find the target or run out of nodes
        Node current = head;

        while (current != null && current.data != target) {
            current = current.next;
        }

        // If current is null, we walked off the end without finding target
        if (current == null) {
            System.out.println("Target " + target + " was not found.");
            return head;
        }

        // We found the target. Create the new node.
        Node newNode = createNode(data);

        /*
            Suppose we found target 20:

            current
               ↓
              [20] → [30] → null

            We want to insert [25] right after [20]:

              [20] → [25] → [30] → null


            STEP 1: Make the new node point to whatever current points to.

                newNode.next = current.next;

            State after STEP 1:

            current          newNode
               ↓                ↓
              [20] → [30]      [25] → [30]

            Both current and newNode.next point to [30].
            That is fine — we haven't lost anything.


            STEP 2: Make current point to the new node.

                current.next = newNode;

            State after STEP 2:

              [20] → [25] → [30] → null
               ↑
             current (still here, we just changed where its .next points)


            Why STEP 1 before STEP 2?

            current.next currently holds the address of [30].
            If we did STEP 2 first:

                current.next = newNode;   // current now points to [25]
                newNode.next = current.next;  // current.next is NOW [25], so
                                              // newNode.next = [25] — a cycle!

            We would lose [30] forever.

            Rule: always save the outgoing link in the new node
            BEFORE you overwrite it in the existing node.

            This is the mirror image of insertBefore:

                insertBefore:  wire newNode → current,  then previous → newNode
                insertAfter:   wire newNode → current.next, then current → newNode
        */

        newNode.next = current.next;   // STEP 1: new node inherits current's successor
        current.next = newNode;        // STEP 2: current now points to the new node

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

        System.out.print("Original list:                  ");
        traverse(head);

        /*
            Insert 25 after 20.
            Normal mid-list case.

            Before:   10 → 20 → 30
                            ↓
            After:    10 → 20 → 25 → 30
        */

        head = insertAfter(head, 20, 25);

        System.out.print("After inserting 25 after 20:    ");
        traverse(head);

        /*
            Insert 35 after 30.
            Target is the LAST node — current.next is null.

            STEP 1: newNode.next = current.next = null   → new node's next is null  ✓
            STEP 2: current.next = newNode               → 30 now points to 35      ✓

            The same two lines handle this case perfectly
            with no special-casing needed.

            Before:   10 → 20 → 25 → 30
                                        ↓
            After:    10 → 20 → 25 → 30 → 35
        */

        head = insertAfter(head, 30, 35);

        System.out.print("After inserting 35 after 30:    ");
        traverse(head);

        /*
            Try to insert after a value that does not exist.
            Should print a not-found message and leave the list unchanged.
        */

        head = insertAfter(head, 99, 88);

        System.out.print("After inserting 88 after 99:    ");
        traverse(head);
    }
}
