package taskmanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Handles saving tasks to and loading tasks from TXT files and JSON files.
 * In addition, provides automatic file name generation.
 */
public class IOManager{

    /**
     * Saves tasks to a TXT file.
     *
     * @param input scanner used for console input
     * @param tasks list of tasks to save
     */
    public void saveTasksToTXT(Scanner input, List<Task> tasks){
        System.out.println("Please enter name under which this file will be saved or press Enter to select automatic name:");
        String extension = ".txt";
        // Get valid file path
        Path path = getCorrectFilePathToSave(input, extension);

        try{
            // Create data directory if it does not exist
            Files.createDirectories(Path.of("data"));

            try(BufferedWriter writer = Files.newBufferedWriter(path)){
                writer.write("-------- TASK LIST --------\n");

                for(Task task : tasks){
                    writer.write(task.toString());
                    writer.write("----------------------\n");
                }
            }

            System.out.print("\033[1;32mTasks successfully saved to: \033[0m");
            System.out.println(path.toAbsolutePath());
        }
        catch(IOException e){
            System.out.println("\033[1;31mError while saving file.\033[0m");
        }
    }

    /**
     * Creates valid file path for saving.
     *
     * Allows user to select file name, while checking for duplicate names is data directory,
     * or automatically generates unique file name if no name is entered.
     *
     * @param input scanner used for console input
     * @param extension required file extension (TXT/JSON)
     * @return valid file path for saving
     */
    private Path getCorrectFilePathToSave(Scanner input, String extension){
        Path path;

        while(true){
            String fileName = input.nextLine().trim();
            if(fileName.isEmpty()){
                System.out.println("Generating automatic name...");
                // Generate automatic file path (name)
                path = generateFilePath(extension);
                break;
            }
            else{
                // Add correct extension to filename
                fileName = addExtension(fileName, extension);
                path = Path.of("data/" + fileName);

                // Check for duplicate file name and prevent overwriting existing files
                if(Files.exists(path)){
                    System.out.println("\033[1;31mThis filename already exists.\033[0m Please enter different name or press Enter to select automatic name:");
                    continue;
                }
                break;
            }
        }

        return path;
    }

    /**
     * Generates automatic unique file path.
     *
     * Generates names are in format: tasks1, tasks2...
     *
     * @param extension required file extension
     * @return generated unique file path
     */
    private Path generateFilePath(String extension){
        int counter = 1;
        Path path = Path.of("data/tasks" + counter + extension);

        // Find first available file name
        while(Files.exists(path)){
            counter++;
            path = Path.of("data/tasks" + counter + extension);
        }

        return path;
    }

    /**
     * Loads tasks from TXT file.
     *
     * @param input scanner used for console input
     * @return list of loaded tasks or null if loading fails
     */
    public List<Task> loadTasksFromTXT(Scanner input){
        System.out.println("Enter name of file you would like to load. Available text files in directory 'data':");
        String extension = ".txt";
        // Get valid file path or null if no files are available
        Path path = getCorrectFilePathToLoad(input, extension);
        if(path != null){
            return loadTasksFromTXTInternal(path);
        }
        else{
            return null;
        }
    }

    /**
     * Creates valid file path for loading.
     *
     * Through method "printAvailableFiles" prints available files and validates user input.
     *
     * @param input scanner used for console input
     * @param extension required file extension
     * @return valid file path or null if no files exist
     */
    private Path getCorrectFilePathToLoad(Scanner input, String extension){
        boolean proceed;

        // Print available files with matching extension
        proceed = printAvailableFiles(extension);
        // Return null if no files are available
        if(!proceed){
            return null;
        }

        String fileName = input.nextLine().trim();
        fileName = addExtension(fileName, extension);
        Path path = Path.of("data/" + fileName);

        // Iterate until existing file is entered
        while(!Files.exists(path)){
            System.out.println("\033[1;31mFile does not exist\033[0m, please enter valid file name:");
            fileName = input.nextLine().trim();
            fileName = addExtension(fileName, extension);
            path = Path.of("data/" + fileName);
        }

        return path;
    }

    /**
     * Adds required extension to file name if missing.
     *
     * @param fileName original file name
     * @param extension required extension
     * @return file name with extension
     */
    private String addExtension(String fileName, String extension){
        if(!fileName.endsWith(extension)){
            fileName += extension;
        }
        return fileName;
    }

    /**
     * Prints available files in directory data with specified extension.
     *
     * @param extension file extension to search for
     * @return true if directory data and available files exist (loading can proceed), otherwise false
     */
    private boolean printAvailableFiles(String extension){
        Path dataDirectory = Path.of("data");

        if(!Files.exists(dataDirectory)){
            System.out.println("\033[1;31mDirectory 'data' does not exist, there are no available files to load.\033[0m");
            return false;
        }

        try{
            // Store files with matching extension into list
            List<Path> matchingFiles = Files.list(dataDirectory)
                    .filter(path -> path.toString().endsWith(extension))
                    .toList();

            // If no matching files are found, return to main menu
            if(matchingFiles.isEmpty()){
                System.out.println("\033[1;31mNo " + extension + " files available to load.\033[0m");
                return false;
            }

            // Print available files from data directory with matching extension
            matchingFiles.forEach(path -> System.out.print(path.getFileName() + " "));
            System.out.println();
        }
        catch(IOException e){
            System.out.println("\033[1;31mError while reading the 'data' directory.\033[0m");
        }

        return true;
    }

