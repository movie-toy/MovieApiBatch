package com.movietoy.batch.movietoybatch.job;

import com.movietoy.batch.movietoybatch.domain.MovieInfo;
import com.movietoy.batch.movietoybatch.domain.MovieInfoRepository;
import com.movietoy.batch.movietoybatch.domain.MovieList;
import com.movietoy.batch.movietoybatch.service.MovieApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class MovieInfoConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final MovieApiService movieApiService;
    private final MovieInfoRepository movieInfoRepository;
    private static final int CHUNKSIZE = 100;

    @Bean
    public Job movieInfoJob() {
        log.info("movieInfoJob Start!!!");
        return jobBuilderFactory.get("movieInfoJob")
                .start(movieInfoStep())
                .build();
    }

    @Bean
    public Step movieInfoStep(){
        log.info("movieInfoStep Start!!!");
        return stepBuilderFactory.get("movieInfoStep")
                .<String, MovieInfo>chunk(CHUNKSIZE)
                .reader(movieInfoReader())
                .processor(movieInfoProcessor())
                .writer(movieInfoWriter())
                .build();
    }

    @Bean
    public JpaPagingItemReader<String> movieInfoReader() {
        return new JpaPagingItemReaderBuilder<String>()
                .name("movieInfoReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNKSIZE)
                .queryString("SELECT m.movieCd FROM MovieList m WHERE batchStatus = 'N' and Id <= 10000")
                .build();

    }

    @Bean
    public ItemProcessor<String, MovieInfo> movieInfoProcessor() {
        return movieCd -> movieApiService.selectMovieInfo(movieCd);
    }


    @Bean
    public JpaItemWriter<MovieInfo> movieInfoWriter(){
        JpaItemWriter<MovieInfo> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

//    @Bean
//    public ItemWriter<MovieInfo> movieInfoWriter() {
//        return movieInfo -> {
//            movieInfoRepository.saveAll(movieInfo);
//        };
//    }



}
