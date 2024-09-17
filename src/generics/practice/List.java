package generics.practice;
/** Notes--
 we can only store integers in this list, we cant store objects; so creating a new user_list.
 the current code has a lot of duplication in user, userlist classes(for add and get methods).
 If we want to add a list of strings, its a lot more duplication.
 There are 2 solutions for this. 1.poor one - is to use array of object.
**/

public class List {
    public static void main(String[] args) {

        /**var list = new List(); //from generics package
         list.add(1); //can only add integers initially.
         list.add("1");
         list.add(new User());
         //this is the issue with the first solution. when we try to get an integer value, we will get an error as its an object.
         // so we have to explicitly cast it. Also, if we cast it to wrong type, we will get an invalid cast exception.
         // we only get this at run-time. So, we use generics.
         int number =(int) list.get(0);
         **/

        /** var list = new GenericList<Integer>(); // we will get error in compile time for type erros.
        list.add(1);
        // new GenericList<User>(); list.add(new User()); User user = list.get(0); - can also add user object.
        int number = list.get(0);
         when creating an instance for generic class, we can only use reference type as arguments to generic type-
         such as Object class, User class, String, Integer etc, but not primitive types like int..
         If we have to store primitive values, we need to use their wrapper class. int-> Integer, float->Float **/

        /**
        GenericList<Integer> numbers = new GenericList<>();
        numbers.add(1); //here, we are passing primitive int 1, but java compiler will automatically wrap this class to a instance of
        // Integer class. This is called - BOXING. compiler puts this value inside a box.
        int number = numbers.get(0); //here, java compiler extracts the value stored in integer object. -- UNBOXING.
         **/

//        new GenericList<User>();


    }
    private Object[] items = new Object[10];
    private int count;
    public void add(Object item){
        items[count++] = item;
    }
    public Object get(int index){
        return items[index];
    }

   /** private int[] items = new int[10];
    private int count;
    public void add(int item){
        items[count++] = item;
    }
    public int get(int index){
        return items[index];
    }**/
}
