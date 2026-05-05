package com.voluntrack.api.controller;

import com.voluntrack.api.dto.TaskRequest;
import com.voluntrack.api.model.Task;
import com.voluntrack.api.model.TaskApplication;
import com.voluntrack.api.repository.TaskApplicationRepository;
import com.voluntrack.api.repository.TaskRepository;
import com.voluntrack.api.service.CurrentUserService;
import com.voluntrack.api.service.TaskService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;
  private final TaskRepository tasks;
  private final TaskApplicationRepository applications;
  private final CurrentUserService currentUser;

  public TaskController(TaskService taskService, TaskRepository tasks, TaskApplicationRepository applications,
                        CurrentUserService currentUser) {
    this.taskService = taskService;
    this.tasks = tasks;
    this.applications = applications;
    this.currentUser = currentUser;
  }

  @GetMapping
  public List<Task> all() {
    return tasks.findAll();
  }

  @GetMapping("/open")
  public List<Task> open() {
    return taskService.openTasks();
  }

  @PostMapping
  public Task create(@RequestBody TaskRequest request) {
    return taskService.create(request);
  }

  @PostMapping("/{taskId}/apply")
  public TaskApplication apply(@PathVariable Long taskId) {
    return taskService.apply(taskId, currentUser.get());
  }

  @GetMapping("/applications")
  public List<TaskApplication> applications() {
    return applications.findAll();
  }
}
