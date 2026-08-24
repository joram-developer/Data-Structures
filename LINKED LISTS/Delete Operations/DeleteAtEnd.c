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
// DELETE AT THE END
// ==============================

struct Node *deleteAtEnd(struct Node *head) {

    /*
        ─────────────────────────────────────────
        CASE 1: The list is empty.
        ─────────────────────────────────────────

        head
         ↓
        NULL

        Nothing to delete. Return head unchanged.
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
        [10] → NULL

        That single node IS the last node.
        After deleting it, the list is empty.

        We cannot use the two-pointer walk below because
        there is no "previous" node — head itself is the
        last node.

        Steps:
            free(head);
            head = NULL;
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
        [10] → [20] → [30] → NULL

        We want to remove [30] and make [20] point to NULL.

        This requires TWO pointers, just like insertBefore:

            previous → trails one step behind current
            current  → the node we are examining

        We walk until current->next == NULL, meaning
        current has reached the last node.

        At that point:
            previous → [20]   (the second-to-last node)
            current  → [30]   (the last node — to be deleted)


        STEP 1: Walk to the last node.

            while (current->next != NULL) {
                previous = current;
                current = current->next;
            }

        State when the loop ends:

            previous    current
               ↓           ↓
              [20]   →   [30] → NULL


        STEP 2: Disconnect [30] by making previous point to NULL.

            previous->next = NULL;

        State:

            previous
               ↓
              [20] → NULL        [30] → NULL  (still in memory!)


        STEP 3: Free the last node.

            free(current);

        State:

            [10] → [20] → NULL     ← [30] is gone
    */

    struct Node *current = head;
    struct Node *previous = NULL;

    // Walk until current is the last node
    while (current->next != NULL) {
        previous = current;
        current = current->next;
    }

    // current is now the last node, previous is the one before it
    previous->next = NULL;   // STEP 2: cut the link to the last node
    free(current);           // STEP 3: release the last node's memory

    return head;
}


// ==============================
// MAIN
// ==============================

int main() {

    /*
        Build the list:  10 → 20 → 30 → NULL
    */

    struct Node *head = createNode(10);
    head->next = createNode(20);
    head->next->next = createNode(30);

    printf("Original list:           ");
    traverse(head);
    // 10 20 30

    /*
        Delete the last node.

        Before:   10 → 20 → 30
                              ↓
        After:    10 → 20
    */

    head = deleteAtEnd(head);

    printf("After deleting at end:   ");
    traverse(head);
    // 10 20

    /*
        Delete again — now 20 is the last node.

        Before:   10 → 20
                         ↓
        After:    10
    */

    head = deleteAtEnd(head);

    printf("After deleting at end:   ");
    traverse(head);
    // 10

    /*
        Delete the last remaining node.
        This triggers CASE 2 (single node).

        Before:   10
                   ↓
        After:    (empty list)
    */

    head = deleteAtEnd(head);

    printf("After deleting at end:   ");
    traverse(head);
    // (blank line — empty list)

    /*
        Try to delete from an empty list.
        Should print a message and do nothing.
    */

    head = deleteAtEnd(head);

    return 0;
}
