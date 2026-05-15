package taskmanager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

/**
 * Unit tests for the Task class.
 */
public class TaskTest{

    /**
     * Tests correct task creation using Task constructor.
     */
    @Test
    void testTaskCreation(){
        Task task = new Task("Name", "Description", LocalDate.of(2026, 5, 22),
                1, "Type", false);

        assertEquals("Name", task.getName());
        assertEquals("Description", task.getDescription());
        assertEquals(LocalDate.of(2026, 5, 22), task.getDeadline());
        assertEquals(1, task.getPriority());
        assertEquals("Type", task.getType());
        assertFalse(task.getCompleted());
    }

    /**
     * Tests correct task creation using Task constructor, when optional parameters are not defined.
     */
    @Test
    void testTaskCreationWithoutOptionalParameters(){
        Task task = new Task("Name", null, null, 0, null, false);

        assertEquals("none", task.getDescription());
        assertNull(task.getDeadline());
        assertEquals("none", task.getType());
    }

    /**
     * Tests correct formatting of task output.
     */
    @Test
    void testFormattedTaskPrinting(){
        Task task = new Task("Test Task Name", "Test correct formatted printing",
                LocalDate.of(2026, 6, 1), 0, null, false);

        String correctlyFormattedTask =
                """
                Name: Test Task Name
                Description: Test correct formatted printing
                Deadline: 01.06.2026
                Priority: 0
                Type: none
                Completed: incompleted
                """;

        String actualFormattedTask = task.toString();

        assertEquals(correctlyFormattedTask, actualFormattedTask);
    }

    /**
     * Tests correct formating of task deadline information (displaying days remaining to deadline).
     */
    @Test
    void testTaskPrintingWithFutureDeadline(){
        LocalDate futureDate = LocalDate.now().plusDays(5);

        Task task = new Task("Name", "Description", futureDate, 1, "Type", false);
        String printedTask = task.printTaskWithDeadline("");

        assertTrue(printedTask.contains("(in 5 days)"));
    }

    /**
     * Tests correct formatting of task deadline information (display today if deadline is the same day as current day).
     */
    @Test
    void testTaskPrintingWithTodayDeadline(){
        Task task = new Task("Name", "Description", LocalDate.now(), 1, "Type", false);
        String printedTask = task.printTaskWithDeadline("");

        assertTrue(printedTask.contains("(today)"));
    }

    /**
     * Tests correct formatting of task deadline information (displaying days after deadline).
     */
    @Test
    void testTaskPrintingWithOverdueDeadline(){
        LocalDate overdueDate = LocalDate.now().minusDays(2);

        Task task = new Task("Name", "Description", overdueDate, 1, "Type", false);
        String printedTask = task.printTaskWithDeadline("");

        assertTrue(printedTask.contains("(2 days after deadline)"));
    }

    /**
     * Tests highlighting of keyword specified to the print method.
     */
    @Test
    void testKeywordHighlighting(){
        Task task = new Task("Homework", "Practice Java programming",
                LocalDate.of(2026, 5, 22), 0, "school", false);
        String printedTask = task.printTaskWithDeadline("Java");

        assertTrue(printedTask.contains("\033[1;33mJava\033[0m"));
    }
}
