package com.yizhishang.common.http;

/**
 * @author yizhishang
 * @date 2016/7/14
 */
public class HttpResult {

    private int status;
    private String response;

    public HttpResult() {
        status = 400;
    }

    public HttpResult(int status, String response) {
        this.status = status;
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "HttpResult{" +
                "status=" + status +
                ", response='" + response + '\'' +
                '}';
    }
}
