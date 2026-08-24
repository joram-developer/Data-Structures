#include <stdio.h>
#include <stdlib.h>


// ==============================
// NODE STRUCTURE + HELPERS
// ==============================

struct Node {
    int data;
    struct Node *next;
};

struct Node *createNode(int data) {
    struct Node *newNode = malloc(sizeof(struct Node));
    if (newNode == NULL) return NULL;
    newNode->data = data;
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


// ======================================================
// 1. DELETE AT THE BEGINNING
// ======================================================

/*
    Core pointer idea:

        temp = head;            // save the old node so we can free it
        head = head->next;      // second node becomes the head
        free(temp);             // release the old first node

    Empty list → nothing to delete.
*/

struct Node *deleteAtBeginning(struct Node *head) {

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }

    struct Node *temp = head;   // save the old head
    head = head->next;          // advance head
    free(temp);                 // release the old head

    return head;
}


// ======================================================
// 2. DELETE AT THE END
// ======================================================

/*
    Core pointer idea (two-pointer walk — SLL has no prev!):

        while (current->next != NULL) {     // stop AT the last node
            previous = current;
            current  = current->next;
        }
        previous->next = NULL;              // cut the link
        free(current);                  // release the old tail

    Cases: empty list / single-node list / two or more nodes.
*/

struct Node *deleteAtEnd(struct Node *head) {

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }

    if (head->next == NULL) {       // single node IS the last node
        free(head);
        head = NULL;
        return head;
    }

    struct Node *current = head;
    struct Node *previous = NULL;

    while (current->next != NULL) { // walk until current is the last
        previous = current;
        current = current->next;
    }

    previous->next = NULL;          // second-to-last now ends the list
    free(current);                  // release the old tail

    return head;
}


// ======================================================
// 3. DELETE AFTER A TARGET
// ======================================================

/*
    Core pointer ideas:

        toDelete = current->next;      // save the doomed node FIRST
        current->next = toDelete->next;// rewire around it
        free(toDelete);                // release it

    Guards: empty list / target not found / target is last node.
*/

struct Node *deleteAfter(struct Node *head, int target) {

    if (head == NULL) {
        printf("List is empty. Nothing to delete.\n");
        return head;
    }

    struct Node *current = head;

    while (current != NULL && current->data != target) {
        current = current->next;
    }

    if (current == NULL) {
        printf("Target %d was not found.\n", target);
        return head;
    }

    if (current->next == NULL) {
        printf("No node after %d. Nothing to delete.\n", target);
        return head;
    }

    struct Node *toDelete = current->next;   // STEP 1: save it
    current->next = toDelete->next;          // STEP 2: skip over it
    free(toDelete);                          // STEP 3: release it

    return head;
}


// ======================================================
// FREE THE ENTIRE LIST
// ======================================================

void freeList(struct Node *head) {
    struct Node *current = head;
    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }
}


// ======================================================
// MAIN — all three operations in sequence
// ======================================================

int main() {

    /*
        Build the starting list:

            10 → 20 → 30 → 40 → 50 → NULL
    */

    struct Node *head = createNode(10);
    head->next = createNode(20);
    head->next->next = createNode(30);
    head->next->next->next = createNode(40);
    head->next->next->next->next = createNode(50);

    printf("Original list:               ");
    traverse(head);              // 10 20 30 40 50


    // ──────────────────────────────────────────────────
    // 1. DELETE AFTER 30 → removes 40
    //    10 → 20 → 30 → 50
    // ──────────────────────────────────────────────────

    head = deleteAfter(head, 30);

    printf("After deleteAfter(30):       ");
    traverse(head);              // 10 20 30 50


    // ──────────────────────────────────────────────────
    // 2. DELETE AT START → removes 10
    //    20 → 30 → 50
    // ──────────────────────────────────────────────────

    head = deleteAtBeginning(head);

    printf("After deleteAtBeginning:     ");
    traverse(head);              // 20 30 50


    // ──────────────────────────────────────────────────
    // 3. DELETE AT END → removes 50
    //    20 → 30
    // ──────────────────────────────────────────────────

    head = deleteAtEnd(head);

    printf("After deleteAtEnd:           ");
    traverse(head);              // 20 30


    // ──────────────────────────────────────────────────
    // EDGE CASES — each should print a message, list unchanged
    // ──────────────────────────────────────────────────

    head = deleteAfter(head, 30);      // nothing after last node
    head = deleteAfter(head, 99);      // target not found

    /*
        Drain the list completely, then prove empty-list handling.
    */

    head = deleteAtEnd(head);          // removes 30
    head = deleteAtEnd(head);          // removes 20 (single-node case)
    head = deleteAtEnd(head);          // "List is empty."

    freeList(head);   // safe even when head == NULL

    return 0;
}
