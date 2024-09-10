package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class School {
    public void addCourse(String courseName){
        String query = "INSERT INTO Courses (course_name) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1, courseName);
            stmt.executeUpdate();
            System.out.println("Course '"+courseName+"' added.");
        } catch (SQLException e) {
            System.out.println("Error adding course: "+e.getMessage());
        }
    }

    public void enrollStudent(int studentId, String studentName, String courseName){
        String courseQuery = "SELECT course_id FROM Courses WHERE course_name = ?";
        String studentQuery = "SELECT student_id FROM Students WHERE student_id = ?";
        String addStudentQuery = "INSERT INTO Students (student_id, student_name) VALUES (?, ?)";
        String enrollQuery = "INSERT INTO Enrollments (course_id, student_id) VALUES (?, ?)";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement courseStatement = connection.prepareStatement(courseQuery);
            PreparedStatement studentStatement = connection.prepareStatement(studentQuery);
            PreparedStatement addStudentStatement = connection.prepareStatement(addStudentQuery);
            PreparedStatement enrollStatement = connection.prepareStatement(enrollQuery)){

            // check if course exists
            courseStatement.setString(1, courseName);
            ResultSet courseRS = courseStatement.executeQuery();

            if(!courseRS.next()){
                System.out.println("Error: Course '"+courseName+"' does not exist.");
                return;
            }
            int courseId = courseRS.getInt("course_id");

            // Check if student exists in Students table
            studentStatement.setInt(1, studentId);
            ResultSet studentRS = studentStatement.executeQuery();

            if(!studentRS.next()){
                // if student doesn't exist, add student
                addStudentStatement.setInt(1, studentId);
                addStudentStatement.setString(2, studentName);
                addStudentStatement.executeUpdate();
                System.out.println("Student '"+studentName+"' (ID: "+studentId+") added to the Students table");
            }

            // enroll student in course
            enrollStatement.setInt(1, courseId);
            enrollStatement.setInt(2, studentId);
            enrollStatement.executeUpdate();
            System.out.println("Student '"+studentId+"' enrolled in course '"+courseName+"'.");
        }catch (SQLException e) {
            System.out.println("Error enrolling student: "+e.getMessage());
        }
    }

    // assign grade
    public void assignGrade(int studentId, String courseName, double grade){
        String courseQuery = "SELECT course_id FROM Courses WHERE course_name = ?";
        String gradeQuery = "INSERT INTO Grades (course_id, student_id, grade) VALUES (?, ?, ?)";

        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement courseStatement = connection.prepareStatement(courseQuery);
            PreparedStatement gradeStatement = connection.prepareStatement(gradeQuery)){
            // Fetch course ID
            courseStatement.setString(1, courseName);
            ResultSet RS = courseStatement.executeQuery();
            if(RS.next()){
                int courseID = RS.getInt("course_id");

                // assign grade
                gradeStatement.setInt(1, courseID);
                gradeStatement.setInt(2, studentId);
                gradeStatement.setDouble(3, grade);
                gradeStatement.executeUpdate();
                System.out.println("Grade '"+grade+"' assigned to student '"+ studentId+"' in course '"+courseName+"'.");
            }
            else{
                System.out.println("Error: Course '"+courseName+"' does not exist");
            }
        }
        catch (SQLException e){
            System.out.println("Error assigning grade: "+e.getMessage());
        }
    }
}
