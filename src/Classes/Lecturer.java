/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author aqilahazis
 */
public class Lecturer extends User {
    
    private String lecturerID;
    private String name;
    private String title;
    private String email;
    private String phoneNumber;
    private String academicQualification;
    private String acadLeadID;
    
    private String username;
    
    public Lecturer(String lecturerID, String username,
                String name, String title, String email, String phoneNumber,
                String academicQualification, String acadLeadID) {

    super(username, null, "Lecturer");
    
    this.lecturerID = lecturerID;
    this.name = name;
    this.title = title;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.academicQualification = academicQualification;
    this.acadLeadID = acadLeadID;
    }
    
    public Lecturer(){ //first constructor
        
    }
    
    public Lecturer(String ID){ //second constructor
        this.lecturerID = ID;
    }
    
    // Getters
    public String getLecturerID(){
        return lecturerID;
    }
    
    public String getName(){
        return name;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public String getAcademicQualification(){
        return academicQualification;
    }
    
    public String getAcadLeadID(){
        return acadLeadID;
    }
    
    
    // Setters
    public void setID(String ID) {
        this.lecturerID = ID;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAcademicQualification(String academicQualification) {
        this.academicQualification = academicQualification;
    }
    
    // Methods
      
    // Lect - Db
    public static Lecturer findByUsername(String username) {
        String password = null;

        // 1: Read password from user.txt
        try (BufferedReader br = new BufferedReader(new FileReader("user.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(username) && d[2].equals("Lecturer")) {
                    password = d[1];
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2: Read lecturer profile
        try (BufferedReader br = new BufferedReader(new FileReader("lecturer.txt"))) {
            br.readLine(); // header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[1].equals(username)) {

                    Lecturer lecturer = new Lecturer(
                        d[0], d[1], d[2], d[3],
                        d[4], d[5], d[6], d[7]
                    );

                    lecturer.setPassword(password);
                    return lecturer;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getTotalModulesTeaching() {
        Set<String> modules = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader("moduleLecturer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d[1].equals(this.lecturerID)) {
                    modules.add(d[2]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modules.size();
    }
        
    public int getTotalClasses() {
        Set<String> modLectIDs = new HashSet<>();
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("moduleLecturer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d[1].equals(this.lecturerID)) {
                    modLectIDs.add(d[0]);
                }
            }
        } catch (Exception e) {}

        try (BufferedReader br = new BufferedReader(new FileReader("class.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (modLectIDs.contains(d[3])) count++;
            }
        } catch (Exception e) {}

        return count;
    }
    
    public int getTotalStudents() {
        Set<String> modLectIDs = new HashSet<>();
        Set<String> classIDs = new HashSet<>();
        Set<String> studentIDs = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader("moduleLecturer.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d[1].equals(this.lecturerID)) modLectIDs.add(d[0]);
            }
        } catch (Exception e) {}

        try (BufferedReader br = new BufferedReader(new FileReader("class.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (modLectIDs.contains(d[3])) classIDs.add(d[0]);
            }
        } catch (Exception e) {}

        try (BufferedReader br = new BufferedReader(new FileReader("registeredClass.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (classIDs.contains(d[1])) studentIDs.add(d[2]);
            }
        } catch (Exception e) {}

        return studentIDs.size();
    }

    public int getPendingAssessmentsCount() {
        int pendingCount = 0;

        // get lecturer classes
        Set<String> lecturerClasses = new HashSet<>();
        for (Class c : getClasses()) {
            lecturerClasses.add(c.getClassID());
        }

        // loop through assessments of those classes
        try (BufferedReader br =
                new BufferedReader(new FileReader("assessment.txt"))) {

            br.readLine(); // header
            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split("\\s*,\\s*");

                String assessID = d[0];
                String classID  = d[1];

                if (!lecturerClasses.contains(classID))
                    continue;

                // get enrolled students
                Set<String> enrolled = new HashSet<>();

                try (BufferedReader rc =
                        new BufferedReader(
                            new FileReader("registeredClass.txt"))) {

                    rc.readLine();
                    String rLine;

                    while ((rLine = rc.readLine()) != null) {
                        String[] r = rLine.split("\\s*,\\s*");

                        if (r[1].equals(classID)) {
                            enrolled.add(r[2]);
                        }
                    }
                }

                // get graded students
                Set<String> graded = new HashSet<>();

                try (BufferedReader res = new BufferedReader(new FileReader("result.txt"))) {

                    res.readLine();
                    String resLine;

                    while ((resLine = res.readLine()) != null) {
                        String[] r = resLine.split("\\s*,\\s*");

                        if (r[2].equals(assessID)) {
                            graded.add(r[1]);
                        }
                    }
                }

                // check missing students
                enrolled.removeAll(graded);

                if (!enrolled.isEmpty()) {
                    pendingCount++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pendingCount;
    }

  
    // Lect - Module 
    public List<Module> getModules() {
        List<Module> modules = new ArrayList<>();
        List<String> moduleIDs = new ArrayList<>();

        // read moduleLecturer.txt
        try (BufferedReader br = new BufferedReader(new FileReader("moduleLecturer.txt"))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d[1].equals(this.lecturerID)) {
                    moduleIDs.add(d[2]);
                }
            }
        } catch (IOException e) {}

        // read module.txt
        try (BufferedReader br = new BufferedReader(new FileReader("module.txt"))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (moduleIDs.contains(d[0])) {
                    modules.add(new Module(
                        d[0],
                        d[1],
                        Double.parseDouble(d[2])
                    ));
                }
            }
        } catch (IOException e) {}

        return modules;
    }

    
    // Lect - Class
    public List<String> getModuleIDs() {
        List<String> modules = new ArrayList<>();

        try (BufferedReader br =
                new BufferedReader(new FileReader("moduleLecturer.txt"))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[1].equals(this.lecturerID)) {
                    modules.add(d[2]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return modules;
    }

    public List<Class> getClasses(){
        List<Class> classes = new ArrayList<>();

        try{
            List<String> modLectIDs = new ArrayList<>();

            BufferedReader br1 = new BufferedReader(new FileReader("moduleLecturer.txt"));
            br1.readLine();
            String line;
            
            while((line = br1.readLine()) != null){
                String[] data = line.split("\\s*,\\s*");
                if(data[1].equals(this.lecturerID)){
                    modLectIDs.add(data[0]);
                }
            }

            br1.close();
            BufferedReader br2 =
                new BufferedReader(new FileReader("class.txt"));
            br2.readLine();
            
            while((line = br2.readLine()) != null){
                String[] d = line.split("\\s*,\\s*");
                if(modLectIDs.contains(d[3])){

                    classes.add(
                        new Class(
                            d[0], // classID
                            d[1], // name
                            d[2], // type
                            d[4], // day
                            d[5]  // startTime
                        )
                    );
                }
            }
            br2.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return classes;
    }
    
    public List<Class> getClassesByModule(String moduleID) {
        if (moduleID == null || moduleID.equals("All Courses")) {
            return getClasses();
        }

        List<Class> classes = new ArrayList<>();

        try {
            List<String> validModLectIDs = new ArrayList<>();

            // Find moduleLecturerID for this lecturer + selected module
            BufferedReader br1 = new BufferedReader(
                    new FileReader("moduleLecturer.txt"));
            br1.readLine(); // skip header
            String line;

            while ((line = br1.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                String modLectID = d[0];
                String lecturerID = d[1];
                String modID = d[2];

                if (lecturerID.equals(this.lecturerID)
                        && modID.equals(moduleID)) {
                    validModLectIDs.add(modLectID);
                }
            }
            br1.close();

            // Read classes that match those moduleLecturerID
            BufferedReader br2 = new BufferedReader(
                    new FileReader("class.txt"));
            br2.readLine();

            while ((line = br2.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (validModLectIDs.contains(d[3])) {
                    classes.add(new Class(
                            d[0], // classID
                            d[1], // name
                            d[2], // type
                            d[4], // day
                            d[5]  // startTime
                    ));
                }
            }
            br2.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    
    // Lect - Profile
    public String getMaskedPassword() {
        String pw = getPassword();
        if (pw == null) return "";
        return "*".repeat(pw.length());
    }

    
    public String getAcademicLeaderName() {
        if (acadLeadID == null || acadLeadID.equalsIgnoreCase("NULL")) {
            return "Not Assigned";
        }

        try (BufferedReader br = new BufferedReader(
                new FileReader("academicLeader.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\s*,\\s*");
                if (data[0].equals(acadLeadID)) {
                    return data[2]; // leader name
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Not Assigned";
    }
    
    public ProfileUpdateResult updateProfile(
        String username,
        String name,
        String title,
        String email,
        String phone,
        String academicQualification,
        java.awt.Component parent
    ) {

        if (username.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            return ProfileUpdateResult.EMPTY_FIELDS;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(gmail\\.com|yahoo\\.com|outlook\\.com)$")) {
            return ProfileUpdateResult.INVALID_EMAIL;
        }

        if (!phone.matches("^01\\d{8,9}$")) {
            return ProfileUpdateResult.INVALID_PHONE;
        }

        if (usernameExists(username)) {
            return ProfileUpdateResult.USERNAME_EXISTS;
        }

        if (!confirmPassword(parent)) {
            return ProfileUpdateResult.PASSWORD_FAILED;
        }

        // Update object
        String oldUsername = getUsername();  // store old username
        
        setUsername(username);
        this.name = name;
        this.title = title;
        this.email = email;
        this.phoneNumber = phone;
        this.academicQualification = academicQualification;

        try {
            updateLecturerTxt();
            updateUserTxt(oldUsername);
        } catch (IOException e) {
            return ProfileUpdateResult.IO_ERROR;
        }

        return ProfileUpdateResult.SUCCESS;
    }
    
    private boolean usernameExists(String newUsername) {
        try (BufferedReader br = new BufferedReader(new FileReader("user.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d[0].equalsIgnoreCase(newUsername)
                    && !d[0].equalsIgnoreCase(this.getUsername())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean confirmPassword(java.awt.Component parent) {
        int attempts = 0;

        while (attempts < 3) {
            javax.swing.JPasswordField pf = new javax.swing.JPasswordField();

            int option = javax.swing.JOptionPane.showConfirmDialog(
                parent,
                pf,
                "Enter password to confirm update",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE
            );

            if (option != javax.swing.JOptionPane.OK_OPTION)
                return false;

            String entered = new String(pf.getPassword());

            if (entered.equals(getPassword())) {
                return true;
            }

            attempts++;
            javax.swing.JOptionPane.showMessageDialog(parent,
                "Incorrect password. Attempts left: " + (3 - attempts),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );
        }
        return false;
    }

    private void updateLecturerTxt() throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("lecturer.txt"))) {
            String header = br.readLine();
            sb.append(header).append("\n");

            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(this.lecturerID)) {
                    sb.append(String.join(", ",
                        lecturerID,
                        getUsername(),
                        name,
                        title == null ? "NULL" : title,
                        email,
                        phoneNumber,
                        academicQualification,
                        acadLeadID
                    )).append("\n");
                } else {
                    sb.append(line).append("\n");
                }
            }
        }

        try (java.io.FileWriter fw = new java.io.FileWriter("lecturer.txt")) {
            fw.write(sb.toString());
        }
    }

    private void updateUserTxt(String oldUsername) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("user.txt"))) {
            String header = br.readLine();
            sb.append(header).append("\n");

            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(oldUsername)) {
                    sb.append(String.join(", ",
                        getUsername(),   // new username
                        d[1],            // password
                        d[2]             // role
                    )).append("\n");
                } else {
                    sb.append(line).append("\n");
                }
            }
        }

        try (FileWriter fw = new FileWriter("user.txt")) {
            fw.write(sb.toString());
        }
    }
    
    public PasswordChangeResult changePassword(
        String currentPass,
        String newPass,
        String confirmPass
        ) {

            if (currentPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                return PasswordChangeResult.EMPTY_FIELDS;
            }

            if (!currentPass.equals(getPassword())) {
                return PasswordChangeResult.WRONG_CURRENT_PASSWORD;
            }

            if (newPass.equals(currentPass)) {
                return PasswordChangeResult.SAME_AS_OLD;
            }

            if (!newPass.equals(confirmPass)) {
                return PasswordChangeResult.CONFIRM_MISMATCH;
            }

            if (!newPass.matches(
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$"
            )) {
                return PasswordChangeResult.WEAK_PASSWORD;
            }

            setPassword(newPass);

            try {
                updateUserPasswordTxt();
            } catch (IOException e) {
                return PasswordChangeResult.IO_ERROR;
            }

            return PasswordChangeResult.SUCCESS;
        }

    private void updateUserPasswordTxt() throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("user.txt"))) {
            String header = br.readLine();
            sb.append(header).append("\n");

            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(getUsername())) {
                    sb.append(String.join(", ",
                        d[0],
                        getPassword(),
                        d[2]
                    )).append("\n");
                } else {
                    sb.append(line).append("\n");
                }
            }
        }

        try (java.io.FileWriter fw = new java.io.FileWriter("user.txt")) {
            fw.write(sb.toString());
        }
    }
    
    
    // Lect - Assessment
    public List<Assessment> getAssessments() {
        List<Assessment> assessments = new ArrayList<>();

        // Get all classes taught by this lecturer
        List<Class> classes = getClasses();
        Set<String> classIDs = new HashSet<>();

        for (Class c : classes) {
            classIDs.add(c.getClassID());
        }

        // Read assessment.txt
        try (BufferedReader br =
                new BufferedReader(new FileReader("assessment.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                String assessID   = d[0];
                String classID    = d[1];
                String assessType = d[2];
                double fullMark   = Double.parseDouble(d[3]);
                double weightage  = Double.parseDouble(d[4]);

                // Only include lecturer's classes
                if (classIDs.contains(classID)) {
                    assessments.add(
                        new Assessment(
                            assessID,
                            classID,
                            assessType,
                            fullMark,
                            weightage
                        )
                    );
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return assessments;
    }
    
    
    // Lect - Assessment [Design]
    public double getTotalWeightageForClass(String classID) {
        double total = 0.0;

        try (BufferedReader br = new BufferedReader(
                new FileReader("assessment.txt"))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d[1].equals(classID)) {
                    total += Double.parseDouble(d[4]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }
    
    public boolean checkFinalExamExist(String classID) {
        try (BufferedReader br = new BufferedReader(new FileReader("assessment.txt"))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[1].equals(classID)
                    && d[2].equalsIgnoreCase("Final Exam")) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private String generateAssessmentTypeName(String classID, String assessType) {
        int count = 0;

        try (BufferedReader br =
                new BufferedReader(new FileReader("assessment.txt"))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[1].equals(classID)
                    && d[2].startsWith(assessType)) {
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count == 0 ? assessType : assessType + " " + (count + 1);
    }
    
    public AssessmentCreateResult createAssessment(
        String classID,
        String assessType,
        double fullMark,
        double weightage
    ) {
        double used = getTotalWeightageForClass(classID);

        // Weightage already full
        if (used >= 1.0) {
            return AssessmentCreateResult.WEIGHTAGE_FULL;
        }

        // Weightage exceed
        if (used + weightage > 1.0) {
            return AssessmentCreateResult.WEIGHTAGE_EXCEEDED;
        }

        // Only 1 Final Exam per class
        if (assessType.equalsIgnoreCase("Final Exam")
                && checkFinalExamExist(classID)) {
            return AssessmentCreateResult.FINAL_EXAM_EXISTS;
        }

        // Generate ID & append
        String assessID = generateNextAssessmentID();
        String finalType = generateAssessmentTypeName(classID, assessType);
        
        // Composition
        Assessment newAssessment = new Assessment(assessID, classID, finalType, 
                fullMark, weightage);

        Module module = findModuleByClass(classID);

        if (module != null) {
            module.addAssessment(newAssessment);
        }

        try (java.io.FileWriter fw =
                new java.io.FileWriter("assessment.txt", true)) {

            fw.write(String.join(", ",
                assessID,
                classID,
                finalType,
                String.valueOf(fullMark),
                String.valueOf(weightage)
            ) + "\n");

        } catch (Exception e) {
            e.printStackTrace();
            return AssessmentCreateResult.IO_ERROR;
        }

        return AssessmentCreateResult.SUCCESS;
    }
    
    private Module findModuleByClass(String classID) {
        String moduleID = getModuleIDFromClass(classID);

        for (Module m : getModules()) {
            if (m.getModuleID().equals(moduleID)) {
                return m;
            }
        }
        return null;
    }

    private String generateNextAssessmentID() {
        int max = 0;

        try (BufferedReader br = new BufferedReader(
                new FileReader("assessment.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                String id = d[0]; // A0001

                int num = Integer.parseInt(id.substring(1)); // remove 'A'
                if (num > max) {
                    max = num;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.format("A%04d", max + 1);
    }
    
    
    // Lect - Assessment [Delete]
    public boolean isAssessmentGraded(String assessID) {
        try (BufferedReader br = new BufferedReader(new FileReader("result.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d.length >= 3 && d[2].equals(assessID)) {
                    return true; // at least 1 student graded
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deleteAssessment(String assessID) {
        // Check if graded
        try (BufferedReader br = new BufferedReader(new FileReader("result.txt"))) {
            br.readLine(); // header
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d.length >= 3 && d[2].equals(assessID)) {
                    return false; // already graded
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Rewrite assessment.txt without this ID
        StringBuilder sb = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("assessment.txt"))) {
            String header = br.readLine();
            sb.append(header).append("\n");

            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith(assessID + ", ")) {
                    sb.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        try (java.io.FileWriter fw = new java.io.FileWriter("assessment.txt")) {
            fw.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    
    // Lect - Grading [update mark]
    public Assessment getAssessmentByID(String assessID) {
        for (Assessment a : getAssessments()) {
            if (a.getAssessID().equals(assessID)) {
                return a;
            }
        }
        return null;
    }

    public List<Student> getStudentsForAssessment(String assessID) {
        List<Student> students = new ArrayList<>();

        Assessment a = getAssessmentByID(assessID);
        if (a == null) return students;

        String classID = a.getClassID();

        try (BufferedReader br = new BufferedReader(new FileReader("registeredClass.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[1].equals(classID)) {

                    Student s = Student.findByID(d[2]);
                    if (s != null) {
                        students.add(s);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }

    public Result getResult(String studentID, String assessID) {
        try (BufferedReader br =
                new BufferedReader(new FileReader("result.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                if (line.trim().isEmpty()) continue; 

                String[] d = line.split("\\s*,\\s*");

                if (d.length < 4) continue;


                if (d[1].equals(studentID) && d[2].equals(assessID)) {
                    return new Result(
                        d[1],
                        d[2],
                        Double.parseDouble(d[3]), 
                        d.length >= 5 ? d[4] : ""
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveResult(String studentID, String assessID, Double mark, String feedback) {
        StringBuilder sb = new StringBuilder();
        boolean updated = false;
        String resultID = generateNextResultID();

        try (BufferedReader br = new BufferedReader(
            new FileReader("result.txt"))) {

            String header = br.readLine();
            sb.append(header).append("\n");

            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[1].equals(studentID) && d[2].equals(assessID)) {
                    sb.append(String.join(", ",
                        d[0],
                        studentID,
                        assessID,
                        (mark == null ? "NULL" : String.valueOf(mark)),
                        (feedback == null || feedback.trim().isEmpty()) ? "NULL" : feedback
                    )).append("\n");
                    updated = true;
                } else {
                    sb.append(line).append("\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!updated) {
            sb.append(String.join(", ",
                resultID,
                studentID,
                assessID,
                (mark == null ? "NULL" : String.valueOf(mark)),
                (feedback == null || feedback.trim().isEmpty()) ? "NULL" : feedback
            )).append("\n");
        }

        try (java.io.FileWriter fw =
                new java.io.FileWriter("result.txt")) {
            fw.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assessment a = getAssessmentByID(assessID);

        if (a != null && mark != null) { 
            String moduleID = getModuleIDFromClass(a.getClassID());
            updateModuleResult(studentID, moduleID);
        }
    }
    
    private String generateNextResultID() {
        int max = 0;

        try (BufferedReader br = new BufferedReader(
                new FileReader("result.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                String id = d[0]; // R00001

                int num = Integer.parseInt(id.substring(1)); // remove 'R'
                if (num > max) {
                    max = num;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("R%05d", max + 1);
    }
    
    public void updateModuleResult(String studentID, String moduleID) {

        double lecturePercent = 0.0;
        double tutorialPercent = 0.0;

        // loop through all assignments
        for (Assessment a : getAssessmentsByModule(moduleID)) {

            String aModuleID = getModuleIDFromClass(a.getClassID());
            if (!moduleID.equals(aModuleID)) continue;

            Result r = getResult(studentID, a.getAssessID());
            if (r == null) continue;

            double weightedPercent = a.calculateWeightedMark(r.getMarkReceived());

            if (isLectureClass(a.getClassID())) {
                lecturePercent += weightedPercent;
            } else if (isTutorialClass(a.getClassID())) {
                tutorialPercent += weightedPercent;
            }
        }

        lecturePercent = round2(lecturePercent);
        tutorialPercent = round2(tutorialPercent);

        // Check if both components exist
        boolean hasLecture = lecturePercent > 0;
        boolean hasTutorial = tutorialPercent > 0;

        double finalMark;
        String grade;

        if (!hasLecture || !hasTutorial) {
            finalMark = 0;
            grade = "NULL";
        } else {
            finalMark = round2((lecturePercent + tutorialPercent)/2); 
            grade = ModuleResult.assignGrade(finalMark);
        }


        writeOrUpdateModuleResult(
            studentID,
            moduleID,
            lecturePercent,
            tutorialPercent,
            finalMark,
            grade
        );
        
        Student s = Student.findByID(studentID);
        if (s != null) {
            s.recalculateCGPA();
        }

    }

    private double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private void writeOrUpdateModuleResult(
        String studentID,
        String moduleID,
        double lecture,
        double tutorial,
        double finalMark,
        String grade
    ) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;

        try (BufferedReader br =
                new BufferedReader(new FileReader("moduleResult.txt"))) {

            sb.append(br.readLine()).append("\n"); // header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[1].trim().equals(studentID.trim()) && d[2].trim().equals(moduleID.trim())) {
                    sb.append(String.join(", ",
                        d[0],
                        studentID,
                        moduleID,
                        String.valueOf(lecture),
                        String.valueOf(tutorial),
                        String.valueOf(finalMark),
                        grade
                    )).append("\n");
                    found = true;
                } else {
                    sb.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!found) {
            sb.append(String.join(", ",
                generateNextModuleResultID(),
                studentID,
                moduleID,
                String.valueOf(lecture),
                String.valueOf(tutorial),
                String.valueOf(finalMark),
                grade
            )).append("\n");
        }

        try (FileWriter fw =
                new FileWriter("moduleResult.txt")) {
            fw.write(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get the type of class (Lecture or Tutorial) by classID
    public String getClassType(String classID) {
        try (BufferedReader br =
                new BufferedReader(new FileReader("class.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(classID)) {
                    return d[2]; // Lecture or Tutorial
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ""; // fallback
    }
    
    // Normalize class categories
    private boolean isLectureClass(String classID) {
        String type = getClassType(classID);
        return type != null &&
               type.equalsIgnoreCase("Lecture");
    }

    private boolean isTutorialClass(String classID) {
        String type = getClassType(classID);
        return type != null &&
               (type.equalsIgnoreCase("Tutorial") || type.equalsIgnoreCase("Lab"));
    }

    public String getModuleIDFromClass(String classID) {
        String moduleLecturerID = null;

        // class -> moduleLecturerID
        try (BufferedReader br =
                new BufferedReader(new FileReader("class.txt"))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(classID)) {
                    moduleLecturerID = d[3];
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (moduleLecturerID == null) return null;

        //  moduleLecturerID -> moduleID
        try (BufferedReader br =
                new BufferedReader(new FileReader("moduleLecturer.txt"))) {

            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d[0].equals(moduleLecturerID)) {
                    return d[2]; // moduleID
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String generateNextModuleResultID() {
        int max = 0;

        try (BufferedReader br = new BufferedReader(
                new FileReader("moduleResult.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                String id = d[0]; 

                int num = Integer.parseInt(id.substring(2)); // remove 'MR'
                if (num > max) {
                    max = num;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.format("MR%05d", max + 1);
    }
    
    public List<Assessment> getAssessmentsByModule(String moduleID) {
        List<Assessment> list = new ArrayList<>();

        try (BufferedReader br =
                new BufferedReader(new FileReader("assessment.txt"))) {

            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {

                String[] d = line.split("\\s*,\\s*");
                if (d.length < 5) continue;

                String assessID  = d[0];
                String classID   = d[1];
                String type      = d[2];
                double fullMark  = Double.parseDouble(d[3]);
                double weightage = Double.parseDouble(d[4]);

                String aModuleID = getModuleIDFromClass(classID);

                if (moduleID.equals(aModuleID)) {
                    list.add(new Assessment(
                        assessID, classID, type, fullMark, weightage
                    ));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
    // Lect - View Result
    // Get all students enrolled in a class by classID
    public List<Student> getStudentsForClass(String classID) {
        List<Student> students = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("registeredClass.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d[1].equals(classID)) {
                    Student s = Student.findByID(d[2]);  // Assume Student class has a method to fetch by ID
                    if (s != null) {
                        students.add(s);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    public Result getResultForStudentInClass(String studentID, String classID) {
        String moduleID = getModuleIDFromClass(classID);

        // fetch the result for the student in that module
        try (BufferedReader br = new BufferedReader(new FileReader("moduleResult.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                // Check if the student ID and module ID match
                if (d[1].equals(studentID) && d[2].equals(moduleID)) {

                    // Determine the mark based on class type
                    double mark = 0;
                    if (isTutorialClass(classID)) {
                        mark = Double.parseDouble(d[4]); // tuto mark
                    } else if (isLectureClass(classID)) {
                        mark = Double.parseDouble(d[3]);// lecture mark
                    }


                    // Return the result with the appropriate mark
                    return new Result(
                        d[1],  // studentID
                        d[2],  // moduleID
                        mark,  // mark (lecture/tutorial)
                        d.length >= 5 ? d[5] : "" // feedback (if present)
                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return null if no result is found
        return null;
    }
    
    public Class getClassByID(String classID) {
        for (Class c : getClasses()) {
            if (c.getClassID().equals(classID)) {
                return c;
            }
        }
        return null;
    }

    public String[] getModuleResultForStudent(String studentID, String moduleID) {
        try (BufferedReader br = new BufferedReader(new FileReader("moduleResult.txt"))) {
            br.readLine();
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d.length >= 7 && 
                    d[1].equals(studentID) && 
                    d[2].equals(moduleID)) {
                    return new String[]{d[3], d[4], d[5], d[6]}; // lecture, tutorial, final, grade
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{"N/A", "N/A", "N/A", "N/A"};
    }
    
    public List<String[]> getModuleResults(String moduleID) {
        List<String[]> results = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("moduleResult.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");

                if (d.length < 7) continue; // Ensure have all fields

                String studentID = d[1];
                String modID = d[2];
                String lectureMark = d[3];
                String tutorialMark = d[4];
                String finalMark = d[5];
                String grade = d[6];

                // If specific module selected, filter by it
                if (moduleID != null && !moduleID.equals("All Module")) {
                    if (!modID.equals(moduleID)) {
                        continue;
                    }
                } else {
                    // For "All Module", check if module belongs to lecturer
                    List<String> lecturerModules = getModuleIDs();
                    if (!lecturerModules.contains(modID)) {
                        continue;
                    }
                }

                // Get student name
                Student student = Student.findByID(studentID);
                String studentName = (student != null) ? student.getName() : "Unknown";

                results.add(new String[]{
                    studentID,
                    studentName,
                    lectureMark,
                    tutorialMark,
                    finalMark,
                    grade
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    public List<Student> getStudentsForModule(String moduleID) {
        List<Student> students = new ArrayList<>();
        Set<String> studentIDs = new HashSet<>();

        try {
            // Get all classes for this module that the lecturer teaches
            List<Class> moduleClasses = getClassesByModule(moduleID);

            for (Class c : moduleClasses) {
                // Get students in each class
                List<Student> classStudents = getStudentsForClass(c.getClassID());
                for (Student s : classStudents) {
                    if (!studentIDs.contains(s.getStudentID())) {
                        studentIDs.add(s.getStudentID());
                        students.add(s);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return students;
    }
    
    
    // Lect - Analytics
    
    // Assessment Performance
    public Map<String, Integer> getAssessmentMarkDistribution(String moduleID) {
        Map<String, Integer> distribution = new HashMap<>();

        // Initialize with letter grades (for module-level distribution)
        String[] grades = {"A+", "A", "A-", "B+", "B", "B-", "C", "C-", "D", "F+", "F", "AS"};
        for (String grade : grades) {
            distribution.put(grade, 0);
        }

        // Read moduleResult.txt to get final grades for this module
        try (BufferedReader br = new BufferedReader(new FileReader("moduleResult.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d.length < 7) continue;

                String modID = d[2];
                String grade = d[6]; // Final grade is at index 6

                if (modID.equals(moduleID) && !grade.equals("NULL") && !grade.isEmpty()) {
                    distribution.put(grade, distribution.getOrDefault(grade, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return distribution;
    }

    // Mark ranges for an assessment
    public Map<String, Integer> getAssessmentMarkRanges(String assessID) {
        Map<String, Integer> distribution = new HashMap<>();
        distribution.put("≥75", 0);
        distribution.put("50-74", 0);
        distribution.put("<50", 0);

        try (BufferedReader br = new BufferedReader(new FileReader("result.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d.length < 4) continue;

                String resultAssessID = d[2];
                String markStr = d[3];

                if (resultAssessID.equals(assessID) && !markStr.isEmpty() && !markStr.equals("NULL")) {
                    try {
                        double mark = Double.parseDouble(markStr);

                        // Categorize into the three groups
                        if (mark >= 75) {
                            distribution.put("≥75", distribution.get("≥75") + 1);
                        } else if (mark >= 50) {
                            distribution.put("50-74", distribution.get("50-74") + 1);
                        } else {
                            distribution.put("<50", distribution.get("<50") + 1);
                        }
                    } catch (NumberFormatException e) {
                        // Skip invalid marks
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return distribution;
    }

    // Letter grade distribution for a module
    public Map<String, Integer> getModuleGradeDistribution(String moduleID) {
        Map<String, Integer> gradeCount = new HashMap<>();

        // Initialize all grades
        String[] allGrades = {"A+", "A", "A-", "B+", "B", "B-", "C", "C-", "D", "F+", "F", "AS"};
        for (String grade : allGrades) {
            gradeCount.put(grade, 0);
        }

        try (BufferedReader br = new BufferedReader(new FileReader("moduleResult.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d.length < 7) continue;

                String modID = d[2];
                String grade = d[6];

                // Check if module matches and belongs to lecturer
                boolean moduleMatches = moduleID == null || 
                                       moduleID.equals("All Module") || 
                                       modID.equals(moduleID);

                List<String> lecturerModules = getModuleIDs();

                if (moduleMatches && lecturerModules.contains(modID) && !grade.equals("NULL")) {
                    gradeCount.put(grade, gradeCount.getOrDefault(grade, 0) + 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gradeCount;
    }
    
    // Analytics - Assessment Statistics
    public Map<String, Object> getAssessmentStatistics(String assessID) {
        Map<String, Object> stats = new HashMap<>();

        List<Double> marks = new ArrayList<>();
        double total = 0;
        int count = 0;
        double highest = 0;
        double lowest = 100;

        try (BufferedReader br = new BufferedReader(new FileReader("result.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d.length < 4) continue;

                String resultAssessID = d[2];
                String markStr = d[3];

                if (resultAssessID.equals(assessID) && !markStr.isEmpty() && !markStr.equals("NULL")) {
                    try {
                        double mark = Double.parseDouble(markStr);
                        marks.add(mark);
                        total += mark;
                        count++;
                        if (mark > highest) highest = mark;
                        if (mark < lowest) lowest = mark;
                    } catch (NumberFormatException e) {
                        // Skip invalid marks
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (count > 0) {
            double average = total / count;

            // Calculate median
            Collections.sort(marks);
            double median;
            if (marks.size() % 2 == 0) {
                median = (marks.get(marks.size()/2) + marks.get(marks.size()/2 - 1)) / 2;
            } else {
                median = marks.get(marks.size()/2);
            }

            // Calculate standard deviation
            double variance = 0;
            for (double mark : marks) {
                variance += Math.pow(mark - average, 2);
            }
            variance /= marks.size();
            double stdDev = Math.sqrt(variance);

            stats.put("average", average);
            stats.put("median", median);
            stats.put("highest", highest);
            stats.put("lowest", lowest);
            stats.put("count", count);
            stats.put("stdDev", stdDev);
        } else {
            stats.put("average", 0.0);
            stats.put("median", 0.0);
            stats.put("highest", 0.0);
            stats.put("lowest", 0.0);
            stats.put("count", 0);
            stats.put("stdDev", 0.0);
        }

        return stats;
    }

    // Get classes for a specific module
    public List<Class> getClassesForModule(String moduleID) {
        List<Class> moduleClasses = new ArrayList<>();

        try {
            // Get all classes taught by lecturer
            List<Class> allClasses = getClasses();

            for (Class c : allClasses) {
                String classModuleID = getModuleIDFromClass(c.getClassID());
                if (classModuleID != null && classModuleID.equals(moduleID)) {
                    moduleClasses.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return moduleClasses;
    }

    // Get assessments for a specific class
    public List<Assessment> getAssessmentsForClass(String classID) {
        List<Assessment> classAssessments = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("assessment.txt"))) {
            br.readLine(); // skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\s*,\\s*");
                if (d.length < 5) continue;

                String assessID = d[0];
                String assessClassID = d[1];
                String type = d[2];
                double fullMark = Double.parseDouble(d[3]);
                double weightage = Double.parseDouble(d[4]);

                if (assessClassID.equals(classID)) {
                    classAssessments.add(new Assessment(assessID, classID, type, fullMark, weightage));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return classAssessments;
    }

    // Get all assessments for a module (across all classes)
    public List<Assessment> getAllAssessmentsForModule(String moduleID) {
        List<Assessment> moduleAssessments = new ArrayList<>();

        // Get all classes for this module
        List<Class> moduleClasses = getClassesForModule(moduleID);

        for (Class c : moduleClasses) {
            moduleAssessments.addAll(getAssessmentsForClass(c.getClassID()));
        }

        return moduleAssessments;
    }
    
    public Map<String, Integer> getClassGradeDistribution(String classID) {
        Map<String, Integer> gradeCount = new HashMap<>();

        String[] grades = {"A+", "A", "A-", "B+", "B", "B-",
                           "C", "C-", "D", "F+", "F"};

        for (String g : grades) {
            gradeCount.put(g, 0);
        }

        String moduleID = getModuleIDFromClass(classID);

        List<Student> students = getStudentsForClass(classID);

        for (Student s : students) {

            String[] result =
                getModuleResultForStudent(
                    s.getStudentID(), moduleID);

            String markStr;

            if (isLectureClass(classID)) {
                markStr = result[0];
            } else if (isTutorialClass(classID)) {
                markStr = result[1];
            } else {
                continue;
            }

            if (markStr == null ||
                markStr.equals("0.0") ||
                markStr.equals("NULL") ||
                markStr.equals("N/A")) {
                continue;
            }

            double mark;
            try {
                mark = Double.parseDouble(markStr);
            } catch (Exception e) {
                continue;
            }
            String grade =
                ModuleResult.assignGrade(mark);

            gradeCount.put(
                grade,
                gradeCount.getOrDefault(grade, 0) + 1
            );
        }


        return gradeCount;
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
        task.addRowtoTable(table, containLines, 3);
    }
    
    public String getlastIDnumber(){
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("lecturer.txt");
        List<String> lastrow = data.subList(data.size()-1, data.size());
        String row = lastrow.get(0);
        String[] eachline = row.split(", ");
        String lastID = eachline[0];
        return lastID;
    }
    
    public int registerLecturer(String ID, String username, String name, String title, String email, String phoneNo, String acadqual, String acadlead){
        int number = 0;
        List<String> writtendata = new ArrayList<>();
        if (ID.isEmpty() || name.isEmpty() || email.isEmpty() || phoneNo.isEmpty() || title.isEmpty()){
            JOptionPane.showMessageDialog(null, 
                                      "Please fill all required field", 
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
        
        acadlead = acadlead.substring(0, acadlead.indexOf(' '));
        
        if (acadqual.equals("")){
            acadqual = "Other";
        } else{
            acadqual = AdminTask.titleCase(acadqual);
        }
        
        
        //String[] studentrow = {lecturerID, username, name, email, phoneNo, course, null};
        List<String> lectrow = List.of(ID, username, name, title, email, phoneNo, acadqual, acadlead);
        //Object[] userrow = {username, password, "Student"};
        //List<String> userrow = List.of(username, password, "Student");
        
        //add to text file
        //buat for loop > check exist ke tak > kalau tak, append > kalau ya, tukar
        // inner loop
        List<String> data = task.readtxt("lecturer.txt");
        List<String> data2 = task.readtxt("lecturer.txt");
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
                    //System.out.println(!eachline2[0].equals(lecturerID));
                    if(!eachline2[0].equals(ID)){
                       List<String> eachrow = Arrays.asList(row2);
                       containLines.addAll(eachrow);
                    }
                    else{
                        //String[] help = studentrow.toArray(new String[0]);
                        String help = String.join(", ", lectrow);
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
                writtendata = lectrow;
                number = 2; //append
            }
        }
        
        task.writetxt(writtendata, "lecturer.txt", number);
        return 1;
 
    }
    
    public static void addLecttoCmb(JComboBox<String> cmbbox, String moduleID){
        cmbbox.removeAllItems();
        List<String> data = AdminTask.readtxt("moduleLecturer.txt");
        List<String> data2 = AdminTask.readtxt("lecturer.txt");
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                //System.out.println(row);
                String[] eachline = row.split(", ");
                if (eachline[2].equals(moduleID)){
                    String lecturerID = eachline[1];
                    for(int j = 1; j < data2.size(); j++){
                        String row2 = data2.get(j);
                        String[] eachline2 = row2.split(", ");
                        if (eachline2[0].equals(lecturerID)){
                            String lecturername = eachline2[0] + " - " + eachline2[2];
                            cmbbox.addItem(lecturername);
                        }
                    }
                }
            }
    }

    public String toFileString() {
        return lecturerID + ", " + username + ", " + name + ", " + title + ", " +
               email + ", " + phoneNumber + ", " + academicQualification + ", " + acadLeadID;
    }

    public static Lecturer fromFileString(String line) {
        String[] parts = line.split(", ", -1);

        return new Lecturer(
            parts[0].trim(),
            parts[1].trim(),
            parts[2].trim(),
            parts[3].trim(),
            parts[4].trim(),
            parts[5].trim(),
            parts[6].trim(),
            parts[7].trim()
        );
    }

    // Load all lecturers from file
    public static List<Lecturer> loadLecturers(String filePath) {
        List<Lecturer> lecturers = new ArrayList<>();
        List<String> lines = FileFunctions.readAllLines(filePath);

        for (String line : lines) {
            if (line.toLowerCase().startsWith("lecturerid")) continue; // skip header
            try {
                lecturers.add(Lecturer.fromFileString(line));
            } catch (Exception e) {
                System.err.println("Invalid line: " + line);
            }
        }

        return lecturers;
    }

    // SAVE
    public static void saveLecturers(String filePath, List<Lecturer> lecturers) {
        List<String> lines = new ArrayList<>();
        lines.add("lecturerID, username, name, title, email, phoneNumber, academicQualification, acadLeadID"); // keep header
        for (Lecturer l : lecturers) {
            lines.add(l.toFileString());
        }
        FileFunctions.writeAllLines(filePath, lines);
    }

    // ADD NEWW
    public static void addLecturer(String filePath, Lecturer lecturer) {
        FileFunctions.appendLine(filePath, lecturer.toFileString());
    }

    @Override
    public String toString() {
        return "Lecturer{" +
                "lecturerID='" + lecturerID + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", academicQualification='" + academicQualification + '\'' +
                ", acadLeadID='" + acadLeadID + '\'' +
                '}';
    }
    
        public void editLectInit(JComboBox cmbTitle, JComboBox cmbAcadLead, JLabel lblID, JTextField txtUsername, JTextField txtFullName,
            JTextField txtEmail, JTextField txtPhone, JTextField txtPw, JTextField txtacadQual){ 
        String username = "";
        String rowID = getLecturerID();
        
        //List<String> containLines = new ArrayList<>();
        AdminTask task = new AdminTask();
        
        //addcourse in combo box
        cmbAcadLead.removeAllItems();
        AcademicLeader acadlead = new AcademicLeader();
        acadlead.addAcadleadtoCmb(cmbAcadLead);
        
        List<String> data = task.readtxt("lecturer.txt");
                
        for(int i = 1; i < data.size(); i++){
                String row = data.get(i);
                String[] eachline = row.split(", ");
                if(eachline[0].equals(rowID)){
                    lblID.setText("ID: " + eachline[0]);
                    username = eachline[1];
                    txtUsername.setText(username);
                    txtFullName.setText(eachline[2]);
                    txtEmail.setText(eachline[4]);
                    txtPhone.setText(eachline[5]);
                    txtacadQual.setText(eachline[6]);
                    String acadleadID = eachline[7];
                    for (int j = 0; j < cmbAcadLead.getItemCount(); j++) {
                        String item = (cmbAcadLead.getItemAt(j)).toString();
                        String txtID = item.substring(0, item.indexOf(' '));
                        if (acadleadID.equals(txtID)){
                            cmbAcadLead.setSelectedIndex(j);
                            break;
                        }
                    }
                    String title = eachline[3];
                    for (int j = 0; j < cmbTitle.getItemCount(); j++) {
                        String item = (cmbTitle.getItemAt(j)).toString();
                        if (title.equals(item)){
                            cmbTitle.setSelectedIndex(j);
                            break;
                        }
                    }
                }
            }
        
        User user = new User();
        user.setUsername(username);
        user.edituserInit(txtPw);
        
    }
        
    public void deletelect(String rowID){ //hasnt been tested
        String username = "";
        List<String> containLines = new ArrayList<>();
        AdminTask task = new AdminTask();
        List<String> data = task.readtxt("lecturer.txt");
        
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
        
        task.writetxt(containLines, "lecturer.txt", 1);
        User user = new User(); //KENA CARI USERNAME BASED ON studentID
        user.deleteuser(username); 
    }        
    
}
