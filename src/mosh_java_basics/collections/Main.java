package mosh_java_basics.collections;

public class Main {
    public static void main(String[] args) {
        var list = new GenericList<String>();
//        list.add("a");
//        list.add("b");
//        for(var item:list.items)
//            System.out.println(item);

//        var iterator = list.iterator();
//        while (iterator.hasNext()){
//            var current = iterator.next();
//            System.out.println(current);
//        }

        list.add("a");
        list.add("b");

        for(var item:list){
            System.out.println(item);
        } // by default, for each call iterator, so we can replace above block of iterator code.

    }
}
