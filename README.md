# University Management System (UMS)

An interactive, graphical desktop application built with **JavaFX** that provides a centralized platform to seamlessly connect university student registration, course enrollment, club management, and event creation. This project is developed to fulfill the requirements of the **BICS 1306: Object-Oriented Programming** course at the International Islamic University Malaysia (IIUM).

---

## 🚀 Key Features

* **User Authentication (Secure Login):** Secure portal utilizing local storage to manage account creation, credential verification, and user sessions.
* **Centralized Navigation Dashboard:** A clean, unified JavaFX graphical interface allowing swift navigation between student, course, and club modules.
* **Full Transaction Management (CRUD):** Complete database interactions allowing authorized users to **Add, Edit, and Delete** student profiles, instructor details, course options, and campus clubs interactively.
* **Dynamic Data Architecture:** Driven entirely by memory-efficient `ArrayList` dynamic collections to handle expanding university records seamlessly.
* **Data Persistence (Local File I/O):** Real-time flat text file backup engines ensuring all data states are saved on exit and reconstructed automatically on application boot.

---

## 👥 Team Members & Role Allocation

The codebase is built collaboratively with explicit separation of concerns mapped across our 6-member project team:

| Member Name | Core Responsibility | Technical Implementation Focus |
| :--- | :--- | :--- |
| **Ahmad Mukhlis bin Zakariah** | Project Manager & Git Admin | Repository workflows, merge management, and User Authentication layout/logic. |
| **Ahmad Zuhdi bin Mohd Soud** | Lead GUI Architect | Core JavaFX structural layout skeleton (`Stage`, sidebar navigation, responsive containers). |
| **Muhammad A'thif Uzair bin Shaedin** | Backend Developer 1 | OOP Inheritance Class Hierarchy design (`Person`, `Student`, `Instructor`) and encapsulation optimization. |
| **Amir Nuzil Irfan bin Amir Zahariman** | Backend Developer 2 | Dynamic core database setup (`DataManager` Singleton), input verification rules, and relational search engines. |
| **Iqmal Hakimi bin Suhaimi** | Feature Developer 1 | UI workspace and backend CRUD wiring for Course & Club/Event operations. |
| **Muhammad Aiman bin Sufian** | Feature Developer 2 & File Handler | UI workspace for Student management and local flat text file I/O pipelines. |

---

## 🏗️ Object-Oriented Programming (OOP) Architecture

To maximize code reusability and maintain strong separation of concerns, the program strictly incorporates standard OOP design patterns:

1.  **Inheritance:** To reduce data duplication, a shared parent model class `Person` is utilized and cleanly extended by `Student` and `Instructor` subclasses.
2.  **Encapsulation:** All attributes across classes (`Student`, `Instructor`, `Course`, `Club`, `Event`) are secured using `private` modifiers and accessed strictly via standardized getter and setter methods.
3.  **The Singleton Pattern:** Data operations channel through a single runtime source of truth (`DataManager`), ensuring that user actions, UI visual tables, and text files remain fully synced.

---

## 🛠️ Requirements & Installation

### Prerequisites
* **Java Development Kit (JDK):** Version 17 or higher.
* **JavaFX SDK:** Version 17 or higher compatible with your platform runtime environment.
* An IDE (IntelliJ IDEA, Eclipse, or NetBeans) with JavaFX support configured.

---

## 📂 Data Storage File Structure

Application data persists in flat text formats situated directly within the project's working directory root:

* `users.txt`: Encrypted credential pairings for authorized application access.

* `students.txt`: Sequential text rows capturing student IDs, names, emails, and active course associations.

* `courses.txt`: Academic records maintaining course identities, credits, and tracking assigned instructors.

* `clubs.txt`: Extracurricular registry mapping organizations, presidents, and linked event details.
