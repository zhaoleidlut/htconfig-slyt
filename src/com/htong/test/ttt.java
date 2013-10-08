package com.htong.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ttt extends JFrame {
   JLabel iconLabel = null; // 用来响应列表框选择的变化

   JList iconJList = null; // 定制的选择列表框

   DefaultListModel dlm;

   public ttt() {
       // 定义Object二维数组,用于初始化下拉框,参数依次为图标,显示文本,提示文本

       dlm = new DefaultListModel();
       for (int i = 0; i < 3; i++) {
           dlm.addElement(i);
       }
       iconJList = new JList(dlm);
       iconJList.setCellRenderer(new CellRenderer());
       JScrollPane jsp = new JScrollPane(iconJList);
       this.setLayout(new BorderLayout());
       this.getContentPane().add(jsp);
       this.setSize(300, 200);
       this.setLocation(500, 300);
       this.setVisible(true);
   }

   public static void main(String[] args) {
       ttt kjl = new ttt();
   }
}


class CellRenderer extends JLabel implements ListCellRenderer {
    CellRenderer() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        // JOptionPane.showMessageDialog(null, value);

//        if (value != null) {
            setText(value.toString());
//
//        }
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());

        }
        return this;
    }
} 
