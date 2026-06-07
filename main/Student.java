/**
 * Student.java — Student Entity
 * =============================
 * Represents a student in the system and extends the Person class.
 *
 * Features:
 * • Stores student-specific data such as student ID.
 * • Maintains an association with a Course object (enrolled course).
 * • Provides getter and setter methods for all attributes.
 * • Includes a method to display formatted student information.
 *
 * Architecture:
 * • Child class of Person, reusing shared attributes like name and email.
 * • Demonstrates object association through the enrolledCourse reference.
 * • Acts as a data model used by higher-level system components.
 *
 * Behavior:
 * • Safely checks for null enrolledCourse before displaying course details.
 *
 * Note:
 * • This class focuses on data representation and simple console output only.
 * • No persistence, validation, or business logic is handled here.
 */


public class Student extends Person {
    private int studentID;
    private Course enrolledCourse;

    public Student(int studentID, String name, String email, Course enrolledCourse) {
        super(name, email);
        this.studentID = studentID;
        this.enrolledCourse = enrolledCourse;
    }

    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public Course getEnrolledCourse() {
        return enrolledCourse;
    }

    public void setEnrolledCourse(Course enrolledCourse) {
        this.enrolledCourse = enrolledCourse;
    }

    public void displayStudentInfo() {
        System.out.println("================================");
        System.out.println("Student ID   : " + studentID);
        System.out.println("Student Name : " + getName());
        System.out.println("Email        : " + getEmail());
        if (enrolledCourse != null) {
            System.out.println("Enrolled Course: " + enrolledCourse.getCourseName());
        } else {
            System.out.println("Not enrolled in any course.");
        }
        System.out.println("================================");
    }
}
