package com.voluntrack.api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;
  private LocalDateTime startsAt;
  private LocalDateTime endsAt;
  private int estimatedHours;
  private int capacity = 1;
  private String qrCode;

  @Enumerated(EnumType.STRING)
  private TaskStatus status = TaskStatus.OPEN;

  @ManyToOne(optional = false)
  private Event event;

  @ManyToOne
  private User assignedVolunteer;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "task_required_skills", joinColumns = @JoinColumn(name = "task_id"))
  @Column(name = "skill")
  private Set<String> requiredSkills = new HashSet<>();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public LocalDateTime getStartsAt() { return startsAt; }
  public void setStartsAt(LocalDateTime startsAt) { this.startsAt = startsAt; }
  public LocalDateTime getEndsAt() { return endsAt; }
  public void setEndsAt(LocalDateTime endsAt) { this.endsAt = endsAt; }
  public int getEstimatedHours() { return estimatedHours; }
  public void setEstimatedHours(int estimatedHours) { this.estimatedHours = estimatedHours; }
  public int getCapacity() { return capacity; }
  public void setCapacity(int capacity) { this.capacity = capacity; }
  public String getQrCode() { return qrCode; }
  public void setQrCode(String qrCode) { this.qrCode = qrCode; }
  public TaskStatus getStatus() { return status; }
  public void setStatus(TaskStatus status) { this.status = status; }
  public Event getEvent() { return event; }
  public void setEvent(Event event) { this.event = event; }
  public User getAssignedVolunteer() { return assignedVolunteer; }
  public void setAssignedVolunteer(User assignedVolunteer) { this.assignedVolunteer = assignedVolunteer; }
  public Set<String> getRequiredSkills() { return requiredSkills; }
  public void setRequiredSkills(Set<String> requiredSkills) { this.requiredSkills = requiredSkills; }
}
