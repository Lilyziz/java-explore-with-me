package ru.practicum.main.service.private_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.dto.ParticipationRequestMapper;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.model.*;
import ru.practicum.main.repository.RequestRepository;
import ru.practicum.main.service.admin_service.AdminEventService;
import ru.practicum.main.service.admin_service.AdminUserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateUserRequestService implements IPrivateUserRequestService {
    private final RequestRepository requestRepository;
    private final AdminUserService userService;
    private final AdminEventService eventService;

    @Override
    public List<ParticipationRequestDto> getAll(Long userId) {
        User user = userService.getById(userId);

        return requestRepository.findAllByRequester(user).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto save(Long userId, Long eventId) {
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId);

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new IllegalStateException("Request already exist");
        }

        if (userId.equals(event.getInitiator().getId())) {
            throw new IllegalStateException("You are a initiator of this event");
        }

        if (!eventService.existsByIdAndState(eventId, State.PUBLISHED)) {
            throw new IllegalStateException("Event is not published");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= requestRepository
                .countByEventAndStatus(event, Status.CONFIRMED)) {
            throw new ConflictException("Participant limit reached");
        }

        ParticipationRequest participation = ParticipationRequest
                .builder()
                .created(LocalDateTime.now())
                .requester(user)
                .event(event)
                .status((event.getParticipantLimit() == 0) || (!event.getRequestModeration()) ? Status.CONFIRMED : Status.PENDING)
                .build();

        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(participation));
    }

    @Override
    @Transactional
    public ParticipationRequestDto updateAndCancel(Long userId, Long requestId) {
        User user = userService.getById(userId);

        ParticipationRequest participationRequest = requestRepository.findByIdAndRequester(requestId, user);
        if (participationRequest == null) {
            throw new BadRequestException("There is no request or you have no access");
        }

        participationRequest.setStatus(Status.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(participationRequest));
    }
}
