package ru.practicum.main.service.public_service;

import org.springframework.stereotype.Service;
import ru.practicum.main.dto.CategoryDto;

import java.util.List;

@Service
public interface IPublicCategoryService {
    CategoryDto getById(Long catId);

    List<CategoryDto> getAll(Integer from, Integer size);
}
