package com.voluntrack.api.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String description;
  private String locationName;
  private Double latitude;
  private Double longitude;
  private LocalDate eventDate;

  @ManyToOne
  private User organizer;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }
  public String getLocationName() { return locationName; }
  public void setLocationName(String locationName) { this.locationName = locationName; }
  public Double getLatitude() { return latitude; }
  public void setLatitude(Double latitude) { this.latitude = latitude; }
  public Double getLongitude() { return longitude; }
  public void setLongitude(Double longitude) { this.longitude = longitude; }
  public LocalDate getEventDate() { return eventDate; }
  public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
  public User getOrganizer() { return organizer; }
  public void setOrganizer(User organizer) { this.organizer = organizer; }
}
