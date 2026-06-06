import java.util.Scanner;

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

    public static void courseRegister() {
        System.out.print("\nInput the number of course to register: ");
        int countCourse = input.nextInt();
        input.nextLine();

        courses = new Course[countCourse];

        for (int i = 0; i < courses.length; i++) {
            System.out.print("Enter course " + (i + 1) + " name: ");
            String courseName = input.nextLine();

            System.out.print("Enter course code: ");
            String courseCode = input.nextLine();

            System.out.print("Enter course credit: ");
            int credit = input.nextInt();
            input.nextLine();

            System.out.print("Enter instructor's name: ");
            String instructorName = input.nextLine();

            courses[i] = new Course(courseName, courseCode, credit, instructorName);
            System.out.println("");
        }

        System.out.println("\n--- List of Registered Courses ---");
        for (Course course : courses) {
            course.displayCourseInfo();
        }
    }

    public static void studentRegister() {
        System.out.print("\nInput the number of student who want to register: ");
        int countStudent = input.nextInt();
        input.nextLine();

        Student[] students = new Student[countStudent];

        for (int i = 0; i < students.length; i++) {
            int studentID;
            System.out.print("Enter student " + (i + 1) + " ID: ");
            studentID = input.nextInt();
            input.nextLine();

            System.out.print("Enter student name: ");
            String studentName = input.nextLine();

            System.out.print("Enter student email: ");
            String email = input.nextLine();

            System.out.print("Enter the course code for the enrolled course: ");
            String courseCode = input.nextLine();

            Course enrolledCourse = null;
            for (Course course : courses) {
                if (course.getCourseCode().equals(courseCode)) {
                    enrolledCourse = course;
                    break;
                }
            }

            students[i] = new Student(studentID, studentName, email, enrolledCourse);
            System.out.println("");
        }

        System.out.println("\n--- List of Registered Students ---");
        for (Student student : students) {
            student.displayStudentInfo();
        }
    }

    public static void clubRegister() {
        System.out.print("\nInput the number of clubs who want to register: ");
        int countClub = input.nextInt();
        input.nextLine();

        Club[] clubArray = new Club[countClub];

        for (int i = 0; i < clubArray.length; i++) {
            System.out.print("\nEnter Name for Club " + (i + 1) + ": ");
            String name = input.nextLine();

            System.out.print("Enter President Name: ");
            String pres = input.nextLine();

            System.out.print("Enter an upcoming event for this club: ");
            String eventName = input.nextLine();

            System.out.print("Enter event date: ");
            String eventDate = input.nextLine();

            System.out.print("Enter event location: ");
            String eventLocation = input.nextLine();

            System.out.print("Enter event attendee count for the event: ");
            int attendeeCount = input.nextInt();
            input.nextLine();

            clubArray[i] = new Club(name, pres);
            clubArray[i].createEvent(eventName, eventDate, eventLocation, attendeeCount);
        }

        System.out.println("\n--- List of Registered Clubs and Events ---");
        for (Club club : clubArray) {
            club.showClubDetails();
            System.out.println("");
        }
    }
}
