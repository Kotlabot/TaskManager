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

public class IOManager{

    public void saveTasksToTXT(Scanner input, List<Task> tasks){
        System.out.println("Please enter name under which this file will be saved or press Enter to select automatic name:");
        String extension = ".txt";
        Path path = getCorrectFilePathToSave(input, extension);

        try{
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

    private Path getCorrectFilePathToSave(Scanner input, String extension){
        Path path;

        while(true){
            String fileName = input.nextLine().trim();
            if(fileName.isEmpty()){
                System.out.println("Generating automatic name...");
                path = generateFilePath(extension);
                break;
            }
            else{
                fileName = addExtension(fileName, extension);
                path = Path.of("data/" + fileName);

                if(Files.exists(path)){
                    System.out.println("\033[1;31mThis filename already exists.\033[0m Please enter different name or press Enter to select automatic name:");
                    continue;
                }
                break;
            }
        }

        return path;
    }

    private Path generateFilePath(String extension){
        int counter = 1;
        Path path = Path.of("data/tasks" + counter + extension);

        while(Files.exists(path)){
            counter++;
            path = Path.of("data/tasks" + counter + extension);
        }

        return path;
    }

    public List<Task> loadTasksFromTXT(Scanner input){
        System.out.println("Enter name of file you would like to load. Available text files in directory 'data':");
        String extension = ".txt";
        Path path = getCorrectFilePathToLoad(input, extension);
        if(path != null){
            return loadTasksFromTXTInternal(path);
        }
        else{
            return null;
        }
    }

    private Path getCorrectFilePathToLoad(Scanner input, String extension){
        boolean proceed;

        proceed = printAvailableFiles(extension);
        if(!proceed){
            return null;
        }

        String fileName = input.nextLine().trim();
        fileName = addExtension(fileName, extension);

        Path path = Path.of("data/" + fileName);

        while(!Files.exists(path)){
            System.out.println("\033[1;31mFile does not exist\033[0m, please enter valid file name:");
            fileName = input.nextLine().trim();
            fileName = addExtension(fileName, extension);
            path = Path.of("data/" + fileName);
        }

        return path;
    }

    private String addExtension(String fileName, String extension){
        if(!fileName.endsWith(extension)){
            fileName += extension;
        }
        return fileName;
    }

    private boolean printAvailableFiles(String extension){
        Path dataDirectory = Path.of("data");

        if(!Files.exists(dataDirectory)){
            System.out.println("\033[1;31mDirectory 'data' does not exist, there are no available files to load.\033[0m");
            return false;
        }

        try{
            Files.list(dataDirectory)
                    .filter(path -> path.toString().endsWith(extension))
                    .forEach(path -> System.out.print(path.getFileName() + " "));
            System.out.println();
        }
        catch(IOException e){
            System.out.println("\033[1;31mError while reading the 'data' directory.\033[0m");
        }

        return true;
    }

    private List<Task> loadTasksFromTXTInternal(Path path){
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

            while((line = reader.readLine()) != null){
                if(line.equals("-------- TASK LIST --------")){
                    continue;
                }

                name = extractValueFromLine(line);

                line = reader.readLine();
                String descriptionLine = extractValueFromLine(line);
                if(descriptionLine.equals("none")){
                    description = null;
                }
                else{
                    description = descriptionLine;
                }

                line = reader.readLine();
                String deadlineLine = extractValueFromLine(line);
                if(deadlineLine.equals("none")){
                    deadline = null;
                }
                else{
                    deadline = LocalDate.parse(deadlineLine, formatter);
                }

                line = reader.readLine();
                priority = Integer.parseInt(extractValueFromLine(line));

                line = reader.readLine();
                String typeLine = extractValueFromLine(line);
                if(typeLine.equals("none")){
                    type = null;
                }
                else{
                    type = typeLine;
                }

                line = reader.readLine();
                String completedLine = extractValueFromLine(line);
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

    private String extractValueFromLine(String line){
        return line.substring(line.indexOf(":") + 2);
    }

    public void saveTasksToJSON(Scanner input, List<Task> tasks){
        System.out.println("Please enter name under which this file will be saved or press Enter to select automatic name:");
        String extension = ".json";
        Path path = getCorrectFilePathToSave(input, extension);

        try{
            Files.createDirectories(Path.of("data"));
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.writeValue(path.toFile(), tasks);
            System.out.print("\033[1;32mTasks successfully saved to: \033[0m");
            System.out.println(path.toAbsolutePath());
        }
        catch(IOException e){
            System.out.println("\033[1;31mError while saving file.\033[0m");
        }
    }

    public List<Task> loadTasksFromJSON(Scanner input){
        System.out.println("Enter name of file you would like to load. Available JSON files in directory 'data':");
        String extension = ".json";
        Path path = getCorrectFilePathToLoad(input, extension);

        if(path != null){
            return loadTasksFromJSONInternal(path);
        }
        else{
            return null;
        }
    }

    private List<Task> loadTasksFromJSONInternal(Path path){
        try{
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
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