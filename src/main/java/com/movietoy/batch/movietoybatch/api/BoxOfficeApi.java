package com.movietoy.batch.movietoybatch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movietoy.batch.movietoybatch.domain.DailyMovie;
import com.movietoy.batch.movietoybatch.domain.WeeklyMovie;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class BoxOfficeApi {
    //발급키
    String key = "f778bea14d8ca8349bc583598d1288e9";

    public List<DailyMovie> dailyBoxOffice(){

        List<DailyMovie> dailyBoxOfficeList = new ArrayList<>(); //Rest로 가져온 데이터를 리스트에 넣는다.

        String dailyResponse = "";

        //전날 박스오피스 조회 ( 오늘 날짜꺼는 안나옴.. )
        LocalDateTime time = LocalDateTime.now().minusDays(1);
        String targetDt =  time.format(DateTimeFormatter.ofPattern("yyyMMdd"));

        //ROW 개수
        String itemPerPage = "10";

        //다양성영화(Y)/상업영화(N)/전체(default)
        String multiMovieYn = "";

        //한국영화(K)/외국영화(F)/전체(default)
        String repNationCd = "";

        //상영지역별 코드/전체(default)
        String wideAreaCd = "";

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            // 일일 박스오피스 서비스 호출 (boolean isJson, String targetDt, String itemPerPage,String multiMovieYn, String repNationCd, String wideAreaCd)
            dailyResponse = service.getDailyBoxOffice(true, targetDt, itemPerPage, multiMovieYn, repNationCd, wideAreaCd);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(dailyResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //차근차근 parsing하기
            JSONObject parse_boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");

            //박스오피스 종류
            String boxofficeType = (String) parse_boxOfficeResult.get("boxofficeType");

            //박스오피스 조회 일자
            String showRange = (String) parse_boxOfficeResult.get("showRange");

            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray parse_dailyBoxOfficeList = (JSONArray) parse_boxOfficeResult.get("dailyBoxOfficeList");
            for(int i=0; i<parse_dailyBoxOfficeList.size(); i++){
                JSONObject dailyBoxOffice = (JSONObject) parse_dailyBoxOfficeList.get(i);
                //JSON object -> Java Object(Entity) 변환
                DailyMovie dailyMovie = objectMapper.readValue(dailyBoxOffice.toString(), DailyMovie.class);
                //DB저장
                dailyMovie.setTargetDt(targetDt);
                dailyMovie.setBoxofficeType(boxofficeType);
                dailyMovie.setShowRange(showRange);
                dailyBoxOfficeList.add(dailyMovie);
                //dailyMovieRepository.save(dailyMovie);
            }
        }catch(Exception e){
            e.getMessage();
        }
        return dailyBoxOfficeList;
    }

    public List<WeeklyMovie> weeklyBoxOffice(){

        List<WeeklyMovie> weeklyBoxOfficeList = new ArrayList<>(); //Rest로 가져온 데이터를 리스트에 넣는다.

        String weeklyResponse = "";

        //전주 박스오피스 조회 ( 해당주는 안나옴.. )
        LocalDateTime time = LocalDateTime.now().minusDays(7);
        String targetDt =  time.format(DateTimeFormatter.ofPattern("yyyMMdd"));

        //주간/주말/주중 선택
        String weekGb = "0";

        //ROW 개수
        String itemPerPage = "10";

        //다양성영화(Y)/상업영화(N)/전체(default)
        String multiMovieYn = "";

        //한국영화(K)/외국영화(F)/전체(default)
        String repNationCd = "";

        //상영지역별 코드/전체(default)
        String wideAreaCd = "";

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            // 일일 박스오피스 서비스 호출 (boolean isJson, String targetDt, String itemPerPage, String weekGb, String multiMovieYn, String repNationCd, String wideAreaCd)
            weeklyResponse = service.getWeeklyBoxOffice(true, targetDt, itemPerPage, weekGb, multiMovieYn, repNationCd, wideAreaCd);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(weeklyResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //차근차근 parsing하기
            JSONObject parse_boxOfficeResult = (JSONObject) jsonObject.get("boxOfficeResult");

            //박스오피스 종류
            String boxofficeType = (String) parse_boxOfficeResult.get("boxofficeType");


            //박스오피스 조회 일자
            String showRange = (String) parse_boxOfficeResult.get("showRange");


            //박스오피스 조회 일자
            String yearWeekTime = (String) parse_boxOfficeResult.get("yearWeekTime");


            ObjectMapper objectMapper = new ObjectMapper();
            JSONArray parse_weeklyBoxOfficeList = (JSONArray) parse_boxOfficeResult.get("weeklyBoxOfficeList");
            for(int i=0; i<parse_weeklyBoxOfficeList.size(); i++){
                JSONObject weeklyBoxOffice = (JSONObject) parse_weeklyBoxOfficeList.get(i);
                //JSON object -> Java Object(Entity) 변환
                WeeklyMovie weeklyMovie = objectMapper.readValue(weeklyBoxOffice.toString(), WeeklyMovie.class);
                //DB저장
                weeklyMovie.setTargetDt(targetDt);
                weeklyMovie.setBoxofficeType(boxofficeType);
                weeklyMovie.setShowRange(showRange);
                weeklyMovie.setYearWeekTime(yearWeekTime);
                weeklyBoxOfficeList.add(weeklyMovie);
                //weeklyMovieRepository.save(weeklyMovie);
            }
        }catch(Exception e){
            e.getMessage();
        }
        return weeklyBoxOfficeList;
    }
}
