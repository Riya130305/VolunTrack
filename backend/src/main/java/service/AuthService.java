package com.voluntrack.api.service;

import com.voluntrack.api.dto.*;
import com.voluntrack.api.model.*;
import com.voluntrack.api.repository.UserRepository;
import com.voluntrack.api.repository.VolunteerProfileRepository;
import com.voluntrack.api.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  private final UserRepository users;
  private final VolunteerProfileRepository profiles;
  private final PasswordEncoder encoder;
  private final JwtService jwt;

  public AuthService(UserRepository users, VolunteerProfileRepository profiles, PasswordEncoder encoder, JwtService jwt) {
    this.users = users;
    this.profiles = profiles;
    this.encoder = encoder;
    this.jwt = jwt;
  }

  public AuthResponse register(RegisterRequest request) {
    if (users.existsByEmail(request.email())) {
      throw new IllegalArgumentException("Email already registered");
    }
    User user = new User();
    user.setName(request.name());
    user.setEmail(request.email());
    user.setPasswordHash(encoder.encode(request.password()));
    user.setRole(request.role() == null ? Role.VOLUNTEER : request.role());
    users.save(user);

    if (user.getRole() == Role.VOLUNTEER) {
      VolunteerProfile profile = new VolunteerProfile();
      profile.setUser(user);
      if (request.skills() != null) profile.setSkills(request.skills());
      if (request.availability() != null) profile.setAvailability(request.availability());
      profile.setPhone(request.phone());
      profile.setCity(request.city());
      profiles.save(profile);
    }
    return toResponse(user);
  }

  public AuthResponse login(AuthRequest request) {
    User user = users.findByEmail(request.email()).orElseThrow();
    if (!encoder.matches(request.password(), user.getPasswordHash())) {
      throw new IllegalArgumentException("Invalid credentials");
    }
    return toResponse(user);
  }

  private AuthResponse toResponse(User user) {
    return new AuthResponse(jwt.generate(user), user.getId(), user.getName(), user.getEmail(), user.getRole());
  }
}
