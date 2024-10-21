package org.pavani.strings_practice;

public class CountVowels {
    public static void main(String[] args) {
        System.out.println(countVowels("Hello World"));
        System.out.println(countVowels("Java Programming"));
        System.out.println(countVowels("AeiOu"));
        System.out.println(countVowels(""));
    }

    public static int countVowels(String input){
        int count = 0;
        for(String s: input.split("")){
            if(isVowel(s)){
                count++;
            }
        }
        return count;
    }

    public static boolean isVowel(String str){
        String[] vowels = new String[]{"a","e","i","o","u"};
        for(String s: vowels){
            if(s.equalsIgnoreCase(str)){
                return true;
            }
        }
        return false;
    }
}
