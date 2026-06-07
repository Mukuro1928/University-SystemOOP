ublic class Club
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

