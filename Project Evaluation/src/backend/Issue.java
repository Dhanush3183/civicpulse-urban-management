package com.civicpulse.issue;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "issues")
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    private String description;
    private String category; // e.g., Pothole, Streetlight, Garbage
    private String status;   // e.g., REPORTED, IN_PROGRESS, RESOLVED
    private String location; 
    
    private LocalDateTime reportedAt;

    @PrePersist
    protected void onCreate() {
        this.reportedAt = LocalDateTime.now();
        if(this.status == null) {
            this.status = "REPORTED";
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDateTime getReportedAt() { return reportedAt; }
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
}