public class University {
    private String uniName;
    private String location;
    private Club club;

    public University(String uniName, String location) {
        this.uniName = uniName;
        this.location = location;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public void display() {
        System.out.println("University: " + uniName);
        if (club != null) {
            System.out.println("Associated Club: " + club.getClubName());
        }
        System.out.println("Location: " + location);
    }
}
