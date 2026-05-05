package com.voluntrack.api.dto;

import java.time.LocalDate;

public record EventRequest(
    String title,
    String description,
    String locationName,
    Double latitude,
    Double longitude,
    LocalDate eventDate
) {}
