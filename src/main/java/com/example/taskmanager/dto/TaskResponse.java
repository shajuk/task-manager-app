package com.example.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.Instant;

@Schema(description = "Response body containing task details")
@Data
public class TaskResponse {
    @Schema(description = "Task ID", example = "1")
    private Long id;
    
    @Schema(description = "Task title", example = "Complete project documentation")
    private String title;
    
    @Schema(description = "Task description", example = "Write comprehensive documentation for the project")
    private String description;
    
    @Schema(description = "Username of the assigned user", example = "john_doe")
    private String assignedTo;
    
    @Schema(description = "Task status", example = "Assigned", allowableValues = {"Assigned", "In Progress", "Completed"})
    private String status;
    
    @Schema(description = "Whether the task is completed", example = "false")
    private boolean completed;
    
    @Schema(description = "Task creation timestamp", example = "2025-12-14T10:30:00Z")
    private Instant createdAt;
    
    @Schema(description = "Task last update timestamp", example = "2025-12-14T15:30:00Z")
    private Instant updatedAt;
}
