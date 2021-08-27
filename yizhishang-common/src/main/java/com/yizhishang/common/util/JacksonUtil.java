package com.yizhishang.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author yizhishang
 */
@Slf4j
public class JacksonUtil {

    private static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private JacksonUtil() {

    }

    public static String objectToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        //设置可用单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //设置字段可以不用双引号包括
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //设置时间格式
        mapper.setDateFormat(new SimpleDateFormat(DATE_TIME));
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("方法报错", e);
        }
        return null;
    }


    public static <T> T jsonToObject(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        //设置可用单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //设置字段可以不用双引号包括
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        //设置时间格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        //设置实体无属性和json串属性对应时不会出错
//        mapper.disable(JsonGenerator.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            log.error("方法报错", e);
        }
        return null;
    }


    public static <T> T jsonToList(String json, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        mapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            JavaType javaType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            log.error("方法报错", e);
        }
        return null;
    }


    public static <T> T jsonToList(String json, TypeReference<T> typeReference) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);//设置可用单引号
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);//设置字段可以不用双引号包括
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        mapper.disable(JsonFormat.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        try {
            return mapper.readValue(json, typeReference);
        } catch (Exception e) {
            log.error("方法报错", e);
        }
        return null;
    }


    public static String console(Object t) {
        ObjectMapper mapper = new ObjectMapper();
        //设置可用单引号
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //设置字段可以不用双引号包括
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        String json = "";
        try {
            json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(t);
        } catch (IOException e) {
            log.error("方法报错", e);
        }
        return json;
    }

}