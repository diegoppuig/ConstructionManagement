package systemGuis;


import domain.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

/*
    Davit Dostourian Erbe 281665 & Diego Pereira Puig - 329028
*/

public class RegisterConstructionSite extends javax.swing.JFrame implements Observer {
    
   private ConstructionsManagementSystem system1;
    private DefaultListModel<String> ownerListModel;
    private DefaultListModel<String> foremanListModel;
    private DefaultListModel<String> categoryListModel;
    private Map<String, Integer> selectedCategories; // Category name and its budget in cents
    private double totalBudget;

    
     public RegisterConstructionSite(ConstructionsManagementSystem system) {
        system1 = system;
        ownerListModel = new DefaultListModel<>();
        foremanListModel = new DefaultListModel<>();
        categoryListModel = new DefaultListModel<>();
        selectedCategories = new HashMap<>();
        totalBudget = 0.0;
        initComponents();
        loadOwnerList();
        loadForemanList();
        loadPanelCategories();
        system1.addObserver(this);
    }
 
      private void loadOwnerList() {
        ownerListModel.clear();
        List<Owner> owners = system1.obtainOwners();
        for (Owner owner : owners) {
            ownerListModel.addElement(owner.getName());
        }
        OwnerList.setModel(ownerListModel); // Ensure JList is updated
        System.out.println("Loaded owners: " + ownerListModel);
    }

    private void loadForemanList() {
        foremanListModel.clear();
        List<Foreman> foremen = system1.obtainForemen();
        for (Foreman foreman : foremen) {
            foremanListModel.addElement(foreman.getName());
        }
        ForemanList.setModel(foremanListModel); // Ensure JList is updated
        System.out.println("Loaded foremen: " + foremanListModel);
    }
    
