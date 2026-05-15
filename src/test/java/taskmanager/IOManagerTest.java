package taskmanager;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the IOManager class.
 */
public class IOManagerTest{

    /**
     * Tests saving tasks to a TXT file.
     *
     * Verifies that the file is successfully created and contains
     * correctly formatted task data.
     *
     * @throws IOException if reading or deleting the test file fails
     */
    @Test
    void testSaveTasksToTXT() throws IOException {
        IOManager iomanager = new IOManager();

        Task task = new Task("HomeWork", "Finish Java project", LocalDate.of(2026, 5, 22),
                1, "school", false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        // Simulate user input with name of file
        Scanner input = new Scanner("testOneTask");
        iomanager.saveTasksToTXT(input, tasks);

        // Test task file was created
        Path path = Path.of("data/testOneTask.txt");
        assertTrue(Files.exists(path));

        // Test file content
        String content = Files.readString(path);
        assertTrue(content.contains("Name: HomeWork"));
        assertTrue(content.contains("Description: Finish Java project"));
        assertTrue(content.contains("Deadline: 22.05.2026"));
        assertTrue(content.contains("Priority: 1"));
        assertTrue(content.contains("Type: school"));
        assertTrue(content.contains("Completed: incompleted"));

        // Clean up created files
        Files.deleteIfExists(path);
    }

    /**
     * Tests saving tasks to a TXT file when no file name is specified by the user.
     *
     * Verifies that the method correctly generates unique automatic file names
     * using the format "tasks1.txt", "tasks2.txt"...
     *
     * @throws IOException if file creation or deletion fails
     */
    @Test
    void testSaveTasksToTXTAndGenerateUniqueFileName() throws IOException {
        IOManager iomanager = new IOManager();

        Task task = new Task("HomeWork", "Finish Java project", LocalDate.of(2026, 5, 22),
                1, "school", false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        // Simulate user input with no name specified
        // Method generates automatic name, "tasks1.txt"
        Scanner firstFileInput = new Scanner("""
                
                """);
        iomanager.saveTasksToTXT(firstFileInput, tasks);

        // Simulate user input with no name specified
        // Method generates automatic name, this time "tasks2.txt"
        Scanner secondFileInput = new Scanner("""
                
                """);
        iomanager.saveTasksToTXT(secondFileInput, tasks);

        // Test task files were created with valid names
        Path path1 = Path.of("data/tasks1.txt");
        assertTrue(Files.exists(path1));
        Path path2 = Path.of("data/tasks2.txt");
        assertTrue(Files.exists(path2));

        // Clean up created files
        Files.deleteIfExists(path1);
        Files.deleteIfExists(path2);
    }

    /**
     * Tests saving tasks to a TXT file when the user specifies a duplicate file name.
     *
     * Verifies that the method prevents overwriting existing files and prompts the user to enter a different file name.
     *
     * @throws IOException if file creation or deletion fails
     */
    @Test
    void testSaveTasksToTXTAndNotAllowDuplicateFileNames() throws IOException {
        IOManager iomanager = new IOManager();

        Task task = new Task("HomeWork", "Finish Java project", LocalDate.of(2026, 5, 22),
                1, "school", false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        // Simulate user input with name of file
        Scanner firstInput = new Scanner("testOneTask");
        iomanager.saveTasksToTXT(firstInput, tasks);

        // Simulate user input when duplicate name is provided
        // 1) enter duplicate name "testOneTask". Method outputs error and offers to try different name or generate automatic one.
        // 2) enter valid name
        Scanner secondFileInput = new Scanner("""
                testOneTask
                testDifferentName
                """);

        // Capture console output for adding second task with duplicate name
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        iomanager.saveTasksToTXT(secondFileInput, tasks);

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        assertTrue(output.contains("\033[1;31mThis filename already exists.\033[0m Please enter different name or press Enter to select automatic name:"));

        // Test task files were created with valid names
        Path firstPath = Path.of("data/testOneTask.txt");
        assertTrue(Files.exists(firstPath));
        Path secondPath = Path.of("data/testDifferentName.txt");
        assertTrue(Files.exists(secondPath));

        // Clean up created files
        Files.deleteIfExists(firstPath);
        Files.deleteIfExists(secondPath);
    }

    /**
     * Tests loading tasks from a TXT file.
     *
     * Verifies that task data is correctly loaded and converted into Task objects.
     *
     * @throws IOException if creating, reading, or deleting the test file fails
     */
    @Test
    void testLoadTasksFromTXT() throws IOException {
        IOManager iomanager = new IOManager();
        Files.createDirectories(Path.of("data"));

        // Create file with one task to load later
        Path path = Path.of("data/testLoad.txt");
        String testFileContent = """
                -------- TASK LIST --------
                Name: Homework
                Description: Finish Java Project
                Deadline: 22.05.2026
                Priority: 1
                Type: school
                Completed: incompleted
                ----------------------
                """;
        Files.writeString(path, testFileContent);

        // Simulate user input with valid existing file name to load
        Scanner input = new Scanner("testLoad");
        List<Task> loadedTasks = iomanager.loadTasksFromTXT(input);

        // Test file was successfully loaded
        assertNotNull(loadedTasks);
        assertEquals(1, loadedTasks.size());

        // Test task content
        Task task = loadedTasks.getFirst();
        assertEquals("Homework", task.getName());
        assertEquals("Finish Java Project", task.getDescription());
        assertEquals(LocalDate.of(2026, 5, 22), task.getDeadline());
        assertEquals(1, task.getPriority());
        assertEquals("school", task.getType());
        assertFalse(task.getCompleted());

        // Clean up created files
        Files.deleteIfExists(path);
    }

    /**
     * Tests that the method prints all available TXT files from the data directory before loading tasks.
     *
     * @throws IOException if file creation or deletion fails
     */
    @Test
    void testPrintAvailableFilesWhenLoadTasksFromTXT() throws IOException {
        // Create two TXT files in data directory named "tasks1.txt" and "tasks2.txt"
        IOManager iomanager = new IOManager();
        Task task = new Task("HomeWork", "Finish Java project", LocalDate.of(2026, 5, 22),
                1, "school", false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        Scanner firstSaveInput = new Scanner("tasks1");
        iomanager.saveTasksToTXT(firstSaveInput, tasks);
        Scanner secondSaveInput = new Scanner("tasks2");
        iomanager.saveTasksToTXT(secondSaveInput, tasks);

        // Simulate user input with file name to load
        Scanner loadInput = new Scanner("tasks1");

        // Capture console output for displaying available TXT files to load
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        List<Task> loadedTasks = iomanager.loadTasksFromTXT(loadInput);

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        // Test if method correctly offered existing files to load.
        assertTrue(output.contains("tasks1.txt tasks2.txt"));

        // Clean up created files
        Files.deleteIfExists(Path.of("data/tasks1.txt"));
        Files.deleteIfExists(Path.of("data/tasks2.txt"));
    }

    /**
     * Tests loading tasks from a corrupted TXT file.
     *
     * Verifies that the method correctly handles invalid file content without throwing an exception
     * that would terminate the program.
     *
     * @throws IOException if creating or deleting the test file fails
     */
    @Test
    void testCorruptedFileNotCrashTheProgramWhenLoadingFromTXT() throws IOException {
        IOManager iomanager = new IOManager();
        Files.createDirectories(Path.of("data"));

        // Create file with one task to load later with incorrect deadline formatting
        Path path = Path.of("data/testLoad.txt");
        String testFileContent = """
                -------- TASK LIST --------
                Name: Homework
                Description: Finish Java Project
                Deadline: 45.15.1234
                Priority: 1
                Type: school
                Completed: incompleted
                ----------------------
                """;
        Files.writeString(path, testFileContent);

        // Simulate user input with valid existing file name to load
        Scanner input = new Scanner("testLoad");

        // Capture console output for displaying available TXT files to load
        PrintStream originalOutput = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        List<Task> loadedTasks = iomanager.loadTasksFromTXT(input);

        // Restore original console output
        System.setOut(originalOutput);

        // Convert printed output to String
        String output = outputStream.toString();

        // Test file was not loaded and loaded tasks were set to null
        assertNull(loadedTasks);

        // Test method caught file corruption and outputted error message
        assertTrue(output.contains("\033[1;31mError while loading file.\033[0m"));

        // Clean up created files
        Files.deleteIfExists(path);
    }

    /**
     * Tests saving tasks to a JSON file.
     *
     * Verifies that the file is successfully created and contains correctly serialized task data.
     *
     * @throws IOException  if reading or deleting the test file fails
     */
    @Test
    void testSaveTasksToJSON() throws IOException {
        IOManager iomanager = new IOManager();

        Task task = new Task("HomeWork", "Finish Java project", LocalDate.of(2026, 5, 22),
                1, "school", false);

        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        // Simulate user input with name of file
        Scanner input = new Scanner("testOneTask");
        iomanager.saveTasksToJSON(input, tasks);

        // Test task file was created
        Path path = Path.of("data/testOneTask.json");
        assertTrue(Files.exists(path));

        // Test file content
        String content = Files.readString(path);
        assertTrue(content.contains("\"name\""));
        assertTrue(content.contains("\"HomeWork\""));

        // Clean up created files
        Files.deleteIfExists(path);
    }

    /**
     * Tests loading tasks from a JSON file.
     *
     * Verifies that task data is correctly deserialized into Task objects.
     *
     * @throws IOException if file creation or deletion fails
     */
    @Test
    void testLoadTasksFromJSON() throws IOException {
        // Create one JSON file named "testOneTask.json" for later loading
        IOManager iomanager = new IOManager();
        Task task = new Task("HomeWork", "Finish Java project", LocalDate.of(2026, 5, 22),
                1, "school", false);
        List<Task> tasks = new ArrayList<>();
        tasks.add(task);
        Scanner input = new Scanner("testOneTask");
        iomanager.saveTasksToJSON(input, tasks);

        // Simulate user input with task to load
        Scanner loadInput = new Scanner("testOneTask");
        List<Task> loadedTasks = iomanager.loadTasksFromJSON(loadInput);

        // Test task file was successfully loaded
        assertNotNull(loadedTasks);
        assertEquals(1, loadedTasks.size());

        // Test task content
        Task loadedTask = loadedTasks.getFirst();
        assertEquals("HomeWork", loadedTask.getName());
        assertEquals("Finish Java project", loadedTask.getDescription());
        assertEquals(LocalDate.of(2026, 5, 22), loadedTask.getDeadline());
        assertEquals(1, loadedTask.getPriority());
        assertEquals("school", loadedTask.getType());
        assertFalse(loadedTask.getCompleted());

        // Clean up created files
        Files.deleteIfExists(Path.of("data/testOneTask.json"));
    }
}
