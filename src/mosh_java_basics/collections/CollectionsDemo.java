package mosh_java_basics.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CollectionsDemo {
    public static void show(){
        Collection<String> collection = new ArrayList<>();
//        collection.add("a");
//        collection.add("b");
//        collection.add("c");
        Collections.addAll(collection, "a","b","c");

//        for(var x: collection){
//            System.out.println(x);
//        }
        System.out.println(collection);
//        System.out.println(collection.size());
//        collection.remove("a");
//        System.out.println(collection);
//        collection.clear();
//        System.out.println(collection.isEmpty());
//        var c = collection.contains("a");
//        System.out.println(c);

        Object[] objectArray = collection.toArray(); //every item in array will be instance of object class.
        // It means, if we click on . we dont see any string methods. for that we need to convert to string array.
        String[] stringArray = collection.toArray(new String[0]); // or we can give String[3];
        stringArray[0].split("");
        var stringArray1 = collection.toArray(new String[3]);

        Collection<String> other = new ArrayList<>();
        other.addAll(collection);
        System.out.println(other);

        System.out.println(collection == other); //compares objects in memory // -> so false
        System.out.println(collection.equals(other));

    }

    public static void main(String[] args) {
        show();
    }
}
