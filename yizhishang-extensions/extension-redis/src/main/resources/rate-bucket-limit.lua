local rate_limit_info = redis.pcall("HMGET", KEYS[1], "last_mill_second", "curr_permits", "max_permits", "rate", "app")
local last_mill_second = rate_limit_info[1]
local curr_permits = tonumber(rate_limit_info[2])
local max_permits = tonumber(rate_limit_info[3])
local rate = tonumber(rate_limit_info[4])
local app = tostring(rate_limit_info[5])

if app == nil then
    return 0
end

local local_curr_permits = max_permits

if (type(last_mill_second) ~= 'boolean' and last_mill_second ~= nil) then
    local reverse_permits = math.floor((ARGV[2] - last_mill_second) / 1000) * rate

    if (reverse_permits > 0) then
        redis.pcall("HMSET", KEYS[1], "last_mill_second", ARGV[2])
    end

    local expect_curr_permits = reverse_permits + curr_permits
    local_curr_permits = math.min(expect_curr_permits, max_permits);

else
    redis.pcall("HMSET", KEYS[1], "last_mill_second", ARGV[2])
end

local result = -1
if (local_curr_permits - ARGV[1] > 0) then
    result = 1
    redis.pcall("HMSET", KEYS[1], "curr_permits", local_curr_permits - ARGV[1])
else
    redis.pcall("HMSET", KEYS[1], "curr_permits", local_curr_permits)
end

return result