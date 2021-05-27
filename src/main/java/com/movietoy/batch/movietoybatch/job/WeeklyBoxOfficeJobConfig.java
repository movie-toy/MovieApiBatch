package com.movietoy.batch.movietoybatch.job;


import com.movietoy.batch.movietoybatch.service.BoxOfficeApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WeeklyBoxOfficeJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final BoxOfficeApiService boxOfficeApiService;

    @Bean
    public Job weeklyBoxOfficeJob(){
        log.info("weeklyBoxOfficeJob Start!!!");
        return jobBuilderFactory.get("weeklyBoxOfficeJob")
                .start(weeklyBoxOfficeStep())
                .build();
    }

    @Bean
    public Step weeklyBoxOfficeStep() {
        log.info("weeklyBoxOfficeStep Start!!!");
        return stepBuilderFactory.get("weeklyBoxOfficeStep")
                .tasklet((contribution, chunkContext)->{
                    //주간 박스오피스 Insert
                    boxOfficeApiService.insertWeeklyBoxOffice();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
