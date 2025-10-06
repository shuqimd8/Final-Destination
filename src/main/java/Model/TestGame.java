package Model;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestGame {
    public static void main(String[] args) {
        File file = new File("src/main/java/TxtFiles/Buckets.txt");
        List<String> fileLines = new ArrayList<>();
        //get file path
        String filePath = file.getPath();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileLines.add(line);
                // Process each line here
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        System.out.println(fileLines);

    }
}
