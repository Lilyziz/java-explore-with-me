package ru.practicum.main.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}
