package com.movietoy.batch.movietoybatch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class MovieList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String movieCd;

    private String movieNm;

    private String movieNmEn;

    private String prdtYear;

    private String openDt;

    private String typeNm;

    private String prdtStatNm;

    private String nationAlt;

    private String genreAlt;

    private String repNationNm;

    private String repGenreNm;

    @Column(columnDefinition = "json")
    private String directors;

    @Column(columnDefinition = "json")
    private String companys;

    @Column(length = 1, columnDefinition = "char default 'N' ")
    private String batchStatus;
}
