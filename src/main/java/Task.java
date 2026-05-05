import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private String name;
    private String description;
    private LocalDate deadline;
    private int priority;
    private String type;
    private boolean completed;

    public Task(String name, String description, LocalDate deadline, int priority, String type, boolean completed){
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.type = type;
        this.completed = completed;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDeadline(LocalDate deadline){
        this.deadline = deadline;
    }

    public LocalDate getDeadline(){
        return this.deadline;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getPriority(){
        return this.priority;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }

    public boolean getCompleted(){
        return this.completed;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        return this.type;
    }

    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String deadlineString;
        if(deadline != null){
            deadlineString = deadline.format(formatter);
        }
        else{
            deadlineString = "none";
        }

        String descriptionString;
        if(description != null){
            descriptionString = description;
        }
        else{
            descriptionString = "none";
        }

        String typeString;
        if(type != null){
            typeString = type;
        }
        else{
            typeString = "none";
        }

        String completedString;
        if(completed){
            completedString = "completed";
        }
        else{
            completedString = "incompleted";
        }

        return """
           Name: %s
           Description: %s
           Deadline: %s
           Priority: %d
           Type: %s
           Completed: %s
           """.formatted(name, descriptionString, deadlineString, priority, typeString, completedString);
    }
}
