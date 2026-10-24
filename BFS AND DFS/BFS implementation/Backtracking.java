/**
 * Backtracking Search in a Binary Tree
 *
 * Backtracking tries one path at a time. If a path leads to a dead end
 * (null) without finding the target, it backs up and tries the next path.
 *
 * Java equivalent of Backtracking.c
 *
 * C used struct Node + malloc; Java uses a static class with references
 * and "new". The recursive backtrack logic is identical.
 */
public class Backtracking {

    static class Node {
        char data;
        Node left;
        Node right;
    }

    static Node createNode(char data) {
        Node n = new Node();
        n.data = data;
        n.left = null;
        n.right = null;
        return n;
    }

    /*
     * backtrack
     *   Returns 1 (true) if the target is found along this path, 0 (false) otherwise.
     *   In C this returned int (1/0). In Java we return boolean for clarity,
     *   but the logic is the same.
     */
    static boolean backtrack(Node root, char target) {
        if (root == null) {
            return false;   // This path failed
        }

        System.out.println("Trying " + root.data);

        if (root.data == target) {
            return true;     // Target found
        }

        // Try left path first
        if (backtrack(root.left, target)) {
            return true;
        }

        // If left path fails, try right path
        if (backtrack(root.right, target)) {
            return true;
        }

        return false;        // Neither path worked
    }

    public static void main(String[] args) {
        Node root = createNode('A');

        root.left = createNode('B');
        root.right = createNode('C');
        root.left.left = createNode('D');
        root.left.right = createNode('E');
        root.right.left = createNode('F');
        root.right.right = createNode('G');

        if (backtrack(root, 'G')) {
            System.out.println("Target found!");
        } else {
            System.out.println("Target not found.");
        }
    }
}
