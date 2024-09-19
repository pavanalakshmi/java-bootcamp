package exception_handling;

import java.util.HashMap;
import java.util.Map;


class StudentAlreadyEnrolledException extends Exception{
    public StudentAlreadyEnrolledException(String studentId) {
        super("Error: Student "+studentId+" is already enrolled in the course");
    }
}

class StudentNotFoundException extends Exception{
    public StudentNotFoundException(String studentId) {
        super("Error: Student "+studentId+" is not enrolled in the course");
    }
}
class GradeNotAssignedException extends Exception{
    public GradeNotAssignedException(String studentId) {
        super("Error: Grade not assigned for student: "+studentId+".");
    }
}

// we cant add custom exception classes inside a generic class.

// Generic course class
public class Course<S,G extends Number> {
    S studentId;
    G grade;
    Map<S, G> studentGrades;

    public Course() {
        studentGrades = new HashMap<>();
    }


    // enroll student
    public void enrollStudent(S studentId) throws StudentAlreadyEnrolledException {
        if(studentGrades.containsKey(studentId)){
            throw new StudentAlreadyEnrolledException(studentId.toString());
        }
        studentGrades.put(studentId, null); //no grades initially
    }

    // assign grade
    public void assignGrade(S studentId, G grade) throws StudentNotFoundException {
        if(!studentGrades.containsKey(studentId)){
            throw new StudentNotFoundException(studentId.toString());
        }
        studentGrades.put(studentId, grade);
    }

    // retrieve grade
    public G getGrade(S studentId) throws StudentNotFoundException, GradeNotAssignedException {
        if(!studentGrades.containsKey(studentId)){
            throw new StudentNotFoundException(studentId.toString());
        }
        G grade = studentGrades.get(studentId);
        if(grade == null){
            throw new GradeNotAssignedException(studentId.toString());
        }
        return grade;
    }

    // list all grades and students
    public Map<S,G> getAllGrades(){
        return new HashMap<>(studentGrades);
    }

    public static void main(String[] args) throws StudentAlreadyEnrolledException, StudentNotFoundException, GradeNotAssignedException {
        Course<String, Double> courseGradeMapOne = new Course<>();

        System.out.println(courseGradeMapOne.getAllGrades());

        courseGradeMapOne.enrollStudent("student1");
        courseGradeMapOne.enrollStudent("student2");
        courseGradeMapOne.enrollStudent("student3");
        courseGradeMapOne.enrollStudent("student4");
        courseGradeMapOne.enrollStudent("student5");

        courseGradeMapOne.assignGrade("student1", 85.00);
        courseGradeMapOne.assignGrade("student2", 72.00);

        System.out.println(courseGradeMapOne.getAllGrades());
        System.out.println("Retrieve grade: "+courseGradeMapOne.getGrade("student1"));

        courseGradeMapOne.enrollStudent("student5");

    }
}
