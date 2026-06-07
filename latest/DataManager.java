import java.util.ArrayList;

public class DataManager {

    // ================================================================== //
    // 1. Dynamic ArrayList Collections (Replaces Old Fixed Arrays)
    // ================================================================== //
    private ArrayList<Student> studentList       = new ArrayList<>();
    private ArrayList<Instructor> instructorList = new ArrayList<>();
    private ArrayList<Course> courseList         = new ArrayList<>();
    private ArrayList<Club> clubList             = new ArrayList<>();

    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private DataManager() {}

    // ================================================================== //
    // 2. Accessor Getters (Used by JavaFX TableViews)
    // ================================================================== //
    public ArrayList<Student> getStudentList()       { return studentList; }
    public ArrayList<Instructor> getInstructorList() { return instructorList; }
    public ArrayList<Course> getCourseList()         { return courseList; }
    public ArrayList<Club> getClubList()             { return clubList; }

    // ================================================================== //
    // 3. STUDENT CRUD Operations (Using Inherited Person Properties)
    // ================================================================== //
    
    // CREATE: Validates inputs and adds a new Student to the collection
    public void addStudent(int studentID, String name, String email, String courseCode) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name cannot be empty.");
        if (studentID <= 0) throw new IllegalArgumentException("Student ID must be a positive number.");
        if (findStudentByID(studentID) != null) throw new IllegalArgumentException("Student ID already exists.");

        // Lookup optional course association
        Course enrolled = findCourseByCode(courseCode);
        studentList.add(new Student(studentID, name.trim(), email.trim(), enrolled));
    }

    // UPDATE: Modifies an existing student's fields using inherited setters
    public boolean editStudent(int studentID, String newName, String newEmail, String newCourseCode) {
        Student student = findStudentByID(studentID);
        if (student == null) return false;

        if (newName != null && !newName.isBlank()) student.setName(newName.trim()); // Inherited setter
        if (newEmail != null && !newEmail.isBlank()) student.setEmail(newEmail.trim()); // Inherited setter
        student.setEnrolledCourse(findCourseByCode(newCourseCode));
        return true;
    }

    // DELETE: Removes a student from the dynamic collection instantly
    public boolean deleteStudent(int studentID) {
        return studentList.removeIf(s -> s.getStudentID() == studentID);
    }

    // ================================================================== //
    // 4. INSTRUCTOR CRUD Operations
    // ================================================================== //
    
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

    // ================================================================== //
    // 5. COURSE & CLUB CRUD Operations
    // ================================================================== //

    public void addCourse(String name, String code, int credit, String instructorName) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("Course code cannot be empty.");
        if (findCourseByCode(code) != null) throw new IllegalArgumentException("Course code already exists.");

        courseList.add(new Course(name.trim(), code.trim().toUpperCase(), credit, instructorName.trim()));
    }

    public boolean deleteCourse(String code) {
        Course target = findCourseByCode(code);
        if (target == null) return false;

        // Clean up un-enrollments so no broken references remain
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

    public boolean deleteClub(String clubName) {
        if (findClubByName(clubName) == null) throw new IllegalArgumentException("Club does not exist.");
        if (findClubByName(clubName).hasEvents()) throw new IllegalStateException("Cannot delete club with existing events. Please remove events first.");
        
        return clubList.removeIf(c -> c.getClubName().equalsIgnoreCase(clubName));
    }

    // ================================================================== //
    // 6. Object Lookup Helpers (Internal Linkage Keys)
    // ================================================================== //
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