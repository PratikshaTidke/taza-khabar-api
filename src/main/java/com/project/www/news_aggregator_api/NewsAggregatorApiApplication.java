package com.project.www.news_aggregator_api;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class NewsAggregatorApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsAggregatorApiApplication.class, args);
	}

}
