package ru.practicum.main.service.private_service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.*;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.*;
import ru.practicum.main.repository.*;
import ru.practicum.main.service.admin_service.AdminEventService;
import ru.practicum.main.service.admin_service.AdminUserService;
import ru.practicum.main.service.public_service.PublicCategoryService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateUserEventsService implements IPrivateUserEventsService {
    private final AdminUserService userService;
    private final AdminEventService eventService;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final RequestRepository requestRepository;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ParticipationRequestDto> getByIdUsersEventsRequests(Long eventId, Long userId) {
        List<ParticipationRequestDto> participationRequestDtos = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId)
                .stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());

        if (participationRequestDtos.isEmpty()) {
            return List.of(new ParticipationRequestDto());
        } else {
            return participationRequestDtos;
        }
    }

    @Override
    public List<EventFullDto> getAll(Long userId, int from, int size) {
        userService.getById(userId);

        Pageable pageable = PageRequest.of(from / size, size);
        return eventService.getAllByInitiatorId(userId, pageable)
                .stream()
                .map(EventMapper::toEventFullDto)
                .map(this::setConfirmedStatus)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        User user = userService.getById(userId);

        Event event = EventMapper.toEvent(newEventDto);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new BadRequestException("Too late for the event");
        }
        Location location = locationRepository.save(newEventDto.getLocation());
        event.setCategory(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category was not found")));

        event.setLocation(location);
        event.setInitiator(user);
        event.setCreatedOn(LocalDateTime.now());
        event.setState(State.PENDING);

        return EventMapper.toEventFullDto(eventService.save(event));
    }

    @Override
    public EventFullDto getById(Long userId, Long eventId) {
        Event event = eventService.getById(eventId);

        if (!event.getInitiator().getId().equals(userId)) {
            throw new BadRequestException("You are not a initiator");
        }

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        eventFullDto.setConfirmedRequests(requestRepository.countByEventIdAndStatus(event.getId(), Status.CONFIRMED));

        return eventFullDto;
    }

    @Override
    @Transactional
    public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest newEventDto) {
        userService.getById(userId);

        Event event = eventService.getById(eventId);
        LocalDateTime dateTime = event.getPublishedOn();

        if (event.getState() != null && event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException("Published event can't be changed");
        }

        if (newEventDto.getEventDate() != null) {
            LocalDateTime timeEvent = LocalDateTime.parse(newEventDto.getEventDate(), dateTimeFormatter);
            if (timeEvent.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new BadRequestException("Invalid time for event");
            }
            event.setEventDate(timeEvent);
        }

        Optional.ofNullable(newEventDto.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(newEventDto.getDescription()).ifPresent(event::setDescription);
        if (newEventDto.getLocation() != null) {
            locationRepository.save(newEventDto.getLocation());
        }
        event.setLocation(newEventDto.getLocation());
        Optional.ofNullable(newEventDto.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(newEventDto.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(newEventDto.getRequestModeration()).ifPresent(event::setRequestModeration);
        if (newEventDto.getStateAction() != null) {
            newEventDto.setStateAction(newEventDto.getStateAction()
                    .equals("PUBLISH_EVENT") ? "PUBLISHED" : newEventDto.getStateAction());
        }
        if (newEventDto.getStateAction() != null) {
            newEventDto.setStateAction(newEventDto.getStateAction()
                    .equals("SEND_TO_REVIEW") ? "PENDING" : newEventDto.getStateAction());
        }
        if (newEventDto.getStateAction() != null) {
            newEventDto.setStateAction(newEventDto.getStateAction()
                    .equals("CANCEL_REVIEW") ? "CANCELED" : newEventDto.getStateAction());
        }
        if (newEventDto.getStateAction() != null) {
            Optional.of(State.valueOf(newEventDto.getStateAction())).ifPresent(event::setState);
        }
        Optional.ofNullable(newEventDto.getTitle()).ifPresent(event::setTitle);

        event.setEventDate(dateTime);
        return EventMapper.toEventFullDto(eventService.save(event));
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateUsersEventsRequests(Long userId, Long eventId,
                                                                    EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        Event event = eventService.getById(eventId);

        List<ParticipationRequest> requestsEvent = requestRepository.findAllByEventId(eventId);


        if (!event.getInitiator().getId().equals(userId)) {
            throw new NotFoundException("You can't change event because you are not a initiator");
        }

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            throw new BadRequestException("Moderation not needed");
        }

        if (event.getParticipantLimit() <= requestRepository.countByEventAndStatus(event, Status.CONFIRMED)) {
            throw new ConflictException("Participant limit was reached");
        }

        List<ParticipationRequest> updatedParticipationRequest = new ArrayList<>();
        for (ParticipationRequest request : requestsEvent) {
            if (eventRequestStatusUpdateRequest.getRequestIds().contains(request.getId())) {
                request.setStatus(Status.valueOf(eventRequestStatusUpdateRequest.getStatus()));
                updatedParticipationRequest.add(request);
            }
        }

        requestRepository.saveAll(updatedParticipationRequest);

        EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();

        for (ParticipationRequest iter : updatedParticipationRequest) {
            if (iter.getStatus().equals(Status.CONFIRMED)) {
                eventRequestStatusUpdateResult.getConfirmedRequests().add(ParticipationRequestMapper
                        .toParticipationRequestDto(iter));
            } else if (iter.getStatus().equals(Status.REJECTED)) {
                eventRequestStatusUpdateResult.getRejectedRequests().add(ParticipationRequestMapper
                        .toParticipationRequestDto(iter));
            }
        }

        return eventRequestStatusUpdateResult;
    }

    @Override
    public EventFullDto setConfirmedStatus(EventFullDto eventDto) {
        eventDto.setConfirmedRequests(requestRepository.countByEventIdAndStatus(eventDto.getId(), Status.CONFIRMED));

        return eventDto;
    }
}
