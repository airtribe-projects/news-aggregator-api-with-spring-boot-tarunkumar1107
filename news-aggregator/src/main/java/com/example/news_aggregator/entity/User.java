package com.example.news_aggregator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.HashSet;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users") // Renamed to avoid conflicts with reserved keywords
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Stored as hashed password

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_preferences", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "preference")
    private Set<String> newsPreferences = new HashSet<>();

}