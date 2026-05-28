/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.User;
import com.urbanBus.server.model.UserRole;
import com.urbanBus.server.service.BusService;
import com.urbanBus.server.service.DriverService;
import com.urbanBus.server.service.TripService;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author aimeb
 */
public class AdminDashboard extends javax.swing.JFrame {
    
    private User currentUser;

    /**
     * Creates new form AdminDashboard
     */
    public AdminDashboard(User user) {
        this.currentUser = user;
        initComponents();
        setLocationRelativeTo(null);
        loadStats(); 
        applyRoleAccess();
    }
    
    private void loadStats() {
        try {
            BusService busService =
                RMIClient
                    .getBusService();
            int count = busService.findAllBuses().size();
            lblBusCount.setText(String.valueOf(count));
        } catch (Exception e) {
            lblBusCount.setText("0");
        }

        try {
            DriverService ds =
                RMIClient
                    .getDriverService();
            int count = ds.findAllDrivers().size();
            lblDriverCount.setText(String.valueOf(count));
        } catch (Exception e) {
            lblDriverCount.setText("0");
        }

        try {
            TripService ts =
                RMIClient
                    .getTripService();
            int count = ts.findAllTrips().size();
            lblTripCount.setText(String.valueOf(count));
        } catch (Exception e) {
            lblTripCount.setText("0");
        }
    }
    
    
    
    
    
    
    
    
private void applyRoleAccess() {
    UserRole role = currentUser.getRole();

    if (role == UserRole.ADMIN) {
        return;
    }

    // Remove all existing buttons
    sidePanel.removeAll();

    // Reset layout
    sidePanel.setLayout(
        new javax.swing.BoxLayout(
            sidePanel,
            javax.swing.BoxLayout.Y_AXIS));
    sidePanel.setBackground(
        new java.awt.Color(26, 62, 111));
    sidePanel.setBorder(
        javax.swing.BorderFactory
            .createEmptyBorder(16, 0, 16, 0));

    // Add menu label
    javax.swing.JLabel menuLabel =
        new javax.swing.JLabel("  MAIN MENU");
    menuLabel.setFont(new java.awt.Font(
        "Arial", java.awt.Font.BOLD, 10));
    menuLabel.setForeground(
        new java.awt.Color(107, 159, 212));
    menuLabel.setBorder(
        javax.swing.BorderFactory
            .createEmptyBorder(0, 16, 8, 0));
    menuLabel.setAlignmentX(
        java.awt.Component.LEFT_ALIGNMENT);
    sidePanel.add(menuLabel);

    // Dashboard button
    javax.swing.JButton dash =
        makeSideBtn(" Dashboard", true);
    sidePanel.add(dash);
    sidePanel.add(
        javax.swing.Box.createVerticalStrut(2));

    // Role specific buttons
    if (role == UserRole.DISPATCHER) {
        addSideButton(" Bus Management",
            () -> {
                dispose();
                new BusManagementForm(currentUser)
                    .setVisible(true);
            });
        addSideButton(" Driver Management",
            () -> {
                dispose();
                new DriverManagementForm(currentUser)
                    .setVisible(true);
            });
        addSideButton(" Route Management",
            () -> {
                dispose();
                new RouteManagementForm(currentUser)
                    .setVisible(true);
            });
        addSideButton(" Trip Scheduling",
            () -> {
                dispose();
                new TripManagementForm(currentUser)
                    .setVisible(true);
            });

    } else if (role == UserRole.ACCOUNTANT) {
        addSideButton(" Tickets", () -> {
            dispose();
            new TicketsManagementForm(currentUser)
                .setVisible(true);
        });
        addSideButton("Reports", () -> {
            dispose();
            new ReportsForm(currentUser)
                .setVisible(true);
        });

    } else if (role ==
            UserRole.MAINTENANCE_OFFICER) {
        addSideButton("Maintenance", () -> {
            dispose();
            new MaintenanceManagementForm(
                currentUser).setVisible(true);
        });
    }

    // Logout at bottom
    sidePanel.add(
        javax.swing.Box.createVerticalGlue());
    javax.swing.JButton logout =
        new javax.swing.JButton(" Logout");
    logout.setFont(new java.awt.Font(
        "Arial", java.awt.Font.PLAIN, 12));
    logout.setForeground(
        new java.awt.Color(244, 160, 160));
    logout.setBackground(
        new java.awt.Color(26, 62, 111));
    logout.setBorderPainted(false);
    logout.setFocusPainted(false);
    logout.setCursor(new java.awt.Cursor(
        java.awt.Cursor.HAND_CURSOR));
    logout.setMaximumSize(
        new java.awt.Dimension(210, 40));
    logout.setMinimumSize(
        new java.awt.Dimension(210, 40));
    logout.setPreferredSize(
        new java.awt.Dimension(210, 40));
    logout.setHorizontalAlignment(
        javax.swing.SwingConstants.LEFT);
    logout.setBorder(
        javax.swing.BorderFactory
            .createEmptyBorder(0, 16, 0, 0));
    logout.setAlignmentX(
        java.awt.Component.LEFT_ALIGNMENT);
    logout.addActionListener(e -> {
        int confirm =
            javax.swing.JOptionPane
                .showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Logout",
                javax.swing.JOptionPane
                    .YES_NO_OPTION);
        if (confirm ==
                javax.swing.JOptionPane
                    .YES_OPTION) {
            dispose();
            new LoginForm().setVisible(true);
        }
    });
    sidePanel.add(logout);

