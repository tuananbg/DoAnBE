package com.company_management.config;

import com.company_management.job.ContractSuspensionActiveJob;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final ApplicationContext applicationContext;

    @Value("${quartz.cron.contractSuspensionActive}")
    private String contractSuspensionActive;

    @Bean
    public AutowiringSpringBeanJobFactory jobFactory() {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public JobDetailFactoryBean contractSuspensionActiveJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ContractSuspensionActiveJob.class);
        factoryBean.setDescription("Contract Renewal Job");
        factoryBean.setDurability(true);
        return factoryBean;
    }

    @Bean
    public CronTriggerFactoryBean contractSuspensionActiveTrigger(
            @Qualifier("contractSuspensionActiveJobDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean cronTriggerFactoryBean = new CronTriggerFactoryBean();
        cronTriggerFactoryBean.setJobDetail(jobDetail);
        cronTriggerFactoryBean.setCronExpression(contractSuspensionActive);
        return cronTriggerFactoryBean;
    }
}
