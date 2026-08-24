#include <stdio.h>
#include <stdlib.h>

struct Node {
    int data;
    struct Node *next;
};

struct Node *createNode(int data) {// Function to create a new node with given data
    struct Node *newNode = malloc(sizeof(struct Node));

    if (newNode == NULL) {
        return NULL;
    }

    newNode->data = data;
    newNode->next = NULL;

    return newNode;
}

void traverse(struct Node *head) {// Function to traverse and print the linked list
    struct Node *current = head;// Start from the head of the list

    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;// Move to the next node
    }

    printf("\n");
}

int main() {

    struct Node *head = createNode(10);// Create the head node with data 10
    struct Node *node2 = createNode(20);
    struct Node *node3 = createNode(30);

    head->next = node2;
    node2->next = node3;

    traverse(head);

    free(head);
    free(node2);
    free(node3);

    return 0;
}