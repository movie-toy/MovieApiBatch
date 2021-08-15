package com.movietoy.batch.movietoybatch.job;

import com.movietoy.batch.movietoybatch.domain.MovieInfo;
import com.movietoy.batch.movietoybatch.domain.MovieInfoRepository;
import com.movietoy.batch.movietoybatch.service.MovieApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
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
        return jobBuilderFactory.get("movieInfoJob")
                .start(movieInfoStep())
                .build();
    }

    @Bean
    public Step movieInfoStep(){
        return stepBuilderFactory.get("movieInfoStep")
                .<String, MovieInfo>chunk(CHUNKSIZE)
                .reader(movieInfoReader())
                .processor(movieInfoProcessor())
                .writer(movieInfoWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<String> movieInfoReader() {

        JpaPagingItemReader<String> reader = new JpaPagingItemReader<String>() {
            @Override
            public int getPage() {
                return 0;
            }
        };
        reader.setQueryString("SELECT m.movieCd FROM MovieList m WHERE m.batchStatus IS NULL ORDER BY m.id asc");
        reader.setPageSize(CHUNKSIZE);
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setName("movieInfoReader");

        return reader;
//        return new JpaPagingItemReaderBuilder<String>()
//                .name("movieInfoReader")
//                .entityManagerFactory(entityManagerFactory)
//                .pageSize(CHUNKSIZE)
//                .queryString("SELECT m.movieCd FROM MovieList m ORDER BY m.Id asc")
//                .build();
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
