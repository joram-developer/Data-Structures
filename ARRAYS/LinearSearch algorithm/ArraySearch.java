public class ArraySearch {
    public static void main(String[] args) {

        int[] A = {10, 20, 30, 40, 50};
        int size = A.length;

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
            System.out.println("Element found at position " + position);
        } else {
            System.out.println("Element not found");
        }
    }
}