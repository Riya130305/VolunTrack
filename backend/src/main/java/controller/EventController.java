package com.voluntrack.api.controller;

import com.voluntrack.api.dto.EventRequest;
import com.voluntrack.api.model.Event;
import com.voluntrack.api.repository.EventRepository;
import com.voluntrack.api.service.CurrentUserService;
import com.voluntrack.api.service.EventService;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {
  private final EventRepository events;
  private final EventService eventService;
  private final CurrentUserService currentUser;

  public EventController(EventRepository events, EventService eventService, CurrentUserService currentUser) {
    this.events = events;
    this.eventService = eventService;
    this.currentUser = currentUser;
  }

  @GetMapping
  public List<Event> all() {
    return events.findAll();
  }

  @PostMapping
  public Event create(@RequestBody EventRequest request) {
    return eventService.create(request, currentUser.get());
  }
}
