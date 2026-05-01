public class Student {
 
    // Attributes (as per UML)
    private int studentID;
    private String studentName;
    private String email;
 
    // Constructor
    public Student(int studentID, String studentName, String email) {
        this.studentID   = studentID;
        this.studentName = studentName;
        this.email       = email;
    }
 
    // ── Getters & Setters ──────────────────────────────────────────────────────
 
    public int getStudentID() {
        return studentID;
    }
 
    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }
 
    public String getStudentName() {
        return studentName;
    }
 
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    // ── Methods (as per UML) ───────────────────────────────────────────────────
 
    /**
     * Register this student for a club event.
     * Relates to the Event class (which extends Club).
     */
    public void registerForEvent(Event event) {
        event.attendeeCount++;
        System.out.println(studentName + " (ID: " + studentID + ")"
                + " has registered for event: " + event.eventName
                + " on " + event.eventDate
                + " at " + event.eventLocation);
    }
 
    /**
     * Process a membership payment for this student.
     * Relates to the MembershipPayment class.
     */
    public void payDues(MembershipPayment payment) {
        payment.processPayment();
        System.out.println(studentName + " (ID: " + studentID + ")"
                + " has paid dues of RM" + payment.getAmount()
                + " for membership: " + payment.getMembershipType());
    }
 
    // ── Display ────────────────────────────────────────────────────────────────
 
    public void displayStudentInfo() {
        System.out.println("================================");
        System.out.println("Student ID   : " + studentID);
        System.out.println("Student Name : " + studentName);
        System.out.println("Email        : " + email);
        System.out.println("================================");
    }
 
