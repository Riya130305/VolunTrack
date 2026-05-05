package com.voluntrack.api.repository;

import com.voluntrack.api.model.Badge;
import com.voluntrack.api.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
  boolean existsByVolunteerAndName(User volunteer, String name);
  List<Badge> findByVolunteer(User volunteer);
}
