public class MergeArrays {
    
    public static void merge1D(int[] A1, int[] A2, int[] result) {
        // Copy A1 to result
        for (int i = 0; i < A1.length; i++) {
            result[i] = A1[i];
        }
        
        // Copy A2 to result
        for (int i = 0; i < A2.length; i++) {
            result[A1.length + i] = A2[i];
        }
    }
    
    public static void printArray(int[] arr) {
        for (int num : arr) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
    
    public static void main(String[] args) {
        int[] A1 = {1, 2, 3, 4, 5};
        int[] A2 = {6, 7, 8, 9};
        int[] result = new int[A1.length + A2.length];
        
        System.out.print("Array 1: ");
        printArray(A1);
        
        System.out.print("Array 2: ");
        printArray(A2);
        
        merge1D(A1, A2, result);
        
        System.out.print("Merged Array: ");
        printArray(result);
    }
}