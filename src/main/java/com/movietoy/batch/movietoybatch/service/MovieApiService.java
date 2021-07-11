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
//            List<MovieList> movieList = movieApi.movieList(Integer.toString(num));
//            if(movieList.size() == 0) break;
//            movieLists.addAll(movieList);
//            num++;
//        }
        return movieLists;
    }

    public MovieInfo selectMovieInfo(String movieCd) {
        //영화 리스트 batch상태 코드 값 Y로 변경 ( Y = 상세 데이터 넣음, NULL = 상세 데이터 안넣음 )
        MovieList movieList = movieListRepository.findByMovieCd(movieCd);
        MovieInfo movieInfo = new MovieInfo();
        movieInfo = movieApi.movieInfo(movieCd);
        //API에서 호출할 수 있는 일일 트래픽을 넘어갔으면 key를 바꾼다.
        if(movieInfo == null) {
            System.out.println("********************key out********************");
            //상인 키 -> 호진 키
            if (movieApi.getKey().equals("f778bea14d8ca8349bc583598d1288e9")) {
                System.out.println("********************key 상인 -> 호진********************");
                movieApi.setKey("aa70b0701d04d2ab017306b706c486d8");
                movieInfo = movieApi.movieInfo(movieCd);
            }
        }

        if(movieInfo == null) {
            System.out.println("********************key out********************");
            //호진 키 -> 홍제 키
            if (movieApi.getKey().equals("aa70b0701d04d2ab017306b706c486d8")) {
                System.out.println("********************key 호진 -> 홍제********************");
                movieApi.setKey("6b52eea1663b4710d8589939f0097554");
                movieInfo = movieApi.movieInfo(movieCd);
            }
        }

        if(movieInfo == null) {
            System.out.println("********************key out********************");
            //홍제 키 -> 상인 키2
            if (movieApi.getKey().equals("6b52eea1663b4710d8589939f0097554")) {
                System.out.println("********************key 홍제 -> 상인2********************");
                movieApi.setKey("ac4b5031aa97be6f4c74686ce6b4fbf5");
                movieInfo = movieApi.movieInfo(movieCd);
            }
        }

        if(movieInfo == null) {
            System.out.println("********************key out********************");
            //상인 키2 -> 호진 키2
            if (movieApi.getKey().equals("ac4b5031aa97be6f4c74686ce6b4fbf5")) {
                System.out.println("********************key 상인2 -> 호진2********************");
                movieApi.setKey("e396c6dfac8b425d0590a2c4d9b822a7");
                movieInfo = movieApi.movieInfo(movieCd);
            }
        }

        if(movieInfo != null){
            System.out.println("===============key OK================");
            movieList.setBatchStatus("Y");
            movieListRepository.save(movieList);
        }

        return movieInfo;
    }
}
