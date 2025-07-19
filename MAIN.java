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
    static class Course {
        String name;
        boolean countsForHS;

        Course(String name, boolean countsForHS) {
            this.name = name;
            this.countsForHS = countsForHS;
        }
    }   

    static class studentINPUT {
        int schoolYear;
        String semester;
        boolean wantsSummer;
        LinkedList<String> completedCourses;

        studentINPUT(int schoolYear, String semester, boolean wantsSummer, LinkedList<String> completedCourses) {
            this.schoolYear = schoolYear;
            this.semester = semester;
            this.wantsSummer = wantsSummer;
            this.completedCourses = completedCourses;
        }
    }

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

        //Welcome message
        System.out.println("Welcome to the college course planning program!");

        //School year user input
        System.out.println("What is your current school year? (9, 10, 11, 12)");
        int schoolYear = scan.nextInt();
        scan.nextLine(); // Consume the newline character left by nextInt()

        //Semester user input
        System.out.println("What semester are you currently in? (Fall, Spring, Summer)");
        String semester = scan.nextLine();

        //Summer courses user input
        System.out.println("Do you want to take summer courses? (yes or no)");
        String summerResponse = scan.nextLine();
        boolean wantsSummer = summerResponse.equalsIgnoreCase("yes");

        //Creating student
        studentINPUT student = new studentINPUT(schoolYear, semester, wantsSummer, new LinkedList<>());

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
        generatePLAN(student, scan);

        //NOTES ON REMINDERS AND LIMITS OF PROGRAM
        System.out.println("*************************************NOTES*************************************");
        System.out.println("Alright! Now having previous knowledge of your current standing this program will prompt you questions to help decide what courses you should take in the next semesters according to the high school equivalency credit. Doing this will help you get college credit equivalent to high school which boosts your GPA if you get a good grade in the class and could finish college and high school requirements. But as well keep in mind that if you get a low grade in the class it will lower your GPA. So do your best! ");


        System.out.println("REMINDER: This is only taking into account the high school equivalency credit classes unless absolutely necessary to take a course not equivalent. As well it does not take into account majors yet.");
        System.out.println("**If you want to complete a major I suggest you look directly through the college website for associates and see if there is any overlapping with general ed.");
        System.out.println("(MUST FINISH INTEG MATH 3 FOR AREA 2) - This is a requirement for the math area, so if you have not completed it, you will need to take it before proceeding with other math courses.");
    }

    static void generatePLAN(studentINPUT student, Scanner scan) {
        String[] semesters = {"Fall", "Spring", "Summer"};
        int startIndex = 0;

        // Determine starting semester based on current semester
        if (student.semester.equalsIgnoreCase("Spring")) {
            startIndex = 1; // Start from Spring
        } else if (student.semester.equalsIgnoreCase("Summer")) {
            startIndex = 2; // Start from Summer
        }

        // Add semesters to queue
        for (int i = startIndex; i < semesters.length; i++) {
            semesterQueue.add(semesters[i]);
        }
        if (student.wantsSummer) {
            semesterQueue.add("Summer");
        }   

        //*************************************COURSE PLANNING*************************************
        System.out.println("*************************************COURSE PLANNING*************************************");

        // IGETC AREA COURSE COMPLETED VALIDATION
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
            System.out.println("\n" + area);
            for (int i = 0; i < options.length; i++) {
                String marker = options[i].countsForHS ? "(*)" : "";
                System.out.println((i + 1) + ". " + options[i].name + " " + marker);
            }

            System.out.print("Choose a course number for this area: ");
            int choice = scan.nextInt();
            scan.nextLine();
            plannedCourses.add(options[choice - 1]);
        }
        System.out.println("\nGeneral Education Plan:");
        recursivePlanner(plannedCourses, semesterQueue);

        //Ending message
        System.out.println("*************************************END OF PLANNING*************************************");
    }

    static void recursivePlanner(LinkedList<Course> courses, Queue<String> semesters) {
        if (courses.isEmpty() || semesters.isEmpty()) return;

        String sem = semesters.poll();
        System.out.println("\nSemester: " + sem);

        int slots = Math.min(2, courses.size());
        for (int i = 0; i < slots; i++) {
            Course course = courses.removeFirst();
            String marker = course.countsForHS ? "(*)" : "";
            System.out.println("- " + course.name + " " + marker);
        }

        recursivePlanner(courses, semesters);
    }

    static void initializeCourses() {
        // Area 1A: English Communication
        geCourses.put("Area 1A: English Communication", new Course[]{
            new Course("English 1A", true)
        });

        // Area 1B: Critical Thinking
        geCourses.put("Area 1B: Critical Thinking", new Course[]{
            new Course("ENGL 13", true),
        });

        // Area 1C: Oral Communication (CSU only)
        geCourses.put("Area 1C: Oral Communication (CSU only)", new Course[]{
            new Course("COMM 1", false),
        });

        // Area 2: Mathematical Concepts and Quantitative Reasoning
        geCourses.put("Area 2: Mathematical Concepts and Quantitative Reasoning", new Course[]{
            new Course("Math 02", true),
            new Course("Math 10", true)
        });

        // Area 3A: Arts
        geCourses.put("Area 3A: Arts", new Course[]{
            new Course("ART 15", true),
            new Course("ART 24", true),
            new Course("ARTD 40A", true),
            new Course("MUSG 10", true),
            new Course("MUSG 14", true)
        });

        // Area 3B: Humanities
        geCourses.put("Area 3B: Humanities", new Course[]{
            new Course("History 17A", true),
            new Course("History 17B", true)
        });

        // Area 4: Social and Behavioral Sciences
        geCourses.put("Area 4: Social and Behavioral Sciences", new Course[]{
            new Course("CLDV 01", true),
            new Course("CLDV 02", true)
        });

        // Area 5A: Physical Sciences
        geCourses.put("Area 5A: Physical Sciences", new Course[]{
            new Course("CHEM 02A", true),
            new Course("PHYS 02A", true)
        });

        // Area 5B: Biological Sciences
        geCourses.put("Area 5B: Biological Sciences", new Course[]{
            new Course("BIOL 01", true)
        });

        // Area 6: Language Other than English (UC only)
        geCourses.put("Area 6: Language Other than English (UC only)", new Course[]{
            new Course("SPAN 01", true),
        });

        // Area 7: Ethnic Studies
        geCourses.put("Area 7: Ethnic Studies", new Course[]{
            new Course("ETHN 01", true)
        });
    }
}
