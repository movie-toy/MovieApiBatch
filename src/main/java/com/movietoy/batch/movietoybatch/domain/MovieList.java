package com.movietoy.batch.movietoybatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MovieList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*영화코드*/
    private String movieCd;

    /*영화명(국문)*/
    private String movieNm;

    /*영화명(영문)*/
    private String movieNmEn;

    /*제작연도*/
    private String prdtYear;

    /*개봉일*/
    private String openDt;

    /*영화유형*/
    private String typeNm;

    /*제작상태*/
    private String prdtStatNm;

    /*제작국가(전체)*/
    private String nationAlt;

    /*영화장르(전체)*/
    private String genreAlt;

    /*대표 제작국가명*/
    private String repNationNm;

    /*대표 장르명*/
    private String repGenreNm;

    /*영화감독*/
    @Column(columnDefinition = "json")
    private String directors;

    /*제작사*/
    @Column(columnDefinition = "json")
    private String companys;

    /*영화상세정보 입력 유무*/
    @Column(length = 1)
    private String batchStatus;

    @CreationTimestamp
    private LocalDateTime modification_date_time;
}
