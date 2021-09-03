package com.yizhishang.common.util;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * 公钥加密-私钥解密
 * 私钥签名-公钥验签
 *
 * @author 袁永君
 * @since 2019/7/18 13:56
 */
public class RSAUtil {

    /**
     * Base64编码后的公钥(2048)
     */
    public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsSzc5zh45HHDij0o4GvI/V269Z2OEyz5T8pFvVB06hvOSyiSKPu5m9GYXl7tnC2KXAWyfDMqMxIx2WKBSoJyqaaspQiwcevO6MUFuinAwCqkyS8jo6Gi43SGaUqDVW0BYWupiUyvVwIACP4LNRLzWHgmg2LLiIsZWex72yTIsuU4qZOXiu9aOQ/bGLFkEV5xZaPnfudRVPm+u+eamSybIhkooA/Ni0fMAVmrSVaHKkaRlgru9XvxYQBEq2gBbW/6jN4Kw7zchYxow6sweH29c+MbXa6unG+nfAD21LeWfzjCuVVaSriOJnjdRiklxkUadMOHczFMQILmouxz0maT4QIDAQAB";

    /**
     * Base64编码后的私钥(2048)
     */
    public static final String PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCxLNznOHjkccOKPSjga8j9Xbr1nY4TLPlPykW9UHTqG85LKJIo+7mb0ZheXu2cLYpcBbJ8MyozEjHZYoFKgnKppqylCLBx687oxQW6KcDAKqTJLyOjoaLjdIZpSoNVbQFha6mJTK9XAgAI/gs1EvNYeCaDYsuIixlZ7HvbJMiy5Tipk5eK71o5D9sYsWQRXnFlo+d+51FU+b6755qZLJsiGSigD82LR8wBWatJVocqRpGWCu71e/FhAESraAFtb/qM3grDvNyFjGjDqzB4fb1z4xtdrq6cb6d8APbUt5Z/OMK5VVpKuI4meN1GKSXGRRp0w4dzMUxAguai7HPSZpPhAgMBAAECggEAZJTYb3dSwjWHUbI4w3bNKnEMkyU3o9EzU8W746aKSOEyMt68YwHMtBW/z0tKzw/XbThaxEFzonygyg0u4vK9T2xstGADcxjwZJp9DttnLnDHDeNx8xHu2/up1ppThqqUTw8EtwpOvDFJfdFi4WV5um6zRoFOlJyK7s7e3WC7gzrGHTN8EAOR+SbMVo11okWdeAabD/2XF8K3EWaYU/SaIu7unJq5fMigUM9G9KX+L+LsjhyFQfpbZSGdk2IXfpl0bvboS/hidqBrpnr6UYCNPUNtwDyel9zhkLUI4bQVtnnrIMU6IpmcZS7/KkfBO+WSY9OvgFxRnXExmOptQlNGVQKBgQDZVWs5LQLcElU2bLCnszJ/7Jr4SX9YTlr7a8BPXZ2MwmX3qemx4hnyD7Hzr1ruPa7NQ5A/W7HAGCmt++zOndvYOiPoWHzQ43WUTRSQRV8Mzhr1mjbg8VYwWnZ6OA9WN52kacicNOSIpAkHXP6J1u4Ar7NVVcD6FCBVlGTwU4g9lwKBgQDQsmgeDHUsSRYiQfwm5CxmL/QVwfLjeQkNQNpZCeMHW84SHjvrZv+zf3pf1mPAytd+Z/swa2i1sDVW1OpBX2wKGZDjoCpqhrDOdPza6sat9yD+A/dMf62q+UjAHwjnG4z8654W87G/kVE7+ob5L5Snf2bNVOf1IgYb7lyI9exZRwKBgQC3/86ImKgMI+VK75hcUqWVkn7daHRNEgGLTP2C9BIYAtmLZao59N/chRc25aZJf6WHmQT97ya9KHfZGimyAAoJtaeC5a++tmRsixm2z1SapcBDYS3HIevDgkM+VEn/x5RKh1jVjx5Q5PfT6RKDJplXsWlV/QUldHavnbQbl80HvwKBgDdiIR9YTwAhpS4SNMhdZSr5Lt1XSw3y4OQTgd5/MTCSvG0yeNRP0SvheKhWTMKtEeMJ1S9UUcVlrcINXt1aZWGIx1qg9qCufd1MTo4KIA+qtaazhr1WZNAqt5PEqKgsN44nORz+Lv2Xouc35PncFqccuDtlfOkgzl47M3NlUAOhAoGBAIiGK0nCxxI8IKnkLIqTlRNYApilHfSwQJBrbOY3OMEFE9HhZOYFzcc37lAruV9VM5MGdbRQaEp/GNGxlMEnfQr6I3JVN4gyt/nP21rVBmQ9YqRDxifPkZbSt29k71h/50BmaF5IgnLB/XuPjsdI4BWLBBbarvs6qAsBCtlEH7FH";

    private static final String RSA = "RSA";

    /**
     * 字节数组转Base64编码
     */
    private static final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * Base64编码转字节数组
     */
    private static final Base64.Decoder decoder = Base64.getDecoder();

