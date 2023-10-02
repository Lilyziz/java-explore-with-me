package ru.practicum.main.service.private_service;

import org.springframework.stereotype.Service;
import ru.practicum.main.dto.ParticipationRequestDto;

import java.util.List;

@Service
public interface IPrivateUserRequestService {
    List<ParticipationRequestDto> getAll(Long userId);

    ParticipationRequestDto save(Long userId, Long eventId);

    ParticipationRequestDto updateAndCancel(Long userId, Long requestId);
}
