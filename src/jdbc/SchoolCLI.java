package jdbc;

import java.util.Scanner;

// CLI - command line interface for enrolling students
public class SchoolCLI {
    private static final School school = new School();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the School Management System");

        while (true){
            System.out.println("> ");
            String input = scanner.nextLine();
            String[] command = input.split(" ");

            switch (command[0]){
                case "add_course":
                    school.addCourse(command[1]);
                    break;
                case "enroll_student":
                    school.enrollStudent(Integer.parseInt(command[1]), command[2], command[3]);
                    break;
                case "assign_grade":
                    school.assignGrade(Integer.parseInt(command[1]), command[2], Double.parseDouble(command[3]));
                    break;
                case "exit":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Unknown command");
            }
        }
    }
}
