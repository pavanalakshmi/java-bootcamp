package basicspractice;

import java.util.ArrayList;
import java.util.List;

public class OddAndEven {
    public static void main(String[] args) {
        System.out.println("First 50 odd numbers: ");
        System.out.println(FiftyOddNumbers());
        System.out.println("First 50 even numbers: ");
        System.out.println(FiftyEvenNumbers());

        List<Integer> oddNumbers = FiftyOddNumbers();
        List<Integer> evenNumbers = FiftyEvenNumbers();

        int sumOfOdd = 0;
        int sumOfEven = 0;
        for(int od: oddNumbers){
            sumOfOdd += od;
        }
        System.out.println("Sum of first 50 odd numbers: " +sumOfOdd);

        for(int ev: evenNumbers){
            sumOfEven += ev;
        }
        System.out.println("Sum of first 50 odd numbers: "+sumOfEven);

    }
    public static List<Integer> FiftyOddNumbers(){
        int val = 0;
        List<Integer> list = new ArrayList<>();
        while(list.size()<50)
        {
            if(val% 2!=0){
                list.add(val);
        }
            val++;
            }
        return list;

    }

    public static List<Integer> FiftyEvenNumbers(){
        int val = 0;
        List<Integer> list = new ArrayList<>();
        while(list.size()<50)
        {
            if(val% 2==0){
                list.add(val);
            }
            val++;
        }
        return list;

    }

}