    /**
     * Loads tasks from valid TXT file.
     *
     * Reads task parameters line by line and creates Task objects from loaded data.
     *
     * @param path path to TXT file
     * @return list of loaded tasks or nul if loading fails
     */
    private List<Task> loadTasksFromTXTInternal(Path path){
        // Store loaded tasks into list
        List<Task> loadedTasks = new ArrayList<>();

        try(BufferedReader reader = Files.newBufferedReader(path)){
            String line;
            String name;
            String description;
            LocalDate deadline;
            int priority;
            String type;
            boolean completed;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            // One iteration of while loop loads one task
            while((line = reader.readLine()) != null){
                // Skip header line
                if(line.equals("-------- TASK LIST --------")){
                    continue;
                }

                // Get task name from file
                name = extractValueFromLine(line);

                line = reader.readLine();
                // Get task description from file
                String descriptionLine = extractValueFromLine(line);
                // Convert "none" value back to null (internal representation)
                if(descriptionLine.equals("none")){
                    description = null;
                }
                else{
                    description = descriptionLine;
                }

                line = reader.readLine();
                // Get task deadline from file
                String deadlineLine = extractValueFromLine(line);
                // Convert "none" value back to null
                if(deadlineLine.equals("none")){
                    deadline = null;
                }
                // Parse deadline string into LocalDate object
                else{
                    deadline = LocalDate.parse(deadlineLine, formatter);
                }

                line = reader.readLine();
                // Get task priority from file and parse into integer
                priority = Integer.parseInt(extractValueFromLine(line));

                line = reader.readLine();
                // Get task type from file
                String typeLine = extractValueFromLine(line);
                // Convert "none" value back to null
                if(typeLine.equals("none")){
                    type = null;
                }
                else{
                    type = typeLine;
                }

                line = reader.readLine();
                // Get completion state from file
                String completedLine = extractValueFromLine(line);
                // Assign correct completion state
                if(completedLine.equals("completed")){
                    completed = true;
                }
                else{
                    completed = false;
                }

                // Skip line dividing two tasks (----------------------)
                reader.readLine();
                Task task = new Task(name, description, deadline, priority, type, completed);
                loadedTasks.add(task);
            }

            System.out.println("\033[1;32mTasks successfully loaded!\033[0m");
        }
        catch(IOException | DateTimeParseException e){
            System.out.println("\033[1;31mError while loading file.\033[0m");
            return null;
        }

        return loadedTasks;
    }

    /**
     * Extracts values from formatted task lines.
     *
     * Example: from formatted line "Name: Task1" is extracted the actual name "Task1".
     *
     * @param line formatted line
     * @return extracted value
     */
    private String extractValueFromLine(String line){
        // Extract value after ": "
        return line.substring(line.indexOf(":") + 2);
    }

    /**
     * Saves tasks to JSON file using Jackson library for serialization.
     *
     * @param input scanner used for console input
     * @param tasks list of tasks to save
     */
    public void saveTasksToJSON(Scanner input, List<Task> tasks){
        System.out.println("Please enter name under which this file will be saved or press Enter to select automatic name:");
        String extension = ".json";
        // Get valid file path (defined by user or automatic generated)
        Path path = getCorrectFilePathToSave(input, extension);

        try{
            // Create data directory if it does not exist
            Files.createDirectories(Path.of("data"));

            ObjectMapper mapper = new ObjectMapper();
            // Register module for LocalDate (deadline) serialization
            mapper.registerModule(new JavaTimeModule());
            // Enable formatted JSON output
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(path.toFile(), tasks);

            System.out.print("\033[1;32mTasks successfully saved to: \033[0m");
            System.out.println(path.toAbsolutePath());
        }
        catch(IOException e){
            System.out.println("\033[1;31mError while saving file.\033[0m");
        }
    }

    /**
     * Loads tasks from JSON file.
     *
     * @param input scanner used for console input
     * @return list of loaded tasks or null if loading fails
     */
    public List<Task> loadTasksFromJSON(Scanner input){
        System.out.println("Enter name of file you would like to load. Available JSON files in directory 'data':");
        String extension = ".json";
        // Get valid file path or null if no files are available
        Path path = getCorrectFilePathToLoad(input, extension);

        if(path != null){
            return loadTasksFromJSONInternal(path);
        }
        else{
            return null;
        }
    }

    /**
     * Loads tasks from specified JSON file using Jackson library for deserialization.
     *
     * @param path path to JSON file
     * @return list of loaded tasks or null if loading fails.
     */
    private List<Task> loadTasksFromJSONInternal(Path path){
        try{
            ObjectMapper mapper = new ObjectMapper();
            // Register module for LocalDate (deadline) deserialization
            mapper.registerModule(new JavaTimeModule());
            // Deserialize JSON array into list of Task object
            List<Task> loadedTasks = mapper.readValue(path.toFile(), new TypeReference<List<Task>>() {});

            System.out.println("\033[1;32mTasks successfully loaded!\033[0m");
            return loadedTasks;
        }
        catch(IOException e){
            System.out.println("\033[1;31mError while loading file.\033[0m");
            return null;
        }
    }
}