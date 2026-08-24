#include <stdio.h>
#include <stdlib.h>


// ==============================
// NODE STRUCTURE
// ==============================

struct Node {
    int data;
    struct Node *next;
};


// ==============================
// HELPER: CREATE A NEW NODE
// ==============================

struct Node *createNode(int data) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) return NULL;
    newNode->data = data;
    newNode->next = NULL;
    return newNode;
}


// ==============================
// HELPER: TRAVERSE THE LIST
// ==============================

void traverse(struct Node *head) {
    struct Node *current = head;
    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;
    }
    printf("\n");
}


// ==============================
// DELETE AFTER A TARGET
// ==============================

struct Node *deleteAfter(struct Node *head, int target) {

    /*
        GOAL: Find the node containing 'target', then
        remove the node that comes immediately after it.

        Compare this with insertAfter — the idea is similar.
        We find the target using one pointer, and because
        a node knows what comes after it (via .next), we
        already have everything we need once we land on it.

        No second pointer required.
    */


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
        CASE 2: Walk the list to find target.
        ─────────────────────────────────────────
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


    /*
        ─────────────────────────────────────────
        CASE 3: Target found, but nothing after it.
        ─────────────────────────────────────────

        current
           ↓
          [30] → NULL

        There is no node after the target to delete.
    */

    if (current->next == NULL) {
        printf("No node after %d. Nothing to delete.\n", target);
        return head;
    }


    /*
        ─────────────────────────────────────────
        CASE 4: Target found, and there IS a node after it.
        ─────────────────────────────────────────

        current
           ↓
          [20] → [30] → [40] → NULL

        We want to delete [30].

        STEP 1: Save the address of the node to delete.

            struct Node *toDelete = current->next;

        State:

            current    toDelete
               ↓           ↓
              [20]  →  [30]  →  [40] → NULL

        We save it now because we are about to overwrite
        current->next. Once we do that, we have no way
        to reach [30] for freeing.


        STEP 2: Skip over the node to delete.

            current->next = toDelete->next;

        This rewires [20] to point directly to [40],
        bypassing [30].

        State:

            current    toDelete
               ↓           ↓
              [20]  →  [40] → NULL
                        ↑
                    toDelete->next
                    (which we copied into current->next)

        Note: toDelete ([30]) is now unreachable from
        the list, but it still sits in memory.


        STEP 3: Free the deleted node.

            free(toDelete);

        State:

              [20] → [40] → NULL     ← [30] is gone

        The key pattern here:

            struct Node *toDelete = current->next;
            current->next = toDelete->next;
            free(toDelete);

        This is the deletion equivalent of insertAfter's
        two-line pointer update — one pointer to save what
        we're about to lose, one to reconnect the chain.
    */

    struct Node *toDelete = current->next;    // STEP 1: save the node to remove
    current->next = toDelete->next;           // STEP 2: skip over it
    free(toDelete);                           // STEP 3: release its memory

    return head;
}


// ==============================
// MAIN
// ==============================

int main() {

    /*
        Build the list:  10 → 20 → 30 → 40 → NULL
    */

    struct Node *head = createNode(10);
    head->next = createNode(20);
    head->next->next = createNode(30);
    head->next->next->next = createNode(40);

    printf("Original list:                  ");
    traverse(head);
    // 10 20 30 40

    /*
        Delete the node after 20 (which is 30).

        Before:   10 → 20 → 30 → 40
                        ↓
        After:    10 → 20 → 40
    */

    head = deleteAfter(head, 20);

    printf("After deleting after 20:        ");
    traverse(head);
    // 10 20 40

    /*
        Delete the node after 10 (which is now 20).

        Before:   10 → 20 → 40
                   ↓
        After:    10 → 40
    */

    head = deleteAfter(head, 10);

    printf("After deleting after 10:        ");
    traverse(head);
    // 10 40

    /*
        Try to delete after 40 — it is the last node,
        so there is nothing after it.
        Should print a message and leave the list unchanged.
    */

    head = deleteAfter(head, 40);

    printf("After deleting after 40:        ");
    traverse(head);
    // 10 40 (unchanged)

    /*
        Try to delete after a value that doesn't exist.
        Should print a not-found message.
    */

    head = deleteAfter(head, 99);

    // Free remaining nodes
    struct Node *current = head;
    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}
