/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Student;

// my packages
import Classes.Session;
import Classes.FilePath;

import Classes.RegisteredClassData;
import Classes.ClassData;
import Classes.CommentData;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import Classes.RegisteredClass;
import Classes.ClassInfo;
import Classes.Comment;
import Login.AFS_LoginPage;

// java packages
import javax.swing.table.DefaultTableModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class StudentFeedback extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentFeedback.class.getName());

    /**
     * Creates new form StudentFeedback
     */
    public StudentFeedback() {
        initComponents();
        
        setSize(1031, 643); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // table columns
        tblCommentHistory.getColumnModel().getColumn(0).setPreferredWidth(150); // class
        tblCommentHistory.getColumnModel().getColumn(1).setPreferredWidth(200); // lecturer
        tblCommentHistory.getColumnModel().getColumn(2).setPreferredWidth(355); // feedback
        tblCommentHistory.setRowHeight(36);
        
        loadHeaderFromSession();
        initClassCombo();
        styleCommentTable(tblCommentHistory, spCommentHistory);
        
        cbClass.setSelectedIndex(0);
        loadCommentsHistory();
    }
    
    private void loadHeaderFromSession() {
        String sid = Session.getStudentId();
        sid = (sid == null) ? "" : sid.trim();

        StudentID.setText(!sid.isBlank() ? sid : "TPXXXXXX");
        StudentRole.setText("Student");
    }
    
    private static class ComboItem {
        final String id;
        final String label;

        ComboItem(String id, String label) {
            this.id = id;
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }
    
    private void styleCommentTable(JTable table, JScrollPane scrollPane) {
        table.setRowHeight(45);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        
        table.setShowGrid(false);
        table.setShowHorizontalLines(false);
        table.setShowVerticalLines(false);

        table.setBorder(null);
        scrollPane.setBorder(null);
        scrollPane.setViewportBorder(null);
        scrollPane.getViewport().setBackground(Color.WHITE);

        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 42));
        header.setFont(new Font("Arial", Font.BOLD, 12));
        
        tblCommentHistory.getColumnModel()
            .getColumn(2)
            .setCellRenderer(new WrapCellRenderer());

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);

                lbl.setHorizontalAlignment(SwingConstants.CENTER);
                lbl.setOpaque(true);
                lbl.setBackground(new Color(169, 201, 160));
                lbl.setForeground(Color.BLACK);
                lbl.setFont(new Font("Arial", Font.BOLD, 12));

                int left = (col == 0) ? 1 : 0;
                lbl.setBorder(BorderFactory.createMatteBorder(1, left, 1, 1, Color.BLACK));

                return lbl;
            }
        };
        header.setDefaultRenderer(headerRenderer);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, col);

                lbl.setOpaque(true);
                lbl.setFont(new Font("Arial", Font.PLAIN, 12));
                lbl.setForeground(Color.BLACK);

                lbl.setBackground(isSelected ? new Color(230, 230, 230) : Color.WHITE);

                if (col == 2) lbl.setHorizontalAlignment(SwingConstants.LEFT);
                else lbl.setHorizontalAlignment(SwingConstants.CENTER);

                int left = (col == 0) ? 1 : 0;
                lbl.setBorder(BorderFactory.createMatteBorder(0, left, 1, 1, Color.BLACK));

                return lbl;
            }
        };

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }
    
    private void initClassCombo() {
        cbClass.removeAllItems();
        cbClass.addItem(new ComboItem("", "-- Select Class --"));
        
        for (var al : cbClass.getActionListeners()) cbClass.removeActionListener(al);

        if (!Session.isLoggedIn() || Session.getStudentId() == null) return;
        String studentId = Session.getStudentId().trim();

        RegisteredClassData rcData = new RegisteredClassData();
        List<RegisteredClass> regs = rcData.findByStudentId(studentId);
        if (regs == null || regs.isEmpty()) return;

        ClassData classData = new ClassData();
        Set<String> seen = new HashSet<>();

        for (RegisteredClass rc : regs) {
            if (rc == null || rc.getClassID() == null) continue;

            String classId = rc.getClassID().trim();
            if (!seen.add(classId.toUpperCase())) continue;

            ClassInfo ci = classData.findById(classId);
            String label = (ci == null) ? classId : ci.getName();

            cbClass.addItem(new ComboItem(classId, label));
        }

        cbClass.addActionListener(e -> loadCommentsHistory());
    }
    
    private void loadCommentsHistory() {
        DefaultTableModel model = (DefaultTableModel) tblCommentHistory.getModel();
        model.setRowCount(0);

        if (!Session.isLoggedIn() || Session.getStudentId() == null) return;
        String studentId = Session.getStudentId().trim();

        ComboItem selected = (ComboItem) cbClass.getSelectedItem();
        String selectedClassId =
                (selected == null || selected.id.isBlank()) ? "" : selected.id.trim();

        RegisteredClassData rcData = new RegisteredClassData();
        List<RegisteredClass> regs = rcData.findByStudentId(studentId);
        if (regs == null || regs.isEmpty()) return;

        List<RegisteredClass> targetRegs = new ArrayList<>();

        for (RegisteredClass rc : regs) {
            if (rc == null || rc.getRegisteredClassID() == null) continue;

            if (selectedClassId.isBlank()) {
                targetRegs.add(rc);
            }
            else if (rc.getClassID() != null
                    && rc.getClassID().trim().equalsIgnoreCase(selectedClassId)) {
                targetRegs.add(rc);
            }
        }

        if (targetRegs.isEmpty()) return;

        // load comments
        CommentData commentData = new CommentData();
        ClassData classData = new ClassData();

        for (RegisteredClass rc : targetRegs) {

            String classId = rc.getClassID();
            String className = classId;
            String lecturerName = "-";

            ClassInfo ci = classData.findById(classId);
            if (ci != null) {
                className = ci.getName();
                lecturerName = resolveLecturerNameForClass(classId);
            }

            List<Comment> comments =
                    commentData.findByRegisteredClassId(rc.getRegisteredClassID());

            if (comments == null) continue;

            for (Comment c : comments) {
                if (c == null) continue;

                String text = c.getComment();
                if (text == null) text = "";

                model.addRow(new Object[]{
                    className,
                    lecturerName,
                    text.trim()
                });
            }
        }
    }
    
    private String resolveLecturerNameForClass(String classId) {
        try {
            // class.txt -> get moduleLectID (4th column)
            String moduleLectId = null;
            try (BufferedReader br = Files.newBufferedReader(Paths.get(FilePath.CLASS_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.toLowerCase().startsWith("classid,")) continue;
                    String[] p = line.split(",");
                    if (p.length < 4) continue;

                    String cid = p[0].trim();
                    if (cid.equalsIgnoreCase(classId)) {
                        moduleLectId = p[3].trim();
                        break;
                    }
                }
            }
            if (moduleLectId == null || moduleLectId.isBlank()) return "-";

            // moduleLecturer.txt -> lecturerID
            String lecturerId = null;
            try (BufferedReader br = Files.newBufferedReader(Paths.get(FilePath.MODULE_LECTURER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.toLowerCase().startsWith("modlectid,")) continue;
                    String[] p = line.split(",");
                    if (p.length < 2) continue;

                    String ml = p[0].trim();
                    if (ml.equalsIgnoreCase(moduleLectId)) {
                        lecturerId = p[1].trim();
                        break;
                    }
                }
            }
            if (lecturerId == null || lecturerId.isBlank()) return "-";

            // lecturer.txt -> title + name
            try (BufferedReader br = Files.newBufferedReader(Paths.get(FilePath.LECTURER_FILE))) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.toLowerCase().startsWith("lecturerid,")) continue;
                    String[] p = line.split(",");
                    if (p.length < 4) continue;

                    String lid = p[0].trim();
                    if (lid.equalsIgnoreCase(lecturerId)) {
                        String title = p[3].trim();
                        String name = p[2].trim();
                        if (title.equalsIgnoreCase("NULL")) title = "";
                        return (title.isBlank() ? name : (title + " " + name)).trim();
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Lecturer resolve error: " + e.getMessage());
        }
        return "-";
    }
    
    private static class WrapCellRenderer extends JTextArea
        implements javax.swing.table.TableCellRenderer {

    public WrapCellRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setFont(new Font("Arial", Font.PLAIN, 12));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            setText(value == null ? "" : value.toString());

            if (isSelected) {
                setBackground(new Color(230, 230, 230));
            } else {
                setBackground(Color.WHITE);
            }

            setForeground(Color.BLACK);
            setBorder(null);

            setSize(table.getColumnModel().getColumn(column).getWidth(), Short.MAX_VALUE);
            int preferredHeight = getPreferredSize().height;

            if (table.getRowHeight(row) != preferredHeight) {
                table.setRowHeight(row, preferredHeight);
            }

            return this;
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
        txtFeedback = new javax.swing.JLabel();
        topUserInfo = new javax.swing.JPanel();
        StudentProfilePic = new javax.swing.JLabel();
        StudentRole = new javax.swing.JLabel();
        StudentID = new javax.swing.JLabel();
        content = new javax.swing.JPanel();
        lblCommentsHistory = new javax.swing.JLabel();
        lblClass = new javax.swing.JLabel();
        cbClass = new javax.swing.JComboBox<>();
        btnNewComment = new javax.swing.JButton();
        spCommentHistory = new javax.swing.JScrollPane();
        tblCommentHistory = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Student Feedback");
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

        txtFeedback.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        txtFeedback.setText("Feedback");

        javax.swing.GroupLayout topTitleLayout = new javax.swing.GroupLayout(topTitle);
        topTitle.setLayout(topTitleLayout);
        topTitleLayout.setHorizontalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topTitleLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(txtFeedback)
                .addContainerGap(542, Short.MAX_VALUE))
        );
        topTitleLayout.setVerticalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topTitleLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(txtFeedback, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        lblCommentsHistory.setBackground(new java.awt.Color(255, 255, 255));
        lblCommentsHistory.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblCommentsHistory.setText("Comments History");
        lblCommentsHistory.setPreferredSize(new java.awt.Dimension(90, 24));

        lblClass.setBackground(new java.awt.Color(255, 255, 255));
        lblClass.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblClass.setText("Class:");
        lblClass.setPreferredSize(new java.awt.Dimension(90, 24));

        cbClass.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cbClass.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "-- Select Class --" }));
        cbClass.addActionListener(this::cbClassActionPerformed);

        btnNewComment.setBackground(new java.awt.Color(204, 255, 204));
        btnNewComment.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnNewComment.setText("Create New Comment");
        btnNewComment.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        btnNewComment.setBorderPainted(false);
        btnNewComment.setFocusPainted(false);
        btnNewComment.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnNewComment.addActionListener(this::btnNewCommentActionPerformed);

        tblCommentHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Class", "Lecturer", "Comment"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCommentHistory.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblCommentHistory.setRowHeight(36);
        tblCommentHistory.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        spCommentHistory.setViewportView(tblCommentHistory);

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addComponent(lblCommentsHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(spCommentHistory)
                            .addGroup(contentLayout.createSequentialGroup()
                                .addComponent(lblClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbClass, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
                                .addComponent(btnNewComment, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50))))
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblClass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbClass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNewComment, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(lblCommentsHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spCommentHistory, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(72, Short.MAX_VALUE))
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

    private void cbClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbClassActionPerformed
        //
    }//GEN-LAST:event_cbClassActionPerformed

    private void btnNewCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewCommentActionPerformed
        Object obj = cbClass.getSelectedItem();

        if (!(obj instanceof ComboItem)) {
            JOptionPane.showMessageDialog(this, "Please select a class first.");
            return;
        }

        ComboItem selected = (ComboItem) obj;

        if (selected.id == null || selected.id.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a class first.");
            return;
        }

        Session.setSelectedClassId(selected.id.trim());

        new StudentFeedbackForm().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnNewCommentActionPerformed

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
    private javax.swing.JButton btnNavAssessments;
    private javax.swing.JButton btnNavDashboard;
    private javax.swing.JButton btnNavFeedback;
    private javax.swing.JButton btnNavGrades;
    private javax.swing.JButton btnNavLogout;
    private javax.swing.JButton btnNavMyClasses;
    private javax.swing.JButton btnNavProfile;
    private javax.swing.JButton btnNewComment;
    private javax.swing.JComboBox<Object> cbClass;
    private javax.swing.JPanel content;
    private javax.swing.JPanel dbBase;
    private javax.swing.JPanel gap1;
    private javax.swing.JPanel gap2;
    private javax.swing.JPanel gap3;
    private javax.swing.JLabel lblAFSlogo;
    private javax.swing.JLabel lblClass;
    private javax.swing.JLabel lblCommentsHistory;
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
    private javax.swing.JScrollPane spCommentHistory;
    private javax.swing.JTable tblCommentHistory;
    private javax.swing.JPanel top;
    private javax.swing.JPanel topTitle;
    private javax.swing.JPanel topUserInfo;
    private javax.swing.JLabel txtFeedback;
    // End of variables declaration//GEN-END:variables
}
