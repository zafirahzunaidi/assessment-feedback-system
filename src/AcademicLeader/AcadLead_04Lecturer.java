/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package AcademicLeader;

/**
 *
 * @author Lenovo
 */

import Classes.Module;
import Classes.Lecturer;
import Classes.ModuleLecturer;
import Classes.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.util.*;
import java.util.List;

public class AcadLead_04Lecturer extends javax.swing.JPanel {
    private User currentUser;
    private final String moduleFile = "module.txt";
    private final String lecturerFile = "lecturer.txt";
    private final String moduleLecturerFile = "moduleLecturer.txt";

    private List<Module> allModules;
    private List<Lecturer> allLecturers;
    private List<ModuleLecturer> allAssignments;

    /**
     * Creates new form AcadLeadLecturer
     */
    public AcadLead_04Lecturer(User currentUser) {
        initComponents();
        this.currentUser = currentUser;
        lblUsername.setText(currentUser.getUsername());
        lblRole.setText(currentUser.getRole());
        loadAllData();
        addTableListeners();
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                refreshData();
    }
});
    }
    
    private void loadAllData() {
        // Load data from files
        allModules = Module.loadModules(moduleFile);
        allLecturers = Lecturer.loadLecturers(lecturerFile);
        allAssignments = ModuleLecturer.loadModuleLecturers(moduleLecturerFile);

        // Populate tables
        populateModuleTable();
        populateLecturerTable();
        populateModuleLecturerTable();
    }

