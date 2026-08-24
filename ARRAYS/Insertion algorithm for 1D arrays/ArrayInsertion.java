public class ArrayInsertion {
    public static void main(String[] args) {

        int[] A = {10, 20, 30, 40};
        int capacity = A.length;
        int size = 4;

        int key = 25;
        int location = 2;

        // Check if the array is full
        if (size == capacity) {
            System.out.println("Array is full. Cannot insert.");
            return;
        }
        else{

        // Shift elements to the right
        for (int i = size; i > location; i--) {
            A[i] = A[i - 1];
        }

        // Insert the new element
        A[location] = key;

        // Increase size
        size++;

        // Display array
        System.out.print("Array after insertion: ");

        for (int i = 0; i < size; i++) {
            System.out.print(A[i] + " ");
        }
    }
}
}

/*Both implementations have the same asymptotic 
time and space complexity because they perform 
the same number of element shifts and use constant 
additional space. The language used does not change 
the underlying insertion algorithm.*/