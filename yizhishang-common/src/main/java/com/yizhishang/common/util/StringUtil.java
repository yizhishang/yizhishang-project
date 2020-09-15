package com.yizhishang.common.util;

import com.google.common.collect.Maps;
import com.yizhishang.common.enums.RegexpEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yizhishang
 */
@Slf4j
public class StringUtil extends StringUtils {

    private static final Pattern RE_UNICODE = Pattern.compile("\\\\u([0-9a-zA-Z]{4})");
    private static List<Pattern> patterns = null;
    private static final Random RANDOM = new Random();

    /**
     * 插入换行符<br/>
     *
     * @param source
     * @param span   每隔x这个字符就插入一个换行符
     * @return
     */
    public static String insertBreakChar(String source, int span) {
        StringBuilder result = new StringBuilder();
        if (source != null) {
            char[] charArray = source.toCharArray();
            int count = 0;
            for (char x : charArray) {
                count++;
                result.append(x);
                //如果到了跨度值，则插入，并将计数器归0
                if (count == span) {
                    result.append("<br/>");
                    count = 0;
                }
            }
        }
        return result.toString();
    }

    /**
     * 去掉结尾指定字符
     */
    public static String trimEnd(String input, String charsToTrim) {
        return input.replaceAll("[" + charsToTrim + "]+$", "");
    }

    /**
     * 去掉开头指定字符
     */
    public static String trimStart(String input, String charsToTrim) {
        return input.replaceAll("^[" + charsToTrim + "]+", "");
    }

    /**
     * 去掉首尾指定字符
     */
    public static String trim(String input, String charsToTrim) {
        return input.replaceAll("^[" + charsToTrim + "]+|[" + charsToTrim + "]+$", "");
    }

    /**
     * 去掉首尾指定字符
     *
     * @param charsToTrim 只要首尾的字符是此字符数字中包含的，就会被去掉
     */
    public static String trim(String input, char[] charsToTrim) {
        if (isBlank(input)) {
            return "";
        }
        int start = 0;
        int end = 0;
        for (char c : charsToTrim) {
            if (input.indexOf(c) == 0) {
                if (start == 1) {
                    continue;
                }
                input = input.substring(1);
                start++;
            }
            if (input.lastIndexOf(c) == input.length() - 1) {
                if (end == 1) {
                    continue;
                }
                input = input.substring(0, input.length() - 1);
                end++;
            }
        }
        return input;
    }

