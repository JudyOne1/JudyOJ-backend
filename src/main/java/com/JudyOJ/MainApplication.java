package com.JudyOJ;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 主类（项目启动入口）
 *
 * @author Judy 
 *  
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.JudyOJ.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    //todo  acm模式与leetcode模式
    //todo  docker代码沙箱
    //todo  代码记录
    //todo  判题日志完善
    //todo  代码缓存
}
