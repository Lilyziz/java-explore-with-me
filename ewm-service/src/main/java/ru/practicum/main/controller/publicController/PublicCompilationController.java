package ru.practicum.main.controller.publicController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.service.pub.PublicCompilationService;

import java.util.List;

@Slf4j
@RequestMapping("/compilations")
@RestController
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    public PublicCompilationController(PublicCompilationService publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getCompilation(@RequestParam(required = false) Boolean pinned,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Get all compilations");
        return publicCompilationService.getAll(pinned, from, size);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getById(@PathVariable Long compId) {
        log.debug("Get compilation with id {}", compId);
        return publicCompilationService.getById(compId);
    }
}
