import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner userInput = new Scanner(System.in);
        TaskManager tasks = new TaskManager();
        IOManager iomanager = new IOManager();

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
                case "stxt":
                    iomanager.saveTasksToTXT(userInput, tasks.tasks);
                    break;
                case "ltxt":
                    // Call a method to load tasks as text file
                    break;
                case"sjson":
                    // Call a method to save tasks as JSON file
                    break;
                case "ljson":
                    // Call method to load tasks as JSON file
                    break;
                case "p":
                    tasks.printAllTasks();
                    break;
                case "pp":
                    tasks.printTasksSortedByPriority();
                    break;
                case "pd":
                    tasks.printTasksSortedByDeadline();
                    break;
                case "pt":
                    tasks.printTasksOfType(userInput);
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
                   1) To ADD new task, enter 'a' and press Enter
                   2) To DELETE existing task, enter 'd' and press Enter
                   3) To EDIT existing tasks, enter 'e' and press Enter
                   4) To SAVE existing tasks as text file, enter 'stxt' and press Enter
                   5) To LOAD a list of tasks from text file, enter 'ltxt' and press Enter
                   6) To SAVE existing tasks as JSON file, enter 'sjson' and press Enter
                   7) To LOAD a list of tasks from JSON file, enter 'ljson' and press Enter
                   8) To PRINT all tasks, enter 'p' and press Enter
                   9) To SORT and PRINT tasks according to PRIORITY, enter 'pp' and press Enter
                   10) To SORT and PRINT tasks according to DEADLINE, enter 'pd' and press Enter
                   11) To PRINT tasks with defined TYPE, enter 'pt' and press Enter
                   12) To END this program, enter 'q' and press Enter
                """
        );
    }
}

