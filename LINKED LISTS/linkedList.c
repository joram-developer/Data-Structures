#include <stdio.h>
#include <stdlib.h>

struct Node {
    int data;
    struct Node *next;
};

int main() {

    struct Node *node1;
    struct Node *node2;
    struct Node *node3;

    // Allocate memory
    node1 = malloc(sizeof(struct Node));
    node2 = malloc(sizeof(struct Node));
    node3 = malloc(sizeof(struct Node));

    // Check allocation
    if (node1 == NULL || node2 == NULL || node3 == NULL) {
        printf("Memory allocation failed.\n");
        return 1;
    }

    // Store data
    node1->data = 10;
    node2->data = 20;
    node3->data = 30;

    // Connect nodes
    node1->next = node2;
    node2->next = node3;
    node3->next = NULL;

    // Head points to first node
    struct Node *head = node1;

    // Traverse the list
    struct Node *current = head;

    while (current != NULL) {
        printf("%d ", current->data);
        current = current->next;
    }

    // Free memory
    free(node1);
    free(node2);
    free(node3);

    return 0;
}