//Name: Maritza Ramirez
//Date: July 14, 2025
//This program is a way for college students to plan their courses based on high school equivalency credits.
//It allows students to select courses from different IGETC areas and generates a plan based on

//*************************************IMPORTS*************************************
import java.util.Scanner;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

//*************************************DEFINING METHODS************************************
// This class is used to plan college courses based on high school equivalency credits
// It allows students to select courses from different IGETC areas and generates a plan based on current school year, semester, and completed courses.
public class DuelEnrollment{
    // The Course record holds the name of the course and whether it counts for high school credit
    record Course(String name, boolean countsForHS) {} // Record to hold course information
    // This class holds the student input data
    static class studentINPUT {
        int schoolYear;
        String semester;
        boolean wantsSummer;
        LinkedList<String> completedCourses;

        // Constructor for studentINPUT
        // Initializes the student with school year, semester, summer course preference, and completed courses
        studentINPUT(int schoolYear, String semester, boolean wantsSummer, LinkedList<String> completedCourses) {
            this.schoolYear = schoolYear;
            this.semester = semester;
            this.wantsSummer = wantsSummer;
            this.completedCourses = completedCourses;
        }
    }

    // This scanner is used to read user input from the console
    private static final Scanner scan = new Scanner(System.in);

    // Generic method to get validated input
    public static <T> T getValidatedInput(String prompt, List<T> validOptions, Class<T> type) {
        System.out.println(prompt);
        // Initialize the input variable
        T input = null;

        // This loop will continue until the user provides a valid input that matches one of the valid
        while (true) {
            try {
                String userInput = scan.nextLine().trim();

                // Check input type and parse accordingly
                if (type == Integer.class) {
                    input = type.cast(Integer.parseInt(userInput));
                } else if (type == String.class) {
                    input = type.cast(userInput);
                }

                // Check if the input is in the list of valid options
                if (validOptions.contains(input)) {
                    return input;
                } else {
                    System.out.println("Invalid option. Try again:");
                }

            
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a " + type.getSimpleName() + ".");
            }
        }
    } // End of getValidatedInput

    // This array holds the IGETC areas in which will be called later in the program
    // Each area corresponds to a specific set of courses that students can choose from
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

    // HashMap to hold courses for each IGETC area
    static HashMap<String, Course[]> geCourses = new HashMap<>();
    // LinkedList to hold planned courses, Queue for semesters, and Stack for undo functionality
    static LinkedList<Course> plannedCourses = new LinkedList<>();
    // Queue to hold semesters for planning
    static Queue<String> semesterQueue = new LinkedList<>();
    // Stack to hold courses for undo functionality
    static Stack<Course> undoStack = new Stack<>();

    //*************************************START OF PROGRAM*************************************
    // Main method to run the program
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        //Program message
        System.out.println("*************************************START OF PLANNING*************************************");

        //Welcome message
        System.out.println("Welcome to the college course planning program!");

       //School year user input
        int schoolYear = getValidatedInput("What is your current school year? (9, 10, 11, 12)",
        Arrays.asList(9, 10, 11, 12), Integer.class);
        
        //Semester user input
        String semester = getValidatedInput("What semester are you currently in? (Fall, Spring, Summer)", 
        Arrays.asList("Fall", "Spring", "Summer"), String.class);


        //Summer courses user input
        String summerResponse = getValidatedInput("Do you want to take summer courses? (yes or no)",
        Arrays.asList("yes", "no"), String.class);
        boolean wantsSummer = summerResponse.equalsIgnoreCase("yes");

        //Creating student
        studentINPUT student = new studentINPUT(schoolYear, semester, wantsSummer, new LinkedList<>());

        //Completed courses user input
        System.out.println("Have you completed any college courses?(ex. ENGL 01A) (yes/no): ");
        String completedCoursesResponse = "";
        // Loop until a valid response is given
        while (!(Arrays.asList("yes", "no").contains(completedCoursesResponse))) {
            completedCoursesResponse = scan.nextLine();
        } 
        // If the user has completed courses, prompt for course names
        if (completedCoursesResponse.equalsIgnoreCase("yes")) {
            while (true) {
                System.out.print("Enter course name (or type '-4' to finish): ");
                String completed = scan.nextLine();
                if (completed.equals("-4")) break;
                student.completedCourses.add(completed.toUpperCase());
            }
        } else { // If no completed courses, initialize with an empty list
            System.out.println("No completed courses recorded.");
        }
        initializeCourses();
        // Generate the plan based on the student input
        generatePLAN(student, scan);

