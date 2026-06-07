/**
 * Developed by AMIR NUZIL IRFAN BIN AMIR ZAHARIMAN 2512533
 * University.java — University Entity
 * ===================================
 * Represents a university and its associated details within the system.
 *
 * Features:
 * • Stores university name and location.
 * • Maintains an optional association with a Club object.
 * • Provides a setter method to link a club to the university.
 * • Displays formatted university information via a display method.
 *
 * Architecture:
 * • Acts as a data model representing a higher-level aggregation entity.
 * • Demonstrates object association through the Club reference.
 * • Interacts with other domain objects such as Club in the system.
 *
 * Behavior:
 * • Safely checks for null before displaying associated club information.
 *
 * Note:
 * • This class is focused on simple data representation and output.
 * • Does not include persistence, validation, or business logic.
 */

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
