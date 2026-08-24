#include <stdio.h>
#include <stdlib.h>

struct Node {
    int data;
    struct Node *next;
};

struct Node *createNode(int data) {// Function to create a new node with given data

    struct Node *newNode;

    newNode = malloc(sizeof(struct Node));

    if (newNode == NULL) {
        return NULL;
    }

    newNode->data = data;
    newNode->next = NULL;

    return newNode;
}

int main() {

    struct Node *node1;
    struct Node *node2;
    struct Node *node3;

    node1 = createNode(10);
    node2 = createNode(20);
    node3 = createNode(30);

    if (node1 == NULL || node2 == NULL || node3 == NULL) {
        printf("Memory allocation failed.\n");
        return 1;
    }

    node1->next = node2;
    node2->next = node3;

    struct Node *current = node1;

    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;
    }

    free(node1);
    free(node2);
    free(node3);

    return 0;
}