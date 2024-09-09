package basicspractice;

public class Arrays {
    public static void main(String[] args) {
        String [] cars = {"apple","banana"};
        System.out.println(cars[0]);

        int[] num = {1,2};
        System.out.println(num[0]);

        for (int i=0;i<num.length;i++){
            System.out.println(num[i]);
        }

        int[][] twodArray = {{1,2,3},{4,5,6}};
        System.out.println(twodArray[1][1]);


    }
}
