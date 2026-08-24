#include <stdio.h>
#include <stdlib.h>


// Defines the structure of a node
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


// Traverses the list and prints each node's data
void traverse(struct Node *head) {

    struct Node *current = head;

    while (current != NULL) {

        printf("%d ", current->data);

        current = current->next;
    }

    printf("\n");
}


// Inserts a new node at the end of the list
struct Node *insertAtEnd(struct Node *head, int data) {

    // Create the new node
    struct Node *newNode = createNode(data);

    if (newNode == NULL) {

        printf("Memory allocation failed.\n");

        return head;
    }


    /*
        There are two cases.

        CASE 1: The list is empty

        head
         ↓
        NULL

        Since there is no existing node, the new node
        becomes the first node.

            head = newNode;


        CASE 2: The list is not empty

        head
         ↓
        [10] → [20] → [30] → NULL

        We need to find the LAST node.

        Therefore, we stop when:

            current->next == NULL

        Notice that we do NOT use:

            current == NULL

        because that would move current past the last node.

        We need current to remain pointing at [30] so
        that we can change:

            [30].next

        from:

            NULL

        to:

            newNode

        giving:

        [10] → [20] → [30] → [40] → NULL
    */


    // CASE 1: The list is empty
    if (head == NULL) {

        head = newNode;

        return head;
    }


    // CASE 2: Find the last node
    struct Node *current = head;

    while (current->next != NULL) {

        current = current->next;
    }


    // Connect the last node to the new node
    current->next = newNode;


    return head;
}


int main() {

    /*
        Create our initial list:

        10 → 20 → 30 → NULL
    */

    struct Node *head = createNode(10);

    struct Node *node2 = createNode(20);

    struct Node *node3 = createNode(30);


    // Connect the nodes
    head->next = node2;

    node2->next = node3;


    // Display the original list
    printf("Original list: ");

    traverse(head);


    /*
        Insert 40 at the end:

        Before:

        10 → 20 → 30 → NULL

        After:

        10 → 20 → 30 → 40 → NULL
    */

    head = insertAtEnd(head, 40);


    printf("After inserting 40: ");

    traverse(head);


    /*
        Insert another node at the end.
    */

    head = insertAtEnd(head, 50);


    printf("After inserting 50: ");

    traverse(head);


    /*
        Free all dynamically allocated nodes.

        We use a temporary pointer so that we can
        remember where the next node is before
        freeing the current node.
    */

    struct Node *current = head;

    while (current != NULL) {

        struct Node *temp = current;

        current = current->next;

        free(temp);
    }


    return 0;
}