    // Hide module cards
    if (role == UserRole.DISPATCHER) {
        busCard.setVisible(false); 
        driverCard.setVisible(false);
        routeCard.setVisible(false);
        tripCard.setVisible(false);
        passengerCard.setVisible(false);
        ticketCard.setVisible(false);
        maintenanceCard.setVisible(false);
        usersCard.setVisible(false);
        reportsCards.setVisible(false);
    } else if (role == UserRole.ACCOUNTANT) {
        busCard.setVisible(false);
        driverCard.setVisible(false);
        routeCard.setVisible(false);
        tripCard.setVisible(false);
        passengerCard.setVisible(false);
        ticketCard.setVisible(false);
        maintenanceCard.setVisible(false);
        usersCard.setVisible(false);
        reportsCards.setVisible(false);
        passengerCard.setVisible(false);
        maintenanceCard.setVisible(false);
        usersCard.setVisible(false);
    } else if (role == UserRole.MAINTENANCE_OFFICER) {
        busCard.setVisible(false);
        driverCard.setVisible(false);
        routeCard.setVisible(false);
        tripCard.setVisible(false);
        passengerCard.setVisible(false);
        ticketCard.setVisible(false);
        maintenanceCard.setVisible(false);
        usersCard.setVisible(false);
        reportsCards.setVisible(false);
    }

