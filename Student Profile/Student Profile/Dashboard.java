import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        setTitle("Student Information");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JLabel lblHeader = new JLabel("Student Information", SwingConstants.CENTER);
        lblHeader.setFont(new Font("Arial", Font.BOLD, 26));
        lblHeader.setForeground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 70, 110));
        headerPanel.setPreferredSize(new Dimension(800, 60));

        // Buttons on right side of header
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setOpaque(false); // transparent background

        JButton btnEnroll = new JButton("Enroll");
        JButton btnExit = new JButton("Exit");

        // Button styles
        styleButton(btnEnroll, new Color(46, 204, 113));
        styleButton(btnExit, new Color(231, 76, 60));

        // Add listeners
        btnEnroll.addActionListener(e -> new StudentProfile().setVisible(true));
        btnExit.addActionListener(e -> System.exit(0));

        buttonPanel.add(btnEnroll);
        buttonPanel.add(btnExit);

        headerPanel.add(lblHeader, BorderLayout.CENTER);
        headerPanel.add(buttonPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN MENU =====
        JPanel mainPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Student Box (1-100 Enrolled)
        JPanel studentBox = createDashboardBox("Enrolled Records", "1 - 100", new Color(72, 149, 239));
        studentBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showStudentList();
            }
        });

        // Example extra menu items
        JPanel gradesBox = createDashboardBox("Grades", "View", new Color(46, 204, 113));
        JPanel settingsBox = createDashboardBox("Settings", "Manage", new Color(239, 83, 80));

        mainPanel.add(studentBox);
        mainPanel.add(gradesBox);
        mainPanel.add(settingsBox);

        add(mainPanel, BorderLayout.CENTER);
    }

    // ===== STYLE BUTTON =====
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // ===== CREATE DASHBOARD BOX =====
    private JPanel createDashboardBox(String title, String value, Color bgColor) {
        JPanel panel = new JPanel();
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BorderLayout());
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 16));
        lblValue.setForeground(Color.WHITE);

        panel.add(lblTitle, BorderLayout.CENTER);
        panel.add(lblValue, BorderLayout.SOUTH);

        // Hover effect
        panel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(bgColor.darker());
            }
            public void mouseExited(MouseEvent e) {
                panel.setBackground(bgColor);
            }
        });

        return panel;
    }

    // ===== SHOW STUDENT LIST WINDOW =====
    private void showStudentList() {
        JFrame listFrame = new JFrame("Enrolled Students");
        listFrame.setSize(400, 500);
        listFrame.setLocationRelativeTo(null);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 1; i <= 100; i++) {
            listModel.addElement("Student " + i);
        }

        JList<String> studentList = new JList<>(listModel);
        studentList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(studentList);

        listFrame.add(scrollPane);
        listFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
