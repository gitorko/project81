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

    final NotificationHandler handler;
    final NotifierService notifier;

    @Bean
    CommandLineRunner startListener() {
        return (args) -> {
            log.info("Starting task listener thread...");
            Runnable listener = notifier.createNotificationHandler(handler);
            Thread.ofVirtual().start(listener);
        };
    }
}

