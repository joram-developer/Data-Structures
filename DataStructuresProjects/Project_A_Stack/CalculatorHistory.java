/**
 * ============================================================
 *  Project A  -  Calculator Operation History
 *  Data Structure : Stack (Array-based, LIFO)
 *  Language       : Java
 *
 *  Java equivalent of calculator_history.c
 *
 *  Stack operations implemented:
 *    push()    - add a new operation to the top of the history
 *    pop()     - remove the most-recent operation (Undo)
 *    peek()    - view the most-recent operation without removing it
 *    isEmpty() - check whether the history stack is empty
 *    isFull()  - check whether the history stack is full
 *    display() - print the entire history from top to bottom
 * ============================================================
 */

import java.util.Scanner;

public class CalculatorHistory {

    /* ----------------------------------------------------------
     *  CONSTANTS
     * ---------------------------------------------------------- */
    static final int MAX_HISTORY = 10;   /* maximum number of operations the stack holds */
    static final int MAX_ENTRY    = 64;  /* maximum length of one "operand op operand = result" string */

    /* ----------------------------------------------------------
     *  STACK DEFINITION
     *
     *  C used:  char entries[MAX_HISTORY][MAX_ENTRY]; int top;
     *  Java:    String[] entries; int top;
     *
     *  'top' is an integer index:
     *    -1  -> stack is empty (no entries yet)
     *     0  -> one entry stored at index 0
     *     n  -> (n+1) entries stored; newest entry is at index n
     * ---------------------------------------------------------- */
    static class Stack {
        String[] entries;   /* array of operation strings      */
        int top;             /* index of topmost entry (-1 when empty) */

        Stack() {
            entries = new String[MAX_HISTORY];
            top = -1;        /* stack starts empty */
        }
    }

    /* ----------------------------------------------------------
     *  STACK OPERATIONS
     * ---------------------------------------------------------- */

    /*
     * isEmpty()
     *   Returns true if the stack has no entries, false otherwise.
     *   Condition: top == -1 means nothing has been pushed yet.
     */
    static boolean isEmpty(Stack s) {
        return s.top == -1;
    }

    /*
     * isFull()
     *   Returns true if the stack has reached MAX_HISTORY entries.
     *   Condition: top == MAX_HISTORY - 1 means every array slot is occupied.
     */
    static boolean isFull(Stack s) {
        return s.top == MAX_HISTORY - 1;
    }

    /*
     * push()
     *   Adds a new operation string to the top of the stack.
     *
     *   How the array index moves:
     *     1. Check if full  -> print warning and return.
     *     2. Increment top by 1  (top goes from n to n+1).
     *     3. Store the operation string at entries[top].
     */
    static void push(Stack s, String operation) {
        if (isFull(s)) {
            System.out.println("\n  [!] History is full (max " + MAX_HISTORY + " operations).");
            System.out.println("      Please undo an operation before adding a new one.");
            return;
        }
        s.top++;                          /* move top pointer up   */
        s.entries[s.top] = operation;     /* store the string      */
        System.out.println("\n  [✓] Operation added to history.");
    }

    /*
     * pop()
     *   Removes and returns the topmost (most-recent) operation string.
     *
     *   How the array index moves:
     *     1. Check if empty -> print warning and return null.
     *     2. Save the string at entries[top] so we can return it.
     *     3. Decrement top by 1  (top goes from n to n-1).
     */
    static String pop(Stack s) {
        if (isEmpty(s)) {
            System.out.println("\n  [!] History is empty. Nothing to undo.");
            return null;
        }
        String removed = s.entries[s.top];   /* capture before moving top   */
        s.entries[s.top] = null;              /* clear reference (helps GC) */
        s.top--;                              /* move top pointer down       */
        return removed;
    }

    /*
     * peek()
     *   Returns the topmost operation string WITHOUT removing it.
     *   top does not move at all.
     */
    static String peek(Stack s) {
        if (isEmpty(s)) {
            return null;
        }
        return s.entries[s.top];
    }

    /*
     * display()
     *   Prints all operations from top (most recent) down to index 0 (oldest).
     *   Iterates from top -> 0 so the user always sees the newest entry first,
     *   which visually reinforces the LIFO concept.
     */
    static void display(Stack s) {
        if (isEmpty(s)) {
            System.out.println("\n  [!] History is empty. No operations to display.");
            return;
        }

        System.out.println("\n  ----------------------------------------");
        System.out.println("   OPERATION HISTORY  (newest -> oldest)");
        System.out.println("  ----------------------------------------");
        System.out.println("   TOP");
        System.out.println("    |");

        for (int i = s.top; i >= 0; i--) {
            System.out.println("   [" + i + "]  " + s.entries[i]);
        }

        System.out.println("  ----------------------------------------");
        System.out.println("   " + (s.top + 1) + " operation(s) in history.");
    }

    /* ----------------------------------------------------------
     *  HELPER  -  perform the arithmetic and build the string
     * ---------------------------------------------------------- */

