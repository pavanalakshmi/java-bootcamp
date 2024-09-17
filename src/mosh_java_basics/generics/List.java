package mosh_java_basics.generics;
/** we cant store users in this class, we can only store integers. so we create userlist class.
If we want to store string, we need to create another class. This is very tedious and duplicate.
 1 way to solve this problem is -
 to use array if objects, as object class is base or parent of all reference types in java.
 **/

public class List {
//    private int[] items = new int[10];
//    private int count;
//
//    public void add(int item){
//        items[count++] = item;
//    }
//    public int get(int index){
//        return items[index];
//    }

    // replacing with objects
    private Object[] items = new Object[10];
    private int count;

    public void add(Object item){
        items[count++] = item;
    }
    public Object get(int index){
        return items[index];
    }
/** This is a bad approach -- as -->
    list.add(Integer.valueOf(1));
    list.add("1");
    int number = (int) list.get(0);
    here, since we know its an integer, we are casting explicitly,
    but if we dont know the type and we cast it to incorrect type, we will get a ClassCastException.
    We only identify this at run-time.
    So, to identify errors at compile time, we use generic classes.

 **/
}
