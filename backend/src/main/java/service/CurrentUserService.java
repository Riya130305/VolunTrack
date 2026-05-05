package com.voluntrack.api.service;

import com.voluntrack.api.model.User;
import com.voluntrack.api.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
  private final UserRepository users;

  public CurrentUserService(UserRepository users) {
    this.users = users;
  }

  public User get() {
    String email = SecurityContextHolder.getContext().getAuthentication().getName();
    return users.findByEmail(email).orElseThrow();
  }
}
