package basicspractice;

public class Calculator {
    public static void main(String[] args) {
        String op = "5/0";
//        if(hasOperator(op)){
//            System.out.println(calculate(op));
//        }
//        else{
//            System.out.println("Enter valid operator.");
//        }
        System.out.println(calculate(op));

    }

    public int add(int x, int y){
        return x+y;
    }

    public static int calculate(String op) {
        String[] parts = op.split("[\\+\\-\\*/]");
        int val1 = Integer.parseInt(parts[0]);
        int val2 = Integer.parseInt(parts[1]);

        if (op.contains("+")) {
            return val1 + val2;
        } else if (op.contains("-")) {
            return val1 - val2;
        } else if (op.contains("*")) {
            return val1 * val2;
        } else if (op.contains("/")) {
            if (val2 == 0) {
                System.out.println("Division by zero error");
            } else {
                return val1 / val2;
            }
        }
        else{
        System.out.println("Invalid operation");
        return Integer.MIN_VALUE;
        }
        return 0;
    }
}

//    public static boolean hasOperator(String s){
////        if(s.matches(".*[+\\-*/=%].*)")
//        if(s.matches(".*[\\-+*/].*")){
//            return true;
//        }
//        else{
//            return false;
//        }
//
//    }
