package ru.practicum.main.service.public_service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.CommentMapper;
import ru.practicum.main.model.Event;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.service.admin_service.AdminEventService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicUserCommentService implements IPublicUserCommentService {
    private final CommentRepository commentRepository;
    private final AdminEventService eventService;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllForEvent(Long eventId, int from, int size) {
        Event event = eventService.getById(eventId);
        Pageable pageable = PageRequest.of(from / size, size);

        return commentRepository.findAllCommentByEvent(event, pageable)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
