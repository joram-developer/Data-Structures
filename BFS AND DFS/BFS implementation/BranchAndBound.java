/**
 * Branch and Bound - Simple Example
 *
 * Java equivalent of BranchAndBound.c
 *
 * Each candidate has a lower bound and, if complete, a cost.
 * cost = -1 means the branch is not a complete solution.
 * If a candidate's bound is already >= bestCost, it is pruned.
 *
 * C used a global int bestCost; Java uses a static field.
 */
public class BranchAndBound {

    static int bestCost = 999999;

    /*
     * bound[] : lower bound for each candidate
     * cost[]  : complete solution cost (-1 if incomplete)
     * n       : number of candidates
     */
    static void branchAndBound(int[] bound, int[] cost, int n) {
        for (int i = 0; i < n; i++) {
            if (bound[i] >= bestCost) {
                System.out.println("Prune candidate " + (i + 1) + " (bound " + bound[i] + ")");
                continue;
            }

            System.out.println("Explore candidate " + (i + 1));

            if (cost[i] != -1 && cost[i] < bestCost) {
                bestCost = cost[i];
                System.out.println("New best cost: " + bestCost);
            }
        }
    }

    public static void main(String[] args) {
        int[] bound = {10, 8, 12};
        int[] cost  = {10, 8, 12};
        int n = 3;

        branchAndBound(bound, cost, n);

        System.out.println("Best solution cost: " + bestCost);
    }
}
