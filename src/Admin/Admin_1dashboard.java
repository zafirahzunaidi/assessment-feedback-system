package Admin;

//import adminClasses.AdminTask;
//import adminClasses.User;
import static Admin.Admin_5LecturerMain.tableLecturer;
import Classes.User;
import Classes.AdminTask;
import static Admin.Admin_8AssignClass.cmbModule;
import Classes.Session;
import Login.AFS_LoginPage;
import java.awt.Color;
import java.awt.Dimension;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.JTableHeader;

public class Admin_1dashboard extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Admin_1dashboard.class.getName());
    
    private User admin;

    public Admin_1dashboard() {
        initComponents();
        lblUsername.setText(Session.getUsername());
        lblRole.setText(Session.getRole());
        
        JTableHeader header = tblModule.getTableHeader();
        header.setPreferredSize(new Dimension(100, 30)); 
        
        JTableHeader header2 = tblgrading.getTableHeader();
        header2.setPreferredSize(new Dimension(100, 30)); 
    }
   

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bg = new javax.swing.JLayeredPane();
        side = new javax.swing.JLayeredPane();
        logo = new javax.swing.JLabel();
        btnDashboard = new javax.swing.JButton();
        btnStudent = new javax.swing.JButton();
        btnLect = new javax.swing.JButton();
        btnClass = new javax.swing.JButton();
        btnSettings = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnAcadlead = new javax.swing.JButton();
        userspace = new javax.swing.JLayeredPane();
        lblPfp = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblRole = new javax.swing.JLabel();
        searchspace = new javax.swing.JLayeredPane();
        noticeboardspace = new javax.swing.JLayeredPane();
        jLayeredPane4 = new javax.swing.JLayeredPane();
        grading = new javax.swing.JScrollPane();
        tblgrading = new javax.swing.JTable();
        jLayeredPane7 = new javax.swing.JLayeredPane();
        lblRegisterClass1 = new javax.swing.JLabel();
        lblDasboardTitle = new javax.swing.JLabel();
        lblQuickaction = new javax.swing.JLabel();
        existingLect = new javax.swing.JLayeredPane();
        lblEditLecturer = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        registerclass = new javax.swing.JLayeredPane();
        lblRegisterClass = new javax.swing.JLabel();
        newclass = new javax.swing.JScrollPane();
        tblModule = new javax.swing.JTable();
        studentreg = new javax.swing.JLayeredPane();
        lblStudentReg = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("dashboard"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(1031, 643));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        bg.setBackground(new java.awt.Color(255, 255, 255));
        bg.setOpaque(true);
        bg.setPreferredSize(new java.awt.Dimension(1031, 643));

        side.setBackground(new java.awt.Color(24, 29, 49));
        side.setOpaque(true);

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/assessment-feedback-system-high-resolution-logo-transparent-3 (2).png"))); // NOI18N

        btnDashboard.setBackground(new java.awt.Color(24, 29, 49));
        btnDashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/please.png"))); // NOI18N
        btnDashboard.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnDashboard.setBorderPainted(false);
        btnDashboard.setFocusPainted(false);
        btnDashboard.setOpaque(true);
        btnDashboard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDashboardActionPerformed(evt);
            }
        });

        btnStudent.setBackground(new java.awt.Color(24, 29, 49));
        btnStudent.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/please copy 2.png"))); // NOI18N
        btnStudent.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnStudent.setOpaque(true);
        btnStudent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStudentActionPerformed(evt);
            }
        });

        btnLect.setBackground(new java.awt.Color(24, 29, 49));
        btnLect.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/please copy (2).png"))); // NOI18N
        btnLect.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLect.setOpaque(true);
        btnLect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLectActionPerformed(evt);
            }
        });

        btnClass.setBackground(new java.awt.Color(24, 29, 49));
        btnClass.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/please copy 4.png"))); // NOI18N
        btnClass.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnClass.setOpaque(true);
        btnClass.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClassActionPerformed(evt);
            }
        });

        btnSettings.setBackground(new java.awt.Color(24, 29, 49));
        btnSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/please copy 5.png"))); // NOI18N
        btnSettings.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSettings.setOpaque(true);
        btnSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSettingsActionPerformed(evt);
            }
        });

        btnLogout.setBackground(new java.awt.Color(24, 29, 49));
        btnLogout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/please copy 6.png"))); // NOI18N
        btnLogout.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLogout.setOpaque(true);
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        btnAcadlead.setBackground(new java.awt.Color(24, 29, 49));
        btnAcadlead.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/please copy 3.png"))); // NOI18N
        btnAcadlead.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnAcadlead.setOpaque(true);
        btnAcadlead.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcadleadActionPerformed(evt);
            }
        });

        side.setLayer(logo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        side.setLayer(btnDashboard, javax.swing.JLayeredPane.DEFAULT_LAYER);
        side.setLayer(btnStudent, javax.swing.JLayeredPane.DEFAULT_LAYER);
        side.setLayer(btnLect, javax.swing.JLayeredPane.DEFAULT_LAYER);
        side.setLayer(btnClass, javax.swing.JLayeredPane.DEFAULT_LAYER);
        side.setLayer(btnSettings, javax.swing.JLayeredPane.DEFAULT_LAYER);
        side.setLayer(btnLogout, javax.swing.JLayeredPane.DEFAULT_LAYER);
        side.setLayer(btnAcadlead, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout sideLayout = new javax.swing.GroupLayout(side);
        side.setLayout(sideLayout);
        sideLayout.setHorizontalGroup(
            sideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideLayout.createSequentialGroup()
                .addGroup(sideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sideLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(logo))
                    .addGroup(sideLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(sideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDashboard)
                            .addComponent(btnStudent)
                            .addComponent(btnLect)
                            .addComponent(btnLogout)
                            .addComponent(btnSettings)
                            .addComponent(btnClass)
                            .addComponent(btnAcadlead))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        sideLayout.setVerticalGroup(
            sideLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sideLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(logo)
                .addGap(39, 39, 39)
                .addComponent(btnDashboard)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStudent)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAcadlead)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClass)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSettings)
                .addGap(55, 55, 55)
                .addComponent(btnLogout)
                .addContainerGap(127, Short.MAX_VALUE))
        );

        lblPfp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/pfp.png"))); // NOI18N

        lblUsername.setFont(new java.awt.Font("Helvetica Neue", 1, 16)); // NOI18N
        lblUsername.setText("username");

        lblRole.setText("role");

        userspace.setLayer(lblPfp, javax.swing.JLayeredPane.DEFAULT_LAYER);
        userspace.setLayer(lblUsername, javax.swing.JLayeredPane.DEFAULT_LAYER);
        userspace.setLayer(lblRole, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout userspaceLayout = new javax.swing.GroupLayout(userspace);
        userspace.setLayout(userspaceLayout);
        userspaceLayout.setHorizontalGroup(
            userspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userspaceLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblPfp)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(userspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsername)
                    .addComponent(lblRole))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        userspaceLayout.setVerticalGroup(
            userspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userspaceLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRole, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40))
            .addGroup(userspaceLayout.createSequentialGroup()
                .addComponent(lblPfp)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        searchspace.setBackground(new java.awt.Color(204, 255, 204));

        javax.swing.GroupLayout searchspaceLayout = new javax.swing.GroupLayout(searchspace);
        searchspace.setLayout(searchspaceLayout);
        searchspaceLayout.setHorizontalGroup(
            searchspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 299, Short.MAX_VALUE)
        );
        searchspaceLayout.setVerticalGroup(
            searchspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 46, Short.MAX_VALUE)
        );

        noticeboardspace.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblgrading.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Grade", "Type", "Weightage"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblgrading.setGridColor(new java.awt.Color(102, 102, 102));
        tblgrading.setRowHeight(30);
        tblgrading.setRowSelectionAllowed(false);
        tblgrading.getTableHeader().setReorderingAllowed(false);
        grading.setViewportView(tblgrading);
        if (tblgrading.getColumnModel().getColumnCount() > 0) {
            tblgrading.getColumnModel().getColumn(0).setResizable(false);
            tblgrading.getColumnModel().getColumn(1).setResizable(false);
            tblgrading.getColumnModel().getColumn(1).setPreferredWidth(300);
            tblgrading.getColumnModel().getColumn(2).setResizable(false);
        }

        jLayeredPane4.setLayer(grading, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane4Layout = new javax.swing.GroupLayout(jLayeredPane4);
        jLayeredPane4.setLayout(jLayeredPane4Layout);
        jLayeredPane4Layout.setHorizontalGroup(
            jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane4Layout.createSequentialGroup()
                .addComponent(grading, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jLayeredPane4Layout.setVerticalGroup(
            jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane4Layout.createSequentialGroup()
                .addComponent(grading, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jLayeredPane7Layout = new javax.swing.GroupLayout(jLayeredPane7);
        jLayeredPane7.setLayout(jLayeredPane7Layout);
        jLayeredPane7Layout.setHorizontalGroup(
            jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jLayeredPane7Layout.setVerticalGroup(
            jLayeredPane7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 45, Short.MAX_VALUE)
        );

        lblRegisterClass1.setFont(new java.awt.Font("Helvetica Neue", 1, 20)); // NOI18N
        lblRegisterClass1.setText("Grading Sytem");

        noticeboardspace.setLayer(jLayeredPane4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        noticeboardspace.setLayer(jLayeredPane7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        noticeboardspace.setLayer(lblRegisterClass1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout noticeboardspaceLayout = new javax.swing.GroupLayout(noticeboardspace);
        noticeboardspace.setLayout(noticeboardspaceLayout);
        noticeboardspaceLayout.setHorizontalGroup(
            noticeboardspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noticeboardspaceLayout.createSequentialGroup()
                .addGroup(noticeboardspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(noticeboardspaceLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(noticeboardspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblRegisterClass1)
                            .addComponent(jLayeredPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(noticeboardspaceLayout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(jLayeredPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        noticeboardspaceLayout.setVerticalGroup(
            noticeboardspaceLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(noticeboardspaceLayout.createSequentialGroup()
                .addComponent(lblRegisterClass1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLayeredPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(64, 64, 64)
                .addComponent(jLayeredPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        lblDasboardTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 36)); // NOI18N
        lblDasboardTitle.setText("Dashboard");

        lblQuickaction.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblQuickaction.setText("Quick Action");

        existingLect.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        existingLect.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                existingLectMouseClicked(evt);
            }
        });

        lblEditLecturer.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblEditLecturer.setText("Edit Lecturer Information");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/arrowy.png"))); // NOI18N

        existingLect.setLayer(lblEditLecturer, javax.swing.JLayeredPane.DEFAULT_LAYER);
        existingLect.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout existingLectLayout = new javax.swing.GroupLayout(existingLect);
        existingLect.setLayout(existingLectLayout);
        existingLectLayout.setHorizontalGroup(
            existingLectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, existingLectLayout.createSequentialGroup()
                .addContainerGap(302, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
            .addGroup(existingLectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(existingLectLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(lblEditLecturer)
                    .addContainerGap(58, Short.MAX_VALUE)))
        );
        existingLectLayout.setVerticalGroup(
            existingLectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, existingLectLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(existingLectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(existingLectLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(lblEditLecturer, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(9, Short.MAX_VALUE)))
        );

        registerclass.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        registerclass.setPreferredSize(new java.awt.Dimension(200, 200));

        lblRegisterClass.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblRegisterClass.setText("Register New Class");

        newclass.setName(""); // NOI18N

        tblModule.setBorder(javax.swing.BorderFactory.createCompoundBorder(null, javax.swing.BorderFactory.createEmptyBorder(1, 10, 1, 1)));
        tblModule.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Module ID", "Module Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblModule.setGridColor(new java.awt.Color(102, 102, 102));
        tblModule.setRowHeight(30);
        tblModule.getTableHeader().setReorderingAllowed(false);
        tblModule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblModuleMouseClicked(evt);
            }
        });
        newclass.setViewportView(tblModule);
        if (tblModule.getColumnModel().getColumnCount() > 0) {
            tblModule.getColumnModel().getColumn(0).setResizable(false);
            tblModule.getColumnModel().getColumn(1).setResizable(false);
            tblModule.getColumnModel().getColumn(1).setPreferredWidth(300);
        }

        registerclass.setLayer(lblRegisterClass, javax.swing.JLayeredPane.DEFAULT_LAYER);
        registerclass.setLayer(newclass, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout registerclassLayout = new javax.swing.GroupLayout(registerclass);
        registerclass.setLayout(registerclassLayout);
        registerclassLayout.setHorizontalGroup(
            registerclassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registerclassLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(registerclassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(registerclassLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(newclass, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblRegisterClass))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        registerclassLayout.setVerticalGroup(
            registerclassLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(registerclassLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRegisterClass, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newclass, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        studentreg.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        studentreg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                studentregMouseClicked(evt);
            }
        });

        lblStudentReg.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblStudentReg.setText("New Student Registration");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/arrowy.png"))); // NOI18N

        studentreg.setLayer(lblStudentReg, javax.swing.JLayeredPane.DEFAULT_LAYER);
        studentreg.setLayer(jLabel1, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout studentregLayout = new javax.swing.GroupLayout(studentreg);
        studentreg.setLayout(studentregLayout);
        studentregLayout.setHorizontalGroup(
            studentregLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studentregLayout.createSequentialGroup()
                .addContainerGap(314, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(studentregLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(studentregLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(lblStudentReg)
                    .addContainerGap(63, Short.MAX_VALUE)))
        );
        studentregLayout.setVerticalGroup(
            studentregLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, studentregLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(studentregLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(studentregLayout.createSequentialGroup()
                    .addGap(9, 9, 9)
                    .addComponent(lblStudentReg, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(9, Short.MAX_VALUE)))
        );

        bg.setLayer(side, javax.swing.JLayeredPane.DEFAULT_LAYER);
        bg.setLayer(userspace, javax.swing.JLayeredPane.DEFAULT_LAYER);
        bg.setLayer(searchspace, javax.swing.JLayeredPane.DEFAULT_LAYER);
        bg.setLayer(noticeboardspace, javax.swing.JLayeredPane.DEFAULT_LAYER);
        bg.setLayer(lblDasboardTitle, javax.swing.JLayeredPane.DEFAULT_LAYER);
        bg.setLayer(lblQuickaction, javax.swing.JLayeredPane.DEFAULT_LAYER);
        bg.setLayer(existingLect, javax.swing.JLayeredPane.DEFAULT_LAYER);
        bg.setLayer(registerclass, javax.swing.JLayeredPane.DEFAULT_LAYER);
        bg.setLayer(studentreg, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout bgLayout = new javax.swing.GroupLayout(bg);
        bg.setLayout(bgLayout);
        bgLayout.setHorizontalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addComponent(side, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblQuickaction)
                    .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, bgLayout.createSequentialGroup()
                            .addComponent(lblDasboardTitle)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(searchspace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(userspace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(noticeboardspace, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(registerclass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 747, Short.MAX_VALUE)
                        .addGroup(bgLayout.createSequentialGroup()
                            .addComponent(studentreg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(existingLect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bgLayout.setVerticalGroup(
            bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bgLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblDasboardTitle)
                    .addComponent(searchspace, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userspace, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(noticeboardspace, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblQuickaction)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(existingLect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(studentreg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(registerclass, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(side)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(bg, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnStudentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudentActionPerformed
        Admin_2StudentMain Admin_2StudentMainpage = new Admin_2StudentMain();
        Admin_2StudentMainpage.setVisible(true);
        this.dispose();
        //AdminTask.initUsernameRole(lblUsername.getText(), lblRole.getText(), Admin_2StudentMain.lblUsername, Admin_2StudentMain.lblRole);
    }//GEN-LAST:event_btnStudentActionPerformed

    private void studentregMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_studentregMouseClicked
        Admin_3StudentRegister Admin_3StudentRegisterpage = new Admin_3StudentRegister();
        Admin_3StudentRegisterpage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_studentregMouseClicked

    private void btnLectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLectActionPerformed
        Admin_5LecturerMain Admin_5LecturerMainpage = new Admin_5LecturerMain();
        Admin_5LecturerMainpage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnLectActionPerformed

    private void btnClassActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClassActionPerformed
        Admin_8AssignClass Admin_8AssignClasspage = new Admin_8AssignClass();
        Admin_8AssignClasspage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnClassActionPerformed

    private void btnAcadleadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcadleadActionPerformed
        Admin_10AcadLeadMain Admin_10AcadLeadMainpage = new Admin_10AcadLeadMain();
        Admin_10AcadLeadMainpage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAcadleadActionPerformed

    private void btnSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSettingsActionPerformed
        Admin_13Settings Admin_13Settingspage = new Admin_13Settings();
        Admin_13Settingspage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSettingsActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int result = JOptionPane.showConfirmDialog(null, 
        "Logging out?", 
        "Confirmation", 
        JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){
            new AFS_LoginPage().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void existingLectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_existingLectMouseClicked
        Admin_5LecturerMain Admin_5LecturerMainpage = new Admin_5LecturerMain();
        Admin_5LecturerMainpage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_existingLectMouseClicked

    private void tblModuleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblModuleMouseClicked
        // tengok nama module + ID
        // select the same row number (atau +1)
        int row = tblModule.rowAtPoint(evt.getPoint());
        
        // select it at the combo box page 8
        cmbModule.setSelectedIndex(row);
        
        //go to page 8
        // hide this page
        Admin_8AssignClass Admin_8AssignClasspage = new Admin_8AssignClass();
        Admin_8AssignClasspage.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_tblModuleMouseClicked

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        tblModule.getTableHeader().setBackground(Color.decode("#d9e1ec"));
        tblgrading.getTableHeader().setBackground(Color.decode("#d9e1ec"));
        
        //AdminTask task = new AdminTask();
        AdminTask.initUsernameRole(Session.getUsername(), Session.getRole(), lblUsername, lblRole);
        List<String> data = AdminTask.readtxt("module.txt");
        AdminTask.addRowtoTable(tblModule, data, 2);
        List<String> data2 = AdminTask.readtxt("gradingSystem.txt");
        AdminTask.addRowtoTable(tblgrading, data2, 5);
        
        tblModule.getColumnModel().setColumnMargin(10);
        tblgrading.getColumnModel().setColumnMargin(10);
    }//GEN-LAST:event_formComponentShown

    private void btnDashboardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDashboardActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDashboardActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane bg;
    private javax.swing.JButton btnAcadlead;
    private javax.swing.JButton btnClass;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnLect;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnSettings;
    private javax.swing.JButton btnStudent;
    private javax.swing.JLayeredPane existingLect;
    private javax.swing.JScrollPane grading;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLayeredPane jLayeredPane4;
    private javax.swing.JLayeredPane jLayeredPane7;
    private javax.swing.JLabel lblDasboardTitle;
    private javax.swing.JLabel lblEditLecturer;
    private javax.swing.JLabel lblPfp;
    private javax.swing.JLabel lblQuickaction;
    private javax.swing.JLabel lblRegisterClass;
    private javax.swing.JLabel lblRegisterClass1;
    public static javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblStudentReg;
    public static javax.swing.JLabel lblUsername;
    private javax.swing.JLabel logo;
    private javax.swing.JScrollPane newclass;
    private javax.swing.JLayeredPane noticeboardspace;
    private javax.swing.JLayeredPane registerclass;
    private javax.swing.JLayeredPane searchspace;
    private javax.swing.JLayeredPane side;
    private javax.swing.JLayeredPane studentreg;
    private javax.swing.JTable tblModule;
    private javax.swing.JTable tblgrading;
    private javax.swing.JLayeredPane userspace;
    // End of variables declaration//GEN-END:variables
}
