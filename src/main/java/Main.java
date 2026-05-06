import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner userInput = new Scanner(System.in);
        TaskManager tasks = new TaskManager();
        printMenu();
        while(true){
            String command = userInput.nextLine().trim();
            switch(command){
                case "a":
                    tasks.addTask(userInput);
                    break;
                case "d":
                    tasks.deleteTask(userInput);
                    break;
                case "e":
                    tasks.editTask(userInput);
                    break;
                case "s":
                    // Call a method to save tasks
                    break;
                case "l":
                    // Call a method to load tasks
                    break;
                case "p":
                    tasks.printAllTasks();
                    break;
                case "pp":
                    tasks.printTasksByPriority();
                    break;
                case "pd":
                    // Call a method to print tasks
                    break;
                case "q":
                    System.out.println("Exiting Task Manager...");
                    return;
                default:
                    System.out.println("Invalid command option.");
            }
            printMenu();
        }
    }

    public static void printMenu(){
        System.out.println(
                """
                   ------ Task Manager MAIN MENU -------
                   1) To ADD new task, press "a" and enter key
                   2) To DELETE existing task, press "d" and enter key
                   3) To EDIT existing tasks, press "e" and enter key
                   4) To SAVE existing tasks, press "s" and enter key
                   5) To LOAD a list of tasks, press "l" and enter key
                   6) To PRINT all tasks, press "p" and enter key
                   7) To PRINT tasks according to PRIORITY, press "pp" and enter key
                   8) To PRINT tasks according to DEADLINE, press "pd" and enter key
                   9) To END this program, press "q" and enter key
                """
        );
    }
}

