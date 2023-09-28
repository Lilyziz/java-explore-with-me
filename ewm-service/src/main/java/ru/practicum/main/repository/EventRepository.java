package ru.practicum.main.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.main.model.Event;
import ru.practicum.main.model.State;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findFirstByCategoryId(Long id);

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    @Query("SELECT e FROM Event AS e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users)" +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (cast(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findAdminEvents(
            @Param("users") List<Long> users,
            @Param("states") List<State> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") LocalDateTime rangeStart,
            @Param("rangeEnd") LocalDateTime rangeEnd,
            Pageable pageable);

    Boolean existsByIdAndState(Long eventId, State state);

    @Query("SELECT e FROM Event AS e " +
            " WHERE ((:text IS NULL OR lower(e.annotation) LIKE lower(concat('%', :text, '%'))) " +
            " OR (:text IS NULL OR lower(e.description) LIKE lower(concat('%', :text, '%')))) " +
            " AND (:state IS NULL OR e.state = :state) " +
            " AND (:categories IS NULL OR e.category.id IN :categories) " +
            " AND (:paid IS NULL OR e.paid = :paid) " +
            " AND (cast(:rangeStart AS java.time.LocalDateTime) IS NULL OR e.eventDate >= :rangeStart) " +
            " AND (cast(:rangeEnd AS java.time.LocalDateTime) IS NULL OR e.eventDate <= :rangeEnd)")
    List<Event> findEvents(@Param("text") String text,
                           @Param("state") State state,
                           @Param("categories") List<Long> categories,
                           @Param("paid") Boolean paid,
                           @Param("rangeStart") LocalDateTime rangeStart,
                           @Param("rangeEnd") LocalDateTime rangeEnd,
                           Pageable pageable);
}
