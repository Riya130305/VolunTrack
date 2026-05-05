package com.voluntrack.api.service;

import com.voluntrack.api.model.Badge;
import com.voluntrack.api.model.User;
import com.voluntrack.api.repository.BadgeRepository;
import com.voluntrack.api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class GamificationService {
  private final BadgeRepository badges;
  private final UserRepository users;

  public GamificationService(BadgeRepository badges, UserRepository users) {
    this.badges = badges;
    this.users = users;
  }

  public void addCompletionRewards(User volunteer, int hours) {
    volunteer.setTotalHours(volunteer.getTotalHours() + hours);
    volunteer.setPoints(volunteer.getPoints() + 50 + (hours * 10));
    users.save(volunteer);

    unlock(volunteer, "First Shift", "Completed the first volunteer shift", volunteer.getTotalHours() >= 1);
    unlock(volunteer, "Impact Maker", "Completed 10 volunteer hours", volunteer.getTotalHours() >= 10);
    unlock(volunteer, "Community Hero", "Earned 500 volunteer points", volunteer.getPoints() >= 500);
  }

  private void unlock(User volunteer, String name, String description, boolean condition) {
    if (condition && !badges.existsByVolunteerAndName(volunteer, name)) {
      Badge badge = new Badge();
      badge.setVolunteer(volunteer);
      badge.setName(name);
      badge.setDescription(description);
      badges.save(badge);
    }
  }
}
