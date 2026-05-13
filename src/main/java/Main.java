import java.util.List;
import java.util.Scanner;

public class Main{
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
                    List<Task> loadedTasksTXT = iomanager.loadTasksFromTXT(userInput);
                    if(loadedTasksTXT != null){
                        tasks = new TaskManager(loadedTasksTXT);
                    }
                    break;
                case"sjson":
                    iomanager.saveTasksToJSON(userInput, tasks.tasks);
                    break;
                case "ljson":
                    List<Task> loadedTasksJSON = iomanager.loadTasksFromJSON(userInput);
                    if(loadedTasksJSON != null){
                        tasks = new TaskManager(loadedTasksJSON);
                    }
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
                case "pk":
                    tasks.searchTasksWithKeyword(userInput);
                    break;
                case "stat":
                    tasks.printTasksStatistics();
                    break;
                case "q":
                    System.out.println("\033[1mExiting Task Manager...\033[0m");
                    return;
                default:
                    System.out.println("\033[1;31mInvalid command option.\033[0m");
            }
            printMenu();
        }
    }

    public static void printMenu(){
        System.out.println(
                """
                   \033[1;33m------ Task Manager MAIN MENU -------\033[0m
                   1) To \033[1mADD\033[0m new task, enter \033[1m'a'\033[0m and press Enter
                   2) To \033[1mDELETE\033[0m existing task, enter \033[1m'd'\033[0m and press Enter
                   3) To \033[1mEDIT\033[0m existing tasks, enter \033[1m'e'\033[0m and press Enter
                   4) To \033[1mSAVE\033[0m existing tasks as text file, enter \033[1m'stxt'\033[0m and press Enter
                   5) To \033[1mLOAD\033[0m a list of tasks from text file, enter \033[1m'ltxt'\033[0m and press Enter
                   6) To \033[1mSAVE\033[0m existing tasks as JSON file, enter \033[1m'sjson'\033[0m and press Enter
                   7) To \033[1mLOAD\033[0m a list of tasks from JSON file, enter \033[1m'ljson'\033[0m and press Enter
                   8) To \033[1mPRINT\033[0m all tasks, enter \033[1m'p'\033[0m and press Enter
                   9) To \033[1mSORT\033[0m and \033[1mPRINT\033[0m tasks according to \033[1mPRIORITY\033[0m, enter \033[1m'pp'\033[0m and press Enter
                   10) To \033[1mSORT\033[0m and \033[1mPRINT\033[0m tasks according to \033[1mDEADLINE\033[0m, enter \033[1m'pd'\033[0m and press Enter
                   11) To \033[1mPRINT\033[0m tasks with defined \033[1mTYPE\033[0m, enter \033[1m'pt'\033[0m and press Enter
                   12) To \033[1mSEARCH\033[0m tasks with defined \033[1mKEYWORD\033[0m, enter \033[1m'pk'\033[0m and press Enter
                   13) To \033[1mPRINT\033[0m tasks \033[1mSTATISTICS\033[0m, enter \033[1m'stat'\033[0m and press Enter
                   14) To \033[1mEND\033[0m this program, enter \033[1m'q'\033[0m and press Enter
                """
        );
    }
}

