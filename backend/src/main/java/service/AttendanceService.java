package com.voluntrack.api.service;

import com.voluntrack.api.dto.AttendanceRequest;
import com.voluntrack.api.model.*;
import com.voluntrack.api.repository.AttendanceRepository;
import com.voluntrack.api.repository.TaskRepository;
import java.time.Duration;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class AttendanceService {
  private final AttendanceRepository attendanceRepository;
  private final TaskRepository tasks;
  private final GamificationService gamification;

  public AttendanceService(AttendanceRepository attendanceRepository, TaskRepository tasks, GamificationService gamification) {
    this.attendanceRepository = attendanceRepository;
    this.tasks = tasks;
    this.gamification = gamification;
  }

  public Attendance checkIn(AttendanceRequest request, User volunteer) {
    Task task = tasks.findById(request.taskId()).orElseThrow();
    requireAssigned(task, volunteer);
    requireQr(task, request.qrCode());
    requireNearby(task, request.latitude(), request.longitude());

    Attendance attendance = new Attendance();
    attendance.setTask(task);
    attendance.setVolunteer(volunteer);
    attendance.setCheckInAt(Instant.now());
    attendance.setCheckInLat(request.latitude());
    attendance.setCheckInLng(request.longitude());
    return attendanceRepository.save(attendance);
  }

  public Attendance checkOut(AttendanceRequest request, User volunteer) {
    Task task = tasks.findById(request.taskId()).orElseThrow();
    requireAssigned(task, volunteer);
    Attendance attendance = attendanceRepository.findFirstByTaskAndVolunteerOrderByIdDesc(task, volunteer).orElseThrow();
    attendance.setCheckOutAt(Instant.now());
    attendance.setCheckOutLat(request.latitude());
    attendance.setCheckOutLng(request.longitude());
    int hours = Math.max(1, (int) Math.ceil(Duration.between(attendance.getCheckInAt(), attendance.getCheckOutAt()).toMinutes() / 60.0));
    attendance.setHoursWorked(hours);
    task.setStatus(TaskStatus.COMPLETED);
    tasks.save(task);
    gamification.addCompletionRewards(volunteer, hours);
    return attendanceRepository.save(attendance);
  }

  private void requireAssigned(Task task, User volunteer) {
    if (task.getAssignedVolunteer() == null || !task.getAssignedVolunteer().getId().equals(volunteer.getId())) {
      throw new IllegalArgumentException("Volunteer is not assigned to this task");
    }
  }

  private void requireQr(Task task, String qrCode) {
    if (qrCode == null || !qrCode.equals(task.getQrCode())) {
      throw new IllegalArgumentException("Invalid QR code");
    }
  }

  private void requireNearby(Task task, Double lat, Double lng) {
    if (task.getEvent().getLatitude() == null || lat == null || lng == null) return;
    double distanceMeters = distance(task.getEvent().getLatitude(), task.getEvent().getLongitude(), lat, lng);
    if (distanceMeters > 300) {
      throw new IllegalArgumentException("You must be within 300 meters of the event");
    }
  }

  private double distance(double lat1, double lon1, double lat2, double lon2) {
    double earth = 6371000;
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);
    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
        * Math.sin(dLon / 2) * Math.sin(dLon / 2);
    return earth * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  }
}
