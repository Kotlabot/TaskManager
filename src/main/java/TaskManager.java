import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TaskManager {
    private List<Task> tasks;
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
            System.out.println("No tasks available.\n");
            return;
        }

        System.out.println("------ TASK LIST ------\n");

        for(Task task : tasks){
            System.out.println(task + "\n----------------------\n");
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
        System.out.println("Task successfully created!\n");
        System.out.println("Created Task:\n");
        System.out.println(newTask);
        System.out.println();
    }

    private String setName(Scanner input){
        System.out.println("Write name of new task and press enter key:");
        String name = input.nextLine();
        String taskName;

        if(name.isEmpty()){
            taskName = "Task" + taskCounter++;
            System.out.println("No name specified, defaulting to: '" + taskName + "'");
        }
        else{
            taskName = name;
        }
        return taskName;
    }

    private String setDescription(Scanner input){
        System.out.println("If you would like to add a description of this task, write it down and press enter key, otherwise press enter key:");
        String taskDescription = input.nextLine();
        if(taskDescription.isEmpty()){
            taskDescription = null;
        }
        return taskDescription;
    }

    private LocalDate setDeadline(Scanner input){
        System.out.println("If you would like to add a deadline to this task, enter deadline in format 'DD.MM.YYYY' and press enter key, otherwise, press enter key:");
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
                System.out.println("Invalid format. Please use DD.MM.YYYY or press enter key to skip:");
            }
        }
        return taskDeadline;
    }

    private int setPriority(Scanner input){
        System.out.println("If you would like to add this task a priority, enter a number (lower number = higher priority) and press enter key, otherwise, press enter key (no priority defaults to 0):");
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
                System.out.println("Invalid format. Please select a valid integer or press enter key to skip:");
            }
        }
        return taskPriority;
    }

    private String setType(Scanner input){
        System.out.println("If you would like to add this task's type (work/personal/school subject), enter a type and press enter key, otherwise, press enter key:");
        String taskType = input.nextLine();
        if(taskType.isEmpty()){
            taskType = null;
        }
        return taskType;
    }

    public void deleteTask(Scanner input){
        System.out.println("Enter name of task you would like to delete and press enter key:");
        String taskName = input.nextLine();
        for(Task task : tasks){
            if(Objects.equals(task.getName(), taskName)){
                tasks.remove(task);
                System.out.println("Task " + taskName + " successfully deleted!\n");
                return;
            }
        }
        System.out.println("No task with such name was found!\n");
    }

    public void editTask(Scanner input){
        System.out.println("Enter name of task you would like to edit and press enter key:");
        String taskName = input.nextLine();
        Task editedTask = null;

        for(Task task : tasks){
            if(Objects.equals(task.getName(), taskName)){
                editedTask = task;
            }
        }

        if(editedTask == null){
            System.out.println("No task with such name was found!\n");
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
                        System.out.println("Edited task was deleted, exiting edit mode...\n");
                        return;
                    }
                    break;
                case "q":
                    System.out.println("Exiting edit mode...\n");
                    return;
                default:
                    System.out.println("Invalid command option.\n");
            }
            printEditMenu();
        }
    }

    private void printEditMenu(){
        System.out.println(
                """
                   ------ Task Manager EDIT MENU  -------
                   1) To edit task's NAME, press 'n' and enter key
                   2) To edit task's DESCRIPTION, press 'ds' and enter key
                   3) To edit task's DEADLINE, press 'dl' and enter key
                   4) To edit task's PRIORITY, press 'p' and enter key
                   5) To edit task's TYPE, press 't' and enter key
                   6) To edit task's COMPLETENESS, press 'c' and enter key
                   7) To exit EDIT MODE, press 'q' and enter key
                """
        );
    }

    private void editName(Scanner input, Task task){
        String currentName = task.getName();
        System.out.println("Current name of task: '" + currentName + "'");
        System.out.println("Enter new name and press enter key:");

        while(true){
            String inputName = input.nextLine();
            if(inputName.isEmpty()){
                System.out.println("No new name entered, please enter new name and press enter key or press 'q' to keep the current name:");
                continue;
            }
            if(inputName.equals("q")){
                System.out.println("Keeping current name: '" + currentName + "'\n");
            }
            else{
                task.setName(inputName);
                System.out.println("Name successfully changed from: '" + currentName + "' to: '" + inputName + "'\n");
            }
            break;
        }
    }

    private void editDescription(Scanner input, Task task){
        String currentDescription = task.getDescription();
        System.out.println("Current description of task: '" + currentDescription + "'");
        System.out.println("Enter new description and press enter key:");

        while(true){
            String inputDescription = input.nextLine();
            if(inputDescription.equals("q")){
                System.out.println("Keeping current description: '" + currentDescription + "'\n");
                return;
            }
            if(inputDescription.isEmpty()){
                System.out.println("No new description entered. Add new description or keep old one by pressing 'q':");
            }
            else{
                task.setDescription(inputDescription);
                System.out.println("Description successfully changed from: '" + currentDescription + "' to: '" + inputDescription + "'\n");
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
        System.out.println("Enter new deadline in format DD.MM.YYYY and press enter key, in case you want to delete deadline, just press enter key:");

        while(true){
            String inputDate = input.nextLine().trim();

            if(inputDate.isEmpty()){
                task.setDeadline(null);
                System.out.println("Deadline successfully deleted!\n");
                break;
            }

            if(inputDate.equals("q")){
                System.out.println("Keeping current deadline: '" + currentDeadline + "'\n");
                break;
            }

            try{
                inputDeadline = LocalDate.parse(inputDate, formatter);
                task.setDeadline(inputDeadline);
                System.out.println("Deadline successfully changed from: '" + currentDeadline + "' to: '" + inputDate + "'\n");
                break;
            }
            catch(DateTimeParseException e){
                System.out.println("Invalid format. Please use DD.MM.YYYY, or press 'q' to keep current deadline, or press enter key to delete deadline:");
            }
        }
    }

    private void editPriority(Scanner input, Task task){
        int currentPriority = task.getPriority();
        System.out.println("Current priority of task: '" + currentPriority + "'");
        System.out.println("Enter new priority number (lower number = higher priority) and press enter key:");

        while(true){
            String inputPriority = input.nextLine().trim();

            if(inputPriority.isEmpty()){
                System.out.println("Keeping current priority: '" + currentPriority + "'\n");
                break;
            }

            try{
                int newPriority = Integer.parseInt(inputPriority);
                task.setPriority(newPriority);
                System.out.println("Priority successfully changed from: '" + currentPriority + "' to: '" + newPriority + "'\n");
                break;
            }
            catch(NumberFormatException e){
                System.out.println("Invalid format. Please select a valid integer or press enter key to keep current priority:");
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
                System.out.println("No type specified. Write new type and press enter key, or press 'q' to keep the current type, or press 'd' to delete current type:");
                continue;
            }
            if(newType.equals("d")){
                task.setType(null);
                System.out.println("Type successfully deleted.\n");
                break;
            }
            if(newType.equals("q")){
                System.out.println("Keeping the current type: '" + currentType + "'\n");
                break;
            }
            else{
                task.setType(newType);
                System.out.println("Type successfully changed from: '" + currentType + "' to: '" + newType + "'\n");
                break;
            }
        }
    }

    private boolean editCompleteness(Scanner input, Task task){
        boolean completed = task.getCompleted();
        String taskName = task.getName();

        if(completed){
            System.out.println("Current task is marked as completed. Press enter key to mark task as incompleted, or press 'q' to remain unchanged.");
            while(true){
                String newInput = input.nextLine().trim();
                if(newInput.isEmpty()){
                    task.setCompleted(false);
                    System.out.println("Successfully marked current task as incompleted.\n");
                    return false;
                }
                if(newInput.equals("q")){
                    System.out.println("Keeping the current task completed.\n");
                    return false;
                }
                else{
                    System.out.println("Invalid option! Press enter key to mark task as incompleted, or press 'q' to remain unchanged.");
                }
            }
        }
        else{
            System.out.println("Current task is marked as incompleted. Press enter key to mark task as completed, or press 'q' to remain unchanged.");
            while(true){
                String newInput = input.nextLine().trim();
                if(newInput.isEmpty()){
                    task.setCompleted(true);
                    System.out.println("Successfully marked current task as completed.\n");
                    System.out.println("Task '" + taskName + "' is now completed. Would you like to delete this task? Type 'yes' or 'no' and press enter key:");
                    String answerDelete = input.nextLine().trim();
                    if(answerDelete.equals("yes")){
                        tasks.remove(task);
                        System.out.println("Task successfully deleted!\n");
                        return true;
                    }
                    if(answerDelete.equals("no")){
                        System.out.println("Keeping this task!\n");
                    }
                    else{
                        System.out.println("Invalid option. Keeping this task!\n");
                    }
                    break;
                }
                if(newInput.equals("q")){
                    System.out.println("Keeping the current task incompleted.\n");
                    break;
                }
                else{
                    System.out.println("Invalid option! Press enter key to mark task as completed, or press 'q' to remain unchanged.");
                }
            }
            return false;
        }
    }

    public void printTasksByPriority(){
        if(tasks.isEmpty()){
            System.out.println("No tasks available.\n");
            return;
        }

        Task[] array = tasks.toArray(new Task[0]);
        Task[] sorted = mergeSortByPriority(array);

        System.out.println("------ TASKS SORTED BY PRIORITY ------\n");

        for(Task task : sorted){
            System.out.println(task + "\n--------------------------------------\n");
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

    public void printTasksByDeadline(){
        if(tasks.isEmpty()){
            System.out.println("No tasks available.\n");
            return;
        }

        Task[] array = tasks.toArray(new Task[0]);
        Task[] sorted = mergeSortByDeadline(array);

        System.out.println("------ TASKS SORTED BY DEADLINE ------\n");

        for(Task task : sorted){
            System.out.println(task + "\n--------------------------------------\n");
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

}
