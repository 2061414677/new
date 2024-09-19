package org.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class arrayList {
    public static void main(String[] args) {

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, "张无忌", "张翠山", "张三丰", "张天师", "张无忌", "张翠山", "张三丰", "张天师");
        Collections.shuffle(list);

        String name = list.get(0);
        System.out.println(name);
    }

}
