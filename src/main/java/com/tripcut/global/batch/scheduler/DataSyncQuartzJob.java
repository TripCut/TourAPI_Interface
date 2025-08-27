package com.tripcut.global.batch.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("dataSyncQuartzJob")
@RequiredArgsConstructor
public class DataSyncQuartzJob extends QuartzJobBean {

    private final JobLauncher jobLauncher;
    private final Job dataSyncJob;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(dataSyncJob, jobParameters);
            log.info("데이터 동기화 배치 작업이 실행되었습니다.");
        } catch (Exception e) {
            log.error("데이터 동기화 배치 작업 실행 실패", e);
            throw new JobExecutionException(e);
        }
    }
}