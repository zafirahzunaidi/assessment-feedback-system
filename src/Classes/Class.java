/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

//import static assignment.Admin_8AssignClass.tblSchedule;
//import assignment.Admin_9ClassAssignment;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
//import tableActionBtn.CustomCellRenderer;

/**
 *
 * @author aqilahazis
 */
public class Class extends Module {
    
    private String classID;
    private String name;
    private String type;
    private String classDay;
    private String startTime;
    private double durationHour;
    private int sizeLimit;
    private String venue;
    
    private static String moduleID;
    
    public Class(){
        
    }
    
    public Class(String classType){
        this.type = classType;
    }
    
    public Class(String classID, String name, String type,
                 String classDay, String startTime){

        this.classID = classID;
        this.name = name;
        this.type = type;
        this.classDay = classDay;
        this.startTime = startTime;
    }
    
    public Class(String classID, String name, String type, String classDay,
            String startTime, double durationHour, int sizeLimit, String venue) {

        this.classID = classID;
        this.name = name;
        this.type = type;
        this.classDay = classDay;
        this.startTime = startTime;
        this.durationHour = durationHour;
        this.sizeLimit = sizeLimit;
        this.venue = venue;
    }
    
    // Getters
    public String getClassID(){
        return classID;
    }
    
    public String getName(){
        return name;
    }
    
    public String getType(){
        return type;
    }
 
    public String getClassDay(){
        return classDay;
    }
        
    public String getStartTime(){
        return startTime;
    }
            
    public double getDurationHour(){
        return durationHour;
    }
    
    public int getSizeLimit(){
        return sizeLimit;
    }
        
    public String getVenue(){
        return venue;
    }
    
    public void setType(String classType){  
        this.type = classType;
    }
    
    public static String getmoduleID()
    {
        return moduleID;
    }
    
    public void setmoduleID(String moduleID){  
        this.moduleID = moduleID;
    }
    
    public static void initializeTblsched(JTable table){
        LocalTime time = LocalTime.of(8, 30);
        for (int i = 0; i < table.getRowCount(); i++) {
            table.setValueAt(time, i, 0);
            time = time.plusMinutes(15);
        }
    }
    
