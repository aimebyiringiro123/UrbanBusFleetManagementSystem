/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.User;
import com.urbanBus.server.model.UserRole;
import com.urbanBus.server.service.UserService;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author aimeb
 */
public class UserManagementForm extends javax.swing.JFrame {
    
    private User currentUser;
    private UserService userService;
    private User selectedUser;
    private DefaultTableModel tableModel;

    /**
     * Creates new form UserManagementForm
     */
    public UserManagementForm(User user) {
    this.currentUser = user;
    try {
        this.userService =
            RMIClient.getUserService();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Cannot connect to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    initComponents();
    getContentPane().setBackground(UIUtils.BG_PAGE);
    UIUtils.styleHeaderButton(btnBack); 
    UIUtils.styleField(txtSearch);
    UIUtils.setPlaceholder(txtSearch, "Search by username or role...");
    UIUtils.stylePrimaryButton(btnSearch);
    UIUtils.styleOutlineButton(btnRefresh);
    btnSearch.setPreferredSize(new java.awt.Dimension(100, 36));
    btnRefresh.setPreferredSize(new java.awt.Dimension(100, 36));
    buildFormPanel();
    setLocationRelativeTo(null); 
    setupTable();
    loadUsers();
}
    
    
    
   private void buildFormPanel() {
    jPanel1.removeAll();
    jPanel1.setLayout(new java.awt.GridBagLayout());
    jPanel1.setBackground(java.awt.Color.WHITE);
    jPanel1.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(UIUtils.BORDER, 1, true),
        javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0; gbc.gridx = 0;

    gbc.gridy = 0; gbc.insets = new java.awt.Insets(0, 0, 14, 0);
    jPanel1.add(UIUtils.makeSectionLabel("USER DETAILS"), gbc);

    addFormRow(jPanel1, gbc, 1, "Full Name", txtFullName);
    addFormRow(jPanel1, gbc, 3, "Username",  txtUsername);

    // Password field
    gbc.gridy = 5; gbc.insets = new java.awt.Insets(6, 0, 2, 0);
    javax.swing.JLabel lblPwd = new javax.swing.JLabel("Password");
    lblPwd.setFont(UIUtils.FONT_LABEL); lblPwd.setForeground(UIUtils.TEXT_GRAY);
    jPanel1.add(lblPwd, gbc);
    gbc.gridy = 6; gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    txtPassword.setPreferredSize(new java.awt.Dimension(0, 36));
    txtPassword.setFont(UIUtils.FONT_INPUT);
    txtPassword.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(UIUtils.BORDER, 1, true),
        javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
    ));
    jPanel1.add(txtPassword, gbc);

    addFormRow(jPanel1, gbc, 7, "Email", txtEmail);
    addFormRow(jPanel1, gbc, 9, "Phone", txtPhone);

    // Role combo
    gbc.gridy = 11; gbc.insets = new java.awt.Insets(6, 0, 2, 0);
    javax.swing.JLabel lblRole = new javax.swing.JLabel("Role");
    lblRole.setFont(UIUtils.FONT_LABEL); lblRole.setForeground(UIUtils.TEXT_GRAY);
    jPanel1.add(lblRole, gbc);
    gbc.gridy = 12; gbc.insets = new java.awt.Insets(0, 0, 12, 0);
    cmbRole.setPreferredSize(new java.awt.Dimension(0, 36));
    UIUtils.styleCombo(cmbRole);
    jPanel1.add(cmbRole, gbc);

    // ✅ Active checkbox — re-added here
    gbc.gridy = 13; gbc.insets = new java.awt.Insets(4, 0, 16, 0);
    chkActive.setText("Active");
    chkActive.setFont(UIUtils.FONT_LABEL);
    chkActive.setForeground(UIUtils.TEXT_DARK);
    chkActive.setBackground(java.awt.Color.WHITE);
    chkActive.setFocusPainted(false);
    chkActive.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    chkActive.setSelected(true);   // default to active for new users
    jPanel1.add(chkActive, gbc);

    // Spacer
    gbc.gridy = 14; gbc.weighty = 1.0;
    gbc.fill = java.awt.GridBagConstraints.VERTICAL;
    jPanel1.add(new javax.swing.JPanel() {{ setOpaque(false); }}, gbc);

    // Buttons
    gbc.gridy = 15; gbc.weighty = 0;
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
    jPanel1.add(btnPanel, gbc);

