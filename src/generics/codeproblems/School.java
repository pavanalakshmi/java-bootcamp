package generics.codeproblems;

import java.util.*;

public class School<S, G extends Number> {
    private static final String ADD_COURSE = "add_course";
    private static final String ENROLL_STUDENT = "enroll_student";
    private static final String ASSIGN_GRADE = "assign_grade";
    private static final String LIST_GRADES = "list_grades";
    private static final String LIST_COURSES = "list_courses";
    private static final String REPORT_UNIQUE_COURSES = "report_unique_courses";
    private static final String REPORT_UNIQUE_STUDENTS = "report_unique_students";
    private static final String REPORT_AVERAGE_SCORE = "report_average_score";
    private static final String REPORT_CUMULATIVE_AVERAGE = "report_cumulative_average";
    private static final String EXIT = "exit";

    private final List<String> commands = Arrays.asList(ADD_COURSE, ENROLL_STUDENT, ASSIGN_GRADE, LIST_GRADES, LIST_COURSES,
            REPORT_UNIQUE_COURSES, REPORT_UNIQUE_STUDENTS, REPORT_AVERAGE_SCORE, REPORT_CUMULATIVE_AVERAGE, EXIT);

    Map<String, Course<S,G>> schoolCourseMap;

    public School() {
        schoolCourseMap = new HashMap<>();
    }

    public void addCourse(String courseName){
        schoolCourseMap.put(courseName, new Course<>());
        System.out.println("Course "+courseName+" added.");
    }

    public void enrollStudent(String courseName, S studentId){
        if(schoolCourseMap.containsKey(courseName)){
            schoolCourseMap.get(courseName).enrollStudent(studentId);
            System.out.println("Student "+studentId+" enrolled in "+courseName);
        }
        else{
            System.out.println("Error: Cannot enroll student. Course "+courseName+" does not exist.");
        }
    }

    public void assignGrade(String courseName, S studentId, G grade){
        if(schoolCourseMap.containsKey(courseName)){
            Course<S, G> course = schoolCourseMap.get(courseName);
            if(course.getAllGrades().containsKey(studentId)){
                course.assignGrade(studentId, grade);
                System.out.println("Grade "+grade+" assigned to student "+studentId+" in course "+courseName);
            } else{
                System.out.println("Error: Cannot assign grade. Student "+studentId+" is not enrolled in course "+courseName);
            }
        } else{
            System.out.println("Course "+courseName+" does not exist.");
        }
    }

    public Map<S, G> listGrades(String courseName){
        if(schoolCourseMap.containsKey(courseName)){
            Course<S, G> course = schoolCourseMap.get(courseName);
            Map<S, G> grades = course.listAllGrades();

            if(grades.isEmpty()){
                System.out.println("No grades available for course "+courseName);
            } else{
                for(Map.Entry<S, G> entry : grades.entrySet()){
                    System.out.println("Student: "+entry.getKey()+", Grade: "+entry.getValue());
                }
            }
            return grades;
        } else{
            System.out.println("Course "+courseName+" does not exist.");
            return Collections.emptyMap();
        }
    }

    public void listCourses(){
        System.out.println("Courses offered: ");
        Set<String> courseNames = schoolCourseMap.keySet();
        for(String course:courseNames){
            System.out.println(course);
        }
    }

    @Override
    public String toString() {
        return schoolCourseMap.toString();
    }

    public void reportUniqueCourses(){
        Set<String> courses = schoolCourseMap.keySet();
        System.out.println("Courses offered: ");
        for(String course:courses){
            System.out.println(course);
        }
    }

    public void reportUniqueStudents(){
        Collection<Course<S, G>> students = schoolCourseMap.values();
        Set<S> studentIds = new HashSet<>();
        for(Course<S,G> course: students){
            studentIds.addAll(course.getAllGrades().keySet());
        }
        System.out.println("Unique Students enrolled: ");
        for(S studentId: studentIds){
            System.out.println(studentId);
        }
    }

