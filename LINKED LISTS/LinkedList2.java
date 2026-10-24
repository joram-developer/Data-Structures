/**
 * Simple Linked List - With createNode Helper
 *
 * Java equivalent of linkedList2.c
 *
 * This version uses a createNode() helper, just like the C original.
 * The logic is identical; only the memory model differs:
 *   C:   malloc + NULL check + pointer fields
 *   Java: new (no NULL check needed) + reference fields + garbage collection
 */
public class LinkedList2 {

    static class Node {
        int data;
        Node next;
    }

    /* Function to create a new node with given data.
       C:  malloc + NULL check + assign fields.
       Java: "new" allocates automatically; no NULL check needed. */
    static Node createNode(int data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.next = null;
        return newNode;
    }

    public static void main(String[] args) {

        Node node1;
        Node node2;
        Node node3;

        node1 = createNode(10);
        node2 = createNode(20);
        node3 = createNode(30);

        // In C we checked for NULL after malloc; in Java "new" never returns
        // null, so the check is omitted. (Allocation errors throw OutOfMemoryError.)

        // Connect nodes
        node1.next = node2;
        node2.next = node3;

        Node current = node1;

        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }

        System.out.println();

        // No need to free in Java; the garbage collector handles it.
        // (C used free(node1); free(node2); free(node3);)
    }
}
