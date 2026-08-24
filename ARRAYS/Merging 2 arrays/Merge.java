public class Merge {
    public static void main(String[] args) {
        int[] A1 = {10, 30, 50};
        int[] A2 = {20, 40, 60};
        int size1 = A1.length;
        int size2 = A2.length;
        int[] A = new int[size1 + size2];

        merge(A1, 0, size1 - 1, A2, 0, size2 - 1, A, 0);

        // Display merged array
        System.out.print("Merged array: ");
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + " ");
        }
    }

public static void merge(int[] A1, int L1, int U1,
                        int[] A2, int L2, int U2,
                        int[] A, int L) {
    
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
}