/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

public class ClassInfo {
    private String classID;
    private String name;
    private String type;         // Lecture/Lab/Tutorial
    private String moduleLectID; // MLxxxx
    private String classDay;     // Monday, Tuesday...
    private String startTime;    // 0915... (string)
    private double durationHour;
    private int sizeLimit;
    private String venue;        // Physical/Online
    
    public ClassInfo() {}

    public ClassInfo(String classID, String name, String type, String moduleLectID,
                        String classDay, String startTime, double durationHour,
                        int sizeLimit, String venue) {
        this.classID = classID;
        this.name = name;
        this.type = type;
        this.moduleLectID = moduleLectID;
        this.classDay = classDay;
        this.startTime = startTime;
        this.durationHour = durationHour;
        this.sizeLimit = sizeLimit;
        this.venue = venue;
    }
    
    public String getClassID() { return classID; }
    public void setClassID(String classID) { this.classID = classID; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getModuleLectID() { return moduleLectID; }
    public void setModuleLectID(String moduleLectID) { this.moduleLectID = moduleLectID; }

    public String getClassDay() { return classDay; }
    public void setClassDay(String classDay) { this.classDay = classDay; }

    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }

    public double getDurationHour() { return durationHour; }
    public void setDurationHour(double durationHour) { this.durationHour = durationHour; }

    public int getSizeLimit() { return sizeLimit; }
    public void setSizeLimit(int sizeLimit) { this.sizeLimit = sizeLimit; }

    public String getVenue() { return venue; }
    public void setVenue(String venue) { this.venue = venue; }

    @Override
    public String toString() {
        return classID + " - " + name + " (" + type + ")";
    }
}