private void populateModuleTable() {
    allModules = Module.loadModules(moduleFile); 

    DefaultTableModel model = new DefaultTableModel(
            new Object[]{"Module ID", "Module Name", "Credit Hour"}, 0
    );

    Set<String> assignedModuleIDs = new HashSet<>();
    for (ModuleLecturer ml : allAssignments) {
        assignedModuleIDs.add(ml.getModuleID());
    }

    for (Module m : allModules) {
        if (!assignedModuleIDs.contains(m.getModuleID())) {
            model.addRow(new Object[]{m.getModuleID(), m.getName(), m.getCreditHour()});
        }
    }

    jTable1.setModel(model);
}

    private void populateLecturerTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Lecturer ID", "Username", "Name", "Title", "Email"}, 0
        );

        for (Lecturer l : allLecturers) {
            model.addRow(new Object[]{
                    l.getLecturerID(), l.getUsername(), l.getName(), l.getTitle(), l.getEmail()
            });
        }

        jTable3.setModel(model);
    }

    private void populateModuleLecturerTable() {
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Assignment ID", "Module ID", "Module Name", "Lecturer ID", "Lecturer Name"}, 0
        );

        for (ModuleLecturer ml : allAssignments) {
            Module m = findModuleByID(ml.getModuleID());
            Lecturer l = findLecturerByID(ml.getLecturerID());

            if (m != null && l != null) {
                model.addRow(new Object[]{
                        ml.getModlectID(),
                        m.getModuleID(),
                        m.getName(),
                        l.getLecturerID(),
                        l.getName()
                });
            }
        }

        jTable2.setModel(model);
    }

    private Module findModuleByID(String moduleID) {
        for (Module m : allModules) {
            if (m.getModuleID().equalsIgnoreCase(moduleID)) return m;
        }
        return null;
    }

    private Lecturer findLecturerByID(String lecturerID) {
        for (Lecturer l : allLecturers) {
            if (l.getLecturerID().equalsIgnoreCase(lecturerID)) return l;
        }
        return null;
    }
    
    private void addTableListeners() {
        // Optional: select row on click if needed for UI
        jTable1.addMouseListener(new MouseAdapter() {});
        jTable2.addMouseListener(new MouseAdapter() {});
        jTable3.addMouseListener(new MouseAdapter() {});
    }
    
    private String generateNewModlectID() {
        int max = 0;
        for (ModuleLecturer ml : allAssignments) {
            String idNumStr = ml.getModlectID().replaceAll("[^0-9]", "");
            if (!idNumStr.isEmpty()) {
                int num = Integer.parseInt(idNumStr);
                if (num > max) max = num;
            }
        }
        return "ML" + String.format("%04d", max + 1);
    }
    
    private void refreshData() {
    allModules = Module.loadModules(moduleFile);
    allLecturers = Lecturer.loadLecturers(lecturerFile);
    allAssignments = ModuleLecturer.loadModuleLecturers(moduleLecturerFile);

    populateModuleTable();
    populateLecturerTable();
    populateModuleLecturerTable();
}
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        lblUsername = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        lblRole = new javax.swing.JLabel();
        javax.swing.JLabel jLabel6 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel9 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel10 = new javax.swing.JLabel();

        jLabel7.setFont(new java.awt.Font("Helvetica World", 1, 36)); // NOI18N
        jLabel7.setText("Modules");

        jButton3.setBackground(new java.awt.Color(236, 217, 226));
        jButton3.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jButton3.setText("Create");
        jButton3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton3.addActionListener(this::jButton3ActionPerformed);

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(775, 643));

        jButton1.setBackground(new java.awt.Color(236, 217, 226));
        jButton1.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jButton1.setText("Assign");
        jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton1.setPreferredSize(new java.awt.Dimension(72, 26));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jButton2.setBackground(new java.awt.Color(236, 217, 226));
        jButton2.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jButton2.setText("Remove");
        jButton2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jButton2.setPreferredSize(new java.awt.Dimension(76, 26));
        jButton2.addActionListener(this::jButton2ActionPerformed);

        jTable2.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jTable1.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel8.setFont(new java.awt.Font("Helvetica World", 1, 36)); // NOI18N
        jLabel8.setText("Lecturer");

        jTable3.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        lblUsername.setText("username");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/IconUser.png"))); // NOI18N
        jButton5.setBorder(null);

        lblRole.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblRole.setText("role");

        jLabel6.setFont(new java.awt.Font("Helvetica World", 0, 14)); // NOI18N
        jLabel6.setText("Select the Lecturer to assign the module to:");

        jLabel9.setFont(new java.awt.Font("Helvetica World", 0, 14)); // NOI18N
        jLabel9.setText("Select the Module to be assigned to Lecturer:");

        jLabel10.setFont(new java.awt.Font("Helvetica World", 0, 14)); // NOI18N
        jLabel10.setText("Lecturers currently assigned to Modules:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(271, 271, 271)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel9))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addContainerGap(52, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton5)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsername)
                    .addComponent(lblRole))
                .addGap(33, 33, 33))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton5)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblUsername)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblRole))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(399, 399, 399)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(80, 80, 80))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        int selectedModuleRow = jTable1.getSelectedRow();
        int selectedLecturerRow = jTable3.getSelectedRow();

        if (selectedModuleRow < 0 || selectedLecturerRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select both a module and a lecturer.",
                    "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String moduleID = jTable1.getValueAt(selectedModuleRow, 0).toString();
        String lecturerID = jTable3.getValueAt(selectedLecturerRow, 0).toString();

        // Generate new modlectID
        String newID = generateNewModlectID();

        ModuleLecturer newAssignment = new ModuleLecturer(newID, lecturerID, moduleID);
        allAssignments.add(newAssignment);
        ModuleLecturer.addModuleLecturer(moduleLecturerFile, newAssignment);

        refreshData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select an assignment to remove.",
                    "Selection Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String assignmentID = jTable2.getValueAt(selectedRow, 0).toString();

        allAssignments.removeIf(ml -> ml.getModlectID().equalsIgnoreCase(assignmentID));
        ModuleLecturer.saveModuleLecturers(moduleLecturerFile, allAssignments);

        refreshData();
    }
    // Reset Button
    private void jButton5ActionPerformed() {
        jTable1.clearSelection();
        jTable2.clearSelection();
        jTable3.clearSelection();
    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblUsername;
    // End of variables declaration//GEN-END:variables
}
