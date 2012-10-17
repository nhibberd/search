package main.tool;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static main.tool.Base64.*;

public class Encryptor {
    public static String encrypt(String plaintext, String key) {
        Cipher cipher = cipher();
        init(cipher, Cipher.ENCRYPT_MODE, spec(key));
        byte[] raw = stringToBase64Bytes(plaintext);
        byte[] r = dofinal(cipher, raw);
        return byteToBase64(r);
    }

    public static String decrypt(String ciphertext, String key) {
        Cipher cipher = cipher();
        init(cipher, Cipher.DECRYPT_MODE, spec(key));
        byte[] raw = base64ToByte(ciphertext);
        byte[] r = dofinal(cipher, raw);
        return base64BytesToString(r);
    }

    private static void init(Cipher cipher, int mode, SecretKeySpec spec) {
        try {
            cipher.init(mode, spec);
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static byte[] dofinal(Cipher cipher, byte[] raw) {
        try {
            return cipher.doFinal(raw);
        } catch (IllegalBlockSizeException e) {
            throw new IllegalArgumentException(e);
        } catch (BadPaddingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static SecretKeySpec spec(String key) {
        return new SecretKeySpec(base64ToByte(key), "AES");
    }
    private static Cipher cipher(){
        try {
            return Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchPaddingException e) {
            throw new IllegalStateException(e);
        }
    }
}
