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

void traverseBackward(struct Node *head) {
    if (head == NULL) { printf("\n"); return; }
    struct Node *current = head;
    while (current->next != NULL) current = current->next;
    while (current != NULL) { printf("%d ", current->data); current = current->prev; }
    printf("\n");
}


// ==============================
// 1. INSERT AT THE BEGINNING
// ==============================

/*
    Core pointer idea:

        newNode->next = head;      // new node looks forward at old head
        head->prev   = newNode;    // old head looks back at new node
        head         = newNode;    // new node becomes the first

    Empty list: just head = newNode (prev/next already NULL).
*/

struct Node *insertAtBeginning(struct Node *head, int data) {

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }

    if (head == NULL) {            // empty list — new node is everything
        head = newNode;
        return head;
    }

    newNode->next = head;          // wire the NEW node first...
    head->prev = newNode;          // ...then the OLD node
    head = newNode;

    return head;
}


// ==============================
// 2. INSERT AT THE END
// ==============================

/*
    Core pointer idea:

        newNode->prev = last;      // new node looks back at old tail
        last->next    = newNode;   // old tail points forward to new node

    Walk with current until current->next == NULL, same as SLL.
*/

struct Node *insertAtEnd(struct Node *head, int data) {

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }

    if (head == NULL) {            // empty list
        head = newNode;
        return head;
    }

    struct Node *current = head;

    while (current->next != NULL) {   // find the last node
        current = current->next;
    }

    newNode->prev = current;       // new node looks back FIRST
    current->next = newNode;       // then old tail points forward

    return head;
}


// ==============================
// 3. INSERT BEFORE A TARGET
// ==============================

/*
    Core pointer ideas:

    First-node case (current->prev == NULL):
        newNode->next = current;
        current->prev = newNode;
        head = newNode;

    Mid-list case — FOUR updates, new node wired first:
        newNode->next       = current;        // forward to target
        newNode->prev       = current->prev;  // backward to old predecessor
        current->prev->next = newNode;        // old predecessor → new node
        current->prev       = newNode;        // target → back to new node

    ⚠ Steps 3-4 read current->prev, so run them AFTER steps 1-2,
      and step 3 before step 4.

    No separate `previous` walker needed — current->prev IS it.
*/

struct Node *insertBefore(struct Node *head, int target, int data) {

    struct Node *current = head;

    while (current != NULL && current->data != target) {
        current = current->next;
    }

    if (current == NULL) {
        printf("Target %d was not found.\n", target);
        return head;
    }

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }

    if (current->prev == NULL) {           // target is the first node
        newNode->next = current;
        current->prev = newNode;
        head = newNode;
        return head;
    }

    newNode->next = current;               // STEP 1: forward link
    newNode->prev = current->prev;         // STEP 2: backward link
    current->prev->next = newNode;         // STEP 3: predecessor rewires forward
    current->prev = newNode;               // STEP 4: target rewires backward

    return head;
}


// ==============================
// 4. INSERT AFTER A TARGET
// ==============================

/*
    Core pointer ideas — FOUR updates, new node wired first:

        newNode->next       = current->next;  // forward to old successor
        newNode->prev       = current;        // backward to target
        IF successor exists:                  ⚠ guard against last node!
            current->next->prev = newNode;    // successor looks back
        current->next       = newNode;        // target points to new node

    ⚠ Step "IF" must run BEFORE the last line: it reads current->next.
*/

struct Node *insertAfter(struct Node *head, int target, int data) {

    struct Node *current = head;

    while (current != NULL && current->data != target) {
        current = current->next;
    }

    if (current == NULL) {
        printf("Target %d was not found.\n", target);
        return head;
    }

    struct Node *newNode = createNode(data);
    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }

    newNode->next = current->next;             // STEP 1: forward from new node
    newNode->prev = current;                   // STEP 2: backward from new node

    if (current->next != NULL) {               // STEP 3: guarded!
        current->next->prev = newNode;         //         successor looks back
    }

    current->next = newNode;                   // STEP 4: target points forward

    return head;
}


// ==============================
// MAIN — all four operations in sequence
// ==============================

int main() {

    /*
        Build the starting list manually:

            NULL ← [10] ⇄ [20] ⇄ [30] → NULL
    */

    struct Node *head = createNode(10);
    struct Node *node2 = createNode(20);
    struct Node *node3 = createNode(30);

    head->next = node2;
    node2->prev = head;
    node2->next = node3;
    node3->prev = node2;

    printf("Original list (forward):     ");
    traverse(head);              // 10 20 30


    // ──────────────────────────────────────────────────
    // 1. INSERT AT BEGINNING — insert 5
    //    NULL ← [5] ⇄ [10] ⇄ [20] ⇄ [30]
    // ──────────────────────────────────────────────────

    head = insertAtBeginning(head, 5);

    printf("After insertAtBeginning(5):  ");
    traverse(head);              // 5 10 20 30


    // ──────────────────────────────────────────────────
    // 2. INSERT AT END — insert 40
    //    NULL ← [5] ⇄ [10] ⇄ [20] ⇄ [30] ⇄ [40]
    // ──────────────────────────────────────────────────

    head = insertAtEnd(head, 40);

    printf("After insertAtEnd(40):       ");
    traverse(head);              // 5 10 20 30 40


    // ──────────────────────────────────────────────────
    // 3. INSERT BEFORE 20 — insert 15
    //    NULL ← [5] ⇄ [10] ⇄ [15] ⇄ [20] ⇄ [30] ⇄ [40]
    // ──────────────────────────────────────────────────

    head = insertBefore(head, 20, 15);

    printf("After insertBefore(20, 15):  ");
    traverse(head);              // 5 10 15 20 30 40


    // ──────────────────────────────────────────────────
    // 4. INSERT AFTER 20 — insert 25
    //    NULL ← [5] ⇄ [10] ⇄ [15] ⇄ [20] ⇄ [25] ⇄ [30] ⇄ [40]
    // ──────────────────────────────────────────────────

    head = insertAfter(head, 20, 25);

    printf("After insertAfter(20, 25):   ");
    traverse(head);              // 5 10 15 20 25 30 40


    /*
        Final proof that every link is correct in BOTH directions:
        walk the finished list backwards. If any prev pointer were
        wrong, this would print a wrong or short sequence.
    */

    printf("Final list (backward):       ");
    traverseBackward(head);      // 40 30 25 20 15 10 5


    // Free all nodes
    struct Node *current = head;
    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}
