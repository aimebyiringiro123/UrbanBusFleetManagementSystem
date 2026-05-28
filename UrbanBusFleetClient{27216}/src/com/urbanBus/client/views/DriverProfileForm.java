/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.Driver;
import com.urbanBus.server.model.DriverProfile;
import com.urbanBus.server.model.User;
import com.urbanBus.server.model.UserRole;
import com.urbanBus.server.service
    .DriverProfileService;
import javax.swing.JOptionPane;

/**
 *
 * @author aimeb
 */
public class DriverProfileForm extends javax.swing.JFrame {
    
    private User currentUser;
    private Driver selectedDriver;
    private DriverProfileService profileService;
    private DriverProfile existingProfile;

    /**
     * Creates new form DriverProfileForm
     */
    public DriverProfileForm(User user, Driver driver) {
        this.currentUser = user;
        this.selectedDriver = driver;
        try {
            this.profileService =
                RMIClient.getDriverProfileService();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Cannot connect to server.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        initComponents();
        // ── Background ────────────────────────────────────
    getContentPane().setBackground(UIUtils.BG_PAGE);

    // ── Left card (edit fields panel) ─────────────────
    jPanel2.setBackground(java.awt.Color.WHITE);
    jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(UIUtils.BORDER, 1, true),
        javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));

