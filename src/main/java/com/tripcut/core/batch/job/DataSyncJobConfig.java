package com.tripcut.core.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSyncJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean(name = "dataSyncJob")
    public Job dataSyncJob() {
        return new JobBuilder("dataSyncJob", jobRepository)
                .start(dataSyncStep())
                .build();
    }

    @Bean(name = "dataSyncStep")
    public Step dataSyncStep() {
        return new StepBuilder("dataSyncStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    log.info("데이터 동기화 작업 시작");
                    // TODO: 실제 데이터 동기화 로직 구현
                    log.info("데이터 동기화 작업 완료");
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
    }
} 