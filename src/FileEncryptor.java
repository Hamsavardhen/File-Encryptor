package fileencryptor;
import javax.crypto.SecretKey;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class FileEncryptor extends JFrame {

    private JTextField inputField;
    private JTextField keyField;

    public FileEncryptor() {
        setTitle("AES File Encryptor/Decryptor");
        setSize(500, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JPanel filePanel = new JPanel();
        inputField = new JTextField(25);
        JButton browseBtn = new JButton("Browse...");
        browseBtn.addActionListener(e -> chooseFile());
        filePanel.add(new JLabel("File: "));
        filePanel.add(inputField);
        filePanel.add(browseBtn);
        add(filePanel);

        JPanel keyPanel = new JPanel();
        keyField = new JTextField(25);
        JButton keyBtn = new JButton("Load Key...");
        keyBtn.addActionListener(e -> loadKey());
        keyPanel.add(new JLabel("Key: "));
        keyPanel.add(keyField);
        keyPanel.add(keyBtn);
        add(keyPanel);

        JPanel buttonPanel = new JPanel();
        JButton encryptBtn = new JButton("Encrypt");
        JButton decryptBtn = new JButton("Decrypt");
        encryptBtn.addActionListener(e -> encryptFile());
        decryptBtn.addActionListener(e -> decryptFile());
        buttonPanel.add(encryptBtn);
        buttonPanel.add(decryptBtn);
        add(buttonPanel);

        add(new JLabel("Note: AES 128-bit used. Key must be saved and loaded to decrypt.", SwingConstants.CENTER));
    }

    private void chooseFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void loadKey() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            keyField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    private void encryptFile() {
        try {
            File inputFile = new File(inputField.getText());
            File outputFile = new File(inputFile.getParent(), "encrypted_" + inputFile.getName());
            File keyFile = new File(inputFile.getParent(), "aes.key");

            SecretKey key = AESUtil.generateKey();
            AESUtil.encrypt(inputFile, outputFile, key);
            AESUtil.saveKey(key, keyFile);

            JOptionPane.showMessageDialog(this, "Encryption complete!\nEncrypted file: " + outputFile.getName()
                    + "\nKey saved as: " + keyFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during encryption: " + e.getMessage());
        }
    }

    private void decryptFile() {
        try {
            File inputFile = new File(inputField.getText());
            File outputFile = new File(inputFile.getParent(), "decrypted_" + inputFile.getName());
            File keyFile = new File(keyField.getText());

            SecretKey key = AESUtil.loadKey(keyFile);
            AESUtil.decrypt(inputFile, outputFile, key);

            JOptionPane.showMessageDialog(this, "Decryption complete!\nDecrypted file: " + outputFile.getName());
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error during decryption: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileEncryptor().setVisible(true));
    }
}
