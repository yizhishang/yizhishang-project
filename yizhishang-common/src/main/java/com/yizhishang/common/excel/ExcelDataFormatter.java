package com.yizhishang.common.excel;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description Excel数据导入导出格式化
 * @Author 袁永君
 * @Date 2018/5/10 11:59
 * @Version 1.0
 **/
public class ExcelDataFormatter {

    private Map<String, Map<String, String>> formatter = new HashMap<String, Map<String, String>>();

    public void set(String key, Map<String, String> map) {
        formatter.put(key, map);
    }

    public Map<String, String> get(String key) {
        return formatter.get(key);
    }

}