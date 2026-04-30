package universitymanagementtest;

/**
 * Author: [Member 4 Name]
 * Matric No: [Member 4 Number]
 */
public class Event {
    private String title;
    private String date;

    public Event(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public String getEventDetails() {
        return title + " on " + date;
    }
}
