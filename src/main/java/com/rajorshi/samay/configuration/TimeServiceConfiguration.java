package com.rajorshi.samay.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Getter
@PropertySources({
        @PropertySource("classpath:application.yml")
})
@Slf4j
public class TimeServiceConfiguration {

    @Value("${timeservice.httpDispatcherEnabled:false}")
    private boolean httpDispatcherEnabled;

    @Value("${timeservice.httpDispatcherTimeout:1000}")
    private long httpDispatcherTimeout;

    @Value("${timeservice.dispatcherJobBatchSize:100}")
    private long dispatcherJobBatchSize;

    @Value("${timeservice.callbackExpiryHours:6}")
    private long callbackExpiryHours;

    @Value("${timeservice.purgeJobBatchSize:100}")
    private long purgeJobBatchSize;

    @Value("${timeservice.purgeAfterDays:14}")
    private long purgeAfterDays;

}
