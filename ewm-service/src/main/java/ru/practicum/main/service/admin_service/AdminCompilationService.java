package ru.practicum.main.service.admin_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main.dto.*;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.model.Compilation;
import ru.practicum.main.model.Event;
import ru.practicum.main.repository.CompilationRepository;
import ru.practicum.main.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminCompilationService implements IAdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getEvents() == null) {
            newCompilationDto.setEvents(List.of(Long.MAX_VALUE));
        }

        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), eventShortDtos);
    }

    @Override
    @Transactional
    public CompilationDto update(Long id, UpdateCompilationRequest newCompilationDto) {
        Compilation compilation = compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id = " + id + " was not found"));

        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
            compilation.setEvents(events);
        }

        List<EventShortDto> eventShortDtos = events.stream().map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation), eventShortDtos);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        compilationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id = " + id + " was not found"));

        compilationRepository.deleteById(id);
    }
}
