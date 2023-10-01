package ru.practicum.stats.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.HitDto;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;

@UtilityClass
public class HitMapper {
    public static HitDto toHitDto(Stats stats) {
        return HitDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .build();
    }

    public static Stats stats() {
        return Stats.builder()
                .app("ewm-main-service")
                .ip("127.0.0.0")
                .timestamp(LocalDateTime.now())
                .uri("/events")
                .build();
    }
}
