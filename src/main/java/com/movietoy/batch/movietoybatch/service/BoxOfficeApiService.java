package com.movietoy.batch.movietoybatch.service;

import com.movietoy.batch.movietoybatch.api.BoxOfficeApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoxOfficeApiService {

    private final BoxOfficeApi boxOfficeApi;

    public void insertDailyBoxOffice(){
        boxOfficeApi.dailyBoxOffice();
    }

    public void insertWeeklyBoxOffice(){
        boxOfficeApi.weeklyBoxOffice();
    }

}
