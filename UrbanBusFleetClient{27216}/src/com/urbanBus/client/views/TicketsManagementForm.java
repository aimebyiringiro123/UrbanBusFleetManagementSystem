/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.Passenger;
import com.urbanBus.server.model.Ticket;
import com.urbanBus.server.model.Trip;
import com.urbanBus.server.model.User;
import com.urbanBus.server.model.UserRole;
import com.urbanBus.server.service.PassengerService;
import com.urbanBus.server.service.TicketService;
import com.urbanBus.server.service.TripService;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author aimeb
 */
public class TicketsManagementForm extends javax.swing.JFrame {
    private User currentUser;
    private TicketService ticketService;
    private TripService tripService;
    private PassengerService passengerService;
    private Ticket selectedTicket;
    private DefaultTableModel tableModel;
    private List<Trip> tripList;
    private List<Passenger> passengerList;

    /**
     * Creates new form TicketsManagementForm
     */
    public TicketsManagementForm(User user) {
    this.currentUser = user;
    try {
        this.ticketService =
            RMIClient.getTicketService();
        this.tripService =
            RMIClient.getTripService();
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
    UIUtils.setPlaceholder(txtSearch, "Search by passenger or trip...");
    UIUtils.stylePrimaryButton(btnSearch);
    UIUtils.styleOutlineButton(btnRefresh);
    btnSearch.setPreferredSize(new java.awt.Dimension(100, 36));   
    btnRefresh.setPreferredSize(new java.awt.Dimension(100, 36));
    buildFormPanel();
    setLocationRelativeTo(null); 
    setupTable();
    loadComboBoxes(); 
    loadTickets();
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
    jPanel2.add(UIUtils.makeSectionLabel("TICKET DETAILS"), gbc);

    addComboRow(jPanel2, gbc, 1, "Trip",      cmbTrip);
    addComboRow(jPanel2, gbc, 3, "Passenger", cmbPassenger);
    addFormRow(jPanel2, gbc, 5, "Seat Number",  txtSeatNumber);
    addFormRow(jPanel2, gbc, 7, "Amount Paid",  txtAmount);

    // Spacer
    gbc.gridy = 9; gbc.weighty = 1.0;
    gbc.fill = java.awt.GridBagConstraints.VERTICAL;
    jPanel2.add(new javax.swing.JPanel() {{ setOpaque(false); }}, gbc);

    // Buttons
    gbc.gridy = 10; gbc.weighty = 0;
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
        "ID", "Trip Route", "Passenger",
        "Seat No", "Amount Paid", "Issued Date"
    };
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(
                int row, int col) {
            return false;
        }
    };
    ticketTable.setModel(tableModel);
    ticketTable.setRowHeight(30);
    ticketTable.getTableHeader().setBackground(
        new java.awt.Color(26, 62, 111));
    ticketTable.getTableHeader().setForeground(
        java.awt.Color.WHITE);
    ticketTable.setSelectionBackground(
        new java.awt.Color(210, 228, 255));

    // Hide ID column
    ticketTable.getColumnModel().getColumn(0)
        .setMinWidth(0);
    ticketTable.getColumnModel().getColumn(0)
        .setMaxWidth(0);

