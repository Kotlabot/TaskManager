package taskmanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Manages task operations in the console task manager application.
 *
 * Specifically manages:
 * - creating tasks
 * - deleting tasks
 * - editing tasks
 * - printing tasks
 * - sorting tasks by priority and deadline
 * - filtering tasks by type
 * - searching tasks by keyword
 * - printing tasks statistics
 */
public class TaskManager{
    /**
     * List storing all created tasks.
     */
    private List<Task> tasks;

    /**
     * Counter used for automatic task name generation.
     */
    private int taskCounter = 1;

    /**
     * Creates an empty task manager list.
     *
     * Called when the application starts to prepare empty list for tasks.
     */
    public TaskManager(){
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task manager with an existing list of tasks.
     *
     * Called when loading existing list of tasks from file.
     * @param tasks list of tasks to initialize the manager with
     */
    public TaskManager(List<Task> tasks){
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns list of all stored tasks.
     *
     * @return list of tasks
     */
    public List<Task> getTasks(){
        return tasks;
    }

    /**
     * Print all currently stored tasks.
     */
    public void printAllTasks(){
        if(tasks.isEmpty()){
            System.out.println("\033[1;31mNo tasks available.\n\033[0m");
            return;
        }

        System.out.println("------ TASK LIST ------");

        for(Task task : tasks){
            System.out.println(task.printTaskWithDeadline("") + "----------------------");
        }
    }

    /**
     * Creates new task according to user input and adds this task to task manager list.
     *
     * @param input scanner used for console input
     */
    public void addTask(Scanner input){
        // Call separate methods to set all task parameters.
        String name = setName(input);
        String description = setDescription(input);
        LocalDate deadline = setDeadline(input);
        int priority = setPriority(input);
        String type = setType(input);

        System.out.println("Setting task parameters...");
        Task newTask = new Task(name, description, deadline, priority, type, false);
        tasks.add(newTask);
        System.out.println("\033[1;32mTask successfully created!\n\033[0m");
        System.out.println("\033[1mCreated task:\n\033[0m");
        System.out.println(newTask.printTaskWithDeadline(""));
        System.out.println();
    }

    /**
     * Sets task name.
     *
     * Sets name of task by iterating through while loop until user inputs
     * valid name (not duplicate) or user lets the method generate automatic name.
     *
     * @param input scanner used for console input
     * @return valid task name
     */
    private String setName(Scanner input){
        System.out.println("Write name of new task and press Enter:");
        String taskName;
        boolean duplicate;

        while(true){
            String name = input.nextLine();
            duplicate = false;

            // Generate automatic name according to task counter
            if(name.isEmpty()){
                taskName = "Task" + taskCounter++;
                System.out.println("\033[1;31mNo name specified, defaulting to: '" + taskName + "'\033[0m");
                break;
            }
            // Check for duplicate name
            else{
                for(Task task : tasks){
                    if(task.getName().equals(name)){
                        System.out.println("\033[1;31mThis name already exists.\033[0m Please enter different name and " +
                                "press Enter or just press Enter to generate automatic name: ");
                        duplicate = true;
                    }
                }
                // If no duplicates are found, set the name and break from the loop
                if(!duplicate){
                    taskName = name;
                    break;
                }
            }
        }
        return taskName;
    }

    /**
     * Sets task description.
     *
     * Either sets the description provided by user, or sets the description as null.
     *
     * @param input scanner used for console input
     * @return task description or null
     */
    private String setDescription(Scanner input){
        System.out.println("If you would like to add a description of this task, write it down and press Enter, " +
                "otherwise just press Enter:");
        String taskDescription = input.nextLine();
        if(taskDescription.isEmpty()){
            taskDescription = null;
        }
        return taskDescription;
    }

    /**
     * Sets task deadline.
     *
     * Either sets the deadline provided by user while checking correct
     * format of the date, or sets the deadline as null.
     *
     * @param input scanner used for console input
     * @return valid task deadline or null
     */
    private LocalDate setDeadline(Scanner input){
        System.out.println("If you would like to add a deadline to this task, enter deadline in format 'DD.MM.YYYY'" +
                " and press Enter, otherwise just press Enter:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate taskDeadline = null;

        while(true){
            String inputDate = input.nextLine().trim();

            // If no deadline is specified return null
            if(inputDate.isEmpty()){
                break;
            }

            // Otherwise try to parse the deadline until its in valid format
            try{
                taskDeadline = LocalDate.parse(inputDate, formatter);
                break;
            }
            catch(DateTimeParseException e){
                System.out.println("\033[1;31mInvalid format.\033[0m Please use DD.MM.YYYY or press Enter to skip:");
            }
        }
        return taskDeadline;
    }

    /**
     * Sets task priority.
     *
     * Either sets priority provided by user (where lower number means higher priority),
     * or sets priority as 0, which indicate no priority.
     * @param input scanner used for console input
     * @return task priority
     */
    private int setPriority(Scanner input){
        System.out.println("If you would like to add this task a priority, enter a number (lower number = higher priority) " +
                "and press Enter, otherwise just press Enter (no priority defaults to 0):");
        int taskPriority = 0;
        while(true){
            String inputPriority = input.nextLine().trim();

            if(inputPriority.isEmpty()){
                break;
            }

            try{
                taskPriority = Integer.parseInt(inputPriority);
                break;
            }
            catch(NumberFormatException e){
                System.out.println("\033[1;31mInvalid format.\033[0m Please select a valid integer or press Enter to skip:");
            }
        }
        return taskPriority;
    }

    /**
     * Sets task type.
     *
     * Either sets the type provided by user, or sets the type as null.
     *
     * @param input scanner used for console input
     * @return task type
     */
    private String setType(Scanner input){
        System.out.println("If you would like to add this task's type (work/personal/school subject), enter a type " +
                "and press Enter, otherwise, just press Enter:");
        String taskType = input.nextLine();
        if(taskType.isEmpty()){
            taskType = null;
        }
        return taskType;
    }

    /**
     * Deletes a task with the specified name.
     *
     * @param input scanner used for console input
     */
    public void deleteTask(Scanner input){
        System.out.println("Enter name of task you would like to delete and press Enter:");
        String taskName = input.nextLine();
        for(Task task : tasks){
            if(Objects.equals(task.getName(), taskName)){
                tasks.remove(task);
                System.out.println("\033[1;32mTask " + taskName + " successfully deleted!\n\033[0m");
                return;
            }
        }
        System.out.println("\033[1;31mNo task with such name was found!\n\033[0m");
    }

    /**
     * Opens edit menu for a selected task.
     *
     * Checks for task name defined by user and if the name is valid (task of this name exists), enables editing of this task.
     * Handles editing operations through separate methods.
     *
     * @param input scanner used for console input
     */
    public void editTask(Scanner input){
        System.out.println("Enter name of task you would like to edit and press Enter:");
        String taskName = input.nextLine();
        Task editedTask = null;

        // Find task to be edited by its name
        for(Task task : tasks){
            if(Objects.equals(task.getName(), taskName)){
                editedTask = task;
            }
        }

        if(editedTask == null){
            System.out.println("\033[1;31mNo task with such name was found!\n\033[0m");
            return;
        }

        printEditMenu();

        // Editing mode loop
        while(true){
            String command = input.nextLine().trim();

            // Handles user commands
            switch(command){
                case "n":
                    editName(input, editedTask);
                    break;
                case "ds":
                    editDescription(input, editedTask);
                    break;
                case "dl":
                    editDeadline(input, editedTask);
                    break;
                case "p":
                    editPriority(input, editedTask);
                    break;
                case "t":
                    editType(input, editedTask);
                    break;
                case "c":
                    boolean deleted = editCompleteness(input, editedTask);

                    // In case user delete the editing task, exit edit mode
                    if(deleted){
                        System.out.println("\033[1mEdited task was deleted, exiting edit mode...\n\033[0m");
                        return;
                    }
                    break;
                case "q":
                    System.out.println("\033[1mExiting edit mode...\n\033[0m");
                    return;
                default:
                    System.out.println("\033[1;31mInvalid command option.\n\033[0m");
            }

            // Display edit menu again after command execution
            printEditMenu();
        }
    }

    /**
     * Prints the edit menu with all available commands.
     */
    private void printEditMenu(){
        System.out.println(
                """
                   \033[1;33m------ Task Manager EDIT MENU  -------\033[0m
                   1) To edit task's \033[1mNAME\033[0m, enter \033[1m'n'\033[0m and press Enter
                   2) To edit task's \033[1mDESCRIPTION\033[0m, enter \033[1m'ds'\033[0m and press Enter
                   3) To edit task's \033[1mDEADLINE\033[0m, enter \033[1m'dl'\033[0m and press Enter
                   4) To edit task's \033[1mPRIORITY\033[0m, enter \033[1m'p'\033[0m and press Enter
                   5) To edit task's \033[1mTYPE\033[0m, enter \033[1m't'\033[0m and press Enter
                   6) To edit task's \033[1mCOMPLETENESS\033[0m, enter \033[1m'c'\033[0m and press Enter
                   7) To \033[1mEXIT\033[0m edit mode, enter \033[1m'q'\033[0m and press Enter
                """
        );
    }

    /**
     * Edits existing task name.
     *
     * Edits name of task by iterating through while loop until user inputs
     * valid new name (not duplicate) or decides to keep the current name unchanged.
     *
     * @param input scanner used for console input
     * @param task edited task
     */
    private void editName(Scanner input, Task task){
        String currentName = task.getName();
        System.out.println("Current name of task: '" + currentName + "'");
        System.out.println("Enter new name and press Enter:");
        boolean duplicate;

        while(true){
            String inputName = input.nextLine();
            duplicate = false;

            if(inputName.isEmpty()){
                System.out.println("\033[1;31mNo new name entered.\033[0m Please enter new name and press Enter " +
                        "or enter \033[1m'q'\033[0m and press Enter to keep the current name:");
            }
            else if(inputName.equals("q")){
                System.out.println("Keeping current name: '" + currentName + "'\n");
                break;
            }
            else{
                // Check for duplicate names
                for(Task currentTask : tasks){
                    if(currentTask.getName().equals(inputName)){
                        System.out.println("\033[1;31mThis name already exists.\033[0m Please enter different name and " +
                                "press Enter or enter \033[1m'q'\033[0m and press Enter to keep the current name:");
                        duplicate = true;
                    }
                }
                // If no duplicates are found, edit the name and break from the loop
                if(!duplicate){
                    task.setName(inputName);
                    System.out.println("\033[1;32mName successfully changed from: '" + currentName +
                            "' to: '" + inputName + "'\n\033[0m");
                    break;
                }
            }
        }
    }

    /**
     * Edits task description.
     *
     * Edits description of task by iterating through while loop until user inputs
     * new description or decides to keep the current description unchanged.
     *
     * @param input scanner used for console input
     * @param task edited task
     */
    private void editDescription(Scanner input, Task task){
        String currentDescription = task.getDescription();
        System.out.println("Current description of task: '" + currentDescription + "'");
        System.out.println("Enter new description and press Enter. In case you want to delete current description, " +
                "enter \033[1m'd'\033[0m and press Enter:");

        while(true){
            String inputDescription = input.nextLine();
            if(inputDescription.isEmpty()){
                System.out.println("\033[1;31mNo new description entered.\033[0m Add new description and press Enter or keep old one by entering " +
                        "\033[1m'q'\033[0m and pressing Enter or delete current description by entering \033[1m'd'\033[0m and pressing Enter:");
            }
            else if(inputDescription.equals("d")){
                task.setDescription(null);
                System.out.println("\033[1;32mDescription successfully deleted!\n\033[0m");
                break;
            }
            else if(inputDescription.equals("q")){
                System.out.println("Keeping current description: '" + currentDescription + "'\n");
                break;
            }
            else{
                task.setDescription(inputDescription);
                System.out.println("\033[1;32mDescription successfully changed from: '" + currentDescription + "' to: '"
                        + inputDescription + "'\n\033[0m");
                break;
            }
        }
    }

    /**
     * Edits task deadline.
     *
     * Edits deadline of task by iterating through while loop until user inputs
     * valid deadline or decides to delete the deadline, or keep the current deadline unchanged.
     *
     * @param input scanner used for console input
     * @param task edited task
     */
    private void editDeadline(Scanner input, Task task){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate currentDate = task.getDeadline();
        String currentDeadline;

        // Format current deadline to display it to user
        if(currentDate == null){
            currentDeadline = "none";
        }
        else{
            currentDeadline = currentDate.format(formatter);
        }

        System.out.println("Current deadline of task: '" + currentDeadline + "'");
        System.out.println("Enter new deadline in format DD.MM.YYYY and press Enter. In case you want to delete current " +
                "deadline, enter \033[1m'd'\033[0m and press Enter:");

        while(true){
            String inputDate = input.nextLine().trim();
            if(inputDate.isEmpty()){
                System.out.println("\033[1;31mNo new deadline entered.\033[0m Add new deadline and press Enter or keep old one by entering " +
                        "\033[1m'q'\033[0m and pressing Enter or delete current deadline by entering \033[1m'd'\033[0m and pressing Enter:");
            }
            else if(inputDate.equals("q")){
                System.out.println("Keeping current deadline: '" + currentDeadline + "'\n");
                break;
            }
            else if(inputDate.equals("d")){
                task.setDeadline(null);
                System.out.println("\033[1;32mDeadline successfully deleted!\n\033[0m");
                break;
            }
            // If user specifies some deadline, try to parse it until its in valid format
            else{
                try{
                    currentDate = LocalDate.parse(inputDate, formatter);
                    task.setDeadline(currentDate);
                    System.out.println("\033[1;32mDeadline successfully changed from: '" + currentDeadline + "' to: '"
                            + inputDate + "'\n\033[0m");
                    break;
                }
                catch(DateTimeParseException e){
                    System.out.println("\033[1;31mInvalid format.\033[0m Please use DD.MM.YYYY, or enter \033[1m'q'\033[0m " +
                            "and press Enter to keep current deadline, or enter \033[1m'd'\033[0m and press Enter to delete deadline:");
                }
            }
        }
    }

    /**
     * Edits task priority.
     *
     * Edits priority of task by iterating through while loop until user inputs
     * valid priority (integer value) or decides to keep the current priority unchanged.
     *
     * @param input scanner used for console input
     * @param task edited task
     */
    private void editPriority(Scanner input, Task task){
        int currentPriority = task.getPriority();
        System.out.println("Current priority of task: '" + currentPriority + "'");
        System.out.println("Enter new priority number (lower number = higher priority) and press Enter. In case you " +
                "want to delete current priority, enter \033[1m'd'\033[0m and press Enter:");

        while(true){
            String inputPriority = input.nextLine().trim();
            if(inputPriority.isEmpty()){
                System.out.println("\033[1;31mNo new priority entered.\033[0m Add new priority and press Enter or keep old one by entering " +
                        "\033[1m'q'\033[0m and pressing Enter or delete current priority by entering \033[1m'd'\033[0m and pressing Enter:");
            }
            else if(inputPriority.equals("q")){
                System.out.println("Keeping current priority: '" + currentPriority + "'\n");
                break;
            }
            else if(inputPriority.equals("d")){
                task.setPriority(0);
                System.out.println("\033[1;32mPriority successfully deleted!\n\033[0m");
                break;
            }
            else{
                try{
                    int newPriority = Integer.parseInt(inputPriority);
                    task.setPriority(newPriority);
                    System.out.println("\033[1;32mPriority successfully changed from: '" + currentPriority + "' to: '" + newPriority + "'\n\033[0m");
                    break;
                }
                catch(NumberFormatException e){
                    System.out.println("\033[1;31mInvalid format.\033[0m Please select a valid integer, or enter \033[1m'q'\033[0m " +
                            "and press Enter to keep current priority, or enter \033[1m'd'\033[0m and press Enter to delete priority:");
                }
            }
        }
    }

    /**
     * Edits task type.
     *
     * Edits type of task by iterating through while loop until user inputs
     * new type or decides to delete the type, or keep the current type unchanged.
     *
     * @param input scanner used for user input
     * @param task edited task
     */
    private void editType(Scanner input, Task task){
        String currentType = task.getType();
        System.out.println("Current type of task: '" + currentType + "'");
        System.out.println("Enter new type and press Enter. In case you want to delete current type, enter " +
                "\033[1m'd'\033[0m and press Enter:");

        while(true){
            String newType = input.nextLine();
            if(newType.isEmpty()){
                System.out.println("\033[1;31mNo type specified.\033[0m Write new type and press Enter, or enter \033[1m'q'\033[0m " +
                        "and press Enter to keep the current type, or enter \033[1m'd'\033[0m and press Enter to delete current type:");
            }
            else if(newType.equals("d")){
                task.setType(null);
                System.out.println("\033[1;32mType successfully deleted.\n\033[0m");
                break;
            }
            else if(newType.equals("q")){
                System.out.println("Keeping the current type: '" + currentType + "'\n");
                break;
            }
            else{
                task.setType(newType);
                System.out.println("\033[1;32mType successfully changed from: '" + currentType + "' to: '" + newType + "'\n\033[0m");
                break;
            }
        }
    }

    /**
     * Edits task completion state.
     *
     * As there are two option of completion state (completed/incompleted), method offers to switch
     * to the second state. If the task is changed to completed, method also offers deletion of the completed task.
     *
     * @param input scanner used for console input
     * @param task edited task
     * @return information about task deletion
     */
    private boolean editCompleteness(Scanner input, Task task){
        boolean completed = task.getCompleted();
        String taskName = task.getName();

        // If current task is completed, offer to mark it as incompleted or remain unchanged
        if(completed){
            System.out.println("Current task is marked as completed. Press Enter to mark task as incompleted, or enter" +
                    " \033[1;32m'q'\033[0m and press Enter to remain unchanged.");

            while(true){
                String newInput = input.nextLine().trim();
                if(newInput.isEmpty()){
                    task.setCompleted(false);
                    System.out.println("\033[1;32mSuccessfully marked current task as incompleted.\n\033[0m");
                    break;
                }
                else if(newInput.equals("q")){
                    System.out.println("Keeping the current task completed.\n");
                    break;
                }
                else{
                    System.out.println("\033[1;31mInvalid option!\033[0m Press Enter to mark task as incompleted, or " +
                            "enter \033[1m'q'\033[0m and press Enter to remain unchanged.");
                }
            }
            return false;
        }
        // If current task is incompleted, offer to mark it as completed or remain unchanged
        else{
            System.out.println("Current task is marked as incompleted. Press Enter to mark task as completed, or enter " +
                    "\033[1m'q'\033[0m and press Enter to remain unchanged.");

            while(true){
                String newInput = input.nextLine().trim();
                if(newInput.isEmpty()){
                    task.setCompleted(true);
                    System.out.println("\033[1;32mSuccessfully marked current task as completed.\n\033[0m");
                    System.out.println("Task '" + taskName + "' is now completed. Would you like to delete this task?" +
                            " Type 'yes' or 'no' and press Enter:");

                    String answerDelete = input.nextLine().trim();
                    // Delete completed task and return true to avoid further editing of deleted task
                    if(answerDelete.equals("yes")){
                        tasks.remove(task);
                        System.out.println("\033[1;32mTask successfully deleted!\n\033[0m");
                        return true;
                    }
                    else if(answerDelete.equals("no")){
                        System.out.println("\033[1;32mKeeping this task!\n\033[0m");
                    }
                    // Safe option - if no clear answer is provided, keep current task.
                    else{
                        System.out.println("\033[1;31mInvalid option.\033[0m Keeping this task!\n");
                    }
                    break;
                }
                if(newInput.equals("q")){
                    System.out.println("Keeping the current task incompleted.\n");
                    break;
                }
                else{
                    System.out.println("\033[1;31mInvalid option!\033[0m Press Enter to mark task as completed, or enter " +
                            "\033[1m'q'\033[0m and press Enter to remain unchanged.");
                }
            }
            return false;
        }
    }

    /**
     * Prints list of tasks sorted by priority.
     */
    public void printTasksSortedByPriority(){
        if(tasks.isEmpty()){
            System.out.println("\033[1;31mNo tasks available.\n\033[0m");
            return;
        }

        // Priority "0" represent no priority, therefore in the sorting logic
        // zero values are taken as the highest values possible
        tasks.sort(Comparator.comparingInt(task -> task.getPriority() == 0 ? Integer.MAX_VALUE : task.getPriority()));

        System.out.println("------ TASKS SORTED BY PRIORITY ------");

        for(Task task : tasks){
            System.out.println(task.printTaskWithDeadline("") + "--------------------------------------");
        }
    }

    /**
     * Prints list of tasks sorted by deadline.
     */
    public void printTasksSortedByDeadline(){
        if(tasks.isEmpty()){
            System.out.println("\033[1;31mNo tasks available.\n\033[0m");
            return;
        }

        // Tasks with no deadline are placed at the end
        tasks.sort(Comparator.comparing(Task::getDeadline, Comparator.nullsLast(LocalDate::compareTo)));

        System.out.println("------ TASKS SORTED BY DEADLINE ------");

        for(Task task : tasks){
            System.out.println(task.printTaskWithDeadline("") + "--------------------------------------");
        }
    }

    /**
     * Print task with defined type.
     *
     * Prints tasks of defined type by iterating through while loop until user enters valid
     * existing type or decides to go back to main menu. If existing type is provided, method
     * checks all tasks type and print those that match.
     *
     * @param input scanner used for console input
     */
    public void printTasksOfType(Scanner input){
        System.out.println("Enter task type to print all tasks of this type:");
        while(true){
            String inputType = input.nextLine();

            if(inputType.isEmpty()){
                System.out.println("No type specified, please enter type or enter \033[1m'q'\033[0m and press Enter to go back to main menu:");
            }
            else if(inputType.equals("q")){
                break;
            }
            else{
                // Add all matching tasks to new list
                List<Task> oneTypeTasks = new ArrayList<>();
                for(Task task : tasks){
                    if(task.getType().equals(inputType)){
                        oneTypeTasks.add(task);
                    }
                }

                if(oneTypeTasks.isEmpty()){
                    System.out.println("Defined type does not exist, please enter valid type, or enter \033[1m'q'\033[0m and press Enter to go back to main menu:");
                    continue;
                }
                // If at least one task matched the type, print tasks with provided type
                else{
                    System.out.println("------ TASKS OF TYPE '" + inputType + "' ------");

                    for(Task task : oneTypeTasks){
                        System.out.println(task.printTaskWithDeadline("") + "----------------------");
                    }
                    break;
                }
            }
        }
    }

    /**
     * Prints tasks statistics.
     *
     * Printed statistics include:
     * - total number of tasks
     * - number of completed tasks
     * - number of incompleted tasks
     * - number of upcoming tasks (deadline is ahead)
     * - number of overdue task
     * - average priority of tasks
     * - number of tasks of every type existing
     */
    public void printTasksStatistics(){
        if(tasks.isEmpty()){
            System.out.println("\033[1;31mNo tasks available.\n\033[0m");
            return;
        }

        System.out.println("Task statistics:");
        int numberOfTasks = tasks.size();
        System.out.println("- Total tasks: " + numberOfTasks);
        int completedTasks = 0;
        int incompletedTasks = 0;
        int tasksWithUpcomingDeadline = 0;
        int tasksWithOverdueDeadline = 0;
        double sumOfPriorities = 0;
        // Map for different types across tasks
        Map<String, Integer> types = new HashMap<>();
        // Current date for deadline comparison
        LocalDate today = LocalDate.now();

        for(Task task : tasks){
            if(task.getCompleted()){
                completedTasks++;
            }
            else{
                incompletedTasks++;
            }

            LocalDate deadline = task.getDeadline();
            if(deadline != null){
                // Calculate difference between current date and deadline
                long daysDifference = ChronoUnit.DAYS.between(today, deadline);
                if(daysDifference >= 0){
                    tasksWithUpcomingDeadline++;
                }
                else{
                    tasksWithOverdueDeadline++;
                }
            }

            sumOfPriorities += task.getPriority();

            String taskType = task.getType();
            if(taskType.equals("none")){
                taskType = "undefined";
            }
            // Count occurrences of each task type
            types.put(taskType, types.getOrDefault(taskType, 0) + 1);
        }

        System.out.println("- Completed tasks: " + completedTasks);
        System.out.println("- Incompleted tasks: " + incompletedTasks);
        System.out.println("- Upcoming tasks: " + tasksWithUpcomingDeadline);
        System.out.println("- Overdue tasks: " + tasksWithOverdueDeadline);
        double averagePriority = sumOfPriorities/numberOfTasks;
        System.out.printf("- Average priority: %.2f%n", averagePriority);

        // Print task types sorted by number of occurrences
        types.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .forEach(entry -> System.out.println("- Tasks of type '" + entry.getKey() + "': " + entry.getValue()));

        System.out.println();
    }

    /**
     * Searches and prints tasks containing a specified keyword.
     *
     * The keyword is searched in task name, description and type.
     * Matching keywords are highlighted during printing.
     *
     * @param input scanner used for console input
     */
    public void searchTasksWithKeyword(Scanner input){
        System.out.println("Enter keyword you want to search for tasks by and press Enter:");
        while(true){
            String keyword = input.nextLine();

            if(keyword.isEmpty()){
                System.out.println("\033[1;31mNo keyword specified.\033[0m Please enter type or enter \033[1m'q'\033[0m and press Enter to go back to main menu:");
            }
            else if(keyword.equals("q")){
                break;
            }
            else{
                // Store tasks containing the keyword in new list
                List<Task> keywordTasks = new ArrayList<>();
                for(Task task : tasks){
                    String name = task.getName();
                    String description = task.getDescription();
                    String type = task.getType();

                    // Search keyword in name, description and type
                    if(containsKeyword(name, keyword) || containsKeyword(description, keyword) || containsKeyword(type, keyword)){
                        keywordTasks.add(task);
                    }
                }

                if(keywordTasks.isEmpty()){
                    System.out.println("\033[1;31mDefined keyword does not exist.\033[0m Please enter valid keyword, or enter \033[1m'q'\033[0m and press Enter to go back to main menu:");
                    continue;
                }
                else{
                    System.out.println("------ TASKS CONTAINING KEYWORD '" + keyword + "' ------");

                    for(Task task : keywordTasks){
                        System.out.println(task.printTaskWithDeadline(keyword) + "----------------------");
                    }
                    break;
                }
            }
        }
    }

    /**
     * Checks whether the specified text (task name, description or type) contains the specified keyword.
     * The search is case-insensitive.
     *
     * @param taskParameter text to search keyword in
     * @param keyword keyword to search for
     * @return true if keyword is found, false if not
     */
    private boolean containsKeyword(String taskParameter, String keyword){
        // Split text into individual words
        String[] allWords = taskParameter.split(" ");
        for(String word : allWords){
            // Compare each word ignoring upper/lower case
            if(word.toLowerCase().contains(keyword.toLowerCase())){
                return true;
            }
        }
        return false;
    }
}
