package universitymanagementtest;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: [Member 2 Name]
 * Matric No: [Member 2 Number]
 */
public class Club {
    private String clubName;
    private String president;
    private List<Event> events;

    public Club(String clubName, String president) {
        this.clubName = clubName;
        this.president = president;
        this.events = new ArrayList<>();
    }

    // Composition: The Club creates the Event object internally
    public void createEvent(String name, String date) {
        Event newEvent = new Event(name, date);
        events.add(newEvent);
    }

    public void listEvents() {
        System.out.println("Events for " + clubName + ":");
        for (Event e : events) {
            System.out.println("- " + e.getEventDetails());
        }
    }

    public List<Event> getEvents() { return events; }
    public String getClubName() { return clubName; }
}