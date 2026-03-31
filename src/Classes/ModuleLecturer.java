/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.ArrayList;
import java.util.List;

public class ModuleLecturer {
    
    private String modlectID;
    private String lecturerID;
    private String moduleID;

    public ModuleLecturer(String modlectID, String lecturerID, String moduleID) {
        this.modlectID = modlectID;
        this.lecturerID = lecturerID;
        this.moduleID = moduleID;
    }

    public String getModlectID() { return modlectID; }
    public String getLecturerID() { return lecturerID; }
    public String getModuleID() { return moduleID; }

    
    public static String searchMLID(String moduleID, String lectID){
        String mlID = "";
        
        List<String> data = AdminTask.readtxt("moduleLecturer.txt");
        for(int i = 1; i < data.size(); i++){
            String row = data.get(i);
            String[] eachline = row.split(", ");
            //System.out.println(eachline[0]);
            if (eachline[2].equals(moduleID) && eachline[1].equals(lectID)){
                //eachline[0] = mlID; 
                mlID = eachline[0];
            }
        }
        
        return mlID;
    }
    
        public String toFileString() { return modlectID + ", " + lecturerID + ", " + moduleID; }


    public static List<ModuleLecturer> loadModuleLecturers(String filePath) {
        List<ModuleLecturer> list = new ArrayList<>();
        List<String> lines = FileFunctions.readAllLines(filePath);

        for (String line : lines) {
            if (line.toLowerCase().startsWith("modlectid")) continue; 
            String[] parts = line.split(", ");
            if (parts.length == 3) {
                list.add(new ModuleLecturer(parts[0].trim(), parts[1].trim(), parts[2].trim()));
            }
        }
        return list;
    }

    public static void saveModuleLecturers(String filePath, List<ModuleLecturer> list) {
        List<String> lines = new ArrayList<>();
        lines.add("modlectID, lecturerID, moduleID"); 
        for (ModuleLecturer ml : list) {
            lines.add(ml.toFileString());
        }
        FileFunctions.writeAllLines(filePath, lines);
    }

    public static void addModuleLecturer(String filePath, ModuleLecturer ml) {
        FileFunctions.appendLine(filePath, ml.toFileString());
    }
}
