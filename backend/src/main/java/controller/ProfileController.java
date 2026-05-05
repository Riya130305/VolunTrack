package com.voluntrack.api.controller;

import com.voluntrack.api.dto.ProfileRequest;
import com.voluntrack.api.model.VolunteerProfile;
import com.voluntrack.api.repository.BadgeRepository;
import com.voluntrack.api.repository.VolunteerProfileRepository;
import com.voluntrack.api.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
  private final CurrentUserService currentUser;
  private final VolunteerProfileRepository profiles;
  private final BadgeRepository badges;

  public ProfileController(CurrentUserService currentUser, VolunteerProfileRepository profiles, BadgeRepository badges) {
    this.currentUser = currentUser;
    this.profiles = profiles;
    this.badges = badges;
  }

  @GetMapping("/me")
  public Object me() {
    var user = currentUser.get();
    var profile = profiles.findByUser(user).orElse(null);
    return java.util.Map.of("user", user, "profile", profile, "badges", badges.findByVolunteer(user));
  }

  @PutMapping("/me")
  public VolunteerProfile update(@RequestBody ProfileRequest request) {
    var user = currentUser.get();
    VolunteerProfile profile = profiles.findByUser(user).orElseGet(() -> {
      VolunteerProfile created = new VolunteerProfile();
      created.setUser(user);
      return created;
    });
    profile.setSkills(request.skills());
    profile.setAvailability(request.availability());
    profile.setPhone(request.phone());
    profile.setCity(request.city());
    return profiles.save(profile);
  }
}
