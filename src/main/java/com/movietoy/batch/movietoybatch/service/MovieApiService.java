package com.movietoy.batch.movietoybatch.service;

import com.movietoy.batch.movietoybatch.api.MovieApi;
import com.movietoy.batch.movietoybatch.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieApiService {

    private final MovieListRepository movieListRepository;
    private final MovieInfoRepository movieInfoRepository;
    private final MovieApi movieApi;


    public List<MovieList> selectAllMovieList() {
        List<MovieList> movieLists = new ArrayList<>();
//        int num=1;
//        while(true){
//            log.info("========================="+num+"============================");
//            List<MovieList> movieList = movieApi.movieList(Integer.toString(num));
//            if(movieList.size() == 0) break;
//            movieLists.addAll(movieList);
//            num++;
//        }

        return movieLists;
    }

    public MovieInfo selectMovieInfo(String movieCd) {
        //영화 리스트 batch상태 코드 값 Y로 변경 ( Y = 상세 데이터 넣음, N = 상세 데이터 안넣음 )
        MovieList movieList = movieListRepository.findByMovieCd(movieCd);
        MovieInfo movieInfo = movieApi.movieInfo(movieCd);
        //API에서 호출할 수 있는 일일 트래픽을 넘어갔으면 key를 바꾼다.
        if(movieInfo == null){
            //상인 키 -> 호진 키
            if(movieApi.getKey().equals("f778bea14d8ca8349bc583598d1288e9")){
                movieApi.setKey("aa70b0701d04d2ab017306b706c486d8");
            //호진 키 -> 홍제 키
            } else if (movieApi.getKey().equals("aa70b0701d04d2ab017306b706c486d8")) {
                movieApi.setKey("6b52eea1663b4710d8589939f0097554");
            //홍제 키 -> 상인 키2
            } else if (movieApi.getKey().equals("6b52eea1663b4710d8589939f0097554")) {
                movieApi.setKey("ac4b5031aa97be6f4c74686ce6b4fbf5");
            }
        }
        movieList.setBatchStatus("Y");
        movieListRepository.save(movieList);

        return movieInfo;
    }

}
