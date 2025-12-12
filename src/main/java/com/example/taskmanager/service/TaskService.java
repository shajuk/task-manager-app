package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.entity.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task create(TaskRequest request);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    Task update(Long id, TaskRequest request);
    void delete(Long id);
}
