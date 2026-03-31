/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */

package AcademicLeader;
import Classes.ModuleResult;
import Classes.User;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author Lenovo
 */
public class AcadLead_05Reports extends javax.swing.JPanel {
    private User currentUser;
    private List<ModuleResult> allResults;
    public AcadLead_05Reports(User currentUser) {
        initComponents();
        
        this.currentUser = currentUser;
    

        lblUsername.setText(currentUser.getUsername());
        lblRole.setText(currentUser.getRole());
        
        panelBarChart.setPreferredSize(new java.awt.Dimension(600, 300));
        panelBarChart.revalidate();
        

        panelBarChart.setMinimumSize(new java.awt.Dimension(400, 300));
        panelBarChart.setBackground(Color.WHITE);

        allResults = ModuleResult.loadModuleResult("moduleResult.txt");

        loadModulesIntoComboBox();

        cmbModules.setSelectedItem("All Modules");

        btnShowModuleReportActionPerformed(null);
        
            // Run AFTER layout is complete
        javax.swing.SwingUtilities.invokeLater(() -> {
        btnShowModuleReportActionPerformed(null);
    });
    }
    
    
    private void loadModulesIntoComboBox() {
    cmbModules.removeAllItems();
    cmbModules.addItem("All Modules");

    for (ModuleResult mr : allResults) {
        String moduleId = mr.getModuleID();
        if (((javax.swing.DefaultComboBoxModel<String>) cmbModules.getModel())
                .getIndexOf(moduleId) == -1) {
            cmbModules.addItem(moduleId);
        }
    }
}
    
    private void populateReportsLabels(List<ModuleResult> results) {
        
        double sumLecture = 0, sumTutorial = 0, sumFinal = 0;
        int countLecture = 0, countTutorial = 0, countFinal = 0;
        int pass = 0, fail = 0;

        for (ModuleResult mr : results) {
            // handle NULL values
            Double lec = mr.getLectureMark();
            Double tut = mr.getTutorialMark();
            Double fin = mr.getFinalMark();

            if (lec != null && !Double.isNaN(lec)) {
                sumLecture += lec;
                countLecture++;
            }
            if (tut != null && !Double.isNaN(tut)) {
                sumTutorial += tut;
                countTutorial++;
            }
            if (fin != null && !Double.isNaN(fin)) {
                sumFinal += fin;
                countFinal++;
            }

            if (fin != null && fin >= 50) pass++;
            else if (fin != null) fail++;
        }

        lblAvgLecMark.setText(countLecture > 0 ? String.format("%.2f", sumLecture / countLecture) : "0");
        lblAvgTutoMark.setText(countTutorial > 0 ? String.format("%.2f", sumTutorial / countTutorial) : "0");
        lblAvgFinalMark.setText(countFinal > 0 ? String.format("%.2f", sumFinal / countFinal) : "0");
        lblStudPass.setText(String.valueOf(pass));
        lblStudFail.setText(String.valueOf(fail));
    }
    
    
    
private void showBarChartSwing(List<ModuleResult> results) {

    panelBarChart.removeAll();
    panelBarChart.setLayout(new BorderLayout());

    JPanel chart = new JPanel() {

@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (results.isEmpty()) return;

    int width = getWidth();
    int height = getHeight();
    int maxMark = 100;

    // 1️⃣ Create map FIRST
    Map<String, List<Double>> moduleFinalMarks = new HashMap<>();

    for (ModuleResult mr : results) {
        Double fin = mr.getFinalMark();
        if (fin != null) {
            moduleFinalMarks.putIfAbsent(mr.getModuleID(), new ArrayList<>());
            moduleFinalMarks.get(mr.getModuleID()).add(fin);
        }
    }

    // 2️⃣ Now safe to use size()
    int totalBars = moduleFinalMarks.size();

    int availableWidth = width - 80;
    int barWidth = totalBars > 0 ? availableWidth / (totalBars * 2) : 40;
    int spacing = barWidth;
    int x = 40;

    int i = 0;

    for (String module : new java.util.TreeSet<>(moduleFinalMarks.keySet())) {

        List<Double> marks = moduleFinalMarks.get(module);
        double avg = marks.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        int barHeight = (int) ((avg / maxMark) * (height - 60));

        g.setColor(new Color(200, 160, 190));
        g.fillRect(x + i * (barWidth + spacing),
                height - barHeight - 40,
                barWidth,
                barHeight);

        g.setColor(Color.BLACK);
        g.drawString(module,
                x + i * (barWidth + spacing),
                height - 15);

        g.drawString(String.format("%.1f", avg),
                x + i * (barWidth + spacing),
                height - barHeight - 45);

        i++;
    }

    // Draw axes
    g.drawLine(30, 10, 30, height - 30);
    g.drawLine(30, height - 30, width - 10, height - 30);
        }
    };

