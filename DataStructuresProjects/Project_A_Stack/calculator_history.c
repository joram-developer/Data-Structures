/*
 * ============================================================
 *  Project A  –  Calculator Operation History
 *  Data Structure : Stack (Array-based, LIFO)
 *  Language       : C
 *
 *  Stack operations implemented:
 *    push()    – add a new operation to the top of the history
 *    pop()     – remove the most-recent operation (Undo)
 *    peek()    – view the most-recent operation without removing it
 *    isEmpty() – check whether the history stack is empty
 *    isFull()  – check whether the history stack is full
 *    display() – print the entire history from top to bottom
 * ============================================================
 */

#include <stdio.h>
#include <string.h>

/* ----------------------------------------------------------
 *  CONSTANTS
 * ---------------------------------------------------------- */
#define MAX_HISTORY 10          /* maximum number of operations the stack holds  */
#define MAX_ENTRY   64          /* maximum length of one "operand op operand = result" string */

/* ----------------------------------------------------------
 *  STACK DEFINITION
 *
 *  The stack is an array of strings.
 *  'top' is an integer index:
 *    -1  → stack is empty (no entries yet)
 *     0  → one entry stored at index 0
 *     n  → (n+1) entries stored; newest entry is at index n
 *
 *  Visualisation with MAX_HISTORY = 5 after three pushes:
 *
 *   index  entries[]
 *   -----  -------------------
 *     2    "100 - 25 = 75"   ← top  (most recent, will pop first)
 *     1    "20 * 3 = 60"
 *     0    "10 + 5 = 15"     (oldest)
 * ---------------------------------------------------------- */
typedef struct {
    char entries[MAX_HISTORY][MAX_ENTRY];   /* array of operation strings      */
    int  top;                               /* index of the topmost entry (-1 when empty) */
} Stack;

/* ----------------------------------------------------------
 *  STACK OPERATIONS
 * ---------------------------------------------------------- */

/*
 * isEmpty()
 *   Returns 1 (true) if the stack has no entries, 0 (false) otherwise.
 *   Condition: top == -1 means nothing has been pushed yet.
 */
int isEmpty(const Stack *s) {
    return s->top == -1;
}

/*
 * isFull()
 *   Returns 1 (true) if the stack has reached MAX_HISTORY entries.
 *   Condition: top == MAX_HISTORY - 1 means every array slot is occupied.
 */
int isFull(const Stack *s) {
    return s->top == MAX_HISTORY - 1;
}

/*
 * push()
 *   Adds a new operation string to the top of the stack.
 *
 *   How the array index moves:
 *     1. Check if full  → print warning and return.
 *     2. Increment top by 1  (top goes from n to n+1).
 *     3. Copy the operation string into entries[top].
 *
 *   Example: top was 1 → after push, top becomes 2 and
 *            the new string sits at entries[2].
 */
void push(Stack *s, const char *operation) {
    if (isFull(s)) {
        printf("\n  [!] History is full (max %d operations).\n", MAX_HISTORY);
        printf("      Please undo an operation before adding a new one.\n");
        return;
    }
    s->top++;                                      /* move top pointer up   */
    strncpy(s->entries[s->top], operation, MAX_ENTRY - 1);
    s->entries[s->top][MAX_ENTRY - 1] = '\0';      /* ensure null-terminator */
    printf("\n  [✓] Operation added to history.\n");
}

/*
 * pop()
 *   Removes and returns the topmost (most-recent) operation string.
 *
 *   How the array index moves:
 *     1. Check if empty → print warning and return NULL.
 *     2. Save a pointer to entries[top] so we can print it.
 *     3. Decrement top by 1  (top goes from n to n-1).
 *        The string data still exists in memory but is now
 *        "below" the logical top and will be overwritten on
 *        the next push — this is standard array-stack behaviour.
 *
 *   Example: top was 2 → after pop, top becomes 1 and
 *            entries[2] is logically discarded.
 */
const char *pop(Stack *s) {
    if (isEmpty(s)) {
        printf("\n  [!] History is empty. Nothing to undo.\n");
        return NULL;
    }
    const char *removed = s->entries[s->top];  /* capture before moving top   */
    s->top--;                                   /* move top pointer down       */
    return removed;
}

/*
 * peek()
 *   Returns the topmost operation string WITHOUT removing it.
 *   top does not move at all.
 */
const char *peek(const Stack *s) {
    if (isEmpty(s)) {
        return NULL;
    }
    return s->entries[s->top];
}

/*
 * display()
 *   Prints all operations from top (most recent) down to index 0 (oldest).
 *   Iterates from top → 0 so the user always sees the newest entry first,
 *   which visually reinforces the LIFO concept.
 */
void display(const Stack *s) {
    if (isEmpty(s)) {
        printf("\n  [!] History is empty. No operations to display.\n");
        return;
    }

    printf("\n  ----------------------------------------\n");
    printf("   OPERATION HISTORY  (newest → oldest)\n");
    printf("  ----------------------------------------\n");
    printf("   TOP\n");
    printf("    ↓\n");

    for (int i = s->top; i >= 0; i--) {
        printf("   [%d]  %s\n", i, s->entries[i]);
    }

    printf("  ----------------------------------------\n");
    printf("   %d operation(s) in history.\n", s->top + 1);
}

