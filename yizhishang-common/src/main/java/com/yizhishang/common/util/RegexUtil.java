package com.yizhishang.common.util;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yizhishang
 * @version 1.0
 * @Date 2018年05月10日 14时47分
 */
public class RegexUtil {

    private RegexUtil() {

    }

    public static String findMatchContent(String regx, String source) {
        if (StringUtil.isEmpty(regx) || StringUtil.isEmpty(source)) {
            return null;
        }

        Matcher m = Pattern.compile(regx).matcher(source);
        if (m.find()) {
            return m.group();
        }
        return null;
    }

    public static List<String> findMatchContents(String regx, String source) {
        if (StringUtil.isEmpty(regx) || StringUtil.isEmpty(source)) {
            return Lists.newArrayList();
        }
        ArrayList lst = new ArrayList();
        Matcher m = Pattern.compile(regx).matcher(source);
        while (m.find()) {
            lst.add(m.group());
        }
        return lst;
    }
}
