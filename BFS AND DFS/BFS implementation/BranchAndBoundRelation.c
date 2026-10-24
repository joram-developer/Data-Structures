
#include <stdio.h>

#define MAX_NODES 7

typedef struct {// this is a templet describing one node in a tree
    int id; //Identifies the node. For example, the node A has ID 1.
    int left;// Tells us where to find the left child in the tree array. If it is -1, there is no left child.
    int right;// Tells us where to find the right child in the tree array. If it is -1, there is no right child.
    int bound;
    int solutionCost;  // -1 means this is not a complete solution and a non-negative value such as 8 or 10 represents the cost of a complete solution.
} Node;

Node tree[MAX_NODES] = {// Create an array named tree that can hold seven Node structures.
    {0, 1, 2,  0, -1},  // Start
    {1, 3, 4,  5, -1},  // A
    {2, 5, 6,  4, -1},  // B
    {3, -1, -1, 10, 10}, // A1: complete solution
    {4, -1, -1, 12, -1}, // A2: incomplete branch
    {5, -1, -1,  8,  8}, // B1: complete solution
    {6, -1, -1,  9, -1}  // B2: incomplete branch
};

/*You could access fields of each node like this: tree[0].id, tree[0].left, tree[0].right, etc. */

const char *names[MAX_NODES] = {// char * represents a pointer to characters, commonly used to refer to a string.
    "Start", "A", "B", "A1", "A2", "B1", "B2"
};

/* Branch and Bound using Breadth-First Search */
void branchBoundBFS(void) {
    int queue[MAX_NODES];// this queue will store node indices for processing
    int front = 0, rear = 0;
    int bestCost = 999999; //This variable stores the cost of the best complete solution found so far.
    int bestNode = -1; //This variable stores the index of the node containing the best complete solution.
                       // -1 means no complete solution has been found yet.
    queue[rear++] = 0;

    printf("\nB&B using BFS:\n");

    while (front < rear) {
        int current = queue[front++];

        if (tree[current].bound >= bestCost) {//Is this node's bound greater than or equal to the cost of the best solution we have already found?
            printf("Prune %s (bound %d)\n",
                   names[current], tree[current].bound);
            continue;//This tells the program to stop processing the current iteration of the while loop and move to the next iteration.
            /*Once a node has been pruned, we don't want to process its solution status or add its children to the queue. We have already decided that the branch cannot improve the answer*/
        }

        printf("Explore %s\n", names[current]);

        if (tree[current].solutionCost != -1) {//this condition checks whether the current node represents a complete solution.
            if (tree[current].solutionCost < bestCost) {//Is the cost of this new solution lower than the cost of our current best solution?
                bestCost = tree[current].solutionCost;
                bestNode = current;
                printf("New best: %s, cost %d\n",
                       names[bestNode], bestCost);
            }
            continue;
            /*Once the program identifies a complete solution, it has no reason to expand that node's children.
             A complete solution is an endpoint for this search.
             A complete solution is evaluated as a candidate answer, but it is not expanded further.*/
        }

        if (tree[current].left != -1)
            queue[rear++] = tree[current].left;

        if (tree[current].right != -1)
            queue[rear++] = tree[current].right;
    }

    if (bestNode != -1)
        printf("Best solution: %s, cost %d\n",
               names[bestNode], bestCost);
}

/* Branch and Bound using Depth-First Search */
void branchBoundDFS(void) {
    int stack[MAX_NODES];
    int top = -1;
    int bestCost = 999999;
    int bestNode = -1;

    stack[++top] = 0;

    printf("\nB&B using DFS:\n");

    while (top >= 0) {
        int current = stack[top--];

        if (tree[current].bound >= bestCost) {
            printf("Prune %s (bound %d)\n",
                   names[current], tree[current].bound);
            continue;
        }

        printf("Explore %s\n", names[current]);

        if (tree[current].solutionCost != -1) {
            if (tree[current].solutionCost < bestCost) {
                bestCost = tree[current].solutionCost;
                bestNode = current;
                printf("New best: %s, cost %d\n",
                       names[bestNode], bestCost);
            }
            continue;
        }

        /*
          Push right first so left is explored first.
          A stack is Last In, First Out.
        */
        if (tree[current].right != -1)
            stack[++top] = tree[current].right;

        if (tree[current].left != -1)
            stack[++top] = tree[current].left;
    }

    if (bestNode != -1)
        printf("Best solution: %s, cost %d\n",
               names[bestNode], bestCost);
}

int main(void) {
    branchBoundBFS();
    branchBoundDFS();

    return 0;
}




