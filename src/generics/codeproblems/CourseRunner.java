package generics.codeproblems;

public class CourseRunner {
    public static void main(String[] args) {
        Course<String, Double> courseGradeMapOne = new Course<>();
        Course<Integer, Double> courseGradeMapTwo = new Course<>();

        System.out.println(courseGradeMapOne.listAllGrades());
        courseGradeMapOne.enrollStudent("student1");
        courseGradeMapOne.enrollStudent("student2");
        courseGradeMapOne.enrollStudent("student3");
        courseGradeMapOne.enrollStudent("student4");
        courseGradeMapOne.enrollStudent("student5");

        courseGradeMapTwo.enrollStudent(1);
        courseGradeMapTwo.enrollStudent(2);
        courseGradeMapTwo.enrollStudent(3);
        courseGradeMapTwo.enrollStudent(4);
        courseGradeMapTwo.enrollStudent(5);

        courseGradeMapOne.assignGrade("student1", 85.00);
        courseGradeMapOne.assignGrade("student2", 72.00);

        courseGradeMapTwo.assignGrade(1, 75.00);

        System.out.println(courseGradeMapOne.listAllGrades());
        System.out.println(courseGradeMapTwo.listAllGrades());
        System.out.println("Retrieve grade: "+courseGradeMapOne.getGrade("student1"));
        System.out.println("Average Grade: "+courseGradeMapOne.averageOfGrades());

    }
}
