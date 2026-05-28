/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import com.urbanBus.client.controller.RMIClient;
import com.urbanBus.server.model.Bus;
import com.urbanBus.server.model.Ticket;
import com.urbanBus.server.model.Trip;
import com.urbanBus.server.model.User;
import com.urbanBus.server.model.UserRole;
import com.urbanBus.server.service.BusService;
import com.urbanBus.server.service.TicketService;
import com.urbanBus.server.service.TripService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author aimeb
 */
public class ReportsForm extends javax.swing.JFrame {
    
    private User currentUser;
    private BusService busService;
    private TripService tripService;
    private TicketService ticketService;
    private DefaultTableModel tableModel;
    private String currentReport = "";

    /**
     * Creates new form ReportsForm
     */
    public ReportsForm(User user) {
    this.currentUser = user;
    try {
        this.busService =
            RMIClient.getBusService();
        this.tripService =
            RMIClient.getTripService();
        this.ticketService =
            RMIClient.getTicketService();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null,
            "Cannot connect to server.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    initComponents();
    getContentPane().setBackground(UIUtils.BG_PAGE);
    UIUtils.styleHeaderButton(btnBack);
    jPanel3.setBackground(UIUtils.BG_PAGE);
    buildReportsPanel();
    setupTable();
    setLocationRelativeTo(null);
}
    
    
    
    private void buildReportsPanel() {
    jPanel2.removeAll();
    jPanel2.setLayout(new java.awt.GridBagLayout());
    jPanel2.setBackground(java.awt.Color.WHITE);
    jPanel2.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createLineBorder(UIUtils.BORDER, 1, true),
        javax.swing.BorderFactory.createEmptyBorder(20, 16, 20, 16)
    ));

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.gridx = 0;
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1.0;

    // ── Section: REPORTS ──────────────────────────────
    gbc.gridy = 0;
    gbc.insets = new java.awt.Insets(0, 0, 12, 0);
    jPanel2.add(UIUtils.makeSectionLabel("REPORTS"), gbc);

    java.awt.Dimension reportBtn = new java.awt.Dimension(0, 42);

    gbc.gridy = 1;
    gbc.insets = new java.awt.Insets(0, 0, 8, 0);
    btnFleetReport.setPreferredSize(reportBtn);
    btnFleetReport.setText("Fleet Status Report");
    UIUtils.stylePrimaryButton(btnFleetReport);
    jPanel2.add(btnFleetReport, gbc);

    gbc.gridy = 2;
    btnTripReport.setPreferredSize(reportBtn);
    btnTripReport.setText("Trip Report");
    UIUtils.stylePrimaryButton(btnTripReport);
    jPanel2.add(btnTripReport, gbc);

    gbc.gridy = 3;
    gbc.insets = new java.awt.Insets(0, 0, 20, 0);
    btnRevenueReport.setPreferredSize(reportBtn);
    btnRevenueReport.setText("Revenue Report");
    UIUtils.stylePrimaryButton(btnRevenueReport);
    jPanel2.add(btnRevenueReport, gbc);

    // ── Divider ───────────────────────────────────────
    gbc.gridy = 4;
    gbc.insets = new java.awt.Insets(0, 0, 20, 0);
    javax.swing.JSeparator sep = new javax.swing.JSeparator();
    sep.setForeground(UIUtils.BORDER);
    jPanel2.add(sep, gbc);

    // ── Section: EXPORT ───────────────────────────────
    gbc.gridy = 5;
    gbc.insets = new java.awt.Insets(0, 0, 12, 0);
    jPanel2.add(UIUtils.makeSectionLabel("EXPORT & PRINT"), gbc);

    java.awt.Dimension exportBtn = new java.awt.Dimension(0, 38);

    gbc.gridy = 6;
    gbc.insets = new java.awt.Insets(0, 0, 8, 0);
    btnExportPDF.setPreferredSize(exportBtn);
    btnExportPDF.setText("Export as PDF");
    UIUtils.styleOutlineButton(btnExportPDF);
    jPanel2.add(btnExportPDF, gbc);

    gbc.gridy = 7;
    btnExportExcel.setPreferredSize(exportBtn);
    btnExportExcel.setText("Export as Excel");
    UIUtils.styleOutlineButton(btnExportExcel);
    jPanel2.add(btnExportExcel, gbc);

