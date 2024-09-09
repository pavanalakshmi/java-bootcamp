package basicspractice;

import java.util.Scanner;


public class Palindrome {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a string");
        String s = scanner.nextLine();
        scanner.close();

        System.out.println(IsPalindrome(s));

    }
    public static String IsPalindrome(String s){

        String stringReverse = "";
        s = s.replaceAll("[^A-Za-z0-9]","");

        for (int i=0;i<s.length();i++){
            stringReverse = s.charAt(i) + stringReverse;
        }
//        log(stringReverse);

        if(s.equalsIgnoreCase(stringReverse)){
            return "Given string is a palindrome";
        }
        else{
            return "Given string is not a palindrome";
        }

    }
}
