package com.voluntrack.api.dto;

import com.voluntrack.api.model.Role;

public record AuthResponse(String token, Long userId, String name, String email, Role role) {}
