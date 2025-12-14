package com.example.taskmanager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Description is required")
    private String description;
    @NotBlank(message = "Assigned To is required")
    private String assignedTo;

    @NotBlank(message = "status is required")
    @Pattern(
            regexp = "^(Assigned|In Progress|Completed)$",
            message = "Status must be one of the following: Assigned, In Progress, Completed"
    )
    private String status;
}
