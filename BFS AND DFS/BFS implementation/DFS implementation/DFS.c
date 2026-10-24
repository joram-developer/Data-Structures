
#include <stdio.h>
#include <stdlib.h>

struct Node {
    char data;
    struct Node *left;
    struct Node *right;
};

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

/* Depth-First Search */
void DFS(struct Node* root) {
    if (root == NULL) {
        return;
    }

    printf("%c ", root->data);  // Visit current node

    DFS(root->left);            // Explore left subtree

    DFS(root->right);           // Explore right subtree
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

    printf("DFS traversal: ");
    DFS(root);
    printf("\n");

    return 0;
}
