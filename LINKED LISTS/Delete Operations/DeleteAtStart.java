public class DeleteAtStart {

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
    // DELETE AT THE BEGINNING
    // ======================================================

    /*
        Core pointer idea:

            head = head.next;

        That single line IS delete-at-start in Java.
        The old first node simply loses its last reference
        and the garbage collector reclaims it.

        In C this needed three steps because YOU are the
        memory manager:

            struct Node *temp = head;   // 1. save the address
            head = head->next;          // 2. move head forward
            free(temp);                 // 3. release [10]

        In Java, steps 1 and 3 disappear — there is no free()
        and no way to "lose" an address we still need. Once no
        reference points to the old node, it is unreachable,
        and unreachable objects are collected automatically.
    */

    static Node deleteAtBeginning(Node head) {

        /*
            CASE 1: The list is empty.

                head
                 ↓
                null

            Nothing to delete. Return head unchanged.
        */

        if (head == null) {
            System.out.println("List is empty. Nothing to delete.");
            return head;
        }


        /*
            CASE 2: The list has one or more nodes.

                head
                 ↓
               [10] → [20] → [30] → null

            We want to remove [10] and make [20] the new head.

                head = head.next;

            State after:

                     head
                      ↓
               [10] → [20] → [30] → null

            [10] is now orphaned — nothing points to it anymore.
            In C we would have had to free(temp) at this point.
            In Java, the garbage collector will clean up [10]
            on its own. We never touch it again.
        */

        head = head.next;   // advance head; the old node becomes garbage

        return head;        // return new head so caller can update their variable
    }


    // ======================================================
    // MAIN
    // ======================================================

    public static void main(String[] args) {

        /*
            Build the list:  10 → 20 → 30 → null
        */

        Node head = createNode(10);
        head.next = createNode(20);
        head.next.next = createNode(30);

        System.out.print("Original list:             ");
        traverse(head);
        // 10 20 30

        /*
            Delete the first node.

            Before:   10 → 20 → 30
                       ↓
            After:         20 → 30
        */

        head = deleteAtBeginning(head);

        System.out.print("After deleting at start:   ");
        traverse(head);
        // 20 30

        /*
            Delete again — now 20 is the first node.

            Before:   20 → 30
                       ↓
            After:         30
        */

        head = deleteAtBeginning(head);

        System.out.print("After deleting at start:   ");
        traverse(head);
        // 30

        /*
            Delete the last remaining node.

            Before:   30
                       ↓
            After:    (empty list)
        */

        head = deleteAtBeginning(head);

        System.out.print("After deleting at start:   ");
        traverse(head);
        // (blank line — empty list)

        /*
            Try to delete from an empty list.
            Should print a message and do nothing.
        */

        head = deleteAtBeginning(head);

        /*
            No need to free memory in Java.
            The garbage collector cleans up automatically
            once the deleted nodes become unreachable.
        */
    }
}
