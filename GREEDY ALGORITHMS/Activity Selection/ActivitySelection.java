/*
 * Activity Selection - Greedy Algorithm (Java)
 *
 * Problem: Given n activities, each with a start time and finish time,
 * select the maximum number of activities that can be performed by a
 * single person, assuming only one activity can run at a time.
 *
 * Greedy Strategy: Always pick the next activity with the earliest
 * finish time that starts after the last selected activity ends.
 * Sorting by finish time and greedily selecting is provably optimal.
 */

import java.util.Arrays;
import java.util.Scanner;

public class ActivitySelection {

    static class Activity {
        int id, start, finish;

        Activity(int id, int start, int finish) {
            this.id     = id;
            this.start  = start;
            this.finish = finish;
        }
    }

    static void activitySelection(Activity[] acts) {
        // Sort by finish time
        Arrays.sort(acts, (a, b) -> a.finish - b.finish);

        System.out.println("\nActivity Selection - Greedy Algorithm");
        System.out.println("======================================");
        System.out.printf("%-10s %-10s %-10s%n", "Activity", "Start", "Finish");
        System.out.println("-------------------------------");
        for (Activity a : acts) {
            System.out.printf("%-10d %-10d %-10d%n", a.id, a.start, a.finish);
        }

        System.out.println("\nSelected Activities:");
        System.out.printf("%-10s %-10s %-10s%n", "Activity", "Start", "Finish");
        System.out.println("-------------------------------");

        // Always select the first activity (earliest finish time)
        int lastFinish = acts[0].finish;
        int count = 1;
        System.out.printf("%-10d %-10d %-10d%n", acts[0].id, acts[0].start, acts[0].finish);

        for (int i = 1; i < acts.length; i++) {
            // Select activity if it starts at or after the last selected one finishes
            if (acts[i].start >= lastFinish) {
                System.out.printf("%-10d %-10d %-10d%n", acts[i].id, acts[i].start, acts[i].finish);
                lastFinish = acts[i].finish;
                count++;
            }
        }

        System.out.println("\nTotal activities selected: " + count + " out of " + acts.length);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of activities: ");
        int n = scanner.nextInt();

        if (n <= 0) {
            System.out.println("Invalid number of activities.");
            scanner.close();
            return;
        }

        Activity[] acts = new Activity[n];

        System.out.println("Enter start and finish times for each activity:");
        for (int i = 0; i < n; i++) {
            int start, finish;
            System.out.print("  Activity " + (i + 1) + " - Start: ");
            start = scanner.nextInt();
            System.out.print("  Activity " + (i + 1) + " - Finish: ");
            finish = scanner.nextInt();

            if (finish <= start) {
                System.out.println("  Finish time must be greater than start time. Re-enter.");
                i--;
                continue;
            }

            acts[i] = new Activity(i + 1, start, finish);
        }

        activitySelection(acts);
        scanner.close();
    }
}
