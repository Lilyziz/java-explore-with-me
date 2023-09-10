package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.HitMapper;
import ru.practicum.model.ViewHits;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements IStatsService {
    private final StatsRepository statsRepository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        statsRepository.save(HitMapper.toModel(endpointHit));
    }

    @Override
    public List<ViewHits> findHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (unique) {
            if (uris.isEmpty()) {
                return statsRepository.findStatsAllDistinct(start, end).stream()
                        .map(HitMapper::toDto)
                        .collect(Collectors.toList());
            } else {
                return statsRepository.findStatsInUrisDistinct(start, end, uris).stream()
                        .map(HitMapper::toDto)
                        .collect(Collectors.toList());
            }
        } else {
            if (uris.isEmpty()) {
                return statsRepository.findStatsAll(start, end).stream()
                        .map(HitMapper::toDto)
                        .collect(Collectors.toList());
            } else {
                return statsRepository.findStatsInUris(start, end, uris).stream()
                        .map(HitMapper::toDto)
                        .collect(Collectors.toList());
            }
        }
    }
}
