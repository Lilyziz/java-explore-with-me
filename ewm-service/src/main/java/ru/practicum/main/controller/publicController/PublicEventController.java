package ru.practicum.main.controller.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.EventFullDto;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.service.pub.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
public class PublicEventController {

    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventShortDto> getAllEvents(@RequestParam(required = false) String text,
                                            @RequestParam List<Long> categories,
                                            @RequestParam(required = false) Boolean paid,
                                            @RequestParam(required = false) String rangeStart,
                                            @RequestParam(required = false) String rangeEnd,
                                            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                            @RequestParam(required = false) String sort,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size,
                                            HttpServletRequest request) throws IOException, InterruptedException {

        log.debug("Get all events");
        return publicEventService.getAll(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable Long eventId, HttpServletRequest request)
            throws IOException, InterruptedException {

        log.debug("Get event with id {}", eventId);
        return publicEventService.getById(eventId, request);
    }
}
