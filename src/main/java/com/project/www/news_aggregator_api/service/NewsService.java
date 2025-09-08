package com.project.www.news_aggregator_api.service;

import com.project.www.news_aggregator_api.dto.*;
import com.project.www.news_aggregator_api.exception.NewsApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NewsService {

    @Value("${news.api.key}")
    private String newsApiKey;

    @Value("${gnews.api.key}")
    private String gnewsApiKey;

    @Value("${mediastack.api.key}")
    private String mediaStackApiKey;

    private final String newsApiUrl = "https://newsapi.org/v2/top-headlines";
    private final String gnewsSearchUrl = "https://gnews.io/api/v4/search";
    private final String mediaStackApiUrl = "http://api.mediastack.com/v1/news";

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "newsCache", key = "#country + '-' + #topic")
    public List<ArticleDto> getAggregatedNews(String country, String topic) {
        System.out.println("Fetching news. The app will now handle API failures gracefully.");

        List<ArticleDto> allArticles = new ArrayList<>();

        // Try to fetch from GNews
        try {
            allArticles.addAll(fetchFromGNews(country, topic));
        } catch (Exception e) {
            System.err.println("GNews API failed to respond. Continuing without it.");
        }

        // Try to fetch from MediaStack
        try {
            allArticles.addAll(fetchFromMediaStack(country, topic));
        } catch (Exception e) {
            System.err.println("MediaStack API failed to respond. Continuing without it.");
        }
        
        // Try to fetch from NewsAPI
        try {
            allArticles.addAll(fetchFromNewsApi(country, topic));
        } catch (Exception e) {
            System.err.println("NewsAPI failed to respond. Continuing without it.");
        }

        // Remove duplicates from the combined list of all successful API calls
        return allArticles.stream()
                .filter(distinctByTitle())
                .collect(Collectors.toList());
    }

    private List<ArticleDto> fetchFromMediaStack(String country, String topic) {
        try {
            StringBuilder urlBuilder = new StringBuilder(mediaStackApiUrl);
            urlBuilder.append("?access_key=").append(mediaStackApiKey);
            urlBuilder.append("&countries=").append(country);
            
            if (topic != null && !topic.isEmpty()) {
                urlBuilder.append("&keywords=").append(topic);
            }
            urlBuilder.append("&limit=50");

            MediaStackResponseDto response = restTemplate.getForObject(urlBuilder.toString(), MediaStackResponseDto.class);

            return Objects.requireNonNull(response).getData().stream()
                .filter(mediaStackArticle -> mediaStackArticle.getTitle() != null && !mediaStackArticle.getTitle().isEmpty())
                .map(mediaStackArticle -> {
                    ArticleDto article = new ArticleDto();
                    article.setTitle(mediaStackArticle.getTitle());
                    article.setDescription(mediaStackArticle.getDescription());
                    article.setUrl(mediaStackArticle.getUrl());
                    article.setAuthor(mediaStackArticle.getSource());
                    article.setImageUrl(mediaStackArticle.getImage());
                    return article;
                }).collect(Collectors.toList());

        } catch (HttpClientErrorException e) {
            System.err.println("--- MEDIASTACK ERROR ---");
            System.err.println("Status Code: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            System.err.println("------------------------");
            throw new NewsApiException("Could not fetch from MediaStack. Check backend console.", e);
        } catch (Exception e) {
            System.err.println("General error fetching from MediaStack: " + e.getMessage());
            throw new NewsApiException("Could not fetch articles from MediaStack.", e);
        }
    }

    private List<ArticleDto> fetchFromGNews(String country, String topic) {
        try {
            StringBuilder urlBuilder = new StringBuilder(gnewsSearchUrl);
            
            if (topic != null && !topic.isEmpty()) {
                urlBuilder.append("?q=").append(topic);
                urlBuilder.append("&country=").append(country);
            } else {
                urlBuilder.append("?country=").append(country);
            }
            
            urlBuilder.append("&lang=en&token=").append(gnewsApiKey);

            GNewsApiResponseDto response = restTemplate.getForObject(urlBuilder.toString(), GNewsApiResponseDto.class);

            return Objects.requireNonNull(response).getArticles().stream().map(gnewsArticle -> {
                ArticleDto article = new ArticleDto();
                article.setTitle(gnewsArticle.getTitle());
                article.setDescription(gnewsArticle.getDescription());
                article.setUrl(gnewsArticle.getUrl());
                if (gnewsArticle.getSource() != null) {
                    article.setAuthor(gnewsArticle.getSource().getName());
                }
                article.setImageUrl(gnewsArticle.getImage());
                return article;
            }).collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            System.err.println("--- GNEWS ERROR ---");
            System.err.println("Status Code: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            System.err.println("-------------------");
            throw new NewsApiException("Could not fetch articles from GNews. Check backend console.", e);
        } catch (Exception e) {
            throw new NewsApiException("Could not fetch articles from GNews.", e);
        }
    }
    
    private List<ArticleDto> fetchFromNewsApi(String country, String topic) {
       try {
            StringBuilder urlBuilder = new StringBuilder(newsApiUrl);
            urlBuilder.append("?country=").append(country);
            if (topic != null && !topic.isEmpty()) {
                urlBuilder.append("&q=").append(topic);
            }
            urlBuilder.append("&apiKey=").append(newsApiKey);
            NewsApiResponseDto response = restTemplate.getForObject(urlBuilder.toString(), NewsApiResponseDto.class);
            return Objects.requireNonNull(response).getArticles().stream().map(newsApiArticle -> {
                ArticleDto article = new ArticleDto();
                article.setTitle(newsApiArticle.getTitle());
                article.setDescription(newsApiArticle.getDescription());
                article.setUrl(newsApiArticle.getUrl());
                article.setAuthor(newsApiArticle.getAuthor());
                article.setImageUrl(newsApiArticle.getUrlToImage());
                return article;
            }).collect(Collectors.toList());
        } catch (HttpClientErrorException e) {
            System.err.println("--- NEWSAPI ERROR ---");
            System.err.println("Status Code: " + e.getStatusCode());
            System.err.println("Response Body: " + e.getResponseBodyAsString());
            System.err.println("--------------------");
            throw new NewsApiException("Could not fetch articles from NewsAPI. Check backend console.", e);
        } catch (Exception e) {
            throw new NewsApiException("Could not fetch articles from NewsAPI.", e);
        }
    }

  public static Predicate<ArticleDto> distinctByTitle() {
        Map<String, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(t.getTitle().toLowerCase().replaceAll("\\s+", ""), Boolean.TRUE) == null;
    }
}