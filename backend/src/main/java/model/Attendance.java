package com.voluntrack.api.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "attendance")
public class Attendance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Task task;

  @ManyToOne(optional = false)
  private User volunteer;

  private Instant checkInAt;
  private Instant checkOutAt;
  private Double checkInLat;
  private Double checkInLng;
  private Double checkOutLat;
  private Double checkOutLng;
  private int hoursWorked;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Task getTask() { return task; }
  public void setTask(Task task) { this.task = task; }
  public User getVolunteer() { return volunteer; }
  public void setVolunteer(User volunteer) { this.volunteer = volunteer; }
  public Instant getCheckInAt() { return checkInAt; }
  public void setCheckInAt(Instant checkInAt) { this.checkInAt = checkInAt; }
  public Instant getCheckOutAt() { return checkOutAt; }
  public void setCheckOutAt(Instant checkOutAt) { this.checkOutAt = checkOutAt; }
  public Double getCheckInLat() { return checkInLat; }
  public void setCheckInLat(Double checkInLat) { this.checkInLat = checkInLat; }
  public Double getCheckInLng() { return checkInLng; }
  public void setCheckInLng(Double checkInLng) { this.checkInLng = checkInLng; }
  public Double getCheckOutLat() { return checkOutLat; }
  public void setCheckOutLat(Double checkOutLat) { this.checkOutLat = checkOutLat; }
  public Double getCheckOutLng() { return checkOutLng; }
  public void setCheckOutLng(Double checkOutLng) { this.checkOutLng = checkOutLng; }
  public int getHoursWorked() { return hoursWorked; }
  public void setHoursWorked(int hoursWorked) { this.hoursWorked = hoursWorked; }
}
