/*
 * Coin Change - Greedy Algorithm (Java)
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

import java.util.Arrays;
import java.util.Scanner;

public class CoinChange {

    static void coinChange(int[] coins, int amount) {
        // Sort denominations in descending order
        Integer[] sortedCoins = new Integer[coins.length];
        for (int i = 0; i < coins.length; i++) sortedCoins[i] = coins[i];
        Arrays.sort(sortedCoins, (a, b) -> b - a);

        System.out.println("\nGreedy Coin Change");
        System.out.println("==================");
        System.out.println("Target Amount : " + amount);
        System.out.print("Denominations : ");
        for (int c : sortedCoins) System.out.print(c + " ");
        System.out.println("\n\nCoins selected:");

        int totalCoins = 0;
        int remaining = amount;

        for (int coin : sortedCoins) {
            if (remaining <= 0) break;
            if (coin <= remaining) {
                int count = remaining / coin;
                remaining  = remaining % coin;
                totalCoins += count;
                System.out.println("  Coin " + coin + "  x " + count);
            }
        }

        if (remaining == 0) {
            System.out.println("\nTotal coins used : " + totalCoins);
        } else {
            System.out.println("\nCannot make exact change for " + amount + " with given denominations.");
        }
    }

    public static void main(String[] args) {
        int[] coins = {1, 5, 10, 25, 50};
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the amount to make change for: ");
        int amount = scanner.nextInt();

        if (amount <= 0) {
            System.out.println("Amount must be a positive integer.");
            scanner.close();
            return;
        }

        coinChange(coins, amount);
        scanner.close();
    }
}
