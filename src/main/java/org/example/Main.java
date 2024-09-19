package org.example;

public class Main {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        printArr printArr = new printArr();
        printArr.print(arr); // 调用print方法
        System.out.println("Hello world!");
    }

    public static class printArr {
        public void print(int[] arr) {
            for (int i = 0; i < arr.length; i++) {
                System.out.println(arr[i]);
            }
        }
    }
}
