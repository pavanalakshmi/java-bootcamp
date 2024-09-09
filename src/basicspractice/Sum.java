package basicspractice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sum {
    public static void main(String[] args) {
        List<Integer> arr = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 1));
        int sum = 0; int sum_even=0; int sum_odd = 0;

        for(int i=0;i<arr.size();i++){
            sum = sum+ arr.get(i);
        }
        System.out.println("Sum of array: "+sum);

        for(int i=0;i<arr.size();i++){
            if(arr.get(i)%2==0){
                sum_even = sum_even+ arr.get(i);
            }
        }
        System.out.println("Sum of even numbers in array: "+sum_even);

        for(int i=0;i<arr.size();i++){
            if(arr.get(i)%2!=0){
                sum_odd = sum_odd+ arr.get(i);
            }
        }
        System.out.println("Sum of odd numbers in array: "+sum_odd);

    }
}
