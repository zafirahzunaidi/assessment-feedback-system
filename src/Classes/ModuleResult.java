/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aqilahazis
 */
public class ModuleResult extends Module {
    
    private String moduleResultID; // MRxxxxx
    private double lectureMark;
    private double tutorialMark;
    private double finalMark;
    private String overallGrade;
    
    private String studentID;
    private String moduleID;
    
//    public ModuleResult(String moduleResultID, double lectureMark,
//                  double tutorialMark, double finalMark, String overallGrade) {
//        this.moduleResultID = moduleResultID;
//        this.lectureMark = lectureMark;
//        this.tutorialMark = tutorialMark;
//        this.finalMark = finalMark;
//        this.overallGrade = overallGrade;
//    }
    
//    public ModuleResult() {}

    public ModuleResult(String moduleResultID, String studentID, String moduleID,
                        double lectureMark, double tutoMark, double finalMark,
                        String overallGrade) {
        this.moduleResultID = moduleResultID;
        this.studentID = studentID;
        this.moduleID = moduleID;
        this.lectureMark = lectureMark;
        this.tutorialMark = tutoMark;
        this.finalMark = finalMark;
        this.overallGrade = overallGrade;
    }
    
    public ModuleResult() {}
    
    // Getters
    public String getModuleResultID() {
        return moduleResultID;
    }

    public double getLectureMark() {
        return lectureMark;
    }

    public double getTutorialMark() {
        return tutorialMark;
    }
    
    public double getFinalMark() {
        return finalMark;
    }
    
    public String getOverallGrade() {
        return overallGrade;
    }
    
    
    public void setModuleResultID(String moduleResultID) { this.moduleResultID = moduleResultID; }

    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getModuleID() { return moduleID; }
    public void setModuleID(String moduleID) { this.moduleID = moduleID; }
    public void setLectureMark(Double lectureMark) { this.lectureMark = lectureMark; }
    public void setTutoMark(Double tutoMark) { this.tutorialMark = tutoMark; }
    public void setFinalMark(Double finalMark) { this.finalMark = finalMark; }
    public void setOverallGrade(String overallGrade) { this.overallGrade = overallGrade; }

    @Override
    public String toString() {
        return moduleID + " - " + overallGrade;
    }
    
    // Methods
    public static String assignGrade(double mark) {
        mark = Math.max(0, Math.min(100, mark)); // safety

        try (BufferedReader br =
                new BufferedReader(new FileReader("gradingSystem.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split("\\s*,\\s*");
                if (d.length < 5) continue;

                // Skip NULL
                if (d[2].equalsIgnoreCase("NULL") || d[3].equalsIgnoreCase("NULL"))
                    continue;

                String grade = d[0];
                double min = Double.parseDouble(d[2]);
                double max = Double.parseDouble(d[3]);

                if (mark >= min && mark <= max) {
                    return grade;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "N/A";
    }

    public void computeFinalResult() {
        this.finalMark = (lectureMark + tutorialMark)/2;
        this.overallGrade = assignGrade(finalMark);
    }
    
    
    
    public String toFileString() {
    return moduleResultID + ", " +
           studentID + ", " +
           moduleID + ", " +
           lectureMark + ", " +
           tutorialMark + ", " +
           finalMark + ", " +
           overallGrade;
}

    public static ModuleResult fromFileString(String line) {

    String[] parts = line.split(", ");

    return new ModuleResult(
            
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            Double.parseDouble(parts[3].trim()),
            Double.parseDouble(parts[4].trim()),
            Double.parseDouble(parts[5].trim()),
            parts[6].trim()
    );
}    
    
    public static List<ModuleResult> loadModuleResult(String filePath) {

    List<ModuleResult> moduleresults = new ArrayList<>();
    List<String> lines = FileFunctions.readAllLines(filePath);

    for (String line : lines) {

        if (line.toLowerCase().startsWith("moduleresultid")) {
            continue;
        }

        try {
            moduleresults.add(ModuleResult.fromFileString(line));
        } catch (Exception e) {
            System.err.println("Skipping invalid leader line: " + line);
        }
    }

    return moduleresults;
}
    
}
