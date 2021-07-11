package com.movietoy.batch.movietoybatch.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    /*영화코드*/
    private String movieCd;

    /*영화명(국문)*/
    private String movieNm;

    /*영화명(영문)*/
    private String movieNmEn;

    /*영화명(원문)*/
    private String movieNmOg;

    /*제작연도*/
    private String prdtYear;

    /*개봉연도*/
    private String openDt;

    /*제작상태*/
    private String prdtStatNm;

    /*영화유형*/
    private String typeNm;

    /*제작국가*/
    @Column(columnDefinition = "json")
    private String nations;

    /*장르 정보*/
    @Column(columnDefinition = "json")
    private String genres;

    /*감독*/
    @Column(columnDefinition = "json")
    private String directors;

    /*배우*/
    @Column(columnDefinition = "json")
    private String actors;

    /*참여 영화사*/
    @Column(columnDefinition = "json")
    private String companys;

    /*심의정보*/
    @Column(columnDefinition = "json")
    private String audits;

    @CreationTimestamp
    private LocalDateTime modification_date_time;


}
