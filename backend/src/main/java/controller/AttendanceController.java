package com.voluntrack.api.controller;

import com.voluntrack.api.dto.AttendanceRequest;
import com.voluntrack.api.model.Attendance;
import com.voluntrack.api.service.AttendanceService;
import com.voluntrack.api.service.CurrentUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {
  private final AttendanceService attendance;
  private final CurrentUserService currentUser;

  public AttendanceController(AttendanceService attendance, CurrentUserService currentUser) {
    this.attendance = attendance;
    this.currentUser = currentUser;
  }

  @PostMapping("/check-in")
  public Attendance checkIn(@RequestBody AttendanceRequest request) {
    return attendance.checkIn(request, currentUser.get());
  }

  @PostMapping("/check-out")
  public Attendance checkOut(@RequestBody AttendanceRequest request) {
    return attendance.checkOut(request, currentUser.get());
  }
}
