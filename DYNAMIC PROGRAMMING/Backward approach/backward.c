#include <stdio.h>

#define N 12
#define INF 999999

int main(void) {
    int graph[N + 1][N + 1] = {0};
    int cost[N + 1];
    int next[N + 1];
    int i, j;

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
    for (j = 11; j >= 1; j--) {
        cost[j] = INF;
        next[j] = -1;

        for (i = j + 1; i <= N; i++) {
            if (graph[j][i] != 0 &&
                cost[i] != INF) {

                int total = graph[j][i] + cost[i];

                if (total < cost[j]) {
                    cost[j] = total;
                    next[j] = i;
                }
            }
        }
    }

    printf("Minimum cost: %d\n", cost[1]);
    printf("Shortest path: ");

    i = 1;
    printf("%d", i);

    // While the current vertex is not the destination and there is a next vertex
    while (i != 12 && next[i] != -1) {
        i = next[i];
        printf(" -> %d", i);
    }

    printf("\n");
    return 0;
}

/*Algorithm BACKWARD-SHORTEST-PATH

Input:
    A multistage graph with source S = 1
    Target t = 12
    Edge costs C(j, i)

Output:
    COST(1), the minimum cost from source 1 to target 12


1. Set COST(12) ← 0

2. For j ← 11 down to 1 do

       2.1 Set COST(j) ← ∞

       2.2 For every possible next vertex i from j do

               2.2.1 Check whether an edge exists from j to i

               2.2.2 If an edge j → i exists
                     AND COST(i) is already available, then

                         2.2.2.1 Calculate:
                                 TOTAL ← C(j,i) + COST(i)

                         2.2.2.2 If TOTAL < COST(j), then

                                 COST(j) ← TOTAL

                         2.2.2.3 End If

               2.2.3 End If

           2.3 End For

   2.4 End For

3. Return COST(1)*/

//Single shortest paths

// #include <stdio.h>

// #define N 12
// #define INF 999999

// int main(void) {

//     int graph[N + 1][N + 1] = {0};
//     int dist[N + 1];
//     int pred[N + 1];

//     int i, j;

//     /* Define the edges */
//     graph[1][2] = 9;
//     graph[1][3] = 7;
//     graph[1][4] = 3;
//     graph[1][5] = 2;

//     graph[2][6] = 4;
//     graph[2][7] = 2;
//     graph[2][8] = 1;

//     graph[3][6] = 2;
//     graph[3][7] = 7;

//     graph[4][8] = 11;

//     graph[5][7] = 11;
//     graph[5][8] = 8;

//     graph[6][9] = 6;
//     graph[6][10] = 5;

//     graph[7][9] = 4;
//     graph[7][10] = 3;

//     graph[8][10] = 5;
//     graph[8][11] = 6;

//     graph[9][12] = 4;
//     graph[10][12] = 2;
//     graph[11][12] = 5;


//     /* Initialize distances and predecessors */
//     for (j = 1; j <= N; j++) {
//         dist[j] = INF;
//         pred[j] = -1;
//     }

//     /* Source */
//     dist[1] = 0;


//     /* Calculate shortest distances from source */
//     for (j = 2; j <= N; j++) {

//         for (i = 1; i < j; i++) {

//             if (graph[i][j] != 0 && dist[i] != INF) {

//                 int total = dist[i] + graph[i][j];

//                 if (total < dist[j]) {
//                     dist[j] = total;
//                     pred[j] = i;
//                 }
//             }
//         }
//     }


//     /* Print shortest distances */
//     printf("Shortest distances from vertex 1:\n");

//     for (j = 1; j <= N; j++) {
//         printf("1 -> %d = %d\n", j, dist[j]);
//     }


//     return 0;
// }