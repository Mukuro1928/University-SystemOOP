
package universitymanagementtest;


public class UniversityManagementTest {
    public static void main(String[] args) {
        // 1. Initialize the University (The Container)
        University myUni = new University("International Islamic University Malaysia");

        // 2. Demonstrate AGGREGATION
        // Create objects independently and add them to the University
        Club codingClub = new Club("Computing Society", "Brother Khalid");
        Course oopCourse = new Course("BICS 1304", "Object-Oriented Programming");

        myUni.addClub(codingClub);
        myUni.addCourse(oopCourse);

        // 3. Demonstrate COMPOSITION
        // The Club creates the Event internally. If codingClub is deleted, this event dies.
        codingClub.createEvent("Java Hackathon", "2026-05-20");

        // 4. Initialize a Student
        Student student1 = new Student("2310987", "Ahmad Fauzi");

        // 5. Demonstrate ASSOCIATION
        // Student interacts with the Course and the Club's Event
        System.out.println("--- System Activity Log ---");
        student1.enrollInCourse(oopCourse);

        // Access the event created inside the club to register the student
        if (!codingClub.getEvents().isEmpty()) {
            Event upcomingEvent = codingClub.getEvents().get(0);
            student1.registerForEvent(upcomingEvent);
        }

        // 6. Display Final System State
        System.out.println("\n--- University Status Report ---");
        myUni.displayInfo();
        codingClub.listEvents();
    }
}