/* ----------------------------------------------------------
 *  HELPER  –  perform the arithmetic and build the string
 * ---------------------------------------------------------- */

/*
 * performCalculation()
 *   Reads two numbers and an operator from the user, computes
 *   the result, and writes a formatted string into 'result_str'.
 *   Returns 1 on success, 0 on invalid operator or divide-by-zero.
 */
int performCalculation(char *result_str) {
    double num1, num2, result;
    char   op;

    printf("\n  Enter first number  : ");
    if (scanf("%lf", &num1) != 1) { /* flush bad input */
        while (getchar() != '\n');
        printf("  [!] Invalid number.\n");
        return 0;
    }

    printf("  Enter operator (+, -, *, /) : ");
    scanf(" %c", &op);   /* leading space skips any leftover whitespace / newline */

    printf("  Enter second number : ");
    if (scanf("%lf", &num2) != 1) {
        while (getchar() != '\n');
        printf("  [!] Invalid number.\n");
        return 0;
    }

    /* compute result based on operator */
    switch (op) {
        case '+': result = num1 + num2; break;
        case '-': result = num1 - num2; break;
        case '*': result = num1 * num2; break;
        case '/':
            if (num2 == 0) {
                printf("\n  [!] Error: Division by zero is undefined.\n");
                return 0;
            }
            result = num1 / num2;
            break;
        default:
            printf("\n  [!] Unknown operator '%c'. Use +, -, *, /\n", op);
            return 0;
    }

    /* format: print without unnecessary decimal places                        *
     * If the result is a whole number show it as an integer, otherwise        *
     * show up to two decimal places.                                           */
    if (result == (long long)result &&
        num1   == (long long)num1   &&
        num2   == (long long)num2) {
        snprintf(result_str, MAX_ENTRY,
                 "%.0f %c %.0f = %.0f",
                 num1, op, num2, result);
    } else {
        snprintf(result_str, MAX_ENTRY,
                 "%.2f %c %.2f = %.2f",
                 num1, op, num2, result);
    }

    printf("\n  Result: %s\n", result_str);
    return 1;
}

/* ----------------------------------------------------------
 *  MENU  –  print the main menu banner
 * ---------------------------------------------------------- */
void printMenu(void) {
    printf("\n");
    printf("  ================================\n");
    printf("        CALCULATOR HISTORY        \n");
    printf("  ================================\n");
    printf("  1. Perform Calculation\n");
    printf("  2. Undo Last Operation\n");
    printf("  3. View Last Operation\n");
    printf("  4. Display Operation History\n");
    printf("  5. Check if History is Empty\n");
    printf("  6. Check if History is Full\n");
    printf("  7. Exit\n");
    printf("  ================================\n");
    printf("  Enter choice: ");
}

/* ----------------------------------------------------------
 *  MAIN
 * ---------------------------------------------------------- */
int main(void) {
    Stack  history;
    history.top = -1;   /* initialise: stack starts empty */

    int  choice;
    char operation[MAX_ENTRY];

    printf("\n  Welcome to Calculator History (Stack Demo)\n");
    printf("  Max history size: %d operations\n", MAX_HISTORY);

    while (1) {
        printMenu();

        if (scanf("%d", &choice) != 1) {
            /* clear non-integer input */
            while (getchar() != '\n');
            printf("\n  [!] Please enter a number between 1 and 7.\n");
            continue;
        }

        switch (choice) {

            /* ---- 1. Perform Calculation --------------------------------- */
            case 1:
                if (performCalculation(operation)) {
                    push(&history, operation);
                }
                break;

            /* ---- 2. Undo Last Operation --------------------------------- */
            case 2: {
                const char *removed = pop(&history);
                if (removed != NULL) {
                    printf("\n  Undoing: %s\n", removed);
                    printf("  Operation removed from history.\n");
                }
                break;
            }

            /* ---- 3. View Last Operation (peek) -------------------------- */
            case 3: {
                const char *top_op = peek(&history);
                if (top_op != NULL) {
                    printf("\n  Last operation: %s\n", top_op);
                } else {
                    printf("\n  [!] History is empty. Nothing to view.\n");
                }
                break;
            }

            /* ---- 4. Display Full History -------------------------------- */
            case 4:
                display(&history);
                break;

            /* ---- 5. Check if Empty ------------------------------------- */
            case 5:
                if (isEmpty(&history)) {
                    printf("\n  History is EMPTY.\n");
                } else {
                    printf("\n  History is NOT empty (%d operation(s) stored).\n",
                           history.top + 1);
                }
                break;

            /* ---- 6. Check if Full -------------------------------------- */
            case 6:
                if (isFull(&history)) {
                    printf("\n  History is FULL (%d/%d slots used).\n",
                           MAX_HISTORY, MAX_HISTORY);
                } else {
                    printf("\n  History is NOT full (%d/%d slots used).\n",
                           history.top + 1, MAX_HISTORY);
                }
                break;

            /* ---- 7. Exit ----------------------------------------------- */
            case 7:
                printf("\n  Goodbye!\n\n");
                return 0;

            default:
                printf("\n  [!] Invalid choice. Enter a number between 1 and 7.\n");
        }
    }
}
