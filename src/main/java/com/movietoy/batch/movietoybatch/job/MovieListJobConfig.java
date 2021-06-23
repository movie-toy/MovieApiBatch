package com.movietoy.batch.movietoybatch.job;

import com.movietoy.batch.movietoybatch.api.MovieApi;
import com.movietoy.batch.movietoybatch.domain.MovieList;
import com.movietoy.batch.movietoybatch.service.MovieApiService;
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
public class MovieListJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final MovieApiService movieApiService;
    private static final int CHUNKSIZE = 100; //쓰기 단위인 청크사이즈

    @Bean
    public Job movieListJob(){
        log.info("movieListJob Start!!!");
        return jobBuilderFactory.get("movieListJob")
                .start(movieListStep())
                .build();
    }

    @Bean
    public Step movieListStep(){
        log.info("movieListStep Start!!!");
        return stepBuilderFactory.get("movieListStep")
                .<MovieList, MovieList>chunk(CHUNKSIZE)
                .reader(movieListReader())
                .writer(movieListWriter())
                .build();
    }

    @Bean
    public ListItemReader<MovieList> movieListReader() {
        List<MovieList> movieList = movieApiService.selectAllMovieList();
        return new ListItemReader<>(movieList);
    }

    @Bean
    public JpaItemWriter<MovieList> movieListWriter(){
        JpaItemWriter<MovieList> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
