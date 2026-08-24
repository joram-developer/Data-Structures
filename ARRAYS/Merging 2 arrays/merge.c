#include <stdio.h>

void merge(int A1[], int L1, int U1, 
           int A2[], int L2, int U2, 
           int A[], int L);

int main() {
    int A1[] = {10, 30, 50};
    int A2[] = {20, 40, 60};
    int A[6];
    
    int L1 = 0, U1 = 2; // Bounds for A1
    int L2 = 0, U2 = 2; // Bounds for A2
    int L = 0;          // Starting index for merged array
    
    merge(A1, L1, U1, A2, L2, U2, A, L);
    
    // Display merged array
    printf("Merged array: ");
    for (int i = 0; i < (U1 - L1 + 1) + (U2 - L2 + 1); i++) {
        printf("%d ", A[i]);
    }
    
    return 0;
}
void merge(int A1[], int L1, int U1, 
           int A2[], int L2, int U2, 
           int A[], int L) {
    
    int i1 = L1, i2 = L2, i = L;
    
    // Step 2-3: Main comparison loop
    while (i1 <= U1 && i2 <= U2) {
        if (A1[i1] < A2[i2]) {
            A[i] = A1[i1];
            i1++;
        } else {
            A[i] = A2[i2];
            i2++;
        }
        i++;
    }
    
    // Step 4-5: Copy remaining from A1
    while (i1 <= U1) {
        A[i] = A1[i1];
        i++;
        i1++;
    }
    
    // Step 6-7: Copy remaining from A2
    while (i2 <= U2) {
        A[i] = A2[i2];
        i++;
        i2++;
    }
}