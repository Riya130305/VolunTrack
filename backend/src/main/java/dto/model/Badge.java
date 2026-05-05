package com.voluntrack.api.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "badges")
public class Badge {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private User volunteer;

  private String name;
  private String description;
  private Instant unlockedAt = Instant.now();

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public User getVolunteer() { return volunteer; }
  public void setVolunteer(User volunteer) { this.volunteer = volunteer; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public Instant getUnlockedAt() { return unlockedAt; }
  public void setUnlockedAt(Instant unlockedAt) { this.unlockedAt = unlockedAt; }
}
