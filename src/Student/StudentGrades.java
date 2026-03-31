/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Student;

// my packages
import Classes.ModuleResultData;
import Classes.StudentData;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import Classes.FilePath;
import Classes.Session;

// java packages
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import Classes.ModuleResult;
import Classes.Student;
import Login.AFS_LoginPage;

public class StudentGrades extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentGrades.class.getName());

    /**
     * Creates new form StudentGrades
     */
    public StudentGrades() {
        initComponents();
        
        setSize(1031, 643); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        spModuleResults.getVerticalScrollBar().setUnitIncrement(18);
        
        loadHeaderFromSession();
        loadGradesPage();
        TableStyle(tblModuleResults);
        
        // table columns
        // Column widths (optional but makes it match mockup)
        tblModuleResults.getColumnModel().getColumn(0).setPreferredWidth(250); // module name
        tblModuleResults.getColumnModel().getColumn(1).setPreferredWidth(80);  // lecture
        tblModuleResults.getColumnModel().getColumn(2).setPreferredWidth(80);  // lab
        tblModuleResults.getColumnModel().getColumn(3).setPreferredWidth(90);  // final
        tblModuleResults.getColumnModel().getColumn(4).setPreferredWidth(80);  // grade
        tblModuleResults.getColumnModel().getColumn(5).setPreferredWidth(100); // status
        spModuleResults.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    private void loadHeaderFromSession() {
        String sid = Session.getStudentId();
        sid = (sid == null) ? "" : sid.trim();

        StudentID.setText(!sid.isBlank() ? sid : "TPXXXXXX");
        StudentRole.setText("Student");
    }
    
    private void loadGradesPage() {
        String studentId = Session.getStudentId();
        if (studentId == null || studentId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No active student session.");
            return;
        }
        studentId = studentId.trim();

        // CGPA
        StudentData sd = new StudentData();
        Student s = sd.findById(studentId);
        double cgpa = (s == null) ? 0.0 : s.getCGPA();

        DefaultTableModel model = (DefaultTableModel) tblModuleResults.getModel();
        model.setRowCount(0);

        int completed = 0;
        int ongoing = 0;

        Map<String, String> moduleIdToName = readModuleNames();

        ModuleResultData mrd = new ModuleResultData();
        List<ModuleResult> results = mrd.findByStudentId(studentId);

        for (ModuleResult r : results) {
            boolean isOngoing = (r.getFinalMark() == 0.0) || (r.getOverallGrade() == null);

            if (isOngoing) ongoing++;
            else completed++;

            String moduleId = r.getModuleID();
            String moduleName = moduleIdToName.getOrDefault(moduleId, moduleId);

            model.addRow(new Object[]{
            moduleIdToName.getOrDefault(r.getModuleID(), r.getModuleID()),
            fmt(r.getLectureMark()),
            fmt(r.getTutorialMark()),
            fmt(r.getFinalMark()),
            (r.getOverallGrade() == null ? "—" : r.getOverallGrade()),
            isOngoing ? "Ongoing" : "Completed"
        });
        }

        lblCompletedValue.setText(String.valueOf(completed));
        lblOngoingValue.setText(String.valueOf(ongoing));
        lblCgpaValue.setText(String.format("%.2f", cgpa));
    }
    
    private static String fmt(Double v) {
        if (v == null) return "--";
        if (Math.abs(v - Math.round(v)) < 0.0000001) return String.valueOf((long) Math.round(v));
        return String.valueOf(v);
    }
        
    private Map<String, String> readModuleNames() {
    Map<String, String> map = new HashMap<>();
        Path path = Paths.get(FilePath.MODULE_FILE);

        if (!Files.exists(path)) return map;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.toLowerCase().startsWith("moduleid,")) continue;

                // moduleID, name, creditHour
                String[] p = line.split(",");
                if (p.length < 2) continue;

                map.put(p[0].trim(), p[1].trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading module: " + e.getMessage());
        }
        return map;
    }
        
    private void TableStyle(JTable table) {
        table.setRowHeight(45);
        table.setShowGrid(false);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);
        table.setGridColor(Color.BLACK);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        
        spModuleResults.setBorder(null);
        spModuleResults.getViewport().setBackground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 48));
        header.setFont(new Font("Arial", Font.BOLD, 12));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);

                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                lbl.setBackground(new Color(169, 201, 160));
                lbl.setForeground(Color.BLACK);
                lbl.setOpaque(true);

                int left = (col == 0) ? 1 : 0;
                lbl.setBorder(BorderFactory.createMatteBorder(1, left, 1, 1, Color.BLACK));
                return lbl;
            }
        };
        header.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);

                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setVerticalAlignment(SwingConstants.CENTER);
                lbl.setFont(new Font("Arial", Font.PLAIN, 12));
                lbl.setForeground(Color.BLACK);

                lbl.setBackground(Color.WHITE);
                lbl.setOpaque(true);
                int left = (col == 0) ? 1 : 0;
                lbl.setBorder(BorderFactory.createMatteBorder(0, left, 1, 1, Color.BLACK));
                return lbl;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
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
        txtGrades = new javax.swing.JLabel();
        topUserInfo = new javax.swing.JPanel();
        StudentProfilePic = new javax.swing.JLabel();
        StudentRole = new javax.swing.JLabel();
        StudentID = new javax.swing.JLabel();
        content = new javax.swing.JPanel();
        pnlStats = new javax.swing.JPanel();
        pnlCompletedCard = new javax.swing.JPanel();
        lblCompletedText = new javax.swing.JLabel();
        lblCompletedValue = new javax.swing.JLabel();
        pnlOngoingCard = new javax.swing.JPanel();
        lblOngoingText = new javax.swing.JLabel();
        lblOngoingValue = new javax.swing.JLabel();
        pnlCgpaCard = new javax.swing.JPanel();
        lblCgpaText = new javax.swing.JLabel();
        lblCgpaValue = new javax.swing.JLabel();
        lblModuleResults = new javax.swing.JLabel();
        btnExport = new javax.swing.JButton();
        spModuleResults = new javax.swing.JScrollPane();
        tblModuleResults = new javax.swing.JTable();

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
            .addGap(0, 15, Short.MAX_VALUE)
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
            .addGap(0, 15, Short.MAX_VALUE)
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
        btnNavGrades.setForeground(new java.awt.Color(166, 199, 155));
        btnNavGrades.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ClickGr.png"))); // NOI18N
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
            .addGap(0, 289, Short.MAX_VALUE)
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

        txtGrades.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        txtGrades.setText("Grades");

        javax.swing.GroupLayout topTitleLayout = new javax.swing.GroupLayout(topTitle);
        topTitle.setLayout(topTitleLayout);
        topTitleLayout.setHorizontalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topTitleLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtGrades)
                .addContainerGap(569, Short.MAX_VALUE))
        );
        topTitleLayout.setVerticalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topTitleLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtGrades, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        pnlStats.setBackground(new java.awt.Color(255, 255, 255));
        pnlStats.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlCompletedCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlCompletedCard.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        pnlCompletedCard.setPreferredSize(new java.awt.Dimension(240, 55));

        lblCompletedText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblCompletedText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompletedText.setText("Completed Modules");
        lblCompletedText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblCompletedValue.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblCompletedValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCompletedValue.setText("0");
        lblCompletedValue.setPreferredSize(new java.awt.Dimension(8, 55));

        javax.swing.GroupLayout pnlCompletedCardLayout = new javax.swing.GroupLayout(pnlCompletedCard);
        pnlCompletedCard.setLayout(pnlCompletedCardLayout);
        pnlCompletedCardLayout.setHorizontalGroup(
            pnlCompletedCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCompletedCardLayout.createSequentialGroup()
                .addComponent(lblCompletedText, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCompletedValue, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlCompletedCardLayout.setVerticalGroup(
            pnlCompletedCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCompletedCardLayout.createSequentialGroup()
                .addGroup(pnlCompletedCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCompletedText, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCompletedValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        pnlStats.add(pnlCompletedCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, -1, -1));

        pnlOngoingCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlOngoingCard.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        pnlOngoingCard.setPreferredSize(new java.awt.Dimension(240, 55));

        lblOngoingText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblOngoingText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOngoingText.setText("Ongoing Module");
        lblOngoingText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblOngoingValue.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblOngoingValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOngoingValue.setText("0");
        lblOngoingValue.setPreferredSize(new java.awt.Dimension(8, 55));

        javax.swing.GroupLayout pnlOngoingCardLayout = new javax.swing.GroupLayout(pnlOngoingCard);
        pnlOngoingCard.setLayout(pnlOngoingCardLayout);
        pnlOngoingCardLayout.setHorizontalGroup(
            pnlOngoingCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOngoingCardLayout.createSequentialGroup()
                .addComponent(lblOngoingText, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblOngoingValue, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlOngoingCardLayout.setVerticalGroup(
            pnlOngoingCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlOngoingCardLayout.createSequentialGroup()
                .addGroup(pnlOngoingCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOngoingText, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOngoingValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        pnlStats.add(pnlOngoingCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, -1, -1));

        pnlCgpaCard.setBackground(new java.awt.Color(255, 255, 255));
        pnlCgpaCard.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 102), 1, true));
        pnlCgpaCard.setPreferredSize(new java.awt.Dimension(240, 55));

        lblCgpaText.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblCgpaText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCgpaText.setText("CGPA");
        lblCgpaText.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblCgpaValue.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        lblCgpaValue.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCgpaValue.setText("0");
        lblCgpaValue.setPreferredSize(new java.awt.Dimension(8, 55));

        javax.swing.GroupLayout pnlCgpaCardLayout = new javax.swing.GroupLayout(pnlCgpaCard);
        pnlCgpaCard.setLayout(pnlCgpaCardLayout);
        pnlCgpaCardLayout.setHorizontalGroup(
            pnlCgpaCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCgpaCardLayout.createSequentialGroup()
                .addComponent(lblCgpaText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCgpaValue, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlCgpaCardLayout.setVerticalGroup(
            pnlCgpaCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCgpaCardLayout.createSequentialGroup()
                .addGroup(pnlCgpaCardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblCgpaText, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblCgpaValue, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        pnlStats.add(pnlCgpaCard, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 180, 55));

        lblModuleResults.setBackground(new java.awt.Color(255, 255, 255));
        lblModuleResults.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblModuleResults.setText("Module Results");
        lblModuleResults.setPreferredSize(new java.awt.Dimension(90, 24));

        btnExport.setBackground(new java.awt.Color(204, 255, 204));
        btnExport.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnExport.setText("Export");
        btnExport.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        btnExport.setBorderPainted(false);
        btnExport.setFocusPainted(false);
        btnExport.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnExport.addActionListener(this::btnExportActionPerformed);

        tblModuleResults.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Module Name", "Lecture", "Lab", "Final Mark", "Grade", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblModuleResults.setGridColor(new java.awt.Color(0, 0, 0));
        tblModuleResults.setRowHeight(36);
        tblModuleResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblModuleResults.setShowHorizontalLines(true);
        tblModuleResults.setShowVerticalLines(true);
        spModuleResults.setViewportView(tblModuleResults);

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlStats, javax.swing.GroupLayout.PREFERRED_SIZE, 740, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(spModuleResults)
                            .addGroup(contentLayout.createSequentialGroup()
                                .addComponent(lblModuleResults, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(491, 491, 491)
                                .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14)))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(pnlStats, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblModuleResults, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExport, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(spModuleResults, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(91, Short.MAX_VALUE))
        );

        main.add(content, java.awt.BorderLayout.CENTER);

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

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this,
            "Your PDF gradebook report has been exported successfully.",
            "Export",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_btnExportActionPerformed

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
    private javax.swing.JButton btnExport;
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
    private javax.swing.JLabel lblCgpaText;
    private javax.swing.JLabel lblCgpaValue;
    private javax.swing.JLabel lblCompletedText;
    private javax.swing.JLabel lblCompletedValue;
    private javax.swing.JLabel lblModuleResults;
    private javax.swing.JLabel lblOngoingText;
    private javax.swing.JLabel lblOngoingValue;
    private javax.swing.JPanel main;
    private javax.swing.JPanel pnlAFSlogo;
    private javax.swing.JPanel pnlCgpaCard;
    private javax.swing.JPanel pnlCompletedCard;
    private javax.swing.JPanel pnlNavAssessments;
    private javax.swing.JPanel pnlNavDashboard;
    private javax.swing.JPanel pnlNavFeedback;
    private javax.swing.JPanel pnlNavGrades;
    private javax.swing.JPanel pnlNavLogout;
    private javax.swing.JPanel pnlNavMyClasses;
    private javax.swing.JPanel pnlNavProfile;
    private javax.swing.JPanel pnlOngoingCard;
    private javax.swing.JPanel pnlStats;
    private javax.swing.JPanel sidebar;
    private javax.swing.JScrollPane spModuleResults;
    private javax.swing.JTable tblModuleResults;
    private javax.swing.JPanel top;
    private javax.swing.JPanel topTitle;
    private javax.swing.JPanel topUserInfo;
    private javax.swing.JLabel txtGrades;
    // End of variables declaration//GEN-END:variables
}
