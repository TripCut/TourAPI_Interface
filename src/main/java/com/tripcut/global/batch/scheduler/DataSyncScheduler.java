package com.tripcut.global.batch.scheduler;

import org.quartz.*;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSyncScheduler {

    private final Scheduler scheduler;

    @PostConstruct
    public void init() {
        try {
            JobDetail jobDetail = JobBuilder.newJob(DataSyncQuartzJob.class)
                    .withIdentity("dataSyncJob")
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("dataSyncTrigger")
                    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 1 * * ?")) // 매일 새벽 1시에 실행
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("데이터 동기화 스케줄러가 등록되었습니다.");
        } catch (SchedulerException e) {
            log.error("데이터 동기화 스케줄러 등록 실패", e);
        }
    }
}