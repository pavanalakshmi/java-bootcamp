package basicspractice;

import java.util.Scanner;

public class CountAlphaNumber {
    public static void main(String[] args) {
        String s = "abcabc1abcc";

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an integer or character to be counted for occurence:");
        String n = scanner.next();
        int count = 0;
        for (int i=0;i<s.length();i++){
            if(s.charAt(i) == n.charAt(0)){
                count++;

            }
        }
        System.out.println(count);
    }
}
