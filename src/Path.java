// src/Path.java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Path {
    private String inputFilePath;

    public Path(String inputFilePath) {
        this.inputFilePath = inputFilePath;
    }

    public String readAndTransform(int StartX, int StartY) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;

            content.append("START").append("\n");
            line = br.readLine();
            char kierunek = line.charAt(0);
            String transformed = String.format("FORWARD %d", Integer.parseInt(line.substring(1)));
            content.append(transformed).append("\n");

            while ((line = br.readLine()) != null) {
                // Zmiana na odpowiednią formatkę
                char direction = line.charAt(0);
                int value = Integer.parseInt(line.substring(1)) + 1; // Zwiększamy o 1

                switch (direction) {
                    case 'N':
                        if (kierunek == 'E') {content.append("TURNLEFT").append("\n");}
                        if (kierunek == 'W') {content.append("TURNRIGHT").append("\n");}
                        break;
                    case 'E':
                        if (kierunek == 'S') {content.append("TURNLEFT").append("\n");}
                        if (kierunek == 'N') {content.append("TURNRIGHT").append("\n");}
                        break;
                    case 'S':
                        if (kierunek == 'W') {content.append("TURNLEFT").append("\n");}
                        if (kierunek == 'E') {content.append("TURNRIGHT").append("\n");}
                        break;
                    case 'W':
                        if (kierunek == 'N') {content.append("TURNLEFT").append("\n");}
                        if (kierunek == 'S') {content.append("TURNRIGHT").append("\n");}
                        break;
                }
                kierunek = direction;

                transformed = String.format("FORWARD %d", value);
                content.append(transformed).append("\n");
            }
            content.append("STOP").append("\n");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return content.toString();
    }

    public void writeToFile(String outputFilePath, String content) {
        try (FileWriter fw = new FileWriter(outputFilePath)) {
            fw.write(content);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }
}