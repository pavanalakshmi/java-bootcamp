### **Problem 1: Generic Method to Convert List to Array**

**Objective**:  
Write a generic method `listToArray` that converts a `List<T>` to an array of type `T[]`.

`public static <T> T[] listToArray(List<T> list)`

### **Problem 2: Generic Method to Count Occurrences**

**Objective**:  
Write a generic method `countOccurrences` that counts the number of times a specific element appears in an array. The method should work with an array of any type.

`public static <T> int countOccurrences(T[] array, T element)`



### **Problem 3: Generic GradeBook Class**

#### **Objective:**
Design and implement a generic class `GradeBook` that is capable of storing and managing the grades of students. The `GradeBook` class should be versatile, allowing it to work with any numeric type, such as `Integer`, `Double`, `Float`, etc. This class will provide essential functionalities like adding grades, calculating the average grade, and determining the highest and lowest grades.

#### **Requirements:**

1. **Generic Design:**
   - The `GradeBook` class should be designed using generics to handle any numeric type that extends the `Number` class (e.g., `Integer`, `Double`).
   - The class should ensure type safety by working with these numeric types and performing necessary calculations without relying on specific numeric classes.

2. **Core Functionalities:**
   - **Adding Grades**: The class should support adding individual grades of type `T` to the grade book.
   - **Calculating the Average**: The class should provide a method to calculate and return the average of all the grades stored in the grade book. This average should be returned as a `double` value.
   - **Finding the Highest Grade**: The class should offer a method to find and return the highest grade in the grade book.
   - **Finding the Lowest Grade**: Similarly, the class should provide a method to find and return the lowest grade in the grade book.

3. **Edge Cases Handling:**
   - The class should handle scenarios where no grades have been added (e.g., when calculating the average or finding the highest/lowest grade).
   - It should also be designed to handle cases where grades are added or removed, ensuring that the methods reflect the current state of the grade book.

4. **Ease of Use:**
   - The class should be easy to use for a variety of numeric types without requiring users to implement specific logic for different types.
   - It should provide clear and intuitive methods that can be easily integrated into broader applications, such as student management systems or educational software.

5. **Scalability:**
   - The design should allow the `GradeBook` class to scale well with a large number of grades, efficiently performing calculations and maintaining the integrity of the stored data.

#### **Scenario Example:**

Imagine a scenario where a teacher needs to manage the grades for a class of students. The grades could be in different formats, such as percentages (e.g., 85%, 90%) or GPA scores (e.g., 3.5, 4.0). The teacher wants to store these grades in a `GradeBook` and perform various operations, such as finding the class average, identifying the top-performing student, and recognizing students who might need extra help based on the lowest grades.

By implementing the `GradeBook` class with generics, the teacher can easily manage these grades regardless of their numeric type, ensuring that the class can be used in diverse educational contexts.

#### **Outcome:**

Students should be able to design the `GradeBook` class that can:

- Store grades of a generic numeric type.
- Perform operations like calculating averages and finding the highest and lowest grades.
- Handle edge cases appropriately.
- Offer a user-friendly interface for managing student grades in various educational applications.



### **Problem 4: Generic Course Class**

#### **Objective:**
Design and implement a generic class `Course` that can store and manage information about a course, including the students enrolled and their corresponding grades. The `Course` class should be versatile, allowing it to work with any type of student identifier and any numeric type for grades, such as `Integer`, `Double`, etc.

#### **Requirements:**

1. **Generic Design:**
   - The `Course` class should use generics to handle different types of student identifiers (e.g., `String` for names, `Integer` for student IDs) and numeric grades (e.g., `Integer`, `Double`).
   - The class should ensure type safety by working with these types and performing necessary operations without relying on specific implementations.

2. **Core Functionalities:**
   - **Enrolling Students**: The class should allow enrolling students in the course. Each student should be uniquely identified by an identifier of type `S`.
   - **Assigning Grades**: The class should provide functionality to assign a grade of type `G` to a specific student.
   - **Retrieving Grades**: The class should allow retrieval of a student's grade based on their identifier.
   - **Listing All Grades**: The class should offer a way to retrieve a collection of all students and their corresponding grades.

