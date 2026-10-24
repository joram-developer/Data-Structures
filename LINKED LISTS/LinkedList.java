/**
 * Simple Linked List - Manual Node Creation and Traversal
 *
 * Java equivalent of linkedList.c
 *
 * In C, nodes are created with malloc() and connected with pointers.
 * In Java, nodes are created with "new" and connected with references.
 * Java has no free() - the garbage collector reclaims unused objects.
 *
 * C:  struct Node { int data; struct Node *next; };
 * Java: static class Node { int data; Node next; }
 *
 *   C uses -> to access fields through a pointer:  node1->data
 *   Java uses . to access fields through a reference: node1.data
 *
 *   C uses NULL; Java uses null.
 */
public class LinkedList {

    static class Node {
        int data;
        Node next;
    }

    public static void main(String[] args) {

        Node node1;
        Node node2;
        Node node3;

        // Allocate memory. C: malloc(sizeof(struct Node)); Java: new Node()
        node1 = new Node();
        node2 = new Node();
        node3 = new Node();

        // Store data
        node1.data = 10;
        node2.data = 20;
        node3.data = 30;

        // Connect nodes
        node1.next = node2;
        node2.next = node3;
        node3.next = null;

        // Head points to first node
        Node head = node1;

        // Traverse the list
        Node current = head;

        while (current != null) {
            System.out.print(current.data + " ");
            current = current.next;
        }

        System.out.println();

        // Free memory: not needed in Java. When these references go out of
        // scope, the garbage collector reclaims the nodes automatically.
        // (C used free(node1); free(node2); free(node3);)
    }
}