    private RSAUtil() {

    }

    /**
     * 生成KeyPair对象，包含公钥和私钥
     *
     * @param bit key长度
     * @return KeyPair
     * @throws Exception 异常
     */
    public static KeyPair getKeyPair(int bit) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
        keyPairGenerator.initialize(bit);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 获取base64位之后的公钥字符串
     *
     * @param keyPair KeyPair对象
     * @return 公钥
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return encoder.encodeToString(bytes);
    }

    /**
     * 获取base64位之后的私钥字符串
     *
     * @param keyPair KeyPair对象
     * @return 私钥
     */
    public static String getPrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return encoder.encodeToString(bytes);
    }

    /**
     * 将Base64编码后的公钥转换成PublicKey对象
     *
     * @param publicKey base64编码后的公钥字符串
     * @return PublicKey 公钥对象
     * @throws Exception 异常
     */
    public static PublicKey string2PublicKey(String publicKey) throws Exception {
        byte[] keyBytes = decoder.decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将Base64编码后的私钥转换成PrivateKey对象
     *
     * @param privateKey Base64编码后的私钥字符串
     * @return PrivateKey 私钥对象
     * @throws Exception 异常
     */
    public static PrivateKey string2PrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = decoder.decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 公钥加密
     *
     * @param content 明文
     * @return String 密文
     * @throws Exception 异常
     */
    public static String publicEncrypt(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        // 将Base64编码后的公钥转换成PublicKey对象
        cipher.init(Cipher.ENCRYPT_MODE, string2PublicKey(PUBLIC_KEY));
        byte[] publicEncrypt = cipher.doFinal(content.getBytes());
        return encoder.encodeToString(publicEncrypt);
    }

    /**
     * 公钥加密
     *
     * @param content   明文
     * @param publicKey base64后的公钥字符串
     * @return String 密文
     * @throws Exception 异常
     */
    public static String publicEncrypt(String content, String publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        // 将Base64编码后的公钥转换成PublicKey对象
        cipher.init(Cipher.ENCRYPT_MODE, string2PublicKey(publicKey));
        byte[] publicEncrypt = cipher.doFinal(content.getBytes());
        return encoder.encodeToString(publicEncrypt);
    }

    /**
     * 公钥加密
     *
     * @throws Exception 异常
     */
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 公钥验签
     *
     * @throws Exception 异常
     */
    public static byte[] publicDecrypt(byte[] content, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        return cipher.doFinal(content);
    }

    /**
     * 公钥验签
     *
     * @param content 签名数据
     * @return 明文
     * @throws Exception 异常
     */
    public static String publicDecrypt(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, string2PublicKey(PUBLIC_KEY));
        byte[] result = cipher.doFinal(decoder.decode(content));
        return new String(result);
    }

    /**
     * 公钥验签
     *
     * @param content   签名数据
     * @param publicKey base64后公钥字符串
     * @return 明文
     * @throws Exception 异常
     */
    public static String publicDecrypt(String content, String publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, string2PublicKey(publicKey));
        byte[] result = cipher.doFinal(decoder.decode(content));
        return new String(result);
    }

    /**
     * 私钥签名
     *
     * @throws Exception 异常
     */
    public static byte[] privateEncrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

    /**
     * 私钥签名
     *
     * @param content 明文
     * @return 加密后的内容Base64编码
     * @throws Exception 异常
     */
    public static String privateEncrypt(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, string2PrivateKey(PRIVATE_KEY));
        byte[] result = cipher.doFinal(content.getBytes());
        return encoder.encodeToString(result);
    }

    /**
     * 私钥签名
     *
     * @param content    明文
     * @param privateKey base64后端私钥字符串
     * @return 加密后的内容Base64编码
     * @throws Exception 异常
     */
    public static String privateEncrypt(String content, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.ENCRYPT_MODE, string2PrivateKey(privateKey));
        byte[] result = cipher.doFinal(content.getBytes());
        return encoder.encodeToString(result);
    }

    /**
     * 私钥解密
     *
     * @param content 密文
     * @return 明文
     * @throws Exception 异常
     */
    public static String privateDecrypt(String content) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        // 将Base64编码后的私钥转换成PrivateKey对象
        cipher.init(Cipher.DECRYPT_MODE, string2PrivateKey(PRIVATE_KEY));
        byte[] result = cipher.doFinal(decoder.decode(content));
        return new String(result);
    }

    /**
     * 私钥解密
     *
     * @param content    密文
     * @param privateKey 私钥字符串
     * @return 明文
     * @throws Exception 异常
     */
    public static String privateDecrypt(String content, String privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        // 将Base64编码后的私钥转换成PrivateKey对象
        cipher.init(Cipher.DECRYPT_MODE, string2PrivateKey(privateKey));
        byte[] result = cipher.doFinal(decoder.decode(content));
        return new String(result);
    }

    /**
     * 私钥解密
     *
     * @param content    密文
     * @param privateKey 私钥字符串
     * @return 明文字节数组
     * @throws Exception 异常
     */
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(content);
    }

}
