package com.example.taskmanager.dto;

import lombok.Data;
import java.time.Instant;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private String assignedTo;
    private String status;
    private boolean completed;
    private Instant createdAt;
    private Instant updatedAt;
}
