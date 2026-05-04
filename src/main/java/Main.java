import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner userInput = new Scanner(System.in);
        printMenu();
        while(true){
            String command = userInput.nextLine();
            switch(command){
                case "a":
                    // Call a method to add new task
                    break;
                case "d":
                    // Call a method to delete task
                    break;
                case "e":
                    // Call a method to edit task
                    break;
                case "s":
                    // Call a method to save tasks
                    break;
                case "l":
                    // Call a method to load tasks
                    break;
                case "pp":
                    // Call a method to print tasks
                    break;
                case "pd":
                    // Call a method to print tasks
                    break;
                default:
                    System.out.println("Invalid command option.");
            }
            printMenu();
        }
    }

    public static void printMenu(){
        System.out.println(
                """
                   Task Manager
                   1) To add new task, press "a" and enter key
                   2) To delete existing task, press "d" and enter key
                   3) To edit existing tasks, press "e" and enter key
                   4) To save existing tasks, press "s" and enter key
                   5) To load a list of tasks, press "l" and enter key
                   6) To print tasks according to priority, press "pp" and enter key
                   7) To print tasks according to deadline, press "pd" and enter key
                   8) To end this program, press escape key
                """
        );
    }
}