3. **Edge Cases Handling:**
   - The class should handle cases where a grade is requested for a student who has not been assigned a grade yet or who is not enrolled in the course.
   - It should ensure that grades can be updated if necessary and reflect the latest information for each student.

4. **Ease of Use:**
   - The class should provide an intuitive interface for adding students, assigning grades, and retrieving information.
   - It should be easily adaptable to different educational contexts, whether used in a simple classroom setting or a more complex educational system.

5. **Scalability:**
   - The design should accommodate large numbers of students and efficiently manage the storage and retrieval of their grades.
   - It should maintain the integrity of the data and perform operations in a way that scales well with the number of students.

#### **Scenario Example:**

Imagine a scenario where a teacher is managing a course and needs to keep track of the students enrolled and their grades. Each student can be identified by a unique identifier (such as a name or a student ID), and their grades could be in various formats, such as percentages or GPA scores. The teacher needs to be able to enroll students, assign grades, and retrieve grades as needed, ensuring that the course's data remains accurate and up-to-date.

By implementing the `Course` class with generics, the teacher can manage these tasks seamlessly, regardless of the type of student identifier or grade format, making the class highly adaptable to various educational needs.

#### **Outcome:**

Students should be able to design the `Course` class that can:

- Store and manage a collection of students and their corresponding grades.

- Provide methods for enrolling students, assigning and updating grades, and retrieving grades.

- Handle various types of student identifiers and numeric grades through the use of generics.

- Offer a user-friendly interface suitable for managing a course in different educational contexts.

  

### **Problem 5: Counting Odd Integers in a Collection**

#### **Objective:**
Design and implement a generic method that can count the number of elements in a collection that satisfy a specific property. In this specific case, the method should count the number of odd integers in a collection of integers.

#### **Requirements:**

1. **Generic Method Design:**
   - The method should be generic and able to work with any collection of integers.
   - It should be capable of counting elements that satisfy the "odd" property, meaning that the method needs to identify which integers in the collection are odd.

2. **Input:**
   - The method should accept a collection of integers as its input.
   - The collection can be of any type that implements the `Collection<Integer>` interface, such as `List<Integer>`, `Set<Integer>`, or any other collection type.

3. **Output:**
   - The method should return an integer representing the count of odd integers in the collection.

4. **Core Functionalities:**
   - **Counting Odd Integers**: The method should iterate through the collection and count how many of the integers are odd.
   - **Handling Empty Collections**: The method should return 0 if the collection is empty.
   - **Efficiency**: The method should be efficient in terms of time complexity, ideally iterating through the collection only once.

5. **Edge Cases Handling:**
   - The method should correctly handle collections that are empty or contain no odd integers, returning 0 in such cases.
   - It should also handle collections with a mix of even and odd integers, accurately counting only the odd ones.

6. **Scenario Example:**

Imagine a scenario where you have a list of integers representing the scores of students in a class. You want to find out how many of these scores are odd numbers. By using the generic method you design, you should be able to pass the list of scores to the method and get the count of odd scores, allowing you to easily analyze the distribution of scores.

7. **Outcome:**

Students should be able to design a generic method that:

- Works with any collection of integers to count how many elements satisfy the odd integer property.
- Is flexible and reusable for different collections and different properties (e.g., could be adapted to count prime numbers, palindromes, etc.).
- Handles edge cases like empty collections or collections with no odd integers gracefully.



 ### **Problem 6: Command-Line Driven Generic School Class with Error Handling**

#### **Objective:**

Design and implement a generic class `School` that manages multiple courses and handles operations such as enrolling students, managing grades, and generating reports. The program should be command-line driven, allowing the user to input various commands to perform different functionalities. Additionally, the program should include robust error handling to deal with unknown commands and contextually inappropriate operations.

#### **Requirements:**

1. **Generic Design:**      Map<String, Course<S,G>>
   - The `School` class should use generics to work with any type of course (`Course<S, G>`) , where `S` represents the student identifier and `G` represents the grade type.
   - Ensure type safety while performing operations on different types of courses 

