/**
 * Problem 1: Find the shortest path in an unweighted graph using BFS
 *
 * Java equivalent of bfsProblem.c
 *
 * The graph is represented as an adjacency matrix.
 * BFS finds the shortest path (fewest edges) between two vertices
 * by recording the parent of each visited vertex.
 *
 * C used plain int arrays for the queue, visited, and parent.
 * Java uses the same array approach to mirror the algorithm exactly.
 */
public class BfsProblem {

    static final int N = 6;

    static char[] vertices = {'A', 'B', 'C', 'D', 'E', 'F'};

    /* 1 means an edge exists; 0 means no edge */
    static int[][] graph = {
        {0, 1, 1, 0, 0, 0},  // A
        {1, 0, 0, 1, 0, 0},  // B
        {1, 0, 0, 0, 1, 0},  // C
        {0, 1, 0, 0, 0, 1},  // D
        {0, 0, 1, 0, 0, 1},  // E
        {0, 0, 0, 1, 1, 0}   // F
    };

    /*
     * start is the index of the vertex where the search begins.
     * goal  is the index of the destination vertex we want to reach.
     * parent[] remembers how we reached each vertex.
     */
    static void BFS_shortestPath(int start, int goal) {
        int[] queue = new int[N];
        int front = 0, rear = 0;
        int[] visited = new int[N];       // 1 = visited, 0 = not yet
        int[] parent = new int[N];        // parent of each vertex

        for (int i = 0; i < N; i++) {
            parent[i] = -1;
        }

        visited[start] = 1;
        queue[rear++] = start;

        while (front < rear) {
            int current = queue[front++];

            if (current == goal) {
                break;
            }

            for (int neighbour = 0; neighbour < N; neighbour++) {
                if (graph[current][neighbour] == 1 && visited[neighbour] == 0) {
                    visited[neighbour] = 1;
                    parent[neighbour] = current;
                    queue[rear++] = neighbour;
                }
            }
        }

        if (visited[goal] == 0) {
            System.out.println("No path exists.");
            return;
        }

        int[] path = new int[N];
        int length = 0;

        for (int v = goal; v != -1; v = parent[v]) {
            path[length++] = v;
        }

        System.out.print("Shortest path: ");

        for (int i = length - 1; i >= 0; i--) {
            System.out.print(vertices[path[i]]);

            if (i > 0) {
                System.out.print(" -> ");
            }
        }

        System.out.println("\nNumber of edges: " + (length - 1));
    }

    public static void main(String[] args) {
        BFS_shortestPath(0, 5);  // A to F
    }
}
