package com.demo.project81.service;

import java.sql.Connection;
import java.util.function.Consumer;

import com.demo.project81.domain.Task;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.PGConnection;
import org.postgresql.PGNotification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotifierService {

    static final String TASK_CHANNEL = "tasks";
    final JdbcTemplate jdbcTemplate;

    @Transactional
    public void notifyTaskCreated(Task task) {
        log.info("Notifying task channel!");
        jdbcTemplate.execute("NOTIFY " + TASK_CHANNEL + ", '" + task.getId() + "'");
    }

    public Runnable createNotificationHandler(Consumer<PGNotification> consumer) {
        return () -> {
            jdbcTemplate.execute((Connection connection) -> {
                log.info("notificationHandler: sending LISTEN command...");
                connection.createStatement().execute("LISTEN " + TASK_CHANNEL);

                PGConnection pgConnection = connection.unwrap(PGConnection.class);

                while (!Thread.currentThread().isInterrupted()) {
                    PGNotification[] notifications = pgConnection.getNotifications(10000);
                    if (notifications == null || notifications.length == 0) {
                        continue;
                    }
                    for (PGNotification nt : notifications) {
                        consumer.accept(nt);
                    }
                }
                return 0;
            });

        };
    }
}
