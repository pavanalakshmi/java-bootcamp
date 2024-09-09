package calculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Calculator {
    private static double[] memory = new double[5];
    private static int memoryIndex = 0;
    private static boolean memoryStored = false;

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        System.out.println(calculator.calculate("3 + (2 * (4 - 1))"));
        System.out.println(calculator.calculate("3 + (2 * 4) - 5"));
        System.out.println(calculator.calculate("sqrt(16)"));
        System.out.println(calculator.calculate("2 ^ 3"));
        System.out.println(calculator.calculate("2 ^ 3 M+"));
        System.out.println("Memory: "+calculator.recallAllMemory());
        // 6. Error Handling and Input Validation
        // 5. Memory Functionality

    }

    public void storeInMemory(double value){
        memory[memoryIndex%5] = value;
        memoryStored = true;
        memoryIndex++;
    }
    public double recallMemory(){
        if(!memoryStored){
            System.out.println("Memory empty");
            return 0.0;
        }
        return memory[(memoryIndex-1+5)%5];
    }
    public void clearMemory(){
        memory = new double[5];
        memoryIndex = 0;
        memoryStored=false;
        System.out.println("Memory cleared");
    }
    public String recallAllMemory(){
        if(!memoryStored){
            return "Memory is empty";
        }
        //test
        StringBuilder memoryValues = new StringBuilder();
        for(int i=0;i<Math.min(memoryIndex,5);i++){
            if(i>0){
                memoryValues.append(", ");
            }
            memoryValues.append(memory[i]);
        }
        return memoryValues.toString();
    }

    public double calculate(String expression){
        boolean storeInMemory = expression.endsWith("M+");
        if(storeInMemory){
            expression = expression.replace("M+", "").trim();
        }
        List<String> tokens = tokenize(expression);
        // Handle parentheses first
        tokens = handleParenthesis(tokens);
        // handle sqrt and exponential functions
        tokens = performSqrtAndExponent(tokens);
        // handle multiply and division first
        tokens = performMultiplyAndDivision(tokens);
        // handle subtraction and division
        double result = performSubtractionAndAddition(tokens);

        if(storeInMemory){
            storeInMemory(result);
        }
        return result;
    }

    public List<String> handleParenthesis(List<String> tokens){
        Stack<List<String>> stack = new Stack<>();
        List<String> currentList = new ArrayList<>();

        for(String token: tokens){
            if(token.equals("(")){
                stack.push(currentList);
                currentList = new ArrayList<>();
            }
            else if(token.equals(")")){
                // process the current list and then pop from stack
                List<String> result = performMultiplyAndDivision(currentList);
                double finalresult = performSubtractionAndAddition(result);
                if(!stack.isEmpty()){
                    currentList = stack.pop();
                    currentList.add(String.valueOf(finalresult));
                }
                else{
                    List<String> finalResultAsList = new ArrayList<>();
                    finalResultAsList.add(String.valueOf(finalresult));
                    return finalResultAsList;
                }
            }
            else{
                currentList.add(token);
            }
        }
        return performMultiplyAndDivision(currentList);
    }

    public List<String> performSqrtAndExponent(List<String> tokens){
        List<String> result = new ArrayList<>();
        int i=0;
        while(i<tokens.size()){
            if(tokens.get(i).equals("sqrt")){
                double num = Double.parseDouble(tokens.get(i+1));
                result.add(String.valueOf(Math.sqrt(num)));
                i+=2;
            } else if (tokens.get(i).equals("^")) {
                double base = Double.parseDouble(result.remove(result.size()-1));
                double exponent = Double.parseDouble(tokens.get(i+1));
                result.add(String.valueOf(Math.pow(base,exponent)));
                i+=2;
            }
            else{
                result.add(tokens.get(i));
                i++;
            }
        }
        return result;
    }
    public double performSubtractionAndAddition(List<String> tokens){
        double result = Double.parseDouble(tokens.get(0));
        for(int i=1;i<tokens.size();i+=2){
            String operator = tokens.get(i);
            if(operator.equals("+")){
                result+=Double.parseDouble(tokens.get(i+1));
            }
            else if(operator.equals("-")){
                result-=Double.parseDouble(tokens.get(i+1));
            }
        }
        return result;
    }

    public List<String> performMultiplyAndDivision(List<String> tokens){
        List<String> result = new ArrayList<>();
        int i=0;
        while(i<tokens.size()){
            String token = tokens.get(i);
            if((token.equals("*")) || (token.equals("/"))){
                Double num1 = Double.parseDouble(result.remove(result.size()-1));
                Double num2 = Double.parseDouble(tokens.get(i+1));
                Double calculatedResult = 0.0;

                if(token.equals("*")){
                    calculatedResult = num1*num2;
                }
                else if(token.equals("/")){
                    calculatedResult = num1/num2;
                }
                result.add(String.valueOf(calculatedResult));
                i+=2;
            }
            else{
                result.add(token);
                i++;
            }
        }
        return result;
    }

    public List<String> tokenize(String expression){
        List<String> tokens = new ArrayList<>();
        StringBuilder numbers = new StringBuilder();
        StringBuilder func = new StringBuilder();
        boolean isFunction = false;

        // loop through the given expression as characters
        for(char ch: expression.toCharArray()){
            // if character is a number, append to numbers string.
            if(Character.isDigit(ch) || ch == '.'){
                if(isFunction){
                    func.append(ch);
                }
                else{
                    numbers.append(ch);
                }
            }
            else if (ch == ' '){
                continue;
            }
            else if(Character.isLetter(ch)) {
                isFunction=true;
                func.append(ch);
            }
            else{
                // if char is an operator and numbers is not empty, add the operator to tokens
                if (numbers.length() > 0) {
                    tokens.add(numbers.toString());
                    numbers.setLength(0);
                }
                if(func.length()>0){
                    tokens.add(func.toString());
                    func.setLength(0);
                    isFunction=false;
                }
                tokens.add(Character.toString(ch));
            }
        }
        // Add the last number
        if(numbers.length()>0){
            tokens.add(numbers.toString());
        }
        if(func.length()>0){
            tokens.add(func.toString());
        }
        // the tokens array will be [21, -, 31, *, 22]
//         System.out.println(tokens);
        return tokens;
    }
}
    /**public double calculate(String expression){
        String[] numbers = expression.split("[+-/*]");
        String operator = expression.replaceAll("[0-9]", "");
        int num1 = Integer.parseInt(numbers[0]);
        int num2 = Integer.parseInt(numbers[1]);
        switch (operator){
            case "-" : return num1-num2;
            case "+" : return num1+num2;
            case "/" : if(num2!=0){
                return num1/num2;
            }
            else{
                throw new ArithmeticException("division by zero is not possible");
            }
            case "*" : return num1*num2;
        }
        return 0;
    }
}**/
