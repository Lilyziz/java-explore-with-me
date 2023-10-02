package ru.practicum.main.controller.private_controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.service.private_service.IPrivateUserCommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateUserCommentController {
    private final IPrivateUserCommentService privateUserCommentService;

    @PostMapping("/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto save(@PositiveOrZero @PathVariable Long userId,
                           @PositiveOrZero @PathVariable Long eventId,
                           @Valid @RequestBody CommentDto commentDto) {

        log.debug("Create comment for event with id {} from user with id {} with content {}",
                eventId, userId, commentDto);
        return privateUserCommentService.save(userId, eventId, commentDto);
    }


    @GetMapping
    public List<CommentDto> getAll(@Positive @PathVariable Long userId,
                                   @RequestParam(defaultValue = "0") int from,
                                   @RequestParam(defaultValue = "10") int size) {

        log.debug("Get all comment from user with id {}", userId);
        return privateUserCommentService.getAll(userId, from, size);
    }


    @PatchMapping("/{commentId}")
    public CommentDto update(@PositiveOrZero @PathVariable Long commentId,
                             @PositiveOrZero @PathVariable Long userId,
                             @Valid @RequestBody CommentDto commentDto) {

        log.debug("Update comment with id {} from user with id {} with content {}", commentId, userId, commentDto);
        return privateUserCommentService.update(commentId, userId, commentDto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PositiveOrZero @PathVariable Long userId,
                       @PositiveOrZero @PathVariable Long commentId) {

        log.debug("Delete comment with id {} from user with id {}", commentId, userId);
        privateUserCommentService.delete(commentId, userId);
    }
}
