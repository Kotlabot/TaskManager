package taskmanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

/**
 * Represents one task in task manager application.
 *
 * Every task contain:
 * - name (mandatory)
 * - description (optional)
 * - deadline (optional)
 * - priority (optional)
 * - type (optional)
 * - completion state (default incomplete)
 */
public class Task{
    private String name;
    private String description;
    private LocalDate deadline;
    private int priority;
    private String type;
    private boolean completed;

    /**
     * Creates task with all parameters specified.
     *
     * @param name task name
     * @param description task description
     * @param deadline task deadline
     * @param priority task priority
     * @param type task type
     * @param completed task completion state
     */
    public Task(String name, String description, LocalDate deadline, int priority, String type, boolean completed){
        this.name = name;
        this.description = description;
        this.deadline = deadline;
        this.priority = priority;
        this.type = type;
        this.completed = completed;
    }

    /**
     * Empty constructor required for JSON deserialization.
     */
    public Task(){}

    /**
     * Sets task name.
     *
     * @param name new task name
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * Returns task name.
     *
     * @return task name
     */
    public String getName(){
        return name;
    }

    /**
     * Sets task description.
     *
     * @param description new task description
     */
    public void setDescription(String description){
        this.description = description;
    }

    /**
     * Returns task description.
     *
     * As description is optional, if task has no description, it returns string "none" for printing.
     * @return task description
     */
    public String getDescription(){
        if(description != null){
            return description;
        }
        else{
            return "none";
        }
    }

    /**
     * Sets task deadline.
     *
     * @param deadline new task deadline
     */
    public void setDeadline(LocalDate deadline){
        this.deadline = deadline;
    }

    /**
     * Returns task deadline.
     *
     * @return task deadline
     */
    public LocalDate getDeadline(){
        return deadline;
    }

    /**
     * Sets task priority.
     *
     * @param priority new task priority
     */
    public void setPriority(int priority){
        this.priority = priority;
    }

    /**
     * Returns task priority.
     *
     * @return task priority
     */
    public int getPriority(){
        return priority;
    }

    /**
     * Sets task completion state.
     *
     * @param completed new completion state
     */
    public void setCompleted(boolean completed){
        this.completed = completed;
    }

    /**
     * Returns task completion state.
     *
     * @return task completion state
     */
    public boolean getCompleted(){
        return completed;
    }

    /**
     * Sets task type.
     *
     * @param type new task type
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Returns task type.
     *
     * As type is optional, if task has no type, it returns string "none" for printing.
     * @return task type
     */
    public String getType(){
        if(type != null){
            return type;
        }
        else{
            return "none";
        }
    }

    /**
     * Returns formatted task representation including remaining days until deadline.
     *
     * Used for printing tasks.
     * When using for printing tasks with defined keyword, keyword occurrences are highlighted.
     *
     * @param keyword keyword to highlight
     * @return formatted task information
     */
    public String printTaskWithDeadline(String keyword){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String deadlineString;

        // Format deadline for printing. As deadline is optional parameter, if task has no deadline, print "none".
        // Otherwise, format the deadline to string and add information about remaining days until deadline.
        if(deadline != null){
            deadlineString = deadline.format(formatter);

            LocalDate today = LocalDate.now();

            // Calculate difference between current date and deadline
            long daysDifference = ChronoUnit.DAYS.between(today, deadline);

            if(daysDifference >= 0){
                if(daysDifference == 0){
                    deadlineString += " (today)";
                }
                else if(daysDifference == 1){
                    deadlineString += " (in " + daysDifference + " day)";
                }
                else{
                    deadlineString += " (in " + daysDifference + " days)";
                }
            }
            else{
                if(daysDifference == -1){
                    deadlineString += " (" + Math.abs(daysDifference) + " day after deadline)";
                }
                else{
                    deadlineString += " (" + Math.abs(daysDifference) + " days after deadline)";
                }
            }
        }
        else{
            deadlineString = "none";
        }

        String nameString = getName();
        String descriptionString = getDescription();
        String typeString = getType();

        // Highlight matching keywords in task parameters
        if(!keyword.isEmpty()){
            nameString = highlightKeyword(nameString, keyword);
            descriptionString = highlightKeyword(descriptionString, keyword);
            typeString = highlightKeyword(typeString, keyword);
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
           """.formatted(nameString, descriptionString, deadlineString, priority, typeString, completedString);
    }

    /**
     * Highlight all occurrences of a keyword in the given text.
     *
     * @param text text to search keyword in
     * @param keyword keyword to highlight
     * @return formatted text with highlighted keyword
     */
    private String highlightKeyword(String text, String keyword){
        // Highlight the keywords using ANSI color escape codes.
        String highlightedKeywords = "\033[1;33m" + keyword + "\033[0m";
        // Find all keyword occurrences using case-insensitive regex pattern.
        return text.replaceAll("(?i)" + Pattern.quote(keyword), highlightedKeywords);
    }

    /**
     * Returns formatted task representation.
     *
     * Used for saving tasks to text file.
     *
     * @return formatted task information
     */
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
