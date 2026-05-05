package com.voluntrack.api.controller;

import com.voluntrack.api.dto.AdminStats;
import com.voluntrack.api.model.Role;
import com.voluntrack.api.repository.*;
import com.voluntrack.api.service.TaskService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
  private final UserRepository users;
  private final TaskRepository tasks;
  private final TaskApplicationRepository applications;
  private final AttendanceRepository attendance;
  private final TaskService taskService;

  public AdminController(UserRepository users, TaskRepository tasks, TaskApplicationRepository applications,
                         AttendanceRepository attendance, TaskService taskService) {
    this.users = users;
    this.tasks = tasks;
    this.applications = applications;
    this.attendance = attendance;
    this.taskService = taskService;
  }

  @GetMapping("/stats")
  public AdminStats stats() {
    long totalHours = users.findAll().stream().mapToLong(user -> user.getTotalHours()).sum();
    long volunteers = users.findAll().stream().filter(user -> user.getRole() == Role.VOLUNTEER).count();
    return new AdminStats(volunteers, tasks.count(), applications.count(), attendance.count(), totalHours);
  }

  @PostMapping("/applications/{applicationId}/approve")
  public Object approve(@PathVariable Long applicationId) {
    return taskService.approve(applicationId);
  }
}
