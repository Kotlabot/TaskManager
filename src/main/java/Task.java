import java.time.LocalDate;

public class Task {
    public String name;
    public int priority;
    public LocalDate deadline;
    public boolean completed;
    public String type;
    public String subject;
    public String description;

    public Task(String name){
        this.name = name;
    }
}
