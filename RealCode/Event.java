package uniclubmansys;

public class Event extends Club {
    public String eventName;
    public String eventDate;
    public String eventLocation;
    public int attendeeCount;

    public Event() {
        // Default constructor
    }

    public Event(String eventName, String eventDate, String eventLocation, int attendeeCount) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.attendeeCount = attendeeCount;
    }
    
    public void updateEventDetails(String eventName, String eventDate, String eventLocation, int attendeeCount){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.attendeeCount = attendeeCount;
    }
    
    public String getEventSummary(){
        return "Event: " + eventName + ", Date: " + eventDate + ", Location: " + eventLocation + ", Attendees: " + attendeeCount;
    }
}
