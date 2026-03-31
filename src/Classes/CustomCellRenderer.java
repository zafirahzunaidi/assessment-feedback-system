/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author aqilahazis
 */
public class CustomCellRenderer extends DefaultTableCellRenderer {
//    private final int targetRow;
//    private final int targetColumn;
//    private final int numberofCells;
    private final List<Integer> rowsToColor;
    private final int targetColumnIndex;
    
    //private static List<List<Integer>> information = new ArrayList<>();

    public CustomCellRenderer(List<Integer> rowsToColor, int targetColumnIndex) {
        this.rowsToColor = rowsToColor;
        this.targetColumnIndex = targetColumnIndex;
        //this.information = information;
//        this.targetColumn = information[1];
//        this.numberofCells = numberofCells;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        // Call the super method to handle default rendering (e.g., selection highlighting)
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Check if the current row is in the list AND it is the target column
        if(targetColumnIndex == -1){
            cellComponent.setBackground(Color.BLUE);
        }
        else if (column == targetColumnIndex && rowsToColor.contains(row)) {
            // Change background color for specified rows in the target column
            cellComponent.setBackground(Color.decode("#b8d2e4")); 
            // Optional: change foreground color
            cellComponent.setForeground(Color.BLACK);
        } else {
            // Restore default colors for other cells to avoid rendering issues
            // Use table.getBackground() or table.getForeground() for default
            cellComponent.setBackground(table.getBackground());
            //cellComponent.setBackground(Color.white);
            //cellComponent.setForeground(table.getForeground());
        }
        
        return cellComponent;
    }

//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
//        
//        for (List<Integer> eachlist : information) {
//            int targetColumn = eachlist.get(0);
//            int targetRow = eachlist.get(1);
//            int numberofCells = eachlist.get(2);
//            
//            // Check if the current column is the target column
//            if (column == targetColumn) {
//                // Change the color based on the row index
//                if (row == 1 || row == 3) {
//                    cell.setBackground(Color.PINK); // Set specific rows to PINK
//                } else {
//                    cell.setBackground(Color.WHITE); // Default color for other rows
//                }
//            } else {
//                // Ensure other columns use the default colors (especially important for alternating colors)
//                cell.setBackground(Color.WHITE); 
//            }   
//        }
//
//        // Check if the current cell matches the target coordinates
//        if (row >= targetRow && row <= targetRow + numberofCells && column == targetColumn) {
//            cell.setBackground(Color.decode("#b8d2e4")); // Change to your desired color
//            //cell.setForeground(Color.WHITE); // Optional: change text color for readability
//        } else {
//            // Revert colors for other cells to default (important!)
//            // Using getBackground() and getForeground() ensures default look is maintained
//            //cell.setBackground(table.getBackground());
//            //cell.setBackground(Color.BLUE);
//            //cell.setForeground(table.getForeground());
//        }
//
//        return cell;
//    }
}
