
#include <stdio.h>
#include <stdlib.h>

struct Node {
    char data;
    struct Node *left;
    struct Node *right;
};

struct Node* createNode(char data) {
    struct Node* n = malloc(sizeof(struct Node));
    if (n == NULL) exit(1);

    n->data = data;
    n->left = NULL;
    n->right = NULL;
    return n;
}

int backtrack(struct Node* root, char target) {
    if (root == NULL)
        return 0;  // This path failed

    printf("Trying %c\n", root->data);

    if (root->data == target)
        return 1;  // Target found

    // Try left path first
    if (backtrack(root->left, target))
        return 1;

    // If left path fails, try right path
    if (backtrack(root->right, target))
        return 1;

    return 0;  // Neither path worked
}

int main(void) {
    struct Node* root = createNode('A');

    root->left = createNode('B');
    root->right = createNode('C');
    root->left->left = createNode('D');
    root->left->right = createNode('E');
    root->right->left = createNode('F');
    root->right->right = createNode('G');

    if (backtrack(root, 'G'))
        printf("Target found!\n");
    else
        printf("Target not found.\n");

    return 0;
}
