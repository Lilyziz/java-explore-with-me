package ru.practicum.service;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewHits;

import java.time.LocalDateTime;
import java.util.List;

public interface IStatsService {
    void saveHit(EndpointHit endpointHit);

    List<ViewHits> findHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
