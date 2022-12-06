package com.yizhishang.redis.lock;

import lombok.Data;

import java.util.List;

/**
 * @author 01398723
 */
@Data
public class LockResult {

    /**
     * 加锁是否成功
     */
    private Boolean lock;

    /**
     * 执行方法结果
     */
    private Boolean result;

    /**
     * 获得锁的集合
     */
    private List<String> lockKeys;

    /**
     * 执行错误信息
     */
    private String errorExMsg;
}
