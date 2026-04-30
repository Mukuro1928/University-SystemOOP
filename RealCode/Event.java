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
    
    public void setEventDetails(String eventName, String eventDate, String eventLocation, int attendeeCount){
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.attendeeCount = attendeeCount;
    }

    public void getEventDetails(){
        System.out.println("Event Name: " + eventName);
        System.out.println("Event Date: " + eventDate);
        System.out.println("Event Location: " + eventLocation);
        System.out.println("Attendee Count: " + attendeeCount);
    }
}
