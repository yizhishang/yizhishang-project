package com.yizhishang.common.http;

/**
 * @author yizhishang
 */

public enum RequestMethod {
    /**
     * get 请求
     */
    GET(1),
    POST(2),
    PUT(3),
    DELETE(4),
    POST2(5);

    private int id;

    private RequestMethod(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}
