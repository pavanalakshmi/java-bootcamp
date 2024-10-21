package org.pavani.strings_practice;

public class CapitalizeFirstLetter {
    public static String capitalizeWords(String input){
        String output = "";
        for(String s: input.split(" ")){
            s= s.replace(s.charAt(0), Character.toUpperCase(s.charAt(0)));
            output=output + " "+s;
        }
        return output.trim();
    }

    public static void main(String[] args) {
        System.out.println(capitalizeWords("hello world"));
        System.out.println(capitalizeWords("java programming language"));
    }
}