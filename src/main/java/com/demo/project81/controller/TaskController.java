package com.demo.project81.controller;

import com.demo.project81.domain.Task;
import com.demo.project81.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TaskController {

    final TaskService taskService;

    @SneakyThrows
    @GetMapping("/task/queue")
    public Task queueMessage() {
        String payload = """
                {
                    "name": "test",
                    "city": "bangalore" 
                }
                """;
        Task task = Task.builder()
                .topic("group1")
                .payload(payload)
                .build();
        Task savedTask = taskService.queueTask(task);
        return savedTask;
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.findById(id));
    }
}
