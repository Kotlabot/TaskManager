import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class IOManager {

    public void saveTasksToTXT(Scanner input, List<Task> tasks) {
        System.out.println("Please enter name under which this file will be saved or press Enter to select automatic name:");
        Path path;

        while (true) {
            String fileName = input.nextLine().trim();
            if (fileName.isEmpty()) {
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

        try {

            Files.createDirectories(Path.of("data"));

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {

                for (Task task : tasks) {
                    writer.write("-------- TASK LIST --------");
                    writer.write(task.toString());
                    writer.write("\n----------------------\n");
                }
            }

            System.out.print("Tasks successfully saved to: ");
            System.out.println(path.toAbsolutePath());
        }

        catch (IOException e) {
            System.out.println("Error while saving file.");
        }
    }

    private Path generateFilePath() {
        int counter = 1;
        Path path = Path.of("data/tasks" + counter + ".txt");

        while(Files.exists(path)){
            counter++;
            path = Path.of("data/tasks" + counter + ".txt");
        }

        return path;
    }
}