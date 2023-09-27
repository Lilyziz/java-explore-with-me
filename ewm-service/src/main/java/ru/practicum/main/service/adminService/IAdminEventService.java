package ru.practicum.main.service.adminService;

import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.UpdateEventAdminRequest;

import java.util.List;

public interface IAdminEventService {
    List<EventFullDto> get(List<Long> userIds, List<String> states, List<Long> categoryIds,
                           String rangeStart, String rangeEnd, int from, int size);
    EventFullDto update(Long eventId, UpdateEventAdminRequest adminRequest);
}
