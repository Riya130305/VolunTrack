package com.voluntrack.api.service;

import com.voluntrack.api.model.Task;
import com.voluntrack.api.model.VolunteerProfile;
import java.util.HashSet;
import org.springframework.stereotype.Service;

@Service
public class MatchingService {
  public int score(VolunteerProfile profile, Task task) {
    if (task.getRequiredSkills().isEmpty()) return 100;
    var volunteerSkills = new HashSet<String>();
    profile.getSkills().forEach(skill -> volunteerSkills.add(skill.trim().toLowerCase()));

    long matched = task.getRequiredSkills().stream()
        .map(skill -> skill.trim().toLowerCase())
        .filter(volunteerSkills::contains)
        .count();

    return (int) Math.round((matched * 100.0) / task.getRequiredSkills().size());
  }
}
