/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author aqilahazis
 */
public class Module {
    
    private String moduleID;
    private String name;
    private double creditHour;
    private List<Assessment> assessments;
    
    public Module() {}
    
    public Module(String moduleID, String name, double creditHour) {
        this.moduleID = moduleID;
        this.name = name;
        this.creditHour = creditHour;
        this.assessments = new ArrayList<>();
    }
    
    // Getters
    public String getModuleID() {
        return moduleID;
    }

    public String getName() {
        return name;
    }

    public double getCreditHour() {
        return creditHour;
    }
    
    public void addAssessment(Assessment assessment) {
        assessments.add(assessment);
    }
    
    @Override
    public String toString() {
        return moduleID + " - " + name;
    }
    
    public void setModuleID(String moduleID) { this.moduleID = moduleID; }
    public void setName(String name) { this.name = name; }
    public void setCreditHour(double creditHour) { this.creditHour = creditHour; }
    
    public static void addModuletoCmb(JComboBox<String> cmbbox){
        cmbbox.removeAllItems();
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("Module.txt");
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                //System.out.println(row);
                String[] eachline = row.split(", ");
                String modulename = eachline[0] + " - " + eachline[1];
                cmbbox.addItem(modulename);
            }
    }
    
    public static String getModulename(String moduleID){
        String modulename = "";
        List<String> data = AdminTask.readtxt("module.txt");   
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                String[] eachline = row.split(", ");
                if(eachline[0].equals(moduleID)){
                    modulename = eachline[1];
                    break;
                }
            }
        return modulename;
    }
    
public static List<Module> loadModules(String filePath) {

    List<Module> modules = new ArrayList<>();
    List<String> lines = FileFunctions.readAllLines(filePath);

    for (String line : lines) {

        if (line.toLowerCase().startsWith("moduleid")) {
            continue; // skip header
        }

        Module module = Module.fromFileString(line);

        if (module != null) { // only add valid modules
            modules.add(module);
        }
    }

    return modules;
}

public String toFileString() {
    return moduleID + ", " + name + ", " + creditHour;
}

public static Module fromFileString(String line) {
    String[] parts = line.split(", *"); // split by comma and optional space

    if (parts.length != 3) {
        System.err.println("Skipping invalid module line: " + line);
        return null;
    }

    String id = parts[0].trim();
    String name = parts[1].trim();
    double credits;

    try {
        credits = Double.parseDouble(parts[2].trim());
    } catch (NumberFormatException e) {
        System.err.println("Invalid credit hour in line: " + line);
        return null;
    }

    return new Module(id, name, credits);
}

   
    public static void saveModules(String filePath, List<Module> modules) {

        List<String> lines = new ArrayList<>();

        lines.add("moduleID, name, creditHour ");

        for (Module m : modules) {
            lines.add(m.toFileString());
        }

        FileFunctions.writeAllLines(filePath, lines);
    }

 
    public static void addModule(String filePath, Module module) {
       FileFunctions.appendLine(filePath, module.toFileString());
   }



    
}
