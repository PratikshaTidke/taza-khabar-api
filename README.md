# Taza Khabar - Backend API 📰

This is the backend REST API for Taza Khabar, a full-stack news aggregator application. This server is built with Java and the Spring Boot framework. It is responsible for fetching, aggregating, caching, and de-duplicating news articles from multiple external APIs.

---

## ✨ Features

-   **Multi-API Aggregation:** Fetches and combines news from GNews, MediaStack, and NewsAPI.
-   **Resilient by Design:** Gracefully handles the failure of any external API, ensuring the application remains functional.
-   **Performance Optimized:** Implements server-side caching with Spring Cache (`@Cacheable`) to reduce latency and save API calls.
-   **Robust Error Handling:** Uses a global exception handler (`@RestControllerAdvice`) to provide clear error messages to the frontend.
-   **Data De-duplication:** Filters the combined results to show only unique articles based on their titles.
-   **Tested:** Includes both Unit (JUnit/Mockito) and Integration (`@SpringBootTest`) tests to ensure code quality and reliability.

---

## 🛠️ Tech Stack

-   **Framework:** Spring Boot
-   **Language:** Java 17
-   **Build Tool:** Maven
-   **Core Dependencies:**
    -   Spring Web
    -   Spring Cache
    -   Lombok
-   **Testing:** JUnit 5, Mockito, Spring Boot Test

---

## ⚙️ API Endpoint

The primary endpoint provided by this API is:

### Get Aggregated News

-   **URL:** `/api/v1/news/top-headlines`
-   **Method:** `GET`
-   **Query Parameters:**
    -   `country` (String, required): The 2-letter ISO code for the country (e.g., `in`, `us`).
    -   `topic` (String, required): The search keyword or phrase (e.g., `technology`).

---

## 🚀 How to Run Locally

1.  **Clone the repository:**
    ```bash
    git clone [your-repo-url]
    ```

2.  **Configure API Keys:**
    -   Navigate to `src/main/resources/`.
    -   Create an `application.properties` file.
    -   Add your API keys to the file:
        ```properties
        server.port=8081
        gnews.api.key=YOUR_GNEWS_KEY
        mediastack.api.key=YOUR_MEDIASTACK_KEY
        news.api.key=YOUR_NEWSAPI_KEY
        ```

3.  **Run the application:**
    -   Open the project in your IDE (like Eclipse or IntelliJ).
    -   Run the `NewsAggregatorApiApplication.java` file.
    -   The server will start on `http://localhost:8081`.