    // Refresh
    sidePanel.revalidate();
    sidePanel.repaint();
}
    
    private void addSideButton(String text,
        Runnable action) {
    JButton btn = makeSideBtn(text, false);
    btn.addActionListener(e -> action.run());
    sidePanel.add(btn);
    sidePanel.add(
        javax.swing.Box.createVerticalStrut(2));
}
    
    
    private JButton makeSideBtn(
        String text, boolean active) {
    JButton btn = new JButton(text);
    btn.setFont(new java.awt.Font(
        "Arial", java.awt.Font.PLAIN, 12));
    btn.setForeground(active ? 
        java.awt.Color.WHITE
        : new java.awt.Color(200, 218, 240));
    btn.setBackground(active
        ? new java.awt.Color(35, 77, 133)
        : new java.awt.Color(26, 62, 111));
    btn.setBorderPainted(false);
    btn.setFocusPainted(false);
    btn.setCursor(new java.awt.Cursor(
        java.awt.Cursor.HAND_CURSOR));
    btn.setMaximumSize(
        new java.awt.Dimension(210, 40));
    btn.setMinimumSize(
        new java.awt.Dimension(210, 40));
    btn.setPreferredSize(
        new java.awt.Dimension(210, 40));
    btn.setHorizontalAlignment(
        javax.swing.SwingConstants.LEFT);
    btn.setAlignmentX(
        java.awt.Component.LEFT_ALIGNMENT);
    btn.setBorder(
        javax.swing.BorderFactory
            .createEmptyBorder(0, 16, 0, 0));
    return btn;
}
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        sidePanel = new javax.swing.JPanel();
        btnDashboard = new javax.swing.JButton();
        btnBus = new javax.swing.JButton();
        btnDriver = new javax.swing.JButton();
        btnRoute = new javax.swing.JButton();
        btnTrip = new javax.swing.JButton();
        btnPassenger = new javax.swing.JButton();
        btnMaintenance = new javax.swing.JButton();
        btnUsers = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        btnTicket = new javax.swing.JButton();
        btnReports = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        lblWelcome = new javax.swing.JLabel();
        statBusPanel = new javax.swing.JPanel();
        lblBusCount = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        statDriverPanel = new javax.swing.JPanel();
        lblDriverCount = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        statTripPanel = new javax.swing.JPanel();
        lblTripCount = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        statRevenuePanel = new javax.swing.JPanel();
        lblRevenue = new javax.swing.JLabel();
        busCard = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        driverCard = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        routeCard = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        tripCard = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        passengerCard = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        ticketCard = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        maintenanceCard = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        usersCard = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        reportsCards = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Urban Bus Fleet - Admin Dashboard");

        mainPanel.setBackground(new java.awt.Color(240, 244, 248));
        mainPanel.setPreferredSize(new java.awt.Dimension(1100, 660));
        mainPanel.setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(0, 102, 102));
        headerPanel.setPreferredSize(new java.awt.Dimension(1100, 54));
        headerPanel.setLayout(new java.awt.BorderLayout());

        lblTitle.setFont(new java.awt.Font("Arial", 1, 15)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Urban Bus Fleet Management System");
        headerPanel.add(lblTitle, java.awt.BorderLayout.CENTER);

        lblUserName.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        lblUserName.setForeground(new java.awt.Color(160, 212, 212));
        lblUserName.setText("Welcome");
        headerPanel.add(lblUserName, java.awt.BorderLayout.LINE_END);

        mainPanel.add(headerPanel, java.awt.BorderLayout.PAGE_START);

        sidePanel.setBackground(new java.awt.Color(26, 62, 111));
        sidePanel.setPreferredSize(new java.awt.Dimension(210, 660));
        sidePanel.setLayout(null);

        btnDashboard.setBackground(new java.awt.Color(35, 77, 133));
        btnDashboard.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setText("Dashboard");
        btnDashboard.setBorderPainted(false);
        btnDashboard.setFocusPainted(false);
        btnDashboard.setPreferredSize(new java.awt.Dimension(210, 40));
        sidePanel.add(btnDashboard);
        btnDashboard.setBounds(0, 20, 200, 30);

        btnBus.setBackground(new java.awt.Color(26, 62, 111));
        btnBus.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnBus.setForeground(new java.awt.Color(200, 218, 240));
        btnBus.setText("Bus Management");
        btnBus.setBorderPainted(false);
        btnBus.setFocusPainted(false);
        btnBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBusActionPerformed(evt);
            }
        });
        sidePanel.add(btnBus);
        btnBus.setBounds(0, 70, 200, 30);

        btnDriver.setBackground(new java.awt.Color(26, 62, 111));
        btnDriver.setForeground(new java.awt.Color(200, 218, 240));
        btnDriver.setText("Driver Management");
        btnDriver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDriverActionPerformed(evt);
            }
        });
        sidePanel.add(btnDriver);
        btnDriver.setBounds(0, 120, 200, 30);

        btnRoute.setBackground(new java.awt.Color(26, 62, 111));
        btnRoute.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        btnRoute.setForeground(new java.awt.Color(200, 218, 240));
        btnRoute.setText("  Route Management");
        btnRoute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRouteActionPerformed(evt);
            }
        });
        sidePanel.add(btnRoute);
        btnRoute.setBounds(0, 170, 200, 30);

        btnTrip.setBackground(new java.awt.Color(26, 62, 111));
        btnTrip.setForeground(new java.awt.Color(200, 218, 240));
        btnTrip.setText("Trip Scheduling");
        btnTrip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTripActionPerformed(evt);
            }
        });
        sidePanel.add(btnTrip);
        btnTrip.setBounds(0, 220, 200, 30);

        btnPassenger.setBackground(new java.awt.Color(26, 62, 111));
        btnPassenger.setForeground(new java.awt.Color(200, 218, 240));
        btnPassenger.setText("Passengers");
        btnPassenger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPassengerActionPerformed(evt);
            }
        });
        sidePanel.add(btnPassenger);
        btnPassenger.setBounds(0, 270, 200, 30);

        btnMaintenance.setBackground(new java.awt.Color(26, 62, 111));
        btnMaintenance.setForeground(new java.awt.Color(200, 218, 240));
        btnMaintenance.setText("Maintenance");
        btnMaintenance.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMaintenanceActionPerformed(evt);
            }
        });
        sidePanel.add(btnMaintenance);
        btnMaintenance.setBounds(0, 370, 200, 30);

        btnUsers.setBackground(new java.awt.Color(26, 62, 111));
        btnUsers.setForeground(new java.awt.Color(200, 218, 240));
        btnUsers.setText("User Management");
        btnUsers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUsersActionPerformed(evt);
            }
        });
        sidePanel.add(btnUsers);
        btnUsers.setBounds(0, 420, 200, 30);

        btnLogout.setBackground(new java.awt.Color(26, 62, 111));
        btnLogout.setForeground(new java.awt.Color(244, 160, 160));
        btnLogout.setText("Logout");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        sidePanel.add(btnLogout);
        btnLogout.setBounds(50, 510, 80, 30);

        btnTicket.setBackground(new java.awt.Color(26, 62, 111));
        btnTicket.setForeground(new java.awt.Color(200, 218, 240));
        btnTicket.setText("Tickets");
        btnTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTicketActionPerformed(evt);
            }
        });
        sidePanel.add(btnTicket);
        btnTicket.setBounds(0, 320, 200, 30);

        btnReports.setBackground(new java.awt.Color(26, 62, 111));
        btnReports.setForeground(new java.awt.Color(200, 218, 240));
        btnReports.setText("Reports");
        btnReports.setBorderPainted(false);
        btnReports.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReportsActionPerformed(evt);
            }
        });
        sidePanel.add(btnReports);
        btnReports.setBounds(3, 470, 200, 30);

        mainPanel.add(sidePanel, java.awt.BorderLayout.LINE_START);

        contentPanel.setBackground(new java.awt.Color(240, 244, 248));
        contentPanel.setLayout(null);

        lblWelcome.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        lblWelcome.setForeground(new java.awt.Color(26, 62, 111));
        lblWelcome.setText("Welcome back!");
        contentPanel.add(lblWelcome);
        lblWelcome.setBounds(20, 20, 115, 19);

        statBusPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblBusCount.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        lblBusCount.setForeground(new java.awt.Color(26, 62, 111));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 11)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(136, 136, 136));
        jLabel9.setText("Total Buses");

        javax.swing.GroupLayout statBusPanelLayout = new javax.swing.GroupLayout(statBusPanel);
        statBusPanel.setLayout(statBusPanelLayout);
        statBusPanelLayout.setHorizontalGroup(
            statBusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statBusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblBusCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addContainerGap(77, Short.MAX_VALUE))
        );
        statBusPanelLayout.setVerticalGroup(
            statBusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statBusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statBusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(lblBusCount))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        contentPanel.add(statBusPanel);
        statBusPanel.setBounds(20, 60, 150, 110);

        statDriverPanel.setBackground(new java.awt.Color(255, 255, 255));
        statDriverPanel.setForeground(new java.awt.Color(136, 136, 136));

        lblDriverCount.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        lblDriverCount.setForeground(new java.awt.Color(26, 62, 111));

        jLabel10.setForeground(new java.awt.Color(136, 136, 136));
        jLabel10.setText("Driver");

        javax.swing.GroupLayout statDriverPanelLayout = new javax.swing.GroupLayout(statDriverPanel);
        statDriverPanel.setLayout(statDriverPanelLayout);
        statDriverPanelLayout.setHorizontalGroup(
            statDriverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statDriverPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDriverCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addContainerGap(105, Short.MAX_VALUE))
        );
        statDriverPanelLayout.setVerticalGroup(
            statDriverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statDriverPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statDriverPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(lblDriverCount))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        contentPanel.add(statDriverPanel);
        statDriverPanel.setBounds(210, 60, 150, 110);

        statTripPanel.setBackground(new java.awt.Color(255, 255, 255));

        lblTripCount.setFont(new java.awt.Font("Arial", 0, 22)); // NOI18N
        lblTripCount.setForeground(new java.awt.Color(26, 62, 111));

        jLabel11.setForeground(new java.awt.Color(136, 136, 136));
        jLabel11.setText("Trips");

        javax.swing.GroupLayout statTripPanelLayout = new javax.swing.GroupLayout(statTripPanel);
        statTripPanel.setLayout(statTripPanelLayout);
        statTripPanelLayout.setHorizontalGroup(
            statTripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statTripPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTripCount)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addContainerGap(121, Short.MAX_VALUE))
        );
        statTripPanelLayout.setVerticalGroup(
            statTripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statTripPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(statTripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(lblTripCount))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        contentPanel.add(statTripPanel);
        statTripPanel.setBounds(400, 60, 160, 110);

        statRevenuePanel.setBackground(new java.awt.Color(255, 255, 255));

        lblRevenue.setFont(new java.awt.Font("Arial", 1, 22)); // NOI18N
        lblRevenue.setForeground(new java.awt.Color(26, 62, 111));
        lblRevenue.setText("—");

        javax.swing.GroupLayout statRevenuePanelLayout = new javax.swing.GroupLayout(statRevenuePanel);
        statRevenuePanel.setLayout(statRevenuePanelLayout);
        statRevenuePanelLayout.setHorizontalGroup(
            statRevenuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statRevenuePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRevenue)
                .addContainerGap(118, Short.MAX_VALUE))
        );
        statRevenuePanelLayout.setVerticalGroup(
            statRevenuePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statRevenuePanelLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblRevenue)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        contentPanel.add(statRevenuePanel);
        statRevenuePanel.setBounds(590, 60, 150, 110);

        busCard.setBackground(new java.awt.Color(230, 241, 251));
        busCard.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(24, 95, 165));
        jLabel1.setText("Bus Management");
        busCard.add(jLabel1);
        jLabel1.setBounds(20, 40, 99, 15);

        contentPanel.add(busCard);
        busCard.setBounds(20, 220, 150, 100);

        driverCard.setBackground(new java.awt.Color(225, 245, 238));
        driverCard.setForeground(new java.awt.Color(15, 110, 86));
        driverCard.setLayout(null);

        jLabel2.setForeground(new java.awt.Color(15, 110, 86));
        jLabel2.setText("Driver Management");
        driverCard.add(jLabel2);
        jLabel2.setBounds(30, 40, 94, 14);

        contentPanel.add(driverCard);
        driverCard.setBounds(210, 218, 150, 100);

        routeCard.setBackground(new java.awt.Color(238, 237, 254));
        routeCard.setLayout(null);

        jLabel3.setForeground(new java.awt.Color(83, 74, 183));
        jLabel3.setText("Route Management");
        routeCard.add(jLabel3);
        jLabel3.setBounds(30, 40, 94, 14);

        contentPanel.add(routeCard);
        routeCard.setBounds(400, 219, 150, 90);

        tripCard.setBackground(new java.awt.Color(250, 238, 218));
        tripCard.setLayout(null);

        jLabel4.setForeground(new java.awt.Color(133, 79, 11));
        jLabel4.setText("Trip Scheduling");
        tripCard.add(jLabel4);
        jLabel4.setBounds(40, 40, 72, 14);

        contentPanel.add(tripCard);
        tripCard.setBounds(590, 220, 150, 90);

        passengerCard.setBackground(new java.awt.Color(234, 243, 222));
        passengerCard.setLayout(null);

        jLabel5.setForeground(new java.awt.Color(59, 109, 17));
        jLabel5.setText("Passengers");
        passengerCard.add(jLabel5);
        jLabel5.setBounds(40, 40, 55, 14);

        contentPanel.add(passengerCard);
        passengerCard.setBounds(20, 360, 150, 90);

        ticketCard.setBackground(new java.awt.Color(250, 236, 231));
        ticketCard.setLayout(null);

        jLabel6.setForeground(new java.awt.Color(153, 60, 29));
        jLabel6.setText("Ticket");
        ticketCard.add(jLabel6);
        jLabel6.setBounds(60, 40, 28, 14);

        contentPanel.add(ticketCard);
        ticketCard.setBounds(210, 360, 150, 90);

        maintenanceCard.setBackground(new java.awt.Color(241, 239, 232));
        maintenanceCard.setLayout(null);

        jLabel7.setForeground(new java.awt.Color(95, 94, 90));
        jLabel7.setText("Maintenance");
        maintenanceCard.add(jLabel7);
        jLabel7.setBounds(40, 40, 61, 14);

        contentPanel.add(maintenanceCard);
        maintenanceCard.setBounds(400, 360, 150, 90);

        usersCard.setBackground(new java.awt.Color(251, 234, 240));
        usersCard.setLayout(null);

        jLabel8.setForeground(new java.awt.Color(153, 53, 86));
        jLabel8.setText("User Management");
        usersCard.add(jLabel8);
        jLabel8.setBounds(30, 40, 87, 14);

        contentPanel.add(usersCard);
        usersCard.setBounds(600, 360, 140, 90);

        reportsCards.setBackground(new java.awt.Color(234, 240, 248));
        reportsCards.setLayout(null);

        jLabel12.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(26, 62, 111));
        jLabel12.setText("Reports");
        reportsCards.add(jLabel12);
        jLabel12.setBounds(50, 40, 45, 15);

        contentPanel.add(reportsCards);
        reportsCards.setBounds(20, 490, 150, 90);

        mainPanel.add(contentPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBusActionPerformed
        
        dispose();
        new BusManagementForm(currentUser).setVisible(true);
    }//GEN-LAST:event_btnBusActionPerformed

    private void btnDriverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDriverActionPerformed
        dispose();
        new DriverManagementForm(currentUser).setVisible(true);
    }//GEN-LAST:event_btnDriverActionPerformed

    private void btnRouteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRouteActionPerformed
        dispose();
        new RouteManagementForm(currentUser)
        .setVisible(true);
    }//GEN-LAST:event_btnRouteActionPerformed

    private void btnTripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTripActionPerformed
        dispose();
        new TripManagementForm(currentUser).setVisible(true);    
    }//GEN-LAST:event_btnTripActionPerformed

    private void btnPassengerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPassengerActionPerformed
        dispose();
        new PassengerManagementForm(currentUser).setVisible(true);
    }//GEN-LAST:event_btnPassengerActionPerformed

    private void btnTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTicketActionPerformed
        dispose();
        new TicketsManagementForm(currentUser).setVisible(true);
    }//GEN-LAST:event_btnTicketActionPerformed

    private void btnMaintenanceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMaintenanceActionPerformed
        dispose();
        new MaintenanceManagementForm(currentUser).setVisible(true);
    }//GEN-LAST:event_btnMaintenanceActionPerformed

    private void btnUsersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUsersActionPerformed
       dispose();
       new UserManagementForm(currentUser).setVisible(true);
    }//GEN-LAST:event_btnUsersActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this,
        "Are you sure you want to logout?",
        "Logout", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        dispose();
        new LoginForm().setVisible(true);
    }
    }//GEN-LAST:event_btnLogoutActionPerformed

    private void btnReportsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReportsActionPerformed
        dispose();
        new ReportsForm(currentUser).setVisible(true);
    }//GEN-LAST:event_btnReportsActionPerformed

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
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
         try {
        for (javax.swing.UIManager.LookAndFeelInfo info :
                javax.swing.UIManager
                .getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(
                    info.getClassName());
                break;
            }
        }
    } catch (Exception ex) {
        java.util.logging.Logger.getLogger(
            AdminDashboard.class.getName())
            .log(java.util.logging.Level.SEVERE,
                null, ex);
    }
    java.awt.EventQueue.invokeLater(new Runnable() {
        public void run() {
            // Test user for running directly
            com.urbanBus.server.model.User testUser =
                new com.urbanBus.server.model.User();
            testUser.setFullName("System Administrator");
            testUser.setRole(
                com.urbanBus.server.model.UserRole.ADMIN);
            new AdminDashboard(testUser).setVisible(true);
            
            AdminDashboard dashboard = new AdminDashboard(testUser);
            dashboard.setLocationRelativeTo(null); // ← here too
        
        }
    });
    
   
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBus;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnDriver;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMaintenance;
    private javax.swing.JButton btnPassenger;
    private javax.swing.JButton btnReports;
    private javax.swing.JButton btnRoute;
    private javax.swing.JButton btnTicket;
    private javax.swing.JButton btnTrip;
    private javax.swing.JButton btnUsers;
    private javax.swing.JPanel busCard;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel driverCard;
    private javax.swing.JPanel headerPanel;
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
    private javax.swing.JLabel lblBusCount;
    private javax.swing.JLabel lblDriverCount;
    private javax.swing.JLabel lblRevenue;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTripCount;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JLabel lblWelcome;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel maintenanceCard;
    private javax.swing.JPanel passengerCard;
    private javax.swing.JPanel reportsCards;
    private javax.swing.JPanel routeCard;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JPanel statBusPanel;
    private javax.swing.JPanel statDriverPanel;
    private javax.swing.JPanel statRevenuePanel;
    private javax.swing.JPanel statTripPanel;
    private javax.swing.JPanel ticketCard;
    private javax.swing.JPanel tripCard;
    private javax.swing.JPanel usersCard;
    // End of variables declaration//GEN-END:variables
}
