package fileencryptor;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;

public class AESUtil {

    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // 128-bit AES
        return keyGen.generateKey();
    }

    public static void saveKey(SecretKey key, File file) throws Exception {
        byte[] keyBytes = key.getEncoded();
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(keyBytes);
        }
    }

    public static SecretKey loadKey(File file) throws Exception {
        byte[] keyBytes = new byte[16];
        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(keyBytes);
        }
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static void encrypt(File inputFile, File outputFile, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        processFile(cipher, inputFile, outputFile);
    }

    public static void decrypt(File inputFile, File outputFile, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        processFile(cipher, inputFile, outputFile);
    }

    private static void processFile(Cipher cipher, File in, File out) throws Exception {
        try (FileInputStream fis = new FileInputStream(in);
             FileOutputStream fos = new FileOutputStream(out);
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {

            byte[] buffer = new byte[4096];
            int read;
            while ((read = fis.read(buffer)) != -1) {
                cos.write(buffer, 0, read);
            }
        }
    }
}
