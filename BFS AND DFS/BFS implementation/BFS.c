
#include <stdio.h>
#include <stdlib.h>

/* Structure representing a tree node */
struct Node {
    char data;
    struct Node *left;
    struct Node *right;
};

/* Create a new tree node */
struct Node* createNode(char data) {
    struct Node* newNode = malloc(sizeof(struct Node));

    if (newNode == NULL) {
        printf("Memory allocation failed.\n");
        exit(1);
    }

    newNode->data = data;
    newNode->left = NULL;
    newNode->right = NULL;

    return newNode;
}

/* Queue for storing tree nodes */
struct Queue {
    struct Node* nodes[100];
    int front;
    int rear;
};

/* Initialize the queue */
void initializeQueue(struct Queue* q) {
    q->front = 0;
    q->rear = -1;
}

/* Check whether the queue is empty */
int isEmpty(struct Queue* q) {
    return q->front > q->rear;
}

/* Add a node to the queue */
void enqueue(struct Queue* q, struct Node* node) {
    if (q->rear == 99) {
        printf("Queue is full.\n");
        exit(1);
    }

    q->nodes[++q->rear] = node;
}

/* Remove and return the front node */
struct Node* dequeue(struct Queue* q) {
    if (isEmpty(q)) {
        return NULL;
    }

    return q->nodes[q->front++];
}

/* Breadth-First Search */
void BFS(struct Node* root) {
    if (root == NULL) {
        return;
    }

    struct Queue q;
    initializeQueue(&q);

    enqueue(&q, root);

    while (!isEmpty(&q)) {
        struct Node* current = dequeue(&q);

        printf("%c ", current->data);

        if (current->left != NULL) {
            enqueue(&q, current->left);
        }

        if (current->right != NULL) {
            enqueue(&q, current->right);
        }
    }
}

int main(void) {
    /*
             A
            / \
           B   C
          / \ / \
         D  E F  G
    */

    struct Node* root = createNode('A');

    root->left = createNode('B');
    root->right = createNode('C');

    root->left->left = createNode('D');
    root->left->right = createNode('E');

    root->right->left = createNode('F');
    root->right->right = createNode('G');

    printf("BFS traversal: ");
    BFS(root);
    printf("\n");

    return 0;
}
