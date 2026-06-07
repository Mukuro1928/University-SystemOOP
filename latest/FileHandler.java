import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler 
{
    private static final String STUDENT_FILE = "students.txt";
    private static final String INSTRUCTOR_FILE = "instructors.txt";
    private static final String COURSE_FILE = "courses.txt";
    private static final String CLUB_FILE = "clubs.txt";
    private static final String EVENT_FILE = "event.txt";
    // ================================================================== //
    // 7. READ AND LOAD OPERATION
    // ================================================================== //
    
    public static void saveStudents()
    {
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(STUDENT_FILE);
            for (Student s : DataManager.getStudentList())
            {
                fileWriter.write(s.getStudentID() + "," + s.getName() + "," + s.getEmail() + "\n");
            }  
            
            System.out.println("Students saved.");
        }
        
        catch (IOException e) 
        {
            System.out.println("Error saving students: " + e.getMessage());
        } 
        
        finally 
        {
            try {
                if (fileWriter != null) 
                    fileWriter.close();
            } 
            
            catch (IOException e) 
            {
                System.out.println("Error closing file: " + e.getMessage());
            }
        }
    }
    
    public static void loadStudents()
    {
        ArrayList<Student> list = DataManager.getStudentList();
        list.clear();
        
        try{
            File file = new File(STUDENT_FILE);
            Scanner sc = new Scanner(file);
            
            while(sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                
                if(parts.length == 3)
                {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    
                    list.add(new Student(id, name, email, null));
                }
            }
            
            sc.close();        
            System.out.println("Students loaded");
        }
        
        catch (FileNotFoundException e)
        {
            System.out.println("No student file found. Starting new file");
            saveStudents();
        }
        catch (Exception e)
        {
            System.out.println("Error loading students: " + e.getMessage());
        }
    }
    
//==============================================================================

    public static void saveInstructors()
    {
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(INSTRUCTOR_FILE);
            for (Instructor i : DataManager.getInstructorList())
            {
                fileWriter.write(i.getEmployeeID() + "," + i.getName() + "," + i.getEmail() + "\n");
            }  
            
            System.out.println("Instructor saved.");
        }
        
        catch (IOException e) 
        {
            System.out.println("Error saving Instructor: " + e.getMessage());
        } 
        
        finally 
        {
            try {
                if (fileWriter != null) 
                    fileWriter.close();
            } 
            
            catch (IOException e) 
            {
                System.out.println("Error closing file: " + e.getMessage());
            }
        }
    }
    
    public static void loadInstructors()
    {
        ArrayList<Instructor> list = DataManager.getInstructorList();
        list.clear();
        
        try{
            File file = new File(INSTRUCTOR_FILE);
            Scanner sc = new Scanner(file);
            
            while(sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                
                if(parts.length == 3)
                {
                    String id = parts[0];
                    String name = parts[1];
                    String email = parts[2];
                    
                    list.add(new Instructor(id, name, email));
                }
            }
            
            sc.close();        
            System.out.println("Instructors loaded");
        }
        
        catch (FileNotFoundException e)
        {
            System.out.println("No instructors file found. Starting new file");
            saveInstructors();
        }
        catch (Exception e)
        {
            System.out.println("Error loading instructors: " + e.getMessage());
        }
    }
    
