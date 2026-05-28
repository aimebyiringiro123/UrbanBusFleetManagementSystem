/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.Route;
import com.urbanBus.server.model.RouteStatus;
import com.urbanBus.server.model.User;
import com.urbanBus.server.service.RouteService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import com.urbanBus.server.model.UserRole;
/**
 *
 * @author aimeb
 */
public class RouteManagementForm extends javax.swing.JFrame {
    private User currentUser;
    private RouteService routeService;
    private Route selectedRoute;
    private DefaultTableModel tableModel; 
    

    /**
     * Creates new form RouteManagementForm
     */
   public RouteManagementForm(User user) {
    this.currentUser = user;
    try {
        this.routeService =
            RMIClient.getRouteService();
        if (this.routeService == null) {
            JOptionPane.showMessageDialog(null,
                "Route service not found on server.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Connection error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    initComponents();
    getContentPane().setBackground(UIUtils.BG_PAGE);
    UIUtils.styleHeaderButton(btnBack);
    UIUtils.styleField(txtSearch);
    UIUtils.setPlaceholder(txtSearch, "Search by route name...");
    UIUtils.stylePrimaryButton(btnSearch);
    UIUtils.styleOutlineButton(btnRefresh);
    btnSearch.setPreferredSize(new java.awt.Dimension(100, 36));
    btnRefresh.setPreferredSize(new java.awt.Dimension(100, 36));
    buildFormPanel();
    setLocationRelativeTo(null); 
    setupTable();
    loadRoutes();
}
    
    private void setupTable() {
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(RouteStatus.values()));
    String[] columns = {
        "ID", "Route Name", "Start Point",
        "End Point", "Distance", "Est. Time",
        "Fare", "Status"
    };
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(
                int row, int col) {
            return false;
        }
    };
    routeTable.setModel(tableModel);
    routeTable.setRowHeight(30);
    routeTable.getTableHeader().setBackground(
        new java.awt.Color(26, 62, 111));
    routeTable.getTableHeader().setForeground(
        java.awt.Color.WHITE);
    routeTable.setSelectionBackground(
        new java.awt.Color(210, 228, 255));

    // Hide ID column
    routeTable.getColumnModel().getColumn(0)
        .setMinWidth(0);
    routeTable.getColumnModel().getColumn(0)
        .setMaxWidth(0);

    // Load RouteStatus into combobox
    cmbStatus.setModel(
        new javax.swing.DefaultComboBoxModel(
            RouteStatus.values()));

    // Row click listener
    routeTable.addMouseListener(
            new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(
                java.awt.event.MouseEvent e) {
            int row = routeTable.getSelectedRow();
            if (row != -1) populateForm(row);
        }
    });
    
    UIUtils.styleTable(routeTable);
    UIUtils.styleScrollPane(jScrollPane1);
    routeTable.getColumnModel().getColumn(7).setCellRenderer(UIUtils.statusRenderer());
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

    gbc.gridy = 0;
    gbc.insets = new java.awt.Insets(0, 0, 14, 0);
    jPanel2.add(UIUtils.makeSectionLabel("ROUTE DETAILS"), gbc);

    addFormRow(jPanel2, gbc, 1, "Route Name",       txtRouteName);
    addFormRow(jPanel2, gbc, 3, "Start Point",      txtStartPoint);
    addFormRow(jPanel2, gbc, 5, "End Point",        txtEndPoint);
    addFormRow(jPanel2, gbc, 7, "Distance (km)",    txtDistance);
    addFormRow(jPanel2, gbc, 9, "Est. Time (min)",  txtEstTime);
    addFormRow(jPanel2, gbc, 11, "Fare (RWF)",      txtFare);

    // Status combo
    gbc.gridy = 13;
    gbc.insets = new java.awt.Insets(6, 0, 2, 0);
    javax.swing.JLabel lbl = new javax.swing.JLabel("Status");
    lbl.setFont(UIUtils.FONT_LABEL); lbl.setForeground(UIUtils.TEXT_GRAY);
    jPanel2.add(lbl, gbc);
    gbc.gridy = 14; gbc.insets = new java.awt.Insets(0, 0, 16, 0);
    cmbStatus.setPreferredSize(new java.awt.Dimension(0, 36));
    UIUtils.styleCombo(cmbStatus);
    jPanel2.add(cmbStatus, gbc);

    // Spacer
    gbc.gridy = 15; gbc.weighty = 1.0;
    gbc.fill = java.awt.GridBagConstraints.VERTICAL;
    jPanel2.add(new javax.swing.JPanel() {{ setOpaque(false); }}, gbc);

    // Buttons (this form uses btnSave/btnUpdate/btnDelete/btnClear)
    gbc.gridy = 16; gbc.weighty = 0;
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    javax.swing.JPanel btnPanel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 8, 8));
    btnPanel.setOpaque(false);
    java.awt.Dimension d = new java.awt.Dimension(0, 36);
    btnSave.setPreferredSize(d);   btnUpdate.setPreferredSize(d);
    btnDelete.setPreferredSize(d); btnClear.setPreferredSize(d);
    UIUtils.stylePrimaryButton(btnSave);
    UIUtils.styleOutlineButton(btnUpdate);
    UIUtils.styleDangerButton(btnDelete);
    UIUtils.styleOutlineButton(btnClear);
    btnPanel.add(btnSave);   btnPanel.add(btnUpdate);
    btnPanel.add(btnDelete); btnPanel.add(btnClear);
    jPanel2.add(btnPanel, gbc);
    jPanel2.revalidate(); jPanel2.repaint();
}
    
    
private void loadRoutes() {
    if (routeService == null) {
        JOptionPane.showMessageDialog(this,
            "Not connected to server.\n"
            + "Please restart the application.",
            "Connection Error",
            JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Route> routes =
            routeService.findAllRoutes();
        for (Route route : routes) {
            tableModel.addRow(new Object[]{
                route.getRouteId(),
                route.getRouteName(),
                route.getStartPoint(),
                route.getEndPoint(),
                route.getDistanceKm(),
                route.getEstimatedTime(),
                route.getFare(),
                route.getStatus()
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading routes: "
            + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    private void populateForm(int row) {
    try {
        Long id = (Long) tableModel.getValueAt(row, 0);
        selectedRoute =
            routeService.findRouteById(id);
        txtRouteName.setText(
            selectedRoute.getRouteName());
        txtStartPoint.setText(
            selectedRoute.getStartPoint());
        txtEndPoint.setText(
            selectedRoute.getEndPoint());
        txtDistance.setText(String.valueOf(
            selectedRoute.getDistanceKm()));
        txtEstTime.setText(String.valueOf(
            selectedRoute.getEstimatedTime()));
        txtFare.setText(String.valueOf(
            selectedRoute.getFare()));
        cmbStatus.setSelectedItem(
            selectedRoute.getStatus());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
    private boolean validateForm() {
    if (txtRouteName.getText().trim().isEmpty()) {
        showError("Route name is required.");
        txtRouteName.requestFocus();
        return false;
    }
    if (txtStartPoint.getText().trim().isEmpty()) {
        showError("Start point is required.");
        txtStartPoint.requestFocus();
        return false;
    }
    if (txtEndPoint.getText().trim().isEmpty()) {
        showError("End point is required.");
        txtEndPoint.requestFocus();
        return false;
    }
    if (txtDistance.getText().trim().isEmpty()) {
        showError("Distance is required.");
        txtDistance.requestFocus();
        return false;
    }
    try {
        double dist = Double.parseDouble(
            txtDistance.getText().trim());
        if (dist <= 0) {
            showError(
                "Distance must be greater than zero.");
            txtDistance.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        showError("Distance must be a valid number.");
        txtDistance.requestFocus();
        return false;
    }
    if (txtEstTime.getText().trim().isEmpty()) {
        showError("Estimated time is required.");
        txtEstTime.requestFocus();
        return false;
    }
    try {
        int time = Integer.parseInt(
            txtEstTime.getText().trim());
        if (time <= 0) {
            showError(
                "Estimated time must be greater "
                + "than zero.");
            txtEstTime.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        showError(
            "Estimated time must be a valid number.");
        txtEstTime.requestFocus();
        return false;
    }
    if (txtFare.getText().trim().isEmpty()) {
        showError("Fare is required.");
        txtFare.requestFocus();
        return false;
    }
    try {
        double fare = Double.parseDouble(
            txtFare.getText().trim());
        if (fare <= 0) {
            showError(
                "Fare must be greater than zero.");
            txtFare.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        showError("Fare must be a valid number.");
        txtFare.requestFocus();
        return false;
    }
    return true;
}
    
    private void clearForm() {
    txtRouteName.setText("");
    txtStartPoint.setText("");
    txtEndPoint.setText("");
    txtDistance.setText("");
    txtEstTime.setText("");
    txtFare.setText("");
    cmbStatus.setSelectedIndex(0);
    selectedRoute = null;
    routeTable.clearSelection();
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

        jLabel8 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtRouteName = new javax.swing.JTextField();
        txtStartPoint = new javax.swing.JTextField();
        txtEndPoint = new javax.swing.JTextField();
        txtDistance = new javax.swing.JTextField();
        txtEstTime = new javax.swing.JTextField();
        txtFare = new javax.swing.JTextField();
        cmbStatus = new javax.swing.JComboBox<>();
        btnSave = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        routeTable = new javax.swing.JTable();

        jLabel8.setText("jLabel8");

        jTextField7.setText("jTextField7");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        lblTitle.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Route Management");

        btnBack.setBackground(new java.awt.Color(0, 77, 77));
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("back");
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
                .addGap(23, 23, 23)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTitle)
                    .addComponent(btnBack))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setText("route name");

        jLabel2.setText("start point");

        jLabel3.setText("end point");

        jLabel4.setText("Distance");

        jLabel5.setText("EST time(min)");

        jLabel6.setText("Fare (RWF)");

        jLabel7.setText("Status");

        txtDistance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDistanceActionPerformed(evt);
            }
        });

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnUpdate.setText("update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnClear.setText("clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
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
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addGap(98, 98, 98)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRouteName)
                            .addComponent(txtStartPoint)
                            .addComponent(txtEndPoint)
                            .addComponent(txtDistance)
                            .addComponent(txtEstTime)
                            .addComponent(txtFare)
                            .addComponent(cmbStatus, 0, 125, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(btnUpdate)
                        .addGap(26, 26, 26)
                        .addComponent(btnDelete)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(jLabel1)
                                                            .addComponent(txtRouteName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGap(41, 41, 41)
                                                        .addComponent(jLabel2))
                                                    .addComponent(txtStartPoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(43, 43, 43)
                                                .addComponent(jLabel3))
                                            .addComponent(txtEndPoint, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(45, 45, 45)
                                        .addComponent(jLabel4))
                                    .addComponent(txtDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(43, 43, 43)
                                .addComponent(jLabel5))
                            .addComponent(txtEstTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49)
                        .addComponent(jLabel6))
                    .addComponent(txtFare, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnClear))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(245, 247, 250));

        btnSearch.setText("search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnRefresh.setText("refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        routeTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(routeTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 510, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new AdminDashboard(currentUser).setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void txtDistanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDistanceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDistanceActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
         if (!validateForm()) return;
    try {
        Route route = new Route();
        route.setRouteName(
            txtRouteName.getText().trim());
        route.setStartPoint(
            txtStartPoint.getText().trim());
        route.setEndPoint(
            txtEndPoint.getText().trim());
        route.setDistanceKm(Double.parseDouble(
            txtDistance.getText().trim()));
        route.setEstimatedTime(Integer.parseInt(
            txtEstTime.getText().trim()));
        route.setFare(Double.parseDouble(
            txtFare.getText().trim()));
        route.setStatus((RouteStatus)
            cmbStatus.getSelectedItem());

        routeService.saveRoute(route);
        JOptionPane.showMessageDialog(this,
            "Route saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadRoutes();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (selectedRoute == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a route from the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!validateForm()) return;
    try {
        selectedRoute.setRouteName(
            txtRouteName.getText().trim());
        selectedRoute.setStartPoint(
            txtStartPoint.getText().trim());
        selectedRoute.setEndPoint(
            txtEndPoint.getText().trim());
        selectedRoute.setDistanceKm(
            Double.parseDouble(
                txtDistance.getText().trim()));
        selectedRoute.setEstimatedTime(
            Integer.parseInt(
                txtEstTime.getText().trim()));
        selectedRoute.setFare(Double.parseDouble(
            txtFare.getText().trim()));
        selectedRoute.setStatus((RouteStatus)
            cmbStatus.getSelectedItem());

        routeService.updateRoute(selectedRoute);
        JOptionPane.showMessageDialog(this,
            "Route updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadRoutes();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        if (selectedRoute == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a route from the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete route: "
        + selectedRoute.getRouteName() + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            routeService.deleteRoute(selectedRoute);
            JOptionPane.showMessageDialog(this,
                "Route deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadRoutes();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String keyword = txtSearch.getText().trim();
    if (keyword.isEmpty()) {
        loadRoutes();
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Route> routes =
            routeService.findAllRoutes();
        for (Route route : routes) {
            if (route.getRouteName().toLowerCase()
                    .contains(keyword.toLowerCase())
                || route.getStartPoint().toLowerCase()
                    .contains(keyword.toLowerCase())
                || route.getEndPoint().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    route.getRouteId(),
                    route.getRouteName(),
                    route.getStartPoint(),
                    route.getEndPoint(),
                    route.getDistanceKm(),
                    route.getEstimatedTime(),
                    route.getFare(),
                    route.getStatus()
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadRoutes();
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
            java.util.logging.Logger.getLogger(RouteManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RouteManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RouteManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RouteManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setRole(UserRole.ADMIN);
                new RouteManagementForm(testUser)
                .setVisible(true);
    
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable routeTable;
    private javax.swing.JTextField txtDistance;
    private javax.swing.JTextField txtEndPoint;
    private javax.swing.JTextField txtEstTime;
    private javax.swing.JTextField txtFare;
    private javax.swing.JTextField txtRouteName;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStartPoint;
    // End of variables declaration//GEN-END:variables
}
