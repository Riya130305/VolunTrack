package com.voluntrack.api.repository;

import com.voluntrack.api.model.Attendance;
import com.voluntrack.api.model.Task;
import com.voluntrack.api.model.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
  Optional<Attendance> findFirstByTaskAndVolunteerOrderByIdDesc(Task task, User volunteer);
  List<Attendance> findByVolunteer(User volunteer);
}
