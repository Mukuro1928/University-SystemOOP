package universitymanagementtest;

/**
 * Author: [Member 5 Name]
 * Matric No: [Member 5 Number]
 */
public class Course {
    private String courseCode;
    private String courseName;

    public Course(String courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseCode + ": " + courseName;
    }
}