    /**
     * @param regex 分隔符
     */
    public static List<String> splitToStringList(String input, String regex) {
        List<String> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(input)) {
            String[] strArray = input.split(regex);
            result.addAll(Arrays.asList(strArray));
        }
        return result;
    }

    /**
     * @param regex 分隔符
     */
    public static List<Long> splitToLongList(String input, String regex) {
        List<Long> result = new ArrayList<>();
        if (StringUtils.isNotEmpty(input)) {
            String[] strArray = input.split(regex);

            for (String x : strArray) {
                result.add(Long.parseLong(x));
            }
        }
        return result;
    }

    /**
     * 把String数组 转 Long 数组
     *
     * @param args String数组
     * @return Long数组
     */
    public static Long[] stringsToLongs(String[] args) {
        Long[] result = {};
        if (args == null || args.length < 1) {
            return result;
        }
        result = new Long[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = Long.valueOf(args[i]);
        }
        return result;
    }

    /**
     * 将long数组组合成分隔符分隔的字符串
     *
     * @param regex 分隔符
     */
    public static String longListToString(List<Long> list, String regex) {
        StringBuffer result = new StringBuffer();
        if (list != null && list.isEmpty()) {
            for (Long x : list) {
                result.append(x).append(regex);
            }
        }
        return trimEnd(result.toString(), regex);
    }

    /**
     * 将int数组组合成分隔符分隔的字符串
     *
     * @param regex 分隔符
     */
    public static String integerListToString(List<Integer> list, String regex) {
        StringBuffer result = new StringBuffer();
        if (list != null && list.isEmpty()) {
            for (Integer x : list) {
                result.append(x).append(regex);
            }
        }
        return trimEnd(result.toString(), regex);
    }

    /**
     * 将String数组组合成分隔符分隔的字符串
     *
     * @param regex 分隔符
     */
    public static String stringListToString(List<String> list, String regex) {
        StringBuffer result = new StringBuffer();
        if (list != null && list.isEmpty()) {
            for (String x : list) {
                result.append(x).append(regex);
            }
        }
        return trimEnd(result.toString(), regex);
    }

    /**
     * 将String数组组合成分隔符分隔的字符串
     *
     * @param regex 分隔符
     */
    public static String stringListToString(Object[] list, String regex) {
        StringBuffer result = new StringBuffer();
        if (list != null && list.length > 0) {
            for (int i = 0; i < list.length; i++) {
                result.append(list[i]).append(regex);
            }
        }
        return trimEnd(result.toString(), regex);
    }

    /**
     * 把String数组 转Integer 数组
     *
     * @param args String数组
     * @return Integer数组
     */
    public static Integer[] stringsToIntegers(String[] args) {
        Integer[] result = {};
        if (args == null || args.length < 1) {
            return result;
        }
        result = new Integer[args.length];
        for (int i = 0; i < args.length; i++) {
            result[i] = Integer.valueOf(args[i]);
        }
        return result;
    }

    /**
     * 把String类型转 Timestamp
     *
     * @param time String类型的时间
     * @return Timestamp
     */
    public static Timestamp stringToTimestamp(String time) {
        if (StringUtils.isEmpty(time)) {
            return null;
        }
        if (time.length() < 12) {
            time += " 00:00:00";
        }
        return Timestamp.valueOf(time);
    }

    /**
     * 转换输入字符串中的任何转义字符。
     *
     * @param str 包含要转换的文本的输入字符串。
     * @return 包含任何转换为非转义形式的转义字符的字符串。
     */
    public static String unEscape(String str) {
        Matcher m = RE_UNICODE.matcher(str);
        StringBuffer sb = new StringBuffer(str.length());
        while (m.find()) {
            m.appendReplacement(sb,
                    Character.toString((char) Integer.parseInt(m.group(1), 16)));
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 获取一定长度的随机字符串，包含数字和小写字母
     *
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = RANDOM.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 充值使用的随机生成的订单号
     */
    public static String getTradeNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(new Date()) + getRandomStringByLength(6);
    }

    /**
     * 资金流水使用的流水号生成
     */
    public static String getSerialNo() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(new Date()) + getRandomStringByLength(8);
    }

    /**
     * 转换驼峰命名方式
     */
    public static String toCapitalizeCamelCase(String name) {
        if (name == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder(name.length());
        boolean upperCase = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            if (c == '_') {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }
        name = sb.toString();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static boolean isNotValid(String string) {
        return string == null || "".equals(string) || "".equals(string.trim()) || "null".equalsIgnoreCase(string);
    }

    public static boolean isValid(String string) {
        return !isNotValid(string);
    }


    private static List<Object[]> getXssPatternList() {
        ArrayList list = new ArrayList();
        list.add(new Object[]{"<(no)?script[^>]*>.*?</(no)?script>", Integer.valueOf(2)});
        list.add(new Object[]{"eval\\((.*?)\\)", Integer.valueOf(42)});
        list.add(new Object[]{"expression\\((.*?)\\)", Integer.valueOf(42)});
        list.add(new Object[]{"(javascript:|vbscript:|view-source:)*", Integer.valueOf(2)});
        list.add(new Object[]{"<(\"[^\"]*\"|\'[^\']*\'|[^\'\">])*>", Integer.valueOf(42)});
        list.add(new Object[]{"(window|location|window\\.location|window\\.|\\.location|document\\.cookie|document\\.|alert\\(.*?\\)|window\\.open\\()*", Integer.valueOf(42)});
        list.add(new Object[]{"<+\\s*\\w*\\s*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror=|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+\\s*=+", Integer.valueOf(42)});
        return list;
    }

    private static List<Pattern> getPatterns() {
        if (patterns == null) {
            ArrayList list = new ArrayList();
            String regex;
            Integer flag;
            Iterator var4 = getXssPatternList().iterator();

            while (var4.hasNext()) {
                Object[] arr = (Object[]) var4.next();

                for (int i = 0; i < arr.length; ++i) {
                    regex = (String) arr[0];
                    flag = (Integer) arr[1];
                    list.add(Pattern.compile(regex, flag.intValue()));
                }
            }
            patterns = list;
        }

        return patterns;
    }

    public static String cleanXSS(String value) {
        if (isValid(value)) {
            if (value.contains("\\x")) {
                value = value.replace("\\\\x", "%");
            }

            if (value.contains("%")) {
                try {
                    value = URLDecoder.decode(value, String.valueOf(StandardCharsets.UTF_8));
                } catch (IllegalArgumentException e) {
                    log.error("value-->{}", value);
                    e.printStackTrace();
                } catch (UnsupportedEncodingException var4) {
                    var4.printStackTrace();
                }
            }

            Matcher matcher;
            Iterator<Pattern> iterator = getPatterns().iterator();

            while (iterator.hasNext()) {
                Pattern pattern = iterator.next();
                matcher = pattern.matcher(value);
                if (matcher.find()) {
                    value = matcher.replaceAll("");
                }
            }

            value = value.replace("<", "& lt;").replace(">", "& gt;");
            value = value.replace("\\(", "& #40;").replace("\\)", "& #41;");
            value = value.replace("\'", "& #39;");
            value = value.replace("eval\\((.*)\\)", "");
            value = value.replace("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
            value = value.replace("script", "");
            value = StringUtils.replace(value, "'", "''");
        }

        return value;
    }

    /**
     * 检查一个字符串是否是一个合法的密码（8-20位的数字和字符串组合）
     * 要求：
     * 1、必须要要有8-20位(含)
     * 2、不能全是数字
     * 3、不能全是字母
     *
     * @param source
     * @return
     */
    public static Boolean validPassword(String source) {
        if (isNotValid(source)) {
            return false;
        }
        if (source.length() < 8 || source.length() > 20) {
            return false;
        }

        String regEx = "^[A-Za-z]+$";
        if (Pattern.compile(regEx).matcher(source).matches()) {
            //纯字母
            return false;
        }
        regEx = "^[0-9]+$";
        //纯数字
        return !Pattern.compile(regEx).matcher(source).matches();
    }

    public static Map<String, String> validPasswordWithMsg(String source) {
        Map<String, String> map = Maps.newHashMap();
        if (isNotValid(source)) {
            map.put("result", "false");
            map.put("message", "密码不能为空");
            return map;
        }
        if (source.length() < 8 || source.length() > 20) {
            map.put("result", "false");
            map.put("message", "密码长度在8-20位之间");
            return map;
        }

        String regEx = "^[A-Za-z]+$";
        if (Pattern.compile(regEx).matcher(source).matches()) {
            //纯字母
            map.put("result", "false");
            map.put("message", "密码不能全都是字母");
            return map;
        }
        regEx = "^[0-9]+$";
        if (Pattern.compile(regEx).matcher(source).matches()) {
            //纯数字
            map.put("result", "false");
            map.put("message", "密码不能全都是数字");
            return map;
        }
        map.put("result", "true");
        return map;
    }

    public static boolean isValidPhoneNO(String phoneNum) {
        if (isNotValid(phoneNum)) {
            return false;
        }
        return phoneNum.matches(RegexpEnum.MOBILE_PHONE.getRegexp());
    }

    /**
     * 判断是否包含内网IPv4
     *
     * @param ip 合法的ip地址
     * @return boolean
     */
    public static boolean isContainInnerIPv4(String ip) {
        String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";
        Pattern p = Pattern.compile(reg);
        Matcher matcher = p.matcher(ip);
        return matcher.find();
    }

    public static void main(String[] args) {
        String phoneNum = "19088840046";
        System.out.println(phoneNum.replace("8", "s"));
        System.out.println(phoneNum.replace("8", "s"));
    }

}
