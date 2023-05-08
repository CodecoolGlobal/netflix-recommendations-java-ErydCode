package com.codecool.netflix.logic;

import com.codecool.netflix.data.Credit;
import com.codecool.netflix.data.Title;
import com.codecool.netflix.data.enums.Role;
import com.codecool.netflix.data.enums.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SuppressWarnings("FieldCanBeLocal")
public class SimilarityScoreCalculator {
    private final Integer POINT_FOR_SAME_TYPE = 10;
    private final Integer POINT_FOR_EACH_SIMILAR_GENRE = 20;
    private final Integer POINT_FOR_EACH_SIMILAR_ACTOR = 15;
    private final Integer POINT_FOR_EACH_SIMILAR_DIRECTOR = 30;

    public Integer calculateSimilarityScore(Title titleOfInterest, List<Credit> titleOfInterestCredits, Title comparedTitle, List<Credit> allCredits) {
        //TODO: Your code here
        return Stream.of(getSimilarityScoreBasedOnType(titleOfInterest.getType(), comparedTitle.getType()),
                getSimilarityScoreBasedOnGenre(titleOfInterest.getGenres(), comparedTitle.getGenres()),
                getSimilarityScoreBasedOnActors(getCreditsByRole(getCastForTitle(comparedTitle, allCredits), Role.ACTOR), getCreditsByRole(getCastForTitle(comparedTitle, titleOfInterestCredits), Role.ACTOR)),
                getSimilarityScoreBasedOnDirectors(getCreditsByRole(getCastForTitle(comparedTitle, allCredits), Role.DIRECTOR), getCreditsByRole(getCastForTitle(comparedTitle, titleOfInterestCredits), Role.DIRECTOR)),
                getPointsForImdbScore(comparedTitle)).reduce(0, Integer::sum);
    }

    private List<Credit> getCastForTitle(Title title, List<Credit> credits) {
        //TODO: Your code here
        return new ArrayList<>(credits.stream().filter(credit -> credit.getId().equals(title.getId())).toList());
    }

    private Integer getSimilarityScoreBasedOnType(Type type1, Type type2) {
        //TODO: Your code here
        return type1.equals(type2) ? POINT_FOR_SAME_TYPE : 0;
        //return Stream.of(type1, type2).distinct().count() == 1 = POINT_FOR_SAME_TYPE : 0;
    }

    private Integer getSimilarityScoreBasedOnGenre(List<String> genre1, List<String> genre2) {
        //TODO: Your code here
        return (int) (POINT_FOR_EACH_SIMILAR_GENRE * genre1.stream().filter(genre2::contains).count()); //Maybe distinct() miss!
    }

    private List<String> getCreditsByRole(List<Credit> credits, Role role) {
        //TODO: Your code here
        return new ArrayList<>(credits.stream().filter(credit -> credit.getRole().equals(role)).map(Credit::getName).toList()); //Maybe credit.getRole needs Enum access.
    }

    private Integer getSimilarityScoreBasedOnActors(List<String> actors1, List<String> actors2) {
        //TODO: Your code here
        return (int) (POINT_FOR_EACH_SIMILAR_ACTOR * actors1.stream().filter(actors2::contains).count());
    }

    private Integer getSimilarityScoreBasedOnDirectors(List<String> directors1, List<String> directors2) {
        //TODO: Your code here
        return (int) (POINT_FOR_EACH_SIMILAR_DIRECTOR * directors1.stream().filter(directors2::contains).count());
    }

    private int getPointsForImdbScore(Title comparedTitle) {
        //TODO: Your code here
        return comparedTitle.getImdbScore() != null ? Math.round(comparedTitle.getImdbScore()) : 0;
    }

}
