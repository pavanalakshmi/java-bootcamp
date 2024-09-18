package generics.codeproblems_examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CountOddIntegers {
    public static void main(String[] args) {
        List<Integer> intList = Arrays.asList(1,2,4,3,5,6,7,7);
        ArrayList<Integer> emptyList = new ArrayList<>();
        List<Integer> allEven = Arrays.asList(2,4,6,8);
        List<Integer> allOdd = Arrays.asList(1,3,5,7,9);

        System.out.println(countOddNumbers(intList));
        System.out.println(countOddNumbers(emptyList));
        System.out.println(countOddNumbers(allEven));
        System.out.println(countOddNumbers(allOdd));

    }
    public static int countOddNumbers(Collection<Integer> collection){
        int count =0;
        if(!collection.isEmpty()){
            for(Integer val:collection){
                if(val%2!=0 && val!=null){
                    count++;
                }
            }
        }
        return count;
    }
}
