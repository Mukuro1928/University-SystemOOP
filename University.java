
package universitymanagementtest;

import java.util.ArrayList;
import java.util.List;

public class University {
    private String uniName;
    private List<Club> clubList;
    private List<Course> courseList;

    public University(String uniName) {
        this.uniName = uniName;
        this.clubList = new ArrayList<>();
        this.courseList = new ArrayList<>();
    }

    // Aggregation: Adding existing objects to the list
    public void addClub(Club club) {
        clubList.add(club);
    }

    public void addCourse(Course course) {
        courseList.add(course);
    }

    public void displayInfo() {
        System.out.println("University: " + uniName);
        System.out.println("Total Clubs: " + clubList.size());
        System.out.println("Total Courses: " + courseList.size());
    }

    public String getUniName() { return uniName; }
}