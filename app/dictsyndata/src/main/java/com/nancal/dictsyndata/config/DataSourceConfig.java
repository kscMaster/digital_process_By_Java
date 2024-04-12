package com.nancal.dictsyndata.config;


import org.hibernate.SessionFactory;
import org.hibernate.boot.SessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


/**
 * @author hewei
 * @date 2022/7/14 9:38
 * @Description
 */
@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.test")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix ="spring.datasource.dev")
    public DataSource devDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "dynamicDataSource")
    @Primary  // 优先使用，多数据源
    public DataSource dynamicDataSource(){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        DataSource testDataSource = testDataSource();
        DataSource devDataSource = devDataSource();

        //设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(devDataSource);

        //配置多个数据源
        Map<Object,Object> map = new HashMap<>();
        map.put(DataSourceType.DEV.name(),devDataSource);
        map.put(DataSourceType.TEST.name(),testDataSource);
        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;
    }



    @Bean(name = "testJdbcTemplate")
    public JdbcTemplate testJdbcTemplate() {
        return new JdbcTemplate(testDataSource());
    }

    @Bean(name = "devJdbcTemplate")
    public JdbcTemplate devJdbcTemplate() {
        return new JdbcTemplate(devDataSource());
    }


    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager(entityManagerFactory);
        jpaTransactionManager.setDataSource(dynamicDataSource());
        return jpaTransactionManager;
    }

}
