package org.pavani.strings_practice;

import java.util.Arrays;

public class MergeTwoArrays {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(mergeArrays(new int[]{1,2,3}, new int[]{4,6,3})));
    }
    public static int[] mergeArrays(int[] array1, int[] array2){
        int length = array1.length + array2.length;
        int[] output = new int[length];

        for(int i=0;i<array1.length;i++){
            output[i] = array1[i];
        }
        for(int j=0;j<array2.length;j++){
            output[j+array1.length] = array2[j];
        }
        return output;
    }
}