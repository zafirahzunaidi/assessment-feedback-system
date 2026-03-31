/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

/**
 *
 * @author aqilahazis
 */
public class Assessment {
    
    private String assessID;
    private String classID;
    private String assessType;
    private double fullMark;
    private double weightage;
    
    public Assessment() {}
    
    public Assessment(String assessID, String classID, String assessType,
        double fullMark,double weightage) {
        
        this.assessID = assessID;
        this.classID = classID;
        this.assessType = assessType;
        this.fullMark = fullMark;
        this.weightage = weightage;
    }
    
    // Getters
    public String getAssessID() {
        return assessID;
    }

    public String getClassID() {
        return classID;
    }

    public String getAssessType() {
        return assessType;
    }

    public double getFullMark() {
        return fullMark;
    }

    public double getWeightage() {
        return weightage;
    }
    
    public void setAssessID(String assessID) { this.assessID = assessID; }
    public void setClassID(String classID) { this.classID = classID; }
    public void setAssessType(String assessType) { this.assessType = assessType; }
    public void setFullMark(double fullMark) { this.fullMark = fullMark; }
    public void setWeightage(double weightage) { this.weightage = weightage; }

    @Override
    public String toString() {
        return assessType + " [" + assessID + "]";
    }
    
    // Methods
    public double calculateWeightedMark(double markReceived) {
        if (fullMark <= 0 || weightage <= 0)
            return 0;
        
        return (markReceived / fullMark) * weightage * 100;
    }
    
    public int getTotalStudents() {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("registeredClass.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                // d[1] = classID
                if  (d[1].equalsIgnoreCase(classID)) {
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }


    public int getMarkedCount() {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("result.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) 
                    continue; // skip empty lines

                String[] d = line.split("\\s*,\\s*");

                // Safety check. resultID, stuID, assessID, markreceived
                if (d.length < 4) 
                    continue;

                if (d[2].equalsIgnoreCase(this.assessID)
                    && !d[3].equalsIgnoreCase("NULL")
                    && !d[3].isEmpty()) {

                    count++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getGradingStatus() {
        int total = getTotalStudents();
        int marked = getMarkedCount();

        if (total == 0) return "Not Done";
        if (marked == 0) return "Not Done";
        if (marked < total) return "Partial";
        return "Done";
    }

    public boolean isValidMark(Double mark) {
        if (mark == null) return true; // allow empty
        return mark >= 0 && mark <= fullMark;
    }

    public boolean hasAnyMarks(List<Double> marks) {
        for (Double m : marks) {
            if (m != null) return true;
        }
        return false;
    }
    
}
