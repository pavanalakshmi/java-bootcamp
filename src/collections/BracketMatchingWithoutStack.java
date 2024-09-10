package collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BracketMatchingWithoutStack {
    public static void main(String[] args) {
        System.out.println(isValid("{[()]}"));
        System.out.println(isValid("{[(])}"));
        System.out.println(isValid("[]"));
    }
    public static boolean isValid(String s) {
        List<Character> characterStack = new ArrayList<>();
        HashMap<Character, Character> hashMap = new HashMap<>();
        hashMap.put('{','}');
        hashMap.put('[',']');
        hashMap.put('(',')');

        for(int i=0;i<s.length();i++){
            if(hashMap.containsKey(s.charAt(i))){
                characterStack.add(s.charAt(i));
            }
            else{
                if(characterStack.isEmpty() || (hashMap.get(characterStack.remove(characterStack.size()-1))!=s.charAt(i))){
                    return false;
                }
            }
        }
        return characterStack.isEmpty();
    }
}
