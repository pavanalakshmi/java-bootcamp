package mosh_java_basics;

import java.util.Scanner;

public class FizzBuzz {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter any number: ");
        int number = scanner.nextInt();

       /** if (number%3 == 0 && number%5 ==0)
            System.out.println("FizzBuzz");
        else if(number%5 == 0)
            System.out.println("Fizz");
        else if (number%3 == 0)
            System.out.println("Buzz");
        else
            System.out.println(number);**/
        // DRY - Don't Repeat Code

        if (number%3 == 0){
            if(number%5 ==0)
                System.out.println("FizzBuzz");
            else
                System.out.println("Buzz");
        }
        else if(number%5 == 0)
            System.out.println("Fizz");
        else
            System.out.println(number);

        // even if its reused, [previous one is more readable, as the below one has nested structures and not readable.

    }
}
