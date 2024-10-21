package org.pavani.strings_practice;
import java.util.ArrayList;
import java.util.Arrays;

public class ShiftElements {
    public static void main(String[] args) {
        ShiftElements shiftElements = new ShiftElements();
        System.out.println(Arrays.toString(shiftElements.shiftArray(new int[]{1,2,3,4},2)));
        System.out.println(Arrays.toString(shiftElements.shiftArray(new int[]{10,20,30},1)));
        System.out.println(Arrays.toString(shiftElements.shiftArray(new int[]{1,2,3,2,2,4,3},2)));
    }
    public int[] shiftArray(int[] array, int positions){
        ArrayList<Integer> arrayList = new ArrayList<>();
//        positions = positions%array.length;
        for(int i=0;i<array.length;i++){
            for(int j=array.length-positions;j<array.length;j++){
                arrayList.add(array[j]);
            }
            for(int x=0;x<array.length-positions;x++){
                arrayList.add(array[x]);
            }
            break;
        }
        for(int v=0;v<array.length;v++){
            array[v] = arrayList.get(v);
        }
        return array;
    }
}
