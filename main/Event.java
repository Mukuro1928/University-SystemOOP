/** 
 * Developed By MUHAMMAD A’THIF UZAIR BIN SHAEDIN 2514847 
 * Event.java — Event Entity Class
 * ===============================
 * Represents an event organized by a club within the
 * University Management System.
 *
 * Responsibilities:
 *  • Store event information including name, date,
 *    location, and attendee count.
 *  • Provide methods to create, update, and retrieve
 *    event details.
 *  • Support club event management operations.
 *
 * Features:
 *  • Event name management.
 *  • Event date and location tracking.
 *  • Attendee count recording.
 *  • Bulk event detail updates through a single method.
 *
 * Architecture:
 *  • Serves as a domain model (entity) class.
 *  • Encapsulates event-related data using private fields
 *    and public getter/setter methods.
 *  • Associated with the Club class to represent club events.
 *
 * Design:
 *  • Functions as a lightweight data container that stores
 *    and manages event information throughout the system.
 */

public class Event {
    private String eventName;
    private String eventDate;
    private String eventLocation;
    private int attendeeCount;

    public Event(String eventName, String eventDate, String eventLocation, int attendeeCount) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.attendeeCount = attendeeCount;
    }

    public void setEventDetails(String eventName, String eventDate, String eventLocation, int attendeeCount) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.attendeeCount = attendeeCount;
    }

    public String getEventName() { return eventName; }
    public void setEventName(String eventName) { this.eventName = eventName; }

    public String getEventDate() { return eventDate; }
    public void setEventDate(String eventDate) { this.eventDate = eventDate; }

    public String getEventLocation() { return eventLocation; }
    public void setEventLocation(String eventLocation) { this.eventLocation = eventLocation; }

    public int getAttendeeCount() { return attendeeCount; }
    public void setAttendeeCount(int attendeeCount) { this.attendeeCount = attendeeCount; }

    public void getEventDetails() {
        System.out.println("Event Name: " + eventName);
        System.out.println("Event Date: " + eventDate);
        System.out.println("Event Location: " + eventLocation);
        System.out.println("Attendee Count: " + attendeeCount);
    }
}
