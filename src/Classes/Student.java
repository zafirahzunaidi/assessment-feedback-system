/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author aqilahazis
 */
public class Student extends User {
    
    private String studentID;
    private String name;
    private String email;
    private String phoneNumber;
    private String currentCourse;
    private double CGPA;
    
    private String username;
    
    public Student() {}

    public Student(String studentID, String username, String name,
                   String email, String phoneNumber,
                   String currentCourse, double cgpa) {
        super(username, "", "Student");
        this.studentID = studentID;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.currentCourse = currentCourse;
        this.CGPA = cgpa;
    }
    
    public Student(String studentID, String username,
                String name, String email, String phoneNumber,
                double CGPA) {

    super(username, null, "Student");
    
    this.studentID = studentID;
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.CGPA = CGPA;
    }
    
    // Getters
    public String getStudentID(){
        return studentID;
    }
    
    public void setID(String studentID){
        this.studentID = studentID;
    }
    
    public String getName(){
        return name;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public double getCGPA(){
        return CGPA;
    }
        
    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCGPA(double CGPA) {
        this.CGPA = CGPA;
    }
    
    public String getCurrentCourse() { return currentCourse; }
    public void setCurrentCourse(String currentCourse) { this.currentCourse = currentCourse; }
    
    @Override
    public String toString() {
        return studentID + " - " + getUsername() + " - " + name;
    }
    
    // Methods
    public static Student findByID(String studentID) {
        try (BufferedReader br =
                new BufferedReader(new FileReader("student.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(studentID)) {
                    return new Student(
                        d[0],                  // studentID
                        d[1],                  // username
                        d[2],                  // name
                        d[3],                  // email
                        d[4],                  // phone
                        Double.parseDouble(d[6]) // CGPA
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    // Lect - Grading
    public void recalculateCGPA() {
        double totalWeight = 0;
        int moduleCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("moduleResult.txt"))) {
            br.readLine(); // header
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) 
                    continue;

                String[] d = line.split(", ");

                for (int i = 0; i < d.length; i++) {
                    d[i] = d[i].trim();
                }

                if (d.length < 7) 
                    continue;

                String sID = d[1];
                String grade = d[6];

                if (!sID.equalsIgnoreCase(this.studentID)) 
                    continue;
                if (grade.equalsIgnoreCase("NULL")) 
                    continue;

                double weight = getGradeWeight(grade);

                totalWeight += weight;
                moduleCount++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (moduleCount > 0) {
            this.CGPA =
                Math.round((totalWeight / moduleCount) * 100.0) / 100.0;
            updateStudentCGPA();
        }
    }

    private double getGradeWeight(String grade) {
        if (grade == null) return 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader("gradingSystem.txt"))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split("\\s*,\\s*");

                if (d[0].trim().equalsIgnoreCase(grade.trim())) {
                    return Double.parseDouble(d[4].trim());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return 0.0;
    }

    private void updateStudentCGPA() {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("student.txt"))) {

            String header = br.readLine();
            sb.append(header).append("\n");

            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(this.studentID)) {
                    sb.append(String.join(", ",
                        d[0],
                        d[1],
                        d[2],
                        d[3],
                        d[4],
                        d[5],
                        String.valueOf(this.CGPA)
                    )).append("\n");

                } else {
                    sb.append(line).append("\n");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        try (java.io.FileWriter fw =
                new java.io.FileWriter("student.txt")) {

            fw.write(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void searchfunc(List<String> data, String searchText, List<String> containLines, JTable table){  
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                //System.out.println(row);
                String[] eachline = row.split(", ");
                if(eachline[0].toLowerCase().contains(searchText) == true || eachline[2].toLowerCase().contains(searchText) == true){
                   List<String> eachrow = Arrays.asList(row);
                   containLines.addAll(eachrow); 
                }
            }
        
        
        AdminTask task = new AdminTask();
        task.addRowtoTable(table, containLines, 4);
    }
    
    public void deletestudent(String rowID){ //hasnt been tested
        String username = "";
        List<String> containLines = new ArrayList<>();
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("student.txt");
        
        for(int i = 0; i < data.size(); i++){
                String row = data.get(i);
                String[] eachline = row.split(", ");
                if(!eachline[0].equals(rowID)){
                   List<String> eachrow = Arrays.asList(row);
                   containLines.addAll(eachrow);
                }
                else{
                    username = eachline[1];
                }
            }
        
        task.writetxt(containLines, "student.txt", 1);
        User user = new User(); //KENA CARI USERNAME BASED ON studentID
        user.deleteuser(username); 
    }
    
    public void editStudentInit(JComboBox cmbBox, JLabel lblID, JTextField txtUsername, JTextField txtFullName,
            JTextField txtEmail, JTextField txtPhone, JTextField txtPw){ 
        String username = "";
        String rowID = getStudentID();
        
        //List<String> containLines = new ArrayList<>();
        AdminTask task = new AdminTask();
        
        //addcourse in combo box
        cmbBox.removeAllItems();
        task.addCoursetoCmb(cmbBox);
        
        List<String> data = task.readtxt("student.txt");
                
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                String[] eachline = row.split(", ");
                if(eachline[0].equals(rowID)){
                    lblID.setText("ID: " + eachline[0]);
                    username = eachline[1];
                    txtUsername.setText(username);
                    txtFullName.setText(eachline[2]);
                    txtEmail.setText(eachline[3]);
                    txtPhone.setText(eachline[4]);
                    String courseID = eachline[5];
                    for (int j = 0; j < cmbBox.getItemCount(); j++) {
                        String item = (cmbBox.getItemAt(j)).toString();
                        String course = item.substring(0, item.indexOf(' '));
                        if (courseID.equals(course)){
                            cmbBox.setSelectedIndex(j);
                            break;
                        }
                    }
                }
            }
        
        User user = new User();
        user.setUsername(username);
        user.edituserInit(txtPw);
        
    }
    
    public int registerStudent(String ID, String username, String name, String email, String phoneNo, String course){
        int number = 0;
        List<String> writtendata = new ArrayList<>();
        if (ID.isEmpty() || name.isEmpty() || email.isEmpty() || phoneNo.isEmpty() || course.isEmpty()){
            JOptionPane.showMessageDialog(null, 
                                      "Please fill all field", 
                                      "Error", 
                                      JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        //name title
        AdminTask task = new AdminTask();
        name = task.titleCase(name);
        
        //email has @ and .
        boolean emailvalid = AdminTask.validateEmail(email);
        if (emailvalid == false){ //check
            JOptionPane.showMessageDialog(null, 
                                      "The email isn't valid", 
                                      "Error", 
                                      JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        //phone no validation
        if (!AdminTask.validatePhoneNo(phoneNo)) { //check
            JOptionPane.showMessageDialog(null, 
                                      "The phone number isn't valid", 
                                      "Error", 
                                      JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        course = course.substring(0, course.indexOf(' '));
        
        //String[] studentrow = {studentID, username, name, email, phoneNumber, currentCourse, null};
        List<String> studentrow = List.of(ID, username, name, email, phoneNo, course, "NULL");
        //Object[] userrow = {username, password, "Student"};
        //List<String> userrow = List.of(username, password, "Student");
        
        //add to text file
        //buat for loop > check exist ke tak > kalau tak, append > kalau ya, tukar
        // inner loop
        List<String> data = task.readtxt("student.txt");
        List<String> data2 = task.readtxt("student.txt");
        List<String> containLines = new ArrayList<>();
        
        for (int i = 0; i < data.size(); i++){
            //System.out.println(i);
            String row = data.get(i);
            //System.out.println(row);
            String[] eachline = row.split(", ");
            //System.out.println(eachline[1]);
            if (eachline[1].equals(username)){
                for(int j = 0; j < data2.size(); j++){
                    //System.out.println(j);
                    String row2 = data2.get(j);
                    String[] eachline2 = row2.split(", ");
                    //System.out.println(!eachline2[0].equals(studentID));
                    if(!eachline2[0].equals(ID)){
                       List<String> eachrow = Arrays.asList(row2);
                       containLines.addAll(eachrow);
                    }
                    else{
                        //String[] help = studentrow.toArray(new String[0]);
                        String help = String.join(", ", studentrow);
                        containLines.addAll(Arrays.asList(help));
                        //containLines.addAll(studentrow);
                    }
            }
                //System.out.println(containLines);
                writtendata = containLines;
                number = 1; //overwrite w new data
                break;
            }
            else{
                writtendata = studentrow;
                number = 2; //append
            }
        }
        
        task.writetxt(writtendata, "student.txt", number);
        return 1;
 
    }
    
    public String getlastIDnumber(){
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("student.txt");
        List<String> lastrow = data.subList(data.size()-1, data.size());
        String row = lastrow.get(0);
        String[] eachline = row.split(", ");
        String lastID = eachline[0];
        return lastID;
    }
    
    public static Student findByUsername(String username) {
        try (BufferedReader br =
                new BufferedReader(new FileReader("student.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split("\\s*,\\s*");

                // d[1] = username column
                if (d[1].equalsIgnoreCase(username)) {

                    return new Student(
                            d[0], // studentID
                            d[1], // username
                            d[2], // name
                            d[3], // email
                            d[4], // phone
                            Double.parseDouble(d[6]) // CGPA
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    
}
