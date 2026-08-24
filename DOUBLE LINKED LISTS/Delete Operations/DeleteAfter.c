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
// DELETE AFTER A TARGET
// ==============================

struct Node *deleteAfter(struct Node *head, int target) {

    /*
        GOAL: Find the node containing 'target', then
        remove the node that comes immediately after it.

        Same idea as the SLL version: one pointer finds the
        target, and the node after it is reached via ->next.
        The DLL difference shows up at the END, where the
        connection must be cut in BOTH directions.
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

            head
             ↓
        NULL ← [10] ⇄ [20] ⇄ [30] ⇄ [40] → NULL

        Looking for target = 20.
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
               ... [40] → NULL

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
        NULL ← [20] ⇄ [30] ⇄ [40] → NULL

        We want to delete [30].

        STEP 1: Save the address of the node to delete.

            struct Node *toDelete = current->next;

                current    toDelete
                   ↓           ↓
        NULL ← [20] ⇄  [30]  ⇄ [40] → NULL


        STEP 2: Reconnect the FORWARD direction — make [20]
        point straight at [40], skipping [30].

            current->next = toDelete->next;

                current    toDelete
                   ↓           ↓
        NULL ← [20] ⇄  [30]  ⇄ [40] → NULL
                        ↑__________|
                  (toDelete->next copied into current->next)

        ⚠ Read toDelete->next BEFORE freeing! After free(),
          reading from toDelete is undefined behaviour.


        STEP 3: THE NEW DLL LINE — reconnect the BACKWARD
        direction. In the SLL version there was nothing like this,
        because SLL nodes have no prev. Here, if [40] exists,
        its prev must stop pointing at the doomed [30]:

            IF toDelete->next != NULL:
                toDelete->next->prev = current;

                    [40].prev → [20]

        ⚠ THE GUARD: if we deleted the LAST node ([30] with no
        successor), then toDelete->next is NULL and there is no
        node whose prev needs fixing. Skipping this check crashes
        on exactly that case.


        Boundary check afterwards:
            [20]->next == [40] ✓ and [40]->prev == [20] ✓
            Rules 3 & 4 hold across the splice in both directions.


        STEP 4: Free the deleted node.

            free(toDelete);

        State:

              current
                 ↓
        NULL ← [20] ⇄ [40] → NULL     ← [30] is gone

        Head never changes — we delete strictly AFTER an
        existing node, so head always survives.

        The key pattern:

            struct Node *toDelete = current->next;   // save
            current->next = toDelete->next;          // forward cut
            IF survivor exists: survivor->prev = current;  // backward cut
            free(toDelete);                          // release
    */

    struct Node *toDelete = current->next;       // STEP 1: save the doomed node
    current->next = toDelete->next;              // STEP 2: forward cut

    if (toDelete->next != NULL) {                // STEP 3: guarded backward cut
        toDelete->next->prev = current;          //         survivor looks back
    }

    free(toDelete);                              // STEP 4: release memory

    return head;
}


// ==============================
// MAIN
// ==============================

int main() {

    /*
        Build:  NULL ← [10] ⇄ [20] ⇄ [30] ⇄ [40] → NULL
    */

    struct Node *n1 = createNode(10);
    struct Node *n2 = createNode(20);
    struct Node *n3 = createNode(30);
    struct Node *n4 = createNode(40);

    n1->next = n2;
    n2->prev = n1;
    n2->next = n3;
    n3->prev = n2;
    n3->next = n4;
    n4->prev = n3;

    struct Node *head = n1;

    printf("Original list:                  ");
    traverse(head);              // 10 20 30 40

    /*
        Delete the node after 20 (which is 30) — middle case,
        both cuts fire.

        Before:   NULL ← [10] ⇄ [20] ⇄ [30] ⇄ [40]
        After:    NULL ← [10] ⇄ [20] ⇄ [40]
    */

    head = deleteAfter(head, 20);

    printf("After deleting after 20:        ");
    traverse(head);              // 10 20 40

    /*
        Delete the node after 10 (which is now 20).
    */

    head = deleteAfter(head, 10);

    printf("After deleting after 10:        ");
    traverse(head);              // 10 40

    /*
        Try to delete after 40 — last node, nothing after it.
        Exercises CASE 3 / the guard's negative path.
    */

    head = deleteAfter(head, 40);

    printf("After deleting after 40:        ");
    traverse(head);              // 10 40 (unchanged)

    /*
        Try to delete after a value that doesn't exist.
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
