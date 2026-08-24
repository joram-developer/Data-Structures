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
// DELETE AT THE END
// ==============================

struct Node *deleteAtEnd(struct Node *head) {

    /*
        ─────────────────────────────────────────
        CASE 1: The list is empty.
        ─────────────────────────────────────────
    */

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }


    /*
        ─────────────────────────────────────────
        CASE 2: The list has exactly ONE node.
        ─────────────────────────────────────────

            head
             ↓
        NULL ← [10] → NULL

        head IS the last node. Free it and return NULL.

        In C:   free(head); head = NULL; return head;
        Java:   just set head = null — GC does the rest.
    */

    if (head->next == NULL) {
        free(head);
        head = NULL;
        return head;
    }


    /*
        ─────────────────────────────────────────
        CASE 3: The list has TWO or more nodes.
        ─────────────────────────────────────────

                head
                 ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL

        We want to remove [30] and make [20] point to NULL.


        ⭐ THE BIG DIFFERENCE FROM THE SLL VERSION:

        In the SLL we dragged TWO pointers through the walk:

            previous → trails one step behind current

        because once current reached the last node, there was no
        way back to [20].

        In the DLL, [30] KNOWS its own predecessor:
        current->prev IS "previous". One pointer is enough.


        STEP 1: Walk until current is the last node.

            while (current->next != NULL)
                current = current->next;

                    head    current
                     ↓         ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL


        STEP 2: Cut the link from BOTH sides.

            SLL needed only:
                previous->next = NULL;

            DLL needs the connection cut in both directions:

                current->prev->next = NULL;   // [20].next → NULL  (forward cut)
                // [30].prev still points at [20], but that's fine —
                // we are about to free [30] entirely, so its stale
                // prev pointer disappears with it. Only the SURVIVING
                // node's pointers must be corrected.

            Boundary check: [20]->next == NULL ✓ (rule 2 restored).


        STEP 3: Free the last node (C only).

            free(current);

        State:

                head
                 ↓
        NULL ← [10] ⇄ [20] → NULL     ← [30] is gone

        ⚠ ORDER WARNING: read current->prev BEFORE free(current).
        After free(), reading anything from current is undefined
        behaviour. Here we do it inside step 2's line, before the
        free — correct by construction.
    */

    struct Node *current = head;

    while (current->next != NULL) {      // STEP 1: walk to the last node
        current = current->next;
    }

    current->prev->next = NULL;          // STEP 2: forward cut (surviving side)
    free(current);                       // STEP 3: release the last node

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
        Delete the last node.

        Before:   NULL ← [10] ⇄ [20] ⇄ [30]
        After:    NULL ← [10] ⇄ [20]
    */

    head = deleteAtEnd(head);

    printf("After deleting at end:   ");
    traverse(head);              // 10 20

    /*
        Delete again — now 20 is the last node.
    */

    head = deleteAtEnd(head);

    printf("After deleting at end:   ");
    traverse(head);              // 10

    /*
        Delete the final remaining node — exercises CASE 2.
    */

    head = deleteAtEnd(head);

    printf("After deleting at end:   ");
    traverse(head);              // (blank line — empty list)

    /*
        Try to delete from an empty list.
        Should print a message and do nothing.
    */

    head = deleteAtEnd(head);

    return 0;
}
