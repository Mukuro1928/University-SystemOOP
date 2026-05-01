import java.util.*;

public class Student {

    private int studentID;
    private String studentName;
    private String email;
    private Course enrolledCourse;
 
    public Student(int studentID, String studentName, String email) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.email = email;
        this.enrolledCourse = null;
    }

 
    public int getStudentID() {
        return studentID;
    }
 
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
 
    public String getStudentName() {
        return studentName;
    }
 
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 

    public void registerForEvent(Event event) {
        event.attendeeCount++;
        System.out.println(studentName + " (ID: " + studentID + ")"
                + " has registered for event: " + event.eventName
                + " on " + event.eventDate
                + " at " + event.eventLocation);
    }
 

    public void enrollCourse(Course course) {
        this.enrolledCourse = course;
        course.enrollStudent(this.studentName);
    }

    public void dropCourse() {
        if (enrolledCourse != null) {
            enrolledCourse.dropStudent(this.studentName);
            this.enrolledCourse = null;
        } else {
            System.out.println(studentName + " is not enrolled in any course.");
        }
    }

    public void displayEnrolledCourse() {
        System.out.println("\n--- Enrolled Course for " + studentName
        + " (ID: " + studentID + ") ---");
        if (enrolledCourse != null) {
            enrolledCourse.displayCourseInfo(); 
        } else {
            System.out.println("No course enrolled.");
        }
    }
 
    public void displayStudentInfo() {
        System.out.println("================================");
        System.out.println("Student ID   : " + studentID);
        System.out.println("Student Name : " + studentName);
        System.out.println("Email        : " + email);
        System.out.println("================================");
    }
 
