package ru.practicum.main.service.admin;

import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;

public interface IAdminCategoryService {
    CategoryDto save(NewCategoryDto newCategoryDto);
    void delete(Long id);
    CategoryDto update(Long id, NewCategoryDto newCategoryDto);
}
