package com.yizhishang.common.http;

import cn.hutool.core.map.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * http 请求
 *
 * @author yizhishang
 */
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final RequestConfig REQUEST_CONFIG = RequestConfig.custom()
            .setConnectTimeout(5000).setConnectionRequestTimeout(2000)
            .setSocketTimeout(20000).build();

    private HttpUtils() {

    }

    public static HttpResult request(RequestMethod method, String url, Map<String, String> requestParams) {
        switch (method) {
            case GET:
                return doGet(url, requestParams);
            case POST:
                return doPost(url, requestParams);
            case PUT:
                return doPut(url, requestParams);
            case DELETE:
                return doDelete(url, requestParams);
            case POST2:
                return doPost2(url, requestParams);
            default:
                return null;
        }
    }

    /**
     * Http Get
     *
     * @param url    请求路径
     * @param params 参数
     * @return http响应状态及json结果
     */
    public static HttpResult doGet(String url, Map<String, String> params) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            url = contactUrl(url, params);
            HttpGet httpGet = new HttpGet(url);
            return executeRequest(httpClient, httpGet);
        } catch (IOException e) {
            logger.error(String.format("GET请求异常： %s", url), e);
        }
        return null;
    }

    /**
     * Http Post
     *
     * @param url    请求路径
     * @param params 参数
     * @return http响应状态及json结果
     */
    public static HttpResult doPost(String url, Map<String, String> params) {
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<>();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> e = iterator.next();
            String key = e.getKey();
            String value = e.getValue();
            pairs.add(new BasicNameValuePair(key, value));
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            return executeRequest(httpClient, httpPost, pairs);
        } catch (IOException e) {
            logger.error(String.format("POST请求异常： %s", url), e);
        }
        return null;
    }

    /**
     * Http POST 拼接url的方式请求POST
     *
     * @param url    请求路径
     * @param params 参数
     * @return http响应状态及json结果
     */
    public static HttpResult doPost2(String url, Map<String, String> params) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            url = contactUrl(url, params);
            HttpPost httpPost = new HttpPost(url);
            return executeRequest(httpClient, httpPost);
        } catch (IOException e) {
            logger.error(String.format("POST请求连接异常： %s", url), e);
        }
        return null;
    }

    /**
     * Http Put
     *
     * @param url    请求路径
     * @param params 参数
     * @return http响应状态及json结果
     */
    public static HttpResult doPut(String url, Map<String, String> params) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            url = contactUrl(url, params);
            HttpPut httpPut = new HttpPut(url);
            return executeRequest(httpClient, httpPut);
        } catch (IOException e) {
            logger.error(String.format("PUT请求异常： %s", url), e);
        }
        return null;
    }

    /**
     * Http Delete
     *
     * @param url    请求路径
     * @param params 参数
     * @return http响应状态及json结果
     */
    public static HttpResult doDelete(String url, Map<String, String> params) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            url = contactUrl(url, params);
            HttpDelete httpDelete = new HttpDelete(url);
            return executeRequest(httpClient, httpDelete);
        } catch (IOException e) {
            logger.error(String.format("DELETE请求异常： %s", url), e);
        }
        return null;
    }

    /**
     * 拼装url
     *
     * @param url    url
     * @param params 参数
     */
    private static String contactUrl(String url, Map<String, String> params) {
        if (MapUtil.isNotEmpty(params)) {
            StringBuilder param = new StringBuilder();
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> e = iterator.next();
                String key = e.getKey();
                String value = e.getValue();
                if (value == null || "null".equals(value)) {
                    continue;
                }
                param.append(key).append("=").append(value).append("&");
            }
            if (!"".equals(param.toString())) {
                url += "?" + param.substring(0, param.length() - 1);
            }
        }
        return url;
    }

    public static String getFullPath(HttpServletRequest request) {
        String basePath = request.getRequestURL().toString();
        String queryString = request.getQueryString();
        if (StringUtils.isNotEmpty(queryString)) {
            queryString = "?" + queryString;
        } else {
            queryString = "";
        }

        return basePath + queryString;
    }

    /**
     * 执行GET/PUT/DELETE请求
     */
    private static HttpResult executeRequest(CloseableHttpClient httpClient, HttpRequestBase request) {
        request.setConfig(REQUEST_CONFIG);
        return execute(httpClient, request);
    }

    /**
     * 执行POST请求
     */
    private static HttpResult executeRequest(CloseableHttpClient httpClient, HttpEntityEnclosingRequestBase request, List<NameValuePair> pairs) {
        request.setConfig(REQUEST_CONFIG);
        StringEntity entity;
//        if (pairs.size() == 1 && (pairs.get(0).getName().equals("json") || pairs.get(0).getName().equals("xml"))) {
//            entity = new StringEntity(pairs.get(0).getValue(), StandardCharsets.UTF_8);
//            if(pairs.get(0).getName().equals("json")){
//                entity.setContentType("application/json");
//            } else {
//                entity.setContentType("application/xml");
//            }
//        } else {
        entity = new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8);
//        }
        request.setEntity(entity);
        return execute(httpClient, request);
    }

    /**
     * 执行请求
     *
     * @throws IOException
     */
    private static HttpResult execute(CloseableHttpClient httpClient, HttpRequestBase request) {
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            return getResponseResult(response);
        } catch (IOException e) {
            logger.error("获取请求响应结果异常", e);
        }
        return null;
    }

    /**
     * 获取响应结果
     */
    private static HttpResult getResponseResult(HttpResponse response) {
        int code = response.getStatusLine().getStatusCode();
        HttpResult result = new HttpResult();
        result.setStatus(code);
        if (code == HttpStatus.SC_OK) {
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result.setResponse(EntityUtils.toString(entity, StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                logger.error("获取请求响应结果异常", e);
            }
        }
        return result;
    }
}