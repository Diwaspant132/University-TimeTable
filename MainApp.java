package timetable;

// ============================================================
//  PROGRAMMER 1 — User Interface Module
//  Home page, navigation, visual design
//  NO external dependencies — pure Java AWT/Swing
// ============================================================

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainApp extends JFrame {

    static final Color BG_DARK    = new Color(10,  14,  35);
    static final Color ACCENT     = new Color(99, 102, 241);
    static final Color ACCENT2    = new Color(236, 72,  153);
    static final Color CARD_BG    = new Color(20,  25,  55);
    static final Color TEXT_LIGHT = new Color(226, 232, 240);
    static final Color TEXT_DIM   = new Color(148, 163, 184);
    static final Color SUCCESS    = new Color(52,  211, 153);

    static final Font FONT_TITLE  = new Font("Segoe UI", Font.BOLD,  28);
    static final Font FONT_HEADING= new Font("Segoe UI", Font.BOLD,  18);
    static final Font FONT_BODY   = new Font("Segoe UI", Font.PLAIN, 14);
    static final Font FONT_SMALL  = new Font("Segoe UI", Font.PLAIN, 12);

    private CardLayout cardLayout;
    private JPanel     rootPanel;

    public MainApp() {
        setTitle("UniScheduler Pro — University Timetable System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);

        DatabaseModule.initDB();   // ← no JDBC, no JAR needed

        cardLayout = new CardLayout();
        rootPanel  = new JPanel(cardLayout);
        rootPanel.setBackground(BG_DARK);

        rootPanel.add(buildHomePage(),        "HOME");
        rootPanel.add(new StudentPanel(this), "STUDENT");
        rootPanel.add(new FacultyPanel(this), "FACULTY");

        add(rootPanel);
        setVisible(true);
    }

    // ── Home page ────────────────────────────────────────────
    private JPanel buildHomePage() {
        JPanel page = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Deep background base
                g2.setColor(new Color(7, 11, 25));
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Glowing radial orbs
                RadialGradientPaint rp1 = new RadialGradientPaint(
                    new Point(0, getHeight()/2), getWidth()*0.6f,
                    new float[]{0.0f, 1.0f},
                    new Color[]{new Color(99, 102, 241, 40), new Color(7, 11, 25, 0)}
                );
                g2.setPaint(rp1);
                g2.fillRect(0, 0, getWidth(), getHeight());

                RadialGradientPaint rp2 = new RadialGradientPaint(
                    new Point(getWidth(), 0), getWidth()*0.5f,
                    new float[]{0.0f, 1.0f},
                    new Color[]{new Color(236, 72, 153, 35), new Color(7, 11, 25, 0)}
                );
                g2.setPaint(rp2);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Add a subtle grid overlay pattern
                g2.setColor(new Color(255, 255, 255, 3));
                for(int i=0; i<getWidth(); i+=40) g2.drawLine(i,0,i,getHeight());
                for(int i=0; i<getHeight(); i+=40) g2.drawLine(0,i,getWidth(),i);
            }
        };

        // Header removed per user request

        // Core layout: Split left/right
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 40, 0));
        splitPanel.setOpaque(false);
        splitPanel.setBorder(new EmptyBorder(40, 60, 60, 60));

        // Left Side: Text and Stats
        JPanel leftSide = new JPanel();
        leftSide.setOpaque(false);
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setBorder(new EmptyBorder(50, 0, 0, 0));

        JLabel badge = new JLabel(" SMART AUTOMATION ");
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(new Color(236, 72, 153));
        badge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(236, 72, 153, 150), 1, true),
            new EmptyBorder(4, 8, 4, 8)
        ));
        badge.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel title = new JLabel("<html>University<br>Timetable<br>Generator.</html>");
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("<html><p style='width: 300px; line-height: 1.5;'>A modern, conflict-free scheduling engine designed to instantly organize classes, faculties, and rooms without overlapping timelines.</p></html>");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sub.setForeground(TEXT_DIM);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);
        sub.setBorder(new EmptyBorder(15, 0, 30, 0));

        leftSide.add(badge);
        leftSide.add(Box.createVerticalStrut(20));
        leftSide.add(title);
        leftSide.add(sub);

        // Right Side: Asymmetric Cards
        JPanel rightSide = new JPanel(new GridBagLayout());
        rightSide.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 15, 0, 15);
        gbc.gridy = 0;
        
        JPanel c1 = roleCard("01", "Student Portal", "View personalized timetables by course & semester", ACCENT, () -> showCard("STUDENT"));
        JPanel c2 = roleCard("02", "Faculty Portal", "Manage subjects, assign rooms and generate schedules", ACCENT2, () -> showCard("FACULTY"));
        
        c1.setPreferredSize(new Dimension(220, 320));
        c2.setPreferredSize(new Dimension(220, 320));

        // Offset the cards for asymmetric look
        JPanel cardHolder1 = new JPanel(new BorderLayout()); cardHolder1.setOpaque(false);
        cardHolder1.setBorder(new EmptyBorder(0, 0, 80, 0));
        cardHolder1.add(c1);

        JPanel cardHolder2 = new JPanel(new BorderLayout()); cardHolder2.setOpaque(false);
        cardHolder2.setBorder(new EmptyBorder(80, 0, 0, 0));
        cardHolder2.add(c2);

        rightSide.add(cardHolder1, gbc);
        rightSide.add(cardHolder2, gbc);

        splitPanel.add(leftSide);
        splitPanel.add(rightSide);
        
        page.add(splitPanel, BorderLayout.CENTER);

        // Footer removed per user request

        return page;
    }

    private JPanel roleCard(String number, String title, String desc, Color accent, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(0, 5)) {
            float alpha = 0.4f;
            float hoverY = 0f;
            boolean hov = false;
            { 
                Timer t = new Timer(16, e -> {
                    if (hov && alpha < 0.8f) alpha += 0.05f;
                    if (!hov && alpha > 0.4f) alpha -= 0.05f;
                    if (hov && hoverY > -8f) hoverY -= 1f;
                    if (!hov && hoverY < 0f) hoverY += 1f;
                    repaint();
                });
                t.start();
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) { hov = true; }
                    @Override
                    public void mouseExited (MouseEvent e) { hov = false; }
                    @Override
                    public void mouseClicked(MouseEvent e) { action.run(); }
                });
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int yOffset = (int) hoverY;
                
                // Glass shadow
                g2.setColor(new Color(0, 0, 0, 80));
                g2.fillRoundRect(8, 8 + yOffset, getWidth()-16, getHeight()-16, 24, 24);
                
                // Glass background
                g2.setColor(new Color(255, 255, 255, 12 + (int)(alpha * 15)));
                g2.fillRoundRect(0, yOffset, getWidth(), getHeight() - 10, 24, 24);
                
                // Subtle gradient overlay on glass
                GradientPaint gp = new GradientPaint(0, yOffset, new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), (int)(alpha * 40)), 
                                                     0, getHeight(), new Color(255, 255, 255, 0));
                g2.setPaint(gp);
                g2.fillRoundRect(0, yOffset, getWidth(), getHeight() - 10, 24, 24);
                
                // Glass border
                g2.setColor(new Color(255, 255, 255, 30 + (int)(alpha * 40)));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(0, yOffset, getWidth()-1, getHeight() - 11, 24, 24);
                
                super.paintComponent(g);
            }
            @Override protected void paintChildren(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.translate(0, (int)hoverY);
                super.paintChildren(g2);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(35, 25, 35, 25));

        JLabel num = new JLabel(number);
        num.setFont(new Font("Segoe UI", Font.BOLD, 50));
        num.setForeground(new Color(255,255,255, 30));

        JLabel ttl = new JLabel("<html><br>" + title + "</html>");
        ttl.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        ttl.setForeground(Color.WHITE);
        
        JPanel topHalf = new JPanel(new BorderLayout());
        topHalf.setOpaque(false);
        topHalf.add(num, BorderLayout.NORTH);
        topHalf.add(ttl, BorderLayout.CENTER);

        JLabel dsc = new JLabel("<html><p style='color: rgb(150,160,180);'>" + desc + "</p></html>");
        dsc.setFont(FONT_SMALL);

        JPanel btnWrapper = new JPanel(new BorderLayout());
        btnWrapper.setOpaque(false);
        JButton btn = styledButton("Launch \u2192", accent);
        btn.setPreferredSize(new Dimension(120, 36));
        btn.setOpaque(false);
        btn.addActionListener(e -> action.run());
        
        btnWrapper.add(btn, BorderLayout.WEST);
        btnWrapper.setBorder(new EmptyBorder(20, 0, 0, 0));

        JPanel botHalf = new JPanel(new BorderLayout());
        botHalf.setOpaque(false);
        botHalf.add(dsc, BorderLayout.CENTER);
        botHalf.add(btnWrapper, BorderLayout.SOUTH);

        card.add(topHalf, BorderLayout.NORTH);
        card.add(botHalf, BorderLayout.SOUTH);

        return card;
    }


    // ── Shared helpers ───────────────────────────────────────
    static JButton styledButton(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() :
                            getModel().isRollover()? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setForeground(Color.WHITE); b.setFocusPainted(false);
        b.setBorderPainted(false); b.setContentAreaFilled(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(130, 38));
        return b;
    }

    static JTextField styledField(String placeholder) {
        JTextField f = new JTextField(placeholder, 14);
        f.setBackground(CARD_BG); f.setForeground(TEXT_DIM);
        f.setCaretColor(TEXT_LIGHT); f.setFont(FONT_BODY);
        f.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 70, 120), 1, true),
            new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    void showCard(String name) { cardLayout.show(rootPanel, name); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainApp::new);
    }
}
