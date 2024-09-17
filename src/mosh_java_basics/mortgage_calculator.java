package mosh_java_basics;

import java.text.NumberFormat;
import java.util.Scanner;

public class mortgage_calculator {
    public static void main(String[] args) {
        final byte MONTHS_IN_YEAR = 12;
        final byte PERCENT = 100;
        int principal;
        double annual_interest_rate;
        int years;
        double monthly_interest;
        int numberOfPayments;

        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.print("Enter Principal($1K - $1M): ");
            principal = scanner.nextInt();
            if(principal<1000 || principal> 1_000_000)
                System.out.println("Enter a number between 1,000 and 1,000,000.");
            else
                break;
        }
        while(true){
            System.out.print("Annual Interest rate: ");
            annual_interest_rate = scanner.nextDouble();
            if(annual_interest_rate<1 || annual_interest_rate>30)
                System.out.println("Enter a value greater than 1 and less than or equal to 30.");
            else {
                monthly_interest = annual_interest_rate / PERCENT / MONTHS_IN_YEAR;
                break;
            }
        }
        while(true){
            System.out.print("Period (Years): ");
            years = scanner.nextInt();
            if(years <1 || years>30)
                System.out.println("Enter a value between 1 and 30");
            else{
                numberOfPayments = years * MONTHS_IN_YEAR; // number of months
                break;
            }
        }

        double mortgage = (principal * ((monthly_interest*(Math.pow(1+monthly_interest, numberOfPayments)))/(Math.pow(1+monthly_interest, numberOfPayments)-1)));
        System.out.println("Mortgage: "+NumberFormat.getCurrencyInstance().format(mortgage));
    }
}

//Mortgage
//        M = P (r((1+r)^n)/((1+r)^n)-1)
//



