/**
 * Developed by: MUHAMMAD AIMAN BIN SUFIAN 2511349
 * Person.java — Abstract Base Class
 * =================================
 * Serves as a base class for all person-related entities in the system.
 *
 * Features:
 * • Stores common attributes such as name and email.
 * • Provides getter and setter methods for shared properties.
 * • Acts as a superclass for more specific roles like Instructor.
 *
 * Architecture:
 * • Abstract class used to enforce a common structure across subclasses.
 * • Promotes code reusability by centralizing shared attributes.
 * • Designed to be extended, not instantiated directly.
 *
 * Note:
 * • Does not define abstract methods, but still prevents direct instantiation.
 * • Focuses only on shared data representation for all person-type entities.
 */


public abstract class Person {
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
