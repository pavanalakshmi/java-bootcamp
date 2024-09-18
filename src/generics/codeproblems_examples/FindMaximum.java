package generics.codeproblems_examples;

public class FindMaximum {
    public static<T extends Comparable<T>> T findMaximum(T a, T b, T c){
        T max = a;
        if(b.compareTo(max)>0){
            max = b;
        } if (c.compareTo(max)>0) {
            max = c;
        }
        return max;
    }

    public static void main(String[] args) {
        System.out.println(findMaximum(3, 5, 7));
        System.out.println(findMaximum("apple", "banana", "pear"));
        //Strings are compared lexicographically (dictionary order).
    }
}
