package ru.practicum.main.service.adminService;

import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequest;

@Service
public interface IAdminCompilationService {
    CompilationDto save(NewCompilationDto newCompilationDto);
    CompilationDto update(Long id, UpdateCompilationRequest newCompilationDto);
    void delete(Long id);
}
