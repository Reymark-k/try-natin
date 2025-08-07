import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class Javajava3 extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtID, txtFirst, txtMiddle, txtLast;
    private JComboBox<String> cmbCourse, cmbStatus;

    private static final String DB_FILE = System.getProperty("user.home") + File.separator + "student_db.txt";

    public Javajava3() {
        setTitle("Student Information System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        Color lightBlue = new Color(173, 216, 230);

        // Use BorderLayout for the main frame
        setLayout(new BorderLayout(10, 10));

        // Title at the top
        JLabel titleLabel = new JLabel("Student Information System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(lightBlue);
        titleLabel.setPreferredSize(new Dimension(900, 40));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel (left)
        JPanel formPanel = new JPanel();
        formPanel.setBackground(lightBlue);
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        Font labelFont = new Font("Arial", Font.BOLD, 14);
        Font fieldFont = new Font("Arial", Font.PLAIN, 14);

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(lightBlue);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel[] labels = {
            new JLabel("Student ID"), new JLabel("First Name"),
            new JLabel("Middle Name"), new JLabel("Last Name"),
            new JLabel("Course"), new JLabel("Enrollment Status")
        };

        txtID = new JTextField(12); txtFirst = new JTextField(12); txtMiddle = new JTextField(12); txtLast = new JTextField(12);
        cmbCourse = new JComboBox<>(new String[]{"COMPUTER ENGINEERING", "COMPUTER SCIENCE", "ENTREPRENEUR"});
        cmbStatus = new JComboBox<>(new String[]{"ENROLLED", "NOT ENROLLED"});

        JComponent[] fields = { txtID, txtFirst, txtMiddle, txtLast, cmbCourse, cmbStatus };

        for (int i = 0; i < labels.length; i++) {
            labels[i].setFont(labelFont);
            gbc.gridx = 0; gbc.gridy = i;
            gbc.fill = GridBagConstraints.NONE;
            fieldsPanel.add(labels[i], gbc);

            fields[i].setFont(fieldFont);
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            fieldsPanel.add(fields[i], gbc);
        }

        formPanel.add(fieldsPanel);

        // Button panel (below form)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(lightBlue);
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        JButton btnQuit = new JButton("Quit");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        btnAdd.setFont(buttonFont); btnUpdate.setFont(buttonFont); btnDelete.setFont(buttonFont);
        btnClear.setFont(buttonFont); btnQuit.setFont(buttonFont);

        btnAdd.setBackground(new Color(76, 175, 80)); btnAdd.setForeground(Color.WHITE);
        btnUpdate.setBackground(new Color(33, 150, 243)); btnUpdate.setForeground(Color.WHITE);
        btnDelete.setBackground(new Color(244, 67, 54)); btnDelete.setForeground(Color.WHITE);
        btnClear.setBackground(new Color(255, 193, 7)); btnClear.setForeground(Color.BLACK);
        btnQuit.setBackground(new Color(121, 85, 72)); btnQuit.setForeground(Color.WHITE);

        buttonPanel.add(btnAdd); buttonPanel.add(btnUpdate); buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear); buttonPanel.add(btnQuit);

        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(buttonPanel);

        // Table panel (center)
        model = new DefaultTableModel(new String[]{"ID", "First", "Middle", "Last", "Course", "Status"}, 0);
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(22);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createTitledBorder("Student Records"));

        // Add panels to frame
        add(formPanel, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);

        loadFile();

        btnAdd.addActionListener(e -> {
            if (!isValidID()) return;
            if (isDuplicateID(txtID.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Student ID already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                txtID.requestFocus();
                return;
            }
            model.addRow(new String[]{
                txtID.getText().toUpperCase(),
                txtFirst.getText().toUpperCase(),
                txtMiddle.getText().toUpperCase(),
                txtLast.getText().toUpperCase(),
                cmbCourse.getSelectedItem().toString(),
                cmbStatus.getSelectedItem().toString()
            });
            saveFile();
            clearFields();
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1 || !isValidID()) return;
            String newID = txtID.getText().trim();
            if (!model.getValueAt(row, 0).toString().equals(newID) && isDuplicateID(newID)) {
                JOptionPane.showMessageDialog(this, "Student ID already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                txtID.requestFocus();
                return;
            }
            model.setValueAt(txtID.getText().toUpperCase(), row, 0);
            model.setValueAt(txtFirst.getText().toUpperCase(), row, 1);
            model.setValueAt(txtMiddle.getText().toUpperCase(), row, 2);
            model.setValueAt(txtLast.getText().toUpperCase(), row, 3);
            model.setValueAt(cmbCourse.getSelectedItem().toString(), row, 4);
            model.setValueAt(cmbStatus.getSelectedItem().toString(), row, 5);
            saveFile();
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                model.removeRow(row);
                saveFile();
                clearFields();
            }
        });

        btnClear.addActionListener(e -> clearFields());

        btnQuit.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Quit program?", "Exit", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                txtID.setText(model.getValueAt(row, 0).toString());
                txtFirst.setText(model.getValueAt(row, 1).toString());
                txtMiddle.setText(model.getValueAt(row, 2).toString());
                txtLast.setText(model.getValueAt(row, 3).toString());
                cmbCourse.setSelectedItem(model.getValueAt(row, 4).toString());
                cmbStatus.setSelectedItem(model.getValueAt(row, 5).toString());
            }
        });

        txtID.setBackground(Color.WHITE);
        txtFirst.setBackground(Color.WHITE);
        txtMiddle.setBackground(Color.WHITE);
        txtLast.setBackground(Color.WHITE);
        cmbCourse.setBackground(Color.WHITE);
        cmbStatus.setBackground(Color.WHITE);
    }

    private boolean isValidID() {
        String id = txtID.getText().trim();
        if (!id.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(this, "Student ID must be exactly 6 digits.", "Invalid ID", JOptionPane.WARNING_MESSAGE);
            txtID.requestFocus();
            return false;
        }
        return true;
    }

    private boolean isDuplicateID(String id) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(id)) {
                return true;
            }
        }
        return false;
    }

    private void clearFields() {
        txtID.setText(""); txtFirst.setText(""); txtMiddle.setText("");
        txtLast.setText("");
        cmbCourse.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        txtID.requestFocus();
    }

    private void saveFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DB_FILE))) {
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    bw.write(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1) bw.write(",");
                }
                bw.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file: " + e.getMessage());
        }
    }

    private void loadFile() {
        File file = new File(DB_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",", -1);
                if (row.length == model.getColumnCount()) {
                    model.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Javajava3().setVisible(true));
    }
}
