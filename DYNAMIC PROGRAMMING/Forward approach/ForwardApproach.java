public class ForwardApproach {
    static final int N = 12;
    static final int INF = 999999;

    public static void main(String[] args) {
        int[][] graph = new int[N + 1][N + 1];
        int[] bcost = new int[N + 1];
        int[] pred = new int[N + 1];

        // Define the edges and their costs
        graph[1][2] = 9;
        graph[1][3] = 7;
        graph[1][4] = 3;
        graph[1][5] = 2;

        graph[2][6] = 4;
        graph[2][7] = 2;
        graph[2][8] = 1;

        graph[3][6] = 2;
        graph[3][7] = 7;

        graph[4][8] = 11;

        graph[5][7] = 11;
        graph[5][8] = 8;

        graph[6][9] = 6;
        graph[6][10] = 5;

        graph[7][9] = 4;
        graph[7][10] = 3;

        graph[8][10] = 5;
        graph[8][11] = 6;

        graph[9][12] = 4;
        graph[10][12] = 2;
        graph[11][12] = 5;

        // Initialize all costs and predecessors
        for (int j = 1; j <= N; j++) {
            bcost[j] = INF;
            pred[j] = -1;
        }

        // Base case: source vertex
        bcost[1] = 0;

        // Work forward from vertex 2 to vertex 12
        for (int j = 2; j <= N; j++) {
            for (int i = 1; i < j; i++) {
                if (graph[i][j] != 0 && bcost[i] != INF) {
                    int total = bcost[i] + graph[i][j];

                    if (total < bcost[j]) {
                        bcost[j] = total;
                        pred[j] = i;
                    }
                }
            }
        }

        // Display minimum costs from vertex 1
        System.out.println("Minimum costs from vertex 1:");
        for (int j = 1; j <= N; j++) {
            if (bcost[j] == INF) {
                System.out.println("BCOST(" + j + ") = Unreachable");
            } else {
                System.out.println("BCOST(" + j + ") = " + bcost[j]);
            }
        }

        System.out.println("Minimum cost to vertex 12: " + bcost[12]);

        // Reconstruct the shortest path from vertex 1 to vertex 12
        int[] path = new int[N + 1];
        int count = 0;
        int current = 12;

        while (current != -1) {
            path[count++] = current;

            if (current == 1) {
                break;
            }

            current = pred[current];
        }

        System.out.print("Shortest path: ");
        for (int i = count - 1; i >= 0; i--) {
            System.out.print(path[i]);
            if (i > 0) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
    }
}
