package com.project.www.news_aggregator_api.dto;

import lombok.Data;
import java.util.List;

@Data
public class NewsApiResponseDto {

	 private String status;
	    private int totalResults;
	    private List<NewsApiArticleDto> articles;
	}

