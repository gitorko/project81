package com.demo.project81.repository;

import com.demo.project81.domain.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query(value = """
                    SELECT * FROM task_queue WHERE processed_at is null and id = :id FOR UPDATE SKIP LOCKED;
            """, nativeQuery = true)
    Task findByIdWithLock(@Param("id") Long id);
}
