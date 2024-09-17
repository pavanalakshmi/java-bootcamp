package generics.codeproblems;

import java.util.ArrayList;
import java.util.List;

public class GradeBook <T extends Number> {
    public List<T> gradeBook;

    public GradeBook() {
        this.gradeBook = new ArrayList<>();
    }

    public void addGrade(T grade){
        gradeBook.add(grade);
    }

    public int getNumberOfGrades(){
        return gradeBook.size();
    }

    public boolean removeGrade(T grade){
        if(gradeBook.contains(grade)){
            gradeBook.remove(grade);
            return true;
        }
        return false;
    }

    public String calculateAverage(){
        double sum = 0;
        String msg = "";
        if(gradeBook.isEmpty()){
            msg = "Gradebook is empty to calculate average";
        } else{
            for(T grade: gradeBook){
                sum += grade.doubleValue();
            }
            msg = "Average grade: "+ sum/gradeBook.size();
        }
        return msg;
    }

    public String findHighestGrade(){
        String msg = "";
        Double highestGrade = null;
        if(gradeBook.isEmpty()){
            msg = "Gradebook is empty to find the highest grade";
        } else{
            highestGrade = gradeBook.get(0).doubleValue();
            for(T grade: gradeBook){
                if(grade.doubleValue() > highestGrade){
                    highestGrade = grade.doubleValue();
                }
            }
            msg = "Highest grade: "+highestGrade;
        }
        return msg;
    }

    public String findLowestGrade(){
        String msg = "";
        Double lowestGrade = null;
        if(gradeBook.isEmpty()){
            msg = "Gradebook is empty to find the lowest grade";
        } else{
            lowestGrade = gradeBook.get(0).doubleValue();
            for(T grade: gradeBook){
                if(grade.doubleValue() < lowestGrade){
                    lowestGrade = grade.doubleValue();
                }
            }
            msg = "Lowest grade: "+ lowestGrade;
        }
        return msg;
    }

    @Override
    public String toString() {
        return gradeBook.toString(); //"GradeBook: " +
//                + "\n" +
//                calculateAverage() + "\n" +
//                findHighestGrade() + "\n" +
//                findLowestGrade();
    }
}
