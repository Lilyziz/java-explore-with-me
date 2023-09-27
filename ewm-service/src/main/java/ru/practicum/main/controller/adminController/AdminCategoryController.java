package ru.practicum.main.controller.adminController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;
import ru.practicum.main.service.adminService.AdminCategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@Validated
@AllArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto save(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.debug("Add category: {}", newCategoryDto);
        return adminCategoryService.save(newCategoryDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.debug("Delete category with id {}", id);
        adminCategoryService.delete(id);
    }

    @PatchMapping("/{id}")
    public CategoryDto update(@PathVariable Long id,
                              @Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.debug("Update category with id {} with content: {}", id, newCategoryDto);
        return adminCategoryService.update(id, newCategoryDto);
    }
}
