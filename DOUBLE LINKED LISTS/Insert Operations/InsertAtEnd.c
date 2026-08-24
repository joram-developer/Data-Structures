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
// INSERT AT THE END
// ==============================

struct Node *insertAtEnd(struct Node *head, int data) {

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }


    /*
        CASE 1: The list is empty.

            head → NULL

        The new node becomes the head. Its prev and next
        are already NULL, so all boundary rules hold.

            head = newNode;
    */

    if (head == NULL) {
        head = newNode;
        return head;
    }


    /*
        CASE 2: The list has one or more nodes.

                head
                 ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL

        We want:

        NULL ← [10] ⇄ [20] ⇄ [30] ⇄ [40] → NULL

        STEP 1: walk until current is the LAST node.
                The condition is current->next == NULL —
                exactly the same walk as in the SLL version,
                because we are still moving FORWARD.

                while (current->next != NULL)
                    current = current->next;

        STEP 2: wire the new node's TWO pointers first.

            newNode->prev = current;     // [40].prev → [30]

        (newNode->next is already NULL, correct for a last node.)

        STEP 3: wire the old last node forward.

            current->next = newNode;     // [30].next → [40]

        Boundary check afterwards:
            [40]->next == NULL           ✓ (rule 2 preserved)
            [40]->prev == [30]           ✓ (rules 3 & 4 preserved)

        NOTE: we did NOT need to touch newNode->next here because
        createNode already set it to NULL. In the SLL version the
        last node only needed ONE new link ([30].next); in the DLL
        every new connection needs BOTH directions wired, which is
        why steps 2 and 3 come as a pair.
    */

    struct Node *current = head;

    while (current->next != NULL) {   // STEP 1: find the last node
        current = current->next;
    }

    newNode->prev = current;          // STEP 2: new node looks back
    current->next = newNode;          // STEP 3: old tail points forward

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

    printf("Original list:             ");
    traverse(head);              // 10 20 30

    /*
        Insert 40 at the end.

        Before:   NULL ← [10] ⇄ [20] ⇄ [30]
        After:    NULL ← [10] ⇄ [20] ⇄ [30] ⇄ [40]
    */

    head = insertAtEnd(head, 40);

    printf("After inserting 40 at end: ");
    traverse(head);              // 10 20 30 40

    /*
        Insert into an EMPTY list — exercises CASE 1.
    */

    head = NULL;
    head = insertAtEnd(head, 99);

    printf("Empty-list insert result:  ");
    traverse(head);              // 99

    // Free remaining nodes
    struct Node *current = head;
    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}
