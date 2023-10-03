package ru.practicum.main.repository;

import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.model.Comment;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.User;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByUser(User user, Pageable pageable);

    Optional<Comment> findByIdAndUserId(Long id, Long userId);

    Page<Comment> findAllCommentByEvent(Event event, Pageable pageable);
}