package com.airtribe.lms.pattern.strategy;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.entity.Patron;

import java.util.List;

public interface RecommendationStrategy {

    List<Book> recommend(Patron patron, List<Book> catalog, int limit);
}
