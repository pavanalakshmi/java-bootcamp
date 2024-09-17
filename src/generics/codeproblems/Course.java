package generics.codeproblems;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Course <S, G extends Number> {

    Map<S,G> studentToGradeMap;

    public Course() {
        studentToGradeMap = new HashMap<>();
    }

    public void enrollStudent(S studentId){
        if(!studentToGradeMap.containsKey(studentId)){
            studentToGradeMap.put(studentId, null);
        }
    }

    public void assignGrade(S studentId, G grade){
        if(studentToGradeMap.containsKey(studentId)){
            studentToGradeMap.put(studentId, grade);
        } else{
            System.out.println("Student not enrolled in the course.");
        }
    }

    public Optional<G> getGrade(S studentId){
        if(studentToGradeMap.containsKey(studentId)){
            return Optional.of(studentToGradeMap.get(studentId));
        } else{
            return Optional.empty();
        }
        /** Optional is a container object used to represent the possible presence or absence of a value. Here, G represents the type of the grade (for example, Integer, Double, etc.).
         If the value (grade) is present, an Optional containing the value is returned. If the value is absent, an empty Optional is returned.
         This helps to avoid null values and the risk of NullPointerException
         **/
    }

    public Map<S, G> listAllGrades(){
        return new HashMap<>(studentToGradeMap);
    }

    public boolean isStudentEnrolled(S i){
        boolean isEnrolled = true;
        if(!studentToGradeMap.containsKey(i)){
            isEnrolled = false;
        }
        return isEnrolled;
    }

    public Double averageOfGrades(){
        Double average = 0.0;
        for(Map.Entry<S, G> entry: studentToGradeMap.entrySet()){
            if(entry.getValue()!=null){
                average += (entry.getValue()).doubleValue();
            }
        }
        return average/studentToGradeMap.size();
    }
}
