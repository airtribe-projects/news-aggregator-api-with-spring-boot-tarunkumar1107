package com.example.news_aggregator.controller;

import com.example.news_aggregator.Dao.NewsArticleDto;
import com.example.news_aggregator.entity.User;
import com.example.news_aggregator.repository.UserRepository;
import com.example.news_aggregator.sevice.NewsApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NewsController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NewsApiClient newsApiClient; // Your custom service for external calls

    private User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database."));
    }

    @GetMapping("/news")
    public Mono<ResponseEntity<List<NewsArticleDto>>> getNews() {
        User currentUser = getCurrentAuthenticatedUser();
        Set<String> preferences = currentUser.getNewsPreferences();

        if (preferences.isEmpty()) {
            return Mono.just(ResponseEntity.ok(List.of())); // No preferences, no news
        }

        // Fetch news from multiple sources concurrently
        List<Mono<List<NewsArticleDto>>> newsFetchMonos = preferences.stream()
                .map(preference -> Flux.merge(
                        newsApiClient.fetchNewsFromNewsApiOrg(preference),
                        newsApiClient.fetchNewsFromGNews(preference)
                        // Add calls to other news APIs here
                ).flatMapIterable(list -> list).collectList())
                .collect(Collectors.toList());

        return Mono.zip(newsFetchMonos, results -> {
            List<NewsArticleDto> allNews = Flux.fromArray(results)
                    .cast(List.class) // Cast each result to List
                    .flatMapIterable(list -> list) // Flatten lists
                    .distinct() // Remove duplicate articles if any from different sources
                    .collectList()
                    .block(); // Block until all news is collected (careful with block() in reactive apps)
            return ResponseEntity.ok(allNews);
        });
    }
}
