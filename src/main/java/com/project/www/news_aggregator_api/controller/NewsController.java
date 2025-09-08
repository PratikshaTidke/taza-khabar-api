package com.project.www.news_aggregator_api.controller;

import com.project.www.news_aggregator_api.dto.ArticleDto;
import com.project.www.news_aggregator_api.service.NewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
@RestController
@RequestMapping("/api/v1/news")
@CrossOrigin(origins = "http://localhost:3000") 
public class NewsController {

    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping("/top-headlines")
    public ResponseEntity<List<ArticleDto>> getTopHeadlines(
            @RequestParam(defaultValue = "us") String country,
            @RequestParam(required = false) String topic
           
    ) {
        List<ArticleDto> response = newsService.getAggregatedNews(country, topic);
        return ResponseEntity.ok(response);
    }
}