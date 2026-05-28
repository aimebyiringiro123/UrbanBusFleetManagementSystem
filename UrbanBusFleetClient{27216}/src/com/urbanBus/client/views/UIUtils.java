/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.views;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 *
 * @author aimeb
 */
public class UIUtils {

    // ── Colors ───────────────────────────────────────────────
    public static final Color TEAL        = new Color(0, 102, 102);
    public static final Color TEAL_LIGHT  = new Color(0, 128, 128);
    public static final Color BG_PAGE     = new Color(244, 246, 249);
    public static final Color BG_CARD     = Color.WHITE;
    public static final Color BG_ROW_ALT  = new Color(249, 255, 255);
    public static final Color TEXT_DARK   = new Color(30, 30, 30);
    public static final Color TEXT_GRAY   = new Color(120, 120, 120);
    public static final Color BORDER      = new Color(221, 225, 232);
    public static final Color RED         = new Color(192, 57, 43);

    // ── Fonts ────────────────────────────────────────────────
    public static final Font FONT_LABEL   = new Font("Arial", Font.PLAIN, 12);
    public static final Font FONT_INPUT   = new Font("Arial", Font.PLAIN, 13);
    public static final Font FONT_BUTTON  = new Font("Arial", Font.BOLD,  13);
    public static final Font FONT_TITLE   = new Font("Arial", Font.BOLD,  16);
    public static final Font FONT_SECTION = new Font("Arial", Font.BOLD,  11);

