package com.movietoy.batch.movietoybatch.scheduler;

import com.movietoy.batch.movietoybatch.job.MovieToyApiJobConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SchedulerController {

    //Job 클래스 주입
    private final MovieToyApiJobConfig movieToyApiJobConfig;
    //Job을 실행하기 위한 클래스 주입
    private final JobLauncher jobLauncher;

    // 5초마다 실행
    //@Scheduled(fixedDelay = 5 * 1000L)
    //Cron 표현식
    //    초  분  시  일  월  요일 연도(생략가능)
    //ex) 0  1   1   10  *   *  -> 매월 10일 01시 01분에 실행
    //ex  0  0   14  *   *   *  -> 매일 14시에 실행
    @Scheduled(cron ="0 0 1 * * *") //매일 01시에 실행
    public void executeJob () {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                            .addString("datetime", LocalDateTime.now()
                            .toString())
                            .toJobParameters();
            jobLauncher.run(
                    movieToyApiJobConfig.movieToyApiJob(),
                    jobParameters  // job parameter 설정
            );
        } catch (JobExecutionException ex) {
            log.info(ex.getMessage());
            ex.printStackTrace();
        }
    }
}