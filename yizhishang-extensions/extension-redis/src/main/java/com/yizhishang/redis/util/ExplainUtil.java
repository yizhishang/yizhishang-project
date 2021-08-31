package com.yizhishang.redis.util;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.yizhishang.common.exception.BizException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 袁永君
 * @description : 用于解析 #user.username:#orderCode:test
 * @since :  2020-04-22 19:16
 */
public class ExplainUtil {

    private ExplainUtil() {

    }

    public static String explainKey(String key, ProceedingJoinPoint pjd) {

        Object[] args = pjd.getArgs();
        String[] paramNames = ((CodeSignature) pjd.getSignature()).getParameterNames();

        Map<String, Object> paramMap = Maps.newHashMap();

        for (int i = 0; i < paramNames.length; i++) {
            paramMap.put(paramNames[i], args[i]);
        }

        StringBuilder result = new StringBuilder();

        String[] strList = key.split(":");
        for (String str : strList) {
            result.append(":");
            // 说明是动态属性值
            if (str.startsWith("#")) {
                // 拿到字段
                str = str.substring(1);
                String[] split = str.split("\\.");
                Object param;
                if (split.length < 1) {
                    param = paramMap.get(str);
                } else {
                    param = paramMap.get(split[0]);
                }
                if (split.length > 1) {
                    List<String> collect = Stream.of(split).collect(Collectors.toList());
                    result.append(JSON.toJSON(getValueByKeyList(param, collect)));
                } else {
                    result.append(JSON.toJSON(param));
                }
            } else {
                result.append(str);
            }
        }
        return result.toString();
    }


    /**
     * 单个对象的某个键的值
     * <p>
     * 对象
     *
     * @param key 键
     * @return Object 键在对象中所对应得值 没有查到时返回空字符串
     */
    private static Object getValueByKey(@NotNull Object obj, String key) {
        try {
            // 得到类对象
            Class cls = obj.getClass();
            /* 得到类中的所有属性集合 */
            final List<Field> allFields = new ArrayList<>();

            while (cls != null) {
                Field[] declaredFields = cls.getDeclaredFields();
                allFields.addAll(Arrays.asList(declaredFields));
                cls = cls.getSuperclass();
            }
            for (Field field : allFields) {
                field.setAccessible(true);

                if (field.getName().equals(key)) {
                    return field.get(obj);
                }
            }
        } catch (Exception e) {
            throw new BizException("未知异常");
        }
        return null;
    }

    /**
     * 根据key对象的列表获取最终的对象属性
     *
     * @param object
     * @param keyList
     * @return
     */
    private static Object getValueByKeyList(Object object, List<String> keyList) {
        Assert.notNull(object, keyList.get(0) + " can not be null");
        keyList.remove(0);
        for (String s : keyList) {
            object = getValueByKey(object, s);
            Assert.notNull(object, s + " can not be null");
        }
        return object;
    }
}
