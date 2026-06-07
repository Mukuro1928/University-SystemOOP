/**
 * Developed by: AHMAD MUKHLIS BIN ZAKARIAH 2514371
 * Club.java — Club Entity Class
 * =============================
 * Represents a student club within the University Management System.
 *
 * Responsibilities:
 *  • Store club information including club name and president.
 *  • Manage the club's associated event.
 *  • Support club registration and event organization activities.
 *
 * Features:
 *  • Club name and president management.
 *  • Event creation through object association.
 *  • Event retrieval and display functionality.
 *  • Club information access through getter and setter methods.
 *
 * Architecture:
 *  • Serves as a domain model (entity) class.
 *  • Encapsulates club-related data using private attributes.
 *  • Maintains a one-to-one association with the Event class,
 *    where a club may organize a single event.
 *
 * Design:
 *  • Functions as a lightweight data container that stores
 *    club information and links it to its corresponding event
 *    within the system.
 */

public class Club
{
    private String clubName;
    private String presidentName;
    private Event event;
    
    public Club(String clubName, String presidentName) 
    {
        this.clubName = clubName;
        this.presidentName = presidentName;
    }
    
    public String getClubName() 
    {
        return clubName;
    }

    public void setClubName(String clubName)  
    {
        this.clubName = clubName;
    }

    public String getPresidentName() 
    {
        return presidentName;
    }

    public void setPresidentName(String presidentName) 
    {
        this.presidentName = presidentName;
    }
    
    public void createEvent(String name, String date, String location, int count) 
    {
        event = new Event(name, date, location, count);
    }
    
    public Event getEvent()
    {
        return event;
    }

    public void showClubDetails() {
        System.out.println("Club: " + clubName);
        System.out.println("President: " + presidentName);

        if (event != null) {
            event.getEventDetails();
        } else {
            System.out.println("No event yet.");
        }
    }
}
