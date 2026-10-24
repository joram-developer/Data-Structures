/**
 * Problem 3: Find a particular value in a binary tree using BFS and DFS
 *
 * Java equivalent of DfsAndDfsProblem.c
 *
 * A. Problem statement
 * Given a binary tree and a target value, write two programs to determine
 * whether the target exists in the tree:
 *   - BFS: search level by level using a queue.
 *   - DFS: search down one branch before returning, using recursion.
 *
 * C used struct Node + malloc + int return (1/0).
 * Java uses a static class with references and boolean returns.
 */
public class DfsAndDfsProblem {

    /* Describes what every node in the binary tree should have. */
    static class Node {
        char data;
        Node left;
        Node right;
    }

    /* createNode(value) returns a node with data=value and left/right=null. */
    static Node createNode(char value) {
        Node node = new Node();
        node.data = value;
        node.left = null;
        node.right = null;
        return node;
    }

    /* Breadth-First Search. Returns true if target is found. */
    static boolean BFS(Node root, char target) {
        if (root == null) {
            return false;
        }

        Node[] queue = new Node[100];   // array of 100 references to nodes
        int front = 0, rear = 0;

        queue[rear++] = root;

        while (front < rear) {
            Node current = queue[front++];

            System.out.print(current.data + " ");

            if (current.data == target) {
                return true;
            }

            if (current.left != null) {
                queue[rear++] = current.left;
            }

            if (current.right != null) {
                queue[rear++] = current.right;
            }
        }

        return false;
    }

    /* Depth-First Search (recursive). Returns true if target is found. */
    static boolean DFS(Node root, char target) {
        if (root == null) {
            return false;
        }

        System.out.print(root.data + " ");

        if (root.data == target) {
            return true;
        }

        if (DFS(root.left, target)) {
            return true;
        }

        if (DFS(root.right, target)) {
            return true;
        }

        return false;
    }

    /*
     * Depth-First Search using a stack (iterative alternative).
     * Shown as a comment in the C version; provided here for completeness.
     */
    // static boolean DFS_stack(Node root, char target) {
    //     if (root == null) return false;
    //
    //     Node[] stack = new Node[100];
    //     int top = -1;
    //
    //     stack[++top] = root;
    //
    //     while (top >= 0) {
    //         Node current = stack[top--];
    //
    //         System.out.print(current.data + " ");
    //
    //         if (current.data == target) return true;
    //
    //         /* Push right first, so left is processed first */
    //         if (current.right != null) stack[++top] = current.right;
    //         if (current.left  != null) stack[++top] = current.left;
    //     }
    //
    //     return false;
    // }

    public static void main(String[] args) {
        Node root = createNode('A');

        root.left = createNode('B');
        root.right = createNode('C');
        root.left.left = createNode('D');
        root.left.right = createNode('E');
        root.right.left = createNode('F');
        root.right.right = createNode('G');

        char target = 'F';

        System.out.print("BFS search order: ");
        if (BFS(root, target)) {
            System.out.println("\nTarget found!");
        } else {
            System.out.println("\nTarget not found.");
        }

        System.out.println();

        System.out.print("DFS search order: ");
        if (DFS(root, target)) {
            System.out.println("\nTarget found!");
        } else {
            System.out.println("\nTarget not found.");
        }
    }
}
