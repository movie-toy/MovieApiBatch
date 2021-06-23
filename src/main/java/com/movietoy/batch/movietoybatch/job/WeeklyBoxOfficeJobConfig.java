package com.movietoy.batch.movietoybatch.job;


import com.movietoy.batch.movietoybatch.api.BoxOfficeApi;
import com.movietoy.batch.movietoybatch.domain.WeeklyMovie;
import com.movietoy.batch.movietoybatch.service.BoxOfficeApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WeeklyBoxOfficeJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final BoxOfficeApiService boxOfficeApiService;
    private static final int CHUNKSIZE = 5;

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
                .<WeeklyMovie, WeeklyMovie>chunk(CHUNKSIZE)
                .reader(weeklyBoxOfficeReader())
                .writer(weeklyBoxOfficeWriter())
                .build();
    }

    @Bean
    public ListItemReader<WeeklyMovie> weeklyBoxOfficeReader() {
        List<WeeklyMovie> weeklyMovie = boxOfficeApiService.insertWeeklyBoxOffice();
        return new ListItemReader<>(weeklyMovie);
    }

    @Bean
    public JpaItemWriter<WeeklyMovie> weeklyBoxOfficeWriter(){
        JpaItemWriter<WeeklyMovie> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
