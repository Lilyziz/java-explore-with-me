package ru.practicum.main.controller.adminController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CompilationDto;
import ru.practicum.main.dto.NewCompilationDto;
import ru.practicum.main.dto.UpdateCompilationRequest;
import ru.practicum.main.service.adminService.AdminCompilationService;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {
    private final AdminCompilationService adminCompilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        log.debug("Add compilation: {}", newCompilationDto);

        return adminCompilationService.save(newCompilationDto);
    }

    @PatchMapping("/{id}")
    public CompilationDto update(@PathVariable Long id,
                                 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
        log.debug("Update compilation with id {} with content: {}", id, updateCompilationRequest);

        return adminCompilationService.update(id, updateCompilationRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.debug("Delete compilation with id {}", id);

        adminCompilationService.delete(id);
    }
}
