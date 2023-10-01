package ru.practicum.main.service.privateService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.dto.ParticipationRequestMapper;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.ConflictException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.*;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.RequestRepository;
import ru.practicum.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateUserRequestService implements IPrivateUserRequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getAll(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " was not found"));

        return requestRepository.findAllByRequester(user).stream()
                .map(ParticipationRequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipationRequestDto save(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " was not found"));
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id = " + eventId + " was not found"));

        if (requestRepository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new IllegalStateException("Request already exist");
        }

        if (userId.equals(event.getInitiator().getId())) {
            throw new IllegalStateException("You are a initiator of this event");
        }

        if (!eventRepository.existsByIdAndState(eventId, State.PUBLISHED)) {
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
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("User with id = " + userId + " was not found"));

        ParticipationRequest participationRequest = requestRepository.findByIdAndRequester(requestId, user);
        if (participationRequest == null) {
            throw new BadRequestException("There is no request or you have no access");
        }

        participationRequest.setStatus(Status.CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(requestRepository.save(participationRequest));
    }
}
