package mosh_java_basics.generics;

/**
 T - type or template; T is common convention, we can use any letter
 E - to implement a class which acts as collection, it can store many elements.

 T - is a type parameter for the class; represents the type of objects we store.
 **/

public class GenericList<T extends Number> {
//    private T[] items = new T[10]; //compilation error - as java compiler doesn't know the type of T at this stage.(char class or user class or int).
    // so, we use Object array and cast it to T array.
    private T[] items = (T[]) new Object[10];
    private int count;

    public void add(T item){
        items[count++] = item;
    }

    public T get(int index){
        return items[index];
    }

}
