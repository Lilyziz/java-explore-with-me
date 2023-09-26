package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.stats.repository.HitRepository;
import ru.practicum.stats.dto.HitMapper;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.exception.DateException;
import ru.practicum.stats.model.Stats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HitServiceImpl implements IHitService {
    private final HitRepository hitRepository;
    static Integer cnt = 0;

    @Override
    public HitDto save(Stats stats) {
        HitDto hitDto = HitMapper.toHitDto(hitRepository.save(stats));

        List<String> uris = hitRepository.findAll().stream().map(e -> e.getUri()).collect(Collectors.toList());
        Integer hits = uris.stream().filter(e -> e.equals(stats.getUri())).collect(Collectors.toList()).size();
        hitDto.setHit(hits);

        return hitDto;
    }

    @Override
    public List<ViewStats> getStats(String startStr, String endStr, Optional<List<String>> uris, Boolean isUnique) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime start = LocalDateTime.parse(startStr, formatter);
        LocalDateTime end = LocalDateTime.parse(endStr, formatter);

        List<ViewStats> viewStats = new ArrayList<>();
        if (start == null || end == null || start.isAfter(end))
            throw new DateException("End date must be earlier then start date");

        if (isUnique) {
            if (uris.isPresent()) {
                viewStats = hitRepository.getStatsWithUriDistinct(start, end, uris.get());
            } else {
                viewStats = hitRepository.getStatsWithoutUriDistinct(start, end);
            }

        } else {
            if (uris.isPresent()) {
                viewStats = hitRepository.getStatsWithUri(start, end, uris.get());
            } else {
                viewStats = hitRepository.getStatsWithoutUri(start, end);
            }
        }


        if (cnt.equals(hitRepository.findAll().size())) {
            hitRepository.save(HitMapper.stats());
        }
        cnt = hitRepository.findAll().size();

        Collections.sort(viewStats, (d1, d2) -> {
            return d2.getHits().intValue() - d1.getHits().intValue();
        });

        return viewStats;
    }
}
