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
// CREATE A NEW NODE
// ==============================

struct Node *createNode(int data) {

    struct Node *newNode = malloc(sizeof(struct Node));

    if (newNode == NULL) {
        return NULL;
    }

    newNode->data = data;
    newNode->next = NULL;

    return newNode;
}


// ==============================
// TRAVERSE THE LIST
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
// INSERT AT THE BEGINNING
// ==============================

struct Node *insertAtBeginning(struct Node *head, int data) {

    struct Node *newNode = createNode(data);

    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        return head;
    }

    /*
        The new node must point to the current head first.

            newNode->next = head;

        Then we make the new node the new head.

            head = newNode;

        We must do it in this order so that we don't
        lose our connection to the existing list.
    */

    newNode->next = head;

    head = newNode;

    return head;
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
        If the list is empty, the new node becomes
        the head.

            head → NULL

        becomes:

            head → [newNode] → NULL
    */

    if (head == NULL) {

        head = newNode;

        return head;
    }


    /*
        Otherwise, find the last node.

        We stop when:

            current->next == NULL

        This leaves current pointing at the last node.
    */

    struct Node *current = head;

    while (current->next != NULL) {

        current = current->next;
    }


    // Connect the last node to the new node
    current->next = newNode;

    return head;
}


// ==============================
// INSERT BEFORE A TARGET
// ==============================

struct Node *insertBefore(struct Node *head, int target, int data) {

    struct Node *current = head;

    struct Node *previous = NULL;


    /*
        We need two pointers while searching.

        previous → node before current
        current  → node we are examining

        Example:

            [10] → [20] → [30]
             ↑       ↑
          previous current
    */

    while (current != NULL && current->data != target) {

        previous = current;

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
        If previous == NULL, current is the first node.

        Therefore, there is no node before the target.

        We use the same logic as insertAtBeginning.
    */

    if (previous == NULL) {

        newNode->next = head;

        head = newNode;
    }

    /*
        Otherwise:

            previous → current

        becomes:

            previous → newNode → current
    */

    else {

        newNode->next = current;

        previous->next = newNode;
    }


    return head;
}


// ==============================
// INSERT AFTER A TARGET
// ==============================

struct Node *insertAfter(struct Node *head, int target, int data) {

    struct Node *current = head;


    /*
        Search for the node containing target.
    */

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
              [20] → [30]

        We want:

              [20] → [25] → [30]

        So first make the new node point to
        whatever current was pointing to.

            newNode->next = current->next;

        Then make current point to the new node.

            current->next = newNode;
    */

    newNode->next = current->next;

    current->next = newNode;


    return head;
}


// ==============================
// FREE THE ENTIRE LIST
// ==============================

void freeList(struct Node *head) {

    struct Node *current = head;

    while (current != NULL) {

        struct Node *temp = current;

        current = current->next;

        free(temp);
    }
}


// ==============================
// MAIN
// ==============================

int main() {

    /*
        Create the initial list:

            10 → 20 → 30 → NULL
    */

    struct Node *head = createNode(10);

    struct Node *node2 = createNode(20);

    struct Node *node3 = createNode(30);


    // Connect the nodes

    head->next = node2;

    node2->next = node3;


    printf("Original list: ");
    traverse(head);


    // ==========================================
    // 1. INSERT AT BEGINNING
    // ==========================================

    /*
        Insert 5 at the beginning.

        10 → 20 → 30

        becomes:

        5 → 10 → 20 → 30
    */

    head = insertAtBeginning(head, 5);

    printf("After inserting 5 at beginning: ");
    traverse(head);


    // ==========================================
    // 2. INSERT AT END
    // ==========================================

    /*
        Insert 40 at the end.

        5 → 10 → 20 → 30

        becomes:

        5 → 10 → 20 → 30 → 40
    */

    head = insertAtEnd(head, 40);

    printf("After inserting 40 at end: ");
    traverse(head);


    // ==========================================
    // 3. INSERT BEFORE
    // ==========================================

    /*
        Insert 15 before 20.

        5 → 10 → 20 → 30 → 40

        becomes:

        5 → 10 → 15 → 20 → 30 → 40
    */

    head = insertBefore(head, 20, 15);

    printf("After inserting 15 before 20: ");
    traverse(head);


    // ==========================================
    // 4. INSERT AFTER
    // ==========================================

    /*
        Insert 25 after 20.

        5 → 10 → 15 → 20 → 30 → 40

        becomes:

        5 → 10 → 15 → 20 → 25 → 30 → 40
    */

    head = insertAfter(head, 20, 25);

    printf("After inserting 25 after 20: ");
    traverse(head);


    // ==========================================
    // FREE MEMORY
    // ==========================================

    freeList(head);


    return 0;
}