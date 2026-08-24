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
// DELETE AT THE BEGINNING
// ==============================

struct Node *deleteAtBeginning(struct Node *head) {

    /*
        CASE 1: The list is empty.

        head
         ↓
        NULL

        There is nothing to delete. Return head unchanged.
    */

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }


    /*
        CASE 2: The list has one or more nodes.

        head
         ↓
        [10] → [20] → [30] → NULL

        We want to remove [10] and make [20] the new head.

        STEP 1: Save the current head in a temporary pointer.

            struct Node *temp = head;

        State:

            temp         head
              ↓            ↓
             [10] → [20] → [30] → NULL

            Wait — both temp and head point to [10].
            We need temp so we can free [10] after we
            move head forward.


        STEP 2: Advance head to the next node.

            head = head->next;

        State:

            temp    head
              ↓       ↓
             [10] → [20] → [30] → NULL

        Now head correctly points to [20].
        But [10] is still sitting in memory — we must free it.


        STEP 3: Free the old head node.

            free(temp);

        State:

            head
              ↓
             [20] → [30] → NULL

        [10] is gone. [20] is the new head.


        Why do we need temp?

        If we did head = head->next first and then tried to
        free the old head, we would have already lost the
        address of [10]. We'd have no way to free it.

        temp preserves that address so free() can use it.
    */

    struct Node *temp = head;      // STEP 1: save the address of [10]
    head = head->next;             // STEP 2: move head forward to [20]
    free(temp);                    // STEP 3: free the old [10] node

    return head;                   // return new head so caller can update their pointer
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

    printf("Original list:             ");
    traverse(head);
    // 10 20 30

    /*
        Delete the first node.

        Before:   10 → 20 → 30
                   ↓
        After:    20 → 30
    */

    head = deleteAtBeginning(head);

    printf("After deleting at start:   ");
    traverse(head);
    // 20 30

    /*
        Delete again — now 20 is the first node.

        Before:   20 → 30
                   ↓
        After:    30
    */

    head = deleteAtBeginning(head);

    printf("After deleting at start:   ");
    traverse(head);
    // 30

    /*
        Delete the last remaining node.

        Before:   30
                   ↓
        After:    (empty list)
    */

    head = deleteAtBeginning(head);

    printf("After deleting at start:   ");
    traverse(head);
    // (blank line — empty list)

    /*
        Try to delete from an empty list.
        Should print a message and do nothing.
    */

    head = deleteAtBeginning(head);

    return 0;
}
