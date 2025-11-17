package com.eq.mds.configuration;

import lombok.Data;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * create time 2025/11/17 15:35
 * 文件说明
 *
 * @author xuejiaming
 */
@Data
@ConfigurationProperties(prefix = "spring.datasource")
@Component
public class DynamicDataSourceProperties {
    private Map<String, DataSourceProperties> dynamic = new LinkedHashMap<>();
}
