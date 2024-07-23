package com.demo.project81.service;

import java.time.LocalDateTime;
import java.util.function.Consumer;

import com.demo.project81.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGNotification;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationHandler implements Consumer<PGNotification> {

    final TaskService taskService;

    @Override
    public void accept(PGNotification t) {
        log.info("Notification received: pid={}, name={}, param={}", t.getPID(), t.getName(), t.getParameter());
        Task task = taskService.findByIdWithLock(Long.valueOf(t.getParameter()));
        task.setProcessedAt(LocalDateTime.now());
        task.setProcessedBy(taskService.getHostName() + "_" + Thread.currentThread().getName());
        taskService.save(task);
        log.info("Processed Task: {}", task);
    }

}
