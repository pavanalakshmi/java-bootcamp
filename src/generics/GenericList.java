package generics;
/** T- for type, we can use any letter
 E- to implement a class whioch acts as a collection, so we can store many elements.
 So, T is a type parameter for this class, just like methods have parameters, classes also have parameters.
 T - type of object we want to store in this list.
 When creating an instance of this class, we need to specify an argument or value for this parameter.
 **/

/**
Constraints - If we want to add a constraint or restriction on type parameter.
 If we only want to store numbers -- GenericList<T extends Number>
 here, we can only use number class or its sub-classes.
 Number class in java -- java.lang.Number -- is the superclass of classes Double, Integer, Float.
 Its an abstract class.

 This constraint doesnt have to be a class. It can also be an interface. for eg., Comparable.
 Used for implementing classes that can be compared with each other. eg., we can compare 2 users based on their last login date.

 can give multiple constraints.
 We call T as a bounded type parameter - as it bounded/restricted.
**/

public class GenericList<T extends Comparable> {
    private T[] items = (T[]) new Object[10]; //error if we use new T[10], since java compiler doesnt know the type here, so we can use object and cast it as T.
    private int count;
    public void add(T item){
        items[count++] = item;
    }
    public T get (int index){
        return items[index];
    }
}
