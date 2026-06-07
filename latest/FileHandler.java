import java.io.*;
import java.util.ArrayList;

public class FileHandler {
    private static final String DATA_DIR = "data/";
    private static final String STUDENT_FILE = DATA_DIR + "students.txt";
    private static final String COURSE_FILE  = DATA_DIR + "courses.txt";

    /**
     * Iterates through your live runtime collection arrays and outputs 
     * flat data rows separated by standard parsing tokens (commas).
     */
    public static void saveAllData() {
        // Ensure data directory folder exists
        new File(DATA_DIR).mkdirs();

        // 1. Save Academic Courses
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSE_FILE))) {
            for (Course c : DataManager.getInstance().getCourseList()) {
                writer.println(c.getCourseName() + "," + c.getCourseCode() + "," + 
                               c.getCredit() + "," + c.getInstructor());
            }
        } catch (IOException e) {
            System.err.println("CRITICAL: Failed to write courses file: " + e.getMessage());
        }

        // 2. Save Registered Students
        try (PrintWriter writer = new PrintWriter(new FileWriter(STUDENT_FILE))) {
            for (Student s : DataManager.getInstance().getStudentList()) {
                String courseCode = s.getEnrolledCourse() != null ? s.getEnrolledCourse().getCourseCode() : "NONE";
                writer.println(s.getStudentID() + "," + s.getName() + "," + s.getEmail() + "," + courseCode);
            }
        } catch (IOException e) {
            System.err.println("CRITICAL: Failed to write students file: " + e.getMessage());
        }
    }

    /**
     * Reads local flat files sequentially row-by-row on system startup 
     * and reconstructs objects directly inside your core DataManager singleton list.
     */
    public static void loadAllData() {
        File courseFile = new File(COURSE_FILE);
        File studentFile = new File(STUDENT_FILE);

        // 1. Read Courses first (so students can link to them cleanly)
        if (courseFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(courseFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens.length == 4) {
                        DataManager.getInstance().addCourse(tokens[0], tokens[1], Integer.parseInt(tokens[2]), tokens[3]);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error reloading cached course catalogs: " + e.getMessage());
            }
        }

        // 2. Read Students and rebuild associations
        if (studentFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(studentFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens.length == 4) {
                        int id = Integer.parseInt(tokens[0]);
                        String name = tokens[1];
                        String email = tokens[2];
                        String courseCode = tokens[3].equals("NONE") ? "" : tokens[3];
                        
                        DataManager.getInstance().addStudent(id, name, email, courseCode);
                    }
                }
            } catch (Exception e) {
                System.err.println("Error reloading student registry indexes: " + e.getMessage());
            }
        }
    }
}