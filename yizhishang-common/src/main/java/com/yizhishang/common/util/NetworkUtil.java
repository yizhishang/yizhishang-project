package com.yizhishang.common.util;

import com.google.common.collect.Maps;
import com.yizhishang.common.enums.BrowserEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yizhishang
 * @version 1.0
 * @since 2018年01月15日 14时05分
 */
@Slf4j
public class NetworkUtil {

    private static final String OS_AND_DEVICE_REGEX = "\\([A-Za-z0-9_\\s;\\.\\-\\/:]+\\)";
    private static final String OTHER_REGEX = "\\([A-Za-z0-9_\\s,]+\\)";
    private static final String UNKNOWN = "UNKNOWN";

    /**
     * 匹配IOS系统及其版本的正则表达式
     */
    private static final String IOS_REGEX = "(?=iPhone)(?=\\s*)(?=[0-9_]*)[\\w_\\s]+";

    private static final String ANDROID = "Android";

    /**
     * 匹配安卓系统及其版本的正则表达式
     */
    private static final String ANDROID_REGEX = "(?=Android)(?=[0-9]*)(?=\\s*)(?=\\.*)[A-Za-z0-9_\\s\\.]+";

    private NetworkUtil() {

    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request 请求的request
     * @return ipv4地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址

        String ip = request.getHeader("X-Forwarded-For");
        if (log.isDebugEnabled()) {
            log.debug("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip={}", ip);
        }

        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                if (log.isDebugEnabled()) {
                    log.debug("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip={}", ip);
                }
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                if (log.isDebugEnabled()) {
                    log.debug("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip={}", ip);
                }
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                if (log.isDebugEnabled()) {
                    log.debug("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip={}", ip);
                }
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                if (log.isDebugEnabled()) {
                    log.debug("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip={}", ip);
                }
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (log.isDebugEnabled()) {
                    log.debug("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip={}", ip);
                }
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String ipStr : ips) {
                if (!(UNKNOWN.equalsIgnoreCase(ipStr))) {
                    ip = ipStr;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 获取浏览器版本信息
     */
    public static Map<String, String> getBrowserInfo(HttpServletRequest request) {
        String agent = request.getHeader("user-agent");
        return getBrowserInfo(agent);
    }

    /**
     * 获取浏览器版本信息
     */
    public static Map<String, String> getBrowserInfo(String agent) {
        if (StringUtils.isEmpty(agent)) {
            return null;
        }
        String browserSrc = agent.replaceAll(OS_AND_DEVICE_REGEX, "");
        browserSrc = browserSrc.replaceAll(OTHER_REGEX, "");

        String[] strList = browserSrc.split(" ");
        String[] temp = {};

        Map<String, String> map = Maps.newHashMap();
        for (String str : strList) {
            if (StringUtils.isEmpty(str)) {
                continue;
            }
            if (str.contains("/")) {
                temp = str.split("/");
                if (null != temp && temp.length == 2) {
                    map.put(temp[0], temp[1]);
                }
            } else {
                map.put(str, str);
            }
        }
        String netType = map.containsKey("NetType") ? map.get("NetType") : "";

        String broswer = "";
        String broswerVersion = "";
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("netType", netType);

        for (Map.Entry<String, String> entry : map.entrySet()) {
            //            浏览器信息
            if (entry.getKey().equals("MicroMessenger") && agent.contains("WindowsWechat")) {

                // PC版微信浏览器
                broswer = "MicroMessenger WindowsWechat";
                broswerVersion = entry.getValue();
                break;
            }

            // 微信浏览器
            if (entry.getKey().equals(BrowserEnum.MICRO_MESSENGER.getMessage()) && null != temp && temp.length == 2) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }

            // qq浏览器
            if (entry.getKey().equals(BrowserEnum.TENCENT_TRAVELER.getMessage()) && null != temp && temp.length == 2) {

                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }

            if (agent.contains(BrowserEnum.YIXIN.getMessage())) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }
            if (agent.contains("360SE")) {
                broswer = "360SE";
                break;
            }
            if (agent.contains("360EE")) {
                broswer = "360EE";
                break;
            }

            // 搜狗浏览器
            if (agent.contains("MetaSr") || agent.contains("SE")) {
                broswer = "SouGou Explorer";
                break;
            }

            // 世界之窗浏览器
            if (agent.contains("TheWorld") || entry.getKey().equals("The world")) {
                broswer = "TheWorld";
                break;
            }

            // 傲游浏览器
            if (agent.contains("Maxthon")) {
                broswer = "Maxthon";
                break;
            }
            if (entry.getKey().equals("Opera")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }
            if (entry.getKey().equals("Operamobi")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }

            // 淘宝浏览器
            if (entry.getKey().equals("TaoBrowser")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }

            // 百度浏览器
            if (entry.getKey().equals("BIDUBrowser")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }

            // 猎豹浏览器
            if (entry.getKey().equals("LBBROWSER")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }
            if (agent.contains("UCBrowser") || agent.contains("UCWEB")) {
                broswer = "UCBrowser";
                break;
            }

            // 小米浏览器
            if (entry.getKey().equals("XiaoMi") || entry.getKey().equals("MiuiBrowser")) {
                broswer = "XiaoMi";
                break;
            }

            if (entry.getKey().equals("MQQBrowser") || entry.getKey().equals("QQBrowser") || entry.getKey().equals("QQ")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }

            if (entry.getKey().equals("Navigator")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }
            if (entry.getKey().equals("Firefox")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }
            if (entry.getKey().equals("Chrome")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }
            if (entry.getKey().equals("Safari")) {
                broswer = entry.getKey();
                broswerVersion = entry.getValue();
                break;
            }
        }
        if (!StringUtils.isEmpty(broswer) && !StringUtils.isEmpty(broswerVersion)) {
            resultMap.put("broswer", broswer);
            resultMap.put("broswerVersion", broswerVersion);
            return resultMap;
        }

        // 所有主流的浏览器都判断完了，现在开始判断IE浏览器了
        if (agent.contains("MSIE")) {
            broswer = "IE";
            if (agent.contains("MSIE 5")) {
                broswerVersion = "5";
            } else if (agent.contains("MSIE 6")) {
                broswerVersion = "6";
            } else if (agent.contains("MSIE 7")) {
                broswerVersion = "7";
            } else if (agent.contains("MSIE 8")) {
                broswerVersion = "8";
            } else if (agent.contains("MSIE 9")) {
                broswerVersion = "9";
            } else if (agent.contains("MSIE 10")) {
                broswerVersion = "10";
            }
        } else if (agent.contains("rv:11") && agent.contains("like Gecko")) {
            broswer = "IE";
            broswerVersion = "11";
        }

        resultMap.put("broswer", broswer);
        resultMap.put("broswerVersion", broswerVersion);
        return resultMap;
    }


