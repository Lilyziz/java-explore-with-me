package ru.practicum.main.service.public_service;

import ru.practicum.main.dto.CommentDto;

import java.util.List;

public interface IPublicUserCommentService {
    List<CommentDto> getAllForEvent(Long eventId, int from, int size);
}
