-- last_mill_second ARGV[1],
-- curr_permits ARGV[2] 1
-- max_permits ARGV[3] 10
-- rate ARGV[4] 10
-- app  ARGV[5])

local result=1
redis.pcall("HMSET",KEYS[1],
		"last_mill_second",ARGV[1],
		"curr_permits",ARGV[2],
		"max_permits",ARGV[3],
		"rate",ARGV[4],
		"app",ARGV[5])
return result