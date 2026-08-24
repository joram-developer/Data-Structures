public class MinMaxArray {
    
    // Find minimum element and return {value, row, col}
    public static int[] findMin(int[][] arr) {
        int minVal = arr[0][0];
        int minRow = 0, minCol = 0;
        
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] < minVal) {
                    minVal = arr[i][j];
                    minRow = i;
                    minCol = j;
                }
            }
        }
        
        return new int[]{minVal, minRow, minCol};
    }
    
    // Find maximum element and return {value, row, col}
    public static int[] findMax(int[][] arr) {
        int maxVal = arr[0][0];
        int maxRow = 0, maxCol = 0;
        
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                if (arr[i][j] > maxVal) {
                    maxVal = arr[i][j];
                    maxRow = i;
                    maxCol = j;
                }
            }
        }
        
        return new int[]{maxVal, maxRow, maxCol};
    }
    
    public static void main(String[] args) {
        int[][] arr2D = {
            {15, 8, 23, 12},
            {42, 5, 18, 30},
            {9, 35, 11, 6}
        };
        
        System.out.println("2D Array:");
        for (int i = 0; i < arr2D.length; i++) {
            for (int j = 0; j < arr2D[i].length; j++) {
                System.out.print(arr2D[i][j] + " ");
            }
            System.out.println();
        }
        
        int[] min = findMin(arr2D);
        int[] max = findMax(arr2D);
        
        System.out.println("\nMinimum: " + min[0] + " at location [" + min[1] + "][" + min[2] + "]");
        System.out.println("Maximum: " + max[0] + " at location [" + max[1] + "][" + max[2] + "]");
    }
}