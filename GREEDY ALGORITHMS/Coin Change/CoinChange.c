/*
 * Coin Change - Greedy Algorithm (C)
 *
 * Problem: Given a set of coin denominations and a target amount,
 * find the minimum number of coins needed to make that amount.
 *
 * Greedy Strategy: Always pick the largest denomination that does
 * not exceed the remaining amount.
 *
 * Note: This greedy approach gives the optimal result for standard
 * currency systems where denominations are canonical (e.g. 1,5,10,25).
 */

#include <stdio.h>

// Sort denominations in descending order (bubble sort)
void sortDescending(int coins[], int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (coins[j] < coins[j + 1]) {
                int temp = coins[j];
                coins[j] = coins[j + 1];
                coins[j + 1] = temp;
            }
        }
    }
}

void coinChange(int coins[], int n, int amount) {
    sortDescending(coins, n);

    printf("\nGreedy Coin Change\n");
    printf("==================\n");
    printf("Target Amount : %d\n", amount);
    printf("Denominations : ");
    for (int i = 0; i < n; i++) {
        printf("%d ", coins[i]);
    }
    printf("\n\nCoins selected:\n");

    int totalCoins = 0;
    int remaining = amount;

    for (int i = 0; i < n && remaining > 0; i++) {
        if (coins[i] <= remaining) {
            int count = remaining / coins[i];
            remaining  = remaining % coins[i];
            totalCoins += count;
            printf("  Coin %d  x %d\n", coins[i], count);
        }
    }

    if (remaining == 0) {
        printf("\nTotal coins used : %d\n", totalCoins);
    } else {
        printf("\nCannot make exact change for %d with given denominations.\n", amount);
    }
}

int main() {
    int coins[] = {1, 5, 10, 25, 50};
    int n = sizeof(coins) / sizeof(coins[0]);
    int amount;

    printf("Enter the amount to make change for: ");
    scanf("%d", &amount);

    if (amount <= 0) {
        printf("Amount must be a positive integer.\n");
        return 1;
    }

    coinChange(coins, n, amount);

    return 0;
}
