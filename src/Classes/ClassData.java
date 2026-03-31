/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

// my packages
//import model.ClassInfo;
//import utilities.FilePath;

// java packages
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ClassData {
    public List<ClassInfo> readAll() {
        List<ClassInfo> classes = new ArrayList<>();
        Path path = Paths.get(FilePath.CLASS_FILE);

        if (!Files.exists(path)) {
            System.err.println("Class file not found: " + path.toAbsolutePath());
            return classes;
        }

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.toLowerCase().startsWith("classid,")) continue;

                // classID, name, type, moduleLectID, classDay, startTime, durationHour, sizeLimit, venue
                String[] p = line.split(", ");
                if (p.length < 9) {
                    System.err.println("Invalid class row (skip): " + line);
                    continue;
                }

                String classID = p[0].trim();
                String name = p[1].trim();
                String type = p[2].trim();
                String moduleLectID = p[3].trim();
                String classDay = p[4].trim();
                String startTime = p[5].trim();
                double durationHour = parseDoubleSafe(p[6].trim(), 0.0);
                int sizeLimit = parseIntSafe(p[7].trim(), 0);
                String venue = p[8].trim();

                ClassInfo ci = new ClassInfo(
                        classID, name, type, moduleLectID,
                        classDay, startTime, durationHour,
                        sizeLimit, venue
                );
                classes.add(ci);
            }
        } catch (IOException e) {
            System.err.println("Error reading class file: " + e.getMessage());
        }

        return classes;
    }

    public ClassInfo findById(String classId) {
        if (classId == null) return null;

        for (ClassInfo ci : readAll()) {
            if (classId.equalsIgnoreCase(ci.getClassID())) {
                return ci;
            }
        }
        return null;
    }

    private int parseIntSafe(String v, int fallback) {
        if (v == null) return fallback;
        v = v.trim();
        if (v.isEmpty() || v.equalsIgnoreCase("NULL")) return fallback;

        try {
            return Integer.parseInt(v);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private double parseDoubleSafe(String v, double fallback) {
        if (v == null) return fallback;
        v = v.trim();
        if (v.isEmpty() || v.equalsIgnoreCase("NULL")) return fallback;

        try {
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }
}
