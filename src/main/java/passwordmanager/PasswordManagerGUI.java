package passwordmanager;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PasswordManagerGUI extends JFrame {
    private static final String CREDENTIAL_FILE = "credentials.enc";
    private static final String MASTER_PASSWORD_FILE = "master_password.dat";
    private List<Credential> credentials;
    private DefaultListModel<String> listModel;
    private String masterPassword;

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

        loadCredentials();

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

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveCredentials();
                System.exit(0);
            }
        });
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
            masterPassword = new String(passwordField.getPassword());
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
            masterPassword = new String(passwordField.getPassword());
            return checkMasterPassword(masterPassword);
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
        JTextField urlField = new JTextField();

        Object[] fields = {
                "Title:", titleField,
                "Username:", usernameField,
                "Password:", passwordField,
                "URL:", urlField
        };

        int option = JOptionPane.showConfirmDialog(this, fields, "Add Credential", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String title = titleField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String url = urlField.getText();

            Credential credential = new Credential(title, username, password, url);
            credentials.add(credential);
            listModel.addElement(title);
        }
    }

    private void showCredentialDetails(Credential credential) {
        JOptionPane.showMessageDialog(this,
                "Title: " + credential.getTitle() + "\n" +
                        "Username: " + credential.getUsername() + "\n" +
                        "Password: " + credential.getPassword() + "\n" +
                        "URL: " + credential.getUrl(),
                "Credential Details",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveCredentials() {
        try {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(credentials);
            objectStream.close();
            byte[] serializedData = byteStream.toByteArray();

            SecretKeySpec key = new SecretKeySpec(getAESKeyFromPassword(masterPassword), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedData = cipher.doFinal(serializedData);

            try (FileOutputStream fileOut = new FileOutputStream(CREDENTIAL_FILE)) {
                fileOut.write(encryptedData);
                fileOut.flush();
            }
            System.out.println("Credentials saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCredentials() {
        File file = new File(CREDENTIAL_FILE);
        if (file.exists()) {
            try {
                byte[] encryptedData;
                try (FileInputStream fileIn = new FileInputStream(file)) {
                    encryptedData = fileIn.readAllBytes();
                }

                SecretKeySpec key = new SecretKeySpec(getAESKeyFromPassword(masterPassword), "AES");
                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] serializedData = cipher.doFinal(encryptedData);

                ByteArrayInputStream byteStream = new ByteArrayInputStream(serializedData);
                ObjectInputStream objectStream = new ObjectInputStream(byteStream);
                credentials = (List<Credential>) objectStream.readObject();
                objectStream.close();

                for (Credential credential : credentials) {
                    listModel.addElement(credential.getTitle());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private byte[] getAESKeyFromPassword(String password) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(password.getBytes("UTF-8"));
        return key;
    }
}
