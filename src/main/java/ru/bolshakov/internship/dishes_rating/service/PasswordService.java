package ru.bolshakov.internship.dishes_rating.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class PasswordService implements PasswordEncoder {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String ALGORITHM = "PBKDF2WithHmacSHA1";

    public String encode(CharSequence rawPassword) {
        byte[] salt = generateSalt();
        byte[] encoded = encode(rawPassword, salt);
        return encode(encoded);
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        byte[] digested = decode(encodedPassword);
        byte[] salt = subArray(digested);
        return MessageDigest.isEqual(digested, encode(rawPassword, salt));
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[16];
        SECURE_RANDOM.nextBytes(salt);
        return salt;
    }

    private String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private byte[] encode(CharSequence rawPassword, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(rawPassword.toString().toCharArray(),
                    salt, 65536, 128);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            return concatenate(salt, skf.generateSecret(spec).getEncoded());
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Could not create hash", ex);
        }
    }

    private byte[] decode(String encodedBytes) {
        return Base64.getDecoder().decode(encodedBytes);
    }

    private byte[] subArray(byte[] array) {
        byte[] subarray = new byte[16];
        System.arraycopy(array, 0, subarray, 0, 16);
        return subarray;
    }

    private byte[] concatenate(byte[]... arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }
        byte[] newArray = new byte[length];
        int destPos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, newArray, destPos, array.length);
            destPos += array.length;
        }
        return newArray;
    }
}
