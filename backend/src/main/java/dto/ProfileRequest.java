package com.voluntrack.api.dto;

import java.util.Set;

public record ProfileRequest(Set<String> skills, Set<String> availability, String phone, String city) {}