    /*
     * performCalculation()
     *   Reads two numbers and an operator from the user, computes
     *   the result, and returns the formatted operation string.
     *   Returns null on invalid operator or divide-by-zero.
     */
    static String performCalculation(Scanner sc) {
        System.out.print("\n  Enter first number  : ");
        double num1;
        try {
            num1 = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid number.");
            return null;
        }

        System.out.print("  Enter operator (+, -, *, /) : ");
        String opStr = sc.nextLine().trim();
        if (opStr.length() != 1) {
            System.out.println("\n  [!] Unknown operator. Use +, -, *, /");
            return null;
        }
        char op = opStr.charAt(0);

        System.out.print("  Enter second number : ");
        double num2;
        try {
            num2 = Double.parseDouble(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid number.");
            return null;
        }

        /* compute result based on operator */
        double result;
        switch (op) {
            case '+': result = num1 + num2; break;
            case '-': result = num1 - num2; break;
            case '*': result = num1 * num2; break;
            case '/':
                if (num2 == 0) {
                    System.out.println("\n  [!] Error: Division by zero is undefined.");
                    return null;
                }
                result = num1 / num2;
                break;
            default:
                System.out.println("\n  [!] Unknown operator '" + op + "'. Use +, -, *, /");
                return null;
        }

        /* format: print without unnecessary decimal places.
         * If the result and both operands are whole numbers show them as
         * integers, otherwise show up to two decimal places. */
        String resultStr;
        if (result == (long) result && num1 == (long) num1 && num2 == (long) num2) {
            resultStr = String.format("%.0f %c %.0f = %.0f", num1, op, num2, result);
        } else {
            resultStr = String.format("%.2f %c %.2f = %.2f", num1, op, num2, result);
        }

        System.out.println("\n  Result: " + resultStr);
        return resultStr;
    }

    /* ----------------------------------------------------------
     *  MENU  -  print the main menu banner
     * ---------------------------------------------------------- */
    static void printMenu() {
        System.out.println();
        System.out.println("  ================================");
        System.out.println("        CALCULATOR HISTORY        ");
        System.out.println("  ================================");
        System.out.println("  1. Perform Calculation");
        System.out.println("  2. Undo Last Operation");
        System.out.println("  3. View Last Operation");
        System.out.println("  4. Display Operation History");
        System.out.println("  5. Check if History is Empty");
        System.out.println("  6. Check if History is Full");
        System.out.println("  7. Exit");
        System.out.println("  ================================");
        System.out.print("  Enter choice: ");
    }

    /* ----------------------------------------------------------
     *  MAIN
     * ---------------------------------------------------------- */
    public static void main(String[] args) {
        Stack history = new Stack();   /* initialise: stack starts empty */
        Scanner sc = new Scanner(System.in);

        System.out.println("\n  Welcome to Calculator History (Stack Demo)");
        System.out.println("  Max history size: " + MAX_HISTORY + " operations");

        while (true) {
            printMenu();

            String line = sc.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("\n  [!] Please enter a number between 1 and 7.");
                continue;
            }

            switch (choice) {

                /* ---- 1. Perform Calculation --------------------------------- */
                case 1: {
                    String operation = performCalculation(sc);
                    if (operation != null) {
                        push(history, operation);
                    }
                    break;
                }

                /* ---- 2. Undo Last Operation --------------------------------- */
                case 2: {
                    String removed = pop(history);
                    if (removed != null) {
                        System.out.println("\n  Undoing: " + removed);
                        System.out.println("  Operation removed from history.");
                    }
                    break;
                }

                /* ---- 3. View Last Operation (peek) -------------------------- */
                case 3: {
                    String topOp = peek(history);
                    if (topOp != null) {
                        System.out.println("\n  Last operation: " + topOp);
                    } else {
                        System.out.println("\n  [!] History is empty. Nothing to view.");
                    }
                    break;
                }

                /* ---- 4. Display Full History -------------------------------- */
                case 4:
                    display(history);
                    break;

                /* ---- 5. Check if Empty ------------------------------------- */
                case 5:
                    if (isEmpty(history)) {
                        System.out.println("\n  History is EMPTY.");
                    } else {
                        System.out.println("\n  History is NOT empty ("
                                + (history.top + 1) + " operation(s) stored).");
                    }
                    break;

                /* ---- 6. Check if Full -------------------------------------- */
                case 6:
                    if (isFull(history)) {
                        System.out.println("\n  History is FULL ("
                                + MAX_HISTORY + "/" + MAX_HISTORY + " slots used).");
                    } else {
                        System.out.println("\n  History is NOT full ("
                                + (history.top + 1) + "/" + MAX_HISTORY + " slots used).");
                    }
                    break;

                /* ---- 7. Exit ----------------------------------------------- */
                case 7:
                    System.out.println("\n  Goodbye!\n");
                    sc.close();
                    return;

                default:
                    System.out.println("\n  [!] Invalid choice. Enter a number between 1 and 7.");
            }
        }
    }
}
