package com.project.www.news_aggregator_api.dto;

import lombok.Data;

@Data
public class NewsApiArticleDto {
	 private String author;
	    private String title;
	    private String description;
	    private String url;
	    private String urlToImage; // <-- Field from NewsAPI
}
