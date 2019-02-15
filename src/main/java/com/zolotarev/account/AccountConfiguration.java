package com.zolotarev.account;

import com.opentable.db.postgres.embedded.EmbeddedPostgres;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.DataSourceQueryCountListener;
import net.ttddyy.dsproxy.listener.logging.DefaultQueryLogEntryCreator;
import net.ttddyy.dsproxy.listener.logging.SLF4JQueryLoggingListener;
import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.springframework.boot.autoconfigure.AbstractDependsOnBeanFactoryPostProcessor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.sql.DataSource;
import java.io.IOException;

import static java.util.Collections.emptyList;
import static springfox.documentation.builders.PathSelectors.ant;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

/**
 * Configuration class for application
 */
@Configuration
@EnableSwagger2
@EnableJpaRepositories
@EnableTransactionManagement
public class AccountConfiguration extends AbstractDependsOnBeanFactoryPostProcessor {

    protected AccountConfiguration() {
        super(DataSource.class, "embeddedPostgres");
    }

    @Profile("embeddedPostgres")
    @Bean(destroyMethod = "close")
    public EmbeddedPostgres embeddedPostgres() throws IOException {
        return EmbeddedPostgres.builder().setPort(5432).start();
    }

    @Bean
    @Profile("dev")
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariConfig hikariConfig(DataSourceProperties properties) {
        final HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(properties.getDriverClassName());
        hikariConfig.setUsername(properties.getUsername());
        hikariConfig.setPassword(properties.getPassword());
        hikariConfig.setJdbcUrl(properties.getUrl());
        return hikariConfig;
    }

    @Bean
    @Profile("dev")
    public DataSource dataSource(HikariConfig hikariConfig) {
        final HikariDataSource baseDataSource = new HikariDataSource(hikariConfig);

        final ProxyDataSource proxyDataSource = new ProxyDataSource(baseDataSource);
        proxyDataSource.addListener(new DataSourceQueryCountListener());
        proxyDataSource.addListener(loggingListener());
        return proxyDataSource;
    }

    private SLF4JQueryLoggingListener loggingListener() {
        final SLF4JQueryLoggingListener listener = new SLF4JQueryLoggingListener();
        final DefaultQueryLogEntryCreator logEntryCreator = new DefaultQueryLogEntryCreator();
        logEntryCreator.setMultiline(true);
        listener.setQueryLogEntryCreator(logEntryCreator);
        return listener;
    }

    @Bean
    public Docket api() {
        return new Docket(SWAGGER_2)
                .select()
                .apis(basePackage("com.zolotarev.account"))
                .paths(ant("/api/**/*"))
                .build()
                .apiInfo(new ApiInfo("Account managing application",
                        "Java RESTful API for basic operations above bank accounts",
                        "1.1",
                        null,
                        new Contact("Alexander Zolotarev", null, "zolotarev_a.p@mail.ru"),
                        null,
                        null,
                        emptyList()));
    }

}
