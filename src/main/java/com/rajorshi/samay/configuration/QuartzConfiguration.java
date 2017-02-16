package com.rajorshi.samay.configuration;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@Getter
@PropertySources({
        @PropertySource("classpath:application.yml")
})
@Slf4j
public class QuartzConfiguration {

//    @Value("${quartz.scheduler.instanceName}")
//    private String instanceName;
//
//    @Value("${quartz.scheduler.instanceId}")
//    private String instanceId;
//
//    @Value("${quartz.threadPool.threadCount}")
//    private String threadCount;
//
//    @Value("${quartz.datasource.driverClassName}")
//    private String driverClass;
//
//    @Value("${quartz.datasource.username}")
//    private String userName;
//
//    @Value("${quartz.datasource.password}")
//    private String password;
//
//    @Value("${quartz.datasource.url}")
//    private String dbUrl;
//
//    @Value("${quartz.datasource.validationQuery}")
//    private String validationQuery;
//
//    @Value("${quartz.jobstore.class}")
//    private String jobstoreClass;
//
//    @Value("${quartz.jobstore.driverDelegateClassName}")
//    private String driverDelegateClassName;
//
//    @Inject
//    TimedCallbackDao timedCallbackDao;
//
//    private DataSource dataSource(){
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setDriverClassName(driverClass);
//        hikariConfig.setAutoCommit(false);
//        hikariConfig.setPassword(password);
//        hikariConfig.setUsername(userName);
//        hikariConfig.setJdbcUrl(dbUrl);
//        hikariConfig.setConnectionTestQuery(validationQuery);
//
//        hikariConfig.setMaximumPoolSize(100);
//        hikariConfig.setMinimumIdle(0);
//        hikariConfig.setIdleTimeout(10); // minutes
//        hikariConfig.setConnectionTimeout(30000); // millis
//
//        return new HikariDataSource(hikariConfig);
//    }
//
//    @Bean
//    public JobDetail jobDetail() {
//        return JobBuilder.newJob().ofType(TimedJob.class)
//                .storeDurably()
//                .withIdentity(new JobKey("scheduleId"))
//                .withDescription("Invoke Sample Job service...")
//                .build();
//    }
//
//    @Bean
//    public Trigger trigger(JobDetail job) {
//        return TriggerBuilder.newTrigger().forJob(job)
//                .withIdentity(new TriggerKey("scheduleId"))
//                .withDescription("Sample trigger")
//                .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(1))
//                .build();
//    }
//
//    @Bean
//    public Scheduler scheduler(ApplicationContext applicationContext, Trigger trigger, JobDetail job) throws IOException, SchedulerException {
//
//        Properties quartzProperties = new Properties();
//        quartzProperties.setProperty("org.quartz.scheduler.instanceName", instanceName);
//        quartzProperties.setProperty("org.quartz.scheduler.instanceId", instanceId);
//        quartzProperties.setProperty("org.quartz.threadPool.threadCount", threadCount);
////        quartzProperties.setProperty("org.quartz.jobStore.class", jobstoreClass);
////        quartzProperties.setProperty("org.quartz.jobStore.driverDelegateClass", driverDelegateClassName);
//
//        StdSchedulerFactory factory = new StdSchedulerFactory();
//        factory.initialize(quartzProperties);
//
//        Scheduler scheduler = factory.getScheduler();
//        scheduler.setJobFactory(springBeanJobFactory(applicationContext));
//        scheduler.scheduleJob(job, trigger);
//
//        scheduler.start();
//        return scheduler;
//    }
//
//    @Bean
//    public SpringBeanJobFactory springBeanJobFactory(ApplicationContext applicationContext) {
//        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
//        jobFactory.setApplicationContext(applicationContext);
//        return jobFactory;
//    }

}
