package com.movietoy.batch.movietoybatch.service;

import com.movietoy.batch.movietoybatch.api.MovieApi;
import com.movietoy.batch.movietoybatch.domain.MovieList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieApiService {

    private final MovieApi movieApi;

    public List<MovieList> selectAllMovieList(){
        List<MovieList> movieLists = new ArrayList<>();
        int num=1;
        while(true){
            log.info("========================="+num+"============================");
            List<MovieList> movieList = movieApi.movieList(Integer.toString(num));
            if(movieList.size() == 0) break;
            movieLists.addAll(movieList);
            num++;
        }

        return movieLists;
    }
}
