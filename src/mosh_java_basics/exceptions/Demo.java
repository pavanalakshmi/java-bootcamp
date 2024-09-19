package mosh_java_basics.exceptions;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Demo {
    public static void show() {
        try{
            var reader = new FileReader("file.txt");
            System.out.println("File Opened");
        } catch (
                FileNotFoundException x){ //x is an object of exception which contains info about this exception.
            System.out.println("File doesn't exist: "+x.getMessage());
            x.printStackTrace();
        }
    }
    public static void main(String[] args) {
        show();
    }
}
