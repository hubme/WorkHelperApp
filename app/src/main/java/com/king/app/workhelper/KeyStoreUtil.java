package com.king.app.workhelper;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

/**
 * https://gitee.com/huangxiaoguo/androidGeZhongJiaMiZongJie
 *
 * @author VanceKing
 * @since 19-10-13.
 */
public class KeyStoreUtil {
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final String TYPE_RSA = "RSA";
    public static final String ECB_PKCS1_PADDING = "RSA/ECB/PKCS1Padding";//加密填充方式
    public static final int DEFAULT_KEY_SIZE = 2048;//秘钥默认长度

    private KeyStoreUtil() {

    }

    public static KeyPair generateRSAKeyPair(Context context, @NonNull String keystoreAlias) throws Exception {
        Calendar start = new GregorianCalendar();
        Calendar end = new GregorianCalendar();
        end.add(Calendar.YEAR, 100);
        AlgorithmParameterSpec spec;
        //KeyPairGeneratorSpec在Api 23以上已经废弃，Api level 23以上使用KeyGenParameterSpec。
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(keystoreAlias)
                    // 用于生成自签名证书的主题 X500Principal 接受 RFC 1779/2253的专有名词
                    // eg: "CN=Duke, OU=JavaSoft, O=Sun Microsystems, C=US"
                    .setSubject(new X500Principal("CN=" + keystoreAlias))
                    //用于自签名证书的序列号生成的一对。
                    .setSerialNumber(BigInteger.valueOf(1337))
                    // 签名在有效日期范围内
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
        } else {
            spec = new KeyGenParameterSpec.Builder(keystoreAlias,
                    KeyProperties.PURPOSE_SIGN
                            | KeyProperties.PURPOSE_VERIFY
                            | KeyProperties.PURPOSE_ENCRYPT
                            | KeyProperties.PURPOSE_DECRYPT)
                    .setKeySize(DEFAULT_KEY_SIZE)
                    .setUserAuthenticationRequired(false)
                    .setCertificateSubject(new X500Principal("CN=" + keystoreAlias))
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA1)
                    .setCertificateNotBefore(start.getTime())
                    .setCertificateNotAfter(end.getTime())
                    .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .build();
        }
        KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance(TYPE_RSA, ANDROID_KEY_STORE);
        kpGenerator.initialize(spec);
        return kpGenerator.generateKeyPair();
    }

    public static boolean isAliasExist(@NonNull String keystoreAlias) {
        try {
            KeyStore ks = KeyStore.getInstance(ANDROID_KEY_STORE);
            ks.load(null);
            return ks.containsAlias(keystoreAlias);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String encryptByPublicKey(byte[] publicKey, String textToEncrypt) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory kf = KeyFactory.getInstance(TYPE_RSA);
            PublicKey keyPublic = kf.generatePublic(keySpec);
            Cipher cp = Cipher.getInstance(ECB_PKCS1_PADDING);
            cp.init(Cipher.ENCRYPT_MODE, keyPublic);
            byte[] bytes = cp.doFinal(textToEncrypt.getBytes());
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByPrivateKey(PrivateKey privateKey, String textToDecrypt) {
        try {
            Cipher cipher = Cipher.getInstance(ECB_PKCS1_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            final byte[] decodedData = cipher.doFinal(Base64.decode(textToDecrypt, Base64.DEFAULT));
            return new String(decodedData, Charset.defaultCharset());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
