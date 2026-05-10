import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IOManager{

    public void saveTasksToTXT(Scanner input, List<Task> tasks){
        System.out.println("Please enter name under which this file will be saved or press Enter to select automatic name:");
        Path path;

        while(true){
            String fileName = input.nextLine().trim();
            if(fileName.isEmpty()){
                System.out.println("Generating automatic name...");
                path = generateFilePath();
                break;
            }
            else{
                String filePath = "data/" + fileName + ".txt";
                path = Path.of(filePath);
                if(Files.exists(path)){
                    System.out.println("This filename already exists. Please enter different name or press Enter to select automatic name:");
                    continue;
                }
                break;
            }
        }

        try{
            Files.createDirectories(Path.of("data"));

            try(BufferedWriter writer = Files.newBufferedWriter(path)){
                writer.write("-------- TASK LIST --------\n");

                for(Task task : tasks){
                    writer.write(task.toString());
                    writer.write("----------------------\n");
                }
            }

            System.out.print("Tasks successfully saved to: ");
            System.out.println(path.toAbsolutePath());
        }
        catch(IOException e){
            System.out.println("Error while saving file.");
        }
    }

    private Path generateFilePath(){
        int counter = 1;
        Path path = Path.of("data/tasks" + counter + ".txt");

        while(Files.exists(path)){
            counter++;
            path = Path.of("data/tasks" + counter + ".txt");
        }

        return path;
    }

    public List<Task> loadTasksFromTXT(Scanner input){
        System.out.println("Enter name of file you would like to load. Available text files in directory 'data':");
        boolean proceed;

        proceed = printAvailableTXTFiles();
        if(!proceed){
            return null;
        }

        String fileName = input.nextLine().trim();
        Path path = Path.of("data/" + fileName);

        while(!Files.exists(path)){
            System.out.println("File does not exist, please enter valid file name:");
            fileName = input.nextLine().trim();
            path = Path.of("data/" + fileName);
        }

        return loadTasksInternal(path);
    }

    private boolean printAvailableTXTFiles(){
        Path dataDirectory = Path.of("data");

        if(!Files.exists(dataDirectory)){
            System.out.println("Directory 'data' does not exist, there are no available files to load.");
            return false;
        }

        try{
            Files.list(dataDirectory)
                    .filter(path -> path.toString().endsWith(".txt"))
                    .forEach(path -> System.out.print(path.getFileName() + " "));
            System.out.println();
        }
        catch(IOException e){
            System.out.println("Error while reading the 'data' directory.");
        }

        return true;
    }

    private List<Task> loadTasksInternal(Path path){
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

            System.out.println("Tasks successfully loaded!");
        }
        catch(IOException e){
            System.out.println("Error while loading file.");
        }

        return loadedTasks;
    }

    private String extractValueFromLine(String line){
        return line.substring(line.indexOf(":") + 2);
    }
}