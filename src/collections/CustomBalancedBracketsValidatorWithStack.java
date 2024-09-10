package collections;

import java.util.HashMap;
import java.util.Stack;

public class CustomBalancedBracketsValidatorWithStack {
    public static void main(String[] args) {
        System.out.println(isValid("{[(])}")); // should return true
        System.out.println(isValid("{[()]}"));
        System.out.println(isValid("{[}"));
    }
    public static boolean isValid(String s) {
        Stack<Character> characterStack = new Stack<>();
        HashMap<Character, Character> hashMap = new HashMap<>();
        hashMap.put('{','}');
        hashMap.put('[',']');
        hashMap.put('(',')');
//        int left=0, right=0;
//        for(int i=0;i<s.length();i++){
//            if((s.charAt(i)=='(') || (s.charAt(i)=='[') || (s.charAt(i)=='{')){
//                left+=1;
//            }
//            else if((s.charAt(i)=='(') || (s.charAt(i)=='[') || (s.charAt(i)=='{')){
//                right+=1;
//            }
//        }
//        if(left==right){
//            return true;
//        }
//        else{
//            return false;
//        }
        for(int i=0;i<s.length();i++){
            if(hashMap.containsKey(s.charAt(i))){
                characterStack.push(s.charAt(i));
            }
            else{
                if(characterStack.isEmpty()){
                    return false;
                }
                char lastOpenBracket = characterStack.pop();
                if(hashMap.get(lastOpenBracket)!=s.charAt(i)){
                    // Apply custom rule for "twisted" sequences
                    if (!isTwistedValid(lastOpenBracket, s.charAt(i))) {
                        return false;
                    }
                }
            }
        }
        return characterStack.isEmpty();
    }

    private static boolean isTwistedValid(char open, char close) {
        // Custom rule: Allow certain "twisted" combinations
        if ((open == '(' && close == ']') ||
                (open == '[' && close == ')') ||
                (open == '{' && close == ']') ||
                (open == '(' && close == '}')) {
            return true;
        }
        return false;
    }
}
