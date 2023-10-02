package ru.practicum.main.service.public_service;

import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface IPublicEventService {
    EventFullDto getById(Long eventId, HttpServletRequest request) throws IOException, InterruptedException;

    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                               Boolean onlyAvailable, String sort, Integer from, Integer size,
                               HttpServletRequest request);
}
