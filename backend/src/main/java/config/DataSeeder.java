package com.voluntrack.api.config;

import com.voluntrack.api.model.*;
import com.voluntrack.api.repository.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
  @Bean
  CommandLineRunner seed(UserRepository users, VolunteerProfileRepository profiles, EventRepository events,
                         TaskRepository tasks, PasswordEncoder encoder) {
    return args -> {
      if (users.count() > 0) return;

      User admin = user("Admin", "admin@voluntrack.dev", "password", Role.ADMIN, encoder);
      User volunteer = user("Asha Volunteer", "volunteer@voluntrack.dev", "password", Role.VOLUNTEER, encoder);
      User organizer = user("NGO Organizer", "organizer@voluntrack.dev", "password", Role.ORGANIZER, encoder);
      users.save(admin);
      users.save(volunteer);
      users.save(organizer);

      VolunteerProfile profile = new VolunteerProfile();
      profile.setUser(volunteer);
      profile.setSkills(Set.of("first aid", "crowd management", "translation"));
      profile.setAvailability(Set.of("Saturday morning", "Sunday afternoon"));
      profile.setCity("Delhi");
      profile.setPhone("9999999999");
      profiles.save(profile);

      Event event = new Event();
      event.setTitle("Community Health Camp");
      event.setDescription("Free health checkup and medicine distribution drive.");
      event.setLocationName("Central Community Hall");
      event.setLatitude(28.6139);
      event.setLongitude(77.2090);
      event.setEventDate(LocalDate.now().plusDays(2));
      event.setOrganizer(organizer);
      events.save(event);

      Task task = new Task();
      task.setEvent(event);
      task.setTitle("First Aid Desk");
      task.setDescription("Assist visitors and route urgent cases to doctors.");
      task.setStartsAt(LocalDateTime.now().plusDays(2).withHour(9).withMinute(0));
      task.setEndsAt(LocalDateTime.now().plusDays(2).withHour(13).withMinute(0));
      task.setEstimatedHours(4);
      task.setCapacity(2);
      task.setRequiredSkills(Set.of("first aid", "crowd management"));
      task.setQrCode("DEMO-QR-123");
      tasks.save(task);
    };
  }

  private User user(String name, String email, String password, Role role, PasswordEncoder encoder) {
    User user = new User();
    user.setName(name);
    user.setEmail(email);
    user.setPasswordHash(encoder.encode(password));
    user.setRole(role);
    return user;
  }
}
