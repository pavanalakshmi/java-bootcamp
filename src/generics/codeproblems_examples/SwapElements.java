package generics.codeproblems_examples;

import java.util.Arrays;

public class SwapElements {
    public static <T> void swapElements(T[] array, int index1, int index2){
        T temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }

    public static void main(String[] args) {
        Integer[] numbers = {1,2,3,4};
        swapElements(numbers, 1, 3);
        System.out.println(Arrays.toString(numbers)); // 1,4,3,2

        String[] words = {"Apple", "Banana", "Pears", "Cherries"};
        swapElements(words, 0, 2);
        System.out.println(Arrays.toString(words)); //[Pears, Banana, Apple]
    }
}
