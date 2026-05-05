package com.voluntrack.api.repository;

import com.voluntrack.api.model.TaskApplication;
import com.voluntrack.api.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskApplicationRepository extends JpaRepository<TaskApplication, Long> {
  List<TaskApplication> findByVolunteer(User volunteer);
}
