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
        return name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getDescription(){
        if(description != null){
            return description;
        }
        else{
            return "none";
        }
    }

    public void setDeadline(LocalDate deadline){
        this.deadline = deadline;
    }

    public LocalDate getDeadline(){
        return deadline;
    }

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getPriority(){
        return priority;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }

    public boolean getCompleted(){
        return completed;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getType(){
        if(type != null){
            return type;
        }
        else{
            return "none";
        }
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

        String descriptionString = getDescription();
        String typeString = getType();

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
