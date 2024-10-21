package org.pavani.strings_practice;

import java.util.Arrays;

public class FindMinMax {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(findMinMax(new int[]{1, 2, 3, 4, 5})));
        System.out.println(Arrays.toString(findMinMax(new int[]{10, -3, 7, 2})));
    }

    public static int[] findMinMax(int[] array){
        int[] output = new int[2];
        output[0] = minimum(array);
        output[1] = maximum(array);
        return output;
    }

    public static int minimum(int[] array) {
        int min = array[0];
        for (int i : array) {
            if(i<min) min = i;
        }
        return min;
    }
    public static int maximum(int[] array){
        int max = array[0];
        for (int i : array) {
            if(i>max) max = i;
        }
        return max;
    }

}