    public static void classSchedule(String mlID, JTable table){
        List<List<Integer>> information = new ArrayList<>();
        String day = "", startTime = "", duration = "";
        int columnnumber = -1, rownumber = -1;
        
        List<String> data = AdminTask.readtxt("class.txt");
        for(int i = 1; i < data.size(); i++){
            String row = data.get(i);
            String[] eachline = row.split(", ");
            if (eachline[3].equals(mlID)){
                day = eachline[4];
                startTime = eachline[5];
                duration = eachline[6];
                
                //System.out.println(day);
                
                // Iterate through the columns
                for (int j = 0; j < table.getColumnCount(); j++) {
                    // Get header name at current index
                    String headerName = table.getColumnName(j);
                    if (headerName.equals(day)){
                    columnnumber = j;
                    }
                }
            
                for (int j = 0; j < table.getRowCount(); j++) {
                    // Get header name at current index
                    String rowdata = table.getValueAt(j, 0).toString();
                    if (rowdata.equals(startTime)){
                        rownumber = j;
                    }
                }
                
                double coloredRow = Double.parseDouble(duration);
                coloredRow = (coloredRow * 60) / 15;
                
                information.add(Arrays.asList(columnnumber, rownumber, (int) coloredRow));             
            }
        }  
        
        information.sort(Comparator.comparingInt(list -> list.get(0)));
       
        //System.out.println(information);
        int colnum1 = -1;
        int durationbefore = -1;
        int rowbefore = -1;
        List<Integer> rowsToColor = new ArrayList<>();
        List<Integer> affectedcolumns = new ArrayList<>();
        for (List<Integer> eachlist : information) {
            List<Integer> rowsToColor2 = new ArrayList<>();
            int colnum2 = eachlist.get(0);
            
            //System.out.println(colnum1);
            //System.out.println(colnum2);
            //System.out.println(colnum1 == colnum2);
            
            if(information.size() == 1){
                affectedcolumns.add(colnum2);
                for (int i = eachlist.get(1); i <=  eachlist.get(1) + eachlist.get(2); i++){
                    rowsToColor.add(i);
                }
                
                CustomCellRenderer renderer = new CustomCellRenderer(rowsToColor, colnum2);
                table.getColumnModel().getColumn(colnum2).setCellRenderer(renderer);
                
                break;
            }
            
            else if (colnum1 == colnum2){
                affectedcolumns.add(colnum2);
                
                for (int i = eachlist.get(1); i <=  eachlist.get(1) + eachlist.get(2); i++){
                    rowsToColor.add(i);
                }
                
                for (int i = rowbefore; i <=  rowbefore + durationbefore; i++){
                    rowsToColor.add(i);
                }
                
                CustomCellRenderer renderer = new CustomCellRenderer(rowsToColor, colnum2);
                table.getColumnModel().getColumn(colnum2).setCellRenderer(renderer);
            }
            else {
                affectedcolumns.add(colnum2);
                for (int i = eachlist.get(1); i <=  eachlist.get(1) + eachlist.get(2); i++){
                    rowsToColor2.add(i);
                }
                
                CustomCellRenderer renderer = new CustomCellRenderer(rowsToColor2, colnum2);
                table.getColumnModel().getColumn(colnum2).setCellRenderer(renderer);
            }
            
            colnum1 = colnum2;
            rowbefore = eachlist.get(1);
            durationbefore = eachlist.get(2);
            //System.out.println(rowsToColor);
            //System.out.println(rowsToColor2);
            
            
        }
        
        List<Integer> help = new ArrayList<>();
        //System.out.println("aff: " + affectedcolumns);
        for (int i = 0; i < table.getColumnCount(); i++) {
            if(!affectedcolumns.contains(i)){
                help.add(-1);
                CustomCellRenderer renderer = new CustomCellRenderer(help, i);
                table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
        }
        table.repaint();

    }
    
    public static void classAssignmentInIt(String day, String startTime, String ID, 
            String lectID, JTextField txtlect, JTextField txtday, JTextField txtstarttime){
        //System.out.print(lectID);
        txtlect.setText(lectID);
        txtday.setText(day);
        txtstarttime.setText(startTime);
        
        moduleID = ID;
    }
    
    public void generateClassID(JTextField txtid, JTextField txtname){
        String classType = this.type;
        String prefix;
        String lastnumber = "0";
        String classID = "00";
        String modulename;
        String moduleID = this.moduleID;
        
        moduleID = AdminTask.titleCase(moduleID);
        
        switch (classType) {
            case "Lecture":
                prefix = "LT";
                break;
            case "Lab":
                prefix = "LB";
                break;
            default:
                prefix = "TT";
                break;
        }
        
        List<String> data = AdminTask.readtxt("class.txt");
        for(int i = 1; i < data.size(); i++){
            String row = data.get(i);
            String[] eachline = row.split(", ");
            classID = eachline[0];
            String prefixeachline = classID.substring(0, classID.indexOf('-'));
            modulename = classID.substring(6);
            if(prefixeachline.equals(prefix) && modulename.equals(moduleID)){
                lastnumber = classID.substring(3, 5);
            }
        }
        
        int lastno = Integer.parseInt(lastnumber);
        lastno += 1;
        if(String.valueOf(lastno).length() != 2){
            lastnumber = String.format("%" + 2 + "s", String.valueOf(lastno)).replace(' ', '0');
        }
        
        modulename = Module.getModulename(moduleID.toUpperCase());
        
        classID = prefix + "-" + lastnumber + "-" + moduleID;
        String className = classType + " " + lastnumber + " " + modulename;
        
        txtid.setText(classID);
        txtname.setText(className);

    }
    
    public int saveNewClass(String classType, String ID, String name, String lecturer, String day, String startTime, String duration, String sizelimit, String venue){
        int number = 0;
        List<String> writtendata = new ArrayList<>();
        if (duration.isEmpty() || sizelimit.isEmpty() || venue.isEmpty()){
            JOptionPane.showMessageDialog(null, 
                                      "Please fill all field", 
                                      "Error", 
                                      JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        lecturer = lecturer.substring(0, lecturer.indexOf(' '));
        String mlID = ModuleLecturer.searchMLID(this.moduleID, lecturer);
        
        //String[] studentrow = {ID, username, name, email, phoneNo, course, null};
        List<String> classrow = List.of(ID, name, classType, mlID, day, startTime, duration, sizelimit, venue);
        AdminTask.writetxt(classrow, "class.txt", 2);
        return 1;
 
    }
    
}
