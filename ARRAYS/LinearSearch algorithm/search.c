#include <stdio.h>

int main() {
    int A[] = {10, 20, 30, 40, 50};
    int size = sizeof(A) / sizeof(A[0]);

    int key = 30;
    int position = -1;

    // Search for the key
    for (int i = 0; i < size; i++) {

        if (A[i] == key) {
            position = i;
            break;
        }
    }

    if (position != -1) {
        printf("Element found at position %d\n", position);
    } else {
        printf("Element not found\n");
    }

    return 0;
}