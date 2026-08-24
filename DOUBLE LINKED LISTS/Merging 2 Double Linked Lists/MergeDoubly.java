public class MergeDoubly {

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

    /*
        Build a doubly linked list from an array.
        Every connection is wired in BOTH directions as we go.
    */

    static Node buildList(int[] values) {

        if (values.length == 0) return null;

        Node head = createNode(values[0]);
        Node current = head;

        for (int i = 1; i < values.length; i++) {
            current.next = createNode(values[i]);
            current.next.prev = current;   // back-link: new node → predecessor
            current = current.next;
        }

        return head;
    }

    static void traverse(Node head) {
        Node current = head;
        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }
        System.out.println();
    }

    /*
        Walk to the tail, then follow prev backwards.
        Used AFTER merging to prove the seam is correct
        in the backward direction too.
    */

    static void traverseBackward(Node head) {

        if (head == null) { System.out.println("(empty)"); return; }

        Node current = head;

        while (current.next != null) {      // walk forward to the tail
            current = current.next;
        }

        while (current != null) {           // walk back via prev
            System.out.print(current.data + " ");
            current = current.prev;
        }

        System.out.println();
    }


    // ======================================================
    // MERGE TWO DOUBLY LINKED LISTS
    // ======================================================

    static Node mergeDoubly(Node head1, Node head2) {

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
            STEP 1: Walk to the LAST node of list 1.

                    head1
                      ↓
            null ← [10] ⇄ [20] ⇄ [30] → null   ← this list IS linear,
                                                  so the normal walk works:
                                                  stop when next == null.
            */

        Node last1 = head1;
        while (last1.next != null) {
            last1 = last1.next;
        }


        /*
            ─────────────────────────────────────────
            STEP 2: Connect the seam — in BOTH directions.

                Before:

                    null ← [10] ⇄ [20] ⇄ [30] → null   ← [30].next dangles
                    null ← [40] ⇄ [50] → null          ← [40].prev dangles

                After:

                    null ← [10] ⇄ [20] ⇄ [30] ⇄ [40] ⇄ [50] → null


                ARROW 1 (forward):  last1.next = head2;

                ARROW 2 (backward): head2.prev = last1;

                ⚠ FORGETTING ARROW 2 IS THE CLASSIC BUG.
                The forward traversal would look perfect, but any backward
                traversal from the tail would die at [40]. In an SLL merge
                there is only one arrow — the DLL demands both. This is why
                main() verifies with a BACKWARD traverse.

                No other node changes: O(1) rewiring after the O(n1) walk.
        */

        last1.next = head2;           // ARROW 1: forward across the seam
        head2.prev = last1;           // ARROW 2: backward across the seam


        /*
            Return head1 — the merged list starts where list 1 started.
        */

        return head1;
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        /*
            Build two independent lists:

                list 1:  null ← [10] ⇄ [20] ⇄ [30] → null
                list 2:  null ← [40] ⇄ [50] → null
        */

        Node list1 = buildList(new int[]{10, 20, 30});
        Node list2 = buildList(new int[]{40, 50});

        System.out.print("List 1 before merge: ");
        traverse(list1);             // 10 20 30

        System.out.print("List 2 before merge: ");
        traverse(list2);             // 40 50

        /*
            Merge them into one doubly linked list starting at head1.
        */

        Node merged = mergeDoubly(list1, list2);

        System.out.println();
        System.out.print("Merged (forward):    ");
        traverse(merged);            // 10 20 30 40 50

        /*
            THE PROOF: walk backward from the tail.
            If arrow 2 (head2.prev = last1) were missing, this would
            print only "50" — the backward chain would break at the seam.
        */

        System.out.print("Merged (backward):   ");
        traverseBackward(merged);    // 50 40 30 20 10

        /*
            No free() loop needed — the GC reclaims everything
            once merged goes out of scope.
        */
    }
}
