package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Task Management", description = "Endpoints for managing tasks")
@SecurityRequirement(name = "bearer-jwt")
@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Create a new task", description = "Creates a new task with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Task created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        Task task = taskService.create(request);
        TaskResponse response=mapEntityToDTO(task);
        response.setAssignedTo(task.getAssignedTo().getUsername());
        return ResponseEntity.status(201).body(response);
    }

    @Operation(summary = "List all tasks", description = "Retrieves a paginated list of all tasks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<List<TaskResponse>> list(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(name="page", defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(name="size", defaultValue = "10") int size,
            @Parameter(description = "Field to sort by") @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (ASC or DESC)") @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size,
                "DESC".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        List<TaskResponse> taskList = taskService.findAll(pageRequest)
                .getContent() // Extract the content from the Page object
                .stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskList);
    }

    @Operation(summary = "Get a task by ID", description = "Retrieves a specific task by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> get(@Parameter(description = "Task ID") @PathVariable("id") Long id) {
        Task task = taskService.findById(id).orElseThrow(() -> {
            log.error("Task not found with id: {}", id);
            return new TaskNotFoundException(String.format("Task not found with id: %d", id));
        });
        return ResponseEntity.ok(mapEntityToDTO(task));
    }

    @Operation(summary = "Update a task", description = "Updates an existing task by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Task updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TaskResponse.class))),
        @ApiResponse(responseCode = "404", description = "Task not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@Parameter(description = "Task ID") @PathVariable("id") Long id, @Valid @RequestBody TaskRequest request) {
        Task task = taskService.update(id, request);
        TaskResponse response=mapEntityToDTO(task);
        response.setAssignedTo(task.getAssignedTo().getUsername());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a task", description = "Deletes a task by its ID (Admin only)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Task deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Task not found"),
        @ApiResponse(responseCode = "403", description = "Access forbidden - Admin role required")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@Parameter(description = "Task ID") @PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponse mapEntityToDTO(Task task) {
        return modelMapper.map(task, TaskResponse.class);
    }
}
