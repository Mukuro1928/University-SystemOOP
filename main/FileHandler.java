import java.io.*;

/**
 * Developed by: IQMAL HAKIMI BIN SUHAIMI 2511737
 * FileHandler.java — Data Persistence Module
 * ==========================================
 * Handles the storage and retrieval of application data using
 * text-based files.
 *
 * Responsibilities:
 *  • Save system data to external text files.
 *  • Load previously saved data when the application starts.
 *  • Maintain persistent records for Students, Instructors,
 *    Courses, and Clubs.
 *  • Convert in-memory objects into a file-friendly format
 *    and reconstruct them when loading.
 *
 * Architecture:
 *  • Uses Java File I/O classes such as File, FileReader,
 *    FileWriter, BufferedReader, and PrintWriter.
 *  • Works closely with the DataManager singleton to access
 *    and restore application data.
 *  • Stores each entity type in a separate text file for
 *    improved organization and maintainability.
 *
 * Features:
 *  • Save all system records with a single method call.
 *  • Load all saved records during application startup.
 *  • Preserve relationships between students and courses,
 *    as well as clubs and their events.
 *  • Automatically create the required data directory if it
 *    does not already exist.
 *
 * Design:
 *  • Functions as the persistence layer of the University
 *    Management System, ensuring data remains available
 *    between application sessions.
 */

public class FileHandler {
    private static final String DATA_DIR        = "data/";
    private static final String COURSE_FILE     = DATA_DIR + "courses.txt";
    private static final String INSTRUCTOR_FILE = DATA_DIR + "instructors.txt";
    private static final String STUDENT_FILE    = DATA_DIR + "students.txt";
    private static final String CLUB_FILE       = DATA_DIR + "clubs.txt";

    public static void saveAllData() {
        new File(DATA_DIR).mkdirs();

        saveCourses();
        saveInstructors();
        saveStudents();
        saveClubs();
    }

    private static void saveCourses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSE_FILE))) {
            for (Course c : DataManager.getInstance().getCourseList()) {
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
                String courseCode = s.getEnrolledCourse() != null ? s.getEnrolledCourse().getCourseCode() : "NONE";
                
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
                if (c.getEvent() != null) {
                    writer.println(
                        c.getClubName() + "," + 
                        c.getPresidentName() + "," + 
                        c.getEvent().getEventName() + "," + 
                        c.getEvent().getEventDate() + "," + 
                        c.getEvent().getEventLocation() + "," + 
                        c.getEvent().getAttendeeCount()
                    );
                } else {
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

    public static void loadAllData() {
        loadCourses();
        loadInstructors();
        loadClubs();

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
                        DataManager.getInstance().addClub(clubName, president, "", "", "", 0);
                    } else {
                        DataManager.getInstance().addClub(clubName, president, eventName, eventDate, eventLocation, attendeeCount);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading clubs.txt: " + e.getMessage());
        }
    }
}
