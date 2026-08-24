#include <stdio.h>
#include <stdlib.h>

struct Node {
    int data;
    struct Node *next;
};


// Creates a new node
struct Node *createNode(int data) {

    struct Node *newNode = malloc(sizeof(struct Node));

    if (newNode == NULL) {
        return NULL;
    }

    newNode->data = data;
    newNode->next = NULL;

    return newNode;
}


// Inserts a new node after the given node
void insertAfter(struct Node *previous, int data) {

    // Create the new node
    struct Node *newNode = createNode(data);

    if (newNode == NULL) {
        return;
    }

    /*
        Why do we follow this order?

        Suppose we have:

        previous
           ↓
        [10] → [20] → [30]

        newNode
           ↓
        [15]

        First:

            newNode->next = previous->next;

        This makes:

        [15] → [20]

        We do this BEFORE changing previous->next because
        previous->next currently contains the address of [20].
        We need to preserve that address.

        Then:

            previous->next = newNode;

        This makes:

        [10] → [15] → [20] → [30]

        So the algorithm is:

        1. Make the new node point to what previous was pointing to.
        2. Make previous point to the new node.
    */

    newNode->next = previous->next;
    previous->next = newNode;
}


// Traverses and prints the list
void traverse(struct Node *head) {

    struct Node *current = head;

    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;
    }

    printf("\n");
}


int main() {

    // Create the first three nodes
    struct Node *head = createNode(10);
    struct Node *node2 = createNode(20);
    struct Node *node3 = createNode(30);

    // Connect them
    head->next = node2;
    node2->next = node3;

    printf("Before insertion: ");
    traverse(head);


    // Insert 15 after 10
    insertAfter(head, 15);

    printf("After insertion:  ");
    traverse(head);


    // Free the nodes
    free(head->next->next->next);  // 30
    free(head->next->next);        // 20
    free(head->next);              // 15
    free(head);                    // 10

    return 0;
}