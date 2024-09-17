package mosh_java_basics.generics;

public class Main {
    public static void main(String[] args) {
        /**
        var list = new List();
        list.add(1);
        list.add("1");
        list.add(new User());

        to create instance of generic class
        new GenericList<Integer>().add("a"); // we get compile time error. We get compile time type safety.
        var list = new GenericList<Integer>();
        list.add(1);
        int number = list.get(0);

        var list = new GenericList<User>();
        list.add(new User());
        User user = list.get(0);
**/
/**
        Generics and Primitive types - we can use only reference types as an argument to generics.
        If we want to store primitive types, we have to use wrapper class.
        int -> Integer; float -> Float; boolean -> Boolean
 **/
//        GenericList<Integer> numbers = new GenericList<Integer>();
        GenericList<Integer> numbers = new GenericList<>();
        numbers.add(1); //Java compiler automatically wraps to Integer class instance(box) - Boxing.
        int number = numbers.get(0); //extract the value from Integer object - unboxing.

        /**
        Constraints on Type parameter
        Suppose we want to store only Numbers in the class
        public class GenericList<T extends Number>
        Number class is base class for all numbers - java.lang -- int, float, decimal wrapper classes also.
        new GenericList<String>(); //error since its only a Number class.
        We can also add interface as constarint - Comparable -> to implement comparison between 2 user objects.
        can implement multiple - Comparable & Cloneable
**/


    }
}
