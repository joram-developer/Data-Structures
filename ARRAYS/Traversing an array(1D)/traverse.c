#include <stdio.h>

int main() {
    int A[] = {10, 20, 30, 40, 50};
    int size = sizeof(A) / sizeof(A[0]);

    // Traverse the array
    for (int i = 0; i < size; i++) {
        printf("%d ", A[i]);
    }

    return 0;
}