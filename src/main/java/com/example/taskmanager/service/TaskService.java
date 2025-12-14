package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskRequest;
import com.example.taskmanager.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Task create(TaskRequest request);
    Optional<Task> findById(Long id);
    Page<Task> findAll(PageRequest pageRequest);
    Task update(Long id, TaskRequest request);
    void delete(Long id);
}
