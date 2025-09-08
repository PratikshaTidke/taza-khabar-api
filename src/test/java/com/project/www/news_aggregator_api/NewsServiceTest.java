package com.project.www.news_aggregator_api;


import com.project.www.news_aggregator_api.dto.ArticleDto;
import com.project.www.news_aggregator_api.service.NewsService;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class NewsServiceTest {

	 // The @Test annotation marks this method as a test case
    @Test
    void distinctByTitle_ShouldRemoveDuplicateArticles() {
        // --- ARRANGE ---
        // 1. Create test data: a list with 5 articles, where 2 are duplicates.
        ArticleDto article1 = new ArticleDto();
        article1.setTitle("Unique Article A");

        ArticleDto article2 = new ArticleDto();
        article2.setTitle("Unique Article B");
        
        ArticleDto article3 = new ArticleDto();
        article3.setTitle("Unique Article C");

        ArticleDto duplicateOfA = new ArticleDto();
        duplicateOfA.setTitle("Unique Article A"); // Exact duplicate

        ArticleDto duplicateWithDifferentCase = new ArticleDto();
        duplicateWithDifferentCase.setTitle("unique article b"); // Duplicate with different case and spacing

        List<ArticleDto> articlesWithDuplicates = List.of(
            article1, article2, article3, duplicateOfA, duplicateWithDifferentCase
        );

        // --- ACT ---
        // 2. Call the logic we want to test by filtering the list.
        List<ArticleDto> uniqueArticles = articlesWithDuplicates.stream()
                .filter(NewsService.distinctByTitle())
                .collect(Collectors.toList());

        // --- ASSERT ---
        // 3. Check if the result is what we expect.
        // We expect the final list to have only 3 unique articles.
        assertEquals(3, uniqueArticles.size());
    }
}
