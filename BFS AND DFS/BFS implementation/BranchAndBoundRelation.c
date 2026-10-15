
// #include <stdio.h>

// #define MAX_NODES 7

// typedef struct {
//     int id;
//     int left;
//     int right;
//     int bound;
//     int solutionCost;  // -1 means this is not a complete solution
// } Node;

// Node tree[MAX_NODES] = {
//     {0, 1, 2,  0, -1},  // Start
//     {1, 3, 4,  5, -1},  // A
//     {2, 5, 6,  4, -1},  // B
//     {3, -1, -1, 10, 10}, // A1: complete solution
//     {4, -1, -1, 12, -1}, // A2: incomplete branch
//     {5, -1, -1,  8,  8}, // B1: complete solution
//     {6, -1, -1,  9, -1}  // B2: incomplete branch
// };

// const char *names[MAX_NODES] = {
//     "Start", "A", "B", "A1", "A2", "B1", "B2"
// };

// /* Branch and Bound using Breadth-First Search */
// void branchBoundBFS(void) {
//     int queue[MAX_NODES];
//     int front = 0, rear = 0;
//     int bestCost = 999999;
//     int bestNode = -1;

//     queue[rear++] = 0;

//     printf("\nB&B using BFS:\n");

//     while (front < rear) {
//         int current = queue[front++];

//         if (tree[current].bound >= bestCost) {
//             printf("Prune %s (bound %d)\n",
//                    names[current], tree[current].bound);
//             continue;
//         }

//         printf("Explore %s\n", names[current]);

//         if (tree[current].solutionCost != -1) {
//             if (tree[current].solutionCost < bestCost) {
//                 bestCost = tree[current].solutionCost;
//                 bestNode = current;
//                 printf("New best: %s, cost %d\n",
//                        names[bestNode], bestCost);
//             }
//             continue;
//         }

//         if (tree[current].left != -1)
//             queue[rear++] = tree[current].left;

//         if (tree[current].right != -1)
//             queue[rear++] = tree[current].right;
//     }

//     if (bestNode != -1)
//         printf("Best solution: %s, cost %d\n",
//                names[bestNode], bestCost);
// }

// /* Branch and Bound using Depth-First Search */
// void branchBoundDFS(void) {
//     int stack[MAX_NODES];
//     int top = -1;
//     int bestCost = 999999;
//     int bestNode = -1;

//     stack[++top] = 0;

//     printf("\nB&B using DFS:\n");

//     while (top >= 0) {
//         int current = stack[top--];

//         if (tree[current].bound >= bestCost) {
//             printf("Prune %s (bound %d)\n",
//                    names[current], tree[current].bound);
//             continue;
//         }

//         printf("Explore %s\n", names[current]);

//         if (tree[current].solutionCost != -1) {
//             if (tree[current].solutionCost < bestCost) {
//                 bestCost = tree[current].solutionCost;
//                 bestNode = current;
//                 printf("New best: %s, cost %d\n",
//                        names[bestNode], bestCost);
//             }
//             continue;
//         }

//         /*
//           Push right first so left is explored first.
//           A stack is Last In, First Out.
//         */
//         if (tree[current].right != -1)
//             stack[++top] = tree[current].right;

//         if (tree[current].left != -1)
//             stack[++top] = tree[current].left;
//     }

//     if (bestNode != -1)
//         printf("Best solution: %s, cost %d\n",
//                names[bestNode], bestCost);
// }

// int main(void) {
//     branchBoundBFS();
//     branchBoundDFS();

//     return 0;
// }



#include <stdio.h>

int bestCost = 999999;

/* Each candidate has a lower bound and, if complete, a cost.
   cost = -1 means the branch is not a complete solution. */
void branchAndBound(int bound[], int cost[], int n) {
    for (int i = 0; i < n; i++) {
        if (bound[i] >= bestCost) {
            printf("Prune candidate %d (bound %d)\n",
                   i + 1, bound[i]);
            continue;
        }

        printf("Explore candidate %d\n", i + 1);

        if (cost[i] != -1 && cost[i] < bestCost) {
            bestCost = cost[i];
            printf("New best cost: %d\n", bestCost);
        }
    }
}

int main(void) {
    int bound[] = {10, 8, 12};
    int cost[]  = {10, 8, 12};
    int n = 3;

    branchAndBound(bound, cost, n);

    printf("Best solution cost: %d\n", bestCost);

    return 0;
}
