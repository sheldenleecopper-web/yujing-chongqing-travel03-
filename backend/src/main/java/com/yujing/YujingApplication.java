package com.yujing;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("com.yujing.mapper")
public class YujingApplication {
    public static void main(String[] args) {
        SpringApplication.run(YujingApplication.class, args);
    }
}
