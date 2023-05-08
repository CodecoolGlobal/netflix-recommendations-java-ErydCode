package com.codecool.netflix.logic;

import com.codecool.netflix.data.Credit;
import com.codecool.netflix.data.Title;
import com.codecool.netflix.data.TitleWithSimilarityScore;
import com.codecool.netflix.logic.reader.TitleReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

public class TitleManager implements CsvItemCollection {
    private final TitleReader reader;
    private final SimilarityScoreCalculator comparator;
    private List<Title> titles;

    public TitleManager(TitleReader reader, SimilarityScoreCalculator comparator) {
        this.reader = reader;
        this.comparator = comparator;
    }

    @Override
    public void init() throws IOException {
        titles = reader.readAll("/titles.csv");
    }

    public List<Title> getTopNImdbScoreFromTitles(int n) {
        //TODO: Your code here
        return new ArrayList<>(titles.stream().filter(title -> title.getImdbScore() != null)
                .sorted(Comparator.comparing(Title::getImdbScore).reversed()).limit(n).toList());
    }

    public List<Credit> getAllCreditsForTitle(String userTitle, List<Credit> credits) throws NoSuchElementException {
        //TODO: Your code here
        return new ArrayList<>(credits.stream().filter(credit -> credit.getId().equals(
                titles.stream().filter(title -> title.getTitle().contains(userTitle))
                        .findFirst().orElseThrow(NoSuchElementException::new).getId())).toList());
    }

    // Extra task - offset
    public List<Title> getTopNImdbScoreFromGivenGenre(String genre, int offset) {
        //TODO: Your code here
        return new ArrayList<>(titles.stream().filter(title -> title.getGenres().contains(genre))
                .filter(title -> title.getImdbScore() != null)
                .sorted(Comparator.comparing(Title::getImdbScore).reversed()).limit(offset).toList());
    }

    public List<TitleWithSimilarityScore> getSimilarMoviesByTitle(String titleName, List<Credit> allCredits, int n) {
        //TODO: Your code here
        return new ArrayList<>(titles.stream().filter(title -> !containsIgnoreCase(title.getTitle(), titleName))
                .map(title -> new TitleWithSimilarityScore(title, comparator.calculateSimilarityScore(titles.stream().filter(oneTitle -> containsIgnoreCase(oneTitle.getTitle(), titleName))
                        .findFirst().orElseThrow(() -> new NoSuchElementException("Title with name: " + titleName + " not found!")), allCredits, title, allCredits))
                ).sorted(Comparator.comparingInt(TitleWithSimilarityScore::getSimilarityScore).reversed()).limit(n).toList());
    }

    public List<Title> getTitles() {
        return this.titles;
    }
}
