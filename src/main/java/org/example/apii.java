package org.example;

public class apii {
    public static void main(String[] args) {
        int a = Math.abs(-1);
        int b = Math.max(1, 2);
        int c = Math.min(1, 2);
        char[] d = {'a', 'b', 'c'};
        String e = new String(d);
        String f = new String(d);
        boolean g = e.equals(f);

        // 输出各个变量的值
        System.out.println("a: " + a);
        System.out.println("b: " + b);
        System.out.println("c: " + c);
        System.out.println("e: " + e);
        System.out.println("f: " + f);
        System.out.println("g: " + g);
    }

}
