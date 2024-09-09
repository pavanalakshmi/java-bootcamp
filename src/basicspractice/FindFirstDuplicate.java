package basicspractice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindFirstDuplicate {
    public static void main(String[] args) {
        List<Integer> dup = new ArrayList<>(Arrays.asList(1, 1, 2, 3, 3, 5, 1));
        List<Integer> duplicate = new ArrayList<>();

        for(int i=0; i<dup.size();i++){
            if(duplicate.contains(dup.get(i))){
                System.out.println("First duplicate: "+dup.get(i));
                break;
            }
            else{
                duplicate.add(dup.get(i));
            }
        }
    }
}
