/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Student;

// my packages
import Classes.Session;
import Classes.FilePath;

import Classes.ClassData;
import Classes.RegisteredClassData;
import Classes.CommentData;

import Classes.ClassInfo;
import Classes.RegisteredClass;
import Classes.Comment;
import Login.AFS_LoginPage;

// java packages
import javax.swing.*;
import java.util.List;

public class StudentFeedbackForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentFeedbackForm.class.getName());

    /**
     * Creates new form StudentFeedbackForm
     */
    public StudentFeedbackForm() {
        initComponents();
        
        setSize(1031, 643); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        loadHeaderFromSession();
        loadFormContext();
    }
    
    private void loadHeaderFromSession() {
        String sid = Session.getStudentId();
        sid = (sid == null) ? "" : sid.trim();

        StudentID.setText(!sid.isBlank() ? sid : "TPXXXXXX");
        StudentRole.setText("Student");
    }
    
    private void loadFormContext() {
        tfClass.setEditable(false);
        tfLecturer.setEditable(false);

        String classId = Session.getSelectedClassId();
        classId = (classId == null) ? "" : classId.trim();

        if (classId.isBlank()) {
            tfClass.setText("");
            tfLecturer.setText("");
            JOptionPane.showMessageDialog(this,
                    "No class selected.\nPlease select a class from Feedback page first.",
                    "Missing Class",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // display class name
        ClassData classData = new ClassData();
        ClassInfo ci = classData.findById(classId);

        tfClass.setText(ci == null ? classId : ci.getName());

        // lecturer
        tfLecturer.setText(resolveLecturerNameForClass(classId));
    }
    
    
    
    private String generateNextCommentId(CommentData commentData) {
        List<Comment> all = commentData.readAll();
        int max = 0;

        if (all != null) {
            for (Comment c : all) {
                if (c == null || c.getCommentID() == null) continue;

                String id = c.getCommentID().trim();

                if (!id.toUpperCase().startsWith("C")) continue;

                String digits = id.replaceAll("\\D+", "");
                if (!digits.isEmpty()) {
                    try {
                        max = Math.max(max, Integer.parseInt(digits));
                    } catch (NumberFormatException ignored) {}
                }
            }
        }

        return String.format("C%04d", max + 1);
    }
    
    private String resolveLecturerNameForClass(String classId) {
        try {
            // class.txt -> moduleLectID
            String moduleLectId = null;
            try (var br = java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get(FilePath.CLASS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.toLowerCase().startsWith("classid,")) continue;
                    String[] p = line.split(",");
                    if (p.length < 4) continue;

                    if (p[0].trim().equalsIgnoreCase(classId)) {
                        moduleLectId = p[3].trim();
                        break;
                    }
                }
            }
            if (moduleLectId == null || moduleLectId.isBlank()) return "-";

            // moduleLecturer.txt -> lecturerID
            String lecturerId = null;
            try (var br = java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get(FilePath.MODULE_LECTURER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.toLowerCase().startsWith("modlectid,")) continue;
                    String[] p = line.split(",");
                    if (p.length < 2) continue;

                    if (p[0].trim().equalsIgnoreCase(moduleLectId)) {
                        lecturerId = p[1].trim();
                        break;
                    }
                }
            }
            if (lecturerId == null || lecturerId.isBlank()) return "-";

            // lecturer.txt -> title + name
            try (var br = java.nio.file.Files.newBufferedReader(java.nio.file.Paths.get(FilePath.LECTURER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.toLowerCase().startsWith("lecturerid,")) continue;
                    String[] p = line.split(",");
                    if (p.length < 4) continue;

                    if (p[0].trim().equalsIgnoreCase(lecturerId)) {
                        String name = p[2].trim();
                        String title = p[3].trim();
                        if (title.equalsIgnoreCase("NULL")) title = "";
                        return (title.isBlank() ? name : (title + " " + name)).trim();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Lecturer resolve error: " + e.getMessage());
        }
        return "-";
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
        txtCreateComment = new javax.swing.JLabel();
        topUserInfo = new javax.swing.JPanel();
        StudentProfilePic = new javax.swing.JLabel();
        StudentRole = new javax.swing.JLabel();
        StudentID = new javax.swing.JLabel();
        content = new javax.swing.JPanel();
        lblClass = new javax.swing.JLabel();
        lblLect = new javax.swing.JLabel();
        tfClass = new javax.swing.JTextField();
        tfLecturer = new javax.swing.JTextField();
        lblWriteComment = new javax.swing.JLabel();
        spWriteComment = new javax.swing.JScrollPane();
        taWriteComment = new javax.swing.JTextArea();
        btnSubmitComment = new javax.swing.JButton();
        btnCancelComment = new javax.swing.JButton();

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
        btnNavFeedback.setForeground(new java.awt.Color(166, 199, 155));
        btnNavFeedback.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ClickFb.png"))); // NOI18N
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
        btnNavProfile.setForeground(new java.awt.Color(255, 255, 255));
        btnNavProfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconProfile.png"))); // NOI18N
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

        txtCreateComment.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        txtCreateComment.setText("New Feedback");

        javax.swing.GroupLayout topTitleLayout = new javax.swing.GroupLayout(topTitle);
        topTitle.setLayout(topTitleLayout);
        topTitleLayout.setHorizontalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topTitleLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtCreateComment)
                .addContainerGap(489, Short.MAX_VALUE))
        );
        topTitleLayout.setVerticalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topTitleLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtCreateComment, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        lblClass.setBackground(new java.awt.Color(255, 255, 255));
        lblClass.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblClass.setText("Class:");
        lblClass.setPreferredSize(new java.awt.Dimension(90, 24));

        lblLect.setBackground(new java.awt.Color(255, 255, 255));
        lblLect.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblLect.setText("Lecturer:");
        lblLect.setPreferredSize(new java.awt.Dimension(90, 24));

        tfClass.setEditable(false);
        tfClass.setBackground(new java.awt.Color(255, 255, 255));
        tfClass.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        tfClass.addActionListener(this::tfClassActionPerformed);

        tfLecturer.setEditable(false);
        tfLecturer.setBackground(new java.awt.Color(255, 255, 255));
        tfLecturer.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        tfLecturer.addActionListener(this::tfLecturerActionPerformed);

        lblWriteComment.setBackground(new java.awt.Color(255, 255, 255));
        lblWriteComment.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblWriteComment.setText("Write your comment here:");
        lblWriteComment.setPreferredSize(new java.awt.Dimension(90, 24));

        taWriteComment.setColumns(20);
        taWriteComment.setFont(new java.awt.Font("Arial", 0, 15)); // NOI18N
        taWriteComment.setLineWrap(true);
        taWriteComment.setRows(5);
        taWriteComment.setWrapStyleWord(true);
        spWriteComment.setViewportView(taWriteComment);

        btnSubmitComment.setBackground(new java.awt.Color(153, 204, 0));
        btnSubmitComment.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnSubmitComment.setText("Submit");
        btnSubmitComment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        btnSubmitComment.setBorderPainted(false);
        btnSubmitComment.setFocusPainted(false);
        btnSubmitComment.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSubmitComment.addActionListener(this::btnSubmitCommentActionPerformed);

        btnCancelComment.setBackground(new java.awt.Color(255, 102, 51));
        btnCancelComment.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnCancelComment.setText("Cancel");
        btnCancelComment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        btnCancelComment.setBorderPainted(false);
        btnCancelComment.setFocusPainted(false);
        btnCancelComment.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnCancelComment.addActionListener(this::btnCancelCommentActionPerformed);

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spWriteComment, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblWriteComment, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblLect, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(lblClass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tfClass, javax.swing.GroupLayout.DEFAULT_SIZE, 525, Short.MAX_VALUE)
                            .addComponent(tfLecturer))))
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(contentLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSubmitComment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(btnCancelComment, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(301, 301, 301))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblClass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLect, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tfLecturer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(lblWriteComment, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(spWriteComment, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancelComment, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSubmitComment, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(137, Short.MAX_VALUE))
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

    private void tfClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfClassActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfClassActionPerformed

    private void tfLecturerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfLecturerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfLecturerActionPerformed

    private void btnSubmitCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSubmitCommentActionPerformed
        if (!Session.isLoggedIn() || Session.getStudentId() == null) {
            JOptionPane.showMessageDialog(this, "No active student session.");
            return;
        }

        String studentId = Session.getStudentId().trim();
        String classId = Session.getSelectedClassId();
        classId = (classId == null) ? "" : classId.trim();

        if (classId.isBlank()) {
            JOptionPane.showMessageDialog(this, "No class selected.");
            return;
        }

        String commentText = taWriteComment.getText();
        commentText = (commentText == null) ? "" : commentText.trim();

        if (commentText.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please write a comment before submitting.");
            return;
        }

        RegisteredClassData rcData = new RegisteredClassData();
        List<RegisteredClass> regs = rcData.findByStudentId(studentId);

        String registeredClassId = null;
        if (regs != null) {
            for (RegisteredClass rc : regs) {
                if (rc == null) continue;
                if (rc.getClassID() == null || rc.getRegisteredClassID() == null) continue;

                if (rc.getClassID().trim().equalsIgnoreCase(classId)) {
                    registeredClassId = rc.getRegisteredClassID().trim();
                    break;
                }
            }
        }

        if (registeredClassId == null) {
            JOptionPane.showMessageDialog(this,
                    "You are not registered to this class.\nCannot submit comment.",
                    "Not Registered",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // append comment
        CommentData commentData = new CommentData();
        String newId = generateNextCommentId(commentData);

        Comment c = new Comment(newId, registeredClassId, commentText);
        commentData.append(c);

        JOptionPane.showMessageDialog(this, "Comment submitted successfully.");

        // return to Feedback page
        new StudentFeedback().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnSubmitCommentActionPerformed

    private void btnCancelCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelCommentActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this,
            "No comment has been made.",
            "Cancel Comment",
            javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
        
        new StudentFeedback().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnCancelCommentActionPerformed

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
    private javax.swing.JLabel StudentID;
    private javax.swing.JLabel StudentProfilePic;
    private javax.swing.JLabel StudentRole;
    private javax.swing.JButton btnCancelComment;
    private javax.swing.JButton btnNavAssessments;
    private javax.swing.JButton btnNavDashboard;
    private javax.swing.JButton btnNavFeedback;
    private javax.swing.JButton btnNavGrades;
    private javax.swing.JButton btnNavLogout;
    private javax.swing.JButton btnNavMyClasses;
    private javax.swing.JButton btnNavProfile;
    private javax.swing.JButton btnSubmitComment;
    private javax.swing.JPanel content;
    private javax.swing.JPanel dbBase;
    private javax.swing.JPanel gap1;
    private javax.swing.JPanel gap2;
    private javax.swing.JPanel gap3;
    private javax.swing.JLabel lblAFSlogo;
    private javax.swing.JLabel lblClass;
    private javax.swing.JLabel lblLect;
    private javax.swing.JLabel lblWriteComment;
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
    private javax.swing.JScrollPane spWriteComment;
    private javax.swing.JTextArea taWriteComment;
    private javax.swing.JTextField tfClass;
    private javax.swing.JTextField tfLecturer;
    private javax.swing.JPanel top;
    private javax.swing.JPanel topTitle;
    private javax.swing.JPanel topUserInfo;
    private javax.swing.JLabel txtCreateComment;
    // End of variables declaration//GEN-END:variables
}
