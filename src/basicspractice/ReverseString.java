package basicspractice;

public class ReverseString {
    public static void main(String[] args) {
        String s = "apple";
        String temp = "";

        for(int i=0;i<s.length();i++){
            temp = s.charAt(i) + temp;
        }
        System.out.println(temp);
    }
}
