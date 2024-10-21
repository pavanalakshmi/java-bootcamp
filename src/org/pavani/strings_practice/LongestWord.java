package org.pavani.strings_practice;

public class LongestWord {
    public static void main(String[] args) {
        System.out.println(findLongestWord("I love programming in Java"));
        System.out.println(findLongestWord("The quick brown fox jumps over the lazy dog"));
    }

    public static String findLongestWord(String input){
        int count = 0;
        String output = "";

        for(String s: input.split(" ")){
            if(s.length()>count){
                output = s;
                count = s.length();
            }
        }
        return output;
    }
}