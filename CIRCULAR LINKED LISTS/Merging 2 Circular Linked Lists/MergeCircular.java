public class MergeCircular {

    // ======================================================
    // NODE CLASS + HELPERS
    // ======================================================

    static class Node {
        int data;
        Node next;
    }

    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.next = null;   // caller closes the circle
        return newNode;
    }

    /*
        Build a circular list from an array of values.

        Walk forward linking nodes, then CLOSE THE RING:
        the last node's next points back to head.
    */

    static Node buildCircular(int[] values) {

        if (values.length == 0) return null;

        Node head = createNode(values[0]);
        Node current = head;

        for (int i = 1; i < values.length; i++) {
            current.next = createNode(values[i]);
            current = current.next;
        }

        current.next = head;   // ← close the ring

        return head;
    }

    /*
        Traversal MUST use do-while, not while!

            while (current != head)   ← WRONG: skips head entirely,
                                        because the loop starts ON head.

        A do-while visits head FIRST, then checks "am I back home?"
        after moving. That gives exactly one visit per node.
    */

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

    static Node mergeCircular(Node head1, Node head2) {

        /*
            ─────────────────────────────────────────
            CASE 1: One of the lists is empty.
            ─────────────────────────────────────────

                The merged result is simply the other list.
                (If BOTH are empty, head2 is null → returns null. ✓)
        */

        if (head1 == null) return head2;
        if (head2 == null) return head1;


        /*
            ─────────────────────────────────────────
            STEP 1: Find the LAST node of list 1.
            ─────────────────────────────────────────

                    ┌→ [10] → [20] → [30] ─┐
                    └────── head1 ─────────┘

                ⚠ THE KEY CONDITION: `last1.next != head1`
                  (compare against HEAD — there IS no null in a ring!)

                When last1 = [30]: last1.next == head1 → stop.
                [30] is the node whose next leads back home,
                i.e. the last node.
        */

        Node last1 = head1;
        while (last1.next != head1) {     // stop when next wraps to ITS OWN head
            last1 = last1.next;
        }


        /*
            ─────────────────────────────────────────
            STEP 2: Find the LAST node of list 2.
            ─────────────────────────────────────────
        */

        Node last2 = head2;
        while (last2.next != head2) {
            last2 = last2.next;
        }


        /*
            ─────────────────────────────────────────
            STEP 3: Rewire exactly TWO arrows.
            ─────────────────────────────────────────

            Before:

                ring 1:   ┌→ [10] → [20] → [30] ─┐      [30].next = [10] ✗
                          └───────────────────────┘
                ring 2:   ┌→ [40] → [50] ─┐             [50].next = [40] ✗
                          └───────────────┘

            After:

                merged:   ┌→ [10] → [20] → [30] → [40] → [50] ─┐
                          └─────────────────────────────────────┘

            ARROW 1:  last1.next = head2;
                      Ring 1 opens up and flows into list 2.

            ARROW 2:  last2.next = head1;
                      List 2's end closes onto list 1's start.

            These two arrows were the ONLY ones that were wrong for
            the merged configuration — every other node keeps its
            existing next reference untouched. O(1) rewiring total.

            In C this is where free()-related care would begin at
            teardown time. In Java, nothing extra: when we drop all
            references, the GC reclaims the whole ring.
        */

        last1.next = head2;           // ARROW 1: ring 1 flows into list 2
        last2.next = head1;           // ARROW 2: list 2 closes onto head1


        /*
            Return head1 — the merged ring starts where list 1 started.
        */

        return head1;
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        /*
            Build two independent rings:

                list 1:  ┌→ [10] → [20] → [30] ─┐
                         └──────────────────────┘
                list 2:  ┌→ [40] → [50] ─┐
                         └───────────────┘
        */

        Node list1 = buildCircular(new int[]{10, 20, 30});
        Node list2 = buildCircular(new int[]{40, 50});

        System.out.print("List 1 before merge: ");
        traverse(list1);             // 10 20 30

        System.out.print("List 2 before merge: ");
        traverse(list2);             // 40 50

        /*
            Merge them into one ring starting at list1's head.
        */

        Node merged = mergeCircular(list1, list2);

        System.out.println();
        System.out.print("Merged list:         ");
        traverse(merged);            // 10 20 30 40 50

        /*
            Proof it is truly circular: traverse() above STOPPED by
            itself. In a broken ring it would either loop forever or
            print only part of the list.
        */

        System.out.print("Traverse again:      ");
        traverse(merged);            // 10 20 30 40 50  (still one full lap)
    }
}
