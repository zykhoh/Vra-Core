package com.fyp.Service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchTermParserService {

    private static final String dateFormat = "dd/MM/yyyy";

    public String[] tokenizeSearchTerm(String searchTerm) {
        return searchTerm.split("\\s+");
    }

    public List<String> removePunctuationMark(List<String> searchTerms) {
        for (int i = 0; i < searchTerms.size(); i++) {
            //remove punctuation mark
            searchTerms.set(i, searchTerms.get(i).replaceAll("[^\\w]", " "));
        }
        return searchTerms;
    }

    public List<LocalDate> trimSearchTerms(List<String> searchTerms) {
        List<LocalDate> dates = new ArrayList<>();

        for (String searchTerm : searchTerms) {
            if (isDate(searchTerm)) {
                dates.add(convertStringToLocalDate(searchTerm));
            }
        }

        return dates;
    }

    public boolean hasDate(String[] searchTerms) {
        for (String searchTerm : searchTerms) {
            if (isDate(searchTerm)) {
                return true;
            }
        }
        return false;
    }

    private boolean isDate(String searchTerm) {

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        formatter.setLenient(false);

        try {
            formatter.parse(searchTerm);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private LocalDate convertStringToLocalDate(String searchTerm) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return LocalDate.parse(searchTerm, formatter);
    }
}
