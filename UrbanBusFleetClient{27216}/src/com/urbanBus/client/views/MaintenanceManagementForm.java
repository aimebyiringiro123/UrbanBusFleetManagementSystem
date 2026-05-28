/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.Bus;
import com.urbanBus.server.model.Maintenance;
import com.urbanBus.server.model.User;
import com.urbanBus.server.model.UserRole;
import com.urbanBus.server.service.BusService;
import com.urbanBus.server.service.MaintenanceService;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aimeb
 */
public class MaintenanceManagementForm extends javax.swing.JFrame {
    
    private User currentUser;
    private MaintenanceService maintenanceService;
    private BusService busService;
    private Maintenance selectedMaintenance;
    private DefaultTableModel tableModel;
    private List<Bus> busList;

    /**
     * Creates new form MaintenanceManagementForm
     */
    public MaintenanceManagementForm(User user) {
    this.currentUser = user;
    try {
        this.maintenanceService =
            RMIClient.getMaintenanceService();
        this.busService =
            RMIClient.getBusService();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Cannot connect to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    initComponents();
    getContentPane().setBackground(UIUtils.BG_PAGE);
    UIUtils.styleHeaderButton(btnBack);
    UIUtils.styleField(txtSearch);
    UIUtils.setPlaceholder(txtSearch, "Search by bus or technician...");
    UIUtils.stylePrimaryButton(btnSearch); 
    UIUtils.styleOutlineButton(btnRefresh);
    btnSearch.setPreferredSize(new java.awt.Dimension(100, 36));
    btnRefresh.setPreferredSize(new java.awt.Dimension(100, 36));
    buildFormPanel();
    setLocationRelativeTo(null); 
    setupTable();
    loadComboBoxes(); 
    loadMaintenance();
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
    jPanel2.add(UIUtils.makeSectionLabel("MAINTENANCE DETAILS"), gbc);

    addComboRow(jPanel2, gbc, 1,  "Bus",                cmbBus);
    addFormRow(jPanel2,  gbc, 3,  "Service Date",       txtServiceDate);
    addFormRow(jPanel2,  gbc, 5,  "Service Type",       txtServiceType);
    addFormRow(jPanel2,  gbc, 7,  "Cost (RWF)",         txtCost);
    addFormRow(jPanel2,  gbc, 9,  "Technician",         txtTechnician);
    addFormRow(jPanel2,  gbc, 11, "Next Service (km)",  txtNextService);
    addFormRow(jPanel2,  gbc, 13, "Notes",              txtNotes);

    // ✅ Completed checkbox — re-added here
    gbc.gridy = 15;
    gbc.insets = new java.awt.Insets(10, 0, 16, 0);
    chkCompleted.setText("Completed");
    chkCompleted.setFont(UIUtils.FONT_LABEL);
    chkCompleted.setForeground(UIUtils.TEXT_DARK);
    chkCompleted.setBackground(java.awt.Color.WHITE);
    chkCompleted.setFocusPainted(false);
    chkCompleted.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    jPanel2.add(chkCompleted, gbc);

    // Spacer
    gbc.gridy = 16; gbc.weighty = 1.0;
    gbc.fill = java.awt.GridBagConstraints.VERTICAL;
    jPanel2.add(new javax.swing.JPanel() {{ setOpaque(false); }}, gbc);

    // Buttons
    gbc.gridy = 17; gbc.weighty = 0;
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

    jPanel2.revalidate();
    jPanel2.repaint();
}
    
    
    
    private void addFormRow(javax.swing.JPanel panel,
        java.awt.GridBagConstraints gbc, int y,
        String label, javax.swing.JTextField field)
    {
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
    
    private void addComboRow(javax.swing.JPanel panel,
        java.awt.GridBagConstraints gbc, int y,
        String label, javax.swing.JComboBox combo) {
    gbc.gridy = y;
    gbc.insets = new java.awt.Insets(6, 0, 2, 0);
    javax.swing.JLabel lbl = new javax.swing.JLabel(label);
    lbl.setFont(UIUtils.FONT_LABEL);
    lbl.setForeground(UIUtils.TEXT_GRAY);
    panel.add(lbl, gbc);
    gbc.gridy = y + 1;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    combo.setPreferredSize(new java.awt.Dimension(0, 36));
    UIUtils.styleCombo(combo);
    panel.add(combo, gbc);
}
    private void setupTable() {
    String[] columns = {
        "ID", "Bus", "Service Type",
        "Service Date", "Cost",
        "Technician", "Completed"
    };
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(
                int row, int col) {
            return false;
        }
    };
    maintenanceTable.setModel(tableModel);
    maintenanceTable.setRowHeight(30);
    maintenanceTable.getTableHeader().setBackground(
        new java.awt.Color(26, 62, 111));
    maintenanceTable.getTableHeader().setForeground(
        java.awt.Color.WHITE);
    maintenanceTable.setSelectionBackground(
        new java.awt.Color(210, 228, 255));

    // Hide ID column
    maintenanceTable.getColumnModel().getColumn(0)
        .setMinWidth(0);
    maintenanceTable.getColumnModel().getColumn(0)
        .setMaxWidth(0);

    // Row click listener
    maintenanceTable.addMouseListener(
            new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(
                java.awt.event.MouseEvent e) {
            int row =
                maintenanceTable.getSelectedRow();
            if (row != -1) populateForm(row);
        }
    });
    
    UIUtils.styleTable(maintenanceTable);
    UIUtils.styleScrollPane(jScrollPane1);
   // No status column — skip statusRenderer
}
    
    
    
    
    
    
    private void loadComboBoxes() {
    try {
        busList = busService.findAllBuses();
        cmbBus.removeAllItems();
        for (Bus bus : busList) {
            cmbBus.addItem(bus.getPlateNumber()
                + " - " + bus.getBrand());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
    private void loadMaintenance() {
    if (maintenanceService == null) {
        JOptionPane.showMessageDialog(this,
            "Not connected to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Maintenance> list =
            maintenanceService.findAllMaintenance();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");
        for (Maintenance m : list) {
            tableModel.addRow(new Object[]{
                m.getMaintenanceId(),
                m.getBus().getPlateNumber(),
                m.getServiceType(),
                sdf.format(m.getServiceDate()),
                m.getCost(),
                m.getTechnicianName(),
                m.isCompleted() ? "Yes" : "No"
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading maintenance: "
            + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    
    
    private void populateForm(int row) {
    try {
        Long id = (Long) tableModel.getValueAt(row, 0);
        selectedMaintenance =
            maintenanceService
                .findMaintenanceById(id);

        for (int i = 0; i < busList.size(); i++) {
            if (busList.get(i).getBusId()
                    .equals(selectedMaintenance
                        .getBus().getBusId())) {
                cmbBus.setSelectedIndex(i);
                break;
            }
        }

        txtServiceType.setText(
            selectedMaintenance.getServiceType());
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");
        txtServiceDate.setText(sdf.format(
            selectedMaintenance.getServiceDate()));
        txtCost.setText(String.valueOf(
            selectedMaintenance.getCost()));
        txtTechnician.setText(
            selectedMaintenance.getTechnicianName());
        txtNextService.setText(String.valueOf(
            selectedMaintenance
                .getNextServiceMileage()));
        txtNotes.setText(
            selectedMaintenance.getNotes());
        chkCompleted.setSelected(
            selectedMaintenance.isCompleted());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
    private boolean validateForm() {
    if (cmbBus.getSelectedIndex() == -1) {
        showError("Please select a bus.");
        return false;
    }
    if (txtServiceType.getText().trim().isEmpty()) {
        showError("Service type is required.");
        txtServiceType.requestFocus();
        return false;
    }
    if (txtServiceDate.getText().trim().isEmpty()) {
        showError("Service date is required.");
        txtServiceDate.requestFocus();
        return false;
    }
    try {
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        sdf.parse(txtServiceDate.getText().trim());
    } catch (Exception e) {
        showError(
            "Service date format must be: "
            + "yyyy-MM-dd");
        txtServiceDate.requestFocus();
        return false;
    }
    if (txtCost.getText().trim().isEmpty()) {
        showError("Cost is required.");
        txtCost.requestFocus();
        return false;
    }
    try {
        double cost = Double.parseDouble(
            txtCost.getText().trim());
        if (cost <= 0) {
            showError(
                "Cost must be greater than zero.");
            txtCost.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        showError("Cost must be a valid number.");
        txtCost.requestFocus();
        return false;
    }
    if (txtTechnician.getText().trim().isEmpty()) {
        showError("Technician name is required.");
        txtTechnician.requestFocus();
        return false;
    }
    return true;
}
    
    
    
    
    private void clearForm() {
    if (cmbBus.getItemCount() > 0)
        cmbBus.setSelectedIndex(0);
    txtServiceType.setText("");
    txtServiceDate.setText("");
    txtCost.setText("");
    txtTechnician.setText("");
    txtNextService.setText("");
    txtNotes.setText("");
    chkCompleted.setSelected(false);
    selectedMaintenance = null;
    maintenanceTable.clearSelection();
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

        jComboBox7 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        cmbBus = new javax.swing.JComboBox<>();
        txtServiceType = new javax.swing.JTextField();
        txtServiceDate = new javax.swing.JTextField();
        txtCost = new javax.swing.JTextField();
        txtTechnician = new javax.swing.JTextField();
        txtNextService = new javax.swing.JTextField();
        txtNotes = new javax.swing.JTextField();
        chkCompleted = new javax.swing.JCheckBox();
        saveBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        maintenanceTable = new javax.swing.JTable();

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTextField2.setText("jTextField2");

        jTextField7.setText("jTextField7");

        jButton5.setText("jButton5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        lblTitle.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTitle.setText("Maintenance Management");

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
                .addGap(27, 27, 27)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 648, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(32, 32, 32))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnBack))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Bus");

        jLabel3.setText("Service Type");

        jLabel4.setText("Service Date");

        jLabel5.setText("Cost");

        jLabel6.setText("Technician");

        jLabel7.setText("Next service KM");

        jLabel8.setText("Notes");

        jLabel9.setText("Completed");

        cmbBus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtServiceDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtServiceDateActionPerformed(evt);
            }
        });

        txtNotes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNotesActionPerformed(evt);
            }
        });

        chkCompleted.setText("Completed");

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
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(cmbBus, 0, 165, Short.MAX_VALUE)
                                .addComponent(txtServiceType)
                                .addComponent(txtServiceDate)
                                .addComponent(txtCost)
                                .addComponent(txtTechnician)
                                .addComponent(txtNextService)
                                .addComponent(txtNotes))
                            .addComponent(chkCompleted, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(saveBtn)
                        .addGap(18, 18, 18)
                        .addComponent(updateBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(deleteBtn)
                        .addGap(18, 18, 18)
                        .addComponent(clearBtn)
                        .addGap(19, 19, 19))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(jLabel2)
                                                    .addComponent(cmbBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(31, 31, 31)
                                                .addComponent(jLabel3))
                                            .addComponent(txtServiceType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel4)
                                            .addComponent(txtServiceDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(27, 27, 27)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel5)
                                            .addComponent(txtCost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(31, 31, 31)
                                        .addComponent(jLabel6))
                                    .addComponent(txtTechnician, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(34, 34, 34)
                                .addComponent(jLabel7))
                            .addComponent(txtNextService, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(36, 36, 36)
                        .addComponent(jLabel8))
                    .addComponent(txtNotes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(chkCompleted))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn)
                    .addComponent(clearBtn))
                .addContainerGap(175, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(245, 247, 250));

        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        btnSearch.setText("search");
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

        maintenanceTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(maintenanceTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnSearch)
                .addGap(18, 18, 18)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtServiceDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtServiceDateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtServiceDateActionPerformed

    private void txtNotesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNotesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNotesActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadMaintenance();

    }//GEN-LAST:event_btnRefreshActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
         if (!validateForm()) return;
    try {
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");

        Maintenance maintenance = new Maintenance();
        maintenance.setBus(busList.get(
            cmbBus.getSelectedIndex()));
        maintenance.setServiceType(
            txtServiceType.getText().trim());
        maintenance.setServiceDate(
            sdf.parse(
                txtServiceDate.getText().trim()));
        maintenance.setCost(Double.parseDouble(
            txtCost.getText().trim()));
        maintenance.setTechnicianName(
            txtTechnician.getText().trim());
        if (!txtNextService.getText()
                .trim().isEmpty()) {
            maintenance.setNextServiceMileage(
                Double.parseDouble(
                    txtNextService.getText()
                        .trim()));
        }
        maintenance.setNotes(
            txtNotes.getText().trim());
        maintenance.setCompleted(
            chkCompleted.isSelected());

        maintenanceService
            .saveMaintenance(maintenance);
        JOptionPane.showMessageDialog(this,
            "Maintenance record saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadMaintenance();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        if (selectedMaintenance == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a record from "
            + "the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete "
        + "this maintenance record?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            maintenanceService.deleteMaintenance(
                selectedMaintenance);
            JOptionPane.showMessageDialog(this,
                "Record deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadMaintenance();
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

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new AdminDashboard(currentUser).setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String keyword = txtSearch.getText().trim();
    if (keyword.isEmpty()) {
        loadMaintenance();
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Maintenance> list =
            maintenanceService.findAllMaintenance();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");
        for (Maintenance m : list) {
            if (m.getBus().getPlateNumber()
                    .toLowerCase().contains(
                        keyword.toLowerCase())
                || m.getServiceType().toLowerCase()
                    .contains(
                        keyword.toLowerCase())
                || m.getTechnicianName()
                    .toLowerCase().contains(
                        keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    m.getMaintenanceId(),
                    m.getBus().getPlateNumber(),
                    m.getServiceType(),
                    sdf.format(m.getServiceDate()),
                    m.getCost(),
                    m.getTechnicianName(),
                    m.isCompleted() ? "Yes" : "No"
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (selectedMaintenance == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a record from "
            + "the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!validateForm()) return;
    try {
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd");

        selectedMaintenance.setBus(busList.get(
            cmbBus.getSelectedIndex()));
        selectedMaintenance.setServiceType(
            txtServiceType.getText().trim());
        selectedMaintenance.setServiceDate(
            sdf.parse(
                txtServiceDate.getText().trim()));
        selectedMaintenance.setCost(
            Double.parseDouble(
                txtCost.getText().trim()));
        selectedMaintenance.setTechnicianName(
            txtTechnician.getText().trim());
        if (!txtNextService.getText()
                .trim().isEmpty()) {
            selectedMaintenance
                .setNextServiceMileage(
                    Double.parseDouble(
                        txtNextService.getText()
                            .trim()));
        }
        selectedMaintenance.setNotes(
            txtNotes.getText().trim());
        selectedMaintenance.setCompleted(
            chkCompleted.isSelected());

        maintenanceService.updateMaintenance(
            selectedMaintenance);
        JOptionPane.showMessageDialog(this,
            "Maintenance record updated!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadMaintenance();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setRole(UserRole.ADMIN);
                new MaintenanceManagementForm(testUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JCheckBox chkCompleted;
    private javax.swing.JButton clearBtn;
    private javax.swing.JComboBox<String> cmbBus;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox7;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable maintenanceTable;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtCost;
    private javax.swing.JTextField txtNextService;
    private javax.swing.JTextField txtNotes;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtServiceDate;
    private javax.swing.JTextField txtServiceType;
    private javax.swing.JTextField txtTechnician;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
