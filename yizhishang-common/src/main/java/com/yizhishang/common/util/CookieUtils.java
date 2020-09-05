package com.yizhishang.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * cookie工具类 功能：主要用于 在用户登陆后，校验cookie中的数据是否被篡改 实现： 1、登陆之后对当前域中的 JSESSIONID的值，进行加密,增加一个cookie进行保存 2、登陆之后的操作，在过滤器中进行处理：在coookie中 获取保存的这个cookie的值，验证JSESSIONID是否被篡改
 *
 * @version 1.0
 * @Date 2018年01月12日 09时23分
 */
public class CookieUtils {

    public static final String split = "|";
    public static final String SAFE_COOKIE_NAME = "gspKey";
    private static String privateEncryptKey = "*scxn%iz53F0Z$87rky6&gnuecH%mzv7Jsglt0sK2S2tOa4pQ$0*x@i6Q5VYhwWoJK2LC#U%4Kwau^kbBCIC188EyYY!*KEvP^a";
    private static String domainSign = "_gsp_gr158_";


    private CookieUtils() {

    }

    /**
     * 加密(对源字符串加密)
     */
    public static String encrypt(String cookieValue) {
        if (!StringUtil.isValid(cookieValue)) {
            return "";
        }
        return md5Encrypt(cookieValue);
    }

    /**
     * 校验 cookieValue的合法性
     *
     * @param cookieValue 加密后的cookie值
     * @return false 表示 cookieValue被篡改，true表示验证成功
     * @version v 1.0
     * @Date 2017-11-16 15:50:31
     */
    public static boolean validate(String cookieValue, String encryptCookieValue) {
        if (!StringUtil.isValid(cookieValue) || !StringUtil.isValid(encryptCookieValue)) {
            return false;
        }
        return encryptCookieValue.equals(md5Encrypt(cookieValue));
    }

    private static String md5Encrypt(String src) {
        String result = MDUtil.md5Encrypt(src + domainSign + privateEncryptKey);
        return MDUtil.md5Encrypt(result);
    }

    public static Cookie setCookie(String name, String value) {
        return setCookie(name, value, (Integer) null);
    }

    public static Cookie setCookie(String name, String value, Integer maxAge) {
        return setCookie(name, value, (String) null, (String) null, maxAge);
    }

    public static Cookie setCookie(String name, String value, String domain, String path, Integer maxAge) {
        return setCookie(false, name, value, domain, path, maxAge);
    }

    public static Cookie setCookie(boolean secure, String name, String value, String domain, String path, Integer maxAge) {
        try {
            Cookie e = new Cookie(name, StringUtil.isNotEmpty(value) ? URLEncoder.encode(value, "UTF-8") : null);
            e.setSecure(secure);
            e.setPath(StringUtil.isEmpty(domain) ? "/" : path);
            if (StringUtil.isNotEmpty(domain)) {
                e.setDomain(domain);
            }

            e.setMaxAge(maxAge == null ? -1 : maxAge.intValue());
            return e;
        } catch (UnsupportedEncodingException var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        HashMap cookieMap = new HashMap();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Cookie[] var6 = cookies;
            int var5 = cookies.length;

            for (int var4 = 0; var4 < var5; ++var4) {
                Cookie cookie = var6[var4];
                cookieMap.put(cookie.getName(), cookie);
            }
        }

        return cookieMap;
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Map cookieMap = readCookieMap(request);
        return cookieMap.containsKey(name) ? (Cookie) cookieMap.get(name) : null;
    }

    public static Object getCookieValue(HttpServletRequest request, String name) {
        try {
            Cookie e = getCookie(request, name);
            return e != null ? (StringUtil.isNotEmpty(e.getValue()) ? URLDecoder.decode(e.getValue(), "UTF-8") : null) : null;
        } catch (UnsupportedEncodingException var3) {
            return null;
        }
    }

    public static void remove(HttpServletResponse response, String name) {
        response.addCookie(setCookie(name, null));
    }

    public static void remove(HttpServletResponse response, String name, String domain, String path) {
        response.addCookie(setCookie(name, (String) null, domain, path, Integer.valueOf(0)));
    }

}