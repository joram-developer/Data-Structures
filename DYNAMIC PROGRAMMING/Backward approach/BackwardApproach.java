public class BackwardApproach {
    static final int N = 12;
    static final int INF = 999999;

    public static void main(String[] args) {
        int[][] graph = new int[N + 1][N + 1];
        int[] cost = new int[N + 1];
        int[] next = new int[N + 1];

        /* Edges from the lecturer's multistage graph */
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

        /* Base case: destination */
        cost[12] = 0;
        next[12] = -1;

        /* Work backward from vertex 11 to vertex 1 */
        for (int j = 11; j >= 1; j--) {
            cost[j] = INF;
            next[j] = -1;

            for (int i = j + 1; i <= N; i++) {
                if (graph[j][i] != 0 && cost[i] != INF) {
                    int total = graph[j][i] + cost[i];

                    if (total < cost[j]) {
                        cost[j] = total;
                        next[j] = i;
                    }
                }
            }
        }

        System.out.println("Minimum cost: " + cost[1]);
        System.out.print("Shortest path: ");

        int i = 1;
        System.out.print(i);

        while (i != 12 && next[i] != -1) {
            i = next[i];
            System.out.print(" -> " + i);
        }

        System.out.println();
    }
}
