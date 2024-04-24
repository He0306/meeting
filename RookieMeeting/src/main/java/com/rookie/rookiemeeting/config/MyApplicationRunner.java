package com.rookie.rookiemeeting.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

/**
 * 启动清理缓存
 */
@Configuration
@Slf4j
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    MybatisRedisCache mybatisRedisCache;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Set keys = redisTemplate.keys("*");
        mybatisRedisCache.removeObject(keys);
        redisTemplate.delete(keys);
        log.info("清理redis缓存");
    }
}
