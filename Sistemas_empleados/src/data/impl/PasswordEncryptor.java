package data.impl;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class PasswordEncryptor {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static String encryptPassword(String password, byte[] salt) {
        byte[] hash = hashPassword(password.toCharArray(), salt);
        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String storedHash, String storedSalt) {
        byte[] salt = Base64.getDecoder().decode(storedSalt);
        byte[] hash = hashPassword(password.toCharArray(), salt);
        return Base64.getEncoder().encodeToString(hash).equals(storedHash);
    }

    private static byte[] hashPassword(char[] password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Error al cifrar la contraseña: " + e.getMessage(), e);
        } finally {
            clearPassword(password);
        }
    }

    static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    private static void clearPassword(char[] password) {
        for (int i = 0; i < password.length; i++) {
            password[i] = '\0';
        }
    }
}