    panelBarChart.add(chart, BorderLayout.CENTER);
    panelBarChart.revalidate();
    panelBarChart.repaint();
}
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton7 = new javax.swing.JButton();
        btnShowModuleReport = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        panelBarChart = new javax.swing.JPanel();
        cmbModules = new javax.swing.JComboBox<>();
        jLayeredPane1 = new javax.swing.JLayeredPane();
        jLabel4 = new javax.swing.JLabel();
        lblModId = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        lblStudFail = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblAvgFinalMark = new javax.swing.JLabel();
        lblModReId = new javax.swing.JLabel();
        lblStudPass = new javax.swing.JLabel();
        lblAvgLecMark = new javax.swing.JLabel();
        lblAvgTutoMark = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        lblRole = new javax.swing.JLabel();

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

        jButton1.setText("View Report");

        jButton2.setText("Generate Report");

        jButton3.setText("Download Report");

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

        jButton7.setBackground(new java.awt.Color(236, 217, 226));
        jButton7.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jButton7.setText("Create");
        jButton7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jButton7.addActionListener(this::jButton7ActionPerformed);

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(775, 643));

        btnShowModuleReport.setBackground(new java.awt.Color(236, 217, 226));
        btnShowModuleReport.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        btnShowModuleReport.setText("Refresh");
        btnShowModuleReport.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnShowModuleReport.addActionListener(this::btnShowModuleReportActionPerformed);

        jLabel8.setFont(new java.awt.Font("Helvetica World", 1, 22)); // NOI18N
        jLabel8.setText("Reports");

        panelBarChart.setMaximumSize(new java.awt.Dimension(476, 237));
        panelBarChart.setMinimumSize(new java.awt.Dimension(476, 237));

        javax.swing.GroupLayout panelBarChartLayout = new javax.swing.GroupLayout(panelBarChart);
        panelBarChart.setLayout(panelBarChartLayout);
        panelBarChartLayout.setHorizontalGroup(
            panelBarChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelBarChartLayout.setVerticalGroup(
            panelBarChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        cmbModules.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jLabel4.setText("Average Lecture Mark");

        lblModId.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        lblModId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblModId.setToolTipText("");
        lblModId.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(194, 194, 194), 1, true));

        jLabel3.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jLabel3.setText("Module ID");

        lblStudFail.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        lblStudFail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStudFail.setToolTipText("");
        lblStudFail.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(194, 194, 194), 1, true));

        jLabel9.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jLabel9.setText("No. of Students Failed");

        lblAvgFinalMark.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        lblAvgFinalMark.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAvgFinalMark.setToolTipText("");
        lblAvgFinalMark.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(194, 194, 194), 1, true));

        lblModReId.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        lblModReId.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblModReId.setToolTipText("");
        lblModReId.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(194, 194, 194), 1, true));

        lblStudPass.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        lblStudPass.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblStudPass.setToolTipText("");
        lblStudPass.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(194, 194, 194), 1, true));

        lblAvgLecMark.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        lblAvgLecMark.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAvgLecMark.setToolTipText("");
        lblAvgLecMark.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(194, 194, 194), 1, true));

        lblAvgTutoMark.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        lblAvgTutoMark.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAvgTutoMark.setToolTipText("");
        lblAvgTutoMark.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(194, 194, 194), 1, true));

        jLabel7.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jLabel7.setText("No. of Students Passed");

        jLabel6.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jLabel6.setText("Average Final Mark");

        jLabel5.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jLabel5.setText("Average Tutorial Mark");

        jLabel2.setFont(new java.awt.Font("Helvetica World", 0, 12)); // NOI18N
        jLabel2.setText("Module Result ID");

        jLayeredPane1.setLayer(jLabel4, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblModId, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel3, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblStudFail, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel9, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblAvgFinalMark, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblModReId, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblStudPass, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblAvgLecMark, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(lblAvgTutoMark, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel7, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel6, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel5, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jLabel2, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblAvgTutoMark, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStudPass, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModReId, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAvgLecMark, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAvgFinalMark, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStudFail, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblModId, javax.swing.GroupLayout.PREFERRED_SIZE, 324, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(34, Short.MAX_VALUE))
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jLayeredPane1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(lblModReId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblModId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAvgLecMark, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAvgTutoMark, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblAvgFinalMark, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblStudPass, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblStudFail, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jLayeredPane1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel3)
                        .addGap(28, 28, 28)
                        .addGroup(jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(47, 47, 47))
                            .addComponent(jLabel5))
                        .addGap(28, 28, 28)
                        .addComponent(jLabel6)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel7)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel9)))
                .addContainerGap())
        );

        lblUsername.setText("username");

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/IconUser.png"))); // NOI18N
        jButton5.setBorder(null);

        lblRole.setFont(new java.awt.Font("Segoe UI", 0, 10)); // NOI18N
        lblRole.setText("role");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                .addComponent(panelBarChart, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jButton5)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsername)
                    .addComponent(lblRole))
                .addGap(17, 17, 17))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLayeredPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(86, 86, 86)
                        .addComponent(cmbModules, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(59, 59, 59)
                        .addComponent(btnShowModuleReport, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(76, 76, 76))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(panelBarChart, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButton5)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblUsername)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblRole))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnShowModuleReport, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbModules, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(58, 58, 58))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

    }//GEN-LAST:event_jButton7ActionPerformed

    private void btnShowModuleReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnShowModuleReportActionPerformed
        // TODO add your handling code here:
         String selectedModule = (String) cmbModules.getSelectedItem();

    if (selectedModule.equals("All Modules")) {
        populateReportsLabels(allResults);
        showBarChartSwing(allResults);
        lblModId.setText("All Modules");
        lblModReId.setText("-");
        return;
    }

    List<ModuleResult> filtered = new ArrayList<>();

    for (ModuleResult mr : allResults) {
        if (mr.getModuleID().equals(selectedModule)) {
            filtered.add(mr);
        }
    }

    populateReportsLabels(filtered);
    showBarChartSwing(filtered);

    lblModId.setText(selectedModule);

    if (!filtered.isEmpty()) {
        lblModReId.setText(filtered.get(0).getModuleResultID());
    } else {
        lblModReId.setText("-");
    }
    }//GEN-LAST:event_btnShowModuleReportActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnShowModuleReport;
    private javax.swing.JComboBox<String> cmbModules;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblAvgFinalMark;
    private javax.swing.JLabel lblAvgLecMark;
    private javax.swing.JLabel lblAvgTutoMark;
    private javax.swing.JLabel lblModId;
    private javax.swing.JLabel lblModReId;
    private javax.swing.JLabel lblRole;
    private javax.swing.JLabel lblStudFail;
    private javax.swing.JLabel lblStudPass;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel panelBarChart;
    // End of variables declaration//GEN-END:variables
}
