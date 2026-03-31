package Lecturer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import Classes.Assessment;
import Classes.Lecturer;
import Login.AFS_LoginPage;
import javax.swing.JOptionPane;

public class LecturerAssessment extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LecturerAssessment.class.getName());
    
    private Lecturer lecturer;
    
    public LecturerAssessment(Lecturer lecturer) {
        initComponents();
        styleAssessmentListTable();
        styleScrollBar();
        
        jScrollPane1.getViewport().setBackground(Color.WHITE);
        jScrollPane1.setBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220))
        );
        
        AssessmentListTable.setFillsViewportHeight(true);
    
        ClassSelectionBox.setBackground(new Color(255, 255, 255));
        
        this.lecturer = lecturer;
        
        if (this.lecturer != null) {
            UserName.setText(this.lecturer.getName());
        }
        
        loadClassDropdown();
        loadAssessmentList(null);
    }
    
    private void styleScrollBar() {
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(3, 0));
        jScrollPane1.setBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220))
        );
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
                    btn.setMinimumSize(new Dimension(0, 0));
                    btn.setMaximumSize(new Dimension(0, 0));
                    return btn;
                }    
            }
        );

    }
    
    private void styleAssessmentListTable() {
        AssessmentListTable.setRowHeight(30);
        AssessmentListTable.setShowGrid(true);
        AssessmentListTable.setGridColor(new Color(220, 220, 220));

        javax.swing.table.JTableHeader header = AssessmentListTable.getTableHeader();
        header.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
        header.setBackground(new java.awt.Color(229, 218, 235));
        header.setForeground(new java.awt.Color(50, 50, 50));
        header.setOpaque(true);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));
        
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 30));
        AssessmentListTable.setSelectionBackground(new java.awt.Color(248, 248, 248));
        AssessmentListTable.setBackground(new java.awt.Color(255, 255, 255));
        AssessmentListTable.setOpaque(true);
        

        AssessmentListTable.getColumnModel()
            .getColumn(3)
            .setCellRenderer(new PaddedCenterRenderer(JLabel.CENTER));

        AssessmentListTable.getColumnModel()
            .getColumn(4)
            .setCellRenderer(new PaddedCenterRenderer(JLabel.CENTER));

        AssessmentListTable.getColumnModel()
            .getColumn(1)
            .setCellRenderer(new PaddedCenterRenderer(JLabel.LEFT));

        AssessmentListTable.getColumnModel()
            .getColumn(2)
            .setCellRenderer(new PaddedCenterRenderer(JLabel.LEFT));

        AssessmentListTable.getColumnModel()
            .getColumn(5)
            .setCellRenderer(new ButtonRenderer());

        AssessmentListTable.getColumnModel()
            .getColumn(5)
            .setCellEditor(
                new ButtonEditor(new javax.swing.JCheckBox())
            );        
        
        // Hide first column
        AssessmentListTable.getColumnModel().getColumn(0).setMinWidth(0); 
        AssessmentListTable.getColumnModel().getColumn(0).setMaxWidth(0);
        AssessmentListTable.getColumnModel().getColumn(0).setWidth(0);
    }
    
    class PaddedCenterRenderer extends DefaultTableCellRenderer {

        private final int alignment;

        public PaddedCenterRenderer(int alignment) {
            this.alignment = alignment;
            setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            JLabel c = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
            );

            c.setHorizontalAlignment(alignment);
            c.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

            return c;
        }
    }

    class ButtonRenderer extends javax.swing.JButton
        implements javax.swing.table.TableCellRenderer {

            public ButtonRenderer() {
                java.net.URL imgURL =
                    getClass().getResource("/resources/DeleteIcon4.png");

                if (imgURL != null) {
                    setIcon(new ImageIcon(imgURL));
                } else {
                    System.out.println("Delete icon not found!");
                }
                setOpaque(true);
                setFocusPainted(false);
                setBackground(new Color(255, 255, 255));
                setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 12));
                setBorder(
                    BorderFactory.createLineBorder(new Color(200, 180, 210))
                );
                setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
                setToolTipText("Delete assessment");
            }

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                if (isSelected) {
                    setBackground(new Color(248, 248, 248));
                } else {
                    setBackground(Color.WHITE);
                }

                return this;
            }

    }
    
    class ButtonEditor extends javax.swing.DefaultCellEditor {
        private final JButton button;
        private int selectedRow;

        public ButtonEditor(javax.swing.JCheckBox checkBox) {
            super(checkBox);

            button = new JButton("Delete");
            button.setFocusPainted(false);
            button.setBackground(new Color(229, 218, 235));

            button.addActionListener(e -> {
                fireEditingStopped();

                String assessID =
                    AssessmentListTable.getValueAt(selectedRow, 0).toString();

                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    button,
                    "Are you sure you want to delete this assessment?",
                    "Confirm Delete",
                    javax.swing.JOptionPane.YES_NO_OPTION
                );

                if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

                boolean deleted = lecturer.deleteAssessment(assessID);

                if (!deleted) {
                    javax.swing.JOptionPane.showMessageDialog(
                        button,
                        "❌ Cannot delete.\nThis assessment has already been graded.",
                        "Delete Blocked",
                        javax.swing.JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                javax.swing.JOptionPane.showMessageDialog(
                    button,
                    "✅ Assessment deleted successfully.",
                    "Deleted",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                );

                loadAssessmentList(
                    (String) ClassSelectionBox.getSelectedItem()
                );
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {

            selectedRow = row;
            return button;
        }
    }

    private void loadAssessmentList(String classFilter) {
        javax.swing.table.DefaultTableModel model =
            (javax.swing.table.DefaultTableModel) AssessmentListTable.getModel();

        model.setRowCount(0);

        int rowCount = 0;

        for (Assessment a : lecturer.getAssessments()) {

            if (classFilter != null && !classFilter.equals("All Classes")
                && !a.getClassID().equals(classFilter)) {
                continue;
            }
            

            model.addRow(new Object[]{
                a.getAssessID(),      // hidden
                a.getAssessType(),
                a.getClassID(),
                a.getFullMark(),
                a.getWeightage(),
                "Delete"
            });

            rowCount++;
        }

        if (rowCount == 0) {
            showEmptyMessage("No assessments found for the selected class");
        } else {
            jScrollPane1.setViewportView(AssessmentListTable);
        }
    }


    private void loadClassDropdown() {
        ClassSelectionBox.removeAllItems();

        ClassSelectionBox.addItem("All Classes");

        if (lecturer == null) 
            return;

        for (Classes.Class c : lecturer.getClasses()) {
            ClassSelectionBox.addItem(c.getClassID());
        }
    }
    
    private void showEmptyMessage(String message) {
        JLabel emptyLabel = new JLabel(message);
        emptyLabel.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 13));
        emptyLabel.setForeground(new Color(120, 120, 120));
        emptyLabel.setHorizontalAlignment(JLabel.CENTER);

        jScrollPane1.setViewportView(emptyLabel);
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
        ClassSelectionBox = new javax.swing.JComboBox<>();
        DesignAssessmentButton = new javax.swing.JButton();
        H2AssessmentList = new javax.swing.JLabel();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        AssessmentListTable = new javax.swing.JTable();

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
        AssessmentButton.setForeground(new java.awt.Color(236, 217, 226));
        AssessmentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconAssClick.png"))); // NOI18N
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
        LogoutButton.setText("     Logout");
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
        TitleP1.setText("Assessments");

        UserIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/User Icon.png"))); // NOI18N

        UserName.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        UserName.setText("xxxxxxxxx");

        UserRole.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        UserRole.setText("Lecturer");

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

        DesignAssessmentButton.setBackground(new java.awt.Color(241, 236, 246));
        DesignAssessmentButton.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        DesignAssessmentButton.setText("Design Assessment");
        DesignAssessmentButton.setFocusPainted(false);
        DesignAssessmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DesignAssessmentButtonActionPerformed(evt);
            }
        });

        H2AssessmentList.setFont(new java.awt.Font("Arial", 1, 17)); // NOI18N
        H2AssessmentList.setText("Assessments List");

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        AssessmentListTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        AssessmentListTable.setModel(new javax.swing.table.DefaultTableModel(
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
                "Assessment ID", "Assessment Type", "Class", "Full Mark", "Weightage", "Action"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        AssessmentListTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(AssessmentListTable);
        if (AssessmentListTable.getColumnModel().getColumnCount() > 0) {
            AssessmentListTable.getColumnModel().getColumn(5).setMinWidth(90);
            AssessmentListTable.getColumnModel().getColumn(5).setPreferredWidth(90);
            AssessmentListTable.getColumnModel().getColumn(5).setMaxWidth(90);
        }

        jLayeredPane1.setLayer(jScrollPane1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 728, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(SidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addComponent(H2AssessmentList)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainPanelLayout.createSequentialGroup()
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLayeredPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(MainPanelLayout.createSequentialGroup()
                                .addComponent(TitleP1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(UserIcon)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(UserRole)
                                    .addComponent(UserName)))
                            .addComponent(ClassSelectionBox, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(DesignAssessmentButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(43, 43, 43))))
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
                .addComponent(DesignAssessmentButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ClassSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(H2AssessmentList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void ClassSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClassSelectionBoxActionPerformed
        String selectedClass =
        (String) ClassSelectionBox.getSelectedItem();

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

    private void DesignAssessmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DesignAssessmentButtonActionPerformed
        LecturerAssessment_DesignAssessment modulePage = new LecturerAssessment_DesignAssessment(lecturer);
        modulePage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_DesignAssessmentButtonActionPerformed

    /**
     * @param args the command line arguments
     */


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnalyticsButton;
    private javax.swing.JButton AssessmentButton;
    private javax.swing.JTable AssessmentListTable;
    private javax.swing.JButton ClassButton;
    private javax.swing.JComboBox<String> ClassSelectionBox;
    private javax.swing.JButton DashboardButton;
    private javax.swing.JButton DesignAssessmentButton;
    private javax.swing.JButton GradingButton;
    private javax.swing.JLabel H2AssessmentList;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton ModuleButton;
    private javax.swing.JButton ProfileButton;
    private javax.swing.JPanel SidePanel;
    private javax.swing.JLabel TitleP1;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserRole;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logo;
    // End of variables declaration//GEN-END:variables
}
