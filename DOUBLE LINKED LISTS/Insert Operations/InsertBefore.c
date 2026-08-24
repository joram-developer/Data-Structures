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
// INSERT BEFORE A TARGET
// ==============================

struct Node *insertBefore(struct Node *head, int target, int data) {

    /*
        ⭐ THE BIG DIFFERENCE FROM THE SLL VERSION:

        In the SLL insertBefore we needed TWO walking pointers:

            previous → trails behind current

        because once you land on the target, an SLL node has no
        way to tell you what came before it.

        In the DLL the target node CARRIES its own predecessor:
        current->prev IS the "previous" pointer. So we walk with
        ONE pointer and read the predecessor straight from it.
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
        CASE 1: The target is the FIRST node (previous == NULL).

                current
                   ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL

        There is nothing before the target, so this is exactly
        insertAtBeginning. We can detect it with current->prev == NULL
        (equivalent to previous == NULL in the SLL version).

            newNode->next = current;    // [5].next → [10]
            current->prev = newNode;    // [10].prev → [5]
            head = newNode;             // new node is the first now

        Boundary check: newNode->prev is still NULL ✓ rule 1.
    */

    if (current->prev == NULL) {

        newNode->next = current;
        current->prev = newNode;
        head = newNode;

        return head;
    }


    /*
        CASE 2: The target has a node before it.

                previous  current
                    ↓        ↓
        NULL ← [10] ⇄ [20] ⇄ [30] → NULL
               ↑ previous is really current->prev,
                 we just don't need a separate variable for it!

        We want to insert 15 before 20:

        NULL ← [10] ⇄ [15] ⇄ [20] ⇄ [30] → NULL

        ORDER OF OPERATIONS — new node first, then existing nodes:

        STEP 1: newNode points forward to the target.

            newNode->next = current;         // [15].next → [20]

        STEP 2: newNode points backward to the old predecessor.
                This line is why we READ current->prev NOW:
                it's still the original value.

            newNode->prev = current->prev;   // [15].prev → [10]

        STEP 3: the old predecessor points forward to newNode.

            current->prev->next = newNode;   // [10].next → [15]

        STEP 4: the target points backward to newNode.

            current->prev = newNode;         // [20].prev → [15]

        ⚠ Steps 3 and 4 MUST come after steps 1-2, and step 3 must
        run before step 4: step 3 reads current->prev, and step 4
        overwrites it. If you did step 4 first, current->prev would
        already point at newNode and step 3 would corrupt the list.

        Head does NOT change here — we inserted mid-list.
    */

    newNode->next = current;           // STEP 1: forward link from new node
    newNode->prev = current->prev;     // STEP 2: backward link to old predecessor
    current->prev->next = newNode;     // STEP 3: old predecessor looks forward
    current->prev = newNode;           // STEP 4: target looks backward

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
        Insert 15 before 20 (mid-list case).

        Before:   NULL ← [10] ⇄ [20] ⇄ [30]
        After:    NULL ← [10] ⇄ [15] ⇄ [20] ⇄ [30]
    */

    head = insertBefore(head, 20, 15);

    printf("After inserting 15 before 20:");
    traverse(head);              // 10 15 20 30

    /*
        Insert 5 before 10 (FIRST-NODE case — head changes).
    */

    head = insertBefore(head, 10, 5);

    printf("After inserting 5 before 10: ");
    traverse(head);              // 5 10 15 20 30

    /*
        Target that doesn't exist.
    */

    head = insertBefore(head, 99, 50);

    // Free remaining nodes
    struct Node *current = head;
    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}
