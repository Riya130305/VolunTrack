package com.voluntrack.api.repository;

import com.voluntrack.api.model.Task;
import com.voluntrack.api.model.TaskStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByStatus(TaskStatus status);
}
