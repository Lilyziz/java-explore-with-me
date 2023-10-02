package ru.practicum.main.service.admin_service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.UpdateEventAdminRequest;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.State;

import java.util.List;

@Service
public interface IAdminEventService {
    List<EventFullDto> get(List<Long> userIds, List<String> states, List<Long> categoryIds,
                           String rangeStart, String rangeEnd, int from, int size);

    EventFullDto update(Long eventId, UpdateEventAdminRequest adminRequest);

    List<Event> getAllById(List<Long> ids);

    Event getFirstByCategoryId(Long id);

    Event getById(Long id);

    Boolean existsByIdAndState(Long id, State state);

    Event save(Event event);

    List<Event> getAllByInitiatorId(Long id, Pageable pageable);
}
