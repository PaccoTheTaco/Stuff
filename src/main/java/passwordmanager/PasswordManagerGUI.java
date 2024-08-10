package passwordmanager;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PasswordManagerGUI extends JFrame {
    private static final String CREDENTIAL_FILE = "credentials.txt";
    private static final String MASTER_PASSWORD_FILE = "master_password.dat";
    private List<Credential> credentials;
    private DefaultListModel<String> listModel;

    public PasswordManagerGUI() {
        credentials = new ArrayList<>();
        listModel = new DefaultListModel<>();

        if (!masterPasswordExists()) {
            setMasterPassword();
        } else {
            if (!authenticateUser()) {
                JOptionPane.showMessageDialog(this, "Incorrect password. Exiting.");
                System.exit(0);
            }
        }

        setTitle("Password Manager");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JList<String> credentialList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(credentialList);

        JButton addButton = new JButton("Add Credential");
        addButton.addActionListener(e -> addCredential());

        JButton viewButton = new JButton("View Credential");
        viewButton.addActionListener(e -> {
            int selectedIndex = credentialList.getSelectedIndex();
            if (selectedIndex != -1) {
                showCredentialDetails(credentials.get(selectedIndex));
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean masterPasswordExists() {
        File file = new File(MASTER_PASSWORD_FILE);
        return file.exists();
    }

    private void setMasterPassword() {
        JPasswordField passwordField = new JPasswordField();
        JTextField emailField = new JTextField();

        Object[] fields = {
                "Set Master Password:", passwordField,
                "Enter Email Address:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Setup Password Manager", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String masterPassword = new String(passwordField.getPassword());
            String email = emailField.getText();
            saveMasterPassword(masterPassword, email);
        } else {
            System.exit(0);
        }
    }

    private void saveMasterPassword(String masterPassword, String email) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(masterPassword.getBytes());
            String encodedPassword = Base64.getEncoder().encodeToString(hashedPassword);

            BufferedWriter writer = new BufferedWriter(new FileWriter(MASTER_PASSWORD_FILE));
            writer.write(encodedPassword);
            writer.newLine();
            writer.write(email);
            writer.close();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private boolean authenticateUser() {
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
                "Enter Master Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Authenticate", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            return checkMasterPassword(enteredPassword);
        }
        return false;
    }

    private boolean checkMasterPassword(String enteredPassword) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedPassword = md.digest(enteredPassword.getBytes());
            String encodedPassword = Base64.getEncoder().encodeToString(hashedPassword);

            BufferedReader reader = new BufferedReader(new FileReader(MASTER_PASSWORD_FILE));
            String storedPassword = reader.readLine();
            reader.close();

            return encodedPassword.equals(storedPassword);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addCredential() {
        JTextField titleField = new JTextField();
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        Object[] fields = {
                "Title:", titleField,
                "Username:", usernameField,
                "Password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Credential", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            Credential credential = new Credential(title, username, password);
            credentials.add(credential);
            listModel.addElement(title);
        }
    }

    private void showCredentialDetails(Credential credential) {
        JOptionPane.showMessageDialog(this,
                "Title: " + credential.getTitle() + "\n" +
                        "Username: " + credential.getUsername() + "\n" +
                        "Password: " + credential.getPassword(),
                "Credential Details",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
