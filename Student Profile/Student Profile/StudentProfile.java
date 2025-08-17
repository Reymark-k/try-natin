import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class StudentProfile extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtID, txtFirst, txtMiddle, txtLast, txtAge, txtBirthday, txtContact;
    private JTextField txtParent, txtParentContact, txtReligion, txtLastSchool;
    private JComboBox<String> cmbCourse, cmbStatus, cmbSex;

    private static final String DB_FILE = System.getProperty("user.home") + File.separator + "student_db.txt";

    public StudentProfile() {
        setTitle("Student Information System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        Color lightBlue = new Color(173, 216, 230);

        JLabel titleLabel = new JLabel("Student Information System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(lightBlue);
        titleLabel.setPreferredSize(new Dimension(900, 40));
        add(titleLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(lightBlue);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 8, 4, 8);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {
            "Student ID", "First Name", "Middle Name", "Last Name", "Age", "Birthday",
            "Sex", "Contact Number", "Course", "Enrollment Status",
            "Name of Parent", "Contact No. of Parent", "Religion", "Last School Year"
        };

        Component[] fields = {
            txtID = new JTextField(12), txtFirst = new JTextField(12), txtMiddle = new JTextField(12), txtLast = new JTextField(12),
            txtAge = new JTextField(12), txtBirthday = new JTextField(12), cmbSex = new JComboBox<>(new String[]{"MALE", "FEMALE", "OTHER"}),
            txtContact = new JTextField(12), cmbCourse = new JComboBox<>(new String[]{"COMPUTER ENGINEERING", "COMPUTER SCIENCE", "ENTREPRENEUR"}),
            cmbStatus = new JComboBox<>(new String[]{"ENROLLED", "NOT ENROLLED"}), txtParent = new JTextField(12),
            txtParentContact = new JTextField(12), txtReligion = new JTextField(12), txtLastSchool = new JTextField(12)
        };

        // 2 columns layout
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            formPanel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            formPanel.add((Component) fields[i], gbc);
        }

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");
        JButton btnQuit = new JButton("Quit");

        JButton[] buttons = {btnAdd, btnUpdate, btnDelete, btnClear, btnQuit};
        for (JButton b : buttons) {
            b.setFont(new Font("Arial", Font.BOLD, 14));
            b.setBackground(new Color(33, 150, 243));
            b.setForeground(Color.WHITE);
        }
        btnAdd.setBackground(new Color(76, 175, 80));
        for (JButton b : buttons) buttonPanel.add(b);

        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.setBackground(lightBlue);
        JScrollPane scrollForm = new JScrollPane(formPanel);
        scrollForm.setPreferredSize(new Dimension(400, 400));
        westPanel.add(scrollForm, BorderLayout.CENTER);
        westPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Table setup
        model = new DefaultTableModel(new String[]{
            "ID", "First", "Middle", "Last", "Age", "Birthday", "Sex", "Contact",
            "Course", "Status", "Parent", "Parent Contact", "Religion", "Last School"
        }, 0);
        table = new JTable(model);
        table.setRowHeight(22);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createTitledBorder("Student Records"));

        add(westPanel, BorderLayout.WEST);
        add(scrollTable, BorderLayout.CENTER);

        // Load existing records
        loadFile();

        // Actions
        btnAdd.addActionListener(e -> {
            if (!isValidID()) return;
            if (isDuplicateID(txtID.getText().trim())) {
                JOptionPane.showMessageDialog(this, "Student ID already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                txtID.requestFocus();
                return;
            }
            model.addRow(getFormData());
            saveFile();
            clearFields();
        });

        btnUpdate.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1 || !isValidID()) return;
            if (!table.getValueAt(row, 0).toString().equals(txtID.getText()) &&
                isDuplicateID(txtID.getText())) {
                JOptionPane.showMessageDialog(this, "Student ID already exists.", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Object[] updatedData = getFormData();
            for (int i = 0; i < updatedData.length; i++) {
                model.setValueAt(updatedData[i], row, i);
            }
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
            int choice = JOptionPane.showConfirmDialog(this, "Quit program?", "Exit", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) System.exit(0);
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            txtID.setText(model.getValueAt(row, 0).toString());
            txtFirst.setText(model.getValueAt(row, 1).toString());
            txtMiddle.setText(model.getValueAt(row, 2).toString());
            txtLast.setText(model.getValueAt(row, 3).toString());
            txtAge.setText(model.getValueAt(row, 4).toString());
            txtBirthday.setText(model.getValueAt(row, 5).toString());
            cmbSex.setSelectedItem(model.getValueAt(row, 6).toString());
            txtContact.setText(model.getValueAt(row, 7).toString());
            cmbCourse.setSelectedItem(model.getValueAt(row, 8).toString());
            cmbStatus.setSelectedItem(model.getValueAt(row, 9).toString());
            txtParent.setText(model.getValueAt(row, 10).toString());
            txtParentContact.setText(model.getValueAt(row, 11).toString());
            txtReligion.setText(model.getValueAt(row, 12).toString());
            txtLastSchool.setText(model.getValueAt(row, 13).toString());
        });
    }

    private boolean isValidID() {
        String id = txtID.getText().trim();
        if (!id.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(this, "Student ID must be exactly 6 digits.", "Invalid ID", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isDuplicateID(String id) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(id)) return true;
        }
        return false;
    }

    private Object[] getFormData() {
        return new Object[]{
            txtID.getText().toUpperCase(), txtFirst.getText().toUpperCase(),
            txtMiddle.getText().toUpperCase(), txtLast.getText().toUpperCase(),
            txtAge.getText(), txtBirthday.getText(), cmbSex.getSelectedItem().toString(),
            txtContact.getText(), cmbCourse.getSelectedItem().toString(), cmbStatus.getSelectedItem().toString(),
            txtParent.getText(), txtParentContact.getText(), txtReligion.getText(), txtLastSchool.getText()
        };
    }

    private void clearFields() {
        txtID.setText(""); txtFirst.setText(""); txtMiddle.setText(""); txtLast.setText("");
        txtAge.setText(""); txtBirthday.setText(""); txtContact.setText(""); txtParent.setText("");
        txtParentContact.setText(""); txtReligion.setText(""); txtLastSchool.setText("");
        cmbSex.setSelectedIndex(0); cmbCourse.setSelectedIndex(0); cmbStatus.setSelectedIndex(0);
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
        SwingUtilities.invokeLater(() -> new StudentProfile().setVisible(true));
    }
}

