package ru.practicum.main.controller.privateController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.ParticipationRequestDto;
import ru.practicum.main.service.priv.PrivateUserRequestService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users/{userId}/requests")
public class PrivateUserRequestController {
    private final PrivateUserRequestService privateUserRequestService;

    public PrivateUserRequestController(PrivateUserRequestService privateUserRequestService) {
        this.privateUserRequestService = privateUserRequestService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ParticipationRequestDto> getAll(@PathVariable Long userId) {
        return privateUserRequestService.getAllUserRequest(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto save(@PathVariable Long userId,
                                        @RequestParam Long eventId) {
        return privateUserRequestService.saveUserRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto update(@PathVariable Long userId,
                                          @PathVariable Long requestId) {
        return privateUserRequestService.updateUserRequestCancel(userId, requestId);
    }
}
