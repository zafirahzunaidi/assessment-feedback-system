/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author aqilahazis
 */
public class getTableCellBackground {
    
    public static Color getTableCellBackground(JTable table, int row, int col) {
        TableCellRenderer renderer = table.getCellRenderer(row, col);
        Component component = table.prepareRenderer(renderer, row, col);
        return component.getBackground();
    }
    
}
