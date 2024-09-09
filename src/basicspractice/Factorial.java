package basicspractice;

import java.util.Scanner;

public class Factorial {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an integer:");
        int n = scanner.nextInt();
        int fact = 1;

        if (n>=1 && n<=12){
            for(int i=1;i<=n;i++){
                fact = fact * i;
            }
        }
        System.out.println("Factorial is: "+fact);
    }
}
