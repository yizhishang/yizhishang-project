package com.yizhishang.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密
 */
public class MDUtil {

    private static MessageDigest messageDigest = null;

    private MDUtil() {

    }

    public static String sha1(String text) {
        if (text == null) {
            return null;
        }
        MessageDigest md;
        String outStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(text.getBytes("utf-8"));
            outStr = byteToString(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return outStr;
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
    public static final synchronized String md5Encrypt(String data) {
        if (messageDigest == null) {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(data.getBytes("utf-8"));
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Failed to load the MD5 MessageDigest. We will be unable to function normally.");
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encodeHex(messageDigest.digest());
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
