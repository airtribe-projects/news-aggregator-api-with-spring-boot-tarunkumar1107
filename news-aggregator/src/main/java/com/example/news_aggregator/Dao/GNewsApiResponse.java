package com.example.news_aggregator.Dao;

import lombok.Data;

import java.util.List;

@Data
public class GNewsApiResponse {

    private Integer totalArticles;
    private List<Article> articles;

    @Data
    public static class Article {
        private String title;
        private String description;
        private String content;
        private String url;
        private String image;
        private String publishedAt;
        private Source source;
    }

    @Data
    public static class Source {
        private String name;
        private String url;
    }
}