    // ── Style a text field ───────────────────────────────────
    public static void styleField(JTextField f) {
        f.setFont(FONT_INPUT);
        f.setForeground(TEXT_DARK);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        f.setBackground(Color.WHITE);
        // Highlight border on focus
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TEAL, 2, true),
                    BorderFactory.createEmptyBorder(5, 9, 5, 9)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER, 1, true),
                    BorderFactory.createEmptyBorder(6, 10, 6, 10)
                ));
            }
        });
    }

    // ── Style a combo box ────────────────────────────────────
    public static void styleCombo(JComboBox cb) {
        cb.setFont(FONT_INPUT);
        cb.setBackground(Color.WHITE);
        cb.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    // ── Primary button (teal filled) ─────────────────────────
    public static void stylePrimaryButton(JButton btn) {
    btn.setFont(FONT_BUTTON);
    btn.setBackground(TEAL);
    btn.setForeground(Color.WHITE);
    btn.setOpaque(true);                    
    btn.setContentAreaFilled(true);         
    btn.setFocusPainted(false);
    btn.setBorderPainted(false);
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(TEAL_LIGHT); }
        public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(TEAL); }
    });
}

    // ── Outline button (teal border, white bg) ───────────────
    public static void styleOutlineButton(JButton btn) {
    btn.setFont(FONT_BUTTON);
    btn.setBackground(Color.WHITE);
    btn.setForeground(TEAL);
    btn.setOpaque(true);                   
    btn.setContentAreaFilled(true);        
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createLineBorder(TEAL, 2, true));
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(0, 102, 102, 20)); }
        public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(Color.WHITE); }
    });
}

    // ── Danger button (red outline) ──────────────────────────
    public static void styleDangerButton(JButton btn) {
    btn.setFont(FONT_BUTTON);
    btn.setBackground(Color.WHITE);
    btn.setForeground(RED);
    btn.setOpaque(true);                    
    btn.setContentAreaFilled(true);         
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createLineBorder(RED, 2, true));
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(192, 57, 43, 20)); }
        public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(Color.WHITE); }
    });
}

    // ── Style the JTable ─────────────────────────────────────
    public static void styleTable(JTable table) {
    table.setFont(new Font("Arial", Font.PLAIN, 13));
    table.setRowHeight(36);
    table.setShowVerticalLines(false);
    table.setShowHorizontalLines(true);
    table.setGridColor(new Color(240, 240, 240));
    table.setSelectionBackground(new Color(224, 242, 242));
    table.setSelectionForeground(TEXT_DARK);
    table.setIntercellSpacing(new Dimension(0, 0));
    table.setFillsViewportHeight(true);
    table.setBackground(Color.WHITE);

    // Header
    JTableHeader header = table.getTableHeader();
    header.setFont(new Font("Arial", Font.BOLD, 11));
    header.setBackground(new Color(244, 246, 249));
    header.setForeground(new Color(100, 100, 100));
    header.setPreferredSize(new Dimension(header.getWidth(), 40));
    header.setReorderingAllowed(false);
    header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER));

    // Force header renderer on every column
    for (int i = 0; i < table.getColumnCount(); i++) {
        table.getColumnModel().getColumn(i).setHeaderRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                    JLabel lbl = new JLabel(val != null ? val.toString() : "");
                    lbl.setFont(new Font("Arial", Font.BOLD, 11));
                    lbl.setForeground(new Color(100, 100, 100));
                    lbl.setBackground(new Color(244, 246, 249));
                    lbl.setOpaque(true);
                    lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER),
                        BorderFactory.createEmptyBorder(0, 12, 0, 12)
                    ));
                    return lbl;
                }
            }
        );
    }

    // Alternating rows
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(
                JTable t, Object val, boolean sel, boolean foc, int row, int col) {
            super.getTableCellRendererComponent(t, val, sel, foc, row, col);
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            if (!sel) {
                setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 252, 252));
                setForeground(TEXT_DARK);
            }
            return this;
        }
    });
}

    // ── Status badge renderer (colored pill in table) ────────
    public static TableCellRenderer statusRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
                panel.setBackground(row % 2 == 0 ? Color.WHITE : BG_ROW_ALT);
                if (sel) panel.setBackground(new Color(224, 242, 242));

                JLabel badge = new JLabel(val != null ? val.toString() : "");
                badge.setFont(new Font("Arial", Font.BOLD, 11));
                badge.setOpaque(true);
                badge.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

                String status = val != null ? val.toString() : "";
                switch (status) {
                    case "ACTIVE":
                        badge.setBackground(new Color(224, 245, 236));
                        badge.setForeground(new Color(10, 124, 74));
                        break;
                    case "UNDER_MAINTENANCE":
                        badge.setBackground(new Color(255, 243, 224));
                        badge.setForeground(new Color(180, 83, 9));
                        break;
                    case "RETIRED":
                        badge.setBackground(new Color(240, 240, 240));
                        badge.setForeground(new Color(100, 100, 100));
                        break;
                    case "AVAILABLE":
                        badge.setBackground(new Color(224, 245, 236));
                        badge.setForeground(new Color(10, 124, 74));
                        break;
                    case "ON_TRIP":
                        badge.setBackground(new Color(235, 234, 255));
                        badge.setForeground(new Color(83, 74, 183));
                        break;
                    case "SUSPENDED":
                        badge.setBackground(new Color(253, 235, 235));
                        badge.setForeground(new Color(163, 45, 45));
                        break;
                    case "SCHEDULED":
                        badge.setBackground(new Color(235, 234, 255));
                        badge.setForeground(new Color(83, 74, 183));
                        break;
                    case "ONGOING":
                        badge.setBackground(new Color(224, 245, 236));
                        badge.setForeground(new Color(10, 124, 74));
                        break;
                    case "COMPLETED":
                        badge.setBackground(new Color(240, 240, 240));
                        badge.setForeground(new Color(80, 80, 80));
                        break;
                    case "CANCELLED":
                        badge.setBackground(new Color(253, 235, 235));
                        badge.setForeground(new Color(163, 45, 45));
                        break;
                    default:
                        badge.setBackground(new Color(240, 240, 240));
                        badge.setForeground(new Color(80, 80, 80));
                }
                panel.add(badge);
                return panel;
            }
        };
    }

    // ── Card panel (white, rounded look) ─────────────────────
    public static JPanel makeCard() {
        JPanel p = new JPanel();
        p.setBackground(Color.WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER, 1, true),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        return p;
    }

    // ── Section label (e.g. "BUS DETAILS") ──────────────────
    public static JLabel makeSectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.BOLD, 10));
        lbl.setForeground(TEAL);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return lbl;
    }
    
    public static void styleHeaderButton(JButton btn) {
    btn.setFont(new Font("Arial", Font.BOLD, 13));
    btn.setForeground(Color.GRAY);
    btn.setBackground(new Color(255, 255, 255, 0)); // transparent
    btn.setOpaque(false);
    btn.setContentAreaFilled(false);
    btn.setFocusPainted(false);
    btn.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.WHITE, 1, true),
        BorderFactory.createEmptyBorder(4, 14, 4, 14)
    ));
    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    btn.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseEntered(java.awt.event.MouseEvent e) {
            btn.setContentAreaFilled(true);
            btn.setBackground(new Color(255, 255, 255, 40));
        }
        public void mouseExited(java.awt.event.MouseEvent e) {
            btn.setContentAreaFilled(false);
        }
    });
}
    
    public static void setPlaceholder(JTextField field, String placeholder) {
    field.setText(placeholder);
    field.setForeground(new Color(180, 180, 180));

    field.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
            if (field.getText().equals(placeholder)) {
                field.setText("");
                field.setForeground(TEXT_DARK);
            }
        }
        public void focusLost(FocusEvent e) {
            if (field.getText().trim().isEmpty()) {
                field.setText(placeholder);
                field.setForeground(new Color(180, 180, 180));
            }
        }
    });
}
    
    public static void styleScrollPane(JScrollPane sp) {
    sp.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
    sp.getViewport().setBackground(Color.WHITE);
}
}
