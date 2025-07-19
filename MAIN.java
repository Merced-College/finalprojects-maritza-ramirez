//Name: Maritza Ramirez
//Date: July 14, 2025
//This program is 

//*************************************IMPORTS*************************************
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.io.File;

//*************************************DEFINING METHODS************************************
public class MAIN{
    public static class studentINPUT{
    int schoolYear;
    String semester;
    boolean wantsSummer;
    LinkedList<String> completedCourses;
    }
    
public class IGETCPlanner {
    static final String[] IGETC_AREAS = {
        "Area 1A: English Communication",
        "Area 1B: Critical Thinking",
        "Area 1C: Oral Communication (CSU only)",
        "Area 2: Mathematical Concepts and Quantitative Reasoning",
        "Area 3A: Arts",
        "Area 3B: Humanities",
        "Area 4: Social and Behavioral Sciences",
        "Area 5A: Physical Sciences",
        "Area 5B: Biological Sciences",
        "Area 6: Language Other than English (UC only)",
        "Area 7: Ethnic Studies",
    };

    static HashMap<String, Course[]> geCourses = new HashMap<>();
    static LinkedList<Course> plannedCourses = new LinkedList<>();
    static Queue<String> semesterQueue = new LinkedList<>();

    //*************************************START OF PROGRAM*************************************
    public static void main(String[] args) {
    Scanner scan = new Scanner(System.in);
    
    //Program message
    System.out.println("*************************************START OF PLANNING*************************************");
    
    //*************************************START OF PLANNING*************************************
    //Welcome message
    System.out.println("Welcome to the college course planning program!");

    //Schoool year user input
    System.out.println("What is your current school year? (9, 10, 11, 12)");
    //scanning to get response
    int schoolYear = scan.nextInt();
    scan.nextLine(); // Consume the newline character left by nextInt()

    //Semester user input
    System.out.println("What semester are you currently in? (Fall, Spring, Summer)");
    //scanning to get response
    String semester = scan.nextLine();

    //Summer courses user input
    System.out.println("Do you want to take summer courses? (yes or no)");
    //scanning to get response
    String summerResponse = scan.nextLine();
    //Converting response to boolean
    boolean wantsSummer = summerResponse.equalsIgnoreCase("yes");
    
    //Creating student
    studentINPUT student = new studentINPUT();
    student.schoolYear = schoolYear;
    student.semester = semester;
    student.wantsSummer = wantsSummer;
    student.completedCourses = new LinkedList<>();

    //Completed courses user input
    System.out.println("Have you completed any college courses? (yes/no): ");
        if (scan.nextLine().equalsIgnoreCase("yes")) {
            while (true) {
                System.out.print("Enter course name (or type '-4' to finish): ");
                String completed = scan.nextLine();
                if (completed.equals("-4")) break;
                student.completedCourses.add(completed.toUpperCase());
            }
        } else {
            System.out.println("No completed courses recorded.");
        }
        initializeCourses();
        generateSemesterQueue(student);

    //NOTES ON REMINDERS AND LIMITS OF PROGRAM
    System.out.println("*************************************NOTES*************************************");
    System.out.println("Alright! Now having previous knowledge of your current standing this program will promopt you questions to help decide what courses you should take in the nextsemesters according to the highschool equivalency credit. Doing this will help you get college credit equivalent to highschool which boosts your GPA if you \nget a good grade in the class and could finish college and highschool requirements. But as well keep in mind that if you get a low grade in the class it will \nlower your GPA. So do your best! ");

    System.out.println("REMINDER: This is only taking into account the highschool equivalency credit classes unless absolutely necessary to take a course not equivalent. As well it \ndoes not take into account majors yet.");
    System.out.println("**If you want to complete a major I suggest you look directly through the college website for associates and see if there is any overlapping with general ed.");
    
    //IGETC AREA COURSE COMPLETED VALIDATION
    for (String area : IGETC_AREAS) {
            Course[] options = geCourses.get(area);

            // Skip already completed
            boolean alreadyCompleted = false;
            for (Course c : options) {
                if (student.completedCourses.contains(c.name.toUpperCase())) {
                    System.out.println("Skipping " + area + " (already completed via " + c.name + ")");
                    alreadyCompleted = true;
                    break;
                }
            }
            if (alreadyCompleted) continue;
    
    //*************************************COURSE PLANNING*************************************
    System.out.println("*************************************COURSE PLANNING*************************************");

    //Ending message
    System.out.println("*************************************END OF PLANNING*************************************");
}
}