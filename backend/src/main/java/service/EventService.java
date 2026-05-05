package com.voluntrack.api.service;

import com.voluntrack.api.dto.EventRequest;
import com.voluntrack.api.model.Event;
import com.voluntrack.api.model.User;
import com.voluntrack.api.repository.EventRepository;
import org.springframework.stereotype.Service;

@Service
public class EventService {
  private final EventRepository events;

  public EventService(EventRepository events) {
    this.events = events;
  }

  public Event create(EventRequest request, User organizer) {
    Event event = new Event();
    event.setTitle(request.title());
    event.setDescription(request.description());
    event.setLocationName(request.locationName());
    event.setLatitude(request.latitude());
    event.setLongitude(request.longitude());
    event.setEventDate(request.eventDate());
    event.setOrganizer(organizer);
    return events.save(event);
  }
}
