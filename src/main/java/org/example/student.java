package org.example;

public class student {
    private String name;
    private int age;
    class Student{}
    public student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("姓名：" + name + "，年龄：" + age);
    }
    public static void main(String[] args) {
        student s = new student("张三", 18);
        s.setAge(19);
        s.setName("李四");
        s.show();
    }
}
