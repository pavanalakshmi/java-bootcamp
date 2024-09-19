package mosh_java_basics.exceptions;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class ExceptionsDemo {
    public static void show() {
        sayHello(null);
    }

    public static void sayHello(String name) {
        System.out.println(name.toUpperCase());
    }

    public static void main(String[] args) {
        ExceptionsDemo.show();
    }
}
