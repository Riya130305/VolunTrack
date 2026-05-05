package com.voluntrack.api.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "volunteer_profiles")
public class VolunteerProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false)
  private User user;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "volunteer_skills", joinColumns = @JoinColumn(name = "profile_id"))
  @Column(name = "skill")
  private Set<String> skills = new HashSet<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "volunteer_availability", joinColumns = @JoinColumn(name = "profile_id"))
  @Column(name = "slot")
  private Set<String> availability = new HashSet<>();

  private String phone;
  private String city;

  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }
  public User getUser() { return user; }
  public void setUser(User user) { this.user = user; }
  public Set<String> getSkills() { return skills; }
  public void setSkills(Set<String> skills) { this.skills = skills; }
  public Set<String> getAvailability() { return availability; }
  public void setAvailability(Set<String> availability) { this.availability = availability; }
  public String getPhone() { return phone; }
  public void setPhone(String phone) { this.phone = phone; }
  public String getCity() { return city; }
  public void setCity(String city) { this.city = city; }
}
