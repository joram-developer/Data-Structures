#include <stdio.h>
#include <stdlib.h>


// ==============================
// NODE STRUCTURE
// ==============================

/*
    Compare with the single linked list node:

        SLL:  struct Node { int data; struct Node *next; };
        DLL:  the SAME, plus one extra pointer.

    Every connection between two nodes is now stored
    TWICE — once in each node. That is the whole idea.
*/

struct Node {
    int data;
    struct Node *prev;   // NEW: arrow to the node before this one
    struct Node *next;
};


// ==============================
// CREATE A NEW NODE
// ==============================

struct Node *createNode(int data) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) return NULL;
    newNode->data = data;
    newNode->prev = NULL;   // both pointers start empty
    newNode->next = NULL;
    return newNode;
}


// ==============================
// TRAVERSE THE LIST (FORWARD)
// ==============================

void traverse(struct Node *head) {
    struct Node *current = head;
    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;   // same as SLL
    }
    printf("\n");
}


// ==============================
// TRAVERSE THE LIST (BACKWARD)  ← impossible in an SLL!
// ==============================

/*
    This is the payoff of the prev pointer.

    STEP 1: walk forward until current is the LAST node
            (current->next == NULL).

    STEP 2: follow ->prev backwards, printing as we go,
            until we pass the first node (its prev is NULL).
*/

void traverseBackward(struct Node *head) {

    if (head == NULL) {          // empty list — nothing to print
        printf("\n");
        return;
    }

    struct Node *current = head;

    // STEP 1: walk to the last node
    while (current->next != NULL) {
        current = current->next;
    }

    // STEP 2: walk back using prev
    while (current != NULL) {
        printf("%d ", current->data);
        current = current->prev;
    }

    printf("\n");
}


// ==============================
// INSERT AT THE BEGINNING
// ==============================

struct Node *insertAtBeginning(struct Node *head, int data) {

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }


    /*
        CASE 1: The list is empty.

            head → NULL

        The new node becomes the head. Its prev and next
        are already NULL (set by createNode), so both
        boundary rules hold automatically:

            head->prev == NULL ✓
            head->next == NULL ✓

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

        NULL ← [5] ⇄ [10] ⇄ [20] ⇄ [30] → NULL

        ORDER OF OPERATIONS MATTERS HERE.
        Connect the new node FIRST, then touch existing nodes.

        STEP 1: point the new node at the old head.

            newNode->next = head;

            newNode.next → [10]     (new node wired forward)

        STEP 2: make the old head's prev point BACK at the new node.

            head->prev = newNode;

            [10].prev → [5]         (back-link created)

        STEP 3: move head.

            head = newNode;

        Why this order? If we had done head->prev = newNode
        BEFORE newNode->next = head... actually that specific swap
        would still work here, but building the habit of "wire the
        new node completely, then rewire the list" protects you in
        every other operation where order genuinely matters.

        Boundary check afterwards:
            head->prev == NULL      ✓ (rule 1 preserved)
            [10]->prev == newNode   ✓ (rules 3 & 4 preserved)
    */

    newNode->next = head;   // STEP 1: new node points forward to old head
    head->prev = newNode;   // STEP 2: old head points backward to new node
    head = newNode;         // STEP 3: new node IS the head now

    return head;
}


// ==============================
// MAIN
// ==============================

int main() {

    /*
        Build the initial list:  NULL ← [10] ⇄ [20] ⇄ [30] → NULL
    */

    struct Node *n1 = createNode(10);
    struct Node *n2 = createNode(20);
    struct Node *n3 = createNode(30);

    n1->next = n2;
    n2->prev = n1;
    n2->next = n3;
    n3->prev = n2;

    struct Node *head = n1;

    printf("Original list (forward):   ");
    traverse(head);              // 10 20 30

    /*
        Insert 5 at the beginning.

        Before:   NULL ← [10] ⇄ [20] ⇄ [30]
        After:    NULL ← [5] ⇄ [10] ⇄ [20] ⇄ [30]
    */

    head = insertAtBeginning(head, 5);

    printf("After inserting 5 at start:");
    traverse(head);              // 5 10 20 30

    /*
        Now prove the BACKWARD links really exist:
        walk the same list from tail to head.
    */

    printf("Same list (backward):      ");
    traverseBackward(head);      // 30 20 10 5

    /*
        Free all nodes
    */

    struct Node *current = head;
    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}
