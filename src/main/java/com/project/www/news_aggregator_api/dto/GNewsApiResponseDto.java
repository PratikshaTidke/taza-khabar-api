package com.project.www.news_aggregator_api.dto;



import lombok.Data;
import java.util.List;

@Data
public class GNewsApiResponseDto {
	
	  private int totalArticles;
	    private List<GNewsArticleDto> articles;

}
