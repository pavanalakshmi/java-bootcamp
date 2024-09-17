package mosh_java_basics;

import java.util.Scanner;

public class Fundamentals {
    public static void main(String[] args) {
        /** System.out.println("Hello World");
        byte age = 30;
        long viewsCount = 123_456_789L;
        float price = 10.99F;
        char letter = 'A';
        boolean isEligible = true;

        Date now = new Date(); // now is an instance of date class.
        now.getTime();
        System.out.println(now);

        byte x=1;
        byte y = x;

        Point point1 = new Point(1,1);//(x,y)
        Point point2 = point1;
        point1.x = 2;
        System.out.println(point2);

        String message = "Hello World" + "!!";
        System.out.println(message);
        System.out.println(message.endsWith("!!"));
        int len =  message.length();
        System.out.println("Index of W is: "+message.indexOf('W'));
        System.out.println("Index of sky is: "+message.indexOf("sky"));
        message.replace("!", "*"); // doesn't replace original string, as strings are immutable
        System.out.println(message.toLowerCase());

        String PL = "Hello \"Pavani\"";
        System.out.println(PL);

        String path = "c:\\windows";
        System.out.println(path);
        System.out.println("c:\nwindows");
        System.out.println("c:\twindows");


        int[] numbers = new int[5];
        numbers[0] = 1;
//        numbers[10] = 3; // Exception - errors.
        // other items are initialized to 0 as it is an integer array.
        System.out.println(numbers); //address of the object in memory.
        System.out.println(Arrays.toString(numbers)); //[1,0,0,0,0] can pass toFloat, toInt anything - method overloading

        // alternative for array initialization.
        int[] num = {2,3,1,4,5};
        System.out.println(num.length);
        Arrays.sort(num);
        System.out.println(Arrays.toString(num));

        int[][] matrix = new int[2][2];
        matrix[0][0] = 1;
        System.out.println(Arrays.toString(matrix)); //returns reference as it is multi-dimensional array.
        System.out.println(Arrays.deepToString(matrix)); // to print multi-dimensional array.

        int[][][] threeDArray = new int[3][3][3];
        int[][][] threeD = {{{1,2,3},{4,5,6},{7,8,9}}};
        System.out.println(Arrays.deepToString(threeD));

        final float pi = 3.14f;
//        pi = 1; //throws error



        double result = (double)10 / 3; //casting
        System.out.println(result);

        int x = 2;
        int y=2;
        System.out.println(x++);
        System.out.println(++y);



        short x=1;
        int y=x+2;
        System.out.println(y);

        double m = 1.1;
        double n = m+2; // java automatically converts 2 into 2.0

        int p = (int)m + 2; //EXPLICIT CASTING - can only happen between compatible types i.e., between numbers
        // cant convert number into strings., so we use WRAPPER CLASSES.

        String s = "1";
        Integer.parseInt(s);

        int n = Math.round(1.1f);
        System.out.println(n);
        int y = (int)Math.ceil(1.1f);
        System.out.println(y);

        System.out.println(Math.round(Math.random()*100));
        int n1 = (int) Math.random() * 100; //always returns 0 as we are converting to int beforehand and then using *100


//        NumberFormat currency = new NumberFormat(); //abstract, cant be instantiated error
        NumberFormat currency = NumberFormat.getCurrencyInstance(); // this is a factory method, so we use this

        String result = currency.format(1234567.890);
        System.out.println(result);

        NumberFormat percent = NumberFormat.getPercentInstance();
        String out = percent.format(0.1);
        System.out.println(out);

        // METHOD CHAINING - chaining multiple methods
        String out1 = NumberFormat.getPercentInstance().format(0.25);

         **/

        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter age: "); // gets input from new line because of println method
        System.out.print("Age: ");
        byte age = scanner.nextByte();
        System.out.println("Your age: "+age); //implicit casting number is added to string

        String name = scanner.next(); //only gets first token even if we enter multiple words.
        String name1 = scanner.nextLine();
        System.out.println(name1.trim());
    }
}
