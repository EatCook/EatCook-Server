package com.itcook.cooking.batch.job;

import com.itcook.cooking.domain.domains.user.adaptor.UserAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserBadgeUpdateBatch {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final UserAdaptor userAdaptor;

    @Bean
    public Job userBadgeUpdateJob() {
        return jobBuilderFactory.get("userBadgeUpdateJob")
            .start(userBadgeUpdateStep())
            .build();
    }

    @Bean
    public Step userBadgeUpdateStep() {
        return stepBuilderFactory.get("userBadgeUpdateStep")
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>>>>>>> start step1");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

}
