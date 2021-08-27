package com.yizhishang.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yizhishang
 */
@Configuration
@ConfigurationProperties(prefix = "resources")
public class ResourceIgnoreConfig {

    /**
     * 忽略认证
     */
    private List<String> ignore = new ArrayList<>();

    public List<String> getIgnore() {
        return ignore;
    }

    public void setIgnore(List<String> ignore) {
        this.ignore = ignore;
    }
}