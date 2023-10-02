package ru.practicum.main.service.admin_service;

import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CategoryDto;
import ru.practicum.main.dto.NewCategoryDto;

@Service
public interface IAdminCategoryService {
    CategoryDto save(NewCategoryDto newCategoryDto);

    CategoryDto update(Long id, NewCategoryDto newCategoryDto);

    void delete(Long id);
}
