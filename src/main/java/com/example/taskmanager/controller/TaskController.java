package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.dto.TaskResponse;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.exception.TaskNotFoundException;
import com.example.taskmanager.service.TaskService;
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
@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        Task task = taskService.create(request);
        TaskResponse response=mapEntityToDTO(task);
        response.setAssignedTo(task.getAssignedTo().getUsername());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> list(@RequestParam(name="page", defaultValue = "0") int page,
                                                   @RequestParam(name="size", defaultValue = "10") int size,
                                                   @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                                                   @RequestParam(name = "direction", defaultValue = "ASC") String direction) {

        PageRequest pageRequest = PageRequest.of(page, size,
                "DESC".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());

        List<TaskResponse> taskList = taskService.findAll(pageRequest)
                .getContent() // Extract the content from the Page object
                .stream()
                .map(this::mapEntityToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(taskList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> get(@PathVariable("id") Long id) {
        Task task = taskService.findById(id).orElseThrow(() -> {
            log.error("Task not found with id: {}", id);
            return new TaskNotFoundException(String.format("Task not found with id: %d", id));
        });
        return ResponseEntity.ok(mapEntityToDTO(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(@PathVariable("id") Long id, @Valid @RequestBody TaskRequest request) {
        Task task = taskService.update(id, request);
        TaskResponse response=mapEntityToDTO(task);
        response.setAssignedTo(task.getAssignedTo().getUsername());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private TaskResponse mapEntityToDTO(Task task) {
        return modelMapper.map(task, TaskResponse.class);
    }
}