        //NOTES ON REMINDERS AND LIMITS OF PROGRAM
        System.out.println("*************************************NOTES*************************************");
        System.out.println("Alright! Now having previous knowledge of your current standing this program will prompt you questions to help decide what courses you should take in the next semesters according to the high school equivalency credit. Doing this will help you get college credit equivalent to high school which boosts your GPA if you get a good grade in the class and could finish college and high school requirements. But as well keep in mind that if you get a low grade in the class it will lower your GPA. So do your best! ");


        System.out.println("REMINDER: This is only taking into account the high school equivalency credit classes unless absolutely necessary to take a course not equivalent. As well it does not take into account majors yet.");
        System.out.println("**If you want to complete a major I suggest you look directly through the college website for associates and see if there is any overlapping with general ed.");
        System.out.println("(MUST FINISH INTEG MATH 3 FOR AREA 2) - This is a requirement for the math area, so if you have not completed it, you will need to take it before proceeding with other math courses.");

        // Call the end method here
        end(args);
    }

    //*************************************GENERATE PLAN*************************************
    // This method generates a plan based on the student's input and the courses available in each IGETC area
    // It uses a queue to manage semesters and a stack for undo functionality
    static void generatePLAN(studentINPUT student, Scanner scan) {
        String[] semesters = {"Fall", "Spring", "Summer"};
        // Initialize the queue with semesters based on the student's current semester
        int startIndex = 0;

        // Determine starting semester based on current semester
        if (student.semester.equalsIgnoreCase("Spring")) {
            startIndex = 1; // Start from Spring
        } else if (student.semester.equalsIgnoreCase("Summer")) {
            startIndex = 2; // Start from Summer
        }

        // Add semesters to queue
        for (int i = startIndex; i < semesters.length + startIndex; i++) {
            semesterQueue.add(semesters[i % semesters.length]); // Will loop back around so all the semesters will be in queue
        }
        if (!student.wantsSummer) {
            semesterQueue.remove("Summer");
        }   

        //*************************************COURSE PLANNING*************************************
        System.out.println("*************************************COURSE PLANNING*************************************");
        plannedCourses.clear(); // Clear once before planning

        // IGETC AREA COURSE COMPLETED VALIDATION
        // This loop iterates through each IGETC area and prompts the user to select courses
        Queue<String> IGETC_AREAS_Queue = new LinkedList<>(Arrays.asList(IGETC_AREAS)); 
        // This loop will continue until all areas have been processed
        String lastArea = "";
        while (!IGETC_AREAS_Queue.isEmpty()) {
            String area = IGETC_AREAS_Queue.poll();
            // Check if the area is already completed
            Course[] options = geCourses.get(area);
            if (options == null) {
                System.out.println("No courses found for: " + area);
                continue;
            }

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

            //undoStack.clear(); // Clear the stack for each area selection
            System.out.println("You can select courses from this area. Type 'undo' to remove the last selection.");
            System.out.print("Choose course number(s) for this area (comma separated,ex. 1,3 or type 'undo'): ");
            while (true) { 
                try {
                    String input = scan.nextLine();
                    if (input.equalsIgnoreCase("undo") && !undoStack.isEmpty()) {
                        Course removed = undoStack.pop();
                        // We add the area back to the end so we check at the end if we need to ask the user again
                        // (if they no longer fufill the requirement)
                        IGETC_AREAS_Queue.add(lastArea); // Add to the back of the linked list
                        plannedCourses.remove(removed);
                        System.out.println("Last course selection undone: " + removed.name);
                        continue;
                    }
            
            // Split the input by commas and parse each selection
            String[] selections = input.split(",");
            for (String sel : selections) {
                int idx = Integer.parseInt(sel.trim()) - 1;
                if (idx >= 0 && idx < options.length) {
                    plannedCourses.add(options[idx]);
                    undoStack.push(options[idx]); // Push to stack for undo
                    lastArea = area; // For stack based undo
                } else {throw new NumberFormatException("");} // So it is redone 
                    }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("INVALID input:");
                }
            }
        }
        System.out.println("\nGeneral Education Plan:");
        // Calculate the number of semesters left in high school
        int sem_left = (12 - student.schoolYear); // how many years until graduation
        // Multiply by 2 or 3 depending on whether student wants summer semesters
        if (student.wantsSummer) {
            sem_left *= 3; // Fall, Spring, Summer
        } else {
            sem_left *= 2; // Fall and Spring only
        }
        // Adjust if the current semester is Spring (Spring is the second semester of the year)
        if (student.semester.equalsIgnoreCase("Spring")) {
            sem_left -= 1; // Already halfway through this school year
        } else if (student.wantsSummer && student.semester.equals("Summer")) {
            sem_left -= 2;
        }
        recursivePlanner(plannedCourses, semesterQueue, sem_left);
        // Print the planned courses

        //Ending message
        System.out.println("*************************************END OF PLANNING*************************************");
    }

    //*************************************RECURSIVE PLANNER*************************************
    // This method recursively plans courses for each semester until all courses are planned or semesters run out
    static void recursivePlanner(LinkedList<Course> courses, Queue<String> semesters, int sem_left) {
        if (courses.isEmpty() || sem_left <= 0) return;
       // Print the current semester according to grade and how many are left
        sem_left -= 1;
        String sem = semesters.poll();
        semesters.add(sem); // Add back to the end of the Queue 
        System.out.println("\nSemester: " + sem);

        int slots = Math.min(2, courses.size()); // Max classes per semester
        for (int i = 0; i < slots; i++) {
            Course course = courses.removeFirst();
            String marker = course.countsForHS ? "(*)" : "";
            System.out.println("- " + course.name + " " + marker);
        }
        // Recursively call for the next semester
        recursivePlanner(courses, semesters, sem_left);
    }

    //*************************************INITIALIZE COURSES*************************************
    // Initialize the courses for each IGETC area
    static void initializeCourses() {
        System.out.println("*************************************COURSE AREA CRITERIA IMPORTANT*************************************");
        // Area 1A: English Communication
        geCourses.put("Area 1A: English Communication", new Course[]{
            new Course("ENGL 01A", true)
        });

        // Area 1B: Critical Thinking
        geCourses.put("Area 1B: Critical Thinking", new Course[]{
            new Course("ENGL 13", true),
        });

        // Area 1C: Oral Communication (CSU only)
        geCourses.put("Area 1C: Oral Communication (CSU only)", new Course[]{
            new Course("COMM 1", false),
        });

        System.out.println("Choose 1 course from Area 2: MUST COMPLETE INTEG MATH 3 FOR AREA 2");
        // Area 2: Mathematical Concepts and Quantitative Reasoning
        geCourses.put("Area 2: Mathematical Concepts and Quantitative Reasoning", new Course[]{
            new Course("Math 02", true),
            new Course("Math 10", true)
        });

        System.out.println("Choose 3 courses total for Area 3: RECOMMEND TAKING 1 ART THEN BOTH HISTORY 04 A/B FOR FINISH Sophomore HIST.");
        // Area 3A: Arts
        geCourses.put("Area 3A: Arts", new Course[]{
            new Course("ART 15", true),
            new Course("ART 24", true),
            new Course("ARTD 40A", true),
            new Course("ARTD 40B", true),
            new Course("MUSG 10", true),
            new Course("MUSG 14", true),
            new Course("THTR 01", true)
        });

        // Area 3B: Humanities
        geCourses.put("Area 3B: Humanities", new Course[]{
            new Course("HIST 04A", true),
            new Course("HIST 04B", true)
        });

        System.out.println("Choose 2 courses total for Area 4: RECOMMEND TAKING 1 ART THEN BOTH HISTORY 17 A/B FOR Junior Sophomore HIST.");
        // Area 4: Social and Behavioral Sciences
        geCourses.put("Area 4: Social and Behavioral Sciences", new Course[]{
            new Course("HIST 17A", true),
            new Course("HIST 17B", true),
            new Course("CLDV 01", true),
            new Course("CLDV 02", true),
            new Course("ECON 01", true),
            new Course("ECON 02", true),
            new Course("POSC 01", true),
            new Course("SOC 01", true)
        });

        System.out.println("Choose 2 courses total for Area 5: 1 in 5A and another in 5B: THESE CAN FILL IN FOR INTRODUCTORY Sophomore SCI.");
        // Area 5A: Physical Sciences
        geCourses.put("Area 5A: Physical Sciences", new Course[]{
            new Course("CHEM 02A", true),
            new Course("PHYS 02A", true)
        });

        // Area 5B: Biological Sciences
        geCourses.put("Area 5B: Biological Sciences", new Course[]{
            new Course("BIOL 01", true),
            new Course("BIOL 06", true)
        });

        System.out.println("Choose 1 course for Area 6: RECOMMEND TAKING A LANGUAGE YOUR FAMILIAR WITH THIS IS EQUIVALENT to 2 YEARS OF HIGH SCHOOL LANGUAGE.");
        // Area 6: Language Other than English (UC only)
        geCourses.put("Area 6: Language Other than English (UC only)", new Course[]{
            new Course("SPAN 01", true),
            new Course("HMNG 01", true),
            new Course("FREN 01", true)
        });

        // Area 7: Ethnic Studies
        geCourses.put("Area 7: Ethnic Studies", new Course[]{
            new Course("ETHN 01", false)
        });
    } // End of initializeCourses

    // ENDING STATEMENT
    // This method is called at the end of the program to indicate completion
    public static void end(String[] args) {
        System.out.println("**************************************END OF PROGRAM*************************************");
    }
}
