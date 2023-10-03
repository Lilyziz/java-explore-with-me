package ru.practicum.main.controller.public_controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.service.public_service.PublicCategoryService;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class PublicCategoryController {
    private final PublicCategoryService publicCategoryService;

    @GetMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryDto getById(@PathVariable Long catId) {
        log.debug("Get category with id {}", catId);

        return publicCategoryService.getById(catId);
    }

    @GetMapping("/categories")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryDto> getAll(@RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        log.debug("Get all categories");

        return publicCategoryService.getAll(from, size);
    }
}
