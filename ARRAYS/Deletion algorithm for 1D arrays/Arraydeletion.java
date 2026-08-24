public class Arraydeletion {
    public static void main(String[] args) {

        int[] A = {10, 20, 30, 40, 50};

        int size = 5;
        int location = 2;

        // Check whether the array is empty
        if (size == 0) {
            System.out.println("Array is empty. Cannot delete.");
            return;
        }
        else if (location < 0 || location >= size) {
            System.out.println("Invalid location. Cannot delete.");
            return;
        }
        else{

        // Shift elements to the left
        for (int i = location; i < size - 1; i++) {
            A[i] = A[i + 1];
        }

        // Decrease the size
        size--;

        // Display the array
        System.out.print("Array after deletion: ");

        for (int i = 0; i < size; i++) {
            System.out.print(A[i] + " ");
        }
    }
}

    
}