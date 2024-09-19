package mosh_java_basics.exceptions;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MultipleExceptions {
    public static void show() {
        FileReader reader = null; //cant user var here, because we can use var only when we initialize with FileReader.
        try{
            reader = new FileReader("file.txt");
            var value = reader.read();
            new SimpleDateFormat().parse("");
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
        } catch (IOException | ParseException e) {
            System.out.println("Could not read data.");
//        } catch (ParseException e) {
//            e.printStackTrace();
        }
        finally {
            if(reader!=null){
                try {
                    reader.close(); //only accessible in try block
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
