public class ArraySorting {
    public static void main(String[] args) {

        int[] A = {40, 10, 50, 20, 30};
        int size = 5;

        // Sorting the array
        for (int i = size - 1; i >= 0; i--) {

            for (int j = 0; j < i; j++) {

                if (A[j] > A[j + 1]) {

                    // Swap A[j] and A[j + 1]
                    int temp = A[j];
                    A[j] = A[j + 1];
                    A[j + 1] = temp;
                }
            }
        }

        // Display sorted array
        System.out.print("Sorted array: ");

        for (int i = 0; i < size; i++) {
            System.out.print(A[i] + " ");
        }
    }
}