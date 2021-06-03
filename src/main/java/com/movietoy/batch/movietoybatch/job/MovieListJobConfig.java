package com.movietoy.batch.movietoybatch.job;

import com.movietoy.batch.movietoybatch.api.MovieApi;
import com.movietoy.batch.movietoybatch.domain.MovieList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
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
    private final MovieApi movieApi;
    private static final int CHUNKSIZE = 5; //쓰기 단위인 청크사이즈

    @Bean
    public Job movieListJob(){
        return jobBuilderFactory.get("movieListJob")
                .start(movieListStep())
                .build();
    }

    @Bean
    public Step movieListStep(){
        return stepBuilderFactory.get("movieListStep")
                .<MovieList, MovieList>chunk(CHUNKSIZE)
                .reader(movieListReader(null))
                .writer(movieListWriter())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<MovieList> movieListReader(@Value("#{jobParameters[index]}") String index) {
        log.info("############################# index : " + index+"#########################################");
        List<MovieList> movieList = movieApi.movieList(index);
        return new ListItemReader<>(movieList);
    }

    @Bean
    public JpaItemWriter<MovieList> movieListWriter(){
        JpaItemWriter<MovieList> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
