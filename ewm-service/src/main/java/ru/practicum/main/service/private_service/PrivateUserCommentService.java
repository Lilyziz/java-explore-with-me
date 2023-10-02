package ru.practicum.main.service.private_service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.CommentMapper;
import ru.practicum.main.exception.BadRequestException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.repository.EventRepository;
import ru.practicum.main.repository.UserRepository;
import ru.practicum.main.service.admin_service.AdminEventService;
import ru.practicum.main.service.admin_service.AdminUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateUserCommentService implements IPrivateUserCommentService {
    private final CommentRepository commentRepository;
    private final AdminUserService userService;
    private final AdminEventService eventService;

    @Override
    @Transactional
    public CommentDto save(Long userId, Long eventId, CommentDto commentDto) {
        User user = userService.getById(userId);
        Event event = eventService.getById(eventId);

        Comment comment = CommentMapper.toComment(commentDto);
        comment.setUser(user);
        comment.setEvent(event);

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getAll(Long userId, int from, int size) {
        User user = userService.getById(userId);

        return commentRepository.findAllByUser(user, PageRequest.of(from / size, size))
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto update(Long commentId, Long userId, CommentDto commentDto) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(
                () -> new BadRequestException("Only author can change this comment"));
        comment.setText(commentDto.getText());

        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findByIdAndUserId(commentId, userId).orElseThrow(
                () -> new BadRequestException("Only author can delete this comment"));

        commentRepository.delete(comment);
    }
}