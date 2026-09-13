package com.airtribe.lms.service;

import com.airtribe.lms.entity.Book;
import com.airtribe.lms.entity.Patron;
import com.airtribe.lms.pattern.strategy.RecommendationStrategy;

import java.util.List;

public class RecommendationService {

    private RecommendationStrategy strategy;


    public RecommendationService(RecommendationStrategy recommendationStrategy) {
        this.strategy = recommendationStrategy;
    }

    public void setStrategy(RecommendationStrategy recommendationStrategy) {
        this.strategy = recommendationStrategy;
    }

    public List<Book> recommendFor(Patron patron, List<Book> catalog, int limit) {
        return strategy.recommend(patron, catalog, limit);
    }

}
