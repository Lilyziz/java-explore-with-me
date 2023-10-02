package ru.practicum.main.service.publicService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.CommentDto;
import ru.practicum.main.dto.CommentMapper;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Event;
import ru.practicum.main.repository.CommentRepository;
import ru.practicum.main.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicUserCommentService implements IPublicUserCommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> getAllForEvent(Long eventId, int from, int size) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event was not found"));
        Pageable pageable = PageRequest.of(from / size, size);

        return commentRepository.findAllByEvent(event, pageable)
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }
}
