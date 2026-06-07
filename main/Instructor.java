/**
 * Instructor.java — Instructor Entity
 * ===================================
 * Represents an instructor in the system and extends the Person class.
 *
 * Features:
 * • Stores instructor-specific data such as employee ID.
 * • Inherits common attributes (name, email) from Person.
 * • Provides getter and setter for employee ID.
 * • Includes a method to display formatted instructor information.
 *
 * Architecture:
 * • Child class of Person, demonstrating inheritance for shared attributes.
 * • Used as a data model for instructor-related operations in the system.
 * • Relies on superclass methods (getName, getEmail) for common data access.
 *
 * Note:
 * • This class is focused on data representation and simple output formatting only.
 * • No persistence or business logic is handled here.
 */


public class Instructor extends Person {
    private String employeeID;

    public Instructor(String employeeID, String name, String email) {
        super(name, email);
        this.employeeID = employeeID;
    }

    public String getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(String employeeID) {
        this.employeeID = employeeID;
    }

    public void displayInstructorInfo() {
        System.out.println("================================");
        System.out.println("Instructor ID: " + employeeID);
        System.out.println("Name         : " + getName());
        System.out.println("Email        : " + getEmail());
        System.out.println("================================");
    }
}
