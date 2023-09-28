package ru.practicum.main.service.publicService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventMapper;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.State;
import ru.practicum.main.model.Status;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.RequestRepository;
import ru.practicum.main.stats.StatsServer;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.main.model.State.PUBLISHED;

@Service
@RequiredArgsConstructor
public class PublicEventService implements IPublicEventService {
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatsServer statsServer;
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EventFullDto getById(Long eventId, HttpServletRequest request) throws IOException, InterruptedException {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + eventId + " was not found"));

        if (event.getState() == null || !event.getState().equals(State.PUBLISHED))
            throw new NotFoundException("Event is not published");

        statsServer.saveHit(request);

        Integer currentViews = statsServer.requeryViews(request.getRequestURI());
        event.setViews(currentViews);
        eventRepository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, String rangeStart,
                                      String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                      Integer size, HttpServletRequest request) {


        if (categories.size() < 1 || (text != null && text.length() < 2)) {
            throw new BadRequestException("Text must be longer then 2");
        }

        Pageable pageable = PageRequest.of(from / size, size);
        LocalDateTime start = rangeStart == null ? null : LocalDateTime.parse(rangeStart, dateTimeFormatter);
        LocalDateTime end = rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, dateTimeFormatter);
        List<Event> events = eventRepository.searchEvents2(text, PUBLISHED, categories, paid, start, end, pageable);

        if (Boolean.TRUE.equals(onlyAvailable)) {
            events = events.stream().filter(e -> e.getParticipantLimit() < requestRepository
                            .countAllByEventIdAndStatus(e.getId(), Status.CONFIRMED))
                    .collect(Collectors.toList());
        }

        if (sort != null) {
            switch (sort) {
                case "EVENT_DATE":
                    events = events.stream().sorted(Comparator.comparing(Event::getEventDate))
                            .collect(Collectors.toList());
                    break;
                case "VIEWS":
                    events = events.stream().sorted(Comparator.comparing(Event::getViews))
                            .collect(Collectors.toList());
                    break;
            }
        }

        return events
                .stream()
                .peek(e -> addViews(e.getId()))
                .peek(e -> {
                    try {
                        statsServer.saveHit(request);
                    } catch (IOException | InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    private void addViews(Long id) {
        Event event = eventRepository.findById(id).get();
        Integer views = event.getViews() + 1;
        event.setViews(views);
    }
}
