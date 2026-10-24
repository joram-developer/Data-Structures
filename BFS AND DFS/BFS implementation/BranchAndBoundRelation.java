/**
 * Branch and Bound using BFS and DFS - Relation Example
 *
 * Java equivalent of BranchAndBoundRelation.c
 *
 * A tree of candidates is stored in an array. Each node has a bound,
 * a solution cost (-1 if incomplete), and left/right child indices.
 * Branch and Bound explores the tree, pruning any node whose bound
 * is >= the best complete solution cost found so far.
 *
 * Two search orders are shown:
 *   - branchBoundBFS : uses a queue (FIFO)  -> explores level by level
 *   - branchBoundDFS : uses a stack (LIFO)  -> explores depth first
 *
 * C used a typedef struct Node and a global array tree[].
 * Java uses a static inner class and a static array.
 */
public class BranchAndBoundRelation {

    static final int MAX_NODES = 7;

    /* Template describing one node in a tree.
       C:  typedef struct { int id; int left; int right; int bound; int solutionCost; } Node;
       id            identifies the node (A has ID 1).
       left/right    index of left/right child in the tree array (-1 = none).
       bound         lower bound for this branch.
       solutionCost  -1 = incomplete branch, non-negative = complete solution cost. */
    static class Node {
        int id;
        int left;
        int right;
        int bound;
        int solutionCost;

        Node(int id, int left, int right, int bound, int solutionCost) {
            this.id = id;
            this.left = left;
            this.right = right;
            this.bound = bound;
            this.solutionCost = solutionCost;
        }
    }

    /* Create an array named tree that can hold seven Node structures. */
    static Node[] tree = {
        new Node(0, 1, 2,  0, -1),  // Start
        new Node(1, 3, 4,  5, -1),  // A
        new Node(2, 5, 6,  4, -1),  // B
        new Node(3, -1, -1, 10, 10), // A1: complete solution
        new Node(4, -1, -1, 12, -1), // A2: incomplete branch
        new Node(5, -1, -1,  8,  8), // B1: complete solution
        new Node(6, -1, -1,  9, -1)  // B2: incomplete branch
    };

    /* C: const char *names[]; Java: String[] names. */
    static String[] names = {
        "Start", "A", "B", "A1", "A2", "B1", "B2"
    };

    /* Branch and Bound using Breadth-First Search */
    static void branchBoundBFS() {
        int[] queue = new int[MAX_NODES];   // queue stores node indices
        int front = 0, rear = 0;
        int bestCost = 999999;              // best complete solution cost so far
        int bestNode = -1;                   // index of best complete solution (-1 = none yet)
        queue[rear++] = 0;

        System.out.println("\nB&B using BFS:");

        while (front < rear) {
            int current = queue[front++];

            if (tree[current].bound >= bestCost) {
                System.out.println("Prune " + names[current] + " (bound " + tree[current].bound + ")");
                continue;   // skip the rest of this iteration
            }

            System.out.println("Explore " + names[current]);

            if (tree[current].solutionCost != -1) {
                if (tree[current].solutionCost < bestCost) {
                    bestCost = tree[current].solutionCost;
                    bestNode = current;
                    System.out.println("New best: " + names[bestNode] + ", cost " + bestCost);
                }
                continue;   // complete solution is an endpoint; do not expand children
            }

            if (tree[current].left != -1) {
                queue[rear++] = tree[current].left;
            }

            if (tree[current].right != -1) {
                queue[rear++] = tree[current].right;
            }
        }

        if (bestNode != -1) {
            System.out.println("Best solution: " + names[bestNode] + ", cost " + bestCost);
        }
    }

    /* Branch and Bound using Depth-First Search */
    static void branchBoundDFS() {
        int[] stack = new int[MAX_NODES];
        int top = -1;
        int bestCost = 999999;
        int bestNode = -1;

        stack[++top] = 0;

        System.out.println("\nB&B using DFS:");

        while (top >= 0) {
            int current = stack[top--];

            if (tree[current].bound >= bestCost) {
                System.out.println("Prune " + names[current] + " (bound " + tree[current].bound + ")");
                continue;
            }

            System.out.println("Explore " + names[current]);

            if (tree[current].solutionCost != -1) {
                if (tree[current].solutionCost < bestCost) {
                    bestCost = tree[current].solutionCost;
                    bestNode = current;
                    System.out.println("New best: " + names[bestNode] + ", cost " + bestCost);
                }
                continue;
            }

            /*
              Push right first so left is explored first.
              A stack is Last In, First Out.
            */
            if (tree[current].right != -1) {
                stack[++top] = tree[current].right;
            }

            if (tree[current].left != -1) {
                stack[++top] = tree[current].left;
            }
        }

        if (bestNode != -1) {
            System.out.println("Best solution: " + names[bestNode] + ", cost " + bestCost);
        }
    }

    public static void main(String[] args) {
        branchBoundBFS();
        branchBoundDFS();
    }
}
