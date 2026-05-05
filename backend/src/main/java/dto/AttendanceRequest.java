package com.voluntrack.api.dto;

public record AttendanceRequest(Long taskId, Double latitude, Double longitude, String qrCode) {}
