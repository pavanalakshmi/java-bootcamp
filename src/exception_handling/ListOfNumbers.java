package exception_handling;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ListOfNumbers {
    private static List<Integer> list;
    private static final int SIZE = 10;

    public ListOfNumbers() {
        list = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            list.add(i);
        }
    }

    public void writeList() {
//        PrintWriter out = new PrintWriter(new FileWriter("OutputFile.txt")); //throws compile time error
        PrintWriter out = null;
        try {
            FileWriter f = new FileWriter("OutputFile.txt");
            out = new PrintWriter(f);
            for (int i = 0; i < SIZE; i++) {
                out.println("Value at: " + i + " = " + list.get(i));
            }
//            out.close();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("IndexOutOfBoundsException: " + e.getMessage());
        }
//        catch (Throwable throwable){
//            System.err.println("IndexOutOfBoundsException: "+throwable.getMessage());
//        } // alternative for above catch block
        catch (IOException e) {
            System.err.println("Caught IOException: " + e.getMessage());
        }
//        catch (IOException | IndexOutOfBoundsException e){
//            In multiple exceptions, e is final and we cannot assign any value to it inside the catch block.
//            System.err.println("Exception caught "+e.getMessage());
//        } //can merge above two into this
        finally {
            if (out != null) {
                System.out.println("Closing PrintWriter");
                out.close();
            } else {
                System.out.println("PrintWriter not open");
            }
//            if(f!=null){
//                System.out.println("Closing FileWriter");
//                f.close();
//            }
        }
    }

    // alternate to finally - try with resources
    public static void writeList1() {
        try(FileWriter f = new FileWriter("OutputFile.txt");
            PrintWriter out = new PrintWriter(f)){
            for (int i = 0; i < SIZE; i++) {
                out.println("Value at: " + i + " = " + list.get(i));
            }
        } catch (IOException | IndexOutOfBoundsException e) {
            System.err.println("Exception caught: " + e.getMessage());
        }
    }


    public static String ReadFile() {
        try(FileReader f = new FileReader("OutputFile.txt");
            BufferedReader br = new BufferedReader(f)){
            return br.readLine();
        } catch (IOException | IndexOutOfBoundsException e) {
            System.err.println("Exception caught: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        new ListOfNumbers();
        writeList1();
        System.out.println(ReadFile());
    }
}


