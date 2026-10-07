
#include <stdio.h>

#define N 12
#define INF 999999

int main(void) {
    int graph[N + 1][N + 1] = {0};
    int bcost[N + 1];
    int pred[N + 1];
    int i, j;

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
    for (j = 1; j <= N; j++) {
        bcost[j] = INF;
        pred[j] = -1;
    }

    // Base case: source vertex
    bcost[1] = 0;

    // Work forward from vertex 2 to vertex 12
    for (j = 2; j <= N; j++) {
        for (i = 1; i < j; i++) {
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
    printf("Minimum costs from vertex 1:\n");

    for (j = 1; j <= N; j++) {
        if (bcost[j] == INF) {
            printf("BCOST(%d) = Unreachable\n", j);
        } else {
            printf("BCOST(%d) = %d\n", j, bcost[j]);
        }
    }

    printf("Minimum cost to vertex 12: %d\n", bcost[12]);

    // Reconstruct the shortest path from vertex 1 to vertex 12
    int path[N + 1];
    int count = 0;
    int current = 12;

    while (current != -1) {// While there is a valid predecessor for the current vertex
        path[count++] = current;// Store the current vertex in the path array and increment the count

        if (current == 1) {// If the current vertex is the source vertex, we have reached the start of the path
            break;
        }

        current = pred[current];// Move to the predecessor of the current vertex
    }

    printf("Shortest path: ");

    for (i = count - 1; i >= 0; i--) {
        printf("%d", path[i]);

        if (i > 0) {
            printf(" -> ");
        }
    }

    printf("\n");

    return 0;
}