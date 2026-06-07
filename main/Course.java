/**
 * Developed by: AHMAD ZUHDI BIN MOHD SOUD 2512839 
 * Course.java — Course Data Model
 * ===============================
 * Represents a single academic course entity used in the system.
 *
 * Features:
 * • Stores course details including name, code, credit hours, and instructor.
 * • Provides getter and setter methods for all attributes.
 * • Supports updating course information dynamically via setters.
 * • Includes a utility method to display formatted course details in the console.
 *
 * Architecture:
 * • Acts as a plain data model (POJO) used by higher-level components such as
 * CourseWorkspace and DataManager.
 * • Does not handle file storage, UI logic, or business operations directly.
 * • Designed for simple data encapsulation and transfer between system layers.
 *
 * Note:
 * • This class focuses purely on data representation and does not include validation
 * or persistence logic.
 */


public class Course 
{
    private String courseName;
    private String courseCode;
    private int credit;
    private String instructor;
    
    public Course(String courseName, String courseCode, int credit, String instructor)
    {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credit = credit;
        this.instructor = instructor;
    }
    
    public void setCourseName(String courseName)
    {
        this.courseName = courseName;
    }
    
    public String getCourseName()
    {
        return this.courseName;
    }
    
    
    public void setCourseCode(String courseCode)
    {
        this.courseCode = courseCode;
    }
    
    public String getCourseCode()
    {
        return this.courseCode;
    }
    
    
    public void setCredit(int credit)
    {
        this.credit = credit;
    }
    
    public int getCredit()
    {
        return this.credit;
    }
    
    
    public void setInstructor(String instructor)
    {
        this.instructor = instructor;
    }
    
    public String getInstructor()
    {
        return this.instructor;
    }
    
    public void displayCourseInfo()
    {
        System.out.println("================================");
        System.out.println("Course Name: " + this.courseName);
        System.out.println("Coure Code: " + this.courseCode);
        System.out.println("Course Credit: " + this.credit);
        System.out.println("Instructor: " + this.instructor);
        System.out.println("================================");
    }
}
