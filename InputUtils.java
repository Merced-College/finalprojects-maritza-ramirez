import java.util.List;
import java.util.Scanner;

public class InputUtils {
    public static <T> T getValidatedInput(String prompt, List<T> validOptions, Class<T> type, Scanner scan) {
        System.out.println(prompt);
        T input = null;
        while (true) {
            try {
                String userInput = scan.nextLine().trim();
                if (type == Integer.class) {
                    input = type.cast(Integer.parseInt(userInput));
                } else if (type == String.class) {
                    input = type.cast(userInput);
                }
                if (validOptions.contains(input)) {
                    return input;
                } else {
                    System.out.println("Invalid option. Try again:");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a " + type.getSimpleName() + ".");
            }
        }
    }
}