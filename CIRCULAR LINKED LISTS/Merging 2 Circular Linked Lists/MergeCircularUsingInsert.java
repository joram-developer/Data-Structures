public class MergeCircularUsingInsert {

    // ======================================================
    // This is the SAME merge as MergeCircular.java in this
    // folder — identical mergeCircular() function, same
    // two-arrow logic.
    //
    // THE DIFFERENCE: instead of building the two lists from
    // arrays with buildCircular(), we build them ONE NODE AT A
    // TIME using insertAtEnd() — the way you'd construct lists
    // in a lab test or an exam. See CircularLinkedList.java
    // (one level up) for the full ground-up explanation of
    // insertAtEnd; condensed comments appear here.
    // ======================================================

    static class Node {
        int data;
        Node next;
    }

    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.next = null;   // caller wires it into a ring immediately
        return newNode;
    }


    // ======================================================
    // INSERT AT THE END — maintains the ring invariant:
    // after every call, last node's next == head.
    // ======================================================

    static Node insertAtEnd(Node head, int data) {

        Node newNode = createNode(data);


        /*
            CASE 1: Empty list → one-node ring that points to ITSELF.

                ┌──────┐
                ↓      │
              [10] ────┘

            ★ The self-loop is what makes it circular. An SLL would
              leave newNode.next as null here; we can never allow
              next == null anywhere in a ring.
        */

        if (head == null) {
            newNode.next = newNode;
            head = newNode;
            return head;
        }


        /*
            CASE 2: Non-empty → walk until current is the LAST node.

                ⚠ Stop condition compares against HEAD, not null:

                    while (current.next != head)

                then splice: new node points at head first,
                old last points at the new node.
        */

        Node current = head;

        while (current.next != head) {
            current = current.next;
        }

        newNode.next = head;       // wire the NEW node first
        current.next = newNode;    // then close the ring through it

        return head;
    }


    // ======================================================
    // TRAVERSE — do-while for exactly one full lap
    // ======================================================

    static void traverse(Node head) {

        if (head == null) {
            System.out.println("(empty)");
            return;
        }

        Node current = head;

        do {
            System.out.print(current.data + " ");
            current = current.next;
        } while (current != head);

        System.out.println();
    }


    // ======================================================
    // MERGE TWO CIRCULAR LINKED LISTS
    // ======================================================
    // Full step-by-step explanation lives in MergeCircular.java
    // and in "EXPLANATION - Merging 2 Circular Linked Lists.md".
    // Summary: find each ring's last node, rewire TWO arrows,
    // return head1. O(n1 + n2) time, O(1) extra space.

    static Node mergeCircular(Node head1, Node head2) {

        // Empty-list cases: result is simply the other list.
        if (head1 == null) return head2;
        if (head2 == null) return head1;

        // STEP 1: last node of ring 1 (next wraps to ITS OWN head)
        Node last1 = head1;
        while (last1.next != head1) {
            last1 = last1.next;
        }

        // STEP 2: last node of ring 2
        Node last2 = head2;
        while (last2.next != head2) {
            last2 = last2.next;
        }

        // STEP 3: the only two wrong arrows get fixed
        last1.next = head2;    // ARROW 1: ring 1 flows into list 2
        last2.next = head1;    // ARROW 2: list 2 closes onto head1

        return head1;          // merged ring starts where list 1 started
    }


    // ======================================================
    // MAIN — lists built node-by-node with insertAtEnd
    // ======================================================

    public static void main(String[] args) {

        /*
            Build ring 1 by hand, one insertAtEnd call per value:

            first insert → empty case → self-loop:
                ┌──────┐
                ↓      │
              [10] ────┘

            second insert → walk stops at once ([10].next == head,
            so [10] is the last node), splice [20]:
                ┌→ [10] → [20] ─┐

            third insert → walk stops at [20], splice [30]:
                ┌→ [10] → [20] → [30] ─┐
        */

        Node list1 = null;

        list1 = insertAtEnd(list1, 10);   // empty case → self-loop
        list1 = insertAtEnd(list1, 20);
        list1 = insertAtEnd(list1, 30);

        /*
            Build ring 2 the same way:

                ┌→ [40] → [50] ─┐
        */

        Node list2 = null;

        list2 = insertAtEnd(list2, 40);   // empty case → self-loop
        list2 = insertAtEnd(list2, 50);

        System.out.print("List 1 before merge: ");
        traverse(list1);             // 10 20 30

        System.out.print("List 2 before merge: ");
        traverse(list2);             // 40 50

        /*
            Merge — exactly the same two-arrow surgery as before.
            HOW the rings were BUILT makes no difference to the
            merge function; all it needs is a valid head of each.
        */

        Node merged = mergeCircular(list1, list2);

        System.out.println();
        System.out.print("Merged list:         ");
        traverse(merged);            // 10 20 30 40 50

        System.out.print("Traverse again:      ");
        traverse(merged);            // still one clean lap → truly circular
    }
}
