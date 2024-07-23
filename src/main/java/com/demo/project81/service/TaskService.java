package com.demo.project81.service;

import java.net.InetAddress;
import java.time.LocalDateTime;

import com.demo.project81.domain.Task;
import com.demo.project81.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
    public Task findByIdWithLock(Long id) {
        return taskRepository.findByIdWithLock(id);
    }

    @Transactional
    public Task queueTask(Task task) {
        task.setCreatedAt(LocalDateTime.now());
        task.setCreatedBy(getHostName() + "_" + Thread.currentThread().getName());
        task = taskRepository.save(task);
        notifier.notifyTaskCreated(task);
        return task;
    }

    @SneakyThrows
    public String getHostName() {
        return InetAddress.getLocalHost().getHostName();
    }

    @Transactional
    public Task save(Task task) {
        return taskRepository.save(task);
    }
}
