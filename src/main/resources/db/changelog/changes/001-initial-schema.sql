CREATE TABLE task_queue
(
    id           BIGSERIAL PRIMARY KEY,
    topic        VARCHAR(255),
    payload      TEXT,
    created_at   TIMESTAMP
);