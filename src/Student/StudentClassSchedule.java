/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Student;

// java packages
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import Classes.ClassData;
import Classes.RegisteredClassData;
import Classes.ClassInfo;
import Classes.RegisteredClass;
import Classes.Session;
import Login.AFS_LoginPage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.table.DefaultTableCellRenderer;

public class StudentClassSchedule extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StudentClassSchedule.class.getName());
    private boolean lockToSelectedClass = false;
    /**
     * Creates new form StudentClassSchedule
     */
    public StudentClassSchedule() {
        initComponents();
        
        setSize(1031, 643); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        tblSchedule.setRowHeight(45);
        tblSchedule.setGridColor(Color.BLACK);
        tblSchedule.setIntercellSpacing(new Dimension(0, 0));
        tblSchedule.setFillsViewportHeight(true);
        
        spSchedule.setBorder(null);
        spSchedule.getViewport().setBackground(Color.WHITE);
        spSchedule.setViewportBorder(null);
        spSchedule.getViewport().setBackground(Color.WHITE);
        spSchedule.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spSchedule.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.table.JTableHeader header = tblSchedule.getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(true);
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 48));
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(null);
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent( JTable table,
                    Object value, boolean isSelected, boolean hasFocus, int row, int col)
                    { JLabel lbl = (JLabel) super.getTableCellRendererComponent( table, value, isSelected, 
                                                                                hasFocus, row, col);
                    lbl.setHorizontalAlignment(SwingConstants.CENTER);
                    lbl.setVerticalAlignment(SwingConstants.CENTER);
                    lbl.setBackground(new Color(169, 201, 160));
                    lbl.setForeground(Color.BLACK);
                    lbl.setOpaque(true); int left = (col == 0) ? 1 : 0;
                    lbl.setBorder(BorderFactory.createMatteBorder(1, left, 1, 1, Color.BLACK));
                    return lbl; } };
        header.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.BLACK));
        
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent( JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col)
            { JLabel lbl = (JLabel) super.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, col);
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            lbl.setVerticalAlignment(SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.PLAIN, 12));
            lbl.setForeground(Color.BLACK);
            lbl.setBackground(Color.WHITE);
            
            lbl.setOpaque(true); int left = (col == 0) ? 1 : 0;
            lbl.setBorder(BorderFactory.createMatteBorder(0, left, 1, 1, Color.BLACK));
            return lbl; } }; header.setDefaultRenderer(headerRenderer);
            for (int i = 0; i < tblSchedule.getColumnModel().getColumnCount(); i++)
                { tblSchedule.getColumnModel().getColumn(i).setCellRenderer(cellRenderer); }

        applyScheduleColumnWidths();
        setScheduleColumnWidths();
        loadHeaderFromSession();
        String sel = Session.getSelectedClassId();
        sel = (sel == null) ? "" : sel.trim();
        lockToSelectedClass = !sel.isEmpty();
        loadScheduleTable();
        lockToSelectedClass = false;
        Session.setSelectedClassId("");
    }
    
    private void setScheduleColumnWidths() {
        int[] widths = {110, 90, 90, 190, 90, 87}; 
        // Day, From, Until, Class, Type, Venue

        for (int i = 0; i < widths.length; i++) {
            tblSchedule.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        tblSchedule.getTableHeader().setReorderingAllowed(false);
    }
    
    private void loadHeaderFromSession() {
        String sid = Session.getStudentId();
        sid = (sid == null) ? "" : sid.trim();

        StudentID.setText(!sid.isBlank() ? sid : "TPXXXXXX");
        StudentRole.setText("Student");
    }
    
    private void loadScheduleTable() {
        DefaultTableModel model = (DefaultTableModel) tblSchedule.getModel();
        model.setRowCount(0);

        // session check
        if (!Session.isLoggedIn() || Session.getStudentId() == null || Session.getStudentId().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No active student session.");
            return;
        }

        String studentId = Session.getStudentId().trim();
        String selectedClassId = Session.getSelectedClassId();
        selectedClassId = (selectedClassId == null) ? "" : selectedClassId.trim();

        RegisteredClassData rcData = new RegisteredClassData();
        List<RegisteredClass> regs = rcData.findByStudentId(studentId);

        if (regs == null || regs.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No registered classes found.");
            return;
        }

        ClassData classData = new ClassData();

        for (RegisteredClass rc : regs) {
            if (rc == null || rc.getClassID() == null) continue;

            String classId = rc.getClassID().trim();

            // If came from ">" button, show only that class first
            if (!selectedClassId.isEmpty() && !classId.equalsIgnoreCase(selectedClassId)) {
                continue;
            }

            ClassInfo ci = classData.findById(classId);
            if (ci == null) continue;

            // compute until time (simple hour add)
            String from = formatHHmm(ci.getStartTime());
            String until = addHours(from, ci.getDurationHour());
            
            String dayFilter = cbDay.getSelectedItem().toString();
            String typeFilter = cbType.getSelectedItem().toString();

            if (!dayFilter.equalsIgnoreCase("All") && !ci.getClassDay().equalsIgnoreCase(dayFilter)) {
                continue;
            }
            if (!typeFilter.equalsIgnoreCase("All") && !ci.getType().equalsIgnoreCase(typeFilter)) {
                continue;
            }

            model.addRow(new Object[]{
                ci.getClassDay(),     // day
                from,                 // from
                until,                // until
                ci.getName(),         // class
                ci.getType(),         // type
                ci.getVenue()         // venue
            });
            
        }
        
        sortTableByDayThenTime(model);
        
        if (model.getRowCount() == 0 && !selectedClassId.isEmpty()) {
            Session.setSelectedClassId("");
            loadScheduleTable();
        }
    }
    
    private String addHours(String time, double hoursToAdd) {
        try {
            time = formatHHmm(time);

            String[] parts = time.split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);

            int totalMinutes = hour * 60 + minute;
            int addMinutes = (int) Math.round(hoursToAdd * 60);

            totalMinutes = (totalMinutes + addMinutes) % (24 * 60);

            int newHour = totalMinutes / 60;
            int newMinute = totalMinutes % 60;

            return String.format("%02d:%02d", newHour, newMinute);
        } catch (Exception e) {
            return time;
        }
    }
        
    private String formatHHmm(String raw) {
        if (raw == null) return "";
        raw = raw.trim();

        // Accept "0830" or "830"
        if (raw.matches("\\d{3,4}")) {
            raw = String.format("%04d", Integer.parseInt(raw));
            return raw.substring(0, 2) + ":" + raw.substring(2);
        }

        if (raw.matches("\\d{2}:\\d{2}")) return raw;

        return raw;
    }
    
    private void applyScheduleColumnWidths() {
        int[] w = {110,   80,    80,    247,     70,   70};

        for (int i = 0; i < w.length; i++) {
            tblSchedule.getColumnModel().getColumn(i).setPreferredWidth(w[i]);
        }

        tblSchedule.getTableHeader().setReorderingAllowed(false);
    }
    
    private int dayOrder(String day) {
        if (day == null) return 99;
        switch (day.trim().toLowerCase()) {
            case "monday": return 1;
            case "tuesday": return 2;
            case "wednesday": return 3;
            case "thursday": return 4;
            case "friday": return 5;
            case "saturday": return 6;
            case "sunday": return 7;
            default: return 99;
        }
    }

    private int timeToMinutes(String hhmm) {
        String t = formatHHmm(hhmm);
        try {
            String[] p = t.split(":");
            int h = Integer.parseInt(p[0]);
            int m = Integer.parseInt(p[1]);
            return h * 60 + m;
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    private void sortTableByDayThenTime(DefaultTableModel model) {
        List<Object[]> rows = new ArrayList<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            Object[] r = new Object[model.getColumnCount()];
            for (int j = 0; j < model.getColumnCount(); j++) {
                r[j] = model.getValueAt(i, j);
            }
            rows.add(r);
        }

        rows.sort((a, b) -> {
            String dayA = String.valueOf(a[0]);
            String dayB = String.valueOf(b[0]);
            int d = Integer.compare(dayOrder(dayA), dayOrder(dayB));
            if (d != 0) return d;

            String fromA = String.valueOf(a[1]);
            String fromB = String.valueOf(b[1]);
            return Integer.compare(timeToMinutes(fromA), timeToMinutes(fromB));
        });

        model.setRowCount(0);
        for (Object[] r : rows) model.addRow(r);
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
        lblFilter = new javax.swing.JLabel();
        lblDay = new javax.swing.JLabel();
        cbDay = new javax.swing.JComboBox<>();
        lblType = new javax.swing.JLabel();
        cbType = new javax.swing.JComboBox<>();
        btnReset = new javax.swing.JButton();
        Export = new javax.swing.JButton();
        spSchedule = new javax.swing.JScrollPane();
        tblSchedule = new javax.swing.JTable();

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
        btnNavMyClasses.setForeground(new java.awt.Color(166, 199, 155));
        btnNavMyClasses.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/ClickMC.png"))); // NOI18N
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

        txtRegisterNewClasses.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        txtRegisterNewClasses.setText("Class Schedule");

        javax.swing.GroupLayout topTitleLayout = new javax.swing.GroupLayout(topTitle);
        topTitle.setLayout(topTitleLayout);
        topTitleLayout.setHorizontalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topTitleLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(txtRegisterNewClasses)
                .addContainerGap(479, Short.MAX_VALUE))
        );
        topTitleLayout.setVerticalGroup(
            topTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, topTitleLayout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(txtRegisterNewClasses, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
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

        lblFilter.setBackground(new java.awt.Color(255, 255, 255));
        lblFilter.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblFilter.setText("Filter");
        lblFilter.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        lblDay.setBackground(new java.awt.Color(255, 255, 255));
        lblDay.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblDay.setText("Day:");
        lblDay.setPreferredSize(new java.awt.Dimension(90, 24));

        cbDay.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cbDay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" }));
        cbDay.addActionListener(this::cbDayActionPerformed);

        lblType.setBackground(new java.awt.Color(255, 255, 255));
        lblType.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblType.setText("Type:");
        lblType.setPreferredSize(new java.awt.Dimension(90, 24));

        cbType.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Lecture", "Lab", "Tutorial" }));
        cbType.addActionListener(this::cbTypeActionPerformed);

        btnReset.setBackground(new java.awt.Color(204, 255, 204));
        btnReset.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        btnReset.setText("Reset");
        btnReset.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        btnReset.setBorderPainted(false);
        btnReset.setFocusPainted(false);
        btnReset.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnReset.addActionListener(this::btnResetActionPerformed);

        Export.setBackground(new java.awt.Color(204, 255, 204));
        Export.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        Export.setText("Export");
        Export.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        Export.setBorderPainted(false);
        Export.setFocusPainted(false);
        Export.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        Export.addActionListener(this::ExportActionPerformed);

        tblSchedule.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tblSchedule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Day", "From", "Until", "Class", "Type", "Venue"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSchedule.setMaximumSize(new java.awt.Dimension(2147483647, 330));
        tblSchedule.setMinimumSize(new java.awt.Dimension(90, 300));
        tblSchedule.setPreferredSize(new java.awt.Dimension(260, 80));
        tblSchedule.setRowHeight(44);
        tblSchedule.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblSchedule.setShowGrid(true);
        spSchedule.setViewportView(tblSchedule);

        javax.swing.GroupLayout contentLayout = new javax.swing.GroupLayout(content);
        content.setLayout(contentLayout);
        contentLayout.setHorizontalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(contentLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(spSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 657, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, contentLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(contentLayout.createSequentialGroup()
                                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(Export, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, contentLayout.createSequentialGroup()
                                .addComponent(lblFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(437, 437, 437))
                            .addGroup(contentLayout.createSequentialGroup()
                                .addComponent(lblDay, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(cbDay, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(217, 217, 217)
                                .addComponent(lblType, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        contentLayout.setVerticalGroup(
            contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contentLayout.createSequentialGroup()
                .addComponent(lblFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbDay, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblType, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbType, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52)
                .addGroup(contentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Export, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(spSchedule, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
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

    private void cbDayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDayActionPerformed
        lockToSelectedClass = false;
        Session.setSelectedClassId("");
        loadScheduleTable();
    }//GEN-LAST:event_cbDayActionPerformed

    private void cbTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTypeActionPerformed
        lockToSelectedClass = false;
        Session.setSelectedClassId("");
        loadScheduleTable();
    }//GEN-LAST:event_cbTypeActionPerformed

    private void ExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportActionPerformed
        javax.swing.JOptionPane.showMessageDialog(this,
            "Your PDF class schedule has been exported successfully.",
            "Export",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_ExportActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        cbDay.setSelectedIndex(0);
        cbType.setSelectedIndex(0);
        
        lockToSelectedClass = false;
        Session.setSelectedClassId("");
        loadScheduleTable();
    }//GEN-LAST:event_btnResetActionPerformed

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
    private javax.swing.JButton Export;
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
    private javax.swing.JButton btnReset;
    private javax.swing.JComboBox<String> cbDay;
    private javax.swing.JComboBox<String> cbType;
    private javax.swing.JPanel content;
    private javax.swing.JPanel dbBase;
    private javax.swing.JPanel gap1;
    private javax.swing.JPanel gap2;
    private javax.swing.JPanel gap3;
    private javax.swing.JLabel lblAFSlogo;
    private javax.swing.JLabel lblDay;
    private javax.swing.JLabel lblFilter;
    private javax.swing.JLabel lblType;
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
    private javax.swing.JScrollPane spSchedule;
    private javax.swing.JTable tblSchedule;
    private javax.swing.JPanel top;
    private javax.swing.JPanel topTitle;
    private javax.swing.JPanel topUserInfo;
    private javax.swing.JLabel txtRegisterNewClasses;
    // End of variables declaration//GEN-END:variables
}
