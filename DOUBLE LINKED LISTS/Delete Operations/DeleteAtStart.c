#include <stdio.h>
#include <stdlib.h>


// ==============================
// NODE STRUCTURE + HELPERS
// ==============================

struct Node {
    int data;
    struct Node *prev;
    struct Node *next;
};

struct Node *createNode(int data) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) return NULL;
    newNode->data = data;
    newNode->prev = NULL;
    newNode->next = NULL;
    return newNode;
}

void traverse(struct Node *head) {
    struct Node *current = head;
    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;
    }
    printf("\n");
}


// ==============================
// DELETE AT THE BEGINNING
// ==============================

struct Node *deleteAtBeginning(struct Node *head) {

    /*
        ─────────────────────────────────────────
        CASE 1: The list is empty.
        ─────────────────────────────────────────

            head → NULL

        Nothing to delete. Return head unchanged.
    */

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }


    /*
        ─────────────────────────────────────────
        CASE 2: The list has one or more nodes.
        ─────────────────────────────────────────

                head
                 ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL

        We want to remove [10] and make [20] the new head.

        STEP 1: Save the old head so we can free it later.

            struct Node *temp = head;

                temp,head
                    ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL


        STEP 2: Move head forward.

            head = head->next;

                 temp     head
                   ↓        ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL

        ⚠ NEW DLL STEP — the one the SLL version didn't have:
        [20].prev still points at the deleted [10].
        If we leave it that way, rule 1 breaks and anyone who walks
        backwards from head would land on freed memory.


        STEP 3: Clear the new head's prev — but ONLY if a new
        head exists!

            if (head != NULL)
                head->prev = NULL;

            Why the guard? If the list had exactly ONE node,
            then after step 2, head is NULL — and writing
            head->prev would crash. This guard is the DLL's
            extra edge case compared to delete-at-start in an SLL.


        STEP 4: Free the old node (C only).

            free(temp);

        State:

                  head
                   ↓
        NULL ← [20] ⇄ [30] → NULL

        Boundary check: head->prev == NULL ✓ (rule 1 restored).
    */

    struct Node *temp = head;      // STEP 1: save the address of [10]
    head = head->next;             // STEP 2: move head forward to [20]

    if (head != NULL) {            // STEP 3: clear prev — guarded!
        head->prev = NULL;
    }

    free(temp);                    // STEP 4: release the old [10]

    return head;
}


// ==============================
// MAIN
// ==============================

int main() {

    /*
        Build:  NULL ← [10] ⇄ [20] ⇄ [30] → NULL
    */

    struct Node *n1 = createNode(10);
    struct Node *n2 = createNode(20);
    struct Node *n3 = createNode(30);

    n1->next = n2;
    n2->prev = n1;
    n2->next = n3;
    n3->prev = n2;

    struct Node *head = n1;

    printf("Original list:           ");
    traverse(head);              // 10 20 30

    /*
        Delete the first node.

        Before:   NULL ← [10] ⇄ [20] ⇄ [30]
        After:    NULL ← [20] ⇄ [30]
    */

    head = deleteAtBeginning(head);

    printf("After deleting at start: ");
    traverse(head);              // 20 30

    /*
        Delete again.
    */

    head = deleteAtBeginning(head);

    printf("After deleting at start: ");
    traverse(head);              // 30

    /*
        Delete the last remaining node.
        Exercises the `if (head != NULL)` guard — after this
        deletion head is NULL and there is no prev to clear.
    */

    head = deleteAtBeginning(head);

    printf("After deleting at start: ");
    traverse(head);              // (blank line — empty list)

    /*
        Try to delete from an empty list.
        Should print a message and do nothing.
    */

    head = deleteAtBeginning(head);

    return 0;
}
