package ru.practicum.main.service.admin_service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventMapper;
import ru.practicum.main.dto.UpdateEventAdminRequest;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.State;
import ru.practicum.main.model.Status;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.LocationRepository;
import ru.practicum.main.repository.RequestRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventService implements IAdminEventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;

    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventFullDto> get(List<Long> userIds, List<String> states, List<Long> categoryIds,
                                  String rangeStart, String rangeEnd, int from, int size) {
        List<State> stateList = states == null ? null : states
                .stream()
                .map(State::valueOf)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime start = rangeStart == null ? null : LocalDateTime.parse(rangeStart, dateTimeFormatter);
        LocalDateTime end = rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        List<EventFullDto> events = eventRepository.findAdminEvents(userIds, stateList, categoryIds,
                        start, end, pageable)
                .stream().map(EventMapper::toEventFullDto).collect(Collectors.toList());

        List<EventFullDto> eventFullDtoList = new ArrayList<>();
        EventFullDto event = new EventFullDto();
        for (EventFullDto item : events) {
            event = item;
            Long eventId = item.getId();
            Integer confirmedRequest = requestRepository.countByEventIdAndStatus(eventId,
                    Status.CONFIRMED);
            event.setConfirmedRequests(confirmedRequest);
            eventFullDtoList.add(event);
        }

        return eventFullDtoList;
    }

    @Override
    @Transactional
    public EventFullDto update(Long eventId, UpdateEventAdminRequest adminRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " was not found"));

        LocalDateTime timeEvent = LocalDateTime.now();
        if (adminRequest != null && adminRequest.getEventDate() != null) {
            timeEvent = LocalDateTime.parse(adminRequest.getEventDate(), dateTimeFormatter);
            if (timeEvent.isBefore(LocalDateTime.now().plusHours(1)))
                throw new BadRequestException("Event should start after one hour minimum");
        }

        if (event.getState() != null && !event.getState().equals(State.PENDING)) {
            throw new IllegalStateException("Event is in illegal state");
        }

        Optional.ofNullable(adminRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(adminRequest.getDescription()).ifPresent(event::setDescription);
        event.setEventDate(timeEvent);

        if (adminRequest.getLocation() != null) {
            locationRepository.save(adminRequest.getLocation());
        }

        event.setLocation(adminRequest.getLocation());
        Optional.ofNullable(adminRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(adminRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(adminRequest.getRequestModeration()).ifPresent(event::setRequestModeration);

        if (adminRequest.getStateAction() != null) {
            adminRequest.setStateAction(adminRequest.getStateAction()
                    .equals("PUBLISH_EVENT") ? "PUBLISHED" : adminRequest.getStateAction());
        }
        if (adminRequest.getStateAction() != null) {
            adminRequest.setStateAction(adminRequest.getStateAction()
                    .equals("REJECT_EVENT") ? "CANCELED" : adminRequest.getStateAction());
        }
        if (adminRequest.getStateAction() != null) {
            Optional.of(State.valueOf(adminRequest.getStateAction())).ifPresent(event::setState);
        }
        Optional.ofNullable(adminRequest.getTitle()).ifPresent(event::setTitle);

        return EventMapper.toEventFullDto(eventRepository.save(event));
    }

    @Override
    public List<Event> getAllById(List<Long> ids) {
        return eventRepository.findAllById(ids);
    }

    @Override
    public Event getFirstByCategoryId(Long id) {
        return eventRepository.findFirstByCategoryId(id);
    }

    @Override
    public Event getById(Long id) {
        return eventRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Event with id = " + id + " was not found"));
    }

    @Override
    public Boolean existsByIdAndState(Long id, State state) {
        return eventRepository.existsByIdAndState(id, state);
    }

    @Override
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllByInitiatorId(Long id, Pageable pageable) {
        return eventRepository.findAllByInitiatorId(id, pageable);
    }
}
