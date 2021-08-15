package com.movietoy.batch.movietoybatch.job;


import com.movietoy.batch.movietoybatch.domain.DailyMovie;
import com.movietoy.batch.movietoybatch.service.BoxOfficeApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DailyBoxOfficeJobConfig {

    private final JobBuilderFactory jobBuilderFactory;  //Job 빌더 생성용
    private final StepBuilderFactory stepBuilderFactory; //Step 빌더 생성용
    private final EntityManagerFactory entityManagerFactory;
    private final BoxOfficeApiService boxOfficeApiService;
    private static final int CHUNKSIZE = 5; //쓰기 단위인 청크사이즈


    // JobBuilderFactory를 통해서 tutorialJob을 생성
    @Bean
    public Job dailyBoxOfficeJob(){
        return jobBuilderFactory.get("dailyBoxOfficeJob")
                .start(dailyBoxOfficeStep())
                .build();
    }

    // StepBuilderFactory를 통해서 tutorialStep을 생성
/*    @Bean
    public Step dailyBoxOfficeStep() {
        return stepBuilderFactory.get("dailyBoxOfficeStep")
                //Tasklet 인터페이스 안에 excute 메소드 밖에없기때문에 람다식으로 변환 가능
                //.tasklet(new movieToyApiTasklet())
                .tasklet((contribution, chunkContext)->{
                    //일간 박스오피스 Insert
                    boxOfficeApiService.insertDailyBoxOffice();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }*/

    @Bean
    public Step dailyBoxOfficeStep() {
        return stepBuilderFactory.get("dailyBoxOfficeStep")
                .<DailyMovie, DailyMovie>chunk(CHUNKSIZE)
                .reader(dailyBoxOfficeReader())
                .writer(dailyBoxOfficeWriter())
                .build();
    }

    @Bean
    public ListItemReader<DailyMovie> dailyBoxOfficeReader() {
        List<DailyMovie> dailyMovie = boxOfficeApiService.insertDailyBoxOffice();
        return new ListItemReader<>(dailyMovie);
    }

    @Bean
    public JpaItemWriter<DailyMovie> dailyBoxOfficeWriter(){
        JpaItemWriter<DailyMovie> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
