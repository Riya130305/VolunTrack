package com.voluntrack.api.repository;

import com.voluntrack.api.model.User;
import com.voluntrack.api.model.VolunteerProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VolunteerProfileRepository extends JpaRepository<VolunteerProfile, Long> {
  Optional<VolunteerProfile> findByUser(User user);
}
