
package universitymanagementtest;

public class University {
    private String uniName;
    private Club club; // Single reference instead of a list

    public University(String uniName) {
        this.uniName = uniName;
    }

    // Aggregation: The Club is created outside and passed in
    public void setClub(Club club) {
        this.club = club;
    }

    public void display() {
        System.out.println("University: " + uniName);
        if (club != null) {
            System.out.println("Associated Club: " + club.getClubName());
        }
    }
}
