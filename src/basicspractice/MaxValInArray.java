package basicspractice;

public class MaxValInArray {
    public static void main(String[] args) {
        System.out.println("Maximum value is: "+findMaximumValue(new int[]{1,4,6,5}));
    }
    public static int findMaximumValue(int args[]){
        int max = args[0];
        for (int temp: args){
            if (temp>max){
                max = temp;
            }
        }
        return max;
    }
}
