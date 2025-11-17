package com.eq.mds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * create time 2025/11/17 15:21
 * 文件说明
 *
 * @author xuejiaming
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MDSApplication {
    public static void main(String[] args) {
        SpringApplication.run(MDSApplication.class, args);
    }

}