    public void reportAverageScore(String courseName){
        double average = schoolCourseMap.get(courseName).averageOfGrades();
        System.out.println("Average score for course: "+courseName+": "+average);
    }

    public void reportCumulativeAverage(S studentId){
        Collection<Course<S, G>> courses = schoolCourseMap.values();
        double total =0.0;
        int count = 0;

        for (Course<S, G> course : courses) {
            Optional<G> optionalGrade = course.getGrade(studentId);
            if (optionalGrade.isPresent()) {
                total += optionalGrade.get().doubleValue();
                count++;
            }
        }
        if (count != 0) {
            double average = total / count;
            System.out.println("Cumulative average score for student '" + studentId + "': " + String.format("%.1f", average));
        }
    }

    public void processCommand(String commandString){
        String[] command = commandString.split(" ");
        if(commands.contains(command[0])){
            switch (command[0]){
                case ADD_COURSE:
                    addCourse(command[1]);
                    break;
                case ENROLL_STUDENT:
                    enrollStudent(command[1], (S) command[2]);
                    break;
                case ASSIGN_GRADE:
                    assignGrade(command[1], (S) command[2], (G) Double.valueOf(command[3]));
                    break;
                case LIST_GRADES:
                    listGrades(command[1]);
                    break;
                case LIST_COURSES:
                    listCourses();
                    break;
                case REPORT_UNIQUE_COURSES:
                    reportUniqueCourses();
                    break;
                case REPORT_UNIQUE_STUDENTS:
                    reportUniqueStudents();
                    break;
                case REPORT_AVERAGE_SCORE:
                    reportAverageScore(command[1]);
                    break;
                case REPORT_CUMULATIVE_AVERAGE:
                    reportCumulativeAverage((S) command[1]);
                    break;
            }
        } else{
            System.out.println("Error: Unknown command 'unknown_command'. Please use one of the following commands: add_course, enroll_student, assign_grade, list_grades, list_courses, report_unique_courses, report_unique_students, report_average_score, report_cumulative_average.");
        }
    }

    public List<String> getListOfCommands(String commandString){
        return commands;
    }

    public static void printHelperCommands(){
       /** String menu = """
                - add_course <course_name>    ->	Adds a new course to the school.
                - list_courses    ->	Lists all courses offered by the school.
                - enroll_student <course_name> <id>   ->	Enrolls a student in the specified course.
                - assign_grade <course_name> <id> <g> ->	Assigns a grade to the student in the specified course.
                - list_grades <course_name>   ->	Lists all students and their grades for the specified course.
                - report_unique_courses   ->	Lists all unique courses offered by the school.
                - report_unique_students  ->	Lists all unique students enrolled in the school.
                - report_average_score <course_name>  ->	Reports the average score of the specified course.
                - report_cumulative_average <id>  ->	Reports the cumulative average score for the specified student.
                """;
        **/
        String menu = """
                - add_course <course_name>
                - list_courses
                - enroll_student <course_name> <id>
                - assign_grade <course_name> <id> <g>
                - list_grades <course_name>
                - report_unique_courses
                - report_unique_students
                - report_average_score <course_name>
                - report_cumulative_average <id>
                """;
        System.out.println(menu);
    }

    public static void main(String[] args) {
        School<String, Double> sriKrishnaveni = new School<>();

        sriKrishnaveni.processCommand("add_course AMOD_5210");
        sriKrishnaveni.processCommand("enroll_student AMOD_5210 5B1");
        sriKrishnaveni.processCommand("assign_grade AMOD_5210 5B1 86");

        Scanner scanner = new Scanner(System.in);
        List<String> commands = sriKrishnaveni.commands;
        String commandString;

        do {
            System.out.println("********************* School ******************* \n");
            System.out.println("Please enter any command in below format: ");
            printHelperCommands();
            commandString = scanner.nextLine().trim();
            sriKrishnaveni.processCommand(commandString);
        }
        while(!commandString.equals(commands.get(commands.size()-1))); //exit
    }
}
