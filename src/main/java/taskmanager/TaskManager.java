import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TaskManager{
    public List<Task> tasks;
    private int taskCounter = 1;

    // Create empty list of tasks
    public TaskManager(){
        tasks = new ArrayList<>();
    }

    // Create existing list of tasks (when loading from file)
    public TaskManager(List<Task> tasks){
        this.tasks = new ArrayList<>(tasks);
    }

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

    public void addTask(Scanner input){
        String name = setName(input);
        String description = setDescription(input);
        LocalDate deadline = setDeadline(input);
        int priority = setPriority(input);
        String type = setType(input);

        System.out.println("Setting task parameters...");
        Task newTask = new Task(name, description, deadline, priority, type, false);
        tasks.add(newTask);
        System.out.println("\033[1;32mTask successfully created!\n\033[0m");
        System.out.println("\033[1mCreated Task:\n\033[0m");
        System.out.println(newTask.printTaskWithDeadline(""));
        System.out.println();
    }

    private String setName(Scanner input){
        System.out.println("Write name of new task and press Enter:");
        String taskName;
        boolean duplicate;

        while(true){
            String name = input.nextLine();
            duplicate = false;

            if(name.isEmpty()){
                taskName = "Task" + taskCounter++;
                System.out.println("\033[1;31mNo name specified, defaulting to: '" + taskName + "'\033[0m");
                break;
            }
            else{
                for(Task task : tasks){
                    if(task.getName().equals(name)){
                        System.out.println("\033[1;31mThis name already exists.\033[0m Please enter different name and press Enter or just press Enter to generate automatic name: ");
                        duplicate = true;
                    }
                }
                if(!duplicate){
                    taskName = name;
                    break;
                }
            }
        }
        return taskName;
    }

    private String setDescription(Scanner input){
        System.out.println("If you would like to add a description of this task, write it down and press Enter, otherwise just press Enter:");
        String taskDescription = input.nextLine();
        if(taskDescription.isEmpty()){
            taskDescription = null;
        }
        return taskDescription;
    }

    private LocalDate setDeadline(Scanner input){
        System.out.println("If you would like to add a deadline to this task, enter deadline in format 'DD.MM.YYYY' and press Enter, otherwise just press Enter:");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate taskDeadline = null;

        while(true){
            String inputDate = input.nextLine().trim();

            if(inputDate.isEmpty()){
                break;
            }

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

    private int setPriority(Scanner input){
        System.out.println("If you would like to add this task a priority, enter a number (lower number = higher priority) and press Enter, otherwise just press Enter (no priority defaults to 0):");
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

    private String setType(Scanner input){
        System.out.println("If you would like to add this task's type (work/personal/school subject), enter a type and press Enter, otherwise, press Enter:");
        String taskType = input.nextLine();
        if(taskType.isEmpty()){
            taskType = null;
        }
        return taskType;
    }

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

    public void editTask(Scanner input){
        System.out.println("Enter name of task you would like to edit and press Enter:");
        String taskName = input.nextLine();
        Task editedTask = null;

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
        while(true){
            String command = input.nextLine().trim();
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
            printEditMenu();
        }
    }

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

    private void editName(Scanner input, Task task){
        String currentName = task.getName();
        System.out.println("Current name of task: '" + currentName + "'");
        System.out.println("Enter new name and press Enter:");
        boolean duplicate;

        while(true){
            String inputName = input.nextLine();
            duplicate = false;

            if(inputName.isEmpty()){
                System.out.println("\033[1;31mNo new name entered.\033[0m Please enter new name and press Enter or enter \033[1m'q'\033[0m and press Enter to keep the current name:");
                continue;
            }
            if(inputName.equals("q")){
                System.out.println("Keeping current name: '" + currentName + "'\n");
                break;
            }
            else{
                for(Task currentTask : tasks){
                    if(currentTask.getName().equals(inputName)){
                        System.out.println("\033[1;31mThis name already exists.\033[0m Please enter different name and press Enter or enter \033[1m'q'\033[0m and press Enter to keep the current name:");
                        duplicate = true;
                    }
                }
                if(!duplicate){
                    task.setName(inputName);
                    System.out.println("\033[1;32mName successfully changed from: '" + currentName + "' to: '" + inputName + "'\n\033[0m");
                    break;
                }
            }
        }
    }

    private void editDescription(Scanner input, Task task){
        String currentDescription = task.getDescription();
        System.out.println("Current description of task: '" + currentDescription + "'");
        System.out.println("Enter new description and press Enter:");

        while(true){
            String inputDescription = input.nextLine();
            if(inputDescription.equals("q")){
                System.out.println("Keeping current description: '" + currentDescription + "'\n");
                return;
            }
            if(inputDescription.isEmpty()){
                System.out.println("No new description entered. Add new description or keep old one by entering \033[1m'q'\033[0m and pressing Enter:");
            }
            else{
                task.setDescription(inputDescription);
                System.out.println("\033[1;32mDescription successfully changed from: '" + currentDescription + "' to: '" + inputDescription + "'\n\033[0m");
                break;
            }
        }
    }

    private void editDeadline(Scanner input, Task task){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate inputDeadline = task.getDeadline();
        String currentDeadline;
        if(inputDeadline == null){
            currentDeadline = "none";
        }
        else{
            currentDeadline = inputDeadline.format(formatter);
        }

        System.out.println("Current deadline of task: '" + currentDeadline + "'");
        System.out.println("Enter new deadline in format DD.MM.YYYY and press Enter, in case you want to delete deadline, just press Enter:");

        while(true){
            String inputDate = input.nextLine().trim();

            if(inputDate.isEmpty()){
                task.setDeadline(null);
                System.out.println("\033[1;32mDeadline successfully deleted!\n\033[0m");
                break;
            }

            if(inputDate.equals("q")){
                System.out.println("Keeping current deadline: '" + currentDeadline + "'\n");
                break;
            }

            try{
                inputDeadline = LocalDate.parse(inputDate, formatter);
                task.setDeadline(inputDeadline);
                System.out.println("\033[1;32mDeadline successfully changed from: '" + currentDeadline + "' to: '" + inputDate + "'\n\033[0m");
                break;
            }
            catch(DateTimeParseException e){
                System.out.println("\033[1;31mInvalid format.\033[0m Please use DD.MM.YYYY, or enter \033[1m'q'\033[0m and press Enter to keep current deadline, or press Enter to delete deadline:");
            }
        }
    }

    private void editPriority(Scanner input, Task task){
        int currentPriority = task.getPriority();
        System.out.println("Current priority of task: '" + currentPriority + "'");
        System.out.println("Enter new priority number (lower number = higher priority) and press Enter:");

        while(true){
            String inputPriority = input.nextLine().trim();

            if(inputPriority.isEmpty()){
                System.out.println("Keeping current priority: '" + currentPriority + "'\n");
                break;
            }

            try{
                int newPriority = Integer.parseInt(inputPriority);
                task.setPriority(newPriority);
                System.out.println("\033[1;32mPriority successfully changed from: '" + currentPriority + "' to: '" + newPriority + "'\n\033[0m");
                break;
            }
            catch(NumberFormatException e){
                System.out.println("\033[1;31mInvalid format.\033[0m Please select a valid integer or press Enter to keep current priority:");
            }
        }
    }

    private void editType(Scanner input, Task task){
        String currentType = task.getType();
        System.out.println("Current type of task: '" + currentType + "'");
        System.out.println("Enter new type and press enter key:");

        while(true){
            String newType = input.nextLine();
            if(newType.isEmpty()){
                System.out.println("No type specified. Write new type and press Enter, or enter \033[1m'q'\033[0m and press Enter to keep the current type, or enter \033[1m'd'\033[0m and press Enter to delete current type:");
                continue;
            }
            if(newType.equals("d")){
                task.setType(null);
                System.out.println("\033[1;32mType successfully deleted.\n\033[0m");
                break;
            }
            if(newType.equals("q")){
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

    private boolean editCompleteness(Scanner input, Task task){
        boolean completed = task.getCompleted();
        String taskName = task.getName();

        if(completed){
            System.out.println("Current task is marked as completed. Press Enter to mark task as incompleted, or enter \033[1;32m'q'\033[0m and press Enter to remain unchanged.");
            while(true){
                String newInput = input.nextLine().trim();
                if(newInput.isEmpty()){
                    task.setCompleted(false);
                    System.out.println("\033[1;32mSuccessfully marked current task as incompleted.\n\033[0m");
                    return false;
                }
                if(newInput.equals("q")){
                    System.out.println("Keeping the current task completed.\n");
                    return false;
                }
                else{
                    System.out.println("\033[1;31mInvalid option!\033[0m Press Enter to mark task as incompleted, or enter \033[1m'q'\033[0m and press Enter to remain unchanged.");
                }
            }
        }
        else{
            System.out.println("Current task is marked as incompleted. Press Enter to mark task as completed, or enter \033[1m'q'\033[0m and press Enter to remain unchanged.");
            while(true){
                String newInput = input.nextLine().trim();
                if(newInput.isEmpty()){
                    task.setCompleted(true);
                    System.out.println("\033[1;32mSuccessfully marked current task as completed.\n\033[0m");
                    System.out.println("Task '" + taskName + "' is now completed. Would you like to delete this task? Type 'yes' or 'no' and press Enter:");
                    String answerDelete = input.nextLine().trim();
                    if(answerDelete.equals("yes")){
                        tasks.remove(task);
                        System.out.println("\033[1;32mTask successfully deleted!\n\033[0m");
                        return true;
                    }
                    if(answerDelete.equals("no")){
                        System.out.println("\033[1;32mKeeping this task!\n\033[0m");
                    }
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
                    System.out.println("\033[1;31mInvalid option!\033[0m Press Enter to mark task as completed, or enter \033[1m'q'\033[0m and press Enter to remain unchanged.");
                }
            }
            return false;
        }
    }

    public void printTasksSortedByPriority(){
        if(tasks.isEmpty()){
            System.out.println("\033[1;31mNo tasks available.\n\033[0m");
            return;
        }

        Task[] array = tasks.toArray(new Task[0]);
        Task[] sorted = mergeSortByPriority(array);
        tasks = new ArrayList<>(Arrays.asList(sorted));

        System.out.println("------ TASKS SORTED BY PRIORITY ------");

        for(Task task : sorted){
            System.out.println(task.printTaskWithDeadline("") + "--------------------------------------");
        }
    }

    private Task[] mergeSortByPriority(Task[] array){
        if(array.length <= 1){
            return array;
        }

        int middle = array.length / 2;

        Task[] left = mergeSortByPriority(Arrays.copyOfRange(array, 0, middle));
        Task[] right = mergeSortByPriority(Arrays.copyOfRange(array, middle, array.length));

        return mergeByPriority(left, right);
    }

    private Task[] mergeByPriority(Task[] left, Task[] right){
        Task[] merged = new Task[left.length + right.length];
        int leftIndex = 0;
        int rightIndex = 0;
        int mergedIndex = 0;

        while(leftIndex < left.length && rightIndex < right.length){
            int leftPriority = left[leftIndex].getPriority();
            int rightPriority = right[rightIndex].getPriority();

            if(leftPriority == 0){
                leftPriority = Integer.MAX_VALUE;
            }
            if(rightPriority == 0){
                rightPriority = Integer.MAX_VALUE;
            }
            if(leftPriority <= rightPriority){
                merged[mergedIndex++] = left[leftIndex++];
            }
            else{
                merged[mergedIndex++] = right[rightIndex++];
            }
        }

        while(leftIndex < left.length){
            merged[mergedIndex++] = left[leftIndex++];
        }
        while(rightIndex < right.length){
            merged[mergedIndex++] = right[rightIndex++];
        }

        return merged;
    }

    public void printTasksSortedByDeadline(){
        if(tasks.isEmpty()){
            System.out.println("\033[1;31mNo tasks available.\n\033[0m");
            return;
        }

        Task[] array = tasks.toArray(new Task[0]);
        Task[] sorted = mergeSortByDeadline(array);
        tasks = new ArrayList<>(Arrays.asList(sorted));

        System.out.println("------ TASKS SORTED BY DEADLINE ------");

        for(Task task : sorted){
            System.out.println(task.printTaskWithDeadline("") + "--------------------------------------");
        }
    }

    private Task[] mergeSortByDeadline(Task[] array){
        if(array.length <= 1){
            return array;
        }

        int middle = array.length / 2;

        Task[] left = mergeSortByDeadline(Arrays.copyOfRange(array, 0, middle));
        Task[] right = mergeSortByDeadline(Arrays.copyOfRange(array, middle, array.length));

        return mergeByDeadline(left, right);
    }

    private Task[] mergeByDeadline(Task[] left, Task[] right){
        Task[] merged = new Task[left.length + right.length];
        int leftIndex = 0;
        int rightIndex = 0;
        int mergedIndex = 0;

        while(leftIndex < left.length && rightIndex < right.length){
            LocalDate leftDate = left[leftIndex].getDeadline();
            LocalDate rightDate = right[rightIndex].getDeadline();

            if(leftDate == null){
                merged[mergedIndex++] = right[rightIndex++];
            }
            else if(rightDate == null){
                merged[mergedIndex++] = left[leftIndex++];
            }
            else if(leftDate.isBefore(rightDate) || leftDate.equals(rightDate)){
                merged[mergedIndex++] = left[leftIndex++];
            }
            else{
                merged[mergedIndex++] = right[rightIndex++];
            }
        }

        while(leftIndex < left.length){
            merged[mergedIndex++] = left[leftIndex++];
        }
        while(rightIndex < right.length){
            merged[mergedIndex++] = right[rightIndex++];
        }

        return merged;
    }

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

    public void printTasksStatistics(){
        System.out.println("Task statistics:");
        int numberOfTasks = tasks.size();
        System.out.println("- Total tasks: " + numberOfTasks);
        int completedTasks = 0;
        int incompletedTasks = 0;
        int tasksWithUpcomingDeadline = 0;
        int tasksWithOverdueDeadline = 0;
        double sumOfPriorities = 0;
        Map<String, Integer> types = new HashMap<>();
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
            types.put(taskType, types.getOrDefault(taskType, 0) + 1);
        }

        System.out.println("- Completed tasks: " + completedTasks);
        System.out.println("- Incompleted tasks: " + incompletedTasks);
        System.out.println("- Upcoming tasks: " + tasksWithUpcomingDeadline);
        System.out.println("- Overdue tasks: " + tasksWithOverdueDeadline);
        double averagePriority = sumOfPriorities/numberOfTasks;
        System.out.printf("- Average priority: %.2f%n", averagePriority);

        types.entrySet().stream().sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .forEach(entry -> System.out.println("- Tasks of type '" + entry.getKey() + "': " + entry.getValue()));

        System.out.println();
    }

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
                List<Task> keywordTasks = new ArrayList<>();
                for(Task task : tasks){
                    String name = task.getName();
                    String description = task.getDescription();
                    String type = task.getType();
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

    private boolean containsKeyword(String taskParameter, String keyword){
        String[] allWords = taskParameter.split(" ");
        for(String word : allWords){
            if(word.toLowerCase().contains(keyword.toLowerCase())){
                return true;
            }
        }
        return false;
    }

}
