package Lecturer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Classes.Lecturer;
import Classes.Class;
import Classes.Module;
import Classes.Student;
import Classes.Result;
import Login.AFS_LoginPage;
import javax.swing.JOptionPane;

public class LecturerGrading_OverallResult extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LecturerGrading_OverallResult.class.getName());
    private Lecturer lecturer;
    
    public LecturerGrading_OverallResult(Lecturer lecturer) {
        initComponents();
        ClassSelectionBox.setBackground(new Color(255, 255, 255));
        ModuleSelectionBox.setBackground(new Color(255, 255, 255));
        
        this.lecturer = lecturer;
        if (this.lecturer != null) {
            UserName.setText(this.lecturer.getName());
        }
        UserRole.setText("Lecturer");
        
        styleScrollBar();
        styleClassResultTable();
        styleModuleResultTable();
        styleTabPane();
       
        
        loadClassDropdown();
        loadModuleDropdown();
        
    }
    
    private void loadClassDropdown() {
        ClassSelectionBox.removeAllItems();
        ClassSelectionBox.addItem("All Classes");

        if (lecturer == null) return;

        for (Class c : lecturer.getClasses()) {
            ClassSelectionBox.addItem(c.getClassID());
        }
    }
    
    private void loadModuleDropdown() {
        ModuleSelectionBox.removeAllItems();
        ModuleSelectionBox.addItem("All Module");

        if (lecturer == null) return;

        for (Module m : lecturer.getModules()) {
            ModuleSelectionBox.addItem(m.getModuleID());
        }
    }

    private void styleTabPane() {
        ContentTabbedPane.setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            
            @Override
            protected void paintTabBackground(Graphics g, int tabPlacement, 
                                               int tabIndex, int x, int y, int width, int height, 
                                               boolean isSelected) {
                g.setColor(isSelected ? new Color(182,158,196) : new Color(240, 240, 240));
                g.fillRect(x, y, width, height);
            }

            @Override
            protected void paintTabBorder(Graphics g, int tabPlacement, 
                                          int tabIndex, int x, int y, int width, int height, 
                                          boolean isSelected) {
                g.setColor(new Color(255, 255, 255));
                g.drawRect(x, y, width - 1, height - 1);
            }
        });
    }

    private void styleScrollBar() {
        jScrollPaneClassResult.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPaneClassResult.getVerticalScrollBar().setPreferredSize(new Dimension(3, 0));
        
        jScrollPaneModuleResult.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPaneModuleResult.getVerticalScrollBar().setPreferredSize(new Dimension(3, 0));

        jScrollPaneClassResult.getVerticalScrollBar().setUI(
            new javax.swing.plaf.basic.BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(180, 180, 180);
                    this.trackColor = Color.WHITE;
                }

                @Override
                protected JButton createDecreaseButton(int orientation) {
                    return createZeroButton();
                }

                @Override
                protected JButton createIncreaseButton(int orientation) {
                    return createZeroButton();
                }

                private JButton createZeroButton() {
                    JButton btn = new JButton();
                    btn.setPreferredSize(new Dimension(0, 0));
                    return btn;
                }
            }
        );
        
        jScrollPaneModuleResult.getVerticalScrollBar().setUI(
            new javax.swing.plaf.basic.BasicScrollBarUI() {
                @Override
                protected void configureScrollBarColors() {
                    this.thumbColor = new Color(180, 180, 180);
                    this.trackColor = Color.WHITE;
                }

                @Override
                protected JButton createDecreaseButton(int orientation) {
                    return createZeroButton();
                }

                @Override
                protected JButton createIncreaseButton(int orientation) {
                    return createZeroButton();
                }

                private JButton createZeroButton() {
                    JButton btn = new JButton();
                    btn.setPreferredSize(new Dimension(0, 0));
                    return btn;
                }
            }
        );
    }
    
    private void styleClassResultTable() {
        jScrollPaneClassResult.getViewport().setBackground(Color.WHITE);
        jScrollPaneClassResult.setBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220))
        );

        ClassResultListTable.setFillsViewportHeight(true);

        ClassResultListTable.setRowHeight(30);
        ClassResultListTable.setShowGrid(true);
        ClassResultListTable.setGridColor(new Color(220, 220, 220));
        

        JTableHeader header = ClassResultListTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(229, 218, 235));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200))
        );
        header.setPreferredSize(
            new Dimension(header.getPreferredSize().width, 30)
        );
        header.setOpaque(true);

        ClassResultListTable.setBackground(Color.WHITE);
        ClassResultListTable.setSelectionBackground(
            new Color(248, 248, 248)
        );
        ClassResultListTable.setOpaque(true);

        // Alignment + padding
        ClassResultListTable.getColumnModel()
            .getColumn(0)
            .setCellRenderer(new PaddedRenderer(JLabel.LEFT));

        ClassResultListTable.getColumnModel()
            .getColumn(1)
            .setCellRenderer(new PaddedRenderer(JLabel.LEFT));
        
        ClassResultListTable.getColumnModel()
            .getColumn(2) 
            .setCellRenderer(new PaddedRenderer(JLabel.CENTER));

        
        ClassResultListTable.getColumnModel()
            .getColumn(3) // Status column
            .setCellRenderer(new StatusBadgeRenderer());
           
    }
    
    private void styleModuleResultTable() {
        jScrollPaneModuleResult.getViewport().setBackground(Color.WHITE);
        jScrollPaneModuleResult.setBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220))
        );

        ModuleResultListTable.setFillsViewportHeight(true);
        
        ModuleResultListTable.setRowHeight(30);
        ModuleResultListTable.setShowGrid(true);
        ModuleResultListTable.setGridColor(new Color(220, 220, 220));
        

        JTableHeader header = ModuleResultListTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(229, 218, 235));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200))
        );
        header.setPreferredSize(
            new Dimension(header.getPreferredSize().width, 30)
        );

        ModuleResultListTable.setBackground(Color.WHITE);
        ModuleResultListTable.setSelectionBackground(
            new Color(248, 248, 248)
        );

        // Alignment + padding
        ModuleResultListTable.getColumnModel()
            .getColumn(0)
            .setCellRenderer(new PaddedRenderer(JLabel.LEFT));

        ModuleResultListTable.getColumnModel()
            .getColumn(1)
            .setCellRenderer(new PaddedRenderer(JLabel.LEFT));
        
        ModuleResultListTable.getColumnModel()
            .getColumn(2) 
            .setCellRenderer(new PaddedRenderer(JLabel.CENTER));

        ModuleResultListTable.getColumnModel()
            .getColumn(3) 
            .setCellRenderer(new PaddedRenderer(JLabel.CENTER));
        
        ModuleResultListTable.getColumnModel()
            .getColumn(4) 
            .setCellRenderer(new PaddedRenderer(JLabel.CENTER));
        
        ModuleResultListTable.getColumnModel()
            .getColumn(5) 
            .setCellRenderer(new PaddedRenderer(JLabel.CENTER));
    }
    
    class PaddedRenderer extends DefaultTableCellRenderer {
    private final int alignment;
        public PaddedRenderer(int alignment) {
            this.alignment = alignment;
            setOpaque(true); 
        }

        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column
        ) {

            JLabel c = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
            );
            c.setHorizontalAlignment(alignment);
            c.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
            return c;
        }
    }

    class StatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column
        ) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column
            );

            String status = value.toString();

            label.setHorizontalAlignment(JLabel.CENTER);
            label.setOpaque(true);

            // gap between cell border & badge
            label.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

            switch (status) {
                case "Not Marked" -> {
                    label.setBackground(new Color(255, 247, 241));
                    label.setForeground(new Color(160, 110, 40));
                }
                default -> { // Marked
                    label.setBackground(new Color(240, 240, 240));
                    label.setForeground(new Color(120, 120, 120));
                }
            }

            return label;
        }
    }

    public void loadClassResults(String selectedClass) {
        // Create a model for the table
        DefaultTableModel model = (DefaultTableModel) ClassResultListTable.getModel();
        model.setRowCount(0); // Clear existing rows

        if ("All Classes".equals(selectedClass)) {
            // Retrieve all classes the lecturer is teaching
            List<Class> allClasses = lecturer.getClasses();

            // Loop through each class
            for (Class classObj : allClasses) {
                // Retrieve all students for this class
                List<Student> students = lecturer.getStudentsForClass(classObj.getClassID());

                // For each student, retrieve their results and add to table
                for (Student student : students) {
                    Result result = lecturer.getResultForStudentInClass(student.getStudentID(), classObj.getClassID());

                    // Get the mark, if result is available
                    String mark = result != null && result.getMarkReceived() != 0 ? String.valueOf(result.getMarkReceived()) : "";

                    // Add the student and their result to the table
                    model.addRow(new Object[]{
                        student.getStudentID(),
                        student.getName(),
                        mark, 
                        mark.equals("") ? "Not Marked" : "Marked"
                    });
                }
            }
        } else {
            //Get the class by class ID directly
            Class selectedClassObj = lecturer.getClassByID(selectedClass);

            if (selectedClassObj == null) {
                System.out.println("Class not found: " + selectedClass);
                return;
            }

            System.out.println("Selected Class ID: " + selectedClassObj.getClassID());

            // Retrieve all students for this class
            List<Student> students = lecturer.getStudentsForClass(selectedClassObj.getClassID());

            // For each student, retrieve their results and add to table
            for (Student student : students) {
                Result result = lecturer.getResultForStudentInClass(student.getStudentID(), selectedClassObj.getClassID());

                // Get the mark, if result is available
                String mark = result != null && result.getMarkReceived() != 0 ? String.valueOf(result.getMarkReceived()) : "";

                // Add the student and their result to the table
                model.addRow(new Object[]{
                    student.getStudentID(),
                    student.getName(),
                    mark,
                    mark.equals("") ? "Not Marked" : "Marked"
                });
            }
        }
    }

    private void loadModuleResults(String selectedModule) {
        // Create a model for the table
        DefaultTableModel model = (DefaultTableModel) ModuleResultListTable.getModel();
        model.setRowCount(0); // Clear existing rows

        if (selectedModule == null || "All Module".equals(selectedModule)) {
            // Show all modules taught by lecturer
            List<Module> lecturerModules = lecturer.getModules();

            for (Module module : lecturerModules) {
                // Get results for this module
                List<String[]> moduleResults = lecturer.getModuleResults(module.getModuleID());

                for (String[] row : moduleResults) {

                    model.addRow(row);
                }
            }
        } else {
            // Show specific module results
            List<String[]> moduleResults = lecturer.getModuleResults(selectedModule);

            for (String[] row : moduleResults) {
                model.addRow(row);
            }
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
        UserIcon = new javax.swing.JLabel();
        UserName = new javax.swing.JLabel();
        UserRole = new javax.swing.JLabel();
        H2StudentResultSummary = new javax.swing.JLabel();
        ContentTabbedPane = new javax.swing.JTabbedPane();
        ClassPanel = new javax.swing.JPanel();
        ClassSelectionBox = new javax.swing.JComboBox<>();
        jScrollPaneClassResult = new javax.swing.JScrollPane();
        ClassResultListTable = new javax.swing.JTable();
        ModulePanel = new javax.swing.JPanel();
        ModuleSelectionBox = new javax.swing.JComboBox<>();
        jScrollPaneModuleResult = new javax.swing.JScrollPane();
        ModuleResultListTable = new javax.swing.JTable();
        BackButton = new javax.swing.JButton();

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
        GradingButton.setForeground(new java.awt.Color(236, 217, 226));
        GradingButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconGradingClick.png"))); // NOI18N
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
        AnalyticsButton.setForeground(new java.awt.Color(248, 248, 248));
        AnalyticsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconAnalytics.png"))); // NOI18N
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
        TitleP1.setText("Grading");

        UserIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/User Icon.png"))); // NOI18N

        UserName.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        UserName.setText("xxxxxxxxx");

        UserRole.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        UserRole.setText("Lecturer");

        H2StudentResultSummary.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        H2StudentResultSummary.setText("Student Result Summary");

        ContentTabbedPane.setBackground(new java.awt.Color(255, 255, 255));
        ContentTabbedPane.setFocusable(false);
        ContentTabbedPane.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        ContentTabbedPane.setMinimumSize(new java.awt.Dimension(67, 65));
        ContentTabbedPane.setPreferredSize(new java.awt.Dimension(364, 100));
        ContentTabbedPane.setRequestFocusEnabled(false);

        ClassPanel.setBackground(new java.awt.Color(255, 255, 255));

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

        jScrollPaneClassResult.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPaneClassResult.setBorder(null);
        jScrollPaneClassResult.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        ClassResultListTable.setAutoCreateRowSorter(true);
        ClassResultListTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        ClassResultListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Student ID", "Name", "Class Mark %", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ClassResultListTable.getTableHeader().setReorderingAllowed(false);
        jScrollPaneClassResult.setViewportView(ClassResultListTable);
        if (ClassResultListTable.getColumnModel().getColumnCount() > 0) {
            ClassResultListTable.getColumnModel().getColumn(0).setMinWidth(100);
            ClassResultListTable.getColumnModel().getColumn(0).setMaxWidth(100);
            ClassResultListTable.getColumnModel().getColumn(2).setMinWidth(100);
            ClassResultListTable.getColumnModel().getColumn(2).setMaxWidth(100);
            ClassResultListTable.getColumnModel().getColumn(3).setMinWidth(100);
            ClassResultListTable.getColumnModel().getColumn(3).setMaxWidth(100);
        }

        javax.swing.GroupLayout ClassPanelLayout = new javax.swing.GroupLayout(ClassPanel);
        ClassPanel.setLayout(ClassPanelLayout);
        ClassPanelLayout.setHorizontalGroup(
            ClassPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(ClassSelectionBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPaneClassResult, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
        );
        ClassPanelLayout.setVerticalGroup(
            ClassPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClassPanelLayout.createSequentialGroup()
                .addComponent(ClassSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPaneClassResult, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
        );

        ContentTabbedPane.addTab("      Class     ", ClassPanel);

        ModulePanel.setBackground(new java.awt.Color(255, 255, 255));

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

        jScrollPaneModuleResult.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPaneModuleResult.setBorder(null);
        jScrollPaneModuleResult.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        ModuleResultListTable.setAutoCreateRowSorter(true);
        ModuleResultListTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        ModuleResultListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Student ID", "Name", "Lecture Mark %", "Tutorial Mark %", "Final Mark %", "Grade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ModuleResultListTable.getTableHeader().setReorderingAllowed(false);
        jScrollPaneModuleResult.setViewportView(ModuleResultListTable);
        if (ModuleResultListTable.getColumnModel().getColumnCount() > 0) {
            ModuleResultListTable.getColumnModel().getColumn(0).setMinWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(0).setMaxWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(2).setMinWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(2).setMaxWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(3).setMinWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(3).setMaxWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(4).setMinWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(4).setMaxWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(5).setMinWidth(100);
            ModuleResultListTable.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        javax.swing.GroupLayout ModulePanelLayout = new javax.swing.GroupLayout(ModulePanel);
        ModulePanel.setLayout(ModulePanelLayout);
        ModulePanelLayout.setHorizontalGroup(
            ModulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModulePanelLayout.createSequentialGroup()
                .addGroup(ModulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ModuleSelectionBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ModulePanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jScrollPaneModuleResult, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        ModulePanelLayout.setVerticalGroup(
            ModulePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ModulePanelLayout.createSequentialGroup()
                .addComponent(ModuleSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPaneModuleResult, javax.swing.GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE))
        );

        ContentTabbedPane.addTab("     Module     ", ModulePanel);

        BackButton.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        BackButton.setText("<");
        BackButton.setBorder(null);
        BackButton.setBorderPainted(false);
        BackButton.setContentAreaFilled(false);
        BackButton.setFocusable(false);
        BackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BackButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(SidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                        .addComponent(TitleP1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 493, Short.MAX_VALUE)
                        .addComponent(UserIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserRole)
                            .addComponent(UserName))
                        .addGap(43, 43, 43))
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addComponent(BackButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(H2StudentResultSummary))
                            .addComponent(ContentTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(43, Short.MAX_VALUE))))
        );
        MainPanelLayout.setVerticalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TitleP1)
                    .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(UserIcon)
                        .addGroup(MainPanelLayout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(UserName)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(UserRole))))
                .addGap(18, 18, 18)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BackButton)
                    .addComponent(H2StudentResultSummary))
                .addGap(18, 18, 18)
                .addComponent(ContentTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
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

    private void ClassSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClassSelectionBoxActionPerformed
        String selectedClass = (String) ClassSelectionBox.getSelectedItem();
        loadClassResults(selectedClass);

    }//GEN-LAST:event_ClassSelectionBoxActionPerformed

    private void ModuleSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModuleSelectionBoxActionPerformed
        String selectedModule = (String) ModuleSelectionBox.getSelectedItem();
        loadModuleResults(selectedModule);
    }//GEN-LAST:event_ModuleSelectionBoxActionPerformed

    private void BackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackButtonActionPerformed
        LecturerGrading modulePage = new LecturerGrading(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_BackButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnalyticsButton;
    private javax.swing.JButton AssessmentButton;
    private javax.swing.JButton BackButton;
    private javax.swing.JButton ClassButton;
    private javax.swing.JPanel ClassPanel;
    private javax.swing.JTable ClassResultListTable;
    private javax.swing.JComboBox<String> ClassSelectionBox;
    private javax.swing.JTabbedPane ContentTabbedPane;
    private javax.swing.JButton DashboardButton;
    private javax.swing.JButton GradingButton;
    private javax.swing.JLabel H2StudentResultSummary;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton ModuleButton;
    private javax.swing.JPanel ModulePanel;
    private javax.swing.JTable ModuleResultListTable;
    private javax.swing.JComboBox<String> ModuleSelectionBox;
    private javax.swing.JButton ProfileButton;
    private javax.swing.JPanel SidePanel;
    private javax.swing.JLabel TitleP1;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserRole;
    private javax.swing.JScrollPane jScrollPaneClassResult;
    private javax.swing.JScrollPane jScrollPaneModuleResult;
    private javax.swing.JLabel logo;
    // End of variables declaration//GEN-END:variables
}
