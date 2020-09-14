package com.yizhishang.common.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author 袁永君
 * @date 2019/7/18 13:56
 */
public class RSAUtil {

    private static final String RSA = "RSA";

    private RSAUtil() {

    }

    /**
     * 生成RSA
     *
     * @throws Exception
     */
    public static KeyPair getKeyPair(int bit) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(bit);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取公钥
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    /**
     * 获取私钥(Base64编码)
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    /**
     * 将Base64编码后的公钥转换成PublicKey对象
     *
     * @throws Exception
     */
    public static PublicKey string2PublicKey(String pubStr) throws Exception {
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将Base64编码后的私钥转换成PrivateKey对象
     *
     * @throws Exception
     */
    public static PrivateKey string2PrivateKey(String priStr) throws Exception {
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 公钥加密
     *
     * @throws Exception
     */
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 公钥验签
     *
     * @throws Exception
     */
    public static byte[] publicDecrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 私钥签名
     *
     * @throws Exception
     */
    public static byte[] privateEncrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    /**
     * 私钥解密
     *
     * @throws Exception
     */
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    /**
     * 字节数组转Base64编码
     */
    public static String byte2Base64(byte[] bytes) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(bytes);
    }

    /**
     * Base64编码转字节数组
     */
    public static byte[] base642Byte(String base64Key) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(base64Key);
    }
}
