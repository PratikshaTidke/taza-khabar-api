package com.project.www.news_aggregator_api.dto;

import lombok.Data;

@Data
public class ArticleDto {
	
	private String title;
    private String description;
    private String url;
    private String author;
    private String imageUrl;
}
