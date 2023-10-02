package ru.practicum.main.service.admin_service;

import org.springframework.stereotype.Service;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.UpdateEventAdminRequest;

import java.util.List;

@Service
public interface IAdminEventService {
    List<EventFullDto> get(List<Long> userIds, List<String> states, List<Long> categoryIds,
                           String rangeStart, String rangeEnd, int from, int size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest adminRequest);
}
