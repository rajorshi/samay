package com.rajorshi.samay.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.inject.Inject;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@PropertySource("classpath:database.properties")
@EnableTransactionManagement
public class DatabaseConfiguration {

    @Value("${spring.datasource.driverClassName}")
    private String driverClass;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;

    @Value("${spring.jpa.show-sql:false}")
    private String jpaShowSql;

    @Value("${spring.jpa.hibernate.ddl-auto:update}")
    private String jpaDdlAuto;

    @Value("${spring.jpa.database-platform}")
    private String jpaDialect;

    @Value("${spring.jpa.properties.hibernate.current_session_context_class}")
    private String sessionContext;

    @Value("${spring.jpa.hibernate.naming-strategy}")
    private String namingConvention;

    @Bean
    public EntityManagerFactory entityManagerFactory() {


        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setJpaDialect(new HibernateJpaDialect());
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        factoryBean.setPersistenceUnitName("persistenceUnit");
        factoryBean.setJpaProperties(jpaProperties());
        factoryBean.setPackagesToScan("com.rajorshi.samay.model.repository", "com.rajorshi.samay.configuration");
        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }


    private DataSource dataSource(){
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClass);
        hikariConfig.setAutoCommit(false);
        hikariConfig.setPassword(password);
        hikariConfig.setUsername(userName);
        hikariConfig.setJdbcUrl(dbUrl);
        hikariConfig.setConnectionTestQuery(validationQuery);

        hikariConfig.setMaximumPoolSize(100);
        hikariConfig.setMinimumIdle(0);
        hikariConfig.setIdleTimeout(10); // minutes
        hikariConfig.setConnectionTimeout(2000); // millis

        return new HikariDataSource(hikariConfig);
    }


    private Properties jpaProperties()
    {
        Properties jpaProperties = new Properties();
        jpaProperties.put("show-sql",jpaShowSql);
        jpaProperties.put("hibernate.hbm2ddl.auto",jpaDdlAuto);
        jpaProperties.put("hibernate.dialect",jpaDialect);
        jpaProperties.put("hibernate.current_session_context_class",sessionContext);
        jpaProperties.put("hibernate.id.new_generator_mappings",false); // true only when sequence is managed in
        jpaProperties.put("hibernate.naming-strategy",namingConvention);
        return jpaProperties;

    }

    @Bean
    @Inject
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }

}
