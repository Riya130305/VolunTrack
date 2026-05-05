package com.voluntrack.api.dto;

import com.voluntrack.api.model.Role;
import java.util.Set;

public record RegisterRequest(
    String name,
    String email,
    String password,
    Role role,
    Set<String> skills,
    Set<String> availability,
    String phone,
    String city
) {}
