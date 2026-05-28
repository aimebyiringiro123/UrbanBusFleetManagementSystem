/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.Bus;
import com.urbanBus.server.model.Driver;
import com.urbanBus.server.model.DriverStatus;
import com.urbanBus.server.model.Route;
import com.urbanBus.server.model.Trip;
import com.urbanBus.server.model.TripStatus;
import com.urbanBus.server.model.User;
import com.urbanBus.server.model.UserRole;
import com.urbanBus.server.service.BusService;
import com.urbanBus.server.service.DriverService;
import com.urbanBus.server.service.RouteService;
import com.urbanBus.server.service.TripService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aimeb
 */
public class TripManagementForm extends javax.swing.JFrame {
    
    private User currentUser;
    private TripService tripService;
    private BusService busService;
    private DriverService driverService;
    private RouteService routeService;
    private Trip selectedTrip;
    private DefaultTableModel tableModel;
    private List<Bus> busList;
    private List<Driver> driverList;
    private List<Route> routeList;
    
    

    /**
     * Creates new form TripManagementForm
     */
    public TripManagementForm(User user) {
    this.currentUser = user;
    try {
        this.tripService =
            RMIClient.getTripService();
        this.busService =
            RMIClient.getBusService();
        this.driverService =
            RMIClient.getDriverService();
        this.routeService =
            RMIClient.getRouteService();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Cannot connect to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    initComponents();
    getContentPane().setBackground(UIUtils.BG_PAGE);
    UIUtils.styleHeaderButton(btnBack);
    UIUtils.styleField(txtSearch);
    UIUtils.setPlaceholder(txtSearch, "Search by route or driver...");
    UIUtils.stylePrimaryButton(btnSearch);
    UIUtils.styleOutlineButton(btnRefresh);
    btnSearch.setPreferredSize(new java.awt.Dimension(100, 36));
    btnRefresh.setPreferredSize(new java.awt.Dimension(100, 36));
    buildFormPanel();
    setLocationRelativeTo(null); 
    setupTable();
    loadComboBoxes(); 
    loadTrips();
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
    jPanel2.add(UIUtils.makeSectionLabel("TRIP DETAILS"), gbc);

    // Text fields
    addFormRow(jPanel2, gbc, 1, "Departure Time", txtDeparture);
    addFormRow(jPanel2, gbc, 3, "Arrival Time",   txtArrival);

    // Combo boxes
    addComboRow(jPanel2, gbc, 5,  "Route",  cmbRoute);
    addComboRow(jPanel2, gbc, 7,  "Bus",    cmbBus);
    addComboRow(jPanel2, gbc, 9,  "Driver", cmbDriver);
    addComboRow(jPanel2, gbc, 11, "Status", cmbStatus);

    // Spacer
    gbc.gridy = 13; gbc.weighty = 1.0;
    gbc.fill = java.awt.GridBagConstraints.VERTICAL;
    jPanel2.add(new javax.swing.JPanel() {{ setOpaque(false); }}, gbc);

    // Buttons
    gbc.gridy = 14; gbc.weighty = 0;
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

// Extra helper for combo rows — add this alongside addFormRow
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
        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel(DriverStatus.values()));    
    String[] columns = {
        "ID", "Route", "Bus",
        "Driver", "Departure", "Arrival", "Status"
    };
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(
                int row, int col) {
            return false;
        }
    };
    tripTable.setModel(tableModel);
    tripTable.setRowHeight(30);
    tripTable.getTableHeader().setBackground(
        new java.awt.Color(26, 62, 111));
    tripTable.getTableHeader().setForeground(
        java.awt.Color.WHITE);
    tripTable.setSelectionBackground(
        new java.awt.Color(210, 228, 255));

    // Hide ID column
    tripTable.getColumnModel().getColumn(0)
        .setMinWidth(0);
    tripTable.getColumnModel().getColumn(0)
        .setMaxWidth(0);

    // Load TripStatus into combobox
    cmbStatus.setModel(
        new javax.swing.DefaultComboBoxModel(
            TripStatus.values()));

