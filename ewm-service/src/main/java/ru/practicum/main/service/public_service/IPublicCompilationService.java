package ru.practicum.main.service.public_service;

import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CompilationDto;

import java.util.List;

@Service
public interface IPublicCompilationService {
    CompilationDto getById(Long compId);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);
}
