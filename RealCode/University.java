/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author nuzil
 */
public class University {

    private String name;
    private String location;
    private ArrayList<Club> clubs;

    // Constructor - Initialises attributes and the list
    public University(String name, String location) {
        this.name = name;
        this.location = location;
        this.clubs = new ArrayList<>(); 
    }

    //Method to add a club to the university.
    public void addClub(Club club) {
        clubs.add(club);
        System.out.println("Club '" + club.getClubName() + "' has been added to " + this.name);
    }

    //Method to display all clubs currently registered at the university.
    public void displayAllClubs() {
        System.out.println("\n--- Clubs at " + this.name + " (" + this.location + ") ---");
        
        if (clubs.isEmpty()) {
            System.out.println("No clubs registered yet.");
        } else {
            for (int i = 0; i < clubs.size(); i++) {
                Club currentClub = clubs.get(i); 
                System.out.println("- " + currentClub.getClubName());
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
