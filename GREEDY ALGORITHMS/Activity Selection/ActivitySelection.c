/*
 * Activity Selection - Greedy Algorithm (C)
 *
 * Problem: Given n activities, each with a start time and finish time,
 * select the maximum number of activities that can be performed by a
 * single person, assuming only one activity can run at a time.
 *
 * Greedy Strategy: Always pick the next activity with the earliest
 * finish time that starts after the last selected activity ends.
 * Sorting by finish time and greedily selecting is provably optimal.
 */

#include <stdio.h>

#define MAX 100

typedef struct {
    int id;
    int start;
    int finish;
} Activity;

// Sort activities by finish time (bubble sort)
void sortByFinish(Activity acts[], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (acts[j].finish > acts[j + 1].finish) {
                Activity temp = acts[j];
                acts[j]       = acts[j + 1];
                acts[j + 1]   = temp;
            }
        }
    }
}

void activitySelection(Activity acts[], int n) {
    sortByFinish(acts, n);

    printf("\nActivity Selection - Greedy Algorithm\n");
    printf("======================================\n");
    printf("%-10s %-10s %-10s\n", "Activity", "Start", "Finish");
    printf("-------------------------------\n");
    for (int i = 0; i < n; i++) {
        printf("%-10d %-10d %-10d\n", acts[i].id, acts[i].start, acts[i].finish);
    }

    printf("\nSelected Activities:\n");
    printf("%-10s %-10s %-10s\n", "Activity", "Start", "Finish");
    printf("-------------------------------\n");

    // Always select the first activity (earliest finish time)
    int lastFinish = acts[0].finish;
    int count = 1;
    printf("%-10d %-10d %-10d\n", acts[0].id, acts[0].start, acts[0].finish);

    for (int i = 1; i < n; i++) {
        // Select activity if it starts at or after the last selected one finishes
        if (acts[i].start >= lastFinish) {
            printf("%-10d %-10d %-10d\n", acts[i].id, acts[i].start, acts[i].finish);
            lastFinish = acts[i].finish;
            count++;
        }
    }

    printf("\nTotal activities selected: %d out of %d\n", count, n);
}

int main() {
    int n;

    printf("Enter the number of activities: ");
    scanf("%d", &n);

    if (n <= 0 || n > MAX) {
        printf("Invalid number of activities.\n");
        return 1;
    }

    Activity acts[MAX];

    printf("Enter start and finish times for each activity:\n");
    for (int i = 0; i < n; i++) {
        acts[i].id = i + 1;
        printf("  Activity %d - Start: ", i + 1);
        scanf("%d", &acts[i].start);
        printf("  Activity %d - Finish: ", i + 1);
        scanf("%d", &acts[i].finish);

        if (acts[i].finish <= acts[i].start) {
            printf("  Finish time must be greater than start time. Re-enter.\n");
            i--;
        }
    }

    activitySelection(acts, n);

    return 0;
}
