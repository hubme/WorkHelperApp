package com.king.applib.util;

import android.util.Base64;

import androidx.annotation.NonNull;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author VanceKing
 * @since 19-9-8.
 */
public class EncryptionUtil {
    private static final String DES = "DES";
    private static final String AES = "AES";
    private static final String RSA = "RSA";

    private EncryptionUtil() {
        throw new UnsupportedOperationException("No instances!");
    }

    public static String encryptByAES(String data, String key) {
        try {
            SecretKeySpec secret = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), AES);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            return Base64.encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByAES(String encrypt, String secretKey) {
        try {
            SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), AES);
            Cipher cipher = Cipher.getInstance(AES);
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

    /**
     * 使用 RSA 公钥进行加密，只有对应的私钥才能解密。
     *
     * @param publicKey 经过 Base64 编码的 RSA 公钥
     * @param content   待加密的明文
     * @return 加过密的密文
     */
    public static String encryptByRSAPublicKey(String publicKey, String content) {
        try {
            byte[] buffer = Base64.decode(publicKey, Base64.DEFAULT);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(keySpec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptData = cipher.doFinal(content.getBytes(Charset.defaultCharset()));
            return Base64.encodeToString(encryptData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成指定安全的指定长度的随机数
     *
     * @param length 随机数的长度
     * @return 随机数
     */
    public static String generateSecureRandom(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("error: SecureRandom length < 0");
        }
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        StringBuilder sb = new StringBuilder(length);
        for (byte b : randomBytes) {
            sb.append(Math.abs(Byte.valueOf(b).intValue()) % 10);
        }
        return sb.toString();
    }

    /**
     * DES 加密。
     *
     * @param data 需要解密的数据
     * @param key  密钥
     * @return Base64 编码后的字符串
     */
    public static String encryptByDES(@NonNull String data, @NonNull String key) {
        try {
            // 生成一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key.getBytes());

            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey secureKey = keyFactory.generateSecret(dks);

            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);

            // 用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, secureKey, sr);

            byte[] bt = cipher.doFinal(data.getBytes());
            return Base64.encodeToString(bt, Base64.DEFAULT);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException |
                NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DES 解密
     *
     * @param data 需要解密的 Base64 字符串
     * @param key  DES 密钥
     * @return 明文
     */
    public static String decryptByDes(String data, String key) {
        byte[] dataBytes = Base64.decode(data, Base64.DEFAULT);
        byte[] bt = decryptByDes(dataBytes, key.getBytes());
        return new String(bt);
    }

    private static byte[] decryptByDes(byte[] data, byte[] key) {
        try {
            // 生成一个可信任的随机数源
            SecureRandom sr = new SecureRandom();

            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);

            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);

            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);

            // 用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

            return cipher.doFinal(data);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException |
                NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static KeyPair generateRSAKeyPair(String algorithm, int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
            keyPairGenerator.initialize(keySize);
            return keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptByRSA(String algorithm, PublicKey publicKey, String data) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByRSA(String algorithm, PrivateKey privateKey, String encryptData) {
        try {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] bytes = Base64.decode(encryptData, Base64.DEFAULT);
            return new String(cipher.doFinal(bytes));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
                IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
