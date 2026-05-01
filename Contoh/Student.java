package universitymanagementtest;

public class Student {
    private String name;

    public Student(String name) {
        this.name = name;
    }

    // Association: Passing objects as parameters to a method
    public void attend(Course c, Event e) {
        System.out.println(name + " is attending " + c.getCourseName() + 
                           " and then going to " + e.getDetails());
    }
}
