package com.yizhishang.common.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密
 *
 * @author yizhishang
 */
@Slf4j
public class MDUtil {

    private static volatile MessageDigest md5MessageDigest = null;
    private static volatile MessageDigest sha1MessageDigest = null;

    private MDUtil() {

    }

    private static void initSha1MessageDigest() throws NoSuchAlgorithmException {
        if (sha1MessageDigest == null) {
            synchronized (MDUtil.class) {
                if (sha1MessageDigest == null) {
                    sha1MessageDigest = MessageDigest.getInstance("SHA-1");
                }
            }
        }
    }

    public static String sha1(String text) throws NoSuchAlgorithmException {
        if (text == null) {
            return null;
        }
        initSha1MessageDigest();
        byte[] digest = sha1MessageDigest.digest(text.getBytes(StandardCharsets.UTF_8));
        return byteToString(digest);
    }

    private static String byteToString(byte[] digest) {
        StringBuilder buf = new StringBuilder();
        for (byte b : digest) {
            String tempStr = Integer.toHexString(b & 0xff);
            if (tempStr.length() == 1) {
                buf.append("0").append(tempStr);
            } else {
                buf.append(tempStr);
            }
        }
        return buf.toString().toLowerCase();
    }


    /**
     * MD5加密
     */
    public static final String md5Encrypt(String data) throws NoSuchAlgorithmException {
        initMd5MessageDigest();
        md5MessageDigest.update(data.getBytes(StandardCharsets.UTF_8));
        return encodeHex(md5MessageDigest.digest());
    }

    private static void initMd5MessageDigest() throws NoSuchAlgorithmException {
        if (md5MessageDigest == null) {
            synchronized (MDUtil.class) {
                if (md5MessageDigest == null) {
                    md5MessageDigest = MessageDigest.getInstance("MD5");
                }
            }
        }
    }

    private static final String encodeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; ++i) {
            if ((bytes[i] & 255) < 16) {
                buf.append("0");
            }
            buf.append(Long.toString((long) (bytes[i] & 255), 16));
        }
        return buf.toString().toUpperCase();
    }

}