2. **Command-Line Interface:**
   - **Managing Courses**: Allow the user to add, remove, and list courses via the command line. Each course should be uniquely identified by a course code or name.
   - **Enrolling Students**: Provide functionality to enroll students in a specific course by taking student identifiers from the command line.
   - **Assigning Grades**: Support assigning grades to students via the command line by specifying the course, student identifier, and grade.
   - **Generating Reports**: Offer commands to generate and display reports on student grades across all courses or for specific courses.

3. **Core Functionalities:**
   - **Add Course**: The command `add_course <course_name>` should allow adding a new course.
   - **Enroll Student**: The command `enroll_student <course_name> <student_id>` should enroll a student in the specified course.
   - **Assign Grade**: The command `assign_grade <course_name> <student_id> <grade>` should assign a grade to a student in the specified course.
   - **List Grades**: The command `list_grades <course_name>` should list all students and their grades for the specified course.
   - **List Courses**: The command `list_courses` should list all the courses in the school.

4. **Reporting Features:**
   - **Unique Courses**: The command `report_unique_courses` should list all unique courses being offered in the school.
   - **Unique Students**: The command `report_unique_students` should list all unique students enrolled in the school across all courses.
   - **Average Score by Course**: The command `report_average_score <course_name>` should give the average score obtained by students in a given course.
   - **Cumulative Average Score by Student**: The command `report_cumulative_average <student_id>` should give the cumulative average score of a given student across all courses they are enrolled in.

5. **Error Handling:**
   - **Unknown Command**: If an unknown command is given, the system should output an error message indicating that the command is not recognized and provide a list of available commands.
     - Example: `"Error: Unknown command 'unknown_command'. Please use one of the following commands: add_course, enroll_student, assign_grade, list_grades, list_courses, report_unique_courses, report_unique_students, report_average_score, report_cumulative_average."`
   - **Contextual Errors**: If an operation is not possible in its context, the system should output an appropriate business error message.
     - Example 1: If trying to enroll a student in a non-existent course, the system should display: `"Error: Cannot enroll student. Course 'Physics103' does not exist."`
     - Example 2: If trying to assign a grade to a student who is not enrolled in a course, the system should display: `"Error: Cannot assign grade. Student '12345' is not enrolled in course 'Math101'."`

6. **Ease of Use:**
   - The command-line interface should be intuitive, providing clear instructions for each operation.
   - The system should be robust enough to handle invalid inputs gracefully and guide the user towards correct usage.

7. **Scalability:**
   - The design should allow for scalability in managing a large number of courses and students.
   - Efficiently handle data storage and retrieval even as the number of students and courses increases.

#### **Scenario Example:**

Imagine a scenario where a school administrator needs to manage multiple courses, each with its own set of students and grade requirements. The administrator interacts with the system entirely through the command line, adding courses, enrolling students, assigning grades, and generating reports. The system should be flexible enough to accommodate different types of courses, student identifiers, and grades, making it highly adaptable to various educational contexts. Additionally, the system should handle errors effectively, ensuring that the administrator is guided correctly when mistakes are made.

#### **Outcome:**

Students should be able to design the `School` class and related classes (`Course`, `Gradebook`) that can:

- Manage multiple courses and perform operations like enrolling students and assigning grades through command-line commands.
- Provide a user-friendly interface via the command line for managing all functionalities.
- Generate reports on unique courses, unique students, average scores by course, and cumulative average scores by student.
- Handle unknown commands and contextual errors effectively, providing clear and helpful error messages.
- Adapt to various educational contexts through the use of generics for student identifiers and grades.
- Efficiently scale as the number of courses and students increases.

### **Example Commands:**

```bash
# Managing Courses
add_course Math101
add_course Physics102
list_courses

# Enrolling Students
enroll_student Math101 12345
enroll_student Physics102 54321

# Error Handling: Unknown Command
unknown_command

# Error Handling: Enroll in Non-Existent Course
enroll_student Physics103 12345

# Assigning Grades
assign_grade Math101 12345 85.5
assign_grade Physics102 54321 92

# Error Handling: Assign Grade to Non-Enrolled Student
assign_grade Math101 54321 75

# Listing Grades
list_grades Math101

# Generating Reports
report_unique_courses
report_unique_students
report_average_score Math101
report_cumulative_average 12345
```



