package com.example.news_aggregator.controller;

import com.example.news_aggregator.Dao.NewsPreferenceRequest;
import com.example.news_aggregator.entity.User;
import com.example.news_aggregator.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class NewsPreferenceController {

    @Autowired
    private UserRepository userRepository;

    private User getCurrentAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found in database."));
    }

    @GetMapping("/preferences")
    public ResponseEntity<Set<String>> getUserPreferences() {
        User currentUser = getCurrentAuthenticatedUser();
        return ResponseEntity.ok(currentUser.getNewsPreferences());
    }

    @PutMapping("/preferences")
    public ResponseEntity<?> updateUserPreferences(@Valid @RequestBody NewsPreferenceRequest preferencesRequest) {
        User currentUser = getCurrentAuthenticatedUser();
        currentUser.setNewsPreferences(preferencesRequest.getPreferences());
        userRepository.save(currentUser);
        return ResponseEntity.ok(Map.of("message", "Preferences updated successfully"));
    }
}
