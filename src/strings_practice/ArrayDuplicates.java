package strings_practice;

import java.util.Arrays;

public class ArrayDuplicates {
    public static void main(String[] args) {
        ArrayDuplicates arrayDuplicates = new ArrayDuplicates();
        System.out.println(Arrays.toString(arrayDuplicates.findDuplicates(new int[]{1,2,2,3,2,4,3})));
//        String o = Arrays.toString(arrayDuplicates.findDuplicates(new int[]{1,2,3,2,4,3}));
    }

    private int[] findDuplicates(int[] array){
        int[] temp = new int[array.length];
        int count=0; //boolean search = false;
        for (int i=0;i<array.length;i++){
            for(int j=0;j<array.length;j++){ // or j=i+1
                boolean search = false; //-- reset the search every iteration, can use this instead of else search=false.
                if((i!=j) && (array[i]==array[j])){
                    for(int x=0;x<temp.length;x++){
                        if((array[j] == temp[x])){
                            search = true;
                            break;
                        }
                       // else{search=false;}
                    }
                    if(search!= true){
                        temp[count] = array[j];
                        count++;
                    }
                    break;
                }
            }
        }
        int[] output_array = new int[count]; int y=0;
        while(y<count){
            output_array[y] = temp[y];
            y++;
        }
        return output_array;
}
    }
