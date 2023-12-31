package ru.practicum.main.service.public_service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.CompilationMapper;
import ru.practicum.main.dto.EventMapper;
import ru.practicum.main.dto.EventShortDto;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationService implements IPublicCompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public CompilationDto getById(Long compId) {

        Compilation compilations = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id " + compId + " was not found"));

        List<EventShortDto> eventShortDtos = compilations.getEvents().stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(compilations, eventShortDtos);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        Page<Compilation> compilations;
        if (pinned != null)
            compilations = compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size));
        else {
            compilations = compilationRepository.findAll(PageRequest.of(from / size, size));
        }

        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (Compilation comp : compilations) {
            List<EventShortDto> eventShortDtos = comp.getEvents().stream()
                    .map(EventMapper::toEventShortDto).collect(Collectors.toList());
            compilationDtos.add(CompilationMapper.toCompilationDto(comp, eventShortDtos));
        }

        return compilationDtos;
    }
}
