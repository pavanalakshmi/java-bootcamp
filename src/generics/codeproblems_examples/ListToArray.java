package generics.codeproblems_examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//Write a generic method listToArray that converts a List<T> to an array of type T[].

public class ListToArray {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        System.out.println(Arrays.toString(listToArray(list, new Integer[list.size()])));
//        List<Integer> intList = List.of(1, 2, 3, 4);
//        Integer[] intArray = new Integer[intList.size()];

    }
    public static <T> T[] listToArray(List<T> list, T[] returnArray){
        if(list!=null){
            for(int i=0;i<list.size();i++){
                returnArray[i] = list.get(i);
            }
        }
        else{
            returnArray=null;
        }
        return returnArray;
    }

}
