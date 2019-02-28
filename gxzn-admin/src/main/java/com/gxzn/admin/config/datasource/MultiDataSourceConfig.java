package com.gxzn.admin.config.datasource;

import com.gxzn.admin.core.datascope.DataScopeInterceptor;
import com.gxzn.admin.core.metadata.ProcurementGovMpFieldHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.MybatisMapWrapperFactory;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

/**
 * 多数据源配置<br/>
 * <p>
 */
//@EnableTransactionManagement(order = 2, proxyTargetClass = true)
@EnableTransactionManagement
@Configuration
@MapperScan(basePackages = { "**.mapper" })
@ConditionalOnProperty(prefix = "procurement-gov", name = "muti-datasource-open", havingValue = "true")
public class MultiDataSourceConfig {

    @Value("${spring.datasource.dynamic.datasource.master.url}")
    private String url;

    /**
     * mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        if (url.contains("oracle")) {
            paginationInterceptor.setDialectType(DbType.ORACLE.getDb());
        } else if (url.contains("postgresql")) {
            paginationInterceptor.setDialectType(DbType.POSTGRE_SQL.getDb());
        } else if (url.contains("sqlserver")) {
            paginationInterceptor.setDialectType(DbType.SQL_SERVER.getDb());
        } else {
            paginationInterceptor.setDialectType(DbType.MYSQL.getDb());
        }
        return paginationInterceptor;
    }

    /**
     * 数据范围mybatis插件
     */
    @Bean
    public DataScopeInterceptor dataScopeInterceptor() {
        return new DataScopeInterceptor();
    }

    /**
     * 乐观锁mybatis插件
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
    
    /**
     * 自定义公共字段自动注入
     */
    @ConditionalOnMissingBean
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new ProcurementGovMpFieldHandler();
    }
    
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {
            
            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
              //*注册Map 下划线转驼峰*
                configuration.setObjectWrapperFactory(new MybatisMapWrapperFactory());
            }
        };
    }
    

}
