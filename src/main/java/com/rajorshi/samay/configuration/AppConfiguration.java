package com.rajorshi.samay.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.rajorshi.samay.service.CallbackService;
import com.rajorshi.samay.service.impl.CallbackServiceImpl;
import com.rajorshi.samay.utils.TransactionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Validator;
import java.text.SimpleDateFormat;
import java.util.TimeZone;


@Configuration
@EnableTransactionManagement
@PropertySources({
        @PropertySource("classpath:application.yml")
})
public class AppConfiguration {


    @Bean
    public Validator localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Scope("request")
    @Bean("User")
    public String provideUser(HttpServletRequest request, HttpServletResponse response) {
        if (request != null && request.getHeader("X-User-Id") != null)
            return request.getHeader("X-User-Id");
        return "";
    }

    @Bean
    public FilterRegistrationBean transactionFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(transactionFilter());
        registration.addUrlPatterns("/*");
        registration.setName("TransactionFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean(name = "TransactionFilter")
    public Filter transactionFilter() {
        return new TransactionFilter();
    }


    @Bean
    public CallbackService timedCallbackService()
    {
        return new CallbackServiceImpl();
    }

    @Bean
    public Filter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludeHeaders(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(5120);
        return filter;
    }

    @Primary
    @Bean
    public ObjectMapper objectMapper() {
        // Customize...
        return Jackson2ObjectMapperBuilder.
                json()
                .dateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))
                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .timeZone(TimeZone.getTimeZone("Asia/Calcutta"))
                .build();
    }


}
