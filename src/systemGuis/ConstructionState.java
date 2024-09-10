package systemGuis;

import domain.ConstructionsManagementSystem;
import domain.Category;
import domain.ConstructionSite;
import domain.Expenditures;
import java.awt.Component;
import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class ConstructionState extends javax.swing.JFrame implements Observer {

   private ConstructionsManagementSystem system1;
    private DefaultListModel<String> constructionSiteListModel;
    private DefaultListModel<String> categoryListModel;
    private DefaultListModel<String> registeredExpendituresListModel;
    private DefaultListModel<String> selectedCategoryModel;
    private List<Expenditures> currentExpendituresList;

    public ConstructionState(ConstructionsManagementSystem system) {
        system1 = system;
        initComponents();
        system1.addObserver(this);

        // Initialize the list models and set them to the respective JLists
        constructionSiteListModel = new DefaultListModel<>();
        categoryListModel = new DefaultListModel<>();
        registeredExpendituresListModel = new DefaultListModel<>();
        selectedCategoryModel = new DefaultListModel<>();

        ConstructionList.setModel(constructionSiteListModel);
        categoryConstruction.setModel(categoryListModel);
        registeredExpendituresList.setModel(registeredExpendituresListModel);
        descriptionAmmountCategory.setModel(selectedCategoryModel);
        
        // Set the custom renderer
        descriptionAmmountCategory.setCellRenderer(new ExpenditureCellRenderer());
        descriptionAmmountCategory.setOpaque(true);

        // Load the construction sites
        loadConstructionSites();

        // Add listener for registeredExpendituresList
        registeredExpendituresList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    loadSelectedCategoryExpenditures();
                }
            }
        });
    }

    private void loadConstructionSites() {
        constructionSiteListModel.clear();
        List<ConstructionSite> constructionSites = system1.obtainConstructionSites();

        for (ConstructionSite site : constructionSites) {
            constructionSiteListModel.addElement(site.getAddress());
        }
    }

    private void loadSelectedCategoryExpenditures() {
        int selectedCategoryIndex = registeredExpendituresList.getSelectedIndex();
        if (selectedCategoryIndex != -1) {
            String selectedCategoryName = registeredExpendituresListModel.getElementAt(selectedCategoryIndex);

            int selectedIndex = ConstructionList.getSelectedIndex();
            if (selectedIndex != -1) {
                ConstructionSite selectedSite = system1.obtainConstructionSites().get(selectedIndex);
                Category selectedCategory = new Category(selectedCategoryName, "", 0.0);

                selectedCategoryModel.clear();
                currentExpendituresList = selectedSite.obtainExpendituresPerCategory(selectedCategory);

                System.out.println("Selected Category: " + selectedCategoryName);
                System.out.println("Expenditures List Size: " + currentExpendituresList.size());

                for (Expenditures expenditure : currentExpendituresList) {
                    System.out.println("Adding Expenditure: " + expenditure.getDescription() + " - $" + expenditure.getAmount());
                    selectedCategoryModel.addElement(expenditure.getDescription() + " - $" + expenditure.getAmount());
                    descriptionAmmountCategory.setCellRenderer(new ExpenditureCellRenderer());
                }
                descriptionAmmountCategory.repaint();
            }
        }
    }

    private void displayConstructionSiteDetails(ConstructionSite selectedSite) {
        double totalExpenditures = selectedSite.getTotalExpenditures();
        double totalPaidExpenditures = selectedSite.getTotalPaidExpenditures();
        double totalUnpaidExpenditures = selectedSite.getTotalUnpaidExpenditures();
        double rest = totalPaidExpenditures - totalUnpaidExpenditures;

        this.totalExpenditures.setText(String.format("%.2f", totalExpenditures));
        totalPaid.setText(String.format("%.2f", totalPaidExpenditures));
        totalUnpaid.setText(String.format("%.2f", totalUnpaidExpenditures));
        this.rest.setText(String.format("%.2f", rest));
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ConstructionsManagementSystem) {
            loadConstructionSites();
        }
    }


    private class ExpenditureCellRenderer extends JLabel implements ListCellRenderer<String> {
    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value);

        if (currentExpendituresList != null && index < currentExpendituresList.size()) {
            Expenditures expenditure = currentExpendituresList.get(index);
            ConstructionSite selectedSite = system1.obtainConstructionSites().get(ConstructionList.getSelectedIndex());

            boolean isOnBudgetList = selectedSite.getBudgetCategories().containsKey(expenditure.getCategory().getName());
            boolean isPaid = expenditure.isPaid();

             if (isOnBudgetList && isPaid) {
                setBackground(new Color(0, 255, 51)); // Green: [0,255,51]
                System.out.println("Setting color to GREEN for: " + value);
            } else if (isOnBudgetList && !isPaid) {
                setBackground(new Color(153, 204, 255)); // Cyan: [153,204,255]
                System.out.println("Setting color to CYAN for: " + value);
            } else if (!isOnBudgetList && isPaid) {
                setBackground(new Color(255, 204, 102)); // Orange: [255,204,102]
                System.out.println("Setting color to ORANGE for: " + value);
            } else if (!isOnBudgetList && !isPaid) {
                setBackground(new Color(255, 102, 204)); // Pink: [255,102,204]
                System.out.println("Setting color to PINK for: " + value);
            }
        }

    /*    if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
*/
        setOpaque(true); // Ensure the label is opaque
        return this;
    }
}

    private void loadCategoriesWithExpenditures() {
        int selectedIndex = ConstructionList.getSelectedIndex();
        if (selectedIndex != -1) {
            ConstructionSite selectedSite = system1.obtainConstructionSites().get(selectedIndex);
            
            registeredExpendituresListModel.clear();
            selectedCategoryModel.clear();

            List<Category> categoriesWithExpenditures = selectedSite.obtainCategoriesWithExpenditures();
            for (Category category : categoriesWithExpenditures) {
                registeredExpendituresListModel.addElement(category.getName());
            }

            loadFOremanName.setText(selectedSite.getForeman().getName());
            loadOwnerName.setText(selectedSite.getOwner().getName());
            loadStartConstruction.setText(selectedSite.getStartMonth() + "/" + selectedSite.getStartYear());
            inputPlaned.setText(String.format("%.2f", selectedSite.getTotalBudget()));
        } else {
            registeredExpendituresListModel.clear();
            selectedCategoryModel.clear();

            loadFOremanName.setText("");
            loadOwnerName.setText("");
            loadStartConstruction.setText("");
            inputPlaned.setText("0");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ConstructionListTitle = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ConstructionList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        categoryConstruction = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        registeredExpendituresList = new javax.swing.JList<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        descriptionAmmountCategory = new javax.swing.JList<>();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        inputPlaned = new javax.swing.JLabel();
        totalPaid = new javax.swing.JLabel();
        totalExpenditures = new javax.swing.JLabel();
        totalUnpaid = new javax.swing.JLabel();
        rest = new javax.swing.JLabel();
        loadFOremanName = new javax.swing.JLabel();
        loadOwnerName = new javax.swing.JLabel();
        loadStartConstruction = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Estado de Obra");
        setMinimumSize(new java.awt.Dimension(350, 425));
        setResizable(false);
        getContentPane().setLayout(null);

        ConstructionListTitle.setText("Obras");
        getContentPane().add(ConstructionListTitle);
        ConstructionListTitle.setBounds(10, 20, 50, 16);

        ConstructionList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        ConstructionList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                ConstructionListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(ConstructionList);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(10, 40, 190, 80);

        jLabel1.setText("Presupuesto (Rubros y  montos)");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(10, 150, 190, 16);

        categoryConstruction.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(categoryConstruction);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(10, 170, 190, 146);

        jLabel2.setText("Rubros con gastos en la obra");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(230, 150, 190, 16);

        registeredExpendituresList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(registeredExpendituresList);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(230, 170, 180, 146);

        jLabel3.setText("Gastos del rubro seleccionado");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(450, 150, 200, 16);

        descriptionAmmountCategory.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(descriptionAmmountCategory);

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(450, 170, 260, 146);

        jLabel4.setText("Códigos de colores:");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(90, 330, 120, 16);

        jTextField1.setBackground(new java.awt.Color(0, 255, 51));
        jTextField1.setText("Presupuestado y Reintegrado");
        getContentPane().add(jTextField1);
        jTextField1.setBounds(230, 330, 210, 20);

        jTextField2.setBackground(new java.awt.Color(153, 204, 255));
        jTextField2.setText("Presupuestado y No Reintegrado");
        getContentPane().add(jTextField2);
        jTextField2.setBounds(230, 360, 210, 20);

        jTextField3.setBackground(new java.awt.Color(255, 204, 102));
        jTextField3.setText("No Presupuestado y Reintegrado");
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });
        getContentPane().add(jTextField3);
        jTextField3.setBounds(450, 330, 210, 22);

        jTextField4.setBackground(new java.awt.Color(255, 102, 204));
        jTextField4.setText("No Presupuestado y No Reintegrado");
        getContentPane().add(jTextField4);
        jTextField4.setBounds(450, 360, 210, 22);

        jLabel5.setText("Propietario:");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(240, 20, 80, 16);

        jLabel6.setText("Comienzo de Obra:");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(220, 50, 120, 16);

        jLabel7.setText("Total Gastado Ya Reintegrado:");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(220, 90, 170, 16);

        jLabel8.setText("Capataz:");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(510, 20, 60, 16);

        jLabel9.setText("Total Gastado:");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(490, 60, 100, 16);

        jLabel10.setText("Total Gastado No Reintegrado:");
        getContentPane().add(jLabel10);
        jLabel10.setBounds(490, 90, 180, 16);

        jLabel11.setText("Saldo:");
        getContentPane().add(jLabel11);
        jLabel11.setBounds(640, 130, 43, 16);

        jLabel12.setText("Total Presupuestado:");
        getContentPane().add(jLabel12);
        jLabel12.setBounds(220, 70, 140, 16);

        inputPlaned.setText("0");
        getContentPane().add(inputPlaned);
        inputPlaned.setBounds(410, 70, 70, 16);

        totalPaid.setText("0");
        getContentPane().add(totalPaid);
        totalPaid.setBounds(410, 90, 70, 16);

        totalExpenditures.setText("0");
        getContentPane().add(totalExpenditures);
        totalExpenditures.setBounds(650, 60, 90, 16);

        totalUnpaid.setText("0");
        getContentPane().add(totalUnpaid);
        totalUnpaid.setBounds(670, 90, 50, 16);

        rest.setText("0");
        getContentPane().add(rest);
        rest.setBounds(690, 130, 43, 16);
        getContentPane().add(loadFOremanName);
        loadFOremanName.setBounds(590, 20, 130, 20);
        getContentPane().add(loadOwnerName);
        loadOwnerName.setBounds(370, 20, 130, 20);
        getContentPane().add(loadStartConstruction);
        loadStartConstruction.setBounds(380, 50, 70, 20);

        setBounds(0, 0, 803, 400);
    }// </editor-fold>//GEN-END:initComponents

    private void ConstructionListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_ConstructionListValueChanged
    if (!evt.getValueIsAdjusting()) {
        int selectedIndex = ConstructionList.getSelectedIndex();
        if (selectedIndex != -1) {
            ConstructionSite selectedSite = system1.obtainConstructionSites().get(selectedIndex);

            categoryListModel.clear();
            registeredExpendituresListModel.clear();
            selectedCategoryModel.clear();

            Map<String, Double> categoriesWithBudgets = selectedSite.getBudgetCategories();
            for (Map.Entry<String, Double> entry : categoriesWithBudgets.entrySet()) {
                String categoryDisplay = "• " + entry.getKey() + " - $" + String.format("%.2f", entry.getValue());
                categoryListModel.addElement(categoryDisplay);
            }

            List<Category> categoriesWithExpenditures = selectedSite.obtainCategoriesWithExpenditures();
            for (Category category : categoriesWithExpenditures) {
                registeredExpendituresListModel.addElement(category.getName());
            }

            loadFOremanName.setText(selectedSite.getForeman().getName());
            loadOwnerName.setText(selectedSite.getOwner().getName());
            loadStartConstruction.setText(selectedSite.getStartMonth() + "/" + selectedSite.getStartYear());

            inputPlaned.setText(String.format("%.2f", selectedSite.getTotalBudget()));

            // Display the construction site details
            displayConstructionSiteDetails(selectedSite);
        } else {
            categoryListModel.clear();
            registeredExpendituresListModel.clear();
            selectedCategoryModel.clear();

            loadFOremanName.setText("");
            loadOwnerName.setText("");
            loadStartConstruction.setText("");
            inputPlaned.setText("0");

            totalExpenditures.setText("0");
            totalPaid.setText("0");
            totalUnpaid.setText("0");
            rest.setText("0");
        }
    }
    }//GEN-LAST:event_ConstructionListValueChanged

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed
  private void registeredExpendituresListValueChanged(javax.swing.event.ListSelectionEvent evt) {
         if (!evt.getValueIsAdjusting()) {
            int selectedIndex = registeredExpendituresList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedCategoryName = registeredExpendituresList.getSelectedValue();
                ConstructionSite selectedSite = system1.obtainConstructionSites().get(ConstructionList.getSelectedIndex());

                selectedCategoryModel.clear();
                currentExpendituresList = selectedSite.obtainExpendituresPerCategory(new Category(selectedCategoryName, "", 0.0));
                for (Expenditures expenditure : currentExpendituresList) {
                    selectedCategoryModel.addElement(expenditure.getDescription() + " - $" + expenditure.getAmount());
                }
                descriptionAmmountCategory.repaint();
            }
        }
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
            java.util.logging.Logger.getLogger(ConstructionState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConstructionState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConstructionState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConstructionState.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ConstructionState(system1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> ConstructionList;
    private javax.swing.JLabel ConstructionListTitle;
    private javax.swing.JList<String> categoryConstruction;
    private javax.swing.JList<String> descriptionAmmountCategory;
    private javax.swing.JLabel inputPlaned;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JLabel loadFOremanName;
    private javax.swing.JLabel loadOwnerName;
    private javax.swing.JLabel loadStartConstruction;
    private javax.swing.JList<String> registeredExpendituresList;
    private javax.swing.JLabel rest;
    private javax.swing.JLabel totalExpenditures;
    private javax.swing.JLabel totalPaid;
    private javax.swing.JLabel totalUnpaid;
    // End of variables declaration//GEN-END:variables
}
