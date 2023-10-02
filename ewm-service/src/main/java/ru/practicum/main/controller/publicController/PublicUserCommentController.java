package ru.practicum.main.controller.publicController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CommentDto;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class PublicUserCommentController {
    private final PublicUserCommentController publicUserCommentController;

    @GetMapping
    public List<CommentDto> getAllForEvent(@PositiveOrZero @PathVariable Long eventId,
                                           @RequestParam(defaultValue = "0") int from,
                                           @RequestParam(defaultValue = "10") int size) {
        log.debug("");

        return publicUserCommentController.getAllForEvent(eventId, from, size);
    }
}
