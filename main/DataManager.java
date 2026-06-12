import java.util.ArrayList;

/**
 * Developed by: MUHAMMAD A'THIF UZAIR BIN SHAEDIN - 2514847
 * DataManager.java — Central Data Management Module
 * =================================================
 * Serves as the central repository and controller for all system data
 * within the University Management System.
 *
 * Responsibilities:
 *  • Store and manage collections of Students, Instructors,
 *    Courses, and Clubs.
 *  • Perform CRUD-related operations on system records.
 *  • Validate input data before creating or modifying records.
 *  • Maintain relationships between entities such as
 *    student-course enrollment and club-event associations.
 *  • Provide search and lookup functionality for all entities.
 *
 * Architecture:
 *  • Implements the Singleton design pattern to ensure a single
 *    shared data source throughout the application.
 *  • Uses ArrayLists as the primary in-memory data storage.
 *  • Acts as the intermediary between the user interface
 *    workspaces and the system's domain objects.
 *
 * Features:
 *  • Student Management (Add, Edit, Delete, Search)
 *  • Instructor Management (Add, Edit, Delete, Search)
 *  • Course Management (Add, Delete, Search)
 *  • Club and Event Management (Add, Delete, Search)
 *  • Automatic handling of entity relationships when records
 *    are modified or removed.
 *
 * Design:
 *  • Functions as the application's business logic layer,
 *    ensuring data consistency and centralized control over
 *    all university management operations.
 */

public class DataManager {
    private static ArrayList<Student> studentList       = new ArrayList<>();
    private static ArrayList<Instructor> instructorList = new ArrayList<>();
    private static ArrayList<Course> courseList         = new ArrayList<>();
    private static ArrayList<Club> clubList             = new ArrayList<>();

    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager() {}

    public ArrayList<Student> getStudentList()       { return studentList; }
    public ArrayList<Instructor> getInstructorList() { return instructorList; }
    public ArrayList<Course> getCourseList()         { return courseList; }
    public ArrayList<Club> getClubList()             { return clubList; }

    public void addStudent(int studentID, String name, String email, String courseCode) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        if (studentID <= 0) throw new IllegalArgumentException("Student ID must be a positive number.");
        if (findStudentByID(studentID) != null) throw new IllegalArgumentException("Student ID already exists.");

        // Lookup optional course association
        Course enrolled = findCourseByCode(courseCode);
        studentList.add(new Student(studentID, name.trim(), email.trim(), enrolled));
    }

    public boolean editStudent(int studentID, String newName, String newEmail, String newCourseCode) {
        Student student = findStudentByID(studentID);
        if (student == null) return false;

        if (newName != null && !newName.isBlank()) student.setName(newName.trim());
        if (newEmail != null && !newEmail.isBlank()) student.setEmail(newEmail.trim()); 
        student.setEnrolledCourse(findCourseByCode(newCourseCode));
        return true;
    }

    public boolean deleteStudent(int studentID) {
        return studentList.removeIf(s -> s.getStudentID() == studentID);
    }
    
    public void addInstructor(String employeeID, String name, String email) {
        if (employeeID == null || employeeID.isBlank()) throw new IllegalArgumentException("Employee ID cannot be empty.");
        if (findInstructorByID(employeeID) != null) throw new IllegalArgumentException("Employee ID already exists.");

        instructorList.add(new Instructor(employeeID.trim(), name.trim(), email.trim()));
    }

    public boolean editInstructor(String employeeID, String newName, String newEmail) {
        Instructor instructor = findInstructorByID(employeeID);
        if (instructor == null) return false;

        if (newName != null && !newName.isBlank()) instructor.setName(newName.trim());
        if (newEmail != null && !newEmail.isBlank()) instructor.setEmail(newEmail.trim());
        return true;
    }

    public boolean deleteInstructor(String employeeID) {
        return instructorList.removeIf(i -> i.getEmployeeID().equalsIgnoreCase(employeeID));
    }

    public void addCourse(String name, String code, int credit, String instructorName) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Course code cannot be empty.");
        if (findCourseByCode(code) != null) throw new IllegalArgumentException("Course code already exists.");

        courseList.add(new Course(name.trim(), code.trim().toUpperCase(), credit, instructorName.trim()));
    }

    public boolean editCourse(String code, String newName, int newCredit, String newInstructor) {
        Course course = findCourseByCode(code);
        if (course == null) return false;

        if (newName != null && !newName.isBlank()) course.setCourseName(newName.trim());
        if (newCredit > 0) course.setCredit(newCredit);
        if (newInstructor != null && !newInstructor.isBlank()) course.setInstructor(newInstructor.trim());
        return true;
    }

    public boolean deleteCourse(String code) {
        Course target = findCourseByCode(code);
        if (target == null) return false;

        for (Student s : studentList) {
            if (target.equals(s.getEnrolledCourse())) {
                s.setEnrolledCourse(null);
            }
        }
        return courseList.remove(target);
    }

    public void addClub(String clubName, String presidentName, String eventName, String eventDate, String eventLoc, int attendees) {
        if (clubName == null || clubName.isBlank()) throw new IllegalArgumentException("Club name cannot be empty.");
        if (findClubByName(clubName) != null) throw new IllegalArgumentException("Club already exists.");

        Club newClub = new Club(clubName.trim(), presidentName.trim());
        if (eventName != null && !eventName.isBlank()) {
            newClub.createEvent(eventName.trim(), eventDate.trim(), eventLoc.trim(), attendees);
        }
        clubList.add(newClub);
    }

    public boolean editClub(String clubName, String newPresident, String eventName, String eventDate, String eventLoc, int attendees) {
        Club club = findClubByName(clubName);
        if (club == null) return false;

        if (newPresident != null && !newPresident.isBlank()) club.setPresidentName(newPresident.trim());
        if (eventName != null && !eventName.isBlank()) {
            club.createEvent(eventName.trim(), eventDate.trim(), eventLoc.trim(), attendees);
        } else {
            club.setEvent(null);
        }
        return true;
    }

    public boolean deleteClub(String clubName) {
        if (findClubByName(clubName) == null) throw new IllegalArgumentException("Club does not exist.");
        
        return clubList.removeIf(c -> c.getClubName().equalsIgnoreCase(clubName));
    }

    public Student findStudentByID(int id) {
        for (Student s : studentList) if (s.getStudentID() == id) return s;
        return null;
    }

    public Instructor findInstructorByID(String id) {
        for (Instructor i : instructorList) if (i.getEmployeeID().equalsIgnoreCase(id)) return i;
        return null;
    }

    public Course findCourseByCode(String code) {
        if (code == null || code.isBlank()) return null;
        for (Course c : courseList) if (c.getCourseCode().equalsIgnoreCase(code.trim())) return c;
        return null;
    }

    public Club findClubByName(String name) {
        if (name == null || name.isBlank()) return null;
        for (Club c : clubList) if (c.getClubName().equalsIgnoreCase(name.trim())) return c;
        return null;
    }
}