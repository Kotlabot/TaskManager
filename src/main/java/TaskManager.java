import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

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

    public void printAllTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
            return;
        }

        System.out.println("------ TASK LIST ------\n");

        for (Task task : tasks) {
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
            System.out.println("No name specified, defaulting to:" + taskName);
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

        while (true){
            String inputDate = input.nextLine().trim();

            if (inputDate.isEmpty()){
                break;
            }

            try{
                taskDeadline = LocalDate.parse(inputDate, formatter);
                break;
            } catch (DateTimeParseException e){
                System.out.println("Invalid format. Please use DD.MM.YYYY or press enter key to skip:");
            }
        }
        return taskDeadline;
    }

    private int setPriority(Scanner input){
        System.out.println("If you would like to add this task a priority, enter a number (lower number = higher priority) and press enter key, otherwise, press enter key (no priority defaults to 0):");
        int taskPriority = 0;
        while(true){
            String inputDeadline = input.nextLine().trim();

            if(inputDeadline.isEmpty()){
                break;
            }

            try{
                taskPriority = Integer.parseInt(inputDeadline);
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
                System.out.println("Task " + task.getName() + " successfully deleted!\n");
                return;
            }
        }
        System.out.println("No task with such name was found!\n");
    }

    public void editTask(Scanner input){

    }

}
