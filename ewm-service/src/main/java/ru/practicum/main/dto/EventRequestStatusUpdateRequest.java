package ru.practicum.main.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    List<Long> requestIds;
    String status;
}
