CREATE TABLE task_queue
(
    id             BIGSERIAL PRIMARY KEY,
    topic          VARCHAR(255),
    payload        jsonb,
    created_at     TIMESTAMP,
    created_by     VARCHAR(255),
    processed_at   TIMESTAMP,
    processed_by   VARCHAR(255)
);