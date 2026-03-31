package Lecturer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import Classes.Assessment;
import Classes.Lecturer;
import Classes.Class;
import Login.AFS_LoginPage;

public class LecturerGrading extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LecturerGrading.class.getName());
    private Lecturer lecturer;

    public LecturerGrading(Lecturer lecturer) {
        initComponents();
        ClassSelectionBox.setBackground(new Color(255, 255, 255));

        this.lecturer = lecturer;
        if (this.lecturer != null) {
            UserName.setText(this.lecturer.getName());
        }
        UserRole.setText("Lecturer");

        styleGradingTable();
        styleScrollBar();

        jScrollPane1.getViewport().setBackground(Color.WHITE);
        jScrollPane1.setBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220))
        );

        AssessmentMarkingListTable.setFillsViewportHeight(true);

        loadClassDropdown();
        loadAssessmentList(null);
    }
    
    private void styleScrollBar() {
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(3, 0));

        jScrollPane1.getVerticalScrollBar().setUI(
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
    
    private void styleGradingTable() {
        AssessmentMarkingListTable.setRowHeight(30);
        AssessmentMarkingListTable.setShowGrid(true);
        AssessmentMarkingListTable.setGridColor(new Color(220, 220, 220));
        

        JTableHeader header = AssessmentMarkingListTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBackground(new Color(229, 218, 235));
        header.setForeground(new Color(50, 50, 50));
        header.setBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200))
        );
        header.setPreferredSize(
            new Dimension(header.getPreferredSize().width, 30)
        );

        AssessmentMarkingListTable.setBackground(Color.WHITE);
        AssessmentMarkingListTable.setSelectionBackground(
            new Color(248, 248, 248)
        );

        // Alignment + padding
        AssessmentMarkingListTable.getColumnModel()
            .getColumn(0)
            .setCellRenderer(new PaddedRenderer(JLabel.LEFT));

        AssessmentMarkingListTable.getColumnModel()
            .getColumn(1)
            .setCellRenderer(new PaddedRenderer(JLabel.LEFT));
        
        AssessmentMarkingListTable.getColumnModel()
            .getColumn(2) // Weightage
            .setCellRenderer(new PaddedRenderer(JLabel.CENTER));

        AssessmentMarkingListTable.getColumnModel()
            .getColumn(3) // 
            .setCellRenderer(new PaddedRenderer(JLabel.CENTER));
        
        AssessmentMarkingListTable.getColumnModel()
            .getColumn(4) // Students Marked
            .setCellRenderer(new PaddedRenderer(JLabel.CENTER));

        AssessmentMarkingListTable.getColumnModel()
            .getColumn(5) // Status column
            .setCellRenderer(new StatusBadgeRenderer());    

        // Action button
        AssessmentMarkingListTable.getColumnModel()
            .getColumn(7)
            .setCellRenderer(new ActionButtonRenderer());

        AssessmentMarkingListTable.getColumnModel()
            .getColumn(7)
            .setCellEditor(new ActionButtonEditor(new JCheckBox()));

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

    class ActionButtonRenderer extends JButton implements TableCellRenderer {
        public ActionButtonRenderer() {
            setFocusPainted(false);
            setFont(new Font("Arial", Font.PLAIN, 12));
            setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column
        ) {
            String status = table.getValueAt(row, 5).toString();

            setText(status.equals("Done") ? "View" : "Mark");
            setBackground(isSelected
                ? new Color(248, 248, 248)
                : Color.WHITE
            );

            return this;
        }
    }
    
    class ActionButtonEditor extends DefaultCellEditor {        
        private JButton button;
        private int selectedRow;

        public ActionButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            button = new JButton();
            button.setFocusPainted(false);
            button.setBackground(new Color(229, 218, 235));

            button.addActionListener(e -> {
                fireEditingStopped();

                String assessID = AssessmentMarkingListTable
                    .getValueAt(selectedRow, 6).toString();

                String status = AssessmentMarkingListTable
                    .getValueAt(selectedRow, 5).toString();

                // weightage check
                Assessment a = lecturer.getAssessmentByID(assessID);
                double totalWeightage =
                    lecturer.getTotalWeightageForClass(a.getClassID());

                if (totalWeightage < 1.0) {
                    JOptionPane.showMessageDialog(
                        LecturerGrading.this,
                        "Assessment weightage for this class is not complete (must be 100%).",
                        "Cannot Mark",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                // Only open marking page if weightage = 1.0
                LecturerGrading_Marking page =
                    new LecturerGrading_Marking(
                        lecturer,
                        assessID,
                        status.equals("Done")
                    );

                page.setVisible(true);
                LecturerGrading.this.dispose();
            });

        }

        @Override
        public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected, int row, int column
        ) {
            selectedRow = row;

            String status = table.getValueAt(row, 5).toString();
            button.setText(status.equals("Done") ? "View" : "Mark");

            return button;
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
                case "Done" -> {
                    label.setBackground(new Color(220, 245, 225));
                    label.setForeground(new Color(40, 120, 60));
                }
                case "Partial" -> {
                    label.setBackground(new Color(255, 240, 210));
                    label.setForeground(new Color(160, 110, 40));
                }
                default -> { // Not Done
                    label.setBackground(new Color(240, 240, 240));
                    label.setForeground(new Color(120, 120, 120));
                }
            }

            return label;
        }
    }

    private void loadClassDropdown() {
        ClassSelectionBox.removeAllItems();
        ClassSelectionBox.addItem("All Classes");

        if (lecturer == null) return;

        for (Class c : lecturer.getClasses()) {
            ClassSelectionBox.addItem(c.getClassID());
        }
    }

    private void loadAssessmentList(String classFilter) {
        DefaultTableModel model =
            (DefaultTableModel) AssessmentMarkingListTable.getModel();
        model.setRowCount(0);

        int rowCount = 0;

        for (Assessment a : lecturer.getAssessments()) {

            if (classFilter != null && !classFilter.equals("All Classes")
                && !a.getClassID().equals(classFilter)) {
                continue;
            }

            int totalStudents = a.getTotalStudents();      
            int markedStudents = a.getMarkedCount();      
            String status = a.getGradingStatus();         

            model.addRow(new Object[]{
                a.getAssessType(),        
                a.getClassID(),           
                a.getFullMark(),          
                a.getWeightage(),         
                markedStudents + "/" + totalStudents,
                status,                   
                a.getAssessID(),          // hidden (to save assessID)
                "Action"                  // action button
            });
            rowCount++;
        }

        if (rowCount == 0) {
            showEmptyMessage("No assessments found for the selected class");
        } else {
            jScrollPane1.setViewportView(AssessmentMarkingListTable);
        }
    }

    
    private void showEmptyMessage(String message) {
        JLabel label = new JLabel(message);
        label.setFont(new Font("Arial", Font.ITALIC, 13));
        label.setForeground(new Color(120, 120, 120));
        label.setHorizontalAlignment(JLabel.CENTER);

        jScrollPane1.setViewportView(label);
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
        ViewAllStudResultButton = new javax.swing.JButton();
        ClassSelectionBox = new javax.swing.JComboBox<>();
        H2AssessmentMarkingList = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AssessmentMarkingListTable = new javax.swing.JTable();
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TitleP1.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        TitleP1.setText("Grading");

        ViewAllStudResultButton.setBackground(new java.awt.Color(241, 236, 246));
        ViewAllStudResultButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        ViewAllStudResultButton.setText("View Overall Student Result");
        ViewAllStudResultButton.setFocusPainted(false);
        ViewAllStudResultButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ViewAllStudResultButtonActionPerformed(evt);
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

        H2AssessmentMarkingList.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        H2AssessmentMarkingList.setText("Assessments Marking List");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        AssessmentMarkingListTable.setAutoCreateRowSorter(true);
        AssessmentMarkingListTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        AssessmentMarkingListTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Assessment Type", "Class", "Full Mark", "Weightage", "Students Marked", "Status", "AssessID", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        AssessmentMarkingListTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(AssessmentMarkingListTable);
        if (AssessmentMarkingListTable.getColumnModel().getColumnCount() > 0) {
            AssessmentMarkingListTable.getColumnModel().getColumn(0).setResizable(false);
            AssessmentMarkingListTable.getColumnModel().getColumn(0).setPreferredWidth(124);
            AssessmentMarkingListTable.getColumnModel().getColumn(1).setResizable(false);
            AssessmentMarkingListTable.getColumnModel().getColumn(1).setPreferredWidth(100);
            AssessmentMarkingListTable.getColumnModel().getColumn(2).setResizable(false);
            AssessmentMarkingListTable.getColumnModel().getColumn(2).setPreferredWidth(94);
            AssessmentMarkingListTable.getColumnModel().getColumn(3).setResizable(false);
            AssessmentMarkingListTable.getColumnModel().getColumn(3).setPreferredWidth(94);
            AssessmentMarkingListTable.getColumnModel().getColumn(4).setResizable(false);
            AssessmentMarkingListTable.getColumnModel().getColumn(4).setPreferredWidth(112);
            AssessmentMarkingListTable.getColumnModel().getColumn(5).setResizable(false);
            AssessmentMarkingListTable.getColumnModel().getColumn(5).setPreferredWidth(102);
            AssessmentMarkingListTable.getColumnModel().getColumn(6).setMinWidth(0);
            AssessmentMarkingListTable.getColumnModel().getColumn(6).setPreferredWidth(0);
            AssessmentMarkingListTable.getColumnModel().getColumn(6).setMaxWidth(0);
            AssessmentMarkingListTable.getColumnModel().getColumn(7).setMinWidth(102);
            AssessmentMarkingListTable.getColumnModel().getColumn(7).setPreferredWidth(102);
            AssessmentMarkingListTable.getColumnModel().getColumn(7).setMaxWidth(102);
        }

        UserIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/User Icon.png"))); // NOI18N

        UserName.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        UserName.setText("xxxxxxxxx");

        UserRole.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        UserRole.setText("Lecturer");

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(SidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addComponent(TitleP1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(384, 384, 384)
                                .addComponent(UserIcon)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(UserRole)
                                    .addComponent(UserName)))
                            .addComponent(ClassSelectionBox, 0, 727, Short.MAX_VALUE)
                            .addComponent(ViewAllStudResultButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(H2AssessmentMarkingList, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 728, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(42, Short.MAX_VALUE))
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
                .addGap(26, 26, 26)
                .addComponent(ViewAllStudResultButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ClassSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(H2AssessmentMarkingList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
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

    private void ViewAllStudResultButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ViewAllStudResultButtonActionPerformed
        LecturerGrading_OverallResult modulePage = new LecturerGrading_OverallResult(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ViewAllStudResultButtonActionPerformed

    private void ClassSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClassSelectionBoxActionPerformed
        String selectedClass = (String) ClassSelectionBox.getSelectedItem();
        loadAssessmentList(selectedClass);
    }//GEN-LAST:event_ClassSelectionBoxActionPerformed

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


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnalyticsButton;
    private javax.swing.JButton AssessmentButton;
    private javax.swing.JTable AssessmentMarkingListTable;
    private javax.swing.JButton ClassButton;
    private javax.swing.JComboBox<String> ClassSelectionBox;
    private javax.swing.JButton DashboardButton;
    private javax.swing.JButton GradingButton;
    private javax.swing.JLabel H2AssessmentMarkingList;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton ModuleButton;
    private javax.swing.JButton ProfileButton;
    private javax.swing.JPanel SidePanel;
    private javax.swing.JLabel TitleP1;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserRole;
    private javax.swing.JButton ViewAllStudResultButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logo;
    // End of variables declaration//GEN-END:variables
}
