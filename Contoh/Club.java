package universitymanagementtest;

public class Club {
    private String clubName;
    private Event featuredEvent; // The "part" of the whole

    public Club(String clubName, String eventName, String date) {
        this.clubName = clubName;
        // Composition: Event is instantiated here, not in Main
        this.featuredEvent = new Event(eventName, date);
    }

    public String getClubName() { return clubName; }
    public Event getEvent() { return featuredEvent; }
}
