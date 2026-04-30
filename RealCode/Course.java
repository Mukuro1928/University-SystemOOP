/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sem1.oop;

/**
 *
 * @author LOQ
 */
public class Course 
{
    private String courseName;
    private String courseCode;
    private int credit;
    private String instructor;
    
    public Course()
    {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.credit = credit;
        this.instructor = instructor;
    }
    
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
    
    
    public void setInstructor(String Instructor)
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
        System.out.println("Instructor: " + this.instructor);
        System.out.println("================================");
    }
    
    public void enrollStudent(String student)
    {
        System.out.println(student + " has enrolled in [" + this.courseCode + "]" + this.courseName);
    }
    
    public void dropStudent(String student)
    {
        System.out.println(student + " has been dropped from [" + this.courseCode + "]" + this.courseName);
    }

}