    private void loadPanelCategories() {
        //categoryListModel.clear();
        panelCategories.removeAll();
        List<Category> categories = system1.obtainCategories();
        for (Category category : categories) {
            JButton nuevo = new JButton(category.getName());
            nuevo.setMargin(new Insets(-5, -5, -5, -5));
            nuevo.setBackground(Color.BLACK);
            nuevo.setForeground(Color.WHITE);
            nuevo.setText(category.getName());
            nuevo.addActionListener(new CategoryListener());
            panelCategories.add(nuevo);
        }
        panelCategories.revalidate();
        panelCategories.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ConstructionsManagementSystem){
            loadOwnerList();
            loadForemanList();
            loadPanelCategories();
        }
    }
    
    
    
  private class CategoryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton cual = (JButton) e.getSource();
            String categoryName = cual.getText().split("\n")[0].trim(); // Only get the category name
            String montoStr = JOptionPane.showInputDialog("Ingrese el monto para " + categoryName + ":");
            if (montoStr == null) {
                // User canceled the input dialog
                return;
            }
            try {
                double monto = Double.parseDouble(montoStr);
                int montoCents = (int) (monto * 100); // Convert to cents
                if (monto == 0) {
                    cual.setBackground(Color.BLACK);
                    cual.setForeground(Color.WHITE);
                    cual.setText(categoryName);
                    totalBudget -= selectedCategories.getOrDefault(categoryName, 0);
                    selectedCategories.remove(categoryName);
                } else {
                    cual.setBackground(Color.BLUE);
                    cual.setForeground(Color.WHITE);
                    totalBudget += montoCents - selectedCategories.getOrDefault(categoryName, 0);
                    selectedCategories.put(categoryName, montoCents);
                    cual.setText("<html>" + categoryName + "<br>$" + String.format("%.2f", monto) + "</html>"); // Update button text to include amount below the name
                }
                totalPresupuestoLoad.setText(String.format("%.2f", totalBudget / 100.0)); // Update total budget
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Monto inválido. Por favor, ingrese un número.");
            }
        }
    }
    
    private void updateCategoryPanel() {
        panelCategories.removeAll(); // Remove existing buttons
        for (Category category : system1.obtainCategories()) {
            JButton categoryButton = new JButton(category.getName());
            categoryButton.setMargin(new Insets(-5, -5, -5, -5));
            categoryButton.setBackground(Color.BLACK);
            categoryButton.setForeground(Color.WHITE);
            // Check if category is already selected
            int amount = selectedCategories.getOrDefault(category.getName(), 0);
            if (amount > 0) {
                categoryButton.setBackground(Color.BLUE);
                categoryButton.setText("<html>" + category.getName() + "<br>$" + String.format("%.2f", amount / 100.0) + "</html>"); // Update button text with amount
            }
            categoryButton.addActionListener(new CategoryListener());
            panelCategories.add(categoryButton);
        }
        panelCategories.revalidate();
        panelCategories.repaint();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        OwnerListLabel = new javax.swing.JLabel();
        ForemanListLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        OwnerList = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        ForemanList = new javax.swing.JList<>();
        PermitNumberLable = new javax.swing.JLabel();
        PermitNumberInput = new javax.swing.JTextField();
        AdressLabel = new javax.swing.JLabel();
        AdressInput = new javax.swing.JTextField();
        StartConstructionLabel = new javax.swing.JLabel();
        MonthInput = new javax.swing.JSpinner();
        YearsInput = new javax.swing.JSpinner();
        TotalBudgetLabel = new javax.swing.JLabel();
        javax.swing.JButton AddButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        panelCategories = new javax.swing.JPanel();
        totalPresupuestoLoad = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Registro de Obra");
        setMinimumSize(new java.awt.Dimension(390, 465));
        setPreferredSize(new java.awt.Dimension(390, 465));
        setSize(new java.awt.Dimension(5, 5));
        getContentPane().setLayout(null);

        jLabel1.setText("Registro de Obra");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(20, 10, 100, 16);

        OwnerListLabel.setText("Propietarios");
        getContentPane().add(OwnerListLabel);
        OwnerListLabel.setBounds(20, 40, 70, 16);

        ForemanListLabel.setText("Capataces");
        getContentPane().add(ForemanListLabel);
        ForemanListLabel.setBounds(210, 40, 60, 16);

        OwnerList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(OwnerList);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 60, 160, 146);

        ForemanList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(ForemanList);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(210, 60, 160, 146);

        PermitNumberLable.setText("Permiso Nro");
        getContentPane().add(PermitNumberLable);
        PermitNumberLable.setBounds(410, 30, 80, 16);

        PermitNumberInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PermitNumberInputActionPerformed(evt);
            }
        });
        getContentPane().add(PermitNumberInput);
        PermitNumberInput.setBounds(410, 60, 64, 22);

        AdressLabel.setText("Dirección");
        getContentPane().add(AdressLabel);
        AdressLabel.setBounds(410, 100, 70, 16);

        AdressInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AdressInputActionPerformed(evt);
            }
        });
        getContentPane().add(AdressInput);
        AdressInput.setBounds(410, 130, 160, 22);

        StartConstructionLabel.setText("Comienzo Mes/Año");
        getContentPane().add(StartConstructionLabel);
        StartConstructionLabel.setBounds(410, 180, 110, 16);
        getContentPane().add(MonthInput);
        MonthInput.setBounds(410, 210, 64, 22);
        getContentPane().add(YearsInput);
        YearsInput.setBounds(490, 210, 64, 22);

        TotalBudgetLabel.setText("Total Presupuesto");
        getContentPane().add(TotalBudgetLabel);
        TotalBudgetLabel.setBounds(410, 250, 120, 16);

        AddButton.setText("Agregar");
        AddButton.setToolTipText("");
        AddButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AddButtonMouseClicked(evt);
            }
        });
        AddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddButtonActionPerformed(evt);
            }
        });
        getContentPane().add(AddButton);
        AddButton.setBounds(420, 380, 110, 23);

        panelCategories.setPreferredSize(new java.awt.Dimension(339, 200));
        panelCategories.setLayout(new java.awt.GridLayout(0, 2));
        jScrollPane5.setViewportView(panelCategories);

        getContentPane().add(jScrollPane5);
        jScrollPane5.setBounds(20, 230, 350, 190);

        totalPresupuestoLoad.setText("0");
        getContentPane().add(totalPresupuestoLoad);
        totalPresupuestoLoad.setBounds(560, 250, 80, 16);

        setBounds(0, 0, 667, 441);
    }// </editor-fold>//GEN-END:initComponents

    private void PermitNumberInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PermitNumberInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PermitNumberInputActionPerformed

    private void AdressInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AdressInputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AdressInputActionPerformed

    private void AddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddButtonActionPerformed
        // BUTTON CODE
    }//GEN-LAST:event_AddButtonActionPerformed

    private void AddButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AddButtonMouseClicked
    // Reset totalBudget before starting a new registration process
    totalBudget = 0;
    totalPresupuestoLoad.setText(String.format("%.2f", totalBudget / 100.0)); // Reset totalPresupuestoLoad

    // Extract and validate permit number
    String permitNumber = PermitNumberInput.getText();
    boolean permitNumberExists = false;
    
    if (!isValidPermitNumber(permitNumber)) {
        JOptionPane.showMessageDialog(null, "Permiso Nro inválido. Debe ser un número.");
        return;
    }
      
    for (ConstructionSite site : system1.obtainConstructionSites()) {
        if (site.getPermitNumber().equals(permitNumber)) {
            permitNumberExists = true;
            break;
        }
    }
    if (permitNumberExists) {
        JOptionPane.showMessageDialog(null, "Permiso Nro inválido. Ya existe una obra con el numero de permiso ingresado.");
        return;
    }

    String address = AdressInput.getText();
    if (address.length() < 4) {
        JOptionPane.showMessageDialog(null, "Dirección inválida. Debe tener al menos 4 dígitos.");
        return;
    }

    int month = (Integer) MonthInput.getValue();
    if (month < 1 || month > 12) {
        JOptionPane.showMessageDialog(null, "Mes inválido. Debe estar entre 1 y 12.");
        return;
    }

    int year = (Integer) YearsInput.getValue();
    if (year < 1990) {
        JOptionPane.showMessageDialog(null, "Año inválido. Debe ser igual o mayor a 1990.");
        return;
    }

    // Check if at least one owner and foreman is selected
    if (OwnerList.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un propietario.");
        return;
    }
    if (ForemanList.getSelectedIndex() == -1) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar un capataz.");
        return;
    }

    // Check if at least one category is selected
    if (selectedCategories.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Debe seleccionar al menos una categoría.");
        return;
    }

    // Get selected owner and foreman objects
    int selectedOwnerIndex = OwnerList.getSelectedIndex();
    Owner selectedOwner = system1.obtainOwners().get(selectedOwnerIndex);

    int selectedForemanIndex = ForemanList.getSelectedIndex();
    Foreman selectedForeman = system1.obtainForemen().get(selectedForemanIndex);

    if (year < selectedForeman.getYear()){
        JOptionPane.showMessageDialog(null, "Año inválido. El ano del del obra no puede ser antes del ano de entrada del capataz. (" + selectedForeman.getYear() + ").");
        return;
    }
    
    // Convert selectedCategories to Map<String, Double>
    Map<String, Double> selectedCategoriesInDollars = new HashMap<>();
    for (Map.Entry<String, Integer> entry : selectedCategories.entrySet()) {
        selectedCategoriesInDollars.put(entry.getKey(), entry.getValue() / 100.0);
    }

    // Calculate total budget
    double totalBudgetForRegistration = 0;
    for (double amount : selectedCategories.values()) {
        totalBudgetForRegistration += amount;
    }

    // Create and register the new construction site
    system1.registerConstructionSite(selectedOwner, selectedForeman, permitNumber, address, month, year, totalBudgetForRegistration / 100.0, selectedCategoriesInDollars);

    // Clear form fields and reset total budget
    PermitNumberInput.setText("");
    AdressInput.setText("");
    MonthInput.setValue(1);
    YearsInput.setValue(2024);
    selectedCategories.clear();
    totalBudget = 0; // Reset total budget
    totalPresupuestoLoad.setText("0.00");
    updateCategoryPanel(); // Update category panel to reflect changes

    JOptionPane.showMessageDialog(null, "Obra registrada exitosamente!");
    }
    
 private boolean isValidPermitNumber(String permitNumber) {
        try {
            Integer.parseInt(permitNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    
  
    }//GEN-LAST:event_AddButtonMouseClicked

 
    /**
     * @param args the command line arguments
     */
    public void main(String args[]) {
       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterConstructionSite(new ConstructionsManagementSystem()).setVisible(true);
            }
        });
    
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RegisterConstructionSite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegisterConstructionSite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegisterConstructionSite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegisterConstructionSite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegisterConstructionSite(system1).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AdressInput;
    private javax.swing.JLabel AdressLabel;
    private javax.swing.JList<String> ForemanList;
    private javax.swing.JLabel ForemanListLabel;
    private javax.swing.JSpinner MonthInput;
    private javax.swing.JList<String> OwnerList;
    private javax.swing.JLabel OwnerListLabel;
    private javax.swing.JTextField PermitNumberInput;
    private javax.swing.JLabel PermitNumberLable;
    private javax.swing.JLabel StartConstructionLabel;
    private javax.swing.JLabel TotalBudgetLabel;
    private javax.swing.JSpinner YearsInput;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JPanel panelCategories;
    private javax.swing.JLabel totalPresupuestoLoad;
    // End of variables declaration//GEN-END:variables

}

