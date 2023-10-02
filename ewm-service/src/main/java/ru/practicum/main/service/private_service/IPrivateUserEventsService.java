package ru.practicum.main.service.private_service;

import org.springframework.stereotype.Service;
import ru.practicum.main.dto.*;

import java.util.List;

@Service
public interface IPrivateUserEventsService {
    List<EventFullDto> getAll(Long userId, int from, int size);

    EventFullDto save(Long userId, NewEventDto newEventDto);

    EventFullDto getById(Long userId, Long eventId);

    EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest newEventDto);

    List<ParticipationRequestDto> getByIdUsersEventsRequests(Long eventId, Long userId);

    EventRequestStatusUpdateResult updateUsersEventsRequests(Long userId, Long eventId,
                                                             EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
    EventFullDto setConfirmedStatus(EventFullDto eventDto);
}
