package basicspractice;

import java.util.ArrayList;
import java.util.Scanner;

public class Factors {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter value of n:");
        int n = scanner.nextInt();

        ArrayList<Integer> arrayList = new ArrayList<>();
        for(int i=1;i<=10;i++){
            if (n%i==0){
                arrayList.add(i);
            }
        }
        System.out.println("Factors of given numbers: "+arrayList);
        scanner.close();
    }
}