    gbc.gridy = 8;
    gbc.insets = new java.awt.Insets(0, 0, 0, 0);
    btnPrint.setPreferredSize(exportBtn);
    btnPrint.setText("Print Report");
    UIUtils.styleOutlineButton(btnPrint);
    jPanel2.add(btnPrint, gbc);

    // ── Spacer ────────────────────────────────────────
    gbc.gridy = 9;
    gbc.weighty = 1.0;
    gbc.fill = java.awt.GridBagConstraints.VERTICAL;
    jPanel2.add(new javax.swing.JPanel() {{ setOpaque(false); }}, gbc);

    // ── Report title label styling (right panel) ──────
    lblReportTitle.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));
    lblReportTitle.setForeground(UIUtils.TEAL);

    jPanel2.revalidate();
    jPanel2.repaint();
}
    
    private void setupTable() {
     tableModel = new DefaultTableModel() {
        public boolean isCellEditable(int row, int col) { return false; }
    };
    reportTable.setModel(tableModel);
    UIUtils.styleTable(reportTable);
    UIUtils.styleScrollPane(jScrollPane1);
}
    
    
    private void loadFleetReport() {
    currentReport = "FLEET";
    lblReportTitle.setText(
        "Fleet Status Report");
    tableModel.setRowCount(0);
    tableModel.setColumnCount(0);

    tableModel.addColumn("Plate Number");
    tableModel.addColumn("Brand");
    tableModel.addColumn("Capacity");
    tableModel.addColumn("Fuel Type");
    tableModel.addColumn("Mileage");
    tableModel.addColumn("Status");

    try {
        List<Bus> buses =
            busService.findAllBuses();
        for (Bus bus : buses) {
            tableModel.addRow(new Object[]{
                bus.getPlateNumber(),
                bus.getBrand(),
                bus.getCapacity(),
                bus.getFuelType(),
                bus.getMileage(),
                bus.getStatus()
            });
        }
        JOptionPane.showMessageDialog(this,
            "Fleet report loaded. "
            + buses.size() + " buses found.",
            "Report Ready",
            JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    
    private void loadTripReport() {
    currentReport = "TRIP";
    lblReportTitle.setText("Trip Report");
    tableModel.setRowCount(0);
    tableModel.setColumnCount(0);

    tableModel.addColumn("Route");
    tableModel.addColumn("Bus");
    tableModel.addColumn("Driver");
    tableModel.addColumn("Departure");
    tableModel.addColumn("Status");

    try {
        List<Trip> trips =
            tripService.findAllTrips();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        for (Trip trip : trips) {
            tableModel.addRow(new Object[]{
                trip.getRoute().getRouteName(),
                trip.getBus().getPlateNumber(),
                trip.getDriver().getFullName(),
                sdf.format(trip.getDepartureTime()),
                trip.getStatus()
            });
        }
        JOptionPane.showMessageDialog(this,
            "Trip report loaded. "
            + trips.size() + " trips found.",
            "Report Ready",
            JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    
    
    private void loadRevenueReport() {
    currentReport = "REVENUE";
    lblReportTitle.setText("Revenue Report");
    tableModel.setRowCount(0);
    tableModel.setColumnCount(0);

    tableModel.addColumn("Route");
    tableModel.addColumn("Passenger");
    tableModel.addColumn("Seat No");
    tableModel.addColumn("Amount Paid");
    tableModel.addColumn("Issued Date");

    try {
        List<Ticket> tickets =
            ticketService.findAllTickets();
        SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");
        double totalRevenue = 0;
        for (Ticket ticket : tickets) {
            tableModel.addRow(new Object[]{
                ticket.getTrip().getRoute()
                    .getRouteName(),
                ticket.getPassenger().getFullName(),
                ticket.getSeatNumber(),
                ticket.getAmountPaid(),
                sdf.format(ticket.getIssuedDate())
            });
            totalRevenue += ticket.getAmountPaid();
        }
        // Add total row
        tableModel.addRow(new Object[]{
            "TOTAL", "", "", totalRevenue, ""
        });
        JOptionPane.showMessageDialog(this,
            "Revenue report loaded.\n"
            + "Total Revenue: "
            + String.format("%.2f", totalRevenue)
            + " RWF",
            "Report Ready",
            JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnFleetReport = new javax.swing.JButton();
        btnTripReport = new javax.swing.JButton();
        btnRevenueReport = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btnExportPDF = new javax.swing.JButton();
        btnExportExcel = new javax.swing.JButton();
        btnPrint = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        reportTable = new javax.swing.JTable();
        lblReportTitle = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel1.setText("Reports & Export");

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
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(53, 53, 53))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnBack))
                .addGap(16, 16, 16))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        btnFleetReport.setText("Fleet  Status Report");
        btnFleetReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFleetReportActionPerformed(evt);
            }
        });

        btnTripReport.setText("Trip Report");
        btnTripReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTripReportActionPerformed(evt);
            }
        });

        btnRevenueReport.setText("RevenueReport");
        btnRevenueReport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRevenueReportActionPerformed(evt);
            }
        });

        jLabel2.setText("Export As: ");

        btnExportPDF.setText("Export PDF");
        btnExportPDF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportPDFActionPerformed(evt);
            }
        });

        btnExportExcel.setText("Export Excel");
        btnExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportExcelActionPerformed(evt);
            }
        });

        btnPrint.setBackground(new java.awt.Color(26, 62, 111));
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setText("🖨 Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnRevenueReport, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTripReport, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFleetReport, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(75, 75, 75))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btnExportPDF)
                        .addGap(18, 18, 18)
                        .addComponent(btnExportExcel))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnFleetReport)
                .addGap(18, 18, 18)
                .addComponent(btnTripReport)
                .addGap(18, 18, 18)
                .addComponent(btnRevenueReport)
                .addGap(75, 75, 75)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(btnExportPDF)
                    .addComponent(btnExportExcel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jPanel3.setBackground(new java.awt.Color(245, 247, 250));

        reportTable.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(reportTable);

        lblReportTitle.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        lblReportTitle.setText("report table");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(270, 270, 270)
                .addComponent(lblReportTitle)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblReportTitle)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void btnTripReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTripReportActionPerformed
        loadTripReport();
    }//GEN-LAST:event_btnTripReportActionPerformed

    private void btnFleetReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFleetReportActionPerformed
        loadFleetReport();
    }//GEN-LAST:event_btnFleetReportActionPerformed

    private void btnExportPDFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportPDFActionPerformed
         if (currentReport.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Please load a report first.",
            "Warning",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save PDF Report");
    fileChooser.setSelectedFile(
        new java.io.File(
            currentReport + "_Report.pdf"));
    int result = fileChooser.showSaveDialog(this);
    if (result != JFileChooser.APPROVE_OPTION)
        return;

    String filePath = fileChooser
        .getSelectedFile().getAbsolutePath();
    if (!filePath.endsWith(".pdf"))
        filePath += ".pdf";

    try {
        Document document =
            new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document,
            new FileOutputStream(filePath));
        document.open();

        // Title
        Font titleFont = FontFactory.getFont(
            FontFactory.HELVETICA_BOLD, 16);
        Paragraph title = new Paragraph(
            lblReportTitle.getText(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Date
        Font dateFont = FontFactory.getFont(
            FontFactory.HELVETICA, 10);
        Paragraph date = new Paragraph(
            "Generated: "
            + new SimpleDateFormat(
                "yyyy-MM-dd HH:mm")
                .format(new java.util.Date()),
            dateFont);
        date.setAlignment(Element.ALIGN_CENTER);
        document.add(date);
        document.add(
            new Paragraph(" "));

        // Table
        int cols = tableModel.getColumnCount();
        PdfPTable table = new PdfPTable(cols);
        table.setWidthPercentage(100);

        // Headers
        com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10,
        com.itextpdf.text.BaseColor.WHITE);
        for (int i = 0; i < cols; i++) {
            PdfPCell cell = new PdfPCell(
                new Phrase(
                    tableModel.getColumnName(i),
                    headerFont));
            cell.setBackgroundColor(
                new com.itextpdf.text.BaseColor(
                    26, 62, 111));
            cell.setHorizontalAlignment(
                Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        // Data rows
        com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
        for (int row = 0;
                row < tableModel.getRowCount();
                row++) {
            for (int col = 0; col < cols; col++) {
                Object val =
                    tableModel.getValueAt(row, col);
                PdfPCell cell = new PdfPCell(
                    new Phrase(
                        val != null ?
                            val.toString() : "",
                        dataFont));
                cell.setPadding(4);
                if (row % 2 == 0) {
                    cell.setBackgroundColor(
                        new com.itextpdf.text
                            .BaseColor(
                                240, 240, 240));
                }
                table.addCell(cell);
            }
        }
        document.add(table);
        document.close();

        JOptionPane.showMessageDialog(this,
            "PDF exported successfully!\n"
            + filePath,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error exporting PDF: "
            + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnExportPDFActionPerformed

    private void btnExportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportExcelActionPerformed
        if (currentReport.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Please load a report first.",
            "Warning",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle(
        "Save Excel Report");
    fileChooser.setSelectedFile(
        new java.io.File(
            currentReport + "_Report.xls"));
    int result = fileChooser.showSaveDialog(this);
    if (result != JFileChooser.APPROVE_OPTION)
        return;

    String filePath = fileChooser
        .getSelectedFile().getAbsolutePath();
    if (!filePath.endsWith(".xls"))
        filePath += ".xls";

    try {
        HSSFWorkbook workbook = new HSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet =
            workbook.createSheet(
                lblReportTitle.getText());

        // Header style
        CellStyle headerStyle =
            workbook.createCellStyle();
        headerStyle.setFillForegroundColor(
            IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(
            org.apache.poi.ss.usermodel
                .FillPatternType.SOLID_FOREGROUND);
        org.apache.poi.ss.usermodel.Font
                headerFont =
            workbook.createFont();
        headerFont.setBold(true);
        headerFont.setColor(
            IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        // Header row
        Row headerRow = sheet.createRow(0);
        for (int col = 0;
                col < tableModel.getColumnCount();
                col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(
                tableModel.getColumnName(col));
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(col);
        }

        // Data rows
        for (int row = 0;
                row < tableModel.getRowCount();
                row++) {
            Row dataRow =
                sheet.createRow(row + 1);
            for (int col = 0;
                    col < tableModel
                        .getColumnCount();
                    col++) {
                Object val =
                    tableModel.getValueAt(row, col);
                Cell cell =
                    dataRow.createCell(col);
                if (val != null) {
                    cell.setCellValue(
                        val.toString());
                }
            }
        }

        // Auto size columns
        for (int col = 0;
                col < tableModel.getColumnCount();
                col++) {
            sheet.autoSizeColumn(col);
        }

        // Save file
        FileOutputStream fos =
            new FileOutputStream(filePath);
        workbook.write(fos);
        fos.close();
        workbook.close();

        JOptionPane.showMessageDialog(this,
            "Excel exported successfully!\n"
            + filePath,
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error exporting Excel: "
            + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnExportExcelActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        dispose();
        new AdminDashboard(currentUser).setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnRevenueReportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRevenueReportActionPerformed
        loadRevenueReport();
    }//GEN-LAST:event_btnRevenueReportActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        if (currentReport.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Please load a report first.",
            "Warning",
            JOptionPane.WARNING_MESSAGE);
        return;
    }
    try {
        boolean printed =
            reportTable.print(
                javax.swing.JTable
                    .PrintMode.FIT_WIDTH);
        if (printed) {
            JOptionPane.showMessageDialog(this,
                "Report printed successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this,
            "Error printing: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnPrintActionPerformed

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
            java.util.logging.Logger.getLogger(ReportsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReportsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReportsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReportsForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                User testUser = new User();
                testUser.setFullName("System Administrator");
                testUser.setRole(UserRole.ADMIN);
                new ReportsForm(testUser).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnExportExcel;
    private javax.swing.JButton btnExportPDF;
    private javax.swing.JButton btnFleetReport;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRevenueReport;
    private javax.swing.JButton btnTripReport;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblReportTitle;
    private javax.swing.JTable reportTable;
    // End of variables declaration//GEN-END:variables
}
