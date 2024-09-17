package generics.codeproblems;

public class GradeBookRunner {
    public static void main(String[] args) {
        GradeBook<Integer> gradeBookIntegerList = new GradeBook<>();
        gradeBookIntegerList.addGrade(86);
        gradeBookIntegerList.addGrade(75);
        gradeBookIntegerList.addGrade(62);
        gradeBookIntegerList.addGrade(73);
        gradeBookIntegerList.addGrade(80);

        GradeBook<Double> gradeBookDoubleList = new GradeBook<>();
        gradeBookDoubleList.addGrade(86.12);
        gradeBookDoubleList.addGrade(62.76);
        gradeBookDoubleList.addGrade(77.17);
        gradeBookDoubleList.addGrade(81.12);
        gradeBookDoubleList.addGrade(60.00);

        System.out.println("Double GradeBook: "+gradeBookDoubleList);
        System.out.println("Remove Grade: "+gradeBookDoubleList.removeGrade(60.00));
        System.out.println(gradeBookDoubleList);
        System.out.println(gradeBookDoubleList.calculateAverage()); //"Average Grade: "+
        System.out.println(gradeBookDoubleList.findLowestGrade()); //"Lowest Grade: "+
        System.out.println(gradeBookDoubleList.findHighestGrade()); //"Highest Grade: "+

        System.out.println("Integer GradeBook: "+gradeBookIntegerList);
        System.out.println("Remove Grade: "+gradeBookIntegerList.removeGrade(71));
        System.out.println(gradeBookIntegerList);
        System.out.println(gradeBookIntegerList.calculateAverage());
        System.out.println(gradeBookIntegerList.findLowestGrade());
        System.out.println(gradeBookIntegerList.findHighestGrade());

    }
}