    // ── Right card (info display panel) ───────────────
    jPanel3.setBackground(java.awt.Color.WHITE);
    jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(UIUtils.BORDER, 1, true),
        javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20)
    ));

    // ── Style edit fields ─────────────────────────────
    UIUtils.styleField(txtAddress);
    UIUtils.styleField(txtEmergency);
    UIUtils.styleField(txtBloodType);
    UIUtils.styleField(txtYears);

    txtAddress.setPreferredSize(new java.awt.Dimension(0, 36));
    txtEmergency.setPreferredSize(new java.awt.Dimension(0, 36));
    txtBloodType.setPreferredSize(new java.awt.Dimension(0, 36));
    txtYears.setPreferredSize(new java.awt.Dimension(0, 36));

    // ── Style buttons ─────────────────────────────────
    UIUtils.stylePrimaryButton(saveBtn);
    UIUtils.styleDangerButton(closeBtn);

    saveBtn.setPreferredSize(new java.awt.Dimension(
        saveBtn.getPreferredSize().width, 36));
    closeBtn.setPreferredSize(new java.awt.Dimension(
        closeBtn.getPreferredSize().width, 36));

    // ── Style the info labels on the right panel ──────
    // Make the value labels look clean
    java.awt.Font valFont = new java.awt.Font("Arial", java.awt.Font.BOLD, 13);
    java.awt.Font keyFont = new java.awt.Font("Arial", java.awt.Font.PLAIN, 12);

    lblValName.setFont(valFont);       lblValName.setForeground(UIUtils.TEXT_DARK);
    lblValLicense.setFont(valFont);    lblValLicense.setForeground(UIUtils.TEXT_DARK);
    lblValExpiry.setFont(valFont);     lblValExpiry.setForeground(UIUtils.TEXT_DARK);
    lblValPhone.setFont(valFont);      lblValPhone.setForeground(UIUtils.TEXT_DARK);
    lblValStatus.setFont(valFont);     lblValStatus.setForeground(UIUtils.TEAL);
    lblValAddress.setFont(valFont);    lblValAddress.setForeground(UIUtils.TEXT_DARK);
    lblValEmergency.setFont(valFont);  lblValEmergency.setForeground(UIUtils.TEXT_DARK);
    lblValBloodType.setFont(valFont);  lblValBloodType.setForeground(UIUtils.TEXT_DARK);
    lblValExperience.setFont(valFont); lblValExperience.setForeground(UIUtils.TEXT_DARK);

    lblDetailName.setFont(keyFont);       lblDetailName.setForeground(UIUtils.TEXT_GRAY);
    lblDetailLicense.setFont(keyFont);    lblDetailLicense.setForeground(UIUtils.TEXT_GRAY);
    lblDetailExpiry.setFont(keyFont);     lblDetailExpiry.setForeground(UIUtils.TEXT_GRAY);
    lblDetailPhone.setFont(keyFont);      lblDetailPhone.setForeground(UIUtils.TEXT_GRAY);
    lblDetailStatus.setFont(keyFont);     lblDetailStatus.setForeground(UIUtils.TEXT_GRAY);
    lblDetailAddress.setFont(keyFont);    lblDetailAddress.setForeground(UIUtils.TEXT_GRAY);
    lblDetailEmergency.setFont(keyFont);  lblDetailEmergency.setForeground(UIUtils.TEXT_GRAY);
    lblDetailBlood.setFont(keyFont);      lblDetailBlood.setForeground(UIUtils.TEXT_GRAY);
    lblDetailYears.setFont(keyFont);      lblDetailYears.setForeground(UIUtils.TEXT_GRAY);

    setLocationRelativeTo(null);
    setTitle("Driver Profile - " + driver.getFullName());
    loadProfile();
    }
    
    
    
    
    
    
    
   private void loadProfile() {
    try {
        existingProfile =
            profileService.findByDriverId(
                selectedDriver.getDriverId());
        if (existingProfile != null) {
            txtAddress.setText(
                existingProfile.getAddress()
                != null ?
                existingProfile.getAddress()
                : "");
            txtEmergency.setText(
                existingProfile
                    .getEmergencyContact()
                != null ?
                existingProfile
                    .getEmergencyContact()
                : "");
            txtBloodType.setText(
                existingProfile.getBloodType()
                != null ?
                existingProfile.getBloodType()
                : "");
            txtYears.setText(String.valueOf(
                existingProfile
                    .getYearsExperience()));
        }
        // Update display after loading
        updateProfileDisplay();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
    
    private void updateProfileDisplay() {
    // Basic driver info
    lblValName.setText(
        selectedDriver.getFullName());
    lblValLicense.setText(
        selectedDriver.getLicenseNumber());
    lblValExpiry.setText(
        selectedDriver.getLicenseExpiry()
            .toString());
    lblValPhone.setText(
        selectedDriver.getPhone());
    lblValStatus.setText(
        selectedDriver.getStatus().toString());

    // Profile info
    if (existingProfile != null) {
        lblValAddress.setText(
            existingProfile.getAddress() != null
            ? existingProfile.getAddress()
            : "—");
        lblValEmergency.setText(
            existingProfile.getEmergencyContact()
            != null ?
            existingProfile.getEmergencyContact()
            : "—");
        lblValBloodType.setText(
            existingProfile.getBloodType() != null
            ? existingProfile.getBloodType()
            : "—");
        lblValExperience.setText(
            existingProfile.getYearsExperience()
            + " years");
    } else {
        lblValAddress.setText("—");
        lblValEmergency.setText("—");
        lblValBloodType.setText("—");
        lblValExperience.setText("—");
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

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jLabel15 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtAddress = new javax.swing.JTextField();
        txtEmergency = new javax.swing.JTextField();
        txtBloodType = new javax.swing.JTextField();
        txtYears = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblDetailName = new javax.swing.JLabel();
        lblDetailLicense = new javax.swing.JLabel();
        lblDetailExpiry = new javax.swing.JLabel();
        lblDetailPhone = new javax.swing.JLabel();
        lblDetailStatus = new javax.swing.JLabel();
        lblDetailAddress = new javax.swing.JLabel();
        lblDetailEmergency = new javax.swing.JLabel();
        lblDetailBlood = new javax.swing.JLabel();
        lblDetailYears = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblValName = new javax.swing.JLabel();
        lblValLicense = new javax.swing.JLabel();
        lblValExpiry = new javax.swing.JLabel();
        lblValPhone = new javax.swing.JLabel();
        lblValStatus = new javax.swing.JLabel();
        lblValAddress = new javax.swing.JLabel();
        lblValEmergency = new javax.swing.JLabel();
        lblValBloodType = new javax.swing.JLabel();
        lblValExperience = new javax.swing.JLabel();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jLabel15.setText("jLabel15");

        jLabel17.setText("jLabel17");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Driver's profile");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setText("Address");

        jLabel3.setText("Emergency contact");

        jLabel4.setText("Blood type");

        jLabel5.setText("Years of experience");

        txtEmergency.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmergencyActionPerformed(evt);
            }
        });

        saveBtn.setBackground(new java.awt.Color(0, 77, 77));
        saveBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        closeBtn.setBackground(new java.awt.Color(0, 77, 77));
        closeBtn.setForeground(new java.awt.Color(255, 255, 255));
        closeBtn.setText("Close");
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(87, 87, 87)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtBloodType)
                            .addComponent(txtYears)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel2))
                        .addGap(91, 91, 91)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtAddress)
                            .addComponent(txtEmergency))))
                .addGap(109, 109, 109))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE)
                .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(47, 47, 47)
                                .addComponent(jLabel3))
                            .addComponent(txtEmergency, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addComponent(jLabel4))
                    .addComponent(txtBloodType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(txtYears, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(closeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(105, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(245, 247, 250));

        lblDetailName.setText("Full Names: ");

        lblDetailLicense.setText("License No: ");

        lblDetailExpiry.setText("Expiry: ");

        lblDetailPhone.setText("Phone:");

        lblDetailStatus.setText("Status:");

        lblDetailAddress.setText("Address:");

        lblDetailEmergency.setText("Emergency:");

        lblDetailBlood.setText("Blood Type:");

        lblDetailYears.setText("Experience:");

        jLabel6.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N
        jLabel6.setText("Profile");

        lblValName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValName.setText("jLabel7");

        lblValLicense.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValLicense.setText("jLabel8");

        lblValExpiry.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValExpiry.setText("jLabel9");

        lblValPhone.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValPhone.setText("jLabel10");

        lblValStatus.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValStatus.setText("jLabel11");

        lblValAddress.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValAddress.setText("jLabel12");

        lblValEmergency.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValEmergency.setText("jLabel13");

        lblValBloodType.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValBloodType.setText("jLabel14");

        lblValExperience.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblValExperience.setText("jLabel16");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDetailName)
                            .addComponent(lblDetailLicense)
                            .addComponent(lblDetailExpiry)
                            .addComponent(lblDetailPhone)
                            .addComponent(lblDetailStatus)
                            .addComponent(lblDetailAddress)
                            .addComponent(lblDetailEmergency)
                            .addComponent(lblDetailBlood)
                            .addComponent(lblDetailYears))
                        .addGap(44, 44, 44)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblValExperience)
                            .addComponent(lblValBloodType)
                            .addComponent(lblValEmergency)
                            .addComponent(lblValAddress)
                            .addComponent(lblValStatus)
                            .addComponent(lblValPhone)
                            .addComponent(lblValExpiry)
                            .addComponent(lblValLicense)
                            .addComponent(lblValName)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(175, 175, 175)
                        .addComponent(jLabel6)))
                .addContainerGap(171, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jLabel6)
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailName)
                    .addComponent(lblValName))
                .addGap(27, 27, 27)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailLicense)
                    .addComponent(lblValLicense))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailExpiry)
                    .addComponent(lblValExpiry))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailPhone)
                    .addComponent(lblValPhone))
                .addGap(30, 30, 30)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailStatus)
                    .addComponent(lblValStatus))
                .addGap(28, 28, 28)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailAddress)
                    .addComponent(lblValAddress))
                .addGap(26, 26, 26)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailEmergency)
                    .addComponent(lblValEmergency))
                .addGap(32, 32, 32)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailBlood)
                    .addComponent(lblValBloodType))
                .addGap(31, 31, 31)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDetailYears)
                    .addComponent(lblValExperience))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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

    private void txtEmergencyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmergencyActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmergencyActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        try {
            if (existingProfile == null) {
                // Create new profile
                DriverProfile profile =
                    new DriverProfile();
                profile.setDriver(selectedDriver);
                profile.setAddress(
                    txtAddress.getText().trim());
                profile.setEmergencyContact(
                    txtEmergency.getText().trim());
                profile.setBloodType(
                    txtBloodType.getText().trim());
                if (!txtYears.getText()
                        .trim().isEmpty()) {
                    profile.setYearsExperience(
                        Integer.parseInt(
                            txtYears.getText()
                                .trim()));
                }
                profileService.saveProfile(profile);
            } else {
                // Update existing profile
                existingProfile.setAddress(
                    txtAddress.getText().trim());
                existingProfile.setEmergencyContact(
                    txtEmergency.getText().trim());
                existingProfile.setBloodType(
                    txtBloodType.getText().trim());
                if (!txtYears.getText()
                        .trim().isEmpty()) {
                    existingProfile
                        .setYearsExperience(
                            Integer.parseInt(
                                txtYears.getText()
                                    .trim()));
                }
                profileService.updateProfile(
                    existingProfile);
            }
            JOptionPane.showMessageDialog(this,
                "Profile saved successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
            loadProfile();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        dispose();
    }//GEN-LAST:event_closeBtnActionPerformed

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
            java.util.logging.Logger.getLogger(DriverProfileForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DriverProfileForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DriverProfileForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DriverProfileForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setFullName("Test User");
                testUser.setRole(UserRole.ADMIN);
                new DriverManagementForm(testUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeBtn;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblDetailAddress;
    private javax.swing.JLabel lblDetailBlood;
    private javax.swing.JLabel lblDetailEmergency;
    private javax.swing.JLabel lblDetailExpiry;
    private javax.swing.JLabel lblDetailLicense;
    private javax.swing.JLabel lblDetailName;
    private javax.swing.JLabel lblDetailPhone;
    private javax.swing.JLabel lblDetailStatus;
    private javax.swing.JLabel lblDetailYears;
    private javax.swing.JLabel lblValAddress;
    private javax.swing.JLabel lblValBloodType;
    private javax.swing.JLabel lblValEmergency;
    private javax.swing.JLabel lblValExperience;
    private javax.swing.JLabel lblValExpiry;
    private javax.swing.JLabel lblValLicense;
    private javax.swing.JLabel lblValName;
    private javax.swing.JLabel lblValPhone;
    private javax.swing.JLabel lblValStatus;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBloodType;
    private javax.swing.JTextField txtEmergency;
    private javax.swing.JTextField txtYears;
    // End of variables declaration//GEN-END:variables
}
