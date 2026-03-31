/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author aqilahazis
 */
public class Faculty {
    // add data into combo box
    // get name of the faculty based on ID
    
    public static void addfacultytoCmb(JComboBox<String> cmbbox){
        if (cmbbox != null) {
            cmbbox.removeAllItems();
        }
        List<String> data = AdminTask.readtxt("faculty.txt");
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                //System.out.println(row);
                String[] eachline = row.split(", ");
                String facultyname = eachline[0] + " - " + eachline[1];
                cmbbox.addItem(facultyname);
            }
    }
}
