/**
 * Depth-First Search (DFS) - Binary Tree Traversal (Pre-order)
 *
 * DFS goes as deep as possible along each branch before backtracking.
 * This pre-order version visits the current node, then the left subtree,
 * then the right subtree.
 *
 * Java equivalent of DFS.c
 *
 * C used struct Node + malloc + recursion.
 * Java uses a static class with references and the same recursion.
 */
public class DFS {

    static class Node {
        char data;
        Node left;
        Node right;
    }

    static Node createNode(char data) {
        Node newNode = new Node();
        newNode.data = data;
        newNode.left = null;
        newNode.right = null;
        return newNode;
    }

    /* Depth-First Search (pre-order). */
    static void DFS(Node root) {
        if (root == null) {
            return;
        }

        System.out.print(root.data + " ");  // Visit current node

        DFS(root.left);                      // Explore left subtree

        DFS(root.right);                     // Explore right subtree
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

        System.out.print("DFS traversal: ");
        DFS(root);
        System.out.println();
    }
}
