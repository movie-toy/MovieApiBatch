package com.movietoy.batch.movietoybatch.service;

import com.movietoy.batch.movietoybatch.api.BoxOfficeApi;
import com.movietoy.batch.movietoybatch.domain.DailyMovie;
import com.movietoy.batch.movietoybatch.domain.WeeklyMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoxOfficeApiService {

    private final BoxOfficeApi boxOfficeApi;

    public List<DailyMovie> insertDailyBoxOffice(){
        return boxOfficeApi.dailyBoxOffice();
    }

    public List<WeeklyMovie> insertWeeklyBoxOffice(){
        return boxOfficeApi.weeklyBoxOffice();
    }

}
