package ru.practicum.main.service.privateService;

import ru.practicum.main.dto.CommentDto;

import java.util.List;

public interface IPrivateUserCommentService {
    List<CommentDto> getAll(Long userId, int from, int size);
    CommentDto save(Long userId, Long eventId, CommentDto commentDto);
    CommentDto update(Long commentId, Long userId, CommentDto commentDto);
    void delete(Long commentId, Long userId);
}
