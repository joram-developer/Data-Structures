#include <stdio.h>
#include <stdlib.h>


// Node structure
struct Node {
    int data;
    struct Node *next;
};


// Creates and initializes a new node
struct Node *createNode(int data) {

    struct Node *newNode = malloc(sizeof(struct Node));

    if (newNode == NULL) {
        return NULL;
    }

    newNode->data = data;
    newNode->next = NULL;

    return newNode;
}


// Traverses the list and prints every node
void traverse(struct Node *head) {

    struct Node *current = head;

    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;
    }

    printf("\n");
}


// Inserts a new node before the node containing target
struct Node *insertBefore(struct Node *head, int target, int data) {

    struct Node *current = head;
    struct Node *previous = NULL;

    /*
        We need two pointers while searching:

        previous → node before current
        current  → node we are currently examining

        Example:

        [10] → [20] → [30]
         ↑       ↑
      previous current


        When current reaches the target:

        previous → [10]
        current  → [20]

        To insert [15] between them:

        1. Make the new node point to current.
        2. Make previous point to the new node.

        Result:

        [10] → [15] → [20] → [30]


        Special case:

        If previous == NULL, current is the first node.

        There is no node before it, so we use the
        insert-at-beginning algorithm:

        newNode->next = head;
        head = newNode;
    */


    // Search for the target node
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


    // Target is the first node
    if (previous == NULL) {

        newNode->next = head;
        head = newNode;
    }


    // Target is somewhere after the first node
    else {

        newNode->next = current;
        previous->next = newNode;
    }


    return head;
}


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


    /*
        Insert 15 before 20:

        Before:

        10 → 20 → 30

        After:

        10 → 15 → 20 → 30
    */

    head = insertBefore(head, 20, 15);


    printf("After inserting 15 before 20: ");
    traverse(head);


    /*
        Insert 5 before 10.

        This tests the special case where the target
        is the first node.

        Before:

        10 → 15 → 20 → 30

        After:

        5 → 10 → 15 → 20 → 30
    */

    head = insertBefore(head, 10, 5);


    printf("After inserting 5 before 10:  ");
    traverse(head);


    /*
        Try to insert before a value that doesn't exist.
    */

    head = insertBefore(head, 50, 40);


    /*
        Free all nodes.

        We traverse the list while freeing each node
        so that we don't leave dynamically allocated
        memory behind.
    */

    struct Node *current = head;

    while (current != NULL) {

        struct Node *temp = current;

        current = current->next;

        free(temp);
    }


    return 0;
}