package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewHits;
import ru.practicum.service.IStatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class StatsController {
    private final IStatsService statsService;

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void save(@Valid @RequestBody EndpointHit endpointHit) {
        log.info("Received hit, with parameters:" +
                        " app = {}," +
                        " uri = {}," +
                        " ip = {}," +
                        " timestamp = {}",
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp());

        statsService.saveHit(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(value = HttpStatus.OK)
    public List<ViewHits> getStats(@RequestParam(name = "start")
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                   @RequestParam(name = "end")
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                   @RequestParam(name = "uris", defaultValue = "") List<String> uris,
                                   @RequestParam(name = "unique", defaultValue = "false") boolean unique) {
        log.info("Received a request to get statistics with parameters:" +
                " start = {}," +
                " end = {}," +
                " uris = {}," +
                " unique = {}", start, end, uris, unique);

        return statsService.findHits(start, end, uris, unique);
    }
}
