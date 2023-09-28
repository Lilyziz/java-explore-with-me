package ru.practicum.main.controller.privateController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.*;
import ru.practicum.main.service.privateService.PrivateUserEventsService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateUserEventsController {
    private final PrivateUserEventsService privateUserEventsService;

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto getById(@PathVariable Long userId,
                                @PathVariable Long eventId) {
        log.debug("Get event with id {} by user with id {}", eventId, userId);

        return privateUserEventsService.getById(userId, eventId);
    }

    @GetMapping
    public List<EventFullDto> getAll(@PathVariable Long userId,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Get events by user with id {}", userId);

        return privateUserEventsService.getAll(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto save(@PathVariable Long userId,
                             @RequestBody @Valid NewEventDto newEventDto) {
        log.debug("Add event by user with id {} with content: {}", userId, newEventDto);

        return privateUserEventsService.save(userId, newEventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest eventUserRequestDto) {

        return privateUserEventsService.update(userId, eventId, eventUserRequestDto);
    }

    @GetMapping("/{eventId}/requests")
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getByIdRequests(@PathVariable Long userId,
                                                         @PathVariable Long eventId) {

        return privateUserEventsService.getByIdUsersEventsRequests(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateRequests(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {

        return privateUserEventsService.updateUsersEventsRequests(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
