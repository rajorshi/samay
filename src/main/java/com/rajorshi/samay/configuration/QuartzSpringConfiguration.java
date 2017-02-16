package com.rajorshi.samay.configuration;

import com.rajorshi.samay.jobs.DispatchCallbacksJob;
import com.rajorshi.samay.jobs.JobDataMapKeys;
import com.rajorshi.samay.jobs.PurgeOldCallbacksJob;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.quartz.*;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Properties;

@Configuration
@Getter
@PropertySources({
        @PropertySource("classpath:application.yml")
})
@Slf4j
public class QuartzSpringConfiguration {
    // http://www.concretepage.com/spring-4/spring-4-quartz-2-scheduler-integration-annotation-example-using-javaconfig
    // http://www.baeldung.com/spring-quartz-schedule
    // http://www.quartz-scheduler.org/documentation/quartz-2.x/cookbook/MultipleSchedulers.html
    // http://www.quartz-scheduler.org/documentation/quartz-2.x/configuration/ConfigJDBCJobStoreClustering

    @Value("${quartz.scheduler.instanceName}")
    private String instanceName;

    @Value("${quartz.scheduler.instanceId}")
    private String instanceId;

    @Value("${quartz.threadPool.threadCount}")
    private String threadCount;

    @Value("${quartz.jobstore.isClustered}")
    private String isClustered;

    @Value("${quartz.jobstore.clusterCheckinInterval}")
    private String clusterCheckinInterval;

    @Value("${quartz.jobstore.class}")
    private String jobstoreClass;

    @Value("${quartz.jobstore.driverDelegateClassName}")
    private String driverDelegateClassName;

    @Value("${quartz.datasource.driver}")
    private String driver;

    @Value("${quartz.datasource.url}")
    private String dataSourceUrl;

    @Value("${quartz.datasource.user}")
    private String userName;

    @Value("${quartz.datasource.password}")
    private String password;

    @Value("${quartz.datasource.validationQuery}")
    private String validationQuery;

    @Value("${quartz.datasource.maxConnections}")
    private String maxConnections;

    @Value("${quartz.datasource.idleConnectionValidationSeconds}")
    private String idleConnectionValidationSeconds;

    @Inject
    private ApplicationContext applicationContext;

    @Bean("jobDetailNs1")
    public JobDetailFactoryBean jobDetailNs1() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(DispatchCallbacksJob.class);
        jobDetailFactory.setDescription("Invoke Sample Job service...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.getJobDataMap().put(JobDataMapKeys.NAMESPACE, "ns1");
        return jobDetailFactory;
    }

    @Bean("jobDetailNs2")
    public JobDetailFactoryBean jobDetailNs2() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(DispatchCallbacksJob.class);
        jobDetailFactory.setDescription("Invoke Sample Job service...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.getJobDataMap().put(JobDataMapKeys.NAMESPACE, "nm");
        return jobDetailFactory;
    }

    @Bean("purgeJobDetail")
    public JobDetailFactoryBean purgeJobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(PurgeOldCallbacksJob.class);
        jobDetailFactory.setDescription("Purge old jobs service...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.getJobDataMap().put(JobDataMapKeys.DAYS, "14");
        return jobDetailFactory;
    }

    @Bean
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean(@Named("jobDetailNs2") JobDetail job) {
        SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
        trigger.setJobDetail(job);
        trigger.setRepeatInterval(1000);
        trigger.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
        return trigger;
    }

    //Job is scheduled after every 1 minute
    @Bean
    public CronTriggerFactoryBean cronTriggerFactoryBean(@Named("jobDetailNs1") JobDetail job){
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(job);
        stFactory.setName("mytrigger");
        stFactory.setGroup("mygroup");
        stFactory.setCronExpression("0/1 0/1 * 1/1 * ? *");
        return stFactory;
    }

    //Job is scheduled after every 1 minute
    @Bean
    public CronTriggerFactoryBean purgeJobTriggerFactoryBean(@Named("purgeJobDetail") JobDetail job){
        CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
        stFactory.setJobDetail(job);
        stFactory.setName("mytrigger");
        stFactory.setGroup("mygroup");
        stFactory.setCronExpression("0/1 0/1 * 1/1 * ? *");
        return stFactory;
    }

    @Bean("cronJobScheduler")
    public SchedulerFactoryBean cronJobSchedulerFactoryBean(
            @Named("cronTriggerFactoryBean")Trigger trigger
            , @Named("jobDetailNs1") JobDetail job
    ) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setQuartzProperties(getQuartzProperties());

        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);
        return schedulerFactory;
    }

    @Bean("purgeJobScheduler")
    public SchedulerFactoryBean purgeJobSchedulerFactoryBean(
            @Named("purgeJobTriggerFactoryBean")Trigger trigger
            , @Named("purgeJobDetail") JobDetail job
    ) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setQuartzProperties(getQuartzProperties());

        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);
        return schedulerFactory;
    }

    @Bean("simpleJobScheduler")
    public SchedulerFactoryBean simpleJobSchedulerFactoryBean(
            @Named("simpleTriggerFactoryBean")Trigger trigger
            , @Named("jobDetailNs2") JobDetail job
    ) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setQuartzProperties(getQuartzProperties());

        schedulerFactory.setJobFactory(springBeanJobFactory());
        schedulerFactory.setJobDetails(job);
        schedulerFactory.setTriggers(trigger);

        return schedulerFactory;
    }

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    private Properties getQuartzProperties() {

        Properties quartzProperties = new Properties();
        quartzProperties.setProperty("org.quartz.scheduler.instanceName", instanceName);
        quartzProperties.setProperty("org.quartz.scheduler.instanceId", instanceId);
        quartzProperties.setProperty("org.quartz.threadPool.threadCount", threadCount);
        quartzProperties.setProperty("org.quartz.jobStore.useProperties", "true");
        quartzProperties.setProperty("org.quartz.jobStore.isClustered", isClustered);
        quartzProperties.setProperty("org.quartz.jobStore.clusterCheckinInterval", clusterCheckinInterval);
        quartzProperties.setProperty("org.quartz.jobStore.class", jobstoreClass);
        quartzProperties.setProperty("org.quartz.jobStore.driverDelegateClass", driverDelegateClassName);
        quartzProperties.setProperty("org.quartz.jobStore.dataSource", "quartzDb");
        quartzProperties.setProperty("org.quartz.dataSource.quartzDb.driver", driver);
        quartzProperties.setProperty("org.quartz.dataSource.quartzDb.URL", dataSourceUrl);
        quartzProperties.setProperty("org.quartz.dataSource.quartzDb.user", userName);
        quartzProperties.setProperty("org.quartz.dataSource.quartzDb.maxConnections", maxConnections);
        quartzProperties.setProperty("org.quartz.dataSource.quartzDb.validationQuery", validationQuery);
        quartzProperties.setProperty("org.quartz.dataSource.quartzDb.idleConnectionValidationSeconds", idleConnectionValidationSeconds);

        return quartzProperties;
    }

}