    /**
     * 获取系统版本信息
     */
    public static Map<String, String> getRequestSystemInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("user-agent");
        return getRequestSystemInfo(userAgent);
    }

    /**
     * 获取操作系统信息
     */
    public static Map<String, String> getRequestSystemInfo(String userAgent) {
        if (StringUtils.isEmpty(userAgent)) {
            return null;
        }

        //得到用户的操作系统
        String osAndDeviceStr = RegexUtil.findMatchContent(OS_AND_DEVICE_REGEX, userAgent);

        String os = "";
        String userOS = "";
        String deviceInfo = "";
        if (!StringUtils.isEmpty(osAndDeviceStr)) {

            if (osAndDeviceStr.contains("Windows Phone")) {
                deviceInfo = "Mobile";
                os = "Windows Phone";
            }

            if (osAndDeviceStr.contains("Windows NT")) {
                deviceInfo = "PC";
                if (osAndDeviceStr.contains("Windows NT 6.4")) {
                    os = "Windows 10";
                }
                if (osAndDeviceStr.contains("Windows NT 6.3")) {
                    os = "Windows 8.1";
                }
                if (osAndDeviceStr.contains("Windows NT 6.2")) {
                    os = "Windows 8";
                }
                if (osAndDeviceStr.contains("Windows NT 6.1")) {
                    os = "Windows 7";
                }
                if (osAndDeviceStr.contains("Windows NT 6.0")) {
                    os = "Windows vista";
                }
                if (osAndDeviceStr.contains("Windows NT 5.2")) {
                    os = "Windows 2003";
                }
                if (osAndDeviceStr.contains("Windows NT 5.1")) {
                    os = "Windows XP";
                }
                if (osAndDeviceStr.contains("Windows NT 5.0")) {
                    os = "Windows 2000";
                }
            }

            if (osAndDeviceStr.contains("Mac OS")) {
                os = "Mac OS";
                userOS = "IOS";
                if (osAndDeviceStr.contains("iPhone 6sp")) {
                    deviceInfo = "iPhone 6s Plus";
                }

                if (osAndDeviceStr.contains("iPhone 6p")) {
                    deviceInfo = "iPhone 6 Plus";
                }
                if (osAndDeviceStr.contains("iPhone 6s")) {
                    deviceInfo = "iPhone 6s";
                }
                if (osAndDeviceStr.contains("iPhone 5C")) {
                    deviceInfo = "iPhone 5C";
                }
                if (osAndDeviceStr.contains("iPhone 5S")) {
                    deviceInfo = "iPhone 5S";
                }

                if (osAndDeviceStr.contains("iPhone")) {
                    deviceInfo = "iPhone";
                }
                if (osAndDeviceStr.contains("iPad")) {
                    deviceInfo = "iPad";
                }
                if (osAndDeviceStr.contains("iPod")) {
                    deviceInfo = "iPod";
                }

            }

            if (osAndDeviceStr.contains("Linux")) {
                os = "Linux";

                if (osAndDeviceStr.contains(ANDROID)) {
                    String matchResult = RegexUtil.findMatchContent(ANDROID_REGEX, osAndDeviceStr);
                    if (!StringUtils.isEmpty(matchResult) && matchResult.contains(ANDROID)) {
                        userOS = matchResult;
                    } else {
                        userOS = ANDROID;
                    }
                }

                if (osAndDeviceStr.contains("HUAWEI") || osAndDeviceStr.contains("HONOR")) {
                    deviceInfo = "HUAWEI";
                }
                if (osAndDeviceStr.contains("OPPO")) {
                    deviceInfo = "OPPO";
                }
                if (osAndDeviceStr.contains("vivo")) {
                    deviceInfo = "vivo";
                }
                if (osAndDeviceStr.contains("HTC")) {
                    deviceInfo = "HTC";
                }
                // 中兴
                if (osAndDeviceStr.contains("ZTE")) {
                    deviceInfo = "ZTE";
                }

                // 三星
                if (osAndDeviceStr.contains("SAMSUNG") || osAndDeviceStr.contains("SM-") || osAndDeviceStr.contains("SCH--")) {
                    deviceInfo = "SamSung";
                }

                //谷歌
                if (osAndDeviceStr.contains("Nexus")) {
                    deviceInfo = "Nexus";
                }

                //小米系列
                if (osAndDeviceStr.contains("Mi Note 2")) {
                    deviceInfo = "Mi Note 2";
                }
                if (osAndDeviceStr.contains("MI NOTE")) {
                    deviceInfo = "MI NOTE";
                }
                if (osAndDeviceStr.contains("MI MAX")) {
                    deviceInfo = "MI MAX";
                }
                if (osAndDeviceStr.contains("MI PAD")) {
                    deviceInfo = "MI PAD";
                }
                if (osAndDeviceStr.contains("Redmi Note 4X")) {
                    deviceInfo = "Redmi Note 4X";
                }
                if (osAndDeviceStr.contains("Redmi Note 4")) {
                    deviceInfo = "Redmi Note 4";
                }
                if (osAndDeviceStr.contains("Redmi Note 3")) {
                    deviceInfo = "Redmi Note 3";
                }
                if (osAndDeviceStr.contains("Redmi Note 2")) {
                    deviceInfo = "Redmi Note 2";
                }
                if (osAndDeviceStr.contains("Redmi 4A")) {
                    deviceInfo = "Redmi 4A";
                }
                if (osAndDeviceStr.contains("Redmi 4X")) {
                    deviceInfo = "Redmi 4X";
                }
                if (osAndDeviceStr.contains("Redmi 4")) {
                    deviceInfo = "Redmi 4";
                }
                if (osAndDeviceStr.contains("Redmi 3S")) {
                    deviceInfo = "Redmi 3S";
                }
                if (osAndDeviceStr.contains("Redmi 3X")) {
                    deviceInfo = "Redmi 3X";
                }
                if (osAndDeviceStr.contains("Redmi")) {
                    deviceInfo = "Redmi";
                }
                if (osAndDeviceStr.contains("HM NOTE 1S")) {
                    deviceInfo = "Redmi NOTE 1S";
                }
                if (osAndDeviceStr.contains("HM 2A")) {
                    deviceInfo = "Redmi 2A";
                }
                if (osAndDeviceStr.contains("MI 5s Plus")) {
                    deviceInfo = "MI 5s Plus";
                }
                if (osAndDeviceStr.contains("MI 5s")) {
                    deviceInfo = "MI 5s";
                }
                if (osAndDeviceStr.contains("MI 5")) {
                    deviceInfo = "MI 5";
                }
                if (osAndDeviceStr.contains("Mi-4c")) {
                    deviceInfo = "Mi-4c";
                }
                if (osAndDeviceStr.contains("MI 4S")) {
                    deviceInfo = "MI 4S";
                }
                if (osAndDeviceStr.contains("MI 4")) {
                    deviceInfo = "MI 4";
                }
                if (osAndDeviceStr.contains("MI 3W")) {
                    deviceInfo = "MI 3W";
                }
                if (osAndDeviceStr.contains("MI 3C")) {
                    deviceInfo = "MI 3C";
                }
                if (osAndDeviceStr.contains("MI 3")) {
                    deviceInfo = "MI 3";
                }
                if (osAndDeviceStr.contains("MI 2S")) {
                    deviceInfo = "MI 2S";
                }
                if (osAndDeviceStr.contains("MI 2")) {
                    deviceInfo = "MI 2";
                }
            }

            if (osAndDeviceStr.contains("BlackBerry")) {
                os = "BlackBerry";
            }

            if (osAndDeviceStr.contains("SymbianOS")) {
                os = "SymbianOS";
                if (osAndDeviceStr.contains("Nokia")) {
                    deviceInfo = "Nokia";
                }
            }

            if (osAndDeviceStr.contains("WebOs")) {
                os = "WebOs";
            }

            if (osAndDeviceStr.contains("Unix")) {
                os = "Unix";
            }

            if (osAndDeviceStr.contains("SunOS")) {
                os = "SunOS";
            }

        }
        Map<String, String> map = new HashMap<>(3);
        map.put("os", os);
        map.put("userOS", userOS);
        map.put("deviceInfo", deviceInfo);
        return map;
    }


    /**
     * 通过ip地址 获取所在地理区域
     */
    public static String getAreaInfoByIp(String ip) {
        return getAreaInfoByIp(ip, String.valueOf(StandardCharsets.UTF_8));
    }

    /**
     * 通过ip地址 获取所在地理区域
     *
     * @param encodingString 服务器端请求编码。如GBK,UTF-8等
     */
    public static String getAreaInfoByIp(String ip, String encodingString) {
        String content = "ip=" + ip;
        String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = getResult(urlStr, content, encodingString);
        if (StringUtils.isEmpty(returnStr)) {
            return "";
        }
        try {
            // 处理返回的省市区信息
            log.info("从淘宝中返回的原始信息：{}", returnStr);
            String[] temp = returnStr.split(",");
            if (temp.length < 3) {
                return "0";
            }
            // 省份
            String region = (temp[5].split(":"))[1].replaceAll("\"", "");
            region = decodeUnicode(region);
            // 国家
            String country = "";
            // 地区
            String area = "";
            // 市区
            String city = "";
            // 县
            String county = "";
            // ISP公司
            String isp = "";
            for (int i = 0; i < temp.length; i++) {
                switch (i) {
                    case 1:
                        country = (temp[i].split(":"))[2].replaceAll("\"", "");
                        country = decodeUnicode(country);
                        break;
                    case 3:
                        area = (temp[i].split(":"))[1].replaceAll("\"", "");
                        area = decodeUnicode(area);
                        break;
                    case 5:
                        region = (temp[i].split(":"))[1].replaceAll("\"", "");
                        region = decodeUnicode(region);
                        break;
                    case 7:
                        city = (temp[i].split(":"))[1].replaceAll("\"", "");
                        city = decodeUnicode(city);
                        break;
                    case 9:
                        county = (temp[i].split(":"))[1].replaceAll("\"", "");
                        county = decodeUnicode(county);
                        break;
                    case 11:
                        isp = (temp[i].split(":"))[1].replaceAll("\"", "");
                        isp = decodeUnicode(isp);
                        break;
                    default:
                        continue;
                }
            }
            return country + "_" + area + "_" + region + "_" + city + "_" + county + "_" + isp;

        } catch (Exception e) {
            log.error("方法报错", e);
            return "";
        }
    }

    /**
     * @param urlStr   请求的地址
     * @param content  请求的参数 格式为：name=xxx&pwd=xxx
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     */
    private static String getResult(String urlStr, String content, String encoding) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setConnectTimeout(5000);// 设置连接超时时间，单位毫秒
            connection.setReadTimeout(5000);// 设置读取数据超时时间，单位毫秒
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod("POST");// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口

            // 打开输出流往对端服务器写数据
            try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
                // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
                out.writeBytes(content);
                out.flush();// 刷新
            }

            // ,以BufferedReader流来读取
            StringBuffer buffer = new StringBuffer();

            // 往对端写完数据对端服务器返回数据
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding))) {
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            }

            return buffer.toString();
        } catch (IOException e) {
            log.error("方法报错", e);
            log.error("向 {} 发送请求，解析参数:{},所在的地理区域 ，发生异常！", urlStr, content);
        } finally {
            if (connection != null) {
                // 关闭连接
                connection.disconnect();
            }
        }
        return null;
    }

    /**
     * unicode 转换成 中文
     */
    private static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

}
