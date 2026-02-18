package com.example.news_aggregator.Dao;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class NewsPreferenceRequest {
    @NotEmpty(message = "News preferences cannot be empty")
    private Set<String> preferences;
}