//==============================================================================

    public static void saveCourse()
    {
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(COURSE_FILE);
            for (Course c : DataManager.getCourseList())
            {
                fileWriter.write(c.getCourseName()+ "," + c.getCourseCode() + "," + c.getCredit() + "," + c.getInstructor()+ "\n");
            }  
            
            System.out.println("Course saved.");
        }
        
        catch (IOException e) 
        {
            System.out.println("Error saving Course: " + e.getMessage());
        } 
        
        finally 
        {
            try {
                if (fileWriter != null) 
                    fileWriter.close();
            } 
            
            catch (IOException e) 
            {
                System.out.println("Error closing file: " + e.getMessage());
            }
        }
    }
    
    public static void loadCourse()
    {
        ArrayList<Course> list = DataManager.getCourseList();
        list.clear();
        
        try{
            File file = new File(COURSE_FILE);
            Scanner sc = new Scanner(file);
            
            while(sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                
                if(parts.length == 4)
                {
                    String courseName = parts[0];
                    String courseCode = parts[1];
                    int credit = Integer.parseInt(parts[2]);
                    String instructor = parts[3];
                    
                    list.add(new Course(courseName, courseCode, credit, instructor));
                }
            }
            
            sc.close();        
            System.out.println("Courses loaded");
        }
        
        catch (FileNotFoundException e)
        {
            System.out.println("No courses file found. Starting new file");
            saveCourse();
        }
        catch (Exception e)
        {
            System.out.println("Error loading courses: " + e.getMessage());
        }
    }
    
//==============================================================================

    public static void saveClubs()
    {
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(CLUB_FILE);
            for (Club c : DataManager.getClubList())
            {
                fileWriter.write(c.getClubName() + "," + c.getPresidentName() + "\n");
            }  
            
            System.out.println("Clubs saved.");
        }
        
        catch (IOException e) 
        {
            System.out.println("Error saving Clubs: " + e.getMessage());
        } 
        
        finally 
        {
            try {
                if (fileWriter != null) 
                    fileWriter.close();
            } 
            
            catch (IOException e) 
            {
                System.out.println("Error closing file: " + e.getMessage());
            }
        }
    }
    
    public static void loadClubs()
    {
        ArrayList<Club> list = DataManager.getClubList();
        list.clear();
        
        try{
            File file = new File(CLUB_FILE);
            Scanner sc = new Scanner(file);
            
            while(sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                
                if(parts.length == 2)
                {
                    String clubName = parts[0];
                    String president = parts[1];
                    
                    list.add(new Club(clubName, president));
                }
            }
            
            sc.close();        
            System.out.println("Clubs loaded");
        }
        
        catch (FileNotFoundException e)
        {
            System.out.println("No clubs file found. Starting new file");
            saveClubs();
        }
        catch (Exception e)
        {
            System.out.println("Error loading clubs: " + e.getMessage());
        }
    }
    
//==============================================================================

    public static void saveEvents()
    {
        FileWriter fileWriter = null;
        try{
            fileWriter = new FileWriter(EVENT_FILE);
            for (Event e : DataManager.getEventList())
            {
                fileWriter.write(e.getEventName() + "," + e.getEventDate() + "," + e.getEventLocation() + "," + e.getAttendeeCount() + "\n");
            }  
            
            System.out.println("Events saved.");
        }
        
        catch (IOException e) 
        {
            System.out.println("Error saving Events: " + e.getMessage());
        } 
        
        finally 
        {
            try {
                if (fileWriter != null) 
                    fileWriter.close();
            } 
            
            catch (IOException e) 
            {
                System.out.println("Error closing file: " + e.getMessage());
            }
        }
    }
    
    public static void loadEvents()
    {
        ArrayList<Event> list = DataManager.getEventList();
        list.clear();
        
        try{
            File file = new File(EVENT_FILE);
            Scanner sc = new Scanner(file);
            
            while(sc.hasNextLine())
            {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                
                if(parts.length == 4)
                {
                    String name = parts[0];
                    String date = parts[1];
                    String loc = parts[2];
                    int attendees = Integer.parseInt(parts[3]);
                    
                    list.add(new Event(name, date, loc, attendees));
                }
            }
            
            sc.close();        
            System.out.println("Events loaded");
        }
        
        catch (FileNotFoundException e)
        {
            System.out.println("No events file found. Starting new file");
            saveEvents();
        }
        catch (Exception e)
        {
            System.out.println("Error loading events: " + e.getMessage());
        }
    }
    
    public static void saveAll() 
    {
        saveStudents();
        saveInstructors();
        saveCourse();
        saveClubs();
        saveEvents();
    }
    public static void loadAll()
    {
        loadStudents();
        loadInstructors();
        loadCourse();
        loadClubs();
        loadEvents();
    }
}
