/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Lenovo
 */
public class FileFunctions {
      // THIS READS EVERYTHING AH
    public static List<String> readAllLines(String filePath) {
        List<String> lines = new ArrayList<>();
        File file = new File(filePath);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                
                while ((line = reader.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        lines.add(line);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return lines;
    }

    // THIS UPDATESS
    public static void writeAllLines(String filePath, List<String> lines) {
        File file = new File(filePath);

        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    // ADD THE NEW LINE TO BOTTOM OF TEXT FILE MAN
public static void appendLine(String filePath, String line) {
    File file = new File(filePath);

    try {
        if (!file.exists()) {
            file.createNewFile();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line);     
            writer.newLine();        
        }

    } catch (IOException e) {
        System.err.println("Error appending to file: " + e.getMessage());
    }
}

}
