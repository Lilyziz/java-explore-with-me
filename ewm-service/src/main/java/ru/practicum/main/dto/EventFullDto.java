package ru.practicum.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.model.Location;
import ru.practicum.main.model.State;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {
    Long id;

    @NotNull
    String annotation;

    @NotNull
    CategoryDto category;

    Integer confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdOn;

    String description;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;

    @NotNull
    UserShortDto initiator;

    @NotNull
    Location location;

    @NotNull
    Boolean paid;

    Integer participantLimit;
    LocalDateTime publishedOn;
    Boolean requestModeration;
    State state;

    @NotNull
    String title;

    Integer views;
}
