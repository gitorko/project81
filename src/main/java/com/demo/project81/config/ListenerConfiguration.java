package com.demo.project81.config;

import com.demo.project81.service.NotificationHandler;
import com.demo.project81.service.NotifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ListenerConfiguration {

    @Bean
    CommandLineRunner startListener(NotifierService notifier, NotificationHandler handler) {
        return (args) -> {
            log.info("Starting task listener thread...");
            Thread.ofVirtual().name("task-listener").start(notifier.createNotificationHandler(handler));
        };
    }
}