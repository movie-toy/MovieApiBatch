package com.movietoy.batch.movietoybatch.job;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.movietoy.batch.movietoybatch.api.BoxOfficeApi;
import com.movietoy.batch.movietoybatch.domain.Movie;
import com.movietoy.batch.movietoybatch.domain.MovieRepository;
import com.movietoy.batch.movietoybatch.service.BoxOfficeApiService;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MovieToyApiJobConfig {

    private final JobBuilderFactory jobBuilderFactory;  //Job 빌더 생성용
    private final StepBuilderFactory stepBuilderFactory; //Step 빌더 생성용
    private final BoxOfficeApiService boxOfficeApiService;


    // JobBuilderFactory를 통해서 tutorialJob을 생성
    @Bean
    public Job movieToyApiJob(){
        log.info("tutorialJob Start!!!");
        return jobBuilderFactory.get("movieToyApiJob")
                .start(movieToyApiStep())
                .build();
    }

    // StepBuilderFactory를 통해서 tutorialStep을 생성
    @Bean
    public Step movieToyApiStep() {
        log.info("tutorialStep Start!!!");
        return stepBuilderFactory.get("movieToyApiStep")
                //Tasklet 인터페이스 안에 excute 메소드 밖에없기때문에 람다식으로 변환 가능
                //.tasklet(new movieToyApiTasklet())
                .tasklet((contribution, chunkContext)->{
                    log.info("excuted tasklet !!");
                    //일간 박스오피스 Insert
                    boxOfficeApiService.insertDailyBoxOffice();
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
