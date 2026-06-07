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
