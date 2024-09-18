package strings_practice;

public class ArrayIsSorted {
    public static boolean isSorted(int[] array){
        int temp = 0;
        boolean isSorted = false;
        for(int i:array){
            if(temp<i){
                isSorted = true;
                temp = i;
            }
            else{
                isSorted = false;
            }
        }
        return isSorted;
    }

    public static void main(String[] args) {
        System.out.println(isSorted(new int[]{1,2,3,4}));
        System.out.println(isSorted(new int[]{4,3,2,1}));
        System.out.println(isSorted(new int[]{1,2,2,3,4}));
    }
}