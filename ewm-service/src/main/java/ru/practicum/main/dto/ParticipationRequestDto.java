package ru.practicum.main.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.main.model.Status;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    Long id;
    String created;
    Long event;
    Long requester;
    Status status;
}
