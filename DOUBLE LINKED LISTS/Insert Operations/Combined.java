public class Combined {

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
        if (head == null) { System.out.println(); return; }
        Node current = head;
        while (current.next != null) current = current.next;
        while (current != null) { System.out.print(current.data + " "); current = current.prev; }
        System.out.println();
    }


    // ======================================================
    // 1. INSERT AT THE BEGINNING
    // ======================================================

    /*
        Core pointer idea:

            newNode.next = head;      // new node looks forward at old head
            head.prev   = newNode;    // old head looks back at new node
            head         = newNode;   // new node becomes the first

        Empty list: just head = newNode.
        No free() needed anywhere in Java — GC handles removals.
    */

    static Node insertAtBeginning(Node head, int data) {

        Node newNode = createNode(data);

        if (head == null) {
            head = newNode;
            return head;
        }

        newNode.next = head;          // wire the NEW node first...
        head.prev = newNode;          // ...then the OLD node
        head = newNode;

        return head;
    }


    // ======================================================
    // 2. INSERT AT THE END
    // ======================================================

    /*
        Core pointer idea:

            newNode.prev = last;      // new node looks back at old tail
            last.next    = newNode;   // old tail points forward to new node

        Walk with current until current.next == null, same as SLL.
    */

    static Node insertAtEnd(Node head, int data) {

        Node newNode = createNode(data);

        if (head == null) {
            head = newNode;
            return head;
        }

        Node current = head;

        while (current.next != null) {   // find the last node
            current = current.next;
        }

        newNode.prev = current;       // new node looks back FIRST
        current.next = newNode;       // then old tail points forward

        return head;
    }


    // ======================================================
    // 3. INSERT BEFORE A TARGET
    // ======================================================

    /*
        Core pointer ideas:

        First-node case (current.prev == null):
            newNode.next = current;
            current.prev = newNode;
            head = newNode;

        Mid-list case — FOUR updates, new node wired first:
            newNode.next       = current;        // forward to target
            newNode.prev       = current.prev;   // backward to old predecessor
            current.prev.next  = newNode;        // old predecessor → new node
            current.prev       = newNode;        // target → back to new node

        ⚠ Steps 3-4 read current.prev, so run them AFTER steps 1-2,
          and step 3 before step 4.

        No separate `previous` walker needed — current.prev IS it.
    */

    static Node insertBefore(Node head, int target, int data) {

        Node current = head;

        while (current != null && current.data != target) {
            current = current.next;
        }

        if (current == null) {
            System.out.println("Target " + target + " was not found.");
            return head;
        }

        Node newNode = createNode(data);

        if (current.prev == null) {           // target is the first node
            newNode.next = current;
            current.prev = newNode;
            head = newNode;
            return head;
        }

        newNode.next = current;               // STEP 1: forward link
        newNode.prev = current.prev;          // STEP 2: backward link
        current.prev.next = newNode;          // STEP 3: predecessor rewires forward
        current.prev = newNode;               // STEP 4: target rewires backward

        return head;
    }


    // ======================================================
    // 4. INSERT AFTER A TARGET
    // ======================================================

    /*
        Core pointer ideas — FOUR updates, new node wired first:

            newNode.next       = current.next;  // forward to old successor
            newNode.prev       = current;       // backward to target
            IF successor exists:                ⚠ guard against last node!
                current.next.prev = newNode;    // successor looks back
            current.next       = newNode;       // target points to new node

        ⚠ The guarded line must run BEFORE the last line:
          it reads current.next before it gets overwritten.
    */

    static Node insertAfter(Node head, int target, int data) {

        Node current = head;

        while (current != null && current.data != target) {
            current = current.next;
        }

        if (current == null) {
            System.out.println("Target " + target + " was not found.");
            return head;
        }

        Node newNode = createNode(data);

        newNode.next = current.next;             // STEP 1: forward from new node
        newNode.prev = current;                  // STEP 2: backward from new node

        if (current.next != null) {              // STEP 3: guarded!
            current.next.prev = newNode;         //         successor looks back
        }

        current.next = newNode;                  // STEP 4: target points forward

        return head;
    }


    // ======================================================
    // MAIN — all four operations in sequence
    // ======================================================

    public static void main(String[] args) {

        /*
            Build the starting list manually:

                null ← [10] ⇄ [20] ⇄ [30] → null
        */

        Node head = createNode(10);
        Node node2 = createNode(20);
        Node node3 = createNode(30);

        head.next = node2;
        node2.prev = head;
        node2.next = node3;
        node3.prev = node2;

        System.out.print("Original list (forward):     ");
        traverse(head);              // 10 20 30

        // ──────────────────────────────────────────────
        // 1. INSERT AT BEGINNING — insert 5
        // ──────────────────────────────────────────────

        head = insertAtBeginning(head, 5);

        System.out.print("After insertAtBeginning(5):  ");
        traverse(head);              // 5 10 20 30

        // ──────────────────────────────────────────────
        // 2. INSERT AT END — insert 40
        // ──────────────────────────────────────────────

        head = insertAtEnd(head, 40);

        System.out.print("After insertAtEnd(40):       ");
        traverse(head);              // 5 10 20 30 40

        // ──────────────────────────────────────────────
        // 3. INSERT BEFORE 20 — insert 15
        // ──────────────────────────────────────────────

        head = insertBefore(head, 20, 15);

        System.out.print("After insertBefore(20, 15):  ");
        traverse(head);              // 5 10 15 20 30 40

        // ──────────────────────────────────────────────
        // 4. INSERT AFTER 20 — insert 25
        // ──────────────────────────────────────────────

        head = insertAfter(head, 20, 25);

        System.out.print("After insertAfter(20, 25):   ");
        traverse(head);              // 5 10 15 20 25 30 40

        /*
            Final proof that every link is correct in BOTH directions:
            walk the finished list backwards. If any prev reference
            were wrong, this would print a wrong or short sequence.
        */

        System.out.print("Final list (backward):       ");
        traverseBackward(head);      // 40 30 25 20 15 10 5
    }
}
