package com.project.www.news_aggregator_api;

import com.project.www.news_aggregator_api.controller.NewsController;
import com.project.www.news_aggregator_api.dto.ArticleDto;
import com.project.www.news_aggregator_api.service.NewsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Use @WebMvcTest to only test the web layer (the controller)
@WebMvcTest(NewsController.class)
public class NewsControllerTest {

	
	// MockMvc is a tool from Spring to simulate HTTP requests to our controller
    @Autowired
    private MockMvc mockMvc;

    // We create a "mock" (fake) version of NewsService.
    // This stops the test from calling the real news APIs on the internet.
    @MockBean
    private NewsService newsService;

    @Test
    void getTopHeadlines_ShouldReturn_200_OK_And_ListOfArticles() throws Exception {
        // --- ARRANGE ---
        // 1. Create a fake article and a list.
        ArticleDto article1 = new ArticleDto();
        article1.setTitle("Test Title 1");
        List<ArticleDto> fakeArticleList = List.of(article1);

        // 2. Program the mock service: "When getAggregatedNews is called with these
        // specific parameters, then return our fake list."
        Mockito.when(newsService.getAggregatedNews("in", "tech")).thenReturn(fakeArticleList);

        // --- ACT & ASSERT ---
        // 3. Perform a fake GET request to our API endpoint.
        mockMvc.perform(get("/api/v1/news/top-headlines?country=in&topic=tech"))
                // 4. Assert (check) that the HTTP response is correct.
                .andExpect(status().isOk()) // It should be 200 OK
                .andExpect(jsonPath("$", hasSize(1))) // The JSON response should be a list with 1 item
                .andExpect(jsonPath("$[0].title").value("Test Title 1")); // The title of the first item should be correct
    }
}
