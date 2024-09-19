package mosh_java_basics.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListDemo {
    public static void show(){
        List<String> list = new ArrayList<>();
//        list.add("a");
//        list.add("b");
//        list.add("c");
//        list.add(0,"!");
//        System.out.println(list);

        Collections.addAll(list, "a", "b", "c", "c");
        System.out.println(list.get(0));
        list.set(0, "a+");
//        System.out.println(list);
//        list.remove(0);
        System.out.println(list.indexOf("a")); //-1 don't exist
        System.out.println(list.lastIndexOf("c"));
        System.out.println(list.subList(0,2)); // From is inclusive, To is exclusive.
        // sublist doesn't affect original list.
    }

    public static void main(String[] args) {
        show();
    }
}
