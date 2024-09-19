package org.example.api;

public interface inter {
    String name = "hello";
    void print();
    void print2();
    default void print3(){
        System.out.println("default print3");
    }
    static void print4(){
        System.out.println("static print4");
    }

}
