package com.codecool.netflix.logic;

import com.codecool.netflix.data.Credit;
import com.codecool.netflix.data.Title;
import com.codecool.netflix.logic.reader.CreditReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

public class CreditManager implements CsvItemCollection {
    private final CreditReader reader;
    private List<Credit> credits;

    public CreditManager(CreditReader reader) {
        this.reader = reader;
    }

    @Override
    public void init() throws IOException {
        credits = reader.readAll("/credits.csv");
    }

    public List<Title> getAllTitlesByCreditName(String name, List<Title> titles, int n) {
        //TODO: Your code here
        return new ArrayList<>(titles.stream().filter(title -> credits.stream()
                .filter(credit -> containsIgnoreCase(credit.getName(), name)).map(Credit::getId)
                .anyMatch(id -> id.equals(title.getId()))).limit(n).toList());
    }

    public List<Credit> getCredits() {
        return this.credits;
    }
}