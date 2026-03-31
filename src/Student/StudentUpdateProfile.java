/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Student;

// my packages
import Classes.FilePath;
import Classes.Session;
import Login.AFS_LoginPage;


// java packages
import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class StudentUpdateProfile extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentUpdateProfile.class.getName());

    /**
     * Creates new form StudentUpdateProfile
     */
    public StudentUpdateProfile() {
        initComponents();
        
        setSize(1031, 643); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        loadHeaderFromSession();
        loadProfileIntoForm();
    }
    
    private void loadHeaderFromSession() {
            String sid = Session.getStudentId();
            sid = (sid == null) ? "" : sid.trim();

            StudentID.setText(!sid.isBlank() ? sid : "TPXXXXXX");
            StudentRole.setText("Student");
        }

    private void loadProfileIntoForm() {
        String sid = Session.getStudentId();
        sid = (sid == null) ? "" : sid.trim();

        if (sid.isBlank()) {
            JOptionPane.showMessageDialog(this, "No active student session.");
            return;
        }

        StudentRow s = findStudentById(sid);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Student not found in student.txt: " + sid);
            return;
        }

        tfStudentId.setText(s.studentId);
        tfUsername.setText(s.username);
        tfName.setText(s.name);
        tfEmail.setText(s.email);
        tfPhone.setText(s.phone);
    }
        
    private boolean updateStudentProfile(String studentId, String newUsername, String newName, String newEmail, String newPhone) {
        Path path = Paths.get(FilePath.STUDENT_FILE);
        if (!Files.exists(path)) {
            JOptionPane.showMessageDialog(this, "Student file not found:\n" + path.toAbsolutePath());
            return false;
        }
        
        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading student file: " + e.getMessage());
            return false;
        }

        boolean updated = false;
        List<String> out = new ArrayList<>();

        for (String line : lines) {
            if (line == null) continue;

            String raw = line;
            line = line.trim();

            if (line.isEmpty()) {
                out.add(raw);
                continue;
            }
            
            if (line.toLowerCase().startsWith("studentid,")) {
                out.add(raw);
                continue;
            }
            
            // studentID, username, name, email, phoneNumber, currentCourse, CGPA
            String[] p = raw.split(",");
            if (p.length < 7) {
                out.add(raw);
                continue;
            }

            String sid = p[0].trim();
            if (!sid.equalsIgnoreCase(studentId)) {
                out.add(raw);
                continue;
            }

            String username = p[1].trim();
            String currentCourse = p[5].trim();
            String cgpa = p[6].trim();

            String newLine = String.join(", ",
                    sid,
                    newUsername,
                    newName,
                    newEmail,
                    newPhone,
                    currentCourse,
                    cgpa
            );

            out.add(newLine);
            updated = true;
        }

        if (!updated) return false;
        
        // write to temp file then replace
        try {
            Path temp = Paths.get(path.toString() + ".tmp");
            Files.write(temp, out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing student file: " + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    private boolean usernameExistsInUserFile(String username) {
        Path path = Paths.get(FilePath.USER_FILE);
        if (!Files.exists(path)) return false;

        try {
            for (String raw : Files.readAllLines(path)) {
                if (raw == null) continue;
                String line = raw.trim();
                if (line.isEmpty()) continue;
                if (line.toLowerCase().startsWith("username,")) continue;

                String[] p = raw.split(",");
                if (p.length < 3) continue;

                if (p[0].trim().equalsIgnoreCase(username)) return true;
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user file: " + e.getMessage());
        }
        return false;
    }

    private boolean updateUserUsername(String oldUsername, String newUsername, String role) {
        Path path = Paths.get(FilePath.USER_FILE);
        if (!Files.exists(path)) {
            JOptionPane.showMessageDialog(this, "User file not found:\n" + path.toAbsolutePath());
            return false;
        }

        List<String> lines;
        try {
            lines = Files.readAllLines(path);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user file: " + e.getMessage());
            return false;
        }

        boolean updated = false;
        List<String> out = new ArrayList<>();

        for (String raw : lines) {
            if (raw == null) continue;

            String line = raw.trim();
            if (line.isEmpty()) {
                out.add(raw);
                continue;
            }
            if (line.toLowerCase().startsWith("username,")) {
                out.add(raw);
                continue;
            }

            String[] p = raw.split(",");
            if (p.length < 3) {
                out.add(raw);
                continue;
            }

            String u = p[0].trim();
            String pass = p[1].trim();
            String r = p[2].trim();

            // update only matching username + role row
            if (u.equalsIgnoreCase(oldUsername) && r.equalsIgnoreCase(role)) {
                String newLine = String.join(", ",
                        newUsername,
                        pass,
                        r
                );
                out.add(newLine);
                updated = true;
            } else {
                out.add(raw);
            }
        }

        if (!updated) {
            JOptionPane.showMessageDialog(this,
                    "Account not found in user.txt for: " + oldUsername + " (" + role + ")");
            return false;
        }

        try {
            Path temp = Paths.get(path.toString() + ".tmp");
            Files.write(temp, out, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing user file: " + e.getMessage());
            return false;
        }

        return true;
    }
    
    private static class StudentRow {
    String studentId, username, name, email, phone, course, cgpa;
}

    private StudentRow findStudentById(String studentId) {
        Path path = Paths.get(FilePath.STUDENT_FILE);
        if (!Files.exists(path)) return null;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.toLowerCase().startsWith("studentid,")) continue;

                String[] p = line.split(",");
                if (p.length < 7) continue;

                String sid = p[0].trim();
                if (!sid.equalsIgnoreCase(studentId)) continue;

                StudentRow s = new StudentRow();
                s.studentId = sid;
                s.username = p[1].trim();
                s.name = p[2].trim();
                s.email = p[3].trim();
                s.phone = p[4].trim();
                s.course = p[5].trim();
                s.cgpa = p[6].trim();
                return s;
            }
        } catch (IOException e) {
            System.err.println("Error reading student file: " + e.getMessage());
        }
        return null;
    }
    
    private boolean isValidEmail(String email) {
        if (email == null) return false;
        email = email.trim();
        if (email.isBlank()) return false;

        // simple assignment-safe email validation
        return email.contains("@") && email.contains(".") && !email.contains(" ");
    }

    private boolean isValidPhone(String phone) {
        if (phone == null) return false;
        phone = phone.trim();
        if (phone.isBlank()) return false;

        // digits only
        if (!phone.matches("\\d+")) return false;

        return phone.length() == 10;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dbBase = new javax.swing.JPanel();
        sidebar = new javax.swing.JPanel();
        gap1 = new javax.swing.JPanel();
        pnlAFSlogo = new javax.swing.JPanel();
        lblAFSlogo = new javax.swing.JLabel();
        gap2 = new javax.swing.JPanel();
        pnlNavDashboard = new javax.swing.JPanel();
        btnNavDashboard = new javax.swing.JButton();
        pnlNavMyClasses = new javax.swing.JPanel();
        btnNavMyClasses = new javax.swing.JButton();
        pnlNavAssessments = new javax.swing.JPanel();
        btnNavAssessments = new javax.swing.JButton();
        pnlNavGrades = new javax.swing.JPanel();
        btnNavGrades = new javax.swing.JButton();
        pnlNavFeedback = new javax.swing.JPanel();
        btnNavFeedback = new javax.swing.JButton();
        pnlNavProfile = new javax.swing.JPanel();
        btnNavProfile = new javax.swing.JButton();
        pnlNavLogout = new javax.swing.JPanel();
        btnNavLogout = new javax.swing.JButton();
        gap3 = new javax.swing.JPanel();
        main = new javax.swing.JPanel();
        top = new javax.swing.JPanel();
        topTitle = new javax.swing.JPanel();
        txtRegisterNewClasses = new javax.swing.JLabel();
        topUserInfo = new javax.swing.JPanel();
        StudentProfilePic = new javax.swing.JLabel();
        StudentRole = new javax.swing.JLabel();
        StudentID = new javax.swing.JLabel();
        content = new javax.swing.JPanel();
        H2UpdateProfile = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();
        ConfirmUpdateButton = new javax.swing.JButton();
        lblStudentIdText = new javax.swing.JLabel();
        tfStudentId = new javax.swing.JTextField();
        lblEmailText = new javax.swing.JLabel();
        tfEmail = new javax.swing.JTextField();
        lblUsernameText = new javax.swing.JLabel();
        tfUsername = new javax.swing.JTextField();
        lblPhoneText = new javax.swing.JLabel();
        tfPhone = new javax.swing.JTextField();
        lblNameText = new javax.swing.JLabel();
        tfName = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        dbBase.setBackground(new java.awt.Color(255, 255, 255));
        dbBase.setLayout(new java.awt.BorderLayout());

        sidebar.setBackground(new java.awt.Color(24, 29, 49));
        sidebar.setPreferredSize(new java.awt.Dimension(209, 643));
        sidebar.setLayout(new javax.swing.BoxLayout(sidebar, javax.swing.BoxLayout.Y_AXIS));

        gap1.setBackground(new java.awt.Color(24, 29, 49));
        gap1.setPreferredSize(new java.awt.Dimension(209, 43));

        javax.swing.GroupLayout gap1Layout = new javax.swing.GroupLayout(gap1);
        gap1.setLayout(gap1Layout);
        gap1Layout.setHorizontalGroup(
            gap1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );
        gap1Layout.setVerticalGroup(
            gap1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        sidebar.add(gap1);

        pnlAFSlogo.setBackground(new java.awt.Color(24, 29, 49));

        lblAFSlogo.setBackground(new java.awt.Color(24, 29, 49));
        lblAFSlogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/AFS Logo.png"))); // NOI18N
        lblAFSlogo.setText("jLabel1");

        javax.swing.GroupLayout pnlAFSlogoLayout = new javax.swing.GroupLayout(pnlAFSlogo);
        pnlAFSlogo.setLayout(pnlAFSlogoLayout);
        pnlAFSlogoLayout.setHorizontalGroup(
            pnlAFSlogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAFSlogoLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblAFSlogo, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        pnlAFSlogoLayout.setVerticalGroup(
            pnlAFSlogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAFSlogoLayout.createSequentialGroup()
                .addComponent(lblAFSlogo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidebar.add(pnlAFSlogo);

        gap2.setBackground(new java.awt.Color(24, 29, 49));
        gap2.setPreferredSize(new java.awt.Dimension(209, 43));

        javax.swing.GroupLayout gap2Layout = new javax.swing.GroupLayout(gap2);
        gap2.setLayout(gap2Layout);
        gap2Layout.setHorizontalGroup(
            gap2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );
        gap2Layout.setVerticalGroup(
            gap2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );

        sidebar.add(gap2);

        pnlNavDashboard.setBackground(new java.awt.Color(24, 29, 49));
        pnlNavDashboard.setPreferredSize(new java.awt.Dimension(209, 40));

        btnNavDashboard.setBackground(new java.awt.Color(24, 29, 49));
        btnNavDashboard.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNavDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnNavDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconDb.png"))); // NOI18N
        btnNavDashboard.setText("   Dashboard");
        btnNavDashboard.setBorder(null);
        btnNavDashboard.setBorderPainted(false);
        btnNavDashboard.setContentAreaFilled(false);
        btnNavDashboard.setFocusPainted(false);
        btnNavDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNavDashboard.setPreferredSize(new java.awt.Dimension(92, 40));
        btnNavDashboard.setVerifyInputWhenFocusTarget(false);
        btnNavDashboard.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnNavDashboard.addActionListener(this::btnNavDashboardActionPerformed);

        javax.swing.GroupLayout pnlNavDashboardLayout = new javax.swing.GroupLayout(pnlNavDashboard);
        pnlNavDashboard.setLayout(pnlNavDashboardLayout);
        pnlNavDashboardLayout.setHorizontalGroup(
            pnlNavDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavDashboardLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(btnNavDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(57, Short.MAX_VALUE))
        );
        pnlNavDashboardLayout.setVerticalGroup(
            pnlNavDashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNavDashboardLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnNavDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        sidebar.add(pnlNavDashboard);

        pnlNavMyClasses.setBackground(new java.awt.Color(24, 29, 49));
        pnlNavMyClasses.setPreferredSize(new java.awt.Dimension(209, 40));

        btnNavMyClasses.setBackground(new java.awt.Color(24, 29, 49));
        btnNavMyClasses.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNavMyClasses.setForeground(new java.awt.Color(255, 255, 255));
        btnNavMyClasses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconClass.png"))); // NOI18N
        btnNavMyClasses.setText("   My Classes");
        btnNavMyClasses.setBorder(null);
        btnNavMyClasses.setBorderPainted(false);
        btnNavMyClasses.setContentAreaFilled(false);
        btnNavMyClasses.setFocusPainted(false);
        btnNavMyClasses.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNavMyClasses.addActionListener(this::btnNavMyClassesActionPerformed);

        javax.swing.GroupLayout pnlNavMyClassesLayout = new javax.swing.GroupLayout(pnlNavMyClasses);
        pnlNavMyClasses.setLayout(pnlNavMyClassesLayout);
        pnlNavMyClassesLayout.setHorizontalGroup(
            pnlNavMyClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavMyClassesLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnNavMyClasses)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNavMyClassesLayout.setVerticalGroup(
            pnlNavMyClassesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavMyClassesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnNavMyClasses, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidebar.add(pnlNavMyClasses);

        pnlNavAssessments.setBackground(new java.awt.Color(20, 24, 49));
        pnlNavAssessments.setPreferredSize(new java.awt.Dimension(209, 40));

        btnNavAssessments.setBackground(new java.awt.Color(24, 29, 49));
        btnNavAssessments.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNavAssessments.setForeground(new java.awt.Color(255, 255, 255));
        btnNavAssessments.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconAss.png"))); // NOI18N
        btnNavAssessments.setText("  Assessments");
        btnNavAssessments.setBorder(null);
        btnNavAssessments.setBorderPainted(false);
        btnNavAssessments.setContentAreaFilled(false);
        btnNavAssessments.setFocusPainted(false);
        btnNavAssessments.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNavAssessments.addActionListener(this::btnNavAssessmentsActionPerformed);

        javax.swing.GroupLayout pnlNavAssessmentsLayout = new javax.swing.GroupLayout(pnlNavAssessments);
        pnlNavAssessments.setLayout(pnlNavAssessmentsLayout);
        pnlNavAssessmentsLayout.setHorizontalGroup(
            pnlNavAssessmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavAssessmentsLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnNavAssessments, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(60, Short.MAX_VALUE))
        );
        pnlNavAssessmentsLayout.setVerticalGroup(
            pnlNavAssessmentsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNavAssessmentsLayout.createSequentialGroup()
                .addComponent(btnNavAssessments, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sidebar.add(pnlNavAssessments);

        pnlNavGrades.setBackground(new java.awt.Color(24, 29, 49));
        pnlNavGrades.setPreferredSize(new java.awt.Dimension(209, 40));

        btnNavGrades.setBackground(new java.awt.Color(24, 29, 49));
        btnNavGrades.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNavGrades.setForeground(new java.awt.Color(255, 255, 255));
        btnNavGrades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconGrading.png"))); // NOI18N
        btnNavGrades.setText("   Grades");
        btnNavGrades.setBorder(null);
        btnNavGrades.setBorderPainted(false);
        btnNavGrades.setContentAreaFilled(false);
        btnNavGrades.setFocusPainted(false);
        btnNavGrades.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNavGrades.addActionListener(this::btnNavGradesActionPerformed);

        javax.swing.GroupLayout pnlNavGradesLayout = new javax.swing.GroupLayout(pnlNavGrades);
        pnlNavGrades.setLayout(pnlNavGradesLayout);
        pnlNavGradesLayout.setHorizontalGroup(
            pnlNavGradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavGradesLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(btnNavGrades)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNavGradesLayout.setVerticalGroup(
            pnlNavGradesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavGradesLayout.createSequentialGroup()
                .addComponent(btnNavGrades, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sidebar.add(pnlNavGrades);

        pnlNavFeedback.setBackground(new java.awt.Color(24, 29, 49));
        pnlNavFeedback.setPreferredSize(new java.awt.Dimension(209, 40));

        btnNavFeedback.setBackground(new java.awt.Color(24, 29, 49));
        btnNavFeedback.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNavFeedback.setForeground(new java.awt.Color(255, 255, 255));
        btnNavFeedback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconFeedback.png"))); // NOI18N
        btnNavFeedback.setText("   Feedback");
        btnNavFeedback.setBorder(null);
        btnNavFeedback.setBorderPainted(false);
        btnNavFeedback.setContentAreaFilled(false);
        btnNavFeedback.setFocusPainted(false);
        btnNavFeedback.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNavFeedback.addActionListener(this::btnNavFeedbackActionPerformed);

        javax.swing.GroupLayout pnlNavFeedbackLayout = new javax.swing.GroupLayout(pnlNavFeedback);
        pnlNavFeedback.setLayout(pnlNavFeedbackLayout);
        pnlNavFeedbackLayout.setHorizontalGroup(
            pnlNavFeedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavFeedbackLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnNavFeedback)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNavFeedbackLayout.setVerticalGroup(
            pnlNavFeedbackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNavFeedbackLayout.createSequentialGroup()
                .addComponent(btnNavFeedback, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sidebar.add(pnlNavFeedback);

        pnlNavProfile.setBackground(new java.awt.Color(24, 29, 49));
        pnlNavProfile.setPreferredSize(new java.awt.Dimension(209, 40));

        btnNavProfile.setBackground(new java.awt.Color(24, 29, 49));
        btnNavProfile.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNavProfile.setForeground(new java.awt.Color(166, 199, 155));
        btnNavProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ClickPf.png"))); // NOI18N
        btnNavProfile.setText("   Profile");
        btnNavProfile.setBorder(null);
        btnNavProfile.setBorderPainted(false);
        btnNavProfile.setContentAreaFilled(false);
        btnNavProfile.setFocusPainted(false);
        btnNavProfile.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNavProfile.addActionListener(this::btnNavProfileActionPerformed);

        javax.swing.GroupLayout pnlNavProfileLayout = new javax.swing.GroupLayout(pnlNavProfile);
        pnlNavProfile.setLayout(pnlNavProfileLayout);
        pnlNavProfileLayout.setHorizontalGroup(
            pnlNavProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavProfileLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnNavProfile)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNavProfileLayout.setVerticalGroup(
            pnlNavProfileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNavProfileLayout.createSequentialGroup()
                .addComponent(btnNavProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sidebar.add(pnlNavProfile);

        pnlNavLogout.setBackground(new java.awt.Color(24, 29, 49));
        pnlNavLogout.setPreferredSize(new java.awt.Dimension(209, 40));

        btnNavLogout.setBackground(new java.awt.Color(24, 29, 49));
        btnNavLogout.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNavLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnNavLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconLogout.png"))); // NOI18N
        btnNavLogout.setText("   Logout");
        btnNavLogout.setAutoscrolls(true);
        btnNavLogout.setBorder(null);
        btnNavLogout.setBorderPainted(false);
        btnNavLogout.setContentAreaFilled(false);
        btnNavLogout.setFocusPainted(false);
        btnNavLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNavLogout.addActionListener(this::btnNavLogoutActionPerformed);

        javax.swing.GroupLayout pnlNavLogoutLayout = new javax.swing.GroupLayout(pnlNavLogout);
        pnlNavLogout.setLayout(pnlNavLogoutLayout);
        pnlNavLogoutLayout.setHorizontalGroup(
            pnlNavLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNavLogoutLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(btnNavLogout)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNavLogoutLayout.setVerticalGroup(
            pnlNavLogoutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNavLogoutLayout.createSequentialGroup()
                .addComponent(btnNavLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        sidebar.add(pnlNavLogout);

        gap3.setBackground(new java.awt.Color(24, 29, 49));
        gap3.setPreferredSize(new java.awt.Dimension(209, 800));

        javax.swing.GroupLayout gap3Layout = new javax.swing.GroupLayout(gap3);
        gap3.setLayout(gap3Layout);
        gap3Layout.setHorizontalGroup(
            gap3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 209, Short.MAX_VALUE)
        );
        gap3Layout.setVerticalGroup(
            gap3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 266, Short.MAX_VALUE)
        );

        sidebar.add(gap3);

        dbBase.add(sidebar, java.awt.BorderLayout.WEST);

        main.setBackground(new java.awt.Color(255, 255, 255));
        main.setLayout(new java.awt.BorderLayout());

        top.setBackground(new java.awt.Color(255, 255, 255));
        top.setPreferredSize(new java.awt.Dimension(822, 80));
        top.setLayout(new java.awt.BorderLayout());

        topTitle.setBackground(new java.awt.Color(255, 255, 255));
        topTitle.setPreferredSize(new java.awt.Dimension(100, 80));

        txtRegisterNewClasses.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        txtRegisterNewClasses.setText("Profile");

        javax.swing.GroupLayout topTitleLayout = new javax.swing.GroupLayout(topTitle);
        topTitle.setLayout(topTitleLayout);
        topTitleLayout.setHorizontalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topTitleLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtRegisterNewClasses)
                .addContainerGap(577, Short.MAX_VALUE))
        );
        topTitleLayout.setVerticalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topTitleLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtRegisterNewClasses, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );

        top.add(topTitle, java.awt.BorderLayout.CENTER);

        topUserInfo.setBackground(new java.awt.Color(255, 255, 255));
        topUserInfo.setPreferredSize(new java.awt.Dimension(150, 80));

        StudentProfilePic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/User Icon.png"))); // NOI18N
        StudentProfilePic.setText("jLabel2");

        StudentRole.setFont(new java.awt.Font("Arial", 0, 9)); // NOI18N
        StudentRole.setText("Student");

        StudentID.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        StudentID.setText("TPXXXXXX");

        javax.swing.GroupLayout topUserInfoLayout = new javax.swing.GroupLayout(topUserInfo);
        topUserInfo.setLayout(topUserInfoLayout);
        topUserInfoLayout.setHorizontalGroup(
            topUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topUserInfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(StudentProfilePic, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(topUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StudentID, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                    .addGroup(topUserInfoLayout.createSequentialGroup()
                        .addComponent(StudentRole, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        topUserInfoLayout.setVerticalGroup(
            topUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topUserInfoLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(topUserInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(StudentProfilePic, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(topUserInfoLayout.createSequentialGroup()
                        .addComponent(StudentID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StudentRole)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        top.add(topUserInfo, java.awt.BorderLayout.EAST);

        main.add(top, java.awt.BorderLayout.NORTH);

        content.setBackground(new java.awt.Color(255, 255, 255));
        content.setPreferredSize(new java.awt.Dimension(822, 563));

        H2UpdateProfile.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        H2UpdateProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MPButtonUpdateProfile.png"))); // NOI18N
        H2UpdateProfile.setText("Update Profile");
        H2UpdateProfile.setToolTipText("");
        H2UpdateProfile.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        CancelButton.setBackground(new java.awt.Color(204, 222, 125));
        CancelButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        CancelButton.setText("Cancel");
        CancelButton.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        CancelButton.setFocusPainted(false);
        CancelButton.setFocusable(false);
        CancelButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        CancelButton.setIconTextGap(-10);
        CancelButton.setOpaque(true);
        CancelButton.addActionListener(this::CancelButtonActionPerformed);

        ConfirmUpdateButton.setBackground(new java.awt.Color(204, 222, 125));
        ConfirmUpdateButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        ConfirmUpdateButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/MPButtonConfirm.png"))); // NOI18N
        ConfirmUpdateButton.setText("Confirm Update");
        ConfirmUpdateButton.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ConfirmUpdateButton.setFocusPainted(false);
        ConfirmUpdateButton.setFocusable(false);
        ConfirmUpdateButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        ConfirmUpdateButton.setIconTextGap(-13);
        ConfirmUpdateButton.setOpaque(true);
        ConfirmUpdateButton.addActionListener(this::ConfirmUpdateButtonActionPerformed);

        lblStudentIdText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblStudentIdText.setForeground(new java.awt.Color(102, 102, 102));
        lblStudentIdText.setText("Student ID");

        tfStudentId.setEditable(false);
        tfStudentId.setBackground(new java.awt.Color(255, 255, 255));
        tfStudentId.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        tfStudentId.setText("jTextField1");
        tfStudentId.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        tfStudentId.setFocusable(false);
        tfStudentId.setOpaque(true);
        tfStudentId.addActionListener(this::tfStudentIdActionPerformed);

        lblEmailText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblEmailText.setForeground(new java.awt.Color(102, 102, 102));
        lblEmailText.setText("Email");

        tfEmail.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tfEmail.setText("jTextField1");
        tfEmail.addActionListener(this::tfEmailActionPerformed);

        lblUsernameText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblUsernameText.setForeground(new java.awt.Color(102, 102, 102));
        lblUsernameText.setText("Username");

        tfUsername.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tfUsername.setText("jTextField1");
        tfUsername.addActionListener(this::tfUsernameActionPerformed);

        lblPhoneText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblPhoneText.setForeground(new java.awt.Color(102, 102, 102));
        lblPhoneText.setText("Phone Number");

        tfPhone.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tfPhone.setText("jTextField1");
        tfPhone.addActionListener(this::tfPhoneActionPerformed);

        lblNameText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblNameText.setForeground(new java.awt.Color(102, 102, 102));
        lblNameText.setText("Full Name");

        tfName.setEditable(false);
        tfName.setBackground(new java.awt.Color(255, 255, 255));
        tfName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tfName.setText("jTextField1");
        tfName.setFocusable(false);
        tfName.addActionListener(this::tfNameActionPerformed);

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                .addContainerGap(80, Short.MAX_VALUE)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfStudentId, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStudentIdText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEmailText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(contentLayout.createSequentialGroup()
                                .addComponent(lblUsernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(242, 242, 242))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                                .addComponent(CancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)))
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPhoneText, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ConfirmUpdateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(H2UpdateProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(H2UpdateProfile, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(contentLayout.createSequentialGroup()
                                .addComponent(lblStudentIdText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(tfStudentId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(contentLayout.createSequentialGroup()
                                .addComponent(lblEmailText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(tfEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addComponent(lblUsernameText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(tfUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(contentLayout.createSequentialGroup()
                        .addComponent(lblPhoneText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(tfPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(lblNameText, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(tfName, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ConfirmUpdateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(175, Short.MAX_VALUE))
        );

        main.add(content, java.awt.BorderLayout.LINE_END);

        dbBase.add(main, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dbBase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dbBase, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnNavDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavDashboardActionPerformed
        new StudentDashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnNavDashboardActionPerformed

    private void btnNavMyClassesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavMyClassesActionPerformed
        new StudentMyClasses().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnNavMyClassesActionPerformed

    private void btnNavAssessmentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavAssessmentsActionPerformed
        new StudentAssessments().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnNavAssessmentsActionPerformed

    private void btnNavGradesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavGradesActionPerformed
        new StudentGrades().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnNavGradesActionPerformed

    private void btnNavFeedbackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavFeedbackActionPerformed
        new StudentFeedback().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnNavFeedbackActionPerformed

    private void btnNavProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavProfileActionPerformed
        new StudentProfile().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnNavProfileActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this,
            "No data changed.",
            "Cancel Update",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );

        StudentProfile p = new StudentProfile();
        p.setLocationRelativeTo(this);
        p.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_CancelButtonActionPerformed

    private void ConfirmUpdateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmUpdateButtonActionPerformed
        String sid = tfStudentId.getText().trim();
        if (sid.isBlank()) {
            JOptionPane.showMessageDialog(this, "Missing Student ID.");
            return;
        }

        // Get current record FIRST (old username)
        StudentRow current = findStudentById(sid);
        if (current == null) {
            JOptionPane.showMessageDialog(this, "Student not found: " + sid);
            return;
        }
        String oldUsername = current.username == null ? "" : current.username.trim();

        // read inputs
        String name = tfName.getText() == null ? "" : tfName.getText().trim();
        String email = tfEmail.getText() == null ? "" : tfEmail.getText().trim();
        String phone = tfPhone.getText() == null ? "" : tfPhone.getText().trim();
        String username = tfUsername.getText() == null ? "" : tfUsername.getText().trim();

        // validation
        if (name.isBlank()) {
            JOptionPane.showMessageDialog(this, "Full Name cannot be empty.");
            return;
        }

        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email.");
            return;
        }

        if (!isValidPhone(phone)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid phone number (digits only, 10 digits).");
            return;
        }

        if (username.isBlank()) {
            JOptionPane.showMessageDialog(this, "Username cannot be empty.");
            return;
        }

        if (!oldUsername.equalsIgnoreCase(username)) {
            if (usernameExistsInUserFile(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists. Choose another.");
                return;
            }
        }

        // update student.txt
        boolean studentUpdated = updateStudentProfile(sid, username, name, email, phone);
        if (!studentUpdated) {
            JOptionPane.showMessageDialog(this, "Update failed. Student ID not found: " + sid);
            return;
        }

        // update user.txt if username changed
        if (!oldUsername.equalsIgnoreCase(username)) {
            boolean userUpdated = updateUserUsername(oldUsername, username, "Student");

            Session.setUsername(username);
        }

        JOptionPane.showMessageDialog(this, "Profile updated successfully.");
        new StudentProfile().setVisible(true);
        dispose();

    }//GEN-LAST:event_ConfirmUpdateButtonActionPerformed

    private void tfStudentIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfStudentIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfStudentIdActionPerformed

    private void tfEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfEmailActionPerformed

    private void tfUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfUsernameActionPerformed

    private void tfPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfPhoneActionPerformed

    private void tfNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfNameActionPerformed

    private void btnNavLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNavLogoutActionPerformed
        int result = JOptionPane.showConfirmDialog(null, 
                "Logging out?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            new AFS_LoginPage().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnNavLogoutActionPerformed

    /**
     * @param args the command line arguments
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JButton ConfirmUpdateButton;
    private javax.swing.JLabel H2UpdateProfile;
    private javax.swing.JLabel StudentID;
    private javax.swing.JLabel StudentProfilePic;
    private javax.swing.JLabel StudentRole;
    private javax.swing.JButton btnNavAssessments;
    private javax.swing.JButton btnNavDashboard;
    private javax.swing.JButton btnNavFeedback;
    private javax.swing.JButton btnNavGrades;
    private javax.swing.JButton btnNavLogout;
    private javax.swing.JButton btnNavMyClasses;
    private javax.swing.JButton btnNavProfile;
    private javax.swing.JPanel content;
    private javax.swing.JPanel dbBase;
    private javax.swing.JPanel gap1;
    private javax.swing.JPanel gap2;
    private javax.swing.JPanel gap3;
    private javax.swing.JLabel lblAFSlogo;
    private javax.swing.JLabel lblEmailText;
    private javax.swing.JLabel lblNameText;
    private javax.swing.JLabel lblPhoneText;
    private javax.swing.JLabel lblStudentIdText;
    private javax.swing.JLabel lblUsernameText;
    private javax.swing.JPanel main;
    private javax.swing.JPanel pnlAFSlogo;
    private javax.swing.JPanel pnlNavAssessments;
    private javax.swing.JPanel pnlNavDashboard;
    private javax.swing.JPanel pnlNavFeedback;
    private javax.swing.JPanel pnlNavGrades;
    private javax.swing.JPanel pnlNavLogout;
    private javax.swing.JPanel pnlNavMyClasses;
    private javax.swing.JPanel pnlNavProfile;
    private javax.swing.JPanel sidebar;
    private javax.swing.JTextField tfEmail;
    private javax.swing.JTextField tfName;
    private javax.swing.JTextField tfPhone;
    private javax.swing.JTextField tfStudentId;
    private javax.swing.JTextField tfUsername;
    private javax.swing.JPanel top;
    private javax.swing.JPanel topTitle;
    private javax.swing.JPanel topUserInfo;
    private javax.swing.JLabel txtRegisterNewClasses;
    // End of variables declaration//GEN-END:variables
}
