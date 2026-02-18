package com.example.news_aggregator.sevice;

import com.example.news_aggregator.Dao.NewsApiResponse;
import com.example.news_aggregator.Dao.NewsArticleDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsApiClient {

    private final WebClient newsApiClient;
    private final WebClient gNewsClient;
    // ... other news API clients

    @Value("${newsapi.org.api-key}")
    private String newsApiKey;

    @Value("${gnews.io.api-key}")
    private String gNewsApiKey;

    // Add others as needed

    public NewsApiClient(WebClient.Builder webClientBuilder) {
        this.newsApiClient = webClientBuilder.baseUrl("https://newsapi.org/v2/").build();
        this.gNewsClient = webClientBuilder.baseUrl("https://gnews.io/api/v4/").build();
        // ... initialize other clients
    }

    public Mono<List<NewsArticleDto>> fetchNewsFromNewsApiOrg(String query) {
        return newsApiClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/everything")
                        .queryParam("q", query)
                        .queryParam("sortBy", "relevancy")
                        .queryParam("language", "en")
                        .queryParam("apiKey", newsApiKey)
                        .build())
                .retrieve()
                .bodyToMono(NewsApiResponse.class) // Create this DTO based on NewsAPI.org response
                .map(response -> response.getArticles().stream()
                        .map(article -> new NewsArticleDto(
                                article.getTitle(),
                                article.getDescription(),
                                article.getUrl(),
                                article.getSource().getName(),
                                article.getPublishedAt()))
                        .collect(Collectors.toList()))
                .onErrorResume(e -> {
                    System.err.println("Error fetching from NewsAPI.org: " + e.getMessage());
                    return Mono.just(List.of()); // Return empty list on error
                });
    }

    public Mono<List<NewsArticleDto>> fetchNewsFromGNews(String query) {
        return gNewsClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("lang", "en")
                        .queryParam("apikey", gNewsApiKey)
                        .build())
                .retrieve()
                .bodyToMono(GNewsApiResponse.class) // Create this DTO based on GNews.io response
                .map(response -> response.getArticles().stream()
                        .map(article -> new NewsArticleDto(
                                article.getTitle(),
                                article.getDescription(),
                                article.getUrl(),
                                article.getSource().getName(),
                                article.getPublishedAt()))
                        .collect(Collectors.toList()))
                .onErrorResume(e -> {
                    System.err.println("Error fetching from GNews.io: " + e.getMessage());
                    return Mono.just(List.of()); // Return empty list on error
                });
    }

}
