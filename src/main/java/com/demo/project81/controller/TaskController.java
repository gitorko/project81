package com.demo.project81.controller;

import java.time.LocalDateTime;

import com.demo.project81.domain.Task;
import com.demo.project81.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {

    final TaskService taskService;

    @GetMapping("/task/queue")
    public Task queueMessage() {
        Task task = Task.builder()
                .topic("group1")
                .payload("PAYLOAD_" + LocalDateTime.now())
                .build();
        Task savedTask = taskService.queueTask(task);
        return savedTask;
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }
}
