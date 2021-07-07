package com.movietoy.batch.movietoybatch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movietoy.batch.movietoybatch.domain.MovieInfo;
import com.movietoy.batch.movietoybatch.domain.MovieList;
import kr.or.kobis.kobisopenapi.consumer.rest.KobisOpenAPIRestService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class MovieApi {
    //발급키
    public static String key = "f778bea14d8ca8349bc583598d1288e9";

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        MovieApi.key = key;
    }


    public List<MovieList> movieList(String index){

        List<MovieList> allMovieList = new ArrayList<>();

        String movieListResponse = "";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("curPage",index);
        paramMap.put("itemPerPage", "100");
        paramMap.put("movieNm", "");
        paramMap.put("directorNm", "");
        paramMap.put("openStartDt", "");
        paramMap.put("openEndDt", "");
        paramMap.put("prdtStartYear", "");
        paramMap.put("prdtEndYear", "");
        paramMap.put("repNationCd", "");
        String[] movieTypeCdArr = {};
        paramMap.put("movieTypeCdArr", movieTypeCdArr);

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);
            movieListResponse = service.getMovieList(true, paramMap);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(movieListResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //차근차근 parsing하기
            JSONObject parse_movieListResult = (JSONObject) jsonObject.get("movieListResult");

            //JSON object -> Java Object(Entity) 변환하기위한 Mapper 선언
            ObjectMapper objectMapper = new ObjectMapper();

            //새로운 JSONObject 선언
            JSONObject movieListResultObject = new JSONObject();

            JSONArray parse_movieList = (JSONArray) parse_movieListResult.get("movieList");
            for(int i=0; i<parse_movieList.size(); i++){
                JSONObject movieListObject = (JSONObject) parse_movieList.get(i);

                String repNationNm = (String) movieListObject.get("repNationNm");
                movieListResultObject.put("repNationNm", repNationNm);

                String nationAlt = (String) movieListObject.get("nationAlt");
                movieListResultObject.put("nationAlt", nationAlt);

                String repGenreNm = (String) movieListObject.get("repGenreNm");
                movieListResultObject.put("repGenreNm", repGenreNm);

                String movieNm = (String) movieListObject.get("movieNm");
                movieListResultObject.put("movieNm", movieNm);

                String movieCd = (String) movieListObject.get("movieCd");
                movieListResultObject.put("movieCd", movieCd);

                String prdtStatNm = (String) movieListObject.get("prdtStatNm");
                movieListResultObject.put("prdtStatNm", prdtStatNm);

                String prdtYear = (String) movieListObject.get("prdtYear");
                movieListResultObject.put("prdtYear", prdtYear);

                String typeNm = (String) movieListObject.get("typeNm");
                movieListResultObject.put("typeNm", typeNm);

                String openDt = (String) movieListObject.get("openDt");
                movieListResultObject.put("openDt", openDt);

                String movieNmEn = (String) movieListObject.get("movieNmEn");
                movieListResultObject.put("movieNmEn", movieNmEn);

                String genreAlt = (String) movieListObject.get("genreAlt");
                movieListResultObject.put("genreAlt", genreAlt);

                //영화감독(directors) Array 추출
                JSONArray parse_directorsList = (JSONArray) movieListObject.get("directors");
                movieListResultObject.put("directors", parse_directorsList.toString());

                //제작사(companys) Array 추출
                JSONArray parse_companysList = (JSONArray) movieListObject.get("companys");
                movieListResultObject.put("companys", parse_companysList.toString());

                //JSON object -> Java Object(Entity) 변환
                MovieList movieList = objectMapper.readValue(movieListResultObject.toString(), MovieList.class);
                allMovieList.add(movieList);
            }

        }catch(Exception e){
            e.getMessage();
        }
        return allMovieList;
    }


    public MovieInfo movieInfo(String MovieCd){

        MovieInfo movieInfo = null;

        String movieInfoResponse = "";

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("movieCd",MovieCd);

        try {
            // KOBIS 오픈 API Rest Client를 통해 호출
            KobisOpenAPIRestService service = new KobisOpenAPIRestService(key);

            movieInfoResponse = service.getMovieInfo(true, paramMap);

            //JSON Parser 객체 생성
            JSONParser jsonParser = new JSONParser();

            //Parser로 문자열 데이터를 객체로 변환
            Object obj = jsonParser.parse(movieInfoResponse);

            //파싱한 obj를 JSONObject 객체로 변환
            JSONObject jsonObject = (JSONObject) obj;

            //JSON object -> Java Object(Entity) 변환하기위한 Mapper 선언
            ObjectMapper objectMapper = new ObjectMapper();

            //새로운 JSONObject 선언
            JSONObject movieInfoResultObject = new JSONObject();

            //차근차근 parsing하기
            JSONObject parse_movieInfoResult = (JSONObject) jsonObject.get("movieInfoResult");

            //영화 정보
            JSONObject parse_movieInfo = (JSONObject) parse_movieInfoResult.get("movieInfo");

            //영화 코드
            String movieCd = (String) parse_movieInfo.get("movieCd");
            movieInfoResultObject.put("movieCd", movieCd);

            //영화 한글명
            String movieNm = (String) parse_movieInfo.get("movieNm");
            movieInfoResultObject.put("movieNm", movieNm);

            //영화 영문명
            String movieNmEn = (String) parse_movieInfo.get("movieNmEn");
            movieInfoResultObject.put("movieNmEn", movieNmEn);

            //영화 원문명
            String movieNmOg = (String) parse_movieInfo.get("movieNmOg");
            movieInfoResultObject.put("movieNmOg", movieNmOg);

            //제작연도
            String prdtYear = (String) parse_movieInfo.get("prdtYear");
            movieInfoResultObject.put("prdtYear", prdtYear);

            //오픈일자
            String openDt = (String) parse_movieInfo.get("openDt");
            movieInfoResultObject.put("openDt", openDt);

            //제작상태
            String prdtStatNm = (String) parse_movieInfo.get("prdtStatNm");
            movieInfoResultObject.put("prdtStatNm", prdtStatNm);

            //영화유형
            String typeNm = (String) parse_movieInfo.get("typeNm");
            movieInfoResultObject.put("typeNm", typeNm);

            //국가(nations) Array 추출
            JSONArray parse_nationsList = (JSONArray) parse_movieInfo.get("nations");
            movieInfoResultObject.put("nations", parse_nationsList.toString());

            //장르(genres) Array 추출
            JSONArray parse_genresList = (JSONArray) parse_movieInfo.get("genres");
            movieInfoResultObject.put("genres", parse_genresList.toString());

            //영화감독(directors) Array 추출
            JSONArray parse_directorsList = (JSONArray) parse_movieInfo.get("directors");
            movieInfoResultObject.put("directors", parse_directorsList.toString());

            //배우(actors) Array 추출
            JSONArray parse_actorsList = (JSONArray) parse_movieInfo.get("actors");
            movieInfoResultObject.put("actors", parse_actorsList.toString());

            //영화사(companys) Array 추출
            JSONArray parse_companysList = (JSONArray) parse_movieInfo.get("companys");
            movieInfoResultObject.put("companys", parse_companysList.toString());

            //심의정보(audits) Array 추출
            JSONArray parse_auditsList = (JSONArray) parse_movieInfo.get("audits");
            movieInfoResultObject.put("audits", parse_auditsList.toString());

            //JSON object -> Java Object(Entity) 변환
            movieInfo = objectMapper.readValue(movieInfoResultObject.toString(), MovieInfo.class);

        }catch(Exception e){
            e.getMessage();
        }

        return movieInfo;

    }
}
