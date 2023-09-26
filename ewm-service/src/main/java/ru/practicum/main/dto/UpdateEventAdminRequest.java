package ru.practicum.main.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.model.Location;

import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000)
    String description;

    String eventDate;
    Location location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;

    @Size(min = 3, max = 120)
    String title;
}
