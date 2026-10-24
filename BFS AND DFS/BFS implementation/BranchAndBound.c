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