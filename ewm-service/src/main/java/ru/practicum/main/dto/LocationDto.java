package ru.practicum.main.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    float lat;
    float lon;
}
