package Lecturer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import Classes.Lecturer;
import Classes.Class;
import Login.AFS_LoginPage;
import javax.swing.JOptionPane;


public class LecturerClass extends javax.swing.JFrame {
    
    private Lecturer lecturer;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LecturerClass.class.getName());

    public LecturerClass(Lecturer lecturer){
        this(lecturer, null);
    }
    
    public LecturerClass(Lecturer lecturer, String moduleID) {
        initComponents();
        
        this.lecturer = lecturer;
       
        setupClassPage(moduleID);               
    }
    
    private void setupClassPage(String moduleID){
        if (this.lecturer != null) {
            UserName.setText(this.lecturer.getName());
        }
        UserRole.setText("Lecturer");
        
        CourseSelectionBox.setBackground(new Color(255, 255, 255));

        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);

        panelClassesContainer.setLayout(
            new BoxLayout(panelClassesContainer, BoxLayout.Y_AXIS)
        );

        loadCourseDropdown();

        if(moduleID != null){
            CourseSelectionBox.setSelectedItem(moduleID);
            loadClasses(moduleID);
        }else{
            javax.swing.SwingUtilities.invokeLater(() -> {
                loadClasses("All Courses");
            });
        }
    }
    
    private void loadClasses(String module){
        panelClassesContainer.removeAll();

        List<Class> classes = lecturer.getClassesByModule(module);

        boolean lectureHeaderAdded = false;
        boolean labHeaderAdded = false;

        for (Class c : classes) {

            if (c.getType().equalsIgnoreCase("Lecture")) {

                if (!lectureHeaderAdded) {
                    panelClassesContainer.add(createSectionLabel("Lecture"));
                    lectureHeaderAdded = true;
                }

                panelClassesContainer.add(createClassCard(c));
                panelClassesContainer.add(Box.createVerticalStrut(5)); 
            }

            else if (c.getType().equalsIgnoreCase("Lab")
                  || c.getType().equalsIgnoreCase("Tutorial")) {

                if (!labHeaderAdded) {
                    panelClassesContainer.add(createSectionLabel("Lab / Tutorial"));
                    labHeaderAdded = true;
                }

                panelClassesContainer.add(createClassCard(c));
                panelClassesContainer.add(Box.createVerticalStrut(5)); 
            }
        }
        panelClassesContainer.add(Box.createVerticalGlue());
        panelClassesContainer.revalidate();
        panelClassesContainer.repaint();
        
        
        jScrollPane1.revalidate();
        jScrollPane1.repaint();
    }

    private void loadCourseDropdown() {
        CourseSelectionBox.removeActionListener(
            CourseSelectionBox.getActionListeners()[0]
        );

        CourseSelectionBox.removeAllItems();
        CourseSelectionBox.addItem("All Courses");

        List<String> modules = lecturer.getModuleIDs();
        for (String m : modules) {
            CourseSelectionBox.addItem(m);
        }

        CourseSelectionBox.addActionListener(evt -> {
            Object selected = CourseSelectionBox.getSelectedItem();
            if (selected != null) {
                loadClasses(selected.toString());
            }
        });
    }

    private JLabel createSectionLabel(String text){
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.ITALIC, 16));
        label.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        return label;
    }
    
    private JPanel createClassCard(Class c){
        JPanel card = new JPanel();
        
        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2), // outer border
                BorderFactory.createEmptyBorder(10, 14, 10, 14)      // inner padding
            )
        );
        
        card.setBackground(new Color(255, 255, 255));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));


        JLabel id = new JLabel(c.getClassID());
        JLabel time = new JLabel(c.getClassDay() + " " + c.getStartTime());
        JLabel title = new JLabel(
            c.getClassID() + " - " + c.getName());
        
        id.setFont(new Font("Arial", Font.PLAIN, 12));
        time.setFont(new Font("Arial", Font.PLAIN, 12));
        title.setFont(new Font("Arial", Font.PLAIN, 12));
        
        card.add(id);
        card.add(title);
        card.add(time);

        return card;
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
        jScrollPane1 = new javax.swing.JScrollPane();
        panelClassesContainer = new javax.swing.JPanel();
        CourseSelectionBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

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
        ClassButton.setForeground(new java.awt.Color(236, 217, 226));
        ClassButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/SideIconClassClick.png"))); // NOI18N
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
        TitleP1.setText("My Classes");

        UserIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/User Icon.png"))); // NOI18N

        UserName.setFont(new java.awt.Font("Arial", 1, 11)); // NOI18N
        UserName.setText("xxxxxxxxx");

        UserRole.setFont(new java.awt.Font("Segoe UI", 0, 9)); // NOI18N
        UserRole.setText("Lecturer");

        jScrollPane1.setBorder(null);
        jScrollPane1.setHorizontalScrollBar(null);

        panelClassesContainer.setBackground(new java.awt.Color(255, 255, 255));
        panelClassesContainer.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
        panelClassesContainer.setForeground(new java.awt.Color(255, 255, 255));
        panelClassesContainer.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        javax.swing.GroupLayout panelClassesContainerLayout = new javax.swing.GroupLayout(panelClassesContainer);
        panelClassesContainer.setLayout(panelClassesContainerLayout);
        panelClassesContainerLayout.setHorizontalGroup(
            panelClassesContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 718, Short.MAX_VALUE)
        );
        panelClassesContainerLayout.setVerticalGroup(
            panelClassesContainerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(panelClassesContainer);

        CourseSelectionBox.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        CourseSelectionBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CourseSelectionBox.setFocusable(false);
        CourseSelectionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CourseSelectionBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MainPanelLayout = new javax.swing.GroupLayout(MainPanel);
        MainPanel.setLayout(MainPanelLayout);
        MainPanelLayout.setHorizontalGroup(
            MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainPanelLayout.createSequentialGroup()
                .addComponent(SidePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(TitleP1, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 493, Short.MAX_VALUE)
                        .addComponent(UserIcon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(UserRole)
                            .addComponent(UserName)))
                    .addGroup(MainPanelLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(MainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(CourseSelectionBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 678, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(43, 43, 43))
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
                .addComponent(CourseSelectionBox, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(47, Short.MAX_VALUE))
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

    private void CourseSelectionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CourseSelectionBoxActionPerformed
        Object selectedObj = CourseSelectionBox.getSelectedItem();

        if (selectedObj == null) {
            return; // combo is still initializing
        }

        String selected = selectedObj.toString();
        loadClasses(selected);
    }//GEN-LAST:event_CourseSelectionBoxActionPerformed

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

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AnalyticsButton;
    private javax.swing.JButton AssessmentButton;
    private javax.swing.JButton ClassButton;
    private javax.swing.JComboBox<String> CourseSelectionBox;
    private javax.swing.JButton DashboardButton;
    private javax.swing.JButton GradingButton;
    private javax.swing.JButton LogoutButton;
    private javax.swing.JPanel MainPanel;
    private javax.swing.JButton ModuleButton;
    private javax.swing.JButton ProfileButton;
    private javax.swing.JPanel SidePanel;
    private javax.swing.JLabel TitleP1;
    private javax.swing.JLabel UserIcon;
    private javax.swing.JLabel UserName;
    private javax.swing.JLabel UserRole;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel logo;
    private javax.swing.JPanel panelClassesContainer;
    // End of variables declaration//GEN-END:variables
}
