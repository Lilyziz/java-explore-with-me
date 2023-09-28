package ru.practicum.main.controller.privateController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.service.privateService.PrivateUserRequestService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class PrivateUserRequestController {
    private final PrivateUserRequestService privateUserRequestService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAll(@PathVariable Long userId) {
        log.debug("Get all requests by user with id {}", userId);

        return privateUserRequestService.getAll(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable Long userId,
                                        @RequestParam Long eventId) {
        log.debug("Add request for event with id {} by user with id {}", eventId, userId);

        return privateUserRequestService.save(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto update(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        log.debug("Cancel request with id {} by user with id {}", requestId, userId);

        return privateUserRequestService.updateAndCancel(userId, requestId);
    }
}
