package com.king.applib.util;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author VanceKing
 * @since 19-9-8.
 */
public class EncryptionUtil {
    private EncryptionUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static String encryptByAES(String content, String secretKey) {
        try {
            SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return Base64.encodeToString(cipher.doFinal(content.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByAES(String encrypt, String secretKey) {
        try {
            SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(Base64.decode(encrypt, Base64.DEFAULT)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 随机生成 AES 字符串
     *
     * @param length 128, 192或256
     * @return AES 字符串
     */
    public static String generatorAESKey(int length) {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(length);//要生成多少位，只需要修改这里即可128, 192或256  
            SecretKey sk = kg.generateKey();
            byte[] b = sk.getEncoded();
            return byteToHexString(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteToHexString(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte hashInByte : hashInBytes) {
            String hex = Integer.toHexString(0xff & hashInByte);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();

    }
}
