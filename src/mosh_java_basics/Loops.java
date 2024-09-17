package mosh_java_basics;

import java.awt.desktop.SystemEventListener;
import java.util.Scanner;

public class Loops {
    public static void main(String[] args) {

        /**
        for(int i=0; i<5;i++)
            System.out.println("Hello World "+i);
        for(int j=5; j>0; j--)
            System.out.println("Hello "+j);

        int x=10;
        while(x>0){
            System.out.println("World "+x);
            x--;
        }

        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(!input.equals("quit")){
            System.out.print("Input: ");
            input = scanner.next().toLowerCase();
//            System.out.println(input);
        }

        do{
            System.out.print("Input: ");
            input = scanner.next().toLowerCase();
            System.out.println(input);
        } while(!input.equals("quit"));


        Scanner scanner = new Scanner(System.in);
        String input = "";
        while(true){
            System.out.print("Input: ");
            input = scanner.next().toLowerCase();
//            if(!input.equals("quit"))
//                System.out.println(input);
            if(input.equals("pass"))
                continue; //next statements will be ignored and control moves to the beginning of the loop.
            if(input.equals("quit"))
                break;
            System.out.println(input);
        }

         **/

//        For each loop
        String[] fruits = {"Apple", "Mango", "Orange"};
        for(int i=0;i<fruits.length;i++)
            System.out.println(fruits[i]);

        for(String fruit: fruits)
            System.out.println(fruit);



    }
}
