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
