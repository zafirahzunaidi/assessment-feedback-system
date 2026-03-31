/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

//import assignment.Admin_10AcadLeadMain;
//import assignment.Admin_2StudentMain;
import static Admin.Admin_11AcadLeadRegister.cmbFaculty;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class AcademicLeader extends User{
    public AcademicLeader() {}
    private String acadLeadID;
    private String name;
    private String facultyInCharge;
    private String title;
    private String email;
    private String phoneNumber;
    private String academicQualification;

    public AcademicLeader(String acadLeadID, String username, String name, 
                      String facultyInCharge, String title, String email, 
                      String phoneNumber, String academicQualification) {

    super(username, "", "Academic Leader"); 


    this.acadLeadID = acadLeadID;
    this.name = name;
    this.facultyInCharge = facultyInCharge;
    this.title = title;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.academicQualification = academicQualification;
    }

    // Get
    public String getAcadLeadID() { return acadLeadID; }
    public String getName() { return name; }
    public String getFacultyInCharge() { return facultyInCharge; }
    public String getTitle() { return title; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAcademicQualification() { return academicQualification; }

    // Set
    public void setAcadLeadID(String acadLeadID) { this.acadLeadID = acadLeadID; }
    public void setName(String name) { this.name = name; }
    public void setFacultyInCharge(String facultyInCharge) { this.facultyInCharge = facultyInCharge; }
    public void setTitle(String title) { this.title = title; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setAcademicQualification(String academicQualification) { this.academicQualification = academicQualification; }
    

    
    public void addAcadleadtoCmb(JComboBox<String> cmbbox){
//        AdminTask task = new AdminTask();
//        List<String> data = task.readtxt("academicLeader.txt");
//        for(int i = 1; i < data.size(); i++){
//                String row = data.get(i);
//                //System.out.println(row);
//                String[] eachline = row.split(", ");
//                String acadleadname = eachline[0] + " - " + eachline[2] + " (" + eachline[3] + ") ";
//                cmbbox.addItem(acadleadname);
//            }

        List<String> data = AdminTask.readtxt("faculty.txt");
        List<String> data2 = AdminTask.readtxt("academicLeader.txt");
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                //System.out.println(row);
                String[] eachline = row.split(", ");
                String facultyID = eachline[0];
                for(int j = 1; j < data2.size(); j++){
                    String row2 = data2.get(j);
                    String[] eachline2 = row2.split(", ");
                    if (eachline2[3].equals(facultyID)){
                        String acadleadname = eachline2[0] + " - " + eachline2[2] + " (" + eachline[1] + ") ";
                        cmbbox.addItem(acadleadname);
                    }
                }
                
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
        task.addRowtoTable(table, containLines, 7);
    }
    
    public int registerAcadlead(String ID, String username, String name, String facultyIC, String email, String phone, String academicQual){
        int number = 0;
        List<String> writtendata = new ArrayList<>();
        if (ID.isEmpty() || name.isEmpty() || facultyIC.isEmpty()){
            JOptionPane.showMessageDialog(null, 
                                      "Please fill all field", 
                                      "Error", 
                                      JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        //name title
        AdminTask task = new AdminTask();
        name = task.titleCase(name);
                
        //String[] studentrow = {ID, username, name, email, phoneNo, course, null};
        List<String> studentrow = List.of(ID, username, name, facultyIC, "", "", "");
        //Object[] userrow = {username, password, "Student"};
        //List<String> userrow = List.of(username, password, "Student");
        
        //add to text file
        //buat for loop > check exist ke tak > kalau tak, append > kalau ya, tukar
        // inner loop
        List<String> data = task.readtxt("academicLeader.txt");
        List<String> data2 = task.readtxt("academicLeader.txt");
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
                    //System.out.println(!eachline2[0].equals(ID));
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
        
        task.writetxt(writtendata, "academicLeader.txt", number);
        return 1;
 
    }
    
    public static String getlastIDnumber(){
        //AdminTask task = new AdminTask();
        List<String> data = AdminTask.readtxt("academicLeader.txt");
        List<String> lastrow = data.subList(data.size()-1, data.size());
        String row = lastrow.get(0);
        String[] eachline = row.split(", ");
        String lastID = eachline[0];
        return lastID;
    }
    
        public String toFileString() {
        return acadLeadID + ", " +
               getUsername() + ", " +
               name + ", " +
               facultyInCharge + ", " +
               title + ", " +
               email + ", " +
               phoneNumber + ", " +
               academicQualification;
    }

        public static AcademicLeader fromFileString(String line) {
            String[] parts = line.split(", ");

            // Fill missing columns with empty strings
            String[] safe = new String[8];

            for (int i = 0; i < safe.length; i++) {
                safe[i] = (i < parts.length) ? parts[i].trim() : "";
            }

            return new AcademicLeader(
                safe[0],
                safe[1],
                safe[2],
                safe[3],
                safe[4],
                safe[5],
                safe[6],
                safe[7]
            );
        }


        public static List<AcademicLeader> loadLeaders(String filePath) {

        List<AcademicLeader> leaders = new ArrayList<>();
        List<String> lines = FileFunctions.readAllLines(filePath);

        for (String line : lines) {

            if (line.toLowerCase().startsWith("acadleadid")) {
                continue;
            }

            try {
                leaders.add(AcademicLeader.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Skipping invalid leader line: " + line);
            }
        }

        return leaders;
    }

        public static void saveLeaders(String filePath, List<AcademicLeader> leaders) {

        List<String> lines = new ArrayList<>();
        lines.add("acadLeadID,username,name,faculty,title,email,phone,academicQualification");

        for (AcademicLeader a : leaders) {
            lines.add(a.toFileString());
        }

        FileFunctions.writeAllLines(filePath, lines);
    }
        
    public static AcademicLeader findByUsername(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("academicLeader.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                AcademicLeader leader = AcademicLeader.fromFileString(line);

                if (leader.getUsername().equals(username)) {
                    return leader;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
    
    public void editALInit(JComboBox cmbBox, JLabel lblID, JTextField txtUsername, JTextField txtFullName, JTextField txtPw){ 
        String username = "";
        String rowID = getAcadLeadID();
        
        //List<String> containLines = new ArrayList<>();
        AdminTask task = new AdminTask();
        
        //addcourse in combo box
        cmbBox.removeAllItems();
        Faculty.addfacultytoCmb(cmbBox);
        
        List<String> data = task.readtxt("academicLeader.txt");
                
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                String[] eachline = row.split(", ");
                if(eachline[0].equals(rowID)){
                    lblID.setText("ID: " + eachline[0]);
                    username = eachline[1];
                    txtUsername.setText(username);
                    txtFullName.setText(eachline[2]);
                    String courseID = eachline[3];
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
    
    public void deleteAL(String rowID){ //hasnt been tested
        String username = "";
        List<String> containLines = new ArrayList<>();
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("academicLeader.txt");
        
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
        
        task.writetxt(containLines, "academicLeader.txt", 1);
        User user = new User(); //KENA CARI USERNAME BASED ON studentID
        user.deleteuser(username); 
    }


}



