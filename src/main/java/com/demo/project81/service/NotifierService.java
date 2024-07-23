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

    private static final String TASK_CHANNEL = "tasks";
    private final JdbcTemplate tpl;

    @Transactional
    public void notifyTaskCreated(Task task) {
        tpl.execute("NOTIFY " + TASK_CHANNEL + ", '" + task.getId() + "'");
    }

    public Runnable createNotificationHandler(Consumer<PGNotification> consumer) {
        return () -> {
            tpl.execute((Connection c) -> {
                log.info("notificationHandler: sending LISTEN command...");
                c.createStatement().execute("LISTEN " + TASK_CHANNEL);

                PGConnection pgconn = c.unwrap(PGConnection.class);

                while (!Thread.currentThread().isInterrupted()) {
                    PGNotification[] nts = pgconn.getNotifications(10000);
                    if (nts == null || nts.length == 0) {
                        continue;
                    }
                    for (PGNotification nt : nts) {
                        consumer.accept(nt);
                    }
                }
                return 0;
            });

        };
    }
}
