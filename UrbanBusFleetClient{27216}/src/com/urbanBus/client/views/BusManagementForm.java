/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.*;
import com.urbanBus.server.service.BusService;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
    
/**
 *
 * @author aimeb
 */
public class BusManagementForm extends javax.swing.JFrame {
    
    private User currentUser;
    private BusService busService;
    private Bus selectedBus;
    private javax.swing.table.DefaultTableModel tableModel;

    /**
     * Creates new form BusManagementForm
     */
    public BusManagementForm(
        User user) {
    this.currentUser = user;
    try {
        this.busService = 
            RMIClient
                .getBusService();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Cannot connect to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    initComponents();
    buildFormPanel();   
        // ── Apply styling ─────────────────────────────────
    getContentPane().setBackground(UIUtils.BG_PAGE);

    // Style all input fields
    UIUtils.styleField(txtPlate);
    UIUtils.styleField(txtBrand);
    UIUtils.styleField(txtCapacity);
    UIUtils.styleField(txtFuel);
    UIUtils.styleField(txtMileage);
    UIUtils.styleCombo(cmbStatus);

    // Style all buttons
    UIUtils.stylePrimaryButton(saveBtn);
    UIUtils.styleOutlineButton(updateBtn);
    UIUtils.styleOutlineButton(clearBtn);
    UIUtils.styleDangerButton(deleteBtn);
    
    // Make all buttons same size
    Dimension btnSize = new Dimension(100, 36);
    saveBtn.setPreferredSize(btnSize);
    updateBtn.setPreferredSize(btnSize);
    deleteBtn.setPreferredSize(btnSize);
    clearBtn.setPreferredSize(btnSize);
   
   // Style the search field
    UIUtils.styleField(txtSearch);   // use your actual search field variable name
    txtSearch.setPreferredSize(new Dimension(txtSearch.getPreferredSize().width, 36));

   // Style search and refresh buttons  
    UIUtils.stylePrimaryButton(btnSearch);
    UIUtils.styleOutlineButton(btnRefresh);
   
    Dimension searchBtnSize = new Dimension(100, 36);
    btnSearch.setPreferredSize(searchBtnSize);
    btnRefresh.setPreferredSize(searchBtnSize);
    
    UIUtils.styleField(txtSearch);
    UIUtils.setPlaceholder(txtSearch, "Search by plate number/Brand");

    UIUtils.styleHeaderButton(btnBack);
    setLocationRelativeTo(null);  
    setupTable();
    loadBuses();
}
    
    
    
    
  private void setupTable() {
    String[] columns = {
        "ID", "Plate Number", "Brand",
        "Capacity", "Fuel Type", "Mileage", "Status"
    };
    tableModel = new javax.swing.table.DefaultTableModel(
            columns, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    busTable.setModel(tableModel);
    busTable.setRowHeight(30);
    busTable.getTableHeader().setBackground(
        new java.awt.Color(26, 62, 111));
    busTable.getTableHeader().setForeground(
        java.awt.Color.WHITE);
    busTable.setSelectionBackground(
        new java.awt.Color(210, 228, 255));

    // Hide ID column
    busTable.getColumnModel().getColumn(0)
        .setMinWidth(0);
    busTable.getColumnModel().getColumn(0)
        .setMaxWidth(0);

    // Load BusStatus into combobox
    cmbStatus.setModel(
        new javax.swing.DefaultComboBoxModel(
            BusStatus.values()));

    // Table row click listener
    busTable.addMouseListener(
            new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(
                java.awt.event.MouseEvent e) {
            int row = busTable.getSelectedRow();
            if (row != -1) populateForm(row);
        }
    });
    UIUtils.styleTable(busTable);
    busTable.getColumnModel().getColumn(6).setCellRenderer(UIUtils.statusRenderer());
    
}
    
    
  private void loadBuses() {
    try {
        tableModel.setRowCount(0);
        java.util.List<Bus> buses =
            busService.findAllBuses();
        for (Bus bus : buses) {
            tableModel.addRow(new Object[]{
                bus.getBusId(),
                bus.getPlateNumber(),
                bus.getBrand(),
                bus.getCapacity(),
                bus.getFuelType(),
                bus.getMileage(),
                bus.getStatus()
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading buses: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
  
  private void populateForm(int row) {
    try {
        Long id = (Long) tableModel.getValueAt(row, 0);
        selectedBus = busService.findBusById(id);
        txtPlate.setText(selectedBus.getPlateNumber());
        txtBrand.setText(selectedBus.getBrand());
        txtCapacity.setText(
            String.valueOf(selectedBus.getCapacity()));
        txtFuel.setText(selectedBus.getFuelType());
        txtMileage.setText(
            String.valueOf(selectedBus.getMileage()));
        cmbStatus.setSelectedItem(
            selectedBus.getStatus());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
  
  private boolean validateForm() {
    if (txtPlate.getText().trim().isEmpty()) {
        showError("Plate number is required.");
        txtPlate.requestFocus();
        return false;
    }
    if (txtBrand.getText().trim().isEmpty()) {
        showError("Brand is required.");
        txtBrand.requestFocus();
        return false;
    }
    if (txtCapacity.getText().trim().isEmpty()) {
        showError("Capacity is required.");
        txtCapacity.requestFocus();
        return false;
    }
    try {
        int cap = Integer.parseInt(
            txtCapacity.getText().trim());
        if (cap <= 0) {
            showError(
                "Capacity must be greater than zero.");
            txtCapacity.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        showError("Capacity must be a valid number.");
        txtCapacity.requestFocus();
        return false;
    }
    if (txtFuel.getText().trim().isEmpty()) {
        showError("Fuel type is required.");
        txtFuel.requestFocus();
        return false;
    }
    if (txtMileage.getText().trim().isEmpty()) {
        showError("Mileage is required.");
        txtMileage.requestFocus();
        return false;
    }
    try {
        double mil = Double.parseDouble(
            txtMileage.getText().trim());
        if (mil < 0) {
            showError("Mileage cannot be negative.");
            txtMileage.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        showError("Mileage must be a valid number.");
        txtMileage.requestFocus();
        return false;
    }
    return true;
}
    
  
  private void clearForm() {
    txtPlate.setText("");
    txtBrand.setText("");
    txtCapacity.setText("");
    txtFuel.setText("");
    txtMileage.setText("");
    cmbStatus.setSelectedIndex(0);
    selectedBus = null;
    busTable.clearSelection();
}

  
  private void showError(String message) {
    JOptionPane.showMessageDialog(this,
        message, "Validation Error",
        JOptionPane.ERROR_MESSAGE);
}

  
  private void buildFormPanel() {
    // Clear whatever NetBeans put in formPanel
    formPanel.removeAll();
    formPanel.setLayout(new java.awt.GridBagLayout());
    formPanel.setBackground(Color.WHITE);
    formPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(UIUtils.BORDER, 1, true),
        BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.insets = new java.awt.Insets(4, 0, 4, 0);
    gbc.weightx = 1.0;
    gbc.gridx = 0;

    // Section title
    gbc.gridy = 0;
    gbc.insets = new java.awt.Insets(0, 0, 14, 0);
    JLabel section = UIUtils.makeSectionLabel("BUS DETAILS");
    formPanel.add(section, gbc);

    // Helper to add a label + field pair
    addFormRow(formPanel, gbc, 1,  "Plate Number",  txtPlate);
    addFormRow(formPanel, gbc, 3,  "Brand",         txtBrand);
    addFormRow(formPanel, gbc, 5,  "Capacity",      txtCapacity);
    addFormRow(formPanel, gbc, 7,  "Fuel Type",     txtFuel);
    addFormRow(formPanel, gbc, 9,  "Mileage (km)",  txtMileage);

    // Status combo
    gbc.gridy = 11;
    gbc.insets = new java.awt.Insets(4, 0, 2, 0);
    JLabel lblStatus = new JLabel("Status");
    lblStatus.setFont(UIUtils.FONT_LABEL);
    lblStatus.setForeground(UIUtils.TEXT_GRAY);
    formPanel.add(lblStatus, gbc);

    gbc.gridy = 12;
    gbc.insets = new java.awt.Insets(0, 0, 16, 0);
    cmbStatus.setPreferredSize(new Dimension(0, 36));
    UIUtils.styleCombo(cmbStatus);
    formPanel.add(cmbStatus, gbc);

    // Spacer pushes buttons to bottom
    gbc.gridy = 13;
    gbc.weighty = 1.0;
    gbc.fill = java.awt.GridBagConstraints.VERTICAL;
    formPanel.add(new JPanel() {{ setOpaque(false); }}, gbc);

    // Buttons panel
    gbc.gridy = 14;
    gbc.weighty = 0;
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);

    JPanel btnPanel = new JPanel(new java.awt.GridLayout(2, 2, 8, 8));
    btnPanel.setOpaque(false);

    Dimension btnSize = new Dimension(0, 36);
    saveBtn.setPreferredSize(btnSize);
    updateBtn.setPreferredSize(btnSize);
    deleteBtn.setPreferredSize(btnSize);
    clearBtn.setPreferredSize(btnSize);

    UIUtils.stylePrimaryButton(saveBtn);
    UIUtils.styleOutlineButton(updateBtn);
    UIUtils.styleDangerButton(deleteBtn);
    UIUtils.styleOutlineButton(clearBtn);

    btnPanel.add(saveBtn);
    btnPanel.add(updateBtn);
    btnPanel.add(deleteBtn);
    btnPanel.add(clearBtn);

    formPanel.add(btnPanel, gbc);

    formPanel.revalidate();
    formPanel.repaint();
}

// Helper — adds a label row then a field row
private void addFormRow(JPanel panel, java.awt.GridBagConstraints gbc,
                         int gridY, String labelText, JTextField field) {
    gbc.gridy = gridY;
    gbc.insets = new java.awt.Insets(6, 0, 2, 0);
    JLabel lbl = new JLabel(labelText);
    lbl.setFont(UIUtils.FONT_LABEL);
    lbl.setForeground(UIUtils.TEXT_GRAY);
    panel.add(lbl, gbc);

    gbc.gridy = gridY + 1;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    field.setPreferredSize(new Dimension(0, 36));
    UIUtils.styleField(field);
    panel.add(field, gbc);
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
        headerPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        formPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtPlate = new javax.swing.JTextField();
        txtBrand = new javax.swing.JTextField();
        txtCapacity = new javax.swing.JTextField();
        txtFuel = new javax.swing.JTextField();
        txtMileage = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox<>();
        saveBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        tablePanel = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jscrollpane = new javax.swing.JScrollPane();
        busTable = new javax.swing.JTable();

        jTextField6.setText("jTextField6");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Bus Management");

        headerPanel.setBackground(UIUtils.TEAL);

        lblTitle.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setText("Bus Management");

        btnBack.setBackground(new java.awt.Color(0, 77, 77));
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBack, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, headerPanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(lblTitle)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        formPanel.setBackground(new java.awt.Color(255, 255, 255));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIUtils.BORDER, 1, true),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        formPanel.setPreferredSize(new java.awt.Dimension(330, 650));

        jLabel1.setText("Plate Number");

        jLabel2.setText("Brand");

        jLabel3.setText("Capacity");

        jLabel4.setText("Fuel Type");

        jLabel5.setText("Mileage(Km)");

        jLabel6.setText("Status");

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        saveBtn.setText("SAVE");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        updateBtn.setText("Update");
        updateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBtnActionPerformed(evt);
            }
        });

        clearBtn.setText("Clear");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout formPanelLayout = new javax.swing.GroupLayout(formPanel);
        formPanel.setLayout(formPanelLayout);
        formPanelLayout.setHorizontalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtMileage, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                        .addComponent(txtFuel)
                        .addComponent(txtCapacity)
                        .addComponent(txtPlate)
                        .addComponent(txtBrand)))
                .addGap(19, 19, 19))
            .addGroup(formPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearBtn)
                .addContainerGap(13, Short.MAX_VALUE))
        );
        formPanelLayout.setVerticalGroup(
            formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(txtPlate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(txtBrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(txtCapacity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(txtFuel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(55, 55, 55)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtMileage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(formPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn)
                    .addComponent(clearBtn))
                .addContainerGap(139, Short.MAX_VALUE))
        );

        tablePanel.setBackground(new java.awt.Color(245, 247, 250));
        tablePanel.setToolTipText("");

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        busTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jscrollpane.setViewportView(busTable);

        javax.swing.GroupLayout tablePanelLayout = new javax.swing.GroupLayout(tablePanel);
        tablePanel.setLayout(tablePanelLayout);
        tablePanelLayout.setHorizontalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tablePanelLayout.createSequentialGroup()
                .addContainerGap(213, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(133, 133, 133))
            .addComponent(jscrollpane)
        );
        tablePanelLayout.setVerticalGroup(
            tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tablePanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(tablePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jscrollpane, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(headerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(formPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        if (!validateForm()) return;
    try {
        com.urbanBus.server.model.Bus bus =
            new com.urbanBus.server.model.Bus();
        bus.setPlateNumber(txtPlate.getText()
            .trim().toUpperCase());
        bus.setBrand(txtBrand.getText().trim());
        bus.setCapacity(Integer.parseInt(
            txtCapacity.getText().trim()));
        bus.setFuelType(txtFuel.getText().trim());
        bus.setMileage(Double.parseDouble(
            txtMileage.getText().trim()));
        bus.setStatus(
            (com.urbanBus.server.model.BusStatus)
            cmbStatus.getSelectedItem());
        busService.saveBus(bus);
        JOptionPane.showMessageDialog(this,
            "Bus saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadBuses();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (selectedBus == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a bus from the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!validateForm()) return;
    try {
        selectedBus.setPlateNumber(txtPlate.getText()
            .trim().toUpperCase());
        selectedBus.setBrand(txtBrand.getText().trim());
        selectedBus.setCapacity(Integer.parseInt(
            txtCapacity.getText().trim()));
        selectedBus.setFuelType(
            txtFuel.getText().trim());
        selectedBus.setMileage(Double.parseDouble(
            txtMileage.getText().trim()));
        selectedBus.setStatus(
            (com.urbanBus.server.model.BusStatus)
            cmbStatus.getSelectedItem());
        busService.updateBus(selectedBus);
        JOptionPane.showMessageDialog(this,
            "Bus updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadBuses();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (selectedBus == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a bus from the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete bus: "
        + selectedBus.getPlateNumber() + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            busService.deleteBus(selectedBus);
            JOptionPane.showMessageDialog(this,
                "Bus deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadBuses();
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
    String query = txtSearch.getText().trim();
    
   
    if (query.equals("Search by plate number...") || query.isEmpty()) {
        loadBuses();   
        return;
    }
        
    String keyword = txtSearch.getText().trim();
    if (keyword.isEmpty()) {
        loadBuses();
        return;
    }
    try {
        tableModel.setRowCount(0);
        java.util.List<com.urbanBus.server.model.Bus>
            buses = busService.findAllBuses();
        for (com.urbanBus.server.model.Bus bus : buses) {
            if (bus.getPlateNumber().toLowerCase()
                    .contains(keyword.toLowerCase())
                || bus.getBrand().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    bus.getBusId(),
                    bus.getPlateNumber(),
                    bus.getBrand(),
                    bus.getCapacity(),
                    bus.getFuelType(),
                    bus.getMileage(),
                    bus.getStatus()
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new AdminDashboard(currentUser).setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadBuses();
    }//GEN-LAST:event_btnRefreshActionPerformed

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
            java.util.logging.Logger.getLogger(BusManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BusManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BusManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BusManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
        com.urbanBus.server.model.User testUser =
            new com.urbanBus.server.model.User();
        testUser.setFullName("Test User");
        testUser.setRole(
            com.urbanBus.server.model.UserRole.ADMIN);
        new BusManagementForm(testUser).setVisible(true);
    }
});    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JTable busTable;
    private javax.swing.JButton clearBtn;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JScrollPane jscrollpane;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JButton saveBtn;
    private javax.swing.JPanel tablePanel;
    private javax.swing.JTextField txtBrand;
    private javax.swing.JTextField txtCapacity;
    private javax.swing.JTextField txtFuel;
    private javax.swing.JTextField txtMileage;
    private javax.swing.JTextField txtPlate;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
