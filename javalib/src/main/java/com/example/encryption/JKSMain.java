package com.example.encryption;

import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Enumeration;

/**
 * @author VanceKing
 * @since 2019/10/14.
 */
public class JKSMain {
    private static final String keyPath = "C:\\debug.keystore";

    
    /*private void test1() {
        //a. 创建针对jks文件的输入流
        File cafile = new File(keyPath);
        InputStream inputStream = new FileInputStream(cafile);

        //b. 创建KeyStore实例 （store_password密钥库密码）
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream,"android".toCharArray());

        //c. 获取私钥 （alias 为私钥别名，password为私钥密码）
        PrivateKey pk = (PrivateKey) keyStore.getKey("androiddebugkey","android".toCharArray());

        //d. 用私钥对数据签名
        //获取Signature实例，指定签名算法（本例使用MD5withRSA，可自选）
        Signature dsa = Signature.getInstance("MD5withRSA");

        //加载私钥
        dsa.initSign(privateKey);

        RSAKeyPairGenerator generator = new RSAKeyPairGenerator();

        SecureRandom
    }*/

    private static void test2() throws Exception {
        FileInputStream is = new FileInputStream(new File(keyPath));
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(is, "android".toCharArray());
        Enumeration aliasEnum = keyStore.aliases();
        String keyAlias = "androiddebugkey";
        while (aliasEnum.hasMoreElements()) {
            keyAlias = (String) aliasEnum.nextElement();
            System.out.println("别名: " + keyAlias);
        }

        Certificate certificate = keyStore.getCertificate(keyAlias);
        //加载公钥
        PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();

        //加载私钥,这里填私钥密码
        /*PrivateKey privateKey = ((KeyStore.PrivateKeyEntry) keyStore.getEntry(keyAlias,
                new KeyStore.PasswordProtection("android".toCharArray()))).getPrivateKey();*/

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias, "android".toCharArray());
        //base64输出私钥
        String privateKeyStr = Base64.encodeBase64String(privateKey.getEncoded());
        String publicKeyStr = Base64.encodeBase64String(publicKey.getEncoded());
        System.out.println("私钥：" + privateKeyStr);
        System.out.println("公钥：" + publicKeyStr);

        String msg = "encodeBase64String";

        //测试签名
        String sign = Base64.encodeBase64String(sign(msg.getBytes(), privateKey, "SHA1withRSA", null));
        System.out.println(String.format("\"%s\"签名： %s", msg, sign));

        //测试验签
        boolean verfi = verify(msg.getBytes(), Base64.decodeBase64(sign), publicKey, "SHA1withRSA", null);
        System.out.println(String.format("\"%s\"验签： %b", msg, verfi));
    }

    /**
     * 签名
     */
    public static byte[] sign(byte[] message, PrivateKey privateKey, String algorithm, String provider) throws Exception {
        Signature signature;
        if (null == provider || provider.length() == 0) {
            signature = Signature.getInstance(algorithm);
        } else {
            signature = Signature.getInstance(algorithm, provider);
        }
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }

    /**
     * 验签
     */
    public static boolean verify(byte[] message, byte[] signMessage, PublicKey publicKey, String algorithm,
                                 String provider) throws Exception {
        Signature signature;
        if (null == provider || provider.length() == 0) {
            signature = Signature.getInstance(algorithm);
        } else {
            signature = Signature.getInstance(algorithm, provider);
        }
        signature.initVerify(publicKey);
        signature.update(message);
        return signature.verify(signMessage);
    }
}
