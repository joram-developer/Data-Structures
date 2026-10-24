/**
 * Breadth-First Search (BFS) - Binary Tree Traversal
 *
 * BFS explores a tree level by level, starting from the root and visiting
 * all nodes at the current depth before moving to the next depth.
 *
 * Java equivalent of BFS.c
 *
 * In C this used an explicit Queue struct with an array and front/rear
 * indices. In Java we use a simple array-based queue to mirror the same
 * logic. Java uses references instead of pointers, and "new" instead of
 * malloc.
 */
public class BFS {

    /* Structure representing a tree node.
       C:  struct Node { char data; struct Node *left, *right; };
       Java: a static inner class with references instead of pointers. */
    static class Node {
        char data;
        Node left;
        Node right;
    }

    /* Create a new tree node.
       C:  malloc + check NULL + assign fields.
       Java: "new" allocates automatically; no NULL check needed. */
    static Node createNode(char data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.left = null;
        newNode.right = null;
        return newNode;
    }

    /* Breadth-First Search using an array-based queue.
       This mirrors the C Queue struct (nodes[100], front, rear). */
    static void BFS(Node root) {
        if (root == null) {
            return;
        }

        Node[] queue = new Node[100];   // queue of nodes
        int front = 0;                  // front index
        int rear = 0;                   // rear index

        queue[rear++] = root;           // enqueue root

        while (front < rear) {          // while queue is not empty
            Node current = queue[front++];   // dequeue front node

            System.out.print(current.data + " ");

            if (current.left != null) {
                queue[rear++] = current.left;   // enqueue left child
            }

            if (current.right != null) {
                queue[rear++] = current.right;  // enqueue right child
            }
        }
    }

    public static void main(String[] args) {
        /*
                 A
                / \
               B   C
              / \ / \
             D  E F  G
        */

        Node root = createNode('A');

        root.left = createNode('B');
        root.right = createNode('C');

        root.left.left = createNode('D');
        root.left.right = createNode('E');

        root.right.left = createNode('F');
        root.right.right = createNode('G');

        System.out.print("BFS traversal: ");
        BFS(root);
        System.out.println();

        // No need to free memory in Java; the garbage collector handles it.
    }
}
