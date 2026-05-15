package taskmanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Unit tests for the TaskManager class.
 */
public class TaskManagerTest{

    /**
     * Tests correct addition of new task to task manager.
     */
    @Test
    void testAddTask(){
        TaskManager manager = new TaskManager();

        // Simulate user input
        Scanner input = new Scanner("""
                Homework
                Finish Java home project
                20.05.2026
                1
                school
                """);

        manager.addTask(input);
        List<Task> tasks = manager.getTasks();
        Task task = tasks.getFirst();

        assertEquals(1, tasks.size());
        assertEquals("Homework", task.getName());
        assertEquals("Finish Java home project", task.getDescription());
        assertEquals(LocalDate.of(2026, 5, 20), task.getDeadline());
        assertEquals(1, task.getPriority());
        assertEquals("school", task.getType());
        assertFalse(task.getCompleted());
    }

    /**
     * Tests that add method does not allow duplicate names.
     */
    @Test
    void testAddTaskNotAllowDuplicateNames(){
        TaskManager manager = new TaskManager();

        // Simulate user input
        Scanner inputTask1 = new Scanner("""
                Homework
                Finish Java home project
                20.05.2026
                1
                school
                """);

        manager.addTask(inputTask1);

        // Simulate user input with duplicate name
        // 1) enter duplicate name "Homework"
        // 2) method outputs error message about duplicate name and offer to try again or generate automatic name. Enter valid name "Java Homework"
        // 3)-6) enter rest of task parameters
        Scanner inputTask2 = new Scanner("""
                Homework
                Java Homework
                Finish Java home project
                20.05.2026
                1
                school
                """);

        // Capture console output for adding second task with duplicate name
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        manager.addTask(inputTask2);

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("\033[1;31mThis name already exists.\033[0m Please enter different name and " +
                "press Enter or just press Enter to generate automatic name: "));
        assertEquals("Homework", manager.getTasks().get(0).getName());
        assertEquals("Java Homework", manager.getTasks().get(1).getName());
    }

    /**
     * Tests automatic task name generation when user does not specify any name.
     */
    @Test
    void testAutomaticTaskNameGeneration(){
        TaskManager manager = new TaskManager();

        // Simulate user input with no name specified
        Scanner firstInput = new Scanner("""
            
            Finish Java home project
            20.05.2026
            1
            school
            """);

        // Simulate user input with no name specified
        Scanner secondInput = new Scanner("""
            
            Finish Java home project
            20.05.2026
            1
            school
            """);

        // First nameless task defaults to "Task1"
        manager.addTask(firstInput);
        // Second nameless task defaults to "Task2"
        manager.addTask(secondInput);

        List<Task> tasks = manager.getTasks();
        Task firstTask = tasks.getFirst();
        Task secondTask = tasks.get(1);

        assertEquals("Task1", firstTask.getName());
        assertEquals("Task2", secondTask.getName());
    }

    /**
     * Tests that add method does not allow incorrect format of a deadline.
     */
    @Test
    void testAddTaskNotAllowIncorrectDateFormat(){
        TaskManager manager = new TaskManager();

        // Simulate user input
        // 1)-2) enter valid name and description
        // 3) enter nonsense date "45.24.1234". Method outputs error message about incorrect format and offers to try again.
        // 4) enter date in incorrect format "20.5.2026" Method outputs error message about incorrect format and offers to try again.
        // 5) enter date in correct format.
        // 6)-7) enter valid priority and type
        Scanner input = new Scanner("""
                Homework
                Finish Java home project
                45.24.1234
                20.5.2026
                20.05.2026
                1
                school
                """);

        // Capture console output for adding second task with duplicate name
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        manager.addTask(input);

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("\033[1;31mInvalid format.\033[0m Please use DD.MM.YYYY or press Enter to skip:"));
        assertEquals(LocalDate.of(2026, 5, 20), manager.getTasks().getFirst().getDeadline());
    }

    /**
     * Tests that add method does not allow incorrect format of a priority (non-integer inputs).
     */
    @Test
    void testAddTaskNotAllowIncorrectPriorityFormat(){
        TaskManager manager = new TaskManager();

        // Simulate user input
        // 1)-3) enter valid name, description and deadline
        // 4) enter priority in incorrect format (floating point value). Method outputs error message about incorrect format and offers to try again.
        // 5) enter priority in incorrect format (string). Method outputs error message about incorrect format and offers to try again.
        // 6) enter priority in correct format (integer value).
        // 7) enter type
        Scanner input = new Scanner("""
                Homework
                Finish Java home project
                20.05.2026
                15.5
                high priority
                1
                school
                """);

        // Capture console output for adding second task with duplicate name
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        manager.addTask(input);

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("\033[1;31mInvalid format.\033[0m Please select a valid integer or press Enter to skip:"));
        assertEquals(1, manager.getTasks().getFirst().getPriority());
    }

    /**
     * Tests correct task deletion according to specified name of task.
     */
    @Test
    void testDeleteTask(){
        Task task = new Task("Name", null, null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input with name of task to be deleted
        Scanner input = new Scanner ("Name");
        manager.deleteTask(input);

        assertTrue(manager.getTasks().isEmpty());
    }

    /**
     * Tests that no task is deleted when incorrect name is provided.
     */
    @Test
    void testDeleteTaskWithNotValidTaskName(){
        Task task = new Task("Name", null, null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input with name of task to be deleted
        Scanner input = new Scanner ("Task");
        manager.deleteTask(input);

        assertEquals(1, manager.getTasks().size());
    }

    /**
     * Tests that task with incorrect name can not be edited.
     */
    @Test
    void testEditTaskWithNotValidTaskName(){
        Task task = new Task("Name", null, null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input with name of task to be edited
        Scanner input = new Scanner ("Incorrect Name");
        manager.editTask(input);

        assertEquals("Name", manager.getTasks().getFirst().getName());
    }

    /**
     * Tests edit of name of existing task.
     */
    @Test
    void testEditTaskName(){
        Task task = new Task("Name", null, null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input with new name
        // 1) choose task "Name"
        // 2) select edit option "n"
        // 3) enter new name "Task"
        // 4) quit edit mode "q"
        Scanner input = new Scanner("""
            Name
            n
            Task
            q
            """);
        manager.editTask(input);

        Task editedTask = manager.getTasks().getFirst();

        assertEquals("Task", editedTask.getName());
    }

    /**
     * Tests that name editing method does not allow duplicate name.
     */
    @Test
    void testEditTaskNameDuplicate(){
        Task task1 = new Task("Task1", null, null, 0, null, false);
        Task task2 = new Task("Task2", null, null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input with new name
        // 1) choose task "Task2"
        // 2) select edit option "n"
        // 3) enter duplicate name "Task1"
        // 4) method outputs error message about duplicate name and offer to try again. Enter valid name "Task3"
        // 5) exit edit mode "q"
        Scanner input = new Scanner("""
            Task2
            n
            Task1
            Task3
            q
            """);

        // Capture console output
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        manager.editTask(input);

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("\033[1;31mThis name already exists.\033[0m Please enter different name and " +
                "press Enter or enter \033[1m'q'\033[0m and press Enter to keep the current name:"));
        assertTrue(output.contains("\033[1;32mName successfully changed from: 'Task2' to: 'Task3'\n\033[0m"));

    }

    /**
     * Tests edit of description of existing task (deleting current description).
     */
    @Test
    void testEditDeleteTaskDescription(){
        Task task = new Task("Name", "Task test delete description", null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input
        // 1) choose task "Name"
        // 2) select edit option "ds"
        // 3) delete current description "d"
        // 4) quit edit mode "q"
        Scanner input = new Scanner("""
            Name
            ds
            d
            q
            """);
        manager.editTask(input);

        Task editedTask = manager.getTasks().getFirst();

        assertEquals("none", editedTask.getDescription());
    }

    /**
     * Tests edit of completion state.
     *
     * Tests if method correctly marks completion state as completed and then delete the completed task.
     */
    @Test
    void testEditTaskCompletionStateAndDeleteCompletedTask(){
        Task task = new Task("Name", null, null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input
        // 1) choose task "Name"
        // 2) select edit option "c"
        // 3) mark task as completed
        // 4) delete completed task "yes"
        Scanner input = new Scanner("""
            Name
            c
            
            yes
            """);

        manager.editTask(input);

        assertEquals(0, manager.getTasks().size());
    }

    /**
     * Tests edit of completion state.
     *
     * Tests if method correctly marks completion state as completed and then keep the completed task.
     */
    @Test
    void testEditTaskCompletionStateAndKeepCompletedTask(){
        Task task = new Task("Name", null, null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input
        // 1) choose task "Name"
        // 2) select edit option "c"
        // 3) mark task as completed
        // 4) keep completed task "q"
        // 5) exit edit mode "q"
        Scanner input = new Scanner("""
            Name
            c
            
            no
            q
            """);
        manager.editTask(input);

        assertEquals(1, manager.getTasks().size());
    }

    /**
     * Tests that invalid command option in edit menu does not crash the application.
     */
    @Test
    void testEditMenuInvalidOption(){
        Task task = new Task("Task", null, null, 0, null, false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        TaskManager manager = new TaskManager(tasks);

        // Simulate user input with invalid command option
        // 1) enter name of task to enter edit menu "Task"
        // 2) enter invalid command option. Method outputs error message but stays in the edit menu.
        // 3) exit edit mode "q"
        Scanner input = new Scanner("""
            Task
            invalid
            q
            """);

        // Capture console output
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        manager.editTask(input);

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("\033[1;31mInvalid command option.\n\033[0m"));
    }

    /**
     * Tests sorting tasks by priority.
     */
    @Test
    void testSortTasksByPriority(){
        Task task1 = new Task("Task1", null, null, 2, null, false);
        Task task2 = new Task("Task2", null, null, 4, null, false);
        Task task3 = new Task("Task3", null, null, 3, null, false);
        Task task4 = new Task("Task4", null, null, 5, null, false);
        Task task5 = new Task("Task5", null, null, 1, null, false);
        Task task6 = new Task("Task6", null, null, 0, null, false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);
        tasks.add(task6);

        TaskManager manager = new TaskManager(tasks);
        manager.printTasksSortedByPriority();
        List<Task> sortedTasks = manager.getTasks();

        assertEquals("Task5", sortedTasks.get(0).getName());
        assertEquals("Task1", sortedTasks.get(1).getName());
        assertEquals("Task3", sortedTasks.get(2).getName());
        assertEquals("Task2", sortedTasks.get(3).getName());
        assertEquals("Task4", sortedTasks.get(4).getName());
        assertEquals("Task6", sortedTasks.get(5).getName());
    }

    /**
     * Tests sorting tasks by deadline
     */
    @Test
    void testSortTasksByDeadline(){
        Task task1 = new Task("Second", null, LocalDate.of(2026, 6, 25), 0, null, false);
        Task task2 = new Task("First", null, LocalDate.of(2026, 5, 30), 0, null, false);
        Task task3 = new Task("Third", null, LocalDate.of(2026, 6, 28), 0, null, false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        TaskManager manager = new TaskManager(tasks);
        manager.printTasksSortedByDeadline();
        List<Task> sortedTasks = manager.getTasks();

        assertEquals("First", sortedTasks.get(0).getName());
        assertEquals("Second", sortedTasks.get(1).getName());
        assertEquals("Third", sortedTasks.get(2).getName());
    }

    /**
     * Tests sorting method does not throw exception when empty list is provided.
     */
    @Test
    void testSortEmptyTasksList(){
        TaskManager manager = new TaskManager();

        // Capture console output
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        manager.printTasksSortedByDeadline();

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("\033[1;31mNo tasks available.\n\033[0m"));
    }

    /**
     * Tests task statistics counting and printing.
     */
    @Test
    void testTaskStatistics(){
        LocalDate futureDate = LocalDate.now().plusDays(5);
        LocalDate overdueDate = LocalDate.now().minusDays(2);

        Task task1 = new Task("Task1", null, overdueDate, 2, "school", true);
        Task task2 = new Task("Task2", null, overdueDate, 4, "work", true);
        Task task3 = new Task("Task3", null, futureDate, 3, "school", false);
        Task task4 = new Task("Task4", null, futureDate, 5, "work", false);
        Task task5 = new Task("Task5", null,futureDate, 1, "personal", false);
        Task task6 = new Task("Task6", null, futureDate, 0, null, false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);
        tasks.add(task4);
        tasks.add(task5);
        tasks.add(task6);

        TaskManager manager = new TaskManager(tasks);

        // Capture console output
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        manager.printTasksStatistics();

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("- Total tasks: 6"));
        assertTrue(output.contains("- Completed tasks: 2"));
        assertTrue(output.contains("- Incompleted tasks: 4"));
        assertTrue(output.contains("- Upcoming tasks: 4"));
        assertTrue(output.contains("- Overdue tasks: 2"));
        assertTrue(output.contains("- Average priority: 2,50"));
        assertTrue(output.contains("- Tasks of type 'school': 2"));
        assertTrue(output.contains("- Tasks of type 'work': 2"));
        assertTrue(output.contains("- Tasks of type 'personal': 1"));
        assertTrue(output.contains("- Tasks of type 'undefined': 1"));
    }

    /**
     * Tests statistics method does not throw exception when empty list is provided.
     */
    @Test
    void testStatisticForEmptyTasksList(){
        TaskManager manager = new TaskManager();

        // Capture console output
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        manager.printTasksStatistics();

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("\033[1;31mNo tasks available.\n\033[0m"));
    }

}
