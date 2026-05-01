/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nuzil
 */
import java.util.ArrayList;

public class University {
    // Attributes as defined in the UML
    private String name;
    private String location;
    private ArrayList<Club> clubs; // This implements the clubs[]: Club attribute

    // Constructor
    public University(String name, String location) {
        this.name = name;
        this.location = location;
        this.clubs = new ArrayList<>(); // Initialize the list
    }

    // Method to add a club (Aggregation)
    public void addClub(Club club) {
        clubs.add(club);
        System.out.println("Club '" + club.getClubName() + "' has been added to " + this.name);
    }

    // Method to display all clubs
    public void displayAllClubs() {
        System.out.println("\n--- Clubs at " + this.name + " (" + this.location + ") ---");
        if (clubs.isEmpty()) {
            System.out.println("No clubs registered yet.");
        } else {
            for (Club c : clubs) {
                // This assumes the Club class has a getClubName() method
                System.out.println("- " + c.getClubName());
            }
        }
    }
}
