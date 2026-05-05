package com.voluntrack.api.service;

import com.voluntrack.api.dto.TaskRequest;
import com.voluntrack.api.model.*;
import com.voluntrack.api.repository.*;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TaskService {
  private final TaskRepository tasks;
  private final EventRepository events;
  private final TaskApplicationRepository applications;
  private final VolunteerProfileRepository profiles;
  private final MatchingService matching;

  public TaskService(TaskRepository tasks, EventRepository events, TaskApplicationRepository applications,
                     VolunteerProfileRepository profiles, MatchingService matching) {
    this.tasks = tasks;
    this.events = events;
    this.applications = applications;
    this.profiles = profiles;
    this.matching = matching;
  }

  public Task create(TaskRequest request) {
    Event event = events.findById(request.eventId()).orElseThrow();
    Task task = new Task();
    task.setEvent(event);
    task.setTitle(request.title());
    task.setDescription(request.description());
    task.setStartsAt(request.startsAt());
    task.setEndsAt(request.endsAt());
    task.setEstimatedHours(request.estimatedHours());
    task.setCapacity(request.capacity() == 0 ? 1 : request.capacity());
    task.setRequiredSkills(request.requiredSkills());
    task.setQrCode("VT-" + UUID.randomUUID());
    return tasks.save(task);
  }

  public List<Task> openTasks() {
    return tasks.findByStatus(TaskStatus.OPEN);
  }

  public TaskApplication apply(Long taskId, User volunteer) {
    Task task = tasks.findById(taskId).orElseThrow();
    VolunteerProfile profile = profiles.findByUser(volunteer).orElseThrow();
    TaskApplication application = new TaskApplication();
    application.setTask(task);
    application.setVolunteer(volunteer);
    application.setMatchScore(matching.score(profile, task));
    return applications.save(application);
  }

  public TaskApplication approve(Long applicationId) {
    TaskApplication application = applications.findById(applicationId).orElseThrow();
    application.setStatus(ApplicationStatus.APPROVED);
    application.getTask().setAssignedVolunteer(application.getVolunteer());
    application.getTask().setStatus(TaskStatus.ASSIGNED);
    tasks.save(application.getTask());
    return applications.save(application);
  }
}
