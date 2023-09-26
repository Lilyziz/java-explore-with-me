package ru.practicum.stats.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDto;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.model.Stats;
import ru.practicum.stats.service.HitServiceImpl;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class HitController {
    private final HitServiceImpl hitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitDto save(@RequestBody Stats stats) {
        log.debug("Received stats {}", stats);
        return hitService.save(stats);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam(required = false) Optional<List<String>> uris,
                                    @RequestParam(defaultValue = "false") boolean unique) {
        log.debug("Received a request to get statistics with parameters:" +
                " start = {}," +
                " end = {}," +
                " uris = {}," +
                " unique = {}", start, end, uris, unique);
        return hitService.getStats(start, end, uris, unique);
    }
}
