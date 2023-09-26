package ru.practicum.main.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EventRequestStatusUpdateResult {
    List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
    List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}
