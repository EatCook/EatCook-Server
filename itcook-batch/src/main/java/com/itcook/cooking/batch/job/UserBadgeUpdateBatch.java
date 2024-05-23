package com.itcook.cooking.batch.job;

import com.itcook.cooking.domain.domains.user.adaptor.UserAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
    private static final int CHUNK_SIZE = 100;

    @Bean
    public Job userBadgeUpdateJob() {
        return jobBuilderFactory.get("userBadgeUpdateJob")
            .incrementer(new RunIdIncrementer())
            .start(userBadgeUpdateStep())
            .on("FAILED").to(failureStep()).on("*").end()
            .from(userBadgeUpdateStep())
            .on("*").end()
            .end()
            .build();
    }

    // TODO retry
    @Bean
    public Step userBadgeUpdateStep() {
        return stepBuilderFactory.get("userBadgeUpdateStep")
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>>>>>>> start step1");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step failureStep() {
        return stepBuilderFactory.get("failureStep")
            .tasklet((contribution, chunkContext) -> {
                JobExecution jobExecution = chunkContext.getStepContext().getStepExecution()
                    .getJobExecution();
                log.error(
                    "유저 뱃지 업데이트 실패 \n jobExecution id : {} failure : {} \n stepExecution failure : {}",
                    jobExecution.getJobId(),
                    jobExecution.getAllFailureExceptions(), jobExecution.getStepExecutions());
                return RepeatStatus.FINISHED;
            })
            .build();
    }

}
