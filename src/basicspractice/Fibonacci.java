package basicspractice;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Fibonacci {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an integer:");
        int n = scanner.nextInt();
        int n1 = 0, n2=1, temp = 0;

        List<Integer> fib = new ArrayList<>();
        fib.add(n1);
        fib.add(n2);

        for(int i=0;i<n-2;i++){
            temp = n1+n2;
            fib.add(temp);
            n1 = n2;
            n2 = temp;

        }
        System.out.println(fib);

    }
}
