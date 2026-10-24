/**
 * Linked List Traversal - With createNode and traverse Helpers
 *
 * Java equivalent of traverse.c
 *
 * Demonstrates a clean linked list with separate createNode() and
 * traverse() functions, exactly like the C original.
 *
 *   C:  struct Node *createNode(int data);
 *   Java: static Node createNode(int data);
 *
 *   C:  void traverse(struct Node *head);
 *   Java: static void traverse(Node head);
 */
public class Traverse {

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

    /* Function to traverse and print the linked list.
       Starts from the head and follows each next reference to the end. */
    static void traverse(Node head) {
        Node current = head;   // Start from the head of the list

        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;   // Move to the next node
        }

        System.out.println();
    }

    public static void main(String[] args) {

        Node head = createNode(10);    // Create the head node with data 10
        Node node2 = createNode(20);
        Node node3 = createNode(30);

        head.next = node2;
        node2.next = node3;

        traverse(head);

        // No need to free memory in Java.
        // (C used free(head); free(node2); free(node3);)
        // When these references go out of scope the garbage collector
        // reclaims all nodes automatically.
    }
}
