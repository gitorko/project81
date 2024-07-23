package com.demo.project81.service;

import java.time.LocalDateTime;

import com.demo.project81.domain.Task;
import com.demo.project81.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    final TaskRepository taskRepository;
    final NotifierService notifier;

    @Transactional(readOnly = true)
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Transactional
    public Task queueTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        task = taskRepository.save(task);
        notifier.notifyTaskCreated(task);
        return task;
    }

}
