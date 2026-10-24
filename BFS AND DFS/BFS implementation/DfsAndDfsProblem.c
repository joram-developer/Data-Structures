//Problem 3: Find a particular value in a binary tree using BFS and DFS

// A. Problem statement
// Given a binary tree and a target value, write two programs to determine whether the target exists in the tree:
// - BFS: search level by level using a queue.
// - DFS: search down one branch before returning, using recursion.

/*
Algorithm BFS(root, target)

1. If root is NULL, return NOT FOUND.
2. Create an empty queue.
3. Add root to the queue.
4. While the queue is not empty:
      a. Remove the front node.
      b. Process the node.
      c. If its value equals target:
             Return FOUND.
      d. Add its left child if it exists.
      e. Add its right child if it exists.
5. Return NOT FOUND.
*/

/*
Algorithm DFS(root, target)

1. If root is NULL, return NOT FOUND.
2. Process the current node.
3. If its value equals target:
       Return FOUND.
4. Search the left subtree using DFS.
5. If FOUND, return FOUND.
6. Search the right subtree using DFS.
7. If FOUND, return FOUND.
8. Otherwise, return NOT FOUND.
*/
#include <stdio.h>
#include <stdlib.h>

struct Node {// desribes what every node in the binary tree should have
    char data;
    struct Node *left;
    struct Node *right;
};

struct Node* createNode(char value) {
    struct Node* node = malloc(sizeof(struct Node));

    if (node == NULL)
        exit(1);

    node->data = value;
    node->left = NULL;
    node->right = NULL;

    return node;
    // therefore, calling say, createNode(A) will return a pointer to a node with data 'A' and left and right children as NULL
}

/* Breadth-First Search */
int BFS(struct Node* root, char target) {
    if (root == NULL)
        return 0;

    struct Node* queue[100];//This is an array of 100 pointers to nodes
    int front = 0, rear = 0;

    queue[rear++] = root;

    while (front < rear) {//As long as front is less than rear, there is at least one queued node waiting to be processed.
        struct Node* current = queue[front++];

        printf("%c ", current->data);

        if (current->data == target)
            return 1;

        if (current->left != NULL)
            queue[rear++] = current->left;

        if (current->right != NULL)
            queue[rear++] = current->right;
    }

    return 0;
}

/* Depth-First Search */
int DFS(struct Node* root, char target) {
    if (root == NULL)
        return 0;

    printf("%c ", root->data);

    if (root->data == target)
        return 1;

    if (DFS(root->left, target))
        return 1;

    if (DFS(root->right, target))
        return 1;

    return 0;
}


/* Depth-First Search using a stack */
// int DFS(struct Node* root, char target) {
//     if (root == NULL)
//         return 0;

//     struct Node* stack[100];
//     int top = -1;

//     stack[++top] = root;

//     while (top >= 0) {
//         struct Node* current = stack[top--];

//         printf("%c ", current->data);

//         if (current->data == target)
//             return 1;

//         /* Push right first, so left is processed first */
//         if (current->right != NULL)
//             stack[++top] = current->right;

//         if (current->left != NULL)
//             stack[++top] = current->left;
//     }

//     return 0;
// }


int main(void) {
    struct Node* root = createNode('A');

    root->left = createNode('B');
    root->right = createNode('C');
    root->left->left = createNode('D');
    root->left->right = createNode('E');
    root->right->left = createNode('F');
    root->right->right = createNode('G');

    char target = 'F';

    printf("BFS search order: ");
    if (BFS(root, target))
        printf("\nTarget found!\n");
    else
        printf("\nTarget not found.\n");

    printf("\nDFS search order: ");
    if (DFS(root, target))
        printf("\nTarget found!\n");
    else
        printf("\nTarget not found.\n");

    return 0;
}