    jPanel1.revalidate();
    jPanel1.repaint();
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
        cmbRole.setModel(new javax.swing.DefaultComboBoxModel(UserRole.values()));
    String[] columns = {
        "ID", "Full Name", "Username",
        "Email", "Phone", "Role", "Active"
    };
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(
                int row, int col) {
            return false;
        }
    };
    userTable.setModel(tableModel);
    userTable.setRowHeight(30);
    userTable.getTableHeader().setBackground(
        new java.awt.Color(26, 62, 111));
    userTable.getTableHeader().setForeground(
        java.awt.Color.WHITE);
    userTable.setSelectionBackground(
        new java.awt.Color(210, 228, 255));

    // Hide ID column
    userTable.getColumnModel().getColumn(0)
        .setMinWidth(0);
    userTable.getColumnModel().getColumn(0)
        .setMaxWidth(0);

    // Load UserRole into combobox
    cmbRole.setModel(
        new javax.swing.DefaultComboBoxModel(
            UserRole.values()));

    // Row click listener
    userTable.addMouseListener(
            new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(
                java.awt.event.MouseEvent e) {
            int row = userTable.getSelectedRow();
            if (row != -1) populateForm(row);
        }
    });
    UIUtils.styleTable(userTable);
    UIUtils.styleScrollPane(jScrollPane1);
    userTable.getColumnModel().getColumn(5).setCellRenderer(UIUtils.statusRenderer()); // Role column
}
    
    
    
    private void loadUsers() {
    if (userService == null) {
        JOptionPane.showMessageDialog(this,
            "Not connected to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<User> users =
            userService.findAllUsers();
        for (User u : users) {
            tableModel.addRow(new Object[]{
                u.getUserId(),
                u.getFullName(),
                u.getUsername(),
                u.getEmail(),
                u.getPhone(),
                u.getRole(),
                u.isActive() ? "Yes" : "No"
            });
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error loading users: "
            + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    
    private void populateForm(int row) {
    try {
        Long id = (Long) tableModel.getValueAt(row, 0);
        selectedUser = userService.findUserById(id);
        txtFullName.setText(
            selectedUser.getFullName());
        txtUsername.setText(
            selectedUser.getUsername());
        txtPassword.setText(
            selectedUser.getPassword());
        txtEmail.setText(selectedUser.getEmail());
        txtPhone.setText(selectedUser.getPhone());
        cmbRole.setSelectedItem(
            selectedUser.getRole());
        chkActive.setSelected(
            selectedUser.isActive());
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    private boolean validateForm() {
    if (txtFullName.getText().trim().isEmpty()) {
        showError("Full name is required.");
        txtFullName.requestFocus();
        return false;
    }
    if (txtUsername.getText().trim().isEmpty()) {
        showError("Username is required.");
        txtUsername.requestFocus();
        return false;
    }
    if (new String(txtPassword.getPassword())
            .trim().isEmpty()) {
        showError("Password is required.");
        txtPassword.requestFocus();
        return false;
    }
    if (new String(txtPassword.getPassword())
            .trim().length() < 6) {
        showError(
            "Password must be at least "
            + "6 characters.");
        txtPassword.requestFocus();
        return false;
    }
    if (txtEmail.getText().trim().isEmpty()) {
        showError("Email is required.");
        txtEmail.requestFocus();
        return false;
    }
    if (!txtEmail.getText().trim()
            .matches(
                "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
        showError("Please enter a valid email.");
        txtEmail.requestFocus();
        return false;
    }
    if (!txtPhone.getText().trim().isEmpty()) {
        if (!txtPhone.getText().trim()
                .matches("07\\d{8}")) {
            showError(
                "Phone must be valid Rwandan "
                + "number (07XXXXXXXX).");
            txtPhone.requestFocus();
            return false;
        }
    }
    return true;
}
    
    private void clearForm() {
    txtFullName.setText("");
    txtUsername.setText("");
    txtPassword.setText("");
    txtEmail.setText("");
    txtPhone.setText("");
    cmbRole.setSelectedIndex(0);
    chkActive.setSelected(true);
    selectedUser = null;
    userTable.clearSelection();
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

        jLabel9 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jComboBox3 = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        updateBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        txtFullName = new javax.swing.JTextField();
        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();
        txtEmail = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        cmbRole = new javax.swing.JComboBox<>();
        chkActive = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        btnSearch = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();

        jLabel9.setText("jLabel9");

        jButton5.setText("jButton5");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblTitle.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("User Management");

        btnBack.setBackground(new java.awt.Color(0, 77, 77));
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lblTitleLayout = new javax.swing.GroupLayout(lblTitle);
        lblTitle.setLayout(lblTitleLayout);
        lblTitleLayout.setHorizontalGroup(
            lblTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblTitleLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(42, 42, 42))
        );
        lblTitleLayout.setVerticalGroup(
            lblTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblTitleLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(lblTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnBack))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Full Name");

        jLabel3.setText("UserName");

        jLabel4.setText("Password");

        jLabel5.setText("Email");

        jLabel6.setText("Phone");

        jLabel7.setText("Role");

        jLabel8.setText("Active");

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
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

        txtFullName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFullNameActionPerformed(evt);
            }
        });

        txtPhone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPhoneActionPerformed(evt);
            }
        });

        cmbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(saveBtn)
                        .addGap(14, 14, 14)
                        .addComponent(deleteBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(clearBtn))
                    .addComponent(jLabel4)
                    .addComponent(jLabel2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFullName)
                            .addComponent(txtUsername)
                            .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel7))
                        .addGap(92, 92, 92)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(chkActive)
                            .addComponent(txtPhone)
                            .addComponent(cmbRole, 0, 127, Short.MAX_VALUE))))
                .addContainerGap(87, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(txtFullName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPassword, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(chkActive))
                .addGap(68, 68, 68)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(deleteBtn)
                    .addComponent(updateBtn)
                    .addComponent(clearBtn))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(245, 247, 250));

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

        userTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(userTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnSearch)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnRefresh)
                .addGap(31, 31, 31))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSearch)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lblTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtPhoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPhoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPhoneActionPerformed

    private void txtFullNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFullNameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFullNameActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
         if (!validateForm()) return;
    try {
        User user = new User();
        user.setFullName(
            txtFullName.getText().trim());
        user.setUsername(
            txtUsername.getText().trim());
        user.setPassword(new String(
            txtPassword.getPassword()).trim());
        user.setEmail(txtEmail.getText().trim());
        user.setPhone(txtPhone.getText().trim());
        user.setRole((UserRole)
            cmbRole.getSelectedItem());
        user.setActive(chkActive.isSelected());

        userService.saveUser(user);

        /* Send notification via ActiveMQ
        com.urbanBus.server.notification
            .NotificationProducer.sendNewUserAlert(
                currentUser.getEmail(),
                user.getFullName(),
                user.getRole().toString());
        */

        JOptionPane.showMessageDialog(this,
            "User saved successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadUsers();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
         if (selectedUser == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a user from the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (selectedUser.getUsername()
            .equals("admin")) {
        JOptionPane.showMessageDialog(this,
            "Cannot delete the admin account.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    int confirm = JOptionPane.showConfirmDialog(
        this,
        "Are you sure you want to delete user: "
        + selectedUser.getFullName() + "?",
        "Confirm Delete",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE);
    if (confirm == JOptionPane.YES_OPTION) {
        try {
            userService.deleteUser(selectedUser);
            JOptionPane.showMessageDialog(this,
                "User deleted successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            loadUsers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBtnActionPerformed
        if (selectedUser == null) {
        JOptionPane.showMessageDialog(this,
            "Please select a user from the table.",
            "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }
    if (!validateForm()) return;
    try {
        selectedUser.setFullName(
            txtFullName.getText().trim());
        selectedUser.setUsername(
            txtUsername.getText().trim());
        selectedUser.setPassword(new String(
            txtPassword.getPassword()).trim());
        selectedUser.setEmail(
            txtEmail.getText().trim());
        selectedUser.setPhone(
            txtPhone.getText().trim());
        selectedUser.setRole((UserRole)
            cmbRole.getSelectedItem());
        selectedUser.setActive(
            chkActive.isSelected());

        userService.updateUser(selectedUser);
        JOptionPane.showMessageDialog(this,
            "User updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        clearForm();
        loadUsers();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_updateBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        clearForm();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
         String keyword = txtSearch.getText().trim();
    if (keyword.isEmpty()) {
        loadUsers();
        return;
    }
    try {
        tableModel.setRowCount(0);
        List<User> users =
            userService.findAllUsers();
        for (User u : users) {
            if (u.getFullName().toLowerCase()
                    .contains(keyword.toLowerCase())
                || u.getUsername().toLowerCase()
                    .contains(keyword.toLowerCase())
                || u.getEmail().toLowerCase()
                    .contains(
                        keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    u.getUserId(),
                    u.getFullName(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getPhone(),
                    u.getRole(),
                    u.isActive() ? "Yes" : "No"
                });
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        loadUsers();
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
            java.util.logging.Logger.getLogger(UserManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserManagementForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setFullName("System Administrator");
                testUser.setRole(UserRole.ADMIN);
                new UserManagementForm(testUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSearch;
    private javax.swing.JCheckBox chkActive;
    private javax.swing.JButton clearBtn;
    private javax.swing.JComboBox<String> cmbRole;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox<String> jComboBox3;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel lblTitle;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtFullName;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JButton updateBtn;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
