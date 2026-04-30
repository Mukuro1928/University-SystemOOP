package universitymanagementtest;

/**
 * Author: [Member 3 Name]
 * Matric No: [Member 3 Number]
 */
public class Student {
    private String studentID;
    private String name;

    public Student(String studentID, String name) {
        this.studentID = studentID;
        this.name = name;
    }

    // Association: Passing a Course object as a parameter
    public void enrollInCourse(Course course) {
        System.out.println(name + " has enrolled in: " + course.getCourseName());
    }

    // Association: Passing an Event object as a parameter
    public void registerForEvent(Event event) {
        System.out.println(name + " is attending event: " + event.getEventDetails());
    }

    public String getName() { return name; }
}