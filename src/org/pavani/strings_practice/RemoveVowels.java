package org.pavani.strings_practice;

public class RemoveVowels {
    public static void main(String[] args) {
        System.out.println(removeVowels("Hello World"));
        System.out.println(removeVowels("Java Programming"));
    }
    public static String removeVowels(String input){
        String output = "";
        for(String s: input.split("")){
            if(!isVowel(s)){
                output+=s;
            }
        }
        return output;
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
