package shy.sparkproject;

/**
 * Created by Shy on 2016/6/23.
 */
public class Sort1 {

    public static void main(String[] args) {
        int[] arr = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 1, 8};
        System.out.println("排序之前：");
        for (int a : arr) {
            System.out.print(a + " ");
        }
        System.out.println();
        System.out.println("排序之后：");
        int[] sortArr = sort1(arr);
        for (int a : sortArr) {
            System.out.print(a + " ");
        }
    }

    /**
     * 简单选择排序
     *
     * @param arr
     * @return
     */
    public static int[] sort1(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int min = arr[i];
            int n = i;//最小数索引
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < min) {
                    min = arr[j];
                    n = j;
                }
            }
            arr[n] = arr[i];
            arr[i] = min;
        }
        return arr;
    }
}