    // Row click listener
    ticketTable.addMouseListener(
            new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(
                java.awt.event.MouseEvent e) {
            int row = ticketTable.getSelectedRow();
            if (row != -1) populateForm(row);
        }
    });
    UIUtils.styleTable(ticketTable);
    UIUtils.styleScrollPane(jScrollPane1);
    // No status column — skip statusRenderer
}
    
    
    
    
    
    
    private void loadComboBoxes() {
    try {
        tripList = tripService.findAllTrips();
        cmbTrip.removeAllItems();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Trip trip : tripList) {
            cmbTrip.addItem(
                trip.getRoute().getRouteName()
                + " - "
                + sdf.format(trip.getDepartureTime()));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    try {
        passengerList =
            passengerService.findAllPassengers();
        cmbPassenger.removeAllItems();
        for (Passenger p : passengerList) {
            cmbPassenger.addItem(
                p.getFullName()
                + " - " + p.getPhone());
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
    private void loadTickets() {
    if (ticketService == null) {
        JOptionPane.showMessageDialog(this,
            "Not connected to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Ticket> tickets =
            ticketService.findAllTickets();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Ticket ticket : tickets) {
            tableModel.addRow(new Object[]{
                ticket.getTicketId(),
                ticket.getTrip().getRoute()
                    .getRouteName(),
                ticket.getPassenger().getFullName(),
                ticket.getSeatNumber(),
                ticket.getAmountPaid(),
                sdf.format(ticket.getIssuedDate())
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading tickets: "
            + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    
    private void populateForm(int row) {
    try {
        Long id = (Long) tableModel.getValueAt(row, 0);
        selectedTicket =
            ticketService.findTicketById(id);

        for (int i = 0; i < tripList.size(); i++) {
            if (tripList.get(i).getTripId()
                    .equals(selectedTicket
                        .getTrip().getTripId())) {
                cmbTrip.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0;
                i < passengerList.size(); i++) {
            if (passengerList.get(i).getPassengerId()
                    .equals(selectedTicket
                        .getPassenger()
                        .getPassengerId())) {
                cmbPassenger.setSelectedIndex(i);
                break;
            }
        }

        txtSeatNumber.setText(String.valueOf(
            selectedTicket.getSeatNumber()));
        txtAmount.setText(String.valueOf(
            selectedTicket.getAmountPaid()));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
    
    private boolean validateForm() {
    if (cmbTrip.getSelectedIndex() == -1) {
        showError("Please select a trip.");
        return false;
    }
    if (cmbPassenger.getSelectedIndex() == -1) {
        showError("Please select a passenger.");
        return false;
    }
    if (txtSeatNumber.getText().trim().isEmpty()) {
        showError("Seat number is required.");
        txtSeatNumber.requestFocus();
        return false;
    }
    try {
        int seat = Integer.parseInt(
            txtSeatNumber.getText().trim());
        if (seat <= 0) {
            showError(
                "Seat number must be greater "
                + "than zero.");
            txtSeatNumber.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        showError(
            "Seat number must be a valid number.");
        txtSeatNumber.requestFocus();
        return false;
    }
    if (txtAmount.getText().trim().isEmpty()) {
        showError("Amount paid is required.");
        txtAmount.requestFocus();
        return false;
    }
    try {
        double amount = Double.parseDouble(
            txtAmount.getText().trim());
        if (amount <= 0) {
            showError(
                "Amount must be greater than zero.");
            txtAmount.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        showError("Amount must be a valid number.");
        txtAmount.requestFocus();
        return false;
    }
    return true;
}
    
    
    private void clearForm() {
    if (cmbTrip.getItemCount() > 0)
        cmbTrip.setSelectedIndex(0);
    if (cmbPassenger.getItemCount() > 0)
        cmbPassenger.setSelectedIndex(0);
    txtSeatNumber.setText("");
    txtAmount.setText("");
    selectedTicket = null;
    ticketTable.clearSelection();
}

private void showError(String message) {
    JOptionPane.showMessageDialog(this,
        message, "Validation Error",
        JOptionPane.ERROR_MESSAGE);
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify
     * this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cmbTrip = new javax.swing.JComboBox<>();
        cmbPassenger = new javax.swing.JComboBox<>();
        txtSeatNumber = new javax.swing.JTextField();
        txtAmount = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ticketTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Ticket Management");

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
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(29, 29, 29))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnBack))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Trip");

        jLabel3.setText("Passenger");

        jLabel4.setText("Seat Number");

        jLabel5.setText("Amount Paid");

        cmbTrip.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbPassenger.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        saveBtn.setText("Save");
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

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        clearBtn.setText("Clear");
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
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtSeatNumber)
                            .addComponent(txtAmount)
                            .addComponent(cmbPassenger, 0, 125, Short.MAX_VALUE)
                            .addComponent(cmbTrip, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(51, 51, 51))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(saveBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
                        .addComponent(updateBtn)
                        .addGap(18, 18, 18)
                        .addComponent(deleteBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clearBtn)
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(cmbTrip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(46, 46, 46)
                                .addComponent(jLabel3))
                            .addComponent(cmbPassenger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(49, 49, 49)
                        .addComponent(jLabel4))
                    .addComponent(txtSeatNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(49, 49, 49)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(updateBtn)
                    .addComponent(deleteBtn)
                    .addComponent(clearBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        btnRefresh.setText("refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        ticketTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(ticketTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(btnSearch)
                .addGap(18, 18, 18)
                .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
         String keyword = txtSearch.getText().trim();
    if (keyword.isEmpty()) {
        loadTickets();
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<Ticket> tickets =
            ticketService.findAllTickets();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Ticket ticket : tickets) {
            if (ticket.getTrip().getRoute()
                    .getRouteName().toLowerCase()
                    .contains(keyword.toLowerCase())
                || ticket.getPassenger()
                    .getFullName().toLowerCase()
                    .contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    ticket.getTicketId(),
                    ticket.getTrip().getRoute()
                        .getRouteName(),
                    ticket.getPassenger()
                        .getFullName(),
                    ticket.getSeatNumber(),
                    ticket.getAmountPaid(),
                    sdf.format(
                        ticket.getIssuedDate())
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
         if (!validateForm()) return;
    try {
        Ticket ticket = new Ticket();
        ticket.setTrip(tripList.get(
            cmbTrip.getSelectedIndex()));
        ticket.setPassenger(passengerList.get(
            cmbPassenger.getSelectedIndex()));
        ticket.setSeatNumber(Integer.parseInt(
            txtSeatNumber.getText().trim()));
        ticket.setAmountPaid(Double.parseDouble(
            txtAmount.getText().trim()));
        ticket.setIssuedDate(new java.util.Date());

        ticketService.saveTicket(ticket);
        JOptionPane.showMessageDialog(this,
            "Ticket issued successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadTickets();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (selectedTicket == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a ticket from "
            + "the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!validateForm()) return;
    try {
        selectedTicket.setTrip(tripList.get(
            cmbTrip.getSelectedIndex()));
        selectedTicket.setPassenger(
            passengerList.get(
                cmbPassenger.getSelectedIndex()));
        selectedTicket.setSeatNumber(
            Integer.parseInt(
                txtSeatNumber.getText().trim()));
        selectedTicket.setAmountPaid(
            Double.parseDouble(
                txtAmount.getText().trim()));

        ticketService.updateTicket(selectedTicket);
        JOptionPane.showMessageDialog(this,
            "Ticket updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadTickets();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
         if (selectedTicket == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a ticket from "
            + "the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete "
        + "this ticket?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            ticketService.deleteTicket(
                selectedTicket);
            JOptionPane.showMessageDialog(this,
                "Ticket deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadTickets();
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

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
         loadTickets();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new AdminDashboard(currentUser).setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

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
            java.util.logging.Logger.getLogger(TicketsManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TicketsManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TicketsManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TicketsManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setRole(UserRole.ADMIN);
                new TicketsManagementForm(testUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton clearBtn;
    private javax.swing.JComboBox<String> cmbPassenger;
    private javax.swing.JComboBox<String> cmbTrip;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTable ticketTable;
    private javax.swing.JTextField txtAmount;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtSeatNumber;
    private javax.swing.JButton updateBtn;
    // End of variables declaration//GEN-END:variables
}
