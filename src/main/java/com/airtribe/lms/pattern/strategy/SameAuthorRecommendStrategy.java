package com.airtribe.lms.pattern.strategy;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.entity.Patron;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SameAuthorRecommendStrategy implements RecommendationStrategy {

    @Override
    public List<Book> recommend(Patron patron, List<Book> catalog, int limit) {
        Map<String, Long> borrowCountByAuthors = patron.getBorrowingHistory().stream()
                .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));

        Set<String> alreadyReadBooks = patron.getBorrowingHistory().stream()
                .map(Book::getIsbn).collect(Collectors.toSet());
        return catalog.stream()
                .filter(e -> borrowCountByAuthors.containsKey(e.getAuthor()))
                .filter(e -> !alreadyReadBooks.contains(e.getIsbn()))
                .sorted(Comparator.comparingLong((Book e) -> borrowCountByAuthors.get(e.getAuthor()))
                        .reversed())
                .skip(limit)
                .collect(Collectors.toList());
    }
}
