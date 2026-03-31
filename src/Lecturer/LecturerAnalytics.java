package Lecturer;

import java.awt.Color;
import Classes.Lecturer;
import Lecturer.ChartPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import Classes.Student;
import Classes.Module;
import Classes.Class;
import Classes.Lecturer;
import Login.AFS_LoginPage;


public class LecturerAnalytics extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LecturerAnalytics.class.getName());
    
    private Lecturer lecturer;
    private ChartPanel leftChartPanel;
    private ChartPanel rightChartPanel;
    private JPanel rightPanel;
    
    private javax.swing.JPanel ChartsPanel;
    javax.swing.JButton DownloadReportButton;
    
    public LecturerAnalytics(Lecturer lecturer) {
        initComponents();
        
        Dimension comboSize = new Dimension(600, 40);

        ModuleSelectionBox.setPreferredSize(comboSize);
        ModuleSelectionBox.setMaximumSize(comboSize);

        ClassSelectionBox.setPreferredSize(comboSize);
        ClassSelectionBox.setMaximumSize(comboSize);


        ModuleSelectionBox.setBackground(new Color(255, 255, 255));
        ClassSelectionBox.setBackground(new Color(255, 255, 255));

        this.lecturer = lecturer;
        if (this.lecturer != null) {
            UserName.setText(this.lecturer.getName());
        }
        UserRole.setText("Lecturer");

        initializeCharts();

        loadModuleDropdown();

        ClassSelectionBox.setEnabled(false);

    }
    
    private void initializeCharts() {
        // Create chart panels
        leftChartPanel = new ChartPanel("gradeDistribution", "Grade Distribution");
        rightChartPanel = new ChartPanel("assessmentStats", "Assessment Statistics");

        // Create a container panel for the right side
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(rightChartPanel, BorderLayout.CENTER);

        // Set layouts for jPanel1 and jPanel2
        LeftJPanel.setLayout(new java.awt.BorderLayout());
        LeftJPanel.add(leftChartPanel, java.awt.BorderLayout.CENTER);

        RightJPanel.setLayout(new java.awt.BorderLayout());
        RightJPanel.add(rightPanel, java.awt.BorderLayout.CENTER);

        LeftJPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        RightJPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

        LeftJPanel.setBackground(Color.WHITE);
        RightJPanel.setBackground(Color.WHITE);

        // Initially hide save button
        SaveReportButton.setVisible(false);
    }
    
    private void loadModuleDropdown() {
        ModuleSelectionBox.removeAllItems();
        ModuleSelectionBox.addItem("Select Module");

        if (lecturer == null) return;

        for (Module m : lecturer.getModules()) {
            ModuleSelectionBox.addItem(m.getModuleID());
        }
    }
    
    private void loadClassDropdown(String moduleID) {
        ClassSelectionBox.removeAllItems();
        ClassSelectionBox.addItem("Select Class");
        
        if (moduleID == null || moduleID.equals("Select Module")) {
            ClassSelectionBox.setEnabled(false);
            return;
        }
        
        // Get classes for this module
        List<Class> moduleClasses = lecturer.getClassesForModule(moduleID);
        
        if (moduleClasses.isEmpty()) {
            ClassSelectionBox.addItem("No Classes Found");
            ClassSelectionBox.setEnabled(false);
        } else {
            for (Class c : moduleClasses) {
                ClassSelectionBox.addItem(c.getClassID() + " - " + c.getName());
            }
            ClassSelectionBox.setEnabled(true);
        } 
    }
    

    private void loadAnalyticsData() {
        String selectedModule = (String) ModuleSelectionBox.getSelectedItem();
        String selectedClassInfo = (String) ClassSelectionBox.getSelectedItem();

        leftChartPanel.setBarChartData(null);
        rightPanel.removeAll();

        boolean ready =
            selectedModule != null &&
            !selectedModule.equals("Select Module") &&
            selectedClassInfo != null &&
            !selectedClassInfo.equals("Select Class") &&
            !selectedClassInfo.equals("No Classes Found");

        SaveReportButton.setVisible(ready);

        if (!ready) {
            leftChartPanel.showInstructionMessage(
                "Select Module and Class to view analytics"
            );
            rightChartPanel.showInstructionMessage(
                "Statistics will appear here"
            );
            return;
        }

        // Extract classID
        String classID = selectedClassInfo.split(" - ")[0];

        // Analytics
        Map<String, Integer> gradeDist =
            lecturer.getClassGradeDistribution(classID);

        leftChartPanel.setGradeChartData(gradeDist);
        leftChartPanel.setLabels("Grade", "Number of Students");

        // Right: simple class stats
        Map<String, Object> stats =
            calculateClassStats(classID);

        JPanel statsPanel = createStatisticsPanel(stats);
        rightPanel.add(statsPanel, BorderLayout.CENTER);

        leftChartPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // Helper method - statistics panel
    private JPanel createStatisticsPanel(Map<String, Object> stats) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Assessment Statistics");
        title.setFont(new Font("Arial", Font.BOLD, 13));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        if (stats != null && !stats.isEmpty()) {
            // Create a panel for statistics
            JPanel statsGrid = new JPanel(new GridLayout(0, 2, 10, 10));
            statsGrid.setBackground(Color.WHITE);

            double average = (double) stats.getOrDefault("average", 0.0);
            double highest = (double) stats.getOrDefault("highest", 0.0);
            double lowest = (double) stats.getOrDefault("lowest", 0.0);
            int count = (int) stats.getOrDefault("count", 0);

            addStatRow(statsGrid, "Average Mark:", String.format("%.1f", average));
            addStatRow(statsGrid, "Highest Mark:", String.format("%.1f", highest));
            addStatRow(statsGrid, "Lowest Mark:", String.format("%.1f", lowest));
            addStatRow(statsGrid, "Number of Students:", String.valueOf(count));
            panel.add(statsGrid);
        } else {
            JLabel noData = new JLabel("No statistics available");
            noData.setFont(new Font("Arial", Font.ITALIC, 12));
            noData.setForeground(Color.GRAY);
            noData.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(noData);
        }

        return panel;
    }

    private void addStatRow(JPanel panel, String label, String value) {
        JLabel labelL = new JLabel(label);
        labelL.setFont(new Font("Arial", Font.BOLD, 12));

        JLabel valueL = new JLabel(value);
        valueL.setFont(new Font("Arial", Font.PLAIN, 12));

        panel.add(labelL);
        panel.add(valueL);
    }
   
    private Map<String, Object> calculateClassStats(String classID) {
        Map<String, Object> stats = new HashMap<>();

        String moduleID = lecturer.getModuleIDFromClass(classID);
        System.out.println("Module for class = " + moduleID);

        List<Student> students =
            lecturer.getStudentsForClass(classID);

        double total = 0;
        int count = 0;
        double highest = 0;
        double lowest = 0;
        
        System.out.println("DEBUG CLASS = " + classID);
        System.out.println("Students in class:");


        for (Student s : students) {
            String[] result =
                lecturer.getModuleResultForStudent(
                    s.getStudentID(),
                    moduleID
                );

            String classType = lecturer.getClassType(classID);

            String markStr;

            if (classType.equalsIgnoreCase("Lecture")) {
                markStr = result[0];
            } else {
                markStr = result[1];
            }

            if (markStr == null ||
                markStr.equals("0.0") ||
                markStr.equals("NULL") ||
                markStr.equals("N/A") ||
                markStr.isEmpty()) {
                continue;
            }

            double mark = Double.parseDouble(markStr);

            total += mark;
            count++;

            if (mark > highest) highest = mark;
            if (mark < lowest) lowest = mark;
        }


        stats.put("average", count == 0 ? 0 : total / count);
        stats.put("highest", highest);
        stats.put("lowest", lowest);
        stats.put("count", count);

        return stats;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MainPanel = new javax.swing.JPanel();
        SidePanel = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        DashboardButton = new javax.swing.JButton();
        ClassButton = new javax.swing.JButton();
        AssessmentButton = new javax.swing.JButton();
        GradingButton = new javax.swing.JButton();
        ProfileButton = new javax.swing.JButton();
        AnalyticsButton = new javax.swing.JButton();
        LogoutButton = new javax.swing.JButton();
        ModuleButton = new javax.swing.JButton();
        TitleP1 = new javax.swing.JLabel();
        SaveReportButton = new javax.swing.JButton();
        toppanel = new javax.swing.JLayeredPane();
        ClassSelectionBox = new javax.swing.JComboBox<>();
        ModuleSelectionBox = new javax.swing.JComboBox<>();
        LeftJPanel = new javax.swing.JPanel();
        RightJPanel = new javax.swing.JPanel();
        IconPanel = new javax.swing.JPanel();
        UserIcon = new javax.swing.JLabel();
        UserName = new javax.swing.JLabel();
        UserRole = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        MainPanel.setBackground(new java.awt.Color(255, 255, 255));
        MainPanel.setPreferredSize(new java.awt.Dimension(1031, 643));

        SidePanel.setBackground(new java.awt.Color(24, 29, 49));
        SidePanel.setToolTipText("");

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/AFS Logo.png"))); // NOI18N

        DashboardButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        DashboardButton.setForeground(new java.awt.Color(248, 248, 248));
        DashboardButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconDb.png"))); // NOI18N
        DashboardButton.setText("   Dashboard");
        DashboardButton.setAlignmentY(0.0F);
        DashboardButton.setBorder(null);
        DashboardButton.setBorderPainted(false);
        DashboardButton.setContentAreaFilled(false);
        DashboardButton.setDefaultCapable(false);
        DashboardButton.setFocusPainted(false);
        DashboardButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        DashboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardButtonActionPerformed(evt);
            }
        });

        ClassButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        ClassButton.setForeground(new java.awt.Color(248, 248, 248));
        ClassButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconClass.png"))); // NOI18N
        ClassButton.setText("   Class");
        ClassButton.setToolTipText("");
        ClassButton.setActionCommand("Class");
        ClassButton.setAlignmentY(0.0F);
        ClassButton.setBorder(null);
        ClassButton.setBorderPainted(false);
        ClassButton.setContentAreaFilled(false);
        ClassButton.setDefaultCapable(false);
        ClassButton.setFocusPainted(false);
        ClassButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ClassButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClassButtonActionPerformed(evt);
            }
        });

        AssessmentButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        AssessmentButton.setForeground(new java.awt.Color(248, 248, 248));
        AssessmentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconAss.png"))); // NOI18N
        AssessmentButton.setText("   Assessment");
        AssessmentButton.setAlignmentY(0.0F);
        AssessmentButton.setBorder(null);
        AssessmentButton.setBorderPainted(false);
        AssessmentButton.setContentAreaFilled(false);
        AssessmentButton.setDefaultCapable(false);
        AssessmentButton.setFocusPainted(false);
        AssessmentButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AssessmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AssessmentButtonActionPerformed(evt);
            }
        });

        GradingButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        GradingButton.setForeground(new java.awt.Color(248, 248, 248));
        GradingButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconGrading.png"))); // NOI18N
        GradingButton.setText("    Grading");
        GradingButton.setToolTipText("");
        GradingButton.setAlignmentY(0.0F);
        GradingButton.setBorder(null);
        GradingButton.setBorderPainted(false);
        GradingButton.setContentAreaFilled(false);
        GradingButton.setDefaultCapable(false);
        GradingButton.setFocusPainted(false);
        GradingButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        GradingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GradingButtonActionPerformed(evt);
            }
        });

        ProfileButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        ProfileButton.setForeground(new java.awt.Color(248, 248, 248));
        ProfileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconProfile.png"))); // NOI18N
        ProfileButton.setText("    Profile");
        ProfileButton.setAlignmentY(0.0F);
        ProfileButton.setBorder(null);
        ProfileButton.setBorderPainted(false);
        ProfileButton.setContentAreaFilled(false);
        ProfileButton.setDefaultCapable(false);
        ProfileButton.setFocusPainted(false);
        ProfileButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ProfileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProfileButtonActionPerformed(evt);
            }
        });

        AnalyticsButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        AnalyticsButton.setForeground(new java.awt.Color(236, 217, 226));
        AnalyticsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconAnalyticsClick.png"))); // NOI18N
        AnalyticsButton.setText("    Analytics");
        AnalyticsButton.setAlignmentY(0.0F);
        AnalyticsButton.setBorder(null);
        AnalyticsButton.setBorderPainted(false);
        AnalyticsButton.setContentAreaFilled(false);
        AnalyticsButton.setDefaultCapable(false);
        AnalyticsButton.setFocusPainted(false);
        AnalyticsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        AnalyticsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnalyticsButtonActionPerformed(evt);
            }
        });

        LogoutButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        LogoutButton.setForeground(new java.awt.Color(248, 248, 248));
        LogoutButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconLogout.png"))); // NOI18N
        LogoutButton.setText("    Logout");
        LogoutButton.setToolTipText("");
        LogoutButton.setAlignmentY(0.0F);
        LogoutButton.setBorder(null);
        LogoutButton.setBorderPainted(false);
        LogoutButton.setContentAreaFilled(false);
        LogoutButton.setDefaultCapable(false);
        LogoutButton.setFocusPainted(false);
        LogoutButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LogoutButtonActionPerformed(evt);
            }
        });

        ModuleButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        ModuleButton.setForeground(new java.awt.Color(248, 248, 248));
        ModuleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconModule.png"))); // NOI18N
        ModuleButton.setText("   Module");
        ModuleButton.setAlignmentY(0.0F);
        ModuleButton.setBorder(null);
        ModuleButton.setBorderPainted(false);
        ModuleButton.setContentAreaFilled(false);
        ModuleButton.setDefaultCapable(false);
        ModuleButton.setFocusPainted(false);
        ModuleButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ModuleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModuleButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SidePanelLayout = new javax.swing.GroupLayout(SidePanel);
        SidePanel.setLayout(SidePanelLayout);
        SidePanelLayout.setHorizontalGroup(
            SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidePanelLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SidePanelLayout.createSequentialGroup()
                        .addGap(0, 16, Short.MAX_VALUE)
                        .addComponent(logo)
                        .addGap(28, 28, 28))
                    .addGroup(SidePanelLayout.createSequentialGroup()
                        .addGroup(SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(ClassButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(DashboardButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(ModuleButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(AssessmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SidePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ProfileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(AnalyticsButton, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addComponent(LogoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(GradingButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(65, 65, 65))
        );
        SidePanelLayout.setVerticalGroup(
            SidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SidePanelLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(DashboardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ModuleButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ClassButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AssessmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(GradingButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AnalyticsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LogoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(229, Short.MAX_VALUE))
        );

        TitleP1.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        TitleP1.setText("Analytics");

        SaveReportButton.setBackground(new java.awt.Color(229, 218, 235));
        SaveReportButton.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        SaveReportButton.setText("Save Report");
        SaveReportButton.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        SaveReportButton.setFocusPainted(false);
        SaveReportButton.setFocusable(false);
        SaveReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        SaveReportButton.setIconTextGap(-10);
        SaveReportButton.setOpaque(true);
        SaveReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveReportButtonActionPerformed(evt);
            }
        });

        ClassSelectionBox.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ClassSelectionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ClassSelectionBox.setToolTipText("");
        ClassSelectionBox.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ClassSelectionBox.setDoubleBuffered(true);
        ClassSelectionBox.setFocusable(false);
        ClassSelectionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClassSelectionBoxActionPerformed(evt);
            }
        });

        ModuleSelectionBox.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ModuleSelectionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ModuleSelectionBox.setToolTipText("");
        ModuleSelectionBox.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ModuleSelectionBox.setDoubleBuffered(true);
        ModuleSelectionBox.setFocusable(false);
        ModuleSelectionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModuleSelectionBoxActionPerformed(evt);
            }
        });

        toppanel.setLayer(ClassSelectionBox, javax.swing.JLayeredPane.DEFAULT_LAYER);
        toppanel.setLayer(ModuleSelectionBox, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout toppanelLayout = new javax.swing.GroupLayout(toppanel);
        toppanel.setLayout(toppanelLayout);
        toppanelLayout.setHorizontalGroup(
            toppanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toppanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(toppanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(ClassSelectionBox, 0, 727, Short.MAX_VALUE)
                    .addComponent(ModuleSelectionBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        toppanelLayout.setVerticalGroup(
            toppanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(toppanelLayout.createSequentialGroup()
                .addComponent(ModuleSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ClassSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout LeftJPanelLayout = new javax.swing.GroupLayout(LeftJPanel);
        LeftJPanel.setLayout(LeftJPanelLayout);
        LeftJPanelLayout.setHorizontalGroup(
            LeftJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 365, Short.MAX_VALUE)
        );
        LeftJPanelLayout.setVerticalGroup(
            LeftJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 309, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout RightJPanelLayout = new javax.swing.GroupLayout(RightJPanel);
        RightJPanel.setLayout(RightJPanelLayout);
        RightJPanelLayout.setHorizontalGroup(
            RightJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        RightJPanelLayout.setVerticalGroup(
            RightJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        IconPanel.setBackground(new java.awt.Color(255, 255, 255));

        UserIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/User Icon.png"))); // NOI18N

        UserName.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        UserName.setText("xxxxxxxxx");

        UserRole.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        UserRole.setText("Lecturer");

        javax.swing.GroupLayout IconPanelLayout = new javax.swing.GroupLayout(IconPanel);
        IconPanel.setLayout(IconPanelLayout);
        IconPanelLayout.setHorizontalGroup(
            IconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(IconPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(UserIcon)
                .addGap(6, 6, 6)
                .addGroup(IconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(UserName)
                    .addComponent(UserRole)))
        );
        IconPanelLayout.setVerticalGroup(
            IconPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(UserIcon)
            .addGroup(IconPanelLayout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(UserName)
                .addGap(6, 6, 6)
                .addComponent(UserRole))
        );

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(SidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(MainPanelLayout.createSequentialGroup()
                                        .addComponent(LeftJPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(RightJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(toppanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(32, Short.MAX_VALUE))
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addComponent(TitleP1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(IconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(44, 44, 44))))
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(347, 347, 347)
                        .addComponent(SaveReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TitleP1)
                    .addComponent(IconPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(toppanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(LeftJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(RightJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(SaveReportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(SidePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void DashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardButtonActionPerformed
        LecturerDashboard modulePage = new LecturerDashboard(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DashboardButtonActionPerformed

    private void ClassButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClassButtonActionPerformed
        LecturerClass modulePage = new LecturerClass(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ClassButtonActionPerformed

    private void AssessmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AssessmentButtonActionPerformed
        LecturerAssessment modulePage = new LecturerAssessment(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_AssessmentButtonActionPerformed

    private void GradingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GradingButtonActionPerformed
        LecturerGrading modulePage = new LecturerGrading(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_GradingButtonActionPerformed

    private void ProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProfileButtonActionPerformed
        LecturerProfile modulePage = new LecturerProfile(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ProfileButtonActionPerformed

    private void AnalyticsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnalyticsButtonActionPerformed
        LecturerAnalytics modulePage = new LecturerAnalytics(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_AnalyticsButtonActionPerformed

    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogoutButtonActionPerformed
        int result = JOptionPane.showConfirmDialog(null, 
                "Logging out?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            new AFS_LoginPage().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_LogoutButtonActionPerformed

    private void ModuleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModuleButtonActionPerformed
        LecturerModule modulePage = new LecturerModule(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ModuleButtonActionPerformed

    private void ModuleSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModuleSelectionBoxActionPerformed
        String selectedModule = (String) ModuleSelectionBox.getSelectedItem();
        loadClassDropdown(selectedModule);
        loadAnalyticsData();
    }//GEN-LAST:event_ModuleSelectionBoxActionPerformed

    private void ClassSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClassSelectionBoxActionPerformed
        loadAnalyticsData();
    }//GEN-LAST:event_ClassSelectionBoxActionPerformed

    private void SaveReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveReportButtonActionPerformed
        String selectedModule = (String) ModuleSelectionBox.getSelectedItem();
        String selectedClassInfo = (String) ClassSelectionBox.getSelectedItem();

        String classID = selectedClassInfo.split(" - ")[0];

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV File", "csv"));
        chooser.setSelectedFile(new File("AFS_Class_Analytics_" +
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {

            try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {

                writer.write("Class Analytics Report\n");
                writer.write("Generated:," +
                    new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()) + "\n");
                writer.write("Lecturer:," + lecturer.getName() + "\n");
                writer.write("Module:," + selectedModule + "\n");
                writer.write("Class:," + selectedClassInfo + "\n\n");

                // Grade distribution
                Map<String,Integer> grades = lecturer.getClassGradeDistribution(classID);

                writer.write("GRADE DISTRIBUTION\n");
                writer.write("Grade,Students\n");

                for (Map.Entry<String,Integer> e : grades.entrySet()) {
                    writer.write(e.getKey() + "," + e.getValue() + "\n");
                }

                // Stats
                Map<String,Object> stats = calculateClassStats(classID);

                writer.write("\nSTATISTICS\n");
                writer.write("Metric,Value\n");
                writer.write("Average," + stats.get("average") + "\n");
                writer.write("Highest," + stats.get("highest") + "\n");
                writer.write("Lowest," + stats.get("lowest") + "\n");
                writer.write("Student Count," + stats.get("count") + "\n");

                writer.write("\n--- End of Report ---\n");

                JOptionPane.showMessageDialog(this,
                    "Report saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_SaveReportButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnalyticsButton;
    private javax.swing.JButton AssessmentButton;
    private javax.swing.JButton ClassButton;
    private javax.swing.JComboBox<String> ClassSelectionBox;
    private javax.swing.JButton DashboardButton;
    private javax.swing.JButton GradingButton;
    private javax.swing.JPanel IconPanel;
    private javax.swing.JPanel LeftJPanel;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton ModuleButton;
    private javax.swing.JComboBox<String> ModuleSelectionBox;
    private javax.swing.JButton ProfileButton;
    private javax.swing.JPanel RightJPanel;
    private javax.swing.JButton SaveReportButton;
    private javax.swing.JPanel SidePanel;
    private javax.swing.JLabel TitleP1;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserRole;
    private javax.swing.JLabel logo;
    private javax.swing.JLayeredPane toppanel;
    // End of variables declaration//GEN-END:variables
}
