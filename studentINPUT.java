import java.util.LinkedList;

// This class holds the student input data
    public class studentINPUT {
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