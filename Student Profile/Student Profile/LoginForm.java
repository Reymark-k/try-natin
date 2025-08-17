import javax.swing.*;
import java.awt.*;

public class LoginForm extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnCancel, btnRegister;

    // Static variables to store registered credentials
    public static String registeredEmail = "admin@example.com";
    public static String registeredPassword = "123456";

    public LoginForm() {
        setTitle("Login");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JPanel mainPanel = createRoundedPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("Student Login", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(40, 70, 110));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        // Email
        gbc.gridwidth = 1;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(lblEmail, gbc);

        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(txtEmail, gbc);

        // Password
        JLabel lblPassword = new JLabel("Password:");
        lblPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(lblPassword, gbc);

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(txtPassword, gbc);

        // Buttons
        btnLogin = new JButton("Login");
        btnCancel = new JButton("Cancel");
        btnRegister = new JButton("Register");

        styleButton(btnLogin, new Color(72, 149, 239));
        styleButton(btnCancel, new Color(239, 83, 80));
        styleButton(btnRegister, new Color(46, 204, 113));

        JPanel panelButtons = new JPanel();
        panelButtons.setOpaque(false);
        panelButtons.add(btnLogin);
        panelButtons.add(btnCancel);
        panelButtons.add(btnRegister);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        mainPanel.add(panelButtons, gbc);

        add(mainPanel);

        // Actions
        btnLogin.addActionListener(e -> loginAction());
        btnCancel.addActionListener(e -> System.exit(0));
        btnRegister.addActionListener(e -> {
            new RegisterForm().setVisible(true);
            dispose();
        });
    }

    private JPanel createRoundedPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 248, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        panel.setPreferredSize(new Dimension(380, 240));
        panel.setOpaque(false);
        return panel;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void loginAction() {
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (email.equals(registeredEmail) && password.equals(registeredPassword)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            SwingUtilities.invokeLater(() -> {
                new Dashboard().setVisible(true);
            });
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password.",
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}

// ===================== REGISTER FORM =====================
class RegisterForm extends JFrame {
    private JTextField txtName, txtEmail;
    private JPasswordField txtPassword, txtConfirm;
    private JButton btnRegister, btnBack;

    public RegisterForm() {
        setTitle("Register");
        setSize(420, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        JPanel mainPanel = createRoundedPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("Register", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(40, 70, 110));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Name:"), createGbc(0, 1));
        txtName = new JTextField(20);
        mainPanel.add(txtName, createGbc(1, 1));

        mainPanel.add(new JLabel("Email:"), createGbc(0, 2));
        txtEmail = new JTextField(20);
        mainPanel.add(txtEmail, createGbc(1, 2));

        mainPanel.add(new JLabel("Password:"), createGbc(0, 3));
        txtPassword = new JPasswordField(20);
        mainPanel.add(txtPassword, createGbc(1, 3));

        mainPanel.add(new JLabel("Confirm Password:"), createGbc(0, 4));
        txtConfirm = new JPasswordField(20);
        mainPanel.add(txtConfirm, createGbc(1, 4));

        btnRegister = new JButton("Register");
        btnBack = new JButton("Back to Login");
        styleButton(btnRegister, new Color(46, 204, 113));
        styleButton(btnBack, new Color(72, 149, 239));

        JPanel panelButtons = new JPanel();
        panelButtons.setOpaque(false);
        panelButtons.add(btnRegister);
        panelButtons.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        mainPanel.add(panelButtons, gbc);

        add(mainPanel);

        btnRegister.addActionListener(e -> registerAction());
        btnBack.addActionListener(e -> {
            new LoginForm().setVisible(true);
            dispose();
        });
    }

    private JPanel createRoundedPanel() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 248, 255));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };
        panel.setPreferredSize(new Dimension(380, 300));
        panel.setOpaque(false);
        return panel;
    }

    private GridBagConstraints createGbc(int x, int y) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }

    private void registerAction() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirm.getPassword());

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Store registered credentials for LoginForm
        LoginForm.registeredEmail = email;
        LoginForm.registeredPassword = password;

        JOptionPane.showMessageDialog(this, "Registration successful for " + name + "!");
        new LoginForm().setVisible(true);
        dispose();
    }
}

// Dummy Dashboard for testing
class Dashboard extends JFrame {
    public Dashboard() {
        setTitle("Student Profile");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JLabel("Welcome to Student Dashboard!", SwingConstants.CENTER));
    }
}
