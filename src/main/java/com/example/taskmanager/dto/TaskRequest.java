package com.example.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Schema(description = "Request body for task creation/update")
@Data
public class TaskRequest {
    @Schema(description = "Task title", example = "Complete project documentation", required = true)
    @NotBlank(message = "Title is required")
    private String title;
    
    @Schema(description = "Task description", example = "Write comprehensive documentation for the project", required = true)
    @NotBlank(message = "Description is required")
    private String description;
    
    @Schema(description = "Username of the user to whom the task is assigned", example = "john_doe", required = true)
    @NotBlank(message = "Assigned To is required")
    private String assignedTo;

    @Schema(description = "Task status (Assigned, In Progress, or Completed)", 
            example = "Assigned", required = true, allowableValues = {"Assigned", "In Progress", "Completed"})
    @NotBlank(message = "status is required")
    @Pattern(
            regexp = "^(Assigned|In Progress|Completed)$",
            message = "Status must be one of the following: Assigned, In Progress, Completed"
    )
    private String status;
}
