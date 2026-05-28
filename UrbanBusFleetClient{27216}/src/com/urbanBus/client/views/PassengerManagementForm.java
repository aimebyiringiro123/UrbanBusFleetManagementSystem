/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.Passenger;
import com.urbanBus.server.model.PassType;
import com.urbanBus.server.model.User;
import com.urbanBus.server.model.UserRole;
import com.urbanBus.server.service.PassengerService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author aimeb
 */
public class PassengerManagementForm extends javax.swing.JFrame {
    
    private User currentUser;
    private PassengerService passengerService;
    private Passenger selectedPassenger;
    private DefaultTableModel tableModel;

    /**
     * Creates new form PassengerForm
     */
    public PassengerManagementForm(User user) {
    this.currentUser = user;
    try {
        this.passengerService =
            RMIClient.getPassengerService();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Cannot connect to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    initComponents();
    getContentPane().setBackground(UIUtils.BG_PAGE);
    UIUtils.styleHeaderButton(btnBack);
    UIUtils.styleField(txtSearch);
    UIUtils.setPlaceholder(txtSearch, "Search by name or phone...");
    UIUtils.stylePrimaryButton(btnSearch);
    UIUtils.styleOutlineButton(brnRefresh);   // ← brnRefresh not btnRefresh
    btnSearch.setPreferredSize(new java.awt.Dimension(100, 36));
    brnRefresh.setPreferredSize(new java.awt.Dimension(100, 36));
    buildFormPanel();
    setLocationRelativeTo(null); 
    setupTable();
    loadPassengers();
}
    
    
    
    private void buildFormPanel() {
    jPanel2.removeAll();
    jPanel2.setLayout(new java.awt.GridBagLayout());
    jPanel2.setBackground(java.awt.Color.WHITE);
    jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(UIUtils.BORDER, 1, true),
        javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0; gbc.gridx = 0;

    gbc.gridy = 0; gbc.insets = new java.awt.Insets(0, 0, 14, 0);
    jPanel2.add(UIUtils.makeSectionLabel("PASSENGER DETAILS"), gbc);

    addFormRow(jPanel2, gbc, 1, "First Name", txtFirstName);
    addFormRow(jPanel2, gbc, 3, "Last Name",  txtLastName);
    addFormRow(jPanel2, gbc, 5, "Phone",      txtPhone);
    addFormRow(jPanel2, gbc, 7, "Email",      txtEmail);

    // Pass Type combo
    gbc.gridy = 9; gbc.insets = new java.awt.Insets(6, 0, 2, 0);
    javax.swing.JLabel lbl = new javax.swing.JLabel("Pass Type");
    lbl.setFont(UIUtils.FONT_LABEL); lbl.setForeground(UIUtils.TEXT_GRAY);
    jPanel2.add(lbl, gbc);
    gbc.gridy = 10; gbc.insets = new java.awt.Insets(0, 0, 16, 0);
    cmbPassType.setPreferredSize(new java.awt.Dimension(0, 36));
    UIUtils.styleCombo(cmbPassType);
    jPanel2.add(cmbPassType, gbc);

    // Spacer
    gbc.gridy = 11; gbc.weighty = 1.0;
    gbc.fill = java.awt.GridBagConstraints.VERTICAL;
    jPanel2.add(new javax.swing.JPanel() {{ setOpaque(false); }}, gbc);

    // Buttons
    gbc.gridy = 12; gbc.weighty = 0;
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    javax.swing.JPanel btnPanel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 8, 8));
    btnPanel.setOpaque(false);
    java.awt.Dimension d = new java.awt.Dimension(0, 36);
    saveBtn.setPreferredSize(d);   updateBtn.setPreferredSize(d);
    deleteBtn.setPreferredSize(d); clearBtn.setPreferredSize(d);
    UIUtils.stylePrimaryButton(saveBtn);
    UIUtils.styleOutlineButton(updateBtn);
    UIUtils.styleDangerButton(deleteBtn);
    UIUtils.styleOutlineButton(clearBtn);
    btnPanel.add(saveBtn);   btnPanel.add(updateBtn);
    btnPanel.add(deleteBtn); btnPanel.add(clearBtn);
    jPanel2.add(btnPanel, gbc);
    jPanel2.revalidate(); jPanel2.repaint();
}
    
    private void addFormRow(javax.swing.JPanel panel,
        java.awt.GridBagConstraints gbc, int y,
        String label, javax.swing.JTextField field) {
    gbc.gridy = y;
    gbc.insets = new java.awt.Insets(6, 0, 2, 0);
    javax.swing.JLabel lbl = new javax.swing.JLabel(label);
    lbl.setFont(UIUtils.FONT_LABEL);
    lbl.setForeground(UIUtils.TEXT_GRAY);
    panel.add(lbl, gbc);
    gbc.gridy = y + 1;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    field.setPreferredSize(new java.awt.Dimension(0, 36));
    UIUtils.styleField(field);
    panel.add(field, gbc);
}
    
    private void setupTable() {
        cmbPassType.setModel(new javax.swing.DefaultComboBoxModel(PassType.values()));
    String[] columns = {
        "ID", "First Name", "Last Name",
        "Phone", "Email", "Pass Type"
    };
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(
                int row, int col) {
            return false;
        }
    };
    passengerTable.setModel(tableModel);
    passengerTable.setRowHeight(30);
    passengerTable.getTableHeader().setBackground(
        new java.awt.Color(26, 62, 111));
    passengerTable.getTableHeader().setForeground(
        java.awt.Color.WHITE);
    passengerTable.setSelectionBackground(
        new java.awt.Color(210, 228, 255));

    // Hide ID column
    passengerTable.getColumnModel().getColumn(0)
        .setMinWidth(0);
    passengerTable.getColumnModel().getColumn(0)
        .setMaxWidth(0);

    // Load PassType into combobox
    cmbPassType.setModel(
        new javax.swing.DefaultComboBoxModel(
        PassType.values()));

    // Row click listener
    passengerTable.addMouseListener(
            new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(
                java.awt.event.MouseEvent e) {
            int row =
                passengerTable.getSelectedRow();
            if (row != -1) populateForm(row);
        }
    });
    
    UIUtils.styleTable(passengerTable);
    UIUtils.styleScrollPane(jScrollPane1);
    // Column 5 is "Pass Type" — style it as a badge too
    passengerTable.getColumnModel().getColumn(5).setCellRenderer(UIUtils.statusRenderer());
}
    
    
    private void loadPassengers() {
    if (passengerService == null) {
        JOptionPane.showMessageDialog(this,
            "Not connected to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Passenger> passengers =
            passengerService.findAllPassengers();
        for (Passenger p : passengers) {
            tableModel.addRow(new Object[]{
                p.getPassengerId(),
                p.getFirstName(),
                p.getLastName(),
                p.getPhone(),
                p.getEmail(),
                p.getPassType()
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading passengers: "
            + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    private void populateForm(int row) {
    try {
        Long id = (Long) tableModel.getValueAt(row, 0);
        selectedPassenger =
            passengerService.findPassengerById(id);
        txtFirstName.setText(
            selectedPassenger.getFirstName());
        txtLastName.setText(
            selectedPassenger.getLastName());
        txtPhone.setText(
            selectedPassenger.getPhone());
        txtEmail.setText(
            selectedPassenger.getEmail());
        cmbPassType.setSelectedItem(
            selectedPassenger.getPassType());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
    
    private boolean validateForm() {
    if (txtFirstName.getText().trim().isEmpty()) {
        showError("First name is required.");
        txtFirstName.requestFocus();
        return false;
    }
    if (txtLastName.getText().trim().isEmpty()) {
        showError("Last name is required.");
        txtLastName.requestFocus();
        return false;
    }
    if (txtPhone.getText().trim().isEmpty()) {
        showError("Phone number is required.");
        txtPhone.requestFocus();
        return false;
    }
    if (!txtPhone.getText().trim()
            .matches("07\\d{8}")) {
        showError(
            "Phone must be valid Rwandan number "
            + "(07XXXXXXXX).");
        txtPhone.requestFocus();
        return false;
    }
    if (txtEmail.getText().trim().length() > 0) {
        if (!txtEmail.getText().trim()
                .matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showError(
                "Please enter a valid email address.");
            txtEmail.requestFocus();
            return false;
        }
    }
    return true;
}
    
    
    private void clearForm() {
    txtFirstName.setText("");
    txtLastName.setText("");
    txtPhone.setText("");
    txtEmail.setText("");
    cmbPassType.setSelectedIndex(0);
    selectedPassenger = null;
    passengerTable.clearSelection();
}

private void showError(String message) {
    JOptionPane.showMessageDialog(this,
        message, "Validation Error",
        JOptionPane.ERROR_MESSAGE);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField6 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtLastName = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        cmbPassType = new javax.swing.JComboBox<>();
        saveBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        brnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        passengerTable = new javax.swing.JTable();

        jTextField6.setText("jTextField6");

        jButton5.setText("jButton5");

        jButton2.setText("jButton2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        lblTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblTitle.setText("Passenger Management");

        btnBack.setBackground(new java.awt.Color(0, 77, 77));
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("first name");

        jLabel2.setText("last name");

        jLabel3.setText("phone");

        jLabel4.setText("email");

        jLabel5.setText("pass type");

        cmbPassType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        updateBtn.setText("update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        clearBtn.setText("clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtEmail)
                            .addComponent(txtPhone)
                            .addComponent(txtLastName)
                            .addComponent(txtFirstName)
                            .addComponent(cmbPassType, 0, 150, Short.MAX_VALUE))
                        .addGap(42, 42, 42))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(saveBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(updateBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearBtn)
                        .addContainerGap(95, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel1)
                                            .addComponent(txtFirstName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(34, 34, 34)
                                        .addComponent(jLabel2))
                                    .addComponent(txtLastName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(39, 39, 39)
                                .addComponent(jLabel3))
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addComponent(jLabel4))
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(cmbPassType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn)
                    .addComponent(clearBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(245, 247, 250));

        btnSearch.setText("search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        brnRefresh.setText("refresh");
        brnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brnRefreshActionPerformed(evt);
            }
        });

        passengerTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(passengerTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(brnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSearch, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(brnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (selectedPassenger == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a passenger from "
            + "the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!validateForm()) return;
    try {
        selectedPassenger.setFirstName(
            txtFirstName.getText().trim());
        selectedPassenger.setLastName(
            txtLastName.getText().trim());
        selectedPassenger.setPhone(
            txtPhone.getText().trim());
        selectedPassenger.setEmail(
            txtEmail.getText().trim());
        selectedPassenger.setPassType(
            (PassType) cmbPassType.getSelectedItem());

        passengerService.updatePassenger(
            selectedPassenger);
        JOptionPane.showMessageDialog(this,
            "Passenger updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadPassengers();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (!validateForm()) return;
    try {
        Passenger passenger = new Passenger();
        passenger.setFirstName(
            txtFirstName.getText().trim());
        passenger.setLastName(
            txtLastName.getText().trim());
        passenger.setPhone(
            txtPhone.getText().trim());
        passenger.setEmail(
            txtEmail.getText().trim());
        passenger.setPassType(
            (PassType) cmbPassType.getSelectedItem());

        passengerService.savePassenger(passenger);
        JOptionPane.showMessageDialog(this,
            "Passenger saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadPassengers();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
         if (selectedPassenger == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a passenger from "
            + "the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete: "
        + selectedPassenger.getFullName() + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            passengerService.deletePassenger(
                selectedPassenger);
            JOptionPane.showMessageDialog(this,
                "Passenger deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadPassengers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        clearForm();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
         String keyword = txtSearch.getText().trim();
    if (keyword.isEmpty()) {
        loadPassengers();
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Passenger> passengers =
            passengerService.findAllPassengers();
        for (Passenger p : passengers) {
            if (p.getFirstName().toLowerCase()
                    .contains(keyword.toLowerCase())
                || p.getLastName().toLowerCase()
                    .contains(keyword.toLowerCase())
                || p.getPhone().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    p.getPassengerId(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getPhone(),
                    p.getEmail(),
                    p.getPassType()
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void brnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brnRefreshActionPerformed
        loadPassengers();
    }//GEN-LAST:event_brnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new AdminDashboard(currentUser).setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(PassengerManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PassengerManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PassengerManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PassengerManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setRole(UserRole.ADMIN);
                new PassengerManagementForm(testUser)
            .setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton brnRefresh;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton clearBtn;
    private javax.swing.JComboBox<String> cmbPassType;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable passengerTable;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
