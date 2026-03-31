/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

//import assignment.Admin_1dashboard;
//import static assignment.Admin_3StudentRegister.cmbCourse;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import static java.nio.file.Files.lines;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aqilahazis
 */
public class AdminTask {
    
    public AdminTask(){
        
    }
    
    public static void writetxt(List<String> data, String filename, int number){ 
        if (number == 1){ //overwriting
            try { 
               BufferedWriter writer = new BufferedWriter(new FileWriter(filename)); 
               for(int i = 0; i < data.size(); i++){
                  String row = data.get(i);
                  writer.write(row + "\n");
//                  if(i < data.size() - 1)
//                  writer.write(row + "\n"); 
//                  else{
//                   writer.write(row); 
//                  }
               }
               writer.close();
           } catch (IOException ex) {
               ex.printStackTrace(); //not sure what this means, will change later prolly
           }   
        }
        else if (number == 2){ //appending
            try { 
               BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true)); 
               String row = String.join(", ", data);
               writer.write(row + "\n"); 
               writer.close();
           } catch (IOException ex) {
               ex.printStackTrace(); //not sure what this means, will change later prolly
           }   
        }
    }

    public static List<String> readtxt(String filename){
        List<String> lines = new ArrayList<>();
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            int count = 0;
            while((line = reader.readLine()) != null){
                List<String> eachrow = Arrays.asList(line);
                lines.addAll(eachrow);
                //String[] eachline = line.split(", ");
                count += 1;
                
            }
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace(); //not sure what this means, will change later prolly
        }
        return lines;
    }
    
    public static void addRowtoTable(JTable tablename, List<String> data, int number){
        
        //System.out.println(data.get(0));
        DefaultTableModel newtable = (DefaultTableModel) tablename.getModel();
        newtable.setRowCount(0);
        
        switch (number) {
            case 1:
                {
                    Object rowData [] = new Object[3];
                    for(int i = 1; i < data.size(); i++){
                        String row = data.get(i);
                        String[] eachline = row.split(", ");
                        rowData[0] = eachline[2];
                        rowData[1] = eachline[0];
                        rowData[2] = eachline[5];
                        newtable.addRow(rowData);
                    }       break;
                }
            case 2:
                {
                    Object rowData [] = new Object[2];
                    for(int i = 1; i < data.size(); i++){
                        String row = data.get(i);
                        String[] eachline = row.split(", ");
                        rowData[0] = eachline[0];
                        rowData[1] = eachline[1];
                        newtable.addRow(rowData);
                    }       break;
                }
            case 3:
                {
                    Object rowData [] = new Object[3];
                    for(int i = 0; i < data.size(); i++){
                        String row = data.get(i);
                        String[] eachline = row.split(", ");
                        rowData[0] = eachline[2];
                        rowData[1] = eachline[0];
                        rowData[2] = eachline[4];
                        newtable.addRow(rowData);
                    }       break;
                }
            case 4:
                {
                    Object rowData [] = new Object[3];
                    for(int i = 0; i < data.size(); i++){
                        String row = data.get(i);
                        String[] eachline = row.split(", ");
                        rowData[0] = eachline[2];
                        rowData[1] = eachline[0];
                        rowData[2] = eachline[5];
                        newtable.addRow(rowData);
                    }       break;
                }
            case 5:
                {
                    Object rowData [] = new Object[3];
                    for(int i = 1; i < data.size(); i++){
                        String row = data.get(i);
                        String[] eachline = row.split(", ");
                        rowData[0] = eachline[0];
                        rowData[1] = eachline[1];
                        rowData[2] = eachline[4];
                        newtable.addRow(rowData);
                    }       break;
                }
            case 6:
                {
                    Object rowData [] = new Object[3];
                    for(int i = 1; i < data.size(); i++){
                        String row = data.get(i);
                        String[] eachline = row.split(", ");
                        rowData[0] = eachline[2];
                        rowData[1] = eachline[0];
                        rowData[2] = eachline[4];
                        newtable.addRow(rowData);
                    }       break;
                }
            case 7:
                {
                    String facultyname = "";
                    Object rowData [] = new Object[3];
                    for(int i = 0; i < data.size(); i++){
                        String row = data.get(i);
                        String[] eachline = row.split(", ");
                        rowData[0] = eachline[2];
                        rowData[1] = eachline[0];
                        
                        String facultyID = eachline[3];
                        
                        List<String> data2 = AdminTask.readtxt("faculty.txt");
                        for(int j = 1; i < data2.size(); j++){
                            String row2 = data2.get(j);
                            //System.out.println(row);
                            String[] eachline2 = row2.split(", ");
                            if (eachline2[0].equals(facultyID)){
                               facultyname = eachline2[0] + " - " + eachline2[1];
                               break;
                            }
                        }
                        
                        rowData[2] = facultyname;
                        newtable.addRow(rowData);
                    }       break;
                }
            case 8:
                {
                    String facultyname = "";
                    Object rowData [] = new Object[3];
                    for(int i = 1; i < data.size(); i++){
                        String row = data.get(i);
                        String[] eachline = row.split(", ");
                        //System.out.println(row);
                        rowData[0] = eachline[2];
                        rowData[1] = eachline[0];
                        
                        String facultyID = eachline[3];
                        
                        List<String> data2 = AdminTask.readtxt("faculty.txt");
                        for(int j = 1; i < data2.size(); j++){
                            String row2 = data2.get(j);
                            //System.out.println(row);
                            String[] eachline2 = row2.split(", ");
                            if (eachline2[0].equals(facultyID)){
                               facultyname = eachline2[0] + " - " + eachline2[1];
                               break;
                            }
                        }
                        
                        rowData[2] = facultyname;
                        newtable.addRow(rowData);
                    }       break;
                }
            default:
                break;
        }
        
        
        for (int row = 0; row < tablename.getRowCount(); row++){
        int rowHeight = tablename.getRowHeight();

        for (int column = 0; column < tablename.getColumnCount(); column++)
        {
            Component comp = tablename.prepareRenderer(tablename.getCellRenderer(row, column), row, column);
            rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
        }

        tablename.setRowHeight(row, rowHeight);
        }
     
    }
    
    public void addCoursetoCmb(JComboBox<String> cmbbox){
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("course.txt");
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                //System.out.println(row);
                String[] eachline = row.split(", ");
                String coursename = eachline[0] + " - " + eachline[1] + " (" + eachline[2] + ") ";
                cmbbox.addItem(coursename);
            }
    }
    
    public static String titleCase(String text){
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    
    }
    
    public static boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                            "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(email).matches();
    }
    
    public static boolean validatePhoneNo(String phoneNo) {
        String regex = "^(01|601)?[0-9]{9,11}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(phoneNo).matches();
    }
    
    public static void initUsernameRole (String username, String role, JLabel label, JLabel label2){
        label.setText(username);
        label2.setText(role);
    }
}
    
    




