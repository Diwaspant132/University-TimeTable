package timetable;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


class FacultyPanel extends JPanel {

    private final MainApp app;
    private JTextField subField, facField, roomField;
    private JComboBox<String> deptCombo, semCombo, dayCombo, slotCombo;
    private DefaultTableModel schedModel;
    private JLabel statusLbl;

    private static final String[] DAYS  = {"Monday","Tuesday","Wednesday","Thursday","Friday"};
    private static final String[] SLOTS = {
        "08:00 - 09:00","09:00 - 10:00","10:00 - 11:00","11:00 - 12:00",
        "12:00 - 13:00","13:00 - 14:00","14:00 - 15:00","15:00 - 16:00"
    };
    private static final String[] DEPTS = {"Computer Science","Electronics"};
    private static final String[] SEMS  = {"Semester 1","Semester 2","Semester 3","Semester 4"};

    FacultyPanel(MainApp app) {
        this.app = app;
        setOpaque(false);
        setLayout(new BorderLayout());
        buildUI();
    }

    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(20, 25, 45));
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        RadialGradientPaint rp1 = new RadialGradientPaint(
            new Point(getWidth(), getHeight()), getWidth()*0.6f,
            new float[]{0.0f, 1.0f},
            new Color[]{new Color(236, 72, 153, 60), new Color(20, 25, 45, 0)}
        );
        g2.setPaint(rp1);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        g2.setColor(new Color(255, 255, 255, 3));
        for(int i=0; i<getWidth(); i+=40) g2.drawLine(i,0,i,getHeight());
        for(int i=0; i<getHeight(); i+=40) g2.drawLine(0,i,getWidth(),i);
    }

    private void buildUI() {
        // ── Top navigation bar ────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout(12, 0));
        topBar.setOpaque(false);
        topBar.setBorder(new EmptyBorder(25, 40, 10, 40));

        JButton backBtn = new JButton("\u2190 Back to Home") {
            boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { hov=false; repaint(); }
            });
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hov) g2.setColor(new Color(236, 72, 153, 100));
                else     g2.setColor(new Color(255, 255, 255, 10));
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 16, 16);
                super.paintComponent(g);
            }
        };
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false); backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setPreferredSize(new Dimension(140, 36));
        backBtn.addActionListener(e -> app.showCard("HOME"));

        JLabel title = new JLabel("Faculty Management Portal", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JPanel spacer = new JPanel(); spacer.setOpaque(false);
        spacer.setPreferredSize(new Dimension(140, 36));

        topBar.add(backBtn, BorderLayout.WEST);
        topBar.add(title,   BorderLayout.CENTER);
        topBar.add(spacer,  BorderLayout.EAST);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(topBar, BorderLayout.NORTH);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));
        add(headerPanel, BorderLayout.NORTH);

        // ── Main split ────────────────────────────────────────
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildSidebar(), buildTable());
        split.setDividerLocation(340);
        split.setDividerSize(0); // Invisible divider
        split.setBorder(null);
        split.setOpaque(false);
        
        JPanel splitContainer = new JPanel(new BorderLayout());
        splitContainer.setOpaque(false);
        splitContainer.setBorder(new EmptyBorder(0, 40, 40, 40));
        splitContainer.add(split, BorderLayout.CENTER);
        
        add(splitContainer, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 90)); 
                g2.fillRoundRect(0, 0, getWidth()-15, getHeight(), 24, 24);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(0, 0, getWidth()-16, getHeight()-1, 24, 24);
            }
        };
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(25, 25, 25, 40));

        JLabel hdr = new JLabel("Add Session");
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        hdr.setForeground(new Color(236, 72, 153));
        hdr.setBorder(new EmptyBorder(0,0,20,0));
        p.add(hdr);

        subField  = styledField("Subject name");
        facField  = styledField("Faculty name");
        roomField = styledField("Room / Lab");

        dayCombo  = combo(DAYS);
        slotCombo = combo(SLOTS);
        deptCombo = combo(DEPTS);
        semCombo  = combo(SEMS);

        p.add(row("Subject",  subField));  p.add(gap(12));
        p.add(row("Faculty",  facField));  p.add(gap(12));
        p.add(row("Room",     roomField)); p.add(gap(12));
        p.add(row("Day",      dayCombo));  p.add(gap(12));
        p.add(row("Slot",     slotCombo)); p.add(gap(12));
        p.add(row("Dept",     deptCombo)); p.add(gap(12));
        p.add(row("Sem",      semCombo));  p.add(gap(25));

        JButton addBtn  = actionButton("Add Session",  new Color(99, 102, 241));
        JButton autoBtn = actionButton("Auto-Generate", new Color(245, 158, 11));
        JButton clrBtn  = actionButton("Clear All",    new Color(239, 68, 68));

        addBtn.addActionListener(e -> addSession());
        autoBtn.addActionListener(e -> autoGenerate());
        clrBtn.addActionListener(e -> clearAll());

        p.add(addBtn);  p.add(gap(12));
        p.add(autoBtn); p.add(gap(12));
        p.add(clrBtn);  p.add(gap(20));

        statusLbl = new JLabel(" ");
        statusLbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLbl.setForeground(new Color(156, 163, 175));
        statusLbl.setAlignmentX(LEFT_ALIGNMENT);
        p.add(statusLbl);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(p, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel buildTable() {
        JPanel p = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 90)); 
                g2.fillRoundRect(15, 0, getWidth()-15, getHeight(), 24, 24);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawRoundRect(15, 0, getWidth()-16, getHeight()-1, 24, 24);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(25, 45, 25, 25));

        JLabel hdr = new JLabel("All Sessions (Right-click to delete)");
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        hdr.setForeground(new Color(99, 102, 241));
        hdr.setBorder(new EmptyBorder(0, 0, 15, 0));
        p.add(hdr, BorderLayout.NORTH);

        schedModel = new DefaultTableModel(
            new String[]{"Day","Time Slot","Subject","Faculty","Room","Department"}, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable t = new JTable(schedModel);
        styleTable(t);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem del = new JMenuItem("Delete Row");
        del.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row < 0) return;
            DatabaseModule.deleteSession(
                schedModel.getValueAt(row,0).toString(),
                schedModel.getValueAt(row,1).toString(),
                schedModel.getValueAt(row,5).toString());
            schedModel.removeRow(row);
            status("Row deleted.", "warn");
        });
        menu.add(del); t.setComponentPopupMenu(menu);

        JScrollPane sp = new JScrollPane(t);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(BorderFactory.createEmptyBorder());
        p.add(sp, BorderLayout.CENTER);

        refreshTable();
        return p;
    }

    // ── Session logic ────────────────────────────────────────
    private void addSession() {
        String sub  = subField.getText().trim();
        String fac  = facField.getText().trim();
        String room = roomField.getText().trim();
        if (sub.isEmpty() || fac.isEmpty() || room.isEmpty()) {
            status("Fill Subject, Faculty and Room.", "warn"); return;
        }
        String day  = (String) dayCombo.getSelectedItem();
        String slot = (String) slotCombo.getSelectedItem();
        String dept = (String) deptCombo.getSelectedItem();
        String sem  = (String) semCombo.getSelectedItem();

        String conflict = SchedulingEngine.checkConflict(day, slot, fac, room);
        if (conflict != null) { status(conflict, "error"); return; }

        DatabaseModule.insertSession(day, slot, sub, fac, room, dept, sem);
        refreshTable();
        subField.setText(""); facField.setText(""); roomField.setText("");
        status("Session added: " + sub + " on " + day + " " + slot, "ok");
    }

    private void autoGenerate() {
        String dept = (String) deptCombo.getSelectedItem();
        String sem  = (String) semCombo.getSelectedItem();
        int n = SchedulingEngine.autoGenerate(dept, sem);
        refreshTable();
        status("Auto-generated " + n + " sessions for " + dept + " " + sem, "ok");
    }

    private void clearAll() {
        int c = JOptionPane.showConfirmDialog(this,
            "Clear ALL sessions from every department?", "Confirm Clear",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (c == JOptionPane.YES_OPTION) {
            DatabaseModule.clearAll(); refreshTable(); status("All sessions cleared.", "warn");
        }
    }

    private void refreshTable() {
        schedModel.setRowCount(0);
        DatabaseModule.fetchAllSessions().forEach(r -> schedModel.addRow(r));
    }

    private void status(String msg, String type) {
        statusLbl.setForeground(type.equals("ok") ? new Color(110, 231, 183) :
                                type.equals("warn")? new Color(251, 191, 36) : new Color(248, 113, 113));
        statusLbl.setText("• " + msg);
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField(placeholder, 14);
        f.setOpaque(false);
        f.setForeground(Color.WHITE);
        f.setCaretColor(Color.WHITE); 
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 40), 1, true),
            new EmptyBorder(8, 12, 8, 12)));
        return f;
    }
    
    private JButton actionButton(String text, Color baseColor) {
        JButton b = new JButton(text) {
            boolean hov = false;
            { addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { hov=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hov) g2.setColor(baseColor.brighter());
                else g2.setColor(baseColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        b.setAlignmentX(LEFT_ALIGNMENT);
        return b;
    }

    private JPanel row(String label, JComponent field) {
        JPanel r = new JPanel(new BorderLayout(12, 0));
        r.setOpaque(false); r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel l = new JLabel(label); l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(156, 163, 175)); l.setPreferredSize(new Dimension(60, 20));
        r.add(l, BorderLayout.WEST); r.add(field, BorderLayout.CENTER); return r;
    }
    private Component gap(int h) { return Box.createVerticalStrut(h); }
    private JComboBox<String> combo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setBackground(new Color(20, 25, 50)); 
        cb.setForeground(Color.WHITE);
        cb.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return cb;
    }

    private void styleTable(JTable t) {
        t.setOpaque(false);
        t.setForeground(Color.WHITE);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.setRowHeight(45);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setSelectionBackground(new Color(99, 102, 241, 100));
        t.setSelectionForeground(Color.WHITE);
        
        t.getTableHeader().setOpaque(false);
        t.getTableHeader().setBackground(new Color(0, 0, 0, 0));
        t.getTableHeader().setForeground(new Color(236, 72, 153));
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.getTableHeader().setPreferredSize(new Dimension(0, 40));
        
        ((DefaultTableCellRenderer)t.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object v, boolean sel, boolean foc, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tbl, v, sel, foc, r, c);
                l.setHorizontalAlignment(SwingConstants.CENTER);
                l.setOpaque(true);
                
                if (sel) {
                    l.setBackground(new Color(99, 102, 241, 120));
                } else {
                    l.setBackground(r % 2 == 0 ? new Color(255, 255, 255, 10) : new Color(255, 255, 255, 5));
                }
                
                if (v != null && !v.toString().isEmpty()) l.setForeground(new Color(110, 231, 183));
                else l.setForeground(new Color(148, 163, 184));

                if (c == 0) {
                    l.setBackground(new Color(99, 102, 241, 20));
                    l.setForeground(new Color(99, 102, 241));
                    l.setFont(new Font("Segoe UI", Font.BOLD, 14));
                }
                
                l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 20)));
                return l;
            }
        });
    }
}