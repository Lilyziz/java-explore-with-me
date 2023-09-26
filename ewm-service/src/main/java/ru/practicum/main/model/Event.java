package ru.practicum.main.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(length = 3000, nullable = false)
    String annotation;

    @ManyToOne
    Category category;

    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Column(length = 3000)
    String description;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @ManyToOne
    User initiator;

    @ManyToOne
    Location location;

    @Column(nullable = false)
    Boolean paid;

    @Column(name = "participant_limit")
    Integer participantLimit;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    State state;

    @Column(length = 200, nullable = false)
    String title;

    @Builder.Default
    Integer views = 0;
}
