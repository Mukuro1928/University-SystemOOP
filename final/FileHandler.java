package universitymanagementsystem;

import java.io.*;

/**
 * FileHandler.java — Persistent Storage Utility (Phase 3)
 * ========================================================
 * Team Member: Muhammad Aiman bin Sufian
 * * Responsibilities:
 * 1. Serializes live DataManager ArrayLists into flat CSV text files.
 * 2. Reconstructs object relationships on application boot.
 * 3. Handles empty relationships (e.g., unenrolled students, clubs without events).
 */
public class FileHandler {

    // Define storage directory and exact file paths
    private static final String DATA_DIR        = "data/";
    private static final String COURSE_FILE     = DATA_DIR + "courses.txt";
    private static final String INSTRUCTOR_FILE = DATA_DIR + "instructors.txt";
    private static final String STUDENT_FILE    = DATA_DIR + "students.txt";
    private static final String CLUB_FILE       = DATA_DIR + "clubs.txt";

    // ================================================================== //
    // 1. SAVE OPERATIONS (Called by UI Action Buttons)
    // ================================================================== //

    /**
     * Wipes and rewrites all text files to match the current live state of DataManager.
     */
    public static void saveAllData() {
        // Ensure the data/ folder exists before writing
        new File(DATA_DIR).mkdirs();

        saveCourses();
        saveInstructors();
        saveStudents();
        saveClubs();
    }

    private static void saveCourses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSE_FILE))) {
            for (Course c : DataManager.getInstance().getCourseList()) {
                // Format: courseName,courseCode,credit,instructor
                writer.println(
                    c.getCourseName() + "," + 
                    c.getCourseCode() + "," + 
                    c.getCredit() + "," + 
                    c.getInstructor()
                );
            }
        } catch (IOException e) {
            System.err.println("Error writing courses.txt: " + e.getMessage());
        }
    }

    private static void saveInstructors() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(INSTRUCTOR_FILE))) {
            for (Instructor i : DataManager.getInstance().getInstructorList()) {
                // Format: employeeID,name,email
                writer.println(
                    i.getEmployeeID() + "," + 
                    i.getName() + "," + 
                    i.getEmail()
                );
            }
        } catch (IOException e) {
            System.err.println("Error writing instructors.txt: " + e.getMessage());
        }
    }

    private static void saveStudents() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (Student s : DataManager.getInstance().getStudentList()) {
                // Handle optional enrolled course reference safely
                String courseCode = s.getEnrolledCourse() != null ? s.getEnrolledCourse().getCourseCode() : "NONE";
                
                // Format: studentID,name,email,enrolledCourseCode
                writer.println(
                    s.getStudentID() + "," + 
                    s.getName() + "," + 
                    s.getEmail() + "," + 
                    courseCode
                );
            }
        } catch (IOException e) {
            System.err.println("Error writing students.txt: " + e.getMessage());
        }
    }

    private static void saveClubs() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CLUB_FILE))) {
            for (Club c : DataManager.getInstance().getClubList()) {
                // Determine if this Club holds an active Event object
                if (c.getEvent() != null) {
                    // Format: clubName,presidentName,eventName,eventDate,eventLocation,attendeeCount
                    writer.println(
                        c.getClubName() + "," + 
                        c.getPresidentName() + "," + 
                        c.getEvent().getEventName() + "," + 
                        c.getEvent().getEventDate() + "," + 
                        c.getEvent().getEventLocation() + "," + 
                        c.getEvent().getAttendeeCount()
                    );
                } else {
                    // Format: clubName,presidentName,NULL,NULL,NULL,0
                    writer.println(
                        c.getClubName() + "," + 
                        c.getPresidentName() + "," + 
                        "NULL,NULL,NULL,0"
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing clubs.txt: " + e.getMessage());
        }
    }


    // ================================================================== //
    // 2. LOAD OPERATIONS (Called once at application startup)
    // ================================================================== //

    /**
     * Reads all local files and rebuilds the DataManager collections.
     * Order matters: Courses MUST be loaded before Students so the object references link correctly.
     */
    public static void loadAllData() {
        // Load independent objects first
        loadCourses();
        loadInstructors();
        loadClubs();
        
        // Load dependent objects last (Students require Courses to exist)
        loadStudents();
    }

    private static void loadCourses() {
        File file = new File(COURSE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4) {
                    DataManager.getInstance().addCourse(
                        tokens[0].trim(), 
                        tokens[1].trim(), 
                        Integer.parseInt(tokens[2].trim()), 
                        tokens[3].trim()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading courses.txt: " + e.getMessage());
        }
    }

    private static void loadInstructors() {
        File file = new File(INSTRUCTOR_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 3) {
                    DataManager.getInstance().addInstructor(
                        tokens[0].trim(), 
                        tokens[1].trim(), 
                        tokens[2].trim()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading instructors.txt: " + e.getMessage());
        }
    }

    private static void loadStudents() {
        File file = new File(STUDENT_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4) {
                    int id = Integer.parseInt(tokens[0].trim());
                    String name = tokens[1].trim();
                    String email = tokens[2].trim();
                    String courseCode = tokens[3].trim();

                    // If the student has no course, pass empty string to DataManager
                    if (courseCode.equals("NONE")) {
                        courseCode = "";
                    }

                    DataManager.getInstance().addStudent(id, name, email, courseCode);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading students.txt: " + e.getMessage());
        }
    }

    private static void loadClubs() {
        File file = new File(CLUB_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 6) {
                    String clubName = tokens[0].trim();
                    String president = tokens[1].trim();
                    String eventName = tokens[2].trim();
                    String eventDate = tokens[3].trim();
                    String eventLocation = tokens[4].trim();
                    int attendeeCount = Integer.parseInt(tokens[5].trim());

                    if (eventName.equals("NULL")) {
                        // Add club with no event (pass empty strings to DataManager)
                        DataManager.getInstance().addClub(clubName, president, "", "", "", 0);
                    } else {
                        // Add club and trigger its internal Event creation
                        DataManager.getInstance().addClub(clubName, president, eventName, eventDate, eventLocation, attendeeCount);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading clubs.txt: " + e.getMessage());
        }
    }
}