    // Row click listener
    tripTable.addMouseListener(
            new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(
                java.awt.event.MouseEvent e) {
            int row = tripTable.getSelectedRow();
            if (row != -1) populateForm(row);
        }
    });
    
    UIUtils.styleTable(tripTable);
    UIUtils.styleScrollPane(jScrollPane1);
    tripTable.getColumnModel().getColumn(6).setCellRenderer(UIUtils.statusRenderer());
}
    
    
    
    
    
    private void loadComboBoxes() {
    try {
        // Load routes
        routeList = routeService.findAllRoutes();
        cmbRoute.removeAllItems();
        for (Route route : routeList) {
            cmbRoute.addItem(route.getRouteName());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    try {
        // Load active buses only
        busList = busService.findAllBuses();
        cmbBus.removeAllItems();
        for (Bus bus : busList) {
            cmbBus.addItem(bus.getPlateNumber()
                + " - " + bus.getBrand());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    try {
        // Load available drivers only
        driverList =
            driverService.findAllDrivers();
        cmbDriver.removeAllItems();
        for (Driver driver : driverList) {
            cmbDriver.addItem(
                driver.getFullName());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    private void loadTrips() {
    if (tripService == null) {
        JOptionPane.showMessageDialog(this,
            "Not connected to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Trip> trips =
            tripService.findAllTrips();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Trip trip : trips) {
            tableModel.addRow(new Object[]{
                trip.getTripId(),
                trip.getRoute().getRouteName(),
                trip.getBus().getPlateNumber(),
                trip.getDriver().getFullName(),
                sdf.format(trip.getDepartureTime()),
                trip.getArrivalTime() != null ?
                    sdf.format(trip.getArrivalTime())
                    : "—",
                trip.getStatus()
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading trips: "
            + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    private void populateForm(int row) {
    try {
        Long id = (Long) tableModel.getValueAt(row, 0);
        selectedTrip = tripService.findTripById(id);

        // Select route in combobox
        for (int i = 0; i < routeList.size(); i++) {
            if (routeList.get(i).getRouteId()
                    .equals(selectedTrip.getRoute()
                        .getRouteId())) {
                cmbRoute.setSelectedIndex(i);
                break;
            }
        }

        // Select bus in combobox
        for (int i = 0; i < busList.size(); i++) {
            if (busList.get(i).getBusId()
                    .equals(selectedTrip.getBus()
                        .getBusId())) {
                cmbBus.setSelectedIndex(i);
                break;
            }
        }

        // Select driver in combobox
        for (int i = 0; i < driverList.size(); i++) {
            if (driverList.get(i).getDriverId()
                    .equals(selectedTrip.getDriver()
                        .getDriverId())) {
                cmbDriver.setSelectedIndex(i);
                break;
            }
        }

        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        txtDeparture.setText(sdf.format(
            selectedTrip.getDepartureTime()));
        if (selectedTrip.getArrivalTime() != null) {
            txtArrival.setText(sdf.format(
                selectedTrip.getArrivalTime()));
        }
        cmbStatus.setSelectedItem(
            selectedTrip.getStatus());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
    private boolean validateForm() {
    if (cmbRoute.getSelectedIndex() == -1) {
        showError("Please select a route.");
        return false;
    }
    if (cmbBus.getSelectedIndex() == -1) {
        showError("Please select a bus.");
        return false;
    }
    if (cmbDriver.getSelectedIndex() == -1) {
        showError("Please select a driver.");
        return false;
    }
    if (txtDeparture.getText().trim().isEmpty()) {
        showError("Departure time is required.");
        txtDeparture.requestFocus();
        return false;
    }
    try {
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        sdf.setLenient(false);
        Date departure = sdf.parse(
            txtDeparture.getText().trim());
        if (departure.before(new Date())) {
            showError(
                "Departure time must be "
                + "in the future.");
            txtDeparture.requestFocus();
            return false;
        }
    } catch (Exception e) {
        showError(
            "Departure time format must be: "
            + "yyyy-MM-dd HH:mm");
        txtDeparture.requestFocus();
        return false;
    }
    return true;
}
    
    private void clearForm() {
    if (cmbRoute.getItemCount() > 0)
        cmbRoute.setSelectedIndex(0);
    if (cmbBus.getItemCount() > 0)
        cmbBus.setSelectedIndex(0);
    if (cmbDriver.getItemCount() > 0)
        cmbDriver.setSelectedIndex(0);
    txtDeparture.setText("");
    txtArrival.setText("");
    cmbStatus.setSelectedIndex(0);
    selectedTrip = null;
    tripTable.clearSelection();
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

        jFileChooser1 = new javax.swing.JFileChooser();
        jTextField3 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
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
        cmbRoute = new javax.swing.JComboBox<>();
        cmbBus = new javax.swing.JComboBox<>();
        cmbDriver = new javax.swing.JComboBox<>();
        cmbStatus = new javax.swing.JComboBox<>();
        txtDeparture = new javax.swing.JTextField();
        txtArrival = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tripTable = new javax.swing.JTable();

        jTextField3.setText("jTextField3");

        jButton5.setText("jButton5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        lblTitle.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        lblTitle.setText("Trip Scheduling");

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
                .addGap(21, 21, 21)
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
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

        jLabel1.setText("Route");

        jLabel2.setText("Bus");

        jLabel3.setText("Driver");

        jLabel4.setText("Departure Time");

        jLabel5.setText("Arrival Time");

        jLabel6.setText("Status");

        cmbRoute.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbBus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbDriver.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        saveBtn.setText("save");
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
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(74, 74, 74)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDeparture)
                            .addComponent(txtArrival)
                            .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbRoute, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbBus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbDriver, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(saveBtn)
                        .addGap(27, 27, 27)
                        .addComponent(updateBtn)
                        .addGap(18, 18, 18)
                        .addComponent(deleteBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(clearBtn)))
                .addGap(49, 49, 49))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(cmbRoute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(cmbBus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)
                                .addComponent(jLabel3))
                            .addComponent(cmbDriver, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(43, 43, 43)
                        .addComponent(jLabel4))
                    .addComponent(txtDeparture, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtArrival, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn)
                    .addComponent(clearBtn))
                .addContainerGap(87, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(245, 247, 250));

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

        tripTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tripTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefresh)
                .addGap(29, 29, 29))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 668, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 330, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
         if (!validateForm()) return;
    try {
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Trip trip = new Trip();
        trip.setRoute(routeList.get(
            cmbRoute.getSelectedIndex()));
        trip.setBus(busList.get(
            cmbBus.getSelectedIndex()));
        trip.setDriver(driverList.get(
            cmbDriver.getSelectedIndex()));
        trip.setDepartureTime(sdf.parse(
            txtDeparture.getText().trim()));
        if (!txtArrival.getText().trim().isEmpty()) {
            trip.setArrivalTime(sdf.parse(
                txtArrival.getText().trim()));
        }
        trip.setStatus((TripStatus)
            cmbStatus.getSelectedItem());

        tripService.saveTrip(trip);
        JOptionPane.showMessageDialog(this,
            "Trip scheduled successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadTrips();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
         if (selectedTrip == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a trip from the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!validateForm()) return;
    try {
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

        selectedTrip.setRoute(routeList.get(
            cmbRoute.getSelectedIndex()));
        selectedTrip.setBus(busList.get(
            cmbBus.getSelectedIndex()));
        selectedTrip.setDriver(driverList.get(
            cmbDriver.getSelectedIndex()));
        selectedTrip.setDepartureTime(sdf.parse(
            txtDeparture.getText().trim()));
        if (!txtArrival.getText().trim().isEmpty()) {
            selectedTrip.setArrivalTime(sdf.parse(
                txtArrival.getText().trim()));
        }
        selectedTrip.setStatus((TripStatus)
            cmbStatus.getSelectedItem());

        tripService.updateTrip(selectedTrip);
        JOptionPane.showMessageDialog(this,
            "Trip updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadTrips();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
         if (selectedTrip == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a trip from the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete "
        + "this trip?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            tripService.deleteTrip(selectedTrip);
            JOptionPane.showMessageDialog(this,
                "Trip deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadTrips();
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
        loadTrips();
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Trip> trips =
            tripService.findAllTrips();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Trip trip : trips) {
            if (trip.getRoute().getRouteName()
                    .toLowerCase().contains(
                        keyword.toLowerCase())
                || trip.getBus().getPlateNumber()
                    .toLowerCase().contains(
                        keyword.toLowerCase())
                || trip.getDriver().getFullName()
                    .toLowerCase().contains(
                        keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    trip.getTripId(),
                    trip.getRoute().getRouteName(),
                    trip.getBus().getPlateNumber(),
                    trip.getDriver().getFullName(),
                    sdf.format(
                        trip.getDepartureTime()),
                    trip.getArrivalTime() != null ?
                        sdf.format(
                            trip.getArrivalTime())
                        : "—",
                    trip.getStatus()
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadTrips();
    }//GEN-LAST:event_btnRefreshActionPerformed

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
            java.util.logging.Logger.getLogger(TripManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TripManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TripManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TripManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setRole(UserRole.ADMIN);
                new TripManagementForm(testUser)
                .setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton clearBtn;
    private javax.swing.JComboBox<String> cmbBus;
    private javax.swing.JComboBox<String> cmbDriver;
    private javax.swing.JComboBox<String> cmbRoute;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTable tripTable;
    private javax.swing.JTextField txtArrival;
    private javax.swing.JTextField txtDeparture;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
