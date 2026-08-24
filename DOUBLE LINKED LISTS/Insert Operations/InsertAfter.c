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
// INSERT AFTER A TARGET
// ==============================

struct Node *insertAfter(struct Node *head, int target, int data) {

    /*
        Search for the target — same forward walk as the SLL.
        Only ONE pointer needed: once we land on the target,
        current->next already tells us what comes after it.
    */

    struct Node *current = head;

    while (current != NULL && current->data != target) {
        current = current->next;
    }

    // Target was not found
    if (current == NULL) {
        printf("Target %d was not found.\n", target);
        return head;
    }

    // Create the new node
    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }


    /*
        Suppose we have:

                current
                   ↓
        NULL ← [20] ⇄ [30] → NULL

        We want:

        NULL ← [20] ⇄ [25] ⇄ [30] → NULL

        FOUR pointer updates are required (an SLL only needed two!),
        because the [20]-[30] connection exists in BOTH directions
        and we are splicing a node into the middle of it.

        ORDER OF OPERATIONS:
        Wire the NEW node first (its fields are empty and safe
        to overwrite), then fix the existing nodes.

        STEP 1: newNode points forward to [30].

            newNode->next = current->next;

                [25].next → [30]

        STEP 2: newNode points backward to [20].

            newNode->prev = current;

                [25].prev → [20]


        STEP 3: IF there is a node after current ([30] here,
        but this could be NULL if target is the last node!),
        make it point BACK to the new node.

            if (current->next != NULL)
                current->next->prev = newNode;

                [30].prev → [25]

        ⚠ THE CRITICAL GUARD: in the SLL version, inserting after
        the LAST node needed no special handling. In the DLL it DOES —
        because we must also write [30].prev, and if current->next is
        NULL there IS no [30] to update. Skipping this check crashes.

        STEP 4: current points forward to the new node.
                Do this AFTER step 3 — step 3 reads current->next,
                so overwriting it too early loses [30].

            current->next = newNode;

                [20].next → [25]

        Boundary check afterwards:
            [25]->next == [30], [30]->prev == [25]   ✓ rules 3 & 4 up
            [25]->prev == [20], [20]->next == [25]   ✓ rules 3 & 4 down
            If inserted at the very end: [25]->next == NULL ✓ rule 2

        Head does NOT change in any case here — we insert
        strictly after an existing node, so head stays put.
    */

    newNode->next = current->next;          // STEP 1: forward link from new node
    newNode->prev = current;                // STEP 2: backward link from new node

    if (current->next != NULL) {            // STEP 3: guard against last-node case!
        current->next->prev = newNode;      //         old successor looks back
    }

    current->next = newNode;                // STEP 4: target points to new node

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

    printf("Original list:              ");
    traverse(head);              // 10 20 30

    /*
        Insert 25 after 20 (middle case — all four steps fire).

        Before:   NULL ← [10] ⇄ [20] ⇄ [30]
        After:    NULL ← [10] ⇄ [20] ⇄ [25] ⇄ [30]
    */

    head = insertAfter(head, 20, 25);

    printf("After inserting 25 after 20:");
    traverse(head);              // 10 20 25 30

    /*
        Insert 40 after 30 (LAST-NODE case — exercises the guard).
    */

    head = insertAfter(head, 30, 40);

    printf("After inserting 40 after 30:");
    traverse(head);              // 10 20 25 30 40

    /*
        Target that doesn't exist.
    */

    head = insertAfter(head, 99, 50);

    // Free remaining nodes
    struct Node *current = head;
    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}
