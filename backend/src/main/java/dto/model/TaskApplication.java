package com.voluntrack.api.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "applications")
public class TaskApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Task task;

  @ManyToOne(optional = false)
  private User volunteer;

  @Enumerated(EnumType.STRING)
  private ApplicationStatus status = ApplicationStatus.PENDING;

  private int matchScore;
  private Instant appliedAt = Instant.now();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public Task getTask() { return task; }
  public void setTask(Task task) { this.task = task; }
  public User getVolunteer() { return volunteer; }
  public void setVolunteer(User volunteer) { this.volunteer = volunteer; }
  public ApplicationStatus getStatus() { return status; }
  public void setStatus(ApplicationStatus status) { this.status = status; }
  public int getMatchScore() { return matchScore; }
  public void setMatchScore(int matchScore) { this.matchScore = matchScore; }
  public Instant getAppliedAt() { return appliedAt; }
  public void setAppliedAt(Instant appliedAt) { this.appliedAt = appliedAt; }
}
