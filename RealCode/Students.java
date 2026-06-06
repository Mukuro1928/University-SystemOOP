public class Student 
{
    private int studentID;
    private String studentName;
    private String email;
    private Course enrolledCourse;
    
    public Student() 
    {
    }
 
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
 
    public void displayStudentInfo() {
        System.out.println("Student ID   : " + studentID);
        System.out.println("Student Name : " + studentName);
        System.out.println("Email        : " + email);
        System.out.println("================================");
    }
 
}
