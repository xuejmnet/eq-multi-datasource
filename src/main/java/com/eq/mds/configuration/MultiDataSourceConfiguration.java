package com.eq.mds.configuration;

import com.easy.query.api.proxy.client.DefaultEasyEntityQuery;
import com.easy.query.api.proxy.client.EasyEntityQuery;
import com.easy.query.core.api.client.EasyQueryClient;
import com.easy.query.core.basic.jdbc.conn.ConnectionManager;
import com.easy.query.core.bootstrapper.EasyQueryBootstrapper;
import com.easy.query.core.bootstrapper.StarterConfigurer;
import com.easy.query.core.configuration.nameconversion.NameConversion;
import com.easy.query.core.configuration.nameconversion.impl.UnderlinedNameConversion;
import com.easy.query.core.datasource.DataSourceUnitFactory;
import com.easy.query.core.logging.Log;
import com.easy.query.core.logging.LogFactory;
import com.easy.query.core.util.EasyObjectUtil;
import com.easy.query.mysql.config.MySQLDatabaseConfiguration;
import com.easy.query.pgsql.config.PgSQLDatabaseConfiguration;
import com.easy.query.sql.starter.SpringBootStarterBuilder;
import com.easy.query.sql.starter.config.EasyQueryInitializeOption;
import com.easy.query.sql.starter.config.EasyQueryProperties;
import com.easy.query.sql.starter.conn.SpringConnectionManager;
import com.easy.query.sql.starter.conn.SpringDataSourceUnitFactory;
import com.eq.mds.client.DefaultEasyMultiEntityQuery;
import com.eq.mds.client.EasyMultiEntityQuery;
import com.eq.mds.utils.DynamicBeanFactory;
import com.eq.mds.utils.SpringUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * create time 2025/11/17 15:43
 * 文件说明
 *
 * @author xuejiaming
 */
@Configuration
public class MultiDataSourceConfiguration {

    private final DynamicDataSourceProperties props;

    public MultiDataSourceConfiguration(DynamicDataSourceProperties props) {
        String logImplClass = "com.easy.query.sql.starter.logging.Slf4jImpl";
        try {
            Class<?> aClass = Class.forName(logImplClass);
            if (Log.class.isAssignableFrom(aClass)) {
                Class<? extends Log> logClass = EasyObjectUtil.typeCastNullable(aClass);
                LogFactory.useCustomLogging(logClass);
            } else {
                LogFactory.useStdOutLogging();
                System.out.println("cant found log:[" + logImplClass + "]!!!!!!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("cant found log:[" + logImplClass + "]!!!!!!");
            e.printStackTrace();
        }
        this.props = props;
        props.getDynamic().keySet().forEach(key -> {
            DataSourceProperties kp = props.getDynamic().get(key);
            DataSource source = DataSourceBuilder.create()
                    .type(kp.getType())
                    .driverClassName(kp.getDriverClassName())
                    .url(kp.getUrl())
                    .username(kp.getUsername())
                    .password(kp.getPassword()).build();
            DynamicBeanFactory.registerBean(key + "DataSource", source);
            EasyQueryClient easyQueryClient = EasyQueryBootstrapper.defaultBuilderConfiguration()
                    .setDefaultDataSource(source)
                    .replaceService(DataSourceUnitFactory.class, SpringDataSourceUnitFactory.class)//springboot下必须用来支持事务
                    .replaceService(ConnectionManager.class, SpringConnectionManager.class)//springboot下必须用来支持事务
                    .replaceService(NameConversion.class, new UnderlinedNameConversion())
                    .optionConfigure(builder -> {
                        //这边可以搞一写配置如果你需要的话
//                        builder.setPrintSql(true);
                    })
                    .useDatabaseConfigure(new MySQLDatabaseConfiguration())
//                    .useDatabaseConfigure(new PgSQLDatabaseConfiguration())//自行配置处理
                    .build();
            DefaultEasyEntityQuery defaultEasyEntityQuery = new DefaultEasyEntityQuery(easyQueryClient);
            DynamicBeanFactory.registerBean(key, defaultEasyEntityQuery);
            DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(source);
            DynamicBeanFactory.registerBean(key + "TransactionManager", dataSourceTransactionManager);
        });


    }

    @Bean
    public EasyMultiEntityQuery easyMultiEntityQuery() {
        HashMap<String, EasyEntityQuery> extra = new HashMap<>();
        EasyEntityQuery easyEntityQuery = SpringUtils.getBean("primary");
        props.getDynamic().keySet().forEach(key -> {
            EasyEntityQuery eq = SpringUtils.getBean(key);
            extra.put(key, eq);
        });
        return new DefaultEasyMultiEntityQuery("primary", easyEntityQuery, extra);
    }
}
