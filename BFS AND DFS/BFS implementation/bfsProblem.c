
//Problem 1: Find the shortest path in an unweighted graph using BFS

/*
Algorithm BFS_ShortestPath(graph, start, goal)

1. Create an empty queue.
2. Mark start as visited.
3. Add start to the queue.
4. Set parent[start] = NONE.

5. While the queue is not empty:
      a. Remove the front vertex and call it current.

      b. If current equals goal:
             Reconstruct the path using parent.
             Return the path.

      c. For each neighbour of current:
             If the neighbour has not been visited:
                 Mark it as visited.
                 Set parent[neighbour] = current.
                 Add the neighbour to the queue.

6. Return "No path exists".
*/


#include <stdio.h>

#define N 6

char vertices[N] = {'A', 'B', 'C', 'D', 'E', 'F'};

/* 1 means an edge exists; 0 means no edge */
int graph[N][N] = {
    {0, 1, 1, 0, 0, 0},  // A
    {1, 0, 0, 1, 0, 0},  // B
    {1, 0, 0, 0, 1, 0},  // C
    {0, 1, 0, 0, 0, 1},  // D
    {0, 0, 1, 0, 0, 1},  // E
    {0, 0, 0, 1, 1, 0}   // F
};

void BFS_shortestPath(int start, int goal) {//start is the index of the vertex where the search begins.
                                            //goal is the index of the destination vertex we want to reach
    int queue[N];
    int front = 0, rear = 0;
    int visited[N] = {0};
    int parent[N];//The parent array remembers how we reached each vertex.

    for (int i = 0; i < N; i++)
        parent[i] = -1;

    visited[start] = 1;//1 means that vertex was visited. 0 means that vertex was not visited yet.
    queue[rear++] = start;

    while (front < rear) {
        int current = queue[front++];

        if (current == goal)
            break;

        for (int neighbour = 0; neighbour < N; neighbour++) {
            if (graph[current][neighbour] == 1 &&
                !visited[neighbour]) {

                visited[neighbour] = 1;
                parent[neighbour] = current;
                queue[rear++] = neighbour;
            }
        }
    }

    if (!visited[goal]) {
        printf("No path exists.\n");
        return;
    }

    int path[N];
    int length = 0;

    for (int v = goal; v != -1; v = parent[v])
        path[length++] = v;

    printf("Shortest path: ");

    for (int i = length - 1; i >= 0; i--) {
        printf("%c", vertices[path[i]]);

        if (i > 0)
            printf(" -> ");
    }

    printf("\nNumber of edges: %d\n", length - 1);
}

int main(void) {
    BFS_shortestPath(0, 5);  // A to F
    return 0;
}

