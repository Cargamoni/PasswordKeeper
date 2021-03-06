/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.passwordkeeper.forms;

import com.passwordkeeper.classes.AlgorithmAES;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 *
 * @author Cargamoni
 */
public class CategoryFrame extends javax.swing.JFrame {

    /**
     * Creates new form PasswordsFrame
     */
    AlgorithmAES FromClass;
    public CategoryFrame() throws ParserConfigurationException, SAXException, IOException {
        initComponents();
    }

    public CategoryFrame(AlgorithmAES AesClass) throws ParserConfigurationException, SAXException, IOException {
        FromClass = AesClass;
        initComponents();
    }

    /*
    Experimental
     */

    private JRadioButtonMenuItem items[]; // holds items for colors
    String menuItems[] = { "Add Password to Category", "Show Category Passwords","Modify Category","Delete Category" };

    public DefaultListModel CategoryModel() throws IOException, SAXException, ParserConfigurationException
    {
        DefaultListModel model = new DefaultListModel();
        String[] strings = FromClass.CategoryListReturner();
        for(int i = 0; i < strings.length; i++)
            model.addElement(strings[i]);
        return model;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() throws IOException, SAXException, ParserConfigurationException {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jButton1 = new javax.swing.JButton();

        this.setTitle("Category List");

        setResizable(false);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jList1.setModel(CategoryModel());
//        jList1.setModel(new javax.swing.AbstractListModel<String>() {
//            String[] strings = FromClass.CategoryListReturner();
//            public int getSize() { return strings.length; }
//            public String getElementAt(int i) { return strings[i]; }
//        });
        jScrollPane1.setViewportView(jList1);

        jButton1.setText("Add a Category");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jButton1ActionPerformed(evt);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                }
            }});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();

        //Ekranın ortasında çıkması için
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int xLoc = (dim.width/2-this.getSize().width/2);
        int yLoc = (dim.height/2)-this.getSize().height/2;
        this.setLocation(xLoc,yLoc);

    /*
    Experimental
     */
        // Popuplist için event handler
        // Butonlar için Icon bulunacak !
        ItemHandler handler = new ItemHandler();
        ButtonGroup bntGroup = new ButtonGroup();
        items = new JRadioButtonMenuItem[menuItems.length];

        for (int i = 0; i < items.length; i++)
        {
            if(i == items.length -1)
                jPopupMenu1.addSeparator();
            items[i] = new JRadioButtonMenuItem(menuItems[i]);
            jPopupMenu1.add(items[i]);
            bntGroup.add(items[i]);
            items[i].addActionListener(handler);
        }

        jList1.addMouseListener(new MouseAdapter()
         {
             public void mousePressed(MouseEvent event) { checkForTriggerEvent(event); }
             public void mouseReleased(MouseEvent event) { checkForTriggerEvent(event); }
             private void checkForTriggerEvent(MouseEvent event) {
                 if (event.isPopupTrigger()) jPopupMenu1.show(event.getComponent(), event.getX(), event.getY());
             }
         }
        );

    /*
    Experimental Ending
     */


    }// </editor-fold>//GEN-END:initComponents

    /*
     Experimental
     */

    // Popup menü elemanlarına tıklandığı zaman neyin yapılacağının belirleneceği bölüm !
    private class ItemHandler implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            for (int i = 0; i < items.length; i++) {
                if (event.getSource() == items[i]) {
                    //Debug
                    //JOptionPane.showMessageDialog(null, menuItems[i] + " " + String.valueOf(i), "Success", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            if ( i == 0)
                            {
                                if(!jList1.isSelectionEmpty())
                                {
                                    PasswordAddModify AddPassword = new PasswordAddModify(FromClass, jList1.getSelectedIndex());
                                    AddPassword.setVisible(true);
                                    setVisible(false);
                                }
                            }
                            else if(i == 1)
                            {
                                if(!jList1.isSelectionEmpty())
                                {
                                    if(FromClass.CategoryPasswordsReturner(jList1.getSelectedIndex()).length != 0)
                                    {
                                        PasswordFrame ShowPasswords = new PasswordFrame(FromClass, jList1.getSelectedIndex());
                                        ShowPasswords.setVisible(true);
                                        setVisible(false);
                                    }
                                    else
                                    {
                                        JOptionPane.showMessageDialog(null, "There is no Password in this Category !", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }
                            }
                            else if (i == 2)
                            {
                                CreateCategoryFrame ModifyFrame = new CreateCategoryFrame(FromClass, jList1.getSelectedIndex());
                                setVisible(false);
                                ModifyFrame.setVisible(true);
                            }
                            else if(i == 3)
                            {
                                if(!jList1.isSelectionEmpty())
                                {
                                    //JDialog areYouSure = new JDialog();
                                    String[] options = {"Accept", "Decline"};
                                    int x = JOptionPane.showOptionDialog(null, "When category deleted, all category passwords are going to deleted ! \n\n Do you want to proceed ?",
                                            "Category Remove",
                                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, options, options[0]);
                                    if(x == 0)
                                    {
                                        FromClass.DeleteThisCategory(jList1.getSelectedIndex());
                                        JOptionPane.showMessageDialog(null, "Category Removed !", "Success", JOptionPane.INFORMATION_MESSAGE);
                                        jList1.setModel(CategoryModel());
                                    }
                                }
                            }
                        } catch (ParserConfigurationException e) { e.printStackTrace(); } catch (SAXException e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }

                    return;
                }
            }
        }
    }

    /*
     Experimental
     */

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) throws IOException, SAXException, ParserConfigurationException {
        CreateCategoryFrame CreateCategoryButton = new CreateCategoryFrame(FromClass);
        setVisible(false);
        CreateCategoryButton.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CategoryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CategoryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CategoryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CategoryFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new CategoryFrame().setVisible(true);

                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JList<String> jList1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
