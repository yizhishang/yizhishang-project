-- KEYS[1] keys,
-- ARGV[1] limit,
-- ARGV[2] expire

local key = KEYS[1]
local limit = tonumber(ARGV[1])
local expire = tonumber(ARGV[2])

-- 当前调用次数(执行计算器自加)
local count = redis.call('incr', key)
local ttl = redis.call('ttl', key)

-- 调用超过最大值，则直接返回当前调用次数
if count > limit then
    return 0;
end

-- 从第一次调用开始限流，设置对应键值的过期时间
if count == 1 then
    redis.call('expire', key, expire)
else
    if ttl == -1 then
        redis.call('expire', key, expire)
    end
end

return 1;