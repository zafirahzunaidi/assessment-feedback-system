package Lecturer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class ChartPanel extends JPanel {
    Map<String, Integer> barChartData;
    Map<String, Object> statsData;
    List<String[]> performanceData;
    private String chartType;
    String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private JTable table;
    private JScrollPane tableScrollPane;
    private boolean showTable = false;
    private String instructionMessage;
    
    public ChartPanel(String chartType, String title) {
        this.chartType = chartType;
        this.title = title;
        
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400, 300));
        
        // Initialize table components
        table = new JTable();
        table.setFont(new Font("Arial", Font.PLAIN, 11));
        table.setRowHeight(20);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        table.setDefaultEditor(Object.class, null); // Make table non-editable
        
        tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.setVisible(false);
        
        setLayout(new BorderLayout());
        add(tableScrollPane, BorderLayout.CENTER);
    }
    
    public void showInstructionMessage(String message) {
        this.instructionMessage = message;
        this.chartType = "instruction";
        repaint();
    }
    
        // hide table
    public void hideTable() {
        this.showTable = false;
        tableScrollPane.setVisible(false);
        repaint();
    }
    

    public void setBarChartData(Map<String, Integer> data) {
        this.barChartData = data;
        this.chartType = "markDistribution";
        this.instructionMessage = null;
        hideTable();
        repaint();
    }
    
       // set performance data as table
    public void setPerformanceDataAsTable(List<String[]> data) {
        if (data == null || data.isEmpty()) {
            hideTable();
            return;
        }
        
        // Prepare table data
        String[] columnNames = {"Student", "Mark (%)", "Grade"};
        Object[][] tableData = new Object[data.size()][3];
        
        for (int i = 0; i < data.size(); i++) {
            String[] row = data.get(i);
            tableData[i][0] = row[0]; // Name
            tableData[i][1] = row[1]; // Mark
            tableData[i][2] = row.length > 2 ? row[2] : ""; // Grade
        }
        
        showTable(columnNames, tableData);
    }
    
    // show table
    public void showTable(String[] columnNames, Object[][] data) {
        this.showTable = true;
        this.chartType = "table";
        
        table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));
        tableScrollPane.setVisible(true);
        
        repaint();
    }

    public void setStatsData(Map<String, Object> data) {
        this.statsData = data;
        this.chartType = "assessmentStats";
        repaint();
    }
    
    public void setPerformanceData(List<String[]> data) {
        this.performanceData = data;
        this.chartType = "studentPerformance";
        repaint();
    }
    
    public void setLabels(String xAxisLabel, String yAxisLabel) {
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
    }
    
    private void drawInstructionMessage(Graphics2D g2, int width, int height) {
        g2.setFont(new Font("Arial", Font.ITALIC, 14));
        g2.setColor(new Color(120, 120, 120));
        
        // Split long messages into multiple lines
        String[] lines = instructionMessage.split("\\n");
        int lineHeight = 20;
        int startY = height / 2 - (lines.length * lineHeight) / 2;
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            FontMetrics fm = g2.getFontMetrics();
            int lineWidth = fm.stringWidth(line);
            g2.drawString(line, (width - lineWidth) / 2, startY + (i * lineHeight));
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // If showing instruction message
        if (chartType != null && chartType.equals("instruction") && instructionMessage != null) {
            drawInstructionMessage(g2, width, height);
            return;
        }
        
        // Draw title (only if not empty)
        if (title != null && !title.isEmpty()) {
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.setColor(Color.BLACK);
            FontMetrics titleMetrics = g2.getFontMetrics();
            int titleWidth = titleMetrics.stringWidth(title);
            g2.drawString(title, (width - titleWidth) / 2, 30);
        }
        
        if (chartType.equals("markDistribution") && barChartData != null) {
            drawMarkDistributionChart(g2, width, height);
        } else if (chartType.equals("assessmentStats") && statsData != null) {
            drawAssessmentStatsChart(g2, width, height);
        } else if (chartType.equals("studentPerformance") && performanceData != null) {
            drawStudentPerformanceChart(g2, width, height);
        } else if (chartType.equals("gradeDistribution") && barChartData != null) {
            drawGradeDistributionChart(g2, width, height);
        }
    }
    
    private void drawMarkDistributionChart(Graphics2D g2, int width, int height) {
        if (barChartData == null || barChartData.isEmpty()) {
            drawNoDataMessage(g2, width, height);
            return;
        }
        
        int chartX = 60;
        int chartY = 60;
        int chartWidth = width - 120;
        int chartHeight = height - 120;
        
        // Max value for scaling
        int maxValue = barChartData.values().stream().max(Integer::compare).orElse(1);
        
        // Draw bars
        int barCount = barChartData.size();
        int barWidth = Math.min(50, chartWidth / (barCount * 2));
        int spacing = 20;
        
        int x = chartX;
         Color[] colors = {
        new Color(127, 82, 139),  // #7f528b - main purple
        new Color(232, 217, 236), // #e8d9ec - light purple
        new Color(200, 180, 210), // custom purple-grey
        new Color(170, 150, 180), // darker purple-grey
        new Color(140, 120, 150)  // darkest purple-grey
        };
        
        int colorIndex = 0;
        for (Map.Entry<String, Integer> entry : barChartData.entrySet()) {
            String range = entry.getKey();
            int value = entry.getValue();
            
            // Calculate bar height
            int barHeight = (int) ((double) value / maxValue * chartHeight);
            int y = chartY + chartHeight - barHeight;
            
            // Draw bar
            g2.setColor(colors[colorIndex % colors.length]);
            g2.fillRect(x, y, barWidth, barHeight);
            
            // Draw bar outline
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, barWidth, barHeight);
            
            // Draw range label
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            FontMetrics fm = g2.getFontMetrics();
            int rangeWidth = fm.stringWidth(range);
            g2.drawString(range, x + (barWidth - rangeWidth) / 2, chartY + chartHeight + 20);
            
            // Draw value on top of bar
            String valueStr = String.valueOf(value);
            int valueWidth = fm.stringWidth(valueStr);
            if (barHeight > 20) {
                g2.setColor(Color.WHITE);
                g2.drawString(valueStr, x + (barWidth - valueWidth) / 2, y + 15);
            } else {
                g2.setColor(Color.BLACK);
                g2.drawString(valueStr, x + (barWidth - valueWidth) / 2, y - 5);
            }
            
            x += barWidth + spacing;
            colorIndex++;
        }
        
        // Draw axes
        drawAxes(g2, chartX, chartY, chartWidth, chartHeight, maxValue);
        
        // Draw labels
        if (xAxisLabel != null) {
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            int xLabelWidth = g2.getFontMetrics().stringWidth(xAxisLabel);
            g2.drawString(xAxisLabel, chartX + (chartWidth - xLabelWidth) / 2, height - 30);
        }
        
        if (yAxisLabel != null) {
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            int yLabelWidth = g2.getFontMetrics().stringWidth(yAxisLabel);
            // Rotate for Y-axis label
            Graphics2D g2d = (Graphics2D) g2.create();
            g2d.rotate(-Math.PI / 2);
            g2d.drawString(yAxisLabel, -height/2 - yLabelWidth/2, 25);
            g2d.dispose();
        }
    }
    
    private void drawAssessmentStatsChart(Graphics2D g2, int width, int height) {
        if (statsData == null || statsData.isEmpty()) {
            drawNoDataMessage(g2, width, height);
            return;
        }

        int centerX = width / 2;
        int startY = 80;
        int lineHeight = 25;

        // Draw title if it exists
        if (title != null && !title.isEmpty()) {
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.setColor(new Color(50, 50, 50));
            int titleWidth = g2.getFontMetrics().stringWidth(title);
            g2.drawString(title, centerX - titleWidth/2, startY);
            startY += lineHeight + 10;
        }

        // Draw statistics in a clean format
        g2.setFont(new Font("Arial", Font.PLAIN, 12));

        double average = (double) statsData.getOrDefault("average", 0.0);
        double median = (double) statsData.getOrDefault("median", 0.0);
        double highest = (double) statsData.getOrDefault("highest", 0.0);
        double lowest = (double) statsData.getOrDefault("lowest", 0.0);
        int count = (int) statsData.getOrDefault("count", 0);
        double stdDev = (double) statsData.getOrDefault("stdDev", 0.0);

        // Draw each statistic
        g2.drawString(String.format("Average Mark: %.1f", average), centerX - 80, startY);
        startY += lineHeight;

        if (median > 0) { // Only show median if it exists
            g2.drawString(String.format("Median Mark: %.1f", median), centerX - 80, startY);
            startY += lineHeight;
        }

        g2.drawString(String.format("Highest Mark: %.1f", highest), centerX - 80, startY);
        startY += lineHeight;

        g2.drawString(String.format("Lowest Mark: %.1f", lowest), centerX - 80, startY);
        startY += lineHeight;

        g2.drawString(String.format("Number of Students: %d", count), centerX - 80, startY);
        startY += lineHeight;

        if (stdDev > 0) { // Only show std dev if it exists
            g2.drawString(String.format("Standard Deviation: %.2f", stdDev), centerX - 80, startY);
        }
    }
    
    private void drawStudentPerformanceChart(Graphics2D g2, int width, int height) {
        if (performanceData == null || performanceData.isEmpty()) {
            drawNoDataMessage(g2, width, height);
            return;
        }

        // If too many students, show only top 10
        List<String[]> dataToShow = performanceData;
        boolean truncated = false;
        if (performanceData.size() > 10) {
            dataToShow = performanceData.subList(0, 10);
            truncated = true;
        }

        int chartX = 80;
        int chartY = 60;
        int chartWidth = width - 160;
        int chartHeight = height - 120;

        // Find max mark for scaling (but cap at 100)
        double maxMark = dataToShow.stream()
            .mapToDouble(d -> {
                try {
                    return Double.parseDouble(d[1]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            })
            .max()
            .orElse(100);
        maxMark = Math.max(maxMark, 100); // Ensure at least 100 for scaling

        // Draw bars for each student
        int barWidth = Math.max(20, Math.min(30, chartWidth / (dataToShow.size() * 2)));
        int spacing = 10;

        int x = chartX;

        for (String[] studentData : dataToShow) {
            if (studentData.length < 2) continue;

            String studentName = studentData[0];
            double mark;
            try {
                mark = Double.parseDouble(studentData[1]);
            } catch (NumberFormatException e) {
                continue;
            }

            // Calculate bar height
            int barHeight = (int) ((mark / maxMark) * chartHeight);
            int y = chartY + chartHeight - barHeight;

            // Choose color based on mark (using purple scheme)
            Color barColor;
            if (mark >= 75) {
                barColor = new Color(127, 82, 139);     // Purple for distinction
            } else if (mark >= 50) {
                barColor = new Color(232, 217, 236);    // Light purple for pass
            } else {
                barColor = new Color(170, 150, 180);    // Grey-purple for fail
            }

            // Draw bar
            g2.setColor(barColor);
            g2.fillRect(x, y, barWidth, barHeight);
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, barWidth, barHeight);

            // Draw mark on top (only if bar is tall enough)
            if (barHeight > 15) {
                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                String markStr = String.format("%.0f", mark); // Whole number
                int markWidth = g2.getFontMetrics().stringWidth(markStr);
                g2.setColor(Color.WHITE);
                g2.drawString(markStr, x + (barWidth - markWidth) / 2, y + 12);
            }

            // Draw student name (truncated)
            String displayName = studentName;
            if (studentName.length() > 8) {
                displayName = studentName.substring(0, 8) + ".";
            }
            g2.setFont(new Font("Arial", Font.PLAIN, 9));
            int nameWidth = g2.getFontMetrics().stringWidth(displayName);
            g2.setColor(Color.BLACK);
            g2.drawString(displayName, x + (barWidth - nameWidth) / 2, chartY + chartHeight + 15);

            x += barWidth + spacing;
        }

        // Draw axes
        drawAxes(g2, chartX, chartY, x - chartX - spacing, chartHeight, (int)maxMark);

        // Draw legend
        int legendY = chartY - 20;
        g2.setFont(new Font("Arial", Font.PLAIN, 10));

        g2.setColor(new Color(127, 82, 139));
        g2.fillRect(chartX, legendY, 12, 8);
        g2.setColor(Color.BLACK);
        g2.drawString("≥75", chartX + 15, legendY + 8);

        g2.setColor(new Color(232, 217, 236));
        g2.fillRect(chartX + 50, legendY, 12, 8);
        g2.setColor(Color.BLACK);
        g2.drawString("50-74", chartX + 65, legendY + 8);

        g2.setColor(new Color(170, 150, 180));
        g2.fillRect(chartX + 110, legendY, 12, 8);
        g2.setColor(Color.BLACK);
        g2.drawString("<50", chartX + 125, legendY + 8);

        // Show truncation notice if needed
        if (truncated) {
            g2.setFont(new Font("Arial", Font.ITALIC, 10));
            g2.setColor(new Color(100, 100, 100));
            String notice = "Showing top 10 of " + performanceData.size() + " students";
            int noticeWidth = g2.getFontMetrics().stringWidth(notice);
            g2.drawString(notice, (width - noticeWidth) / 2, chartY + chartHeight + 40);
        }
    }
    
    private void drawGradeDistributionChart(Graphics2D g2, int width, int height) {
        if (barChartData == null || barChartData.isEmpty()) {
            drawNoDataMessage(g2, width, height);
            return;
        }

        int chartX = 60;
        int chartY = 60;
        int chartWidth = width - 120;
        int chartHeight = height - 120;

        // Find max value for scaling
        int maxValue = barChartData.values().stream().max(Integer::compare).orElse(1);

        // Define the order of grades (alphabetical)
        String[] gradeOrder = {"A+", "A", "A-", "B+", "B", "B-", "C", "C-", "D", "F+", "F", "AS"};

        // Remove grades that don't have data to keep chart clean
        List<String> gradesToShow = new ArrayList<>();
        for (String grade : gradeOrder) {
            if (barChartData.containsKey(grade)) {
                gradesToShow.add(grade);
            }
        }

        // If no grades with data, show message
        if (gradesToShow.isEmpty()) {
            drawNoDataMessage(g2, width, height);
            return;
        }

        // Draw bars
        int barCount = gradesToShow.size();
        int barWidth = Math.min(40, chartWidth / Math.max(1, barCount * 2));
        int spacing = barWidth;

        int x = chartX;
        int colorIndex = 0;
        Color[] colors = {
            new Color(127, 82, 139),   // Purple
            new Color(232, 217, 236),  // Light Purple
            new Color(200, 180, 210),  // Medium Purple-Grey
            new Color(170, 150, 180),  // Dark Purple-Grey
            new Color(140, 120, 150),  // Very Dark Purple-Grey
            new Color(220, 210, 225)   // Very Light Purple
        };

        for (String grade : gradesToShow) {
            int value = barChartData.get(grade);

            // Calculate bar height
            int barHeight = (int) ((double) value / maxValue * chartHeight);
            int y = chartY + chartHeight - barHeight;

            // Draw bar
            g2.setColor(colors[colorIndex % colors.length]);
            g2.fillRect(x, y, barWidth, barHeight);

            // Draw bar outline
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, barWidth, barHeight);

            // Draw grade label
            g2.setFont(new Font("Arial", Font.PLAIN, 12));
            FontMetrics fm = g2.getFontMetrics();
            int gradeWidth = fm.stringWidth(grade);
            g2.drawString(grade, x + (barWidth - gradeWidth) / 2, chartY + chartHeight + 20);

            // Draw value on top of bar
            String valueStr = String.valueOf(value);
            int valueWidth = fm.stringWidth(valueStr);
            if (barHeight > 15) {
                g2.setColor(Color.WHITE);
                g2.drawString(valueStr, x + (barWidth - valueWidth) / 2, y + 15);
            } else {
                g2.setColor(Color.BLACK);
                g2.drawString(valueStr, x + (barWidth - valueWidth) / 2, y - 5);
            }

            x += barWidth + spacing;
            colorIndex++;
        }

        // Draw axes
        drawAxes(g2, chartX, chartY, chartWidth, chartHeight, maxValue);
    }
    
    private void drawAxes(Graphics2D g2, int chartX, int chartY, int chartWidth, int chartHeight, int maxValue) {
        g2.setColor(Color.BLACK);
        g2.drawLine(chartX, chartY, chartX, chartY + chartHeight); // Y-axis
        g2.drawLine(chartX, chartY + chartHeight, chartX + chartWidth, chartY + chartHeight); // X-axis
        
        // Draw Y-axis labels
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= 5; i++) {
            int yValue = (int) ((double) i / 5 * maxValue);
            int yPos = chartY + chartHeight - (i * chartHeight / 5);
            g2.drawString(String.valueOf(yValue), chartX - 25, yPos + 4);
            g2.drawLine(chartX - 5, yPos, chartX, yPos);
        }
    }
    
    private void drawNoDataMessage(Graphics2D g2, int width, int height) {
        g2.setFont(new Font("Arial", Font.ITALIC, 14));
        g2.setColor(new Color(120, 120, 120));
        String message = "No data available for selected criteria";
        FontMetrics fm = g2.getFontMetrics();
        int messageWidth = fm.stringWidth(message);
        g2.drawString(message, (width - messageWidth) / 2, height / 2);
    }
    
    public void setGradeChartData(Map<String, Integer> data) {
        this.barChartData = data;
        this.chartType = "gradeDistribution";
        this.instructionMessage = null;
        hideTable();
        repaint();
    }

}