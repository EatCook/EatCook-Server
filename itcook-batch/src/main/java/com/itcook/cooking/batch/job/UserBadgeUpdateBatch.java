package com.itcook.cooking.batch.job;


import static com.itcook.cooking.domain.domains.user.domain.entity.QItCookUser.itCookUser;

import com.itcook.cooking.batch.expression.Expression;
import com.itcook.cooking.batch.options.QuerydslNoOffsetNumberOptions;
import com.itcook.cooking.batch.reader.QuerydslNoOffsetPagingItemReader;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.domain.repository.UserQueryRepository;
import com.itcook.cooking.domain.domains.user.domain.repository.dto.UserPostCount;
import java.net.ConnectException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserBadgeUpdateBatch {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory emf;
    private final UserQueryRepository userQueryRepository;

    private int chunkSize;

    @Value("${chunkSize:100}")
    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

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

    @Bean
    public Step userBadgeUpdateStep() {
        return stepBuilderFactory.get("userBadgeUpdateStep")
            .<ItCookUser, ItCookUser>chunk(chunkSize)
            .reader(querydslNoOffsetPagingItemReader())
            .writer(userBadgeUpdateWriter())
            .faultTolerant()
            .retry(ConnectException.class)
            .retryLimit(2)
            .build();
    }

    @Bean
    public QuerydslNoOffsetPagingItemReader<ItCookUser> querydslNoOffsetPagingItemReader() {
        QuerydslNoOffsetNumberOptions<ItCookUser, Long> options = new QuerydslNoOffsetNumberOptions<>(
            itCookUser.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, chunkSize, options,
            jpaQueryFactory -> jpaQueryFactory
                .selectFrom(itCookUser)
        );
    }

    @Bean
    public ItemWriter<ItCookUser> userBadgeUpdateWriter() {
        return new ItemWriter<ItCookUser>() {
            @Override
            public void write(List<? extends ItCookUser> items) throws Exception {
                List<Long> userIds = items.stream().map(ItCookUser::getId).toList();
                List<UserPostCount> userPostCounts = userQueryRepository.getUserPostCount(userIds);

                Map<UserBadge, List<Long>> userBadgeListMap = userPostCounts.stream()
                    .collect(
                        Collectors.groupingBy(
                            UserPostCount::updateUserBadge,
                            Collectors.mapping(UserPostCount::getUserId, Collectors.toList())
                        )
                    );

                
                userBadgeListMap.forEach(userQueryRepository::updateBadge);
            }
        };
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
