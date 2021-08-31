package com.yizhishang.redis.bloom;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author yizhishang
 * @since 2020/8/5 17:44
 */
@Service
public class RedisBloomService {

    private static final String EXISTS_STRING = "return redis.call('BF.EXISTS', ARGV[1], KEYS[1])";

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisBloomService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean add(String key, List<String> values) {
        DefaultRedisScript<Boolean> bloomAdd = new DefaultRedisScript<>();
        bloomAdd.setScriptSource(new ResourceScriptSource(new ClassPathResource("bloomFilter-insert.lua")));
        bloomAdd.setResultType(Boolean.class);
        return redisTemplate.execute(bloomAdd, values, key);
    }

    public Boolean exists(String key, String value) {
        DefaultRedisScript<Boolean> bloomExists = new DefaultRedisScript<>(EXISTS_STRING, Boolean.class);
        return redisTemplate.execute(bloomExists, Collections.singletonList(value), key);
    }
}
