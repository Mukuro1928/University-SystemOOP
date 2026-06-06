import java.util.Scanner;
import java.util.InputMismatchException;

public class Main {

    private static Course[] courses = null;
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        University myUni = new University("IIUM", "Selangor, Gombak");
        System.out.println("--- Welcome to the University Management System ---");
        myUni.display();

        courseRegister();
        studentRegister();
        clubRegister();

        input.close();
    }

    // ─── Helper: read a validated positive integer ────────────────────────────
    private static int readPositiveInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = input.nextInt();
                input.nextLine();
                if (value <= 0) {
                    System.out.println("  [!] Please enter a positive number (greater than 0).");
                    continue;
                }
                return value;
            } catch (InputMismatchException e) {
                input.nextLine(); // flush bad token
                System.out.println("  [!] Invalid input. Please enter a whole number.");
            }
        }
    }

    // ─── Helper: read a non-empty trimmed string ──────────────────────────────
    private static String readNonEmptyString(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String value = input.nextLine().trim();
                if (value.isEmpty()) {
                    System.out.println("  [!] Input cannot be empty. Please try again.");
                    continue;
                }
                return value;
            } catch (Exception e) {
                System.out.println("  [!] Error reading input: " + e.getMessage());
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    public static void courseRegister() {
        System.out.println("\n========== COURSE REGISTRATION ==========");

        int countCourse = readPositiveInt("Input the number of courses to register: ");
        courses = new Course[countCourse];

        for (int i = 0; i < courses.length; i++) {
            System.out.println("\n  -- Course " + (i + 1) + " --");
            try {
                String courseName    = readNonEmptyString("Enter course name: ");
                String courseCode    = readNonEmptyString("Enter course code: ");
                int credit           = readPositiveInt("Enter course credit: ");
                String instructorName = readNonEmptyString("Enter instructor's name: ");

                courses[i] = new Course(courseName, courseCode, credit, instructorName);
                System.out.println("  [✓] Course registered successfully.");

            } catch (Exception e) {
                System.out.println("  [!] Unexpected error registering course " + (i + 1) + ": " + e.getMessage());
                System.out.println("  [!] Skipping this course entry.");
                // Leave courses[i] as null; handled safely during display
            }
        }

        System.out.println("\n--- List of Registered Courses ---");
        boolean anyRegistered = false;
        for (int i = 0; i < courses.length; i++) {
            try {
                if (courses[i] != null) {
                    courses[i].displayCourseInfo();
                    anyRegistered = true;
                } else {
                    System.out.println("  [!] Course slot " + (i + 1) + " has no data (skipped during input).");
                }
            } catch (Exception e) {
                System.out.println("  [!] Error displaying course " + (i + 1) + ": " + e.getMessage());
            }
        }
        if (!anyRegistered) {
            System.out.println("  No courses were successfully registered.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    public static void studentRegister() {
        System.out.println("\n========== STUDENT REGISTRATION ==========");

        if (courses == null || courses.length == 0) {
            System.out.println("  [!] No courses available. Please register courses first.");
            return;
        }

        int countStudent = readPositiveInt("Input the number of students to register: ");
        Student[] students = new Student[countStudent];

        for (int i = 0; i < students.length; i++) {
            System.out.println("\n  -- Student " + (i + 1) + " --");
            try {
                int studentID       = readPositiveInt("Enter student ID (numbers only): ");
                String studentName  = readNonEmptyString("Enter student name: ");
                String email        = readNonEmptyString("Enter student email: ");
                String courseCode   = readNonEmptyString("Enter the course code for the enrolled course: ");

                // Find matching course
                Course enrolledCourse = null;
                for (Course course : courses) {
                    if (course != null && course.getCourseCode().equalsIgnoreCase(courseCode)) {
                        enrolledCourse = course;
                        break;
                    }
                }

                if (enrolledCourse == null) {
                    System.out.println("  [!] Warning: Course code \"" + courseCode + "\" not found.");
                    System.out.println("      Student will be registered without an enrolled course.");
                }

                students[i] = new Student(studentID, studentName, email, enrolledCourse);
                System.out.println("  [✓] Student registered successfully.");

            } catch (Exception e) {
                System.out.println("  [!] Unexpected error registering student " + (i + 1) + ": " + e.getMessage());
                System.out.println("  [!] Skipping this student entry.");
            }
        }

        System.out.println("\n--- List of Registered Students ---");
        boolean anyRegistered = false;
        for (int i = 0; i < students.length; i++) {
            try {
                if (students[i] != null) {
                    students[i].displayStudentInfo();
                    anyRegistered = true;
                } else {
                    System.out.println("  [!] Student slot " + (i + 1) + " has no data (skipped during input).");
                }
            } catch (Exception e) {
                System.out.println("  [!] Error displaying student " + (i + 1) + ": " + e.getMessage());
            }
        }
        if (!anyRegistered) {
            System.out.println("  No students were successfully registered.");
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    public static void clubRegister() {
        System.out.println("\n========== CLUB REGISTRATION ==========");

        int countClub = readPositiveInt("Input the number of clubs to register: ");
        Club[] clubArray = new Club[countClub];

        for (int i = 0; i < clubArray.length; i++) {
            System.out.println("\n  -- Club " + (i + 1) + " --");
            try {
                String name          = readNonEmptyString("Enter Club Name: ");
                String pres          = readNonEmptyString("Enter President Name: ");
                String eventName     = readNonEmptyString("Enter an upcoming event for this club: ");
                String eventDate     = readNonEmptyString("Enter event date: ");
                String eventLocation = readNonEmptyString("Enter event location: ");
                int attendeeCount    = readPositiveInt("Enter event attendee count: ");

                clubArray[i] = new Club(name, pres);
                clubArray[i].createEvent(eventName, eventDate, eventLocation, attendeeCount);
                System.out.println("  [✓] Club registered successfully.");

            } catch (Exception e) {
                System.out.println("  [!] Unexpected error registering club " + (i + 1) + ": " + e.getMessage());
                System.out.println("  [!] Skipping this club entry.");
            }
        }

        System.out.println("\n--- List of Registered Clubs and Events ---");
        boolean anyRegistered = false;
        for (int i = 0; i < clubArray.length; i++) {
            try {
                if (clubArray[i] != null) {
                    clubArray[i].showClubDetails();
                    System.out.println("");
                    anyRegistered = true;
                } else {
                    System.out.println("  [!] Club slot " + (i + 1) + " has no data (skipped during input).");
                }
            } catch (Exception e) {
                System.out.println("  [!] Error displaying club " + (i + 1) + ": " + e.getMessage());
            }
        }
        if (!anyRegistered) {
            System.out.println("  No clubs were successfully registered.");
        }
    }
}