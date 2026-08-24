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


// Inserts a new node at the beginning of the list
struct Node *insertAtBeginning(struct Node *head, int data) {

    struct Node *newNode = createNode(data);

    if (newNode == NULL) {
        printf("Error: Memory allocation failed. Node was not inserted.\n");
        return head;
    }

    /*
        Why do we follow this order?

        Suppose the list is:

        head
         ↓
        [10] → [20] → [30] → NULL

        and newNode contains:

        [5] → NULL


        STEP 1:
        Make the new node point to the current head:

            newNode->next = head;

        Now:

        newNode
           ↓
          [5] → [10] → [20] → [30] → NULL
                  ↑
                 head


        STEP 2:
        Make head point to the new node:

            head = newNode;

        Now:

             head
              ↓
             [5] → [10] → [20] → [30] → NULL


        We do STEP 1 first because head currently contains
        the address of [10]. We need to copy that address
        into newNode->next before changing head.

        Finally, we return the new head because the original
        head in main() needs to be updated.
    */

    newNode->next = head;
    head = newNode;

    return head;
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

    // Create the initial list:
    // 10 → 20 → 30
    struct Node *head = createNode(10);
    struct Node *node2 = createNode(20);
    struct Node *node3 = createNode(30);

    head->next = node2;
    node2->next = node3;

    printf("Before insertion: ");
    traverse(head);


    // Insert 5 at the beginning
    head = insertAtBeginning(head, 5);

    printf("After insertion:  ");
    traverse(head);


    // Free all nodes
    struct Node *current = head;

    while (current != NULL) {
        struct Node *temp = current;
        current = current->next;
        free(temp);
    }

    return 0;
}