To help users understand how to use the command-line interface for the `School` class, you can provide a set of sample command-line prompts. These prompts guide users on how to manage courses, enroll students, assign grades, generate reports, and handle errors. Below are sample command-line prompts for each functionality.

### **Sample Command-Line Prompts**

#### **1. Managing Courses**

- **Add a Course:**
  
  ```bash
  > add_course Math101
  ```
  **Output:**
  ```
  Course 'Math101' added.
  ```
  
- **List All Courses:**
  ```bash
  > list_courses
  ```
  **Output:**
  ```
  Courses offered:
  Math101
  ```

#### **2. Enrolling Students**

- **Enroll a Student in a Course:**
  ```bash
  > enroll_student Math101 12345
  ```
  **Output:**
  ```
  Student '12345' enrolled in course 'Math101'.
  ```

- **Enroll a Student in a Non-Existent Course (Error Handling):**
  ```bash
  > enroll_student Physics103 12345
  ```
  **Output:**
  
  ```
  Error: Cannot enroll student. Course 'Physics103' does not exist.
  ```

#### **3. Assigning Grades**

- **Assign a Grade to a Student:**
  ```bash
  > assign_grade Math101 12345 85.5
  ```
  **Output:**
  ```
  Grade '85.5' assigned to student '12345' in course 'Math101'.
  ```

- **Assign a Grade to a Student Not Enrolled in the Course (Error Handling):**
  ```bash
  > assign_grade Math101 54321 75
  ```
  **Output:**
  ```
  Error: Cannot assign grade. Student '54321' is not enrolled in course 'Math101'.
  ```

#### **4. Listing Grades**

- **List All Grades for a Course:**
  ```bash
  > list_grades Math101
  ```
  **Output:**
  ```
  Grades for course 'Math101':
  Student: 12345 - Grade: 85.5
  ```

- **List Grades for a Non-Existent Course (Error Handling):**
  ```bash
  > list_grades Physics103
  ```
  **Output:**
  ```
  Error: Course 'Physics103' does not exist.
  ```

#### **5. Generating Reports**

- **Report All Unique Courses:**
  ```bash
  > report_unique_courses
  ```
  **Output:**
  ```
  Courses offered:
  Math101
  Physics102
  ```

- **Report All Unique Students:**
  
  ```bash
  > report_unique_students
  ```
  **Output:**
  ```
  Unique students enrolled:
  12345
  67890
  ```
  
- **Report Average Score in a Course:**
  ```bash
  > report_average_score Math101
  ```
  **Output:**
  ```
  Average score for course 'Math101': 85.5
  ```

- **Report Cumulative Average Score for a Student Across All Courses:**
  ```bash
  > report_cumulative_average 12345
  ```
  **Output:**
  ```
  Cumulative average score for student '12345': 87.5
  ```

#### **6. Error Handling**

- **Unknown Command:**
  ```bash
  > unknown_command
  ```
  **Output:**
  ```
  Error: Unknown command 'unknown_command'. Please use a valid command.
  ```

### **Summary of Commands**

| **Command**                           | **Description**                                              |
| ------------------------------------- | ------------------------------------------------------------ |
| `add_course <course_name>`            | Adds a new course to the school.                             |
| `list_courses`                        | Lists all courses offered by the school.                     |
| `enroll_student <course_name> <id>`   | Enrolls a student in the specified course.                   |
| `assign_grade <course_name> <id> <g>` | Assigns a grade to the student in the specified course.      |
| `list_grades <course_name>`           | Lists all students and their grades for the specified course. |
| `report_unique_courses`               | Lists all unique courses offered by the school.              |
| `report_unique_students`              | Lists all unique students enrolled in the school.            |
| `report_average_score <course_name>`  | Reports the average score of the specified course.           |
| `report_cumulative_average <id>`      | Reports the cumulative average score for the specified student. |
| `unknown_command`                     | Handles unknown commands and suggests valid options.         |

These sample command-line prompts and outputs help users understand how to interact with the `School` management system effectively, handling both standard operations and errors.





