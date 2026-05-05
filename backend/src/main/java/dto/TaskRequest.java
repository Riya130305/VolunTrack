package com.voluntrack.api.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record TaskRequest(
    Long eventId,
    String title,
    String description,
    LocalDateTime startsAt,
    LocalDateTime endsAt,
    int estimatedHours,
    int capacity,
    Set<String> requiredSkills
) {}
