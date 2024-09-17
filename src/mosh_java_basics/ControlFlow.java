package mosh_java_basics;

public class ControlFlow {
    public static void main(String[] args) {
        /**
        int x = 1;
        int y = 1;
        System.out.println(x==y); //Equality

        int temperature = 22;
        boolean isWarm = temperature > 20 && temperature<30; //logical AND
        System.out.println(isWarm);

        boolean hasHighIncome = true;
        boolean hasGoodCredit = true;
        boolean isEligible = hasGoodCredit || hasHighIncome; // Logical OR
        System.out.println(isEligible);

        boolean hasCriminalRecord = false;
        boolean isEligible1 = (hasGoodCredit || hasHighIncome) && !hasCriminalRecord;
        System.out.println(isEligible1);
         **/

        int temperature = 32;
        if(temperature > 30) {
            System.out.println("It's a hot day");
            System.out.println("Drink Water");
        }
        else if(temperature>20) //&& temperature<=30 - not required as it comes to this block omly if its <30
            System.out.println("Beautiful day");
        else
            System.out.println("Cold day");
        // This if statement has 3 clauses - if, else-if, else

//        SIMPLIFYING IF STATEMENTS
        int income = 120_000;
//        boolean hasHighIncome;
//        if(income > 100_000)
//            hasHighIncome = true;
//        else
//            hasHighIncome = false;
        // simplifying below

        boolean hasHighIncome = (income > 100_000);

//        TERNERY OPERATOR ?:
//        String className;
//        if (income > 100_000)
//            className = "First";
//        else
//            className = "Economy";

        String className = income > 100_000 ? "First" : "Economy";

        // SWITCH STATEMENTS
        String role = "admin";
//        if(role == "admin")
//            System.out.println("You're an admin");
//        else if (role == "moderator")
//            System.out.println("You're a moderator");
//        else System.out.println("You're a guest");

        switch (role){
            case "admin":
                System.out.println("You're an admin");
                break;
            case "moderator":
                System.out.println("You're a moderator");
                break;
            default:
                System.out.println("You're a guest");
        }

    }

}
