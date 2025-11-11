package com.example.backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String label;
    